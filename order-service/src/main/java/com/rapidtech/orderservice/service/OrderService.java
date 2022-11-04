package com.rapidtech.orderservice.service;

import com.rapidtech.orderservice.dto.*;
import com.rapidtech.orderservice.event.OrderPlacedEvent;
import com.rapidtech.orderservice.model.Order;
import com.rapidtech.orderservice.model.OrderLineItems;
import com.rapidtech.orderservice.repository.OrderLineItemsRepo;
import com.rapidtech.orderservice.repository.OrderRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepo orderRepo;
    private final WebClient.Builder webClientBuilder;

    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
    public String placeOrder(OrderReq orderReq){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems = orderReq.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto).toList();
        order.setOrderLineItemsList(orderLineItems);
        List<String> p = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getProductCode).toList();
        List<BigDecimal> q = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getPrice).toList();
        List<Integer> r = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getQuantity).toList();
        log.info("Memanggil product service");

        //cek di bagian product-service
        ProductStockRes[] productResponsesArr = webClientBuilder.build().get().uri("http://product-services/api/product",
                        uriBuilder -> uriBuilder.queryParam("productCode",p)
                                .queryParam("price",q)
                                .queryParam("quantity",r)
                                .build())
                .retrieve()
                .bodyToMono(ProductStockRes[].class)
                .block();

        boolean allProductsInStock =
                Arrays.stream(productResponsesArr).allMatch(ProductStockRes::isInStock);

        if(allProductsInStock){
            List<String> userNames = order.getOrderLineItemsList().stream()
                    .map(OrderLineItems::getUserName).toList();
            log.info("Memanggil Wallet service");
            //cek ballance di wallet
            WalletBalanceRes[] walletBalanceResArr = webClientBuilder.build().get().uri("http://wallet-service/api/wallet",
                            uriBuilder -> uriBuilder.queryParam("userName", userNames)
                                    .build())
                    .retrieve()
                    .bodyToMono(WalletBalanceRes[].class)
                    .block();
            boolean saldoAvailable = Arrays.stream(walletBalanceResArr).allMatch(WalletBalanceRes::isSaldoAvail);
            if(saldoAvailable){
                orderRepo.save(order);
                kafkaTemplate.send("notificationTopic",new OrderPlacedEvent(order.getOrderNumber()));
                return "Order berhasil dilakukan";
            }else {
                throw new IllegalArgumentException("Saldo Anda tidak cukup....");
            }

        }else {
            throw new IllegalArgumentException("Stok Product tidak mencukupi....");
        }

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto){
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setUserName(orderLineItemsDto.getUserName());
        orderLineItems.setProductCode(orderLineItemsDto.getProductCode());
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        return orderLineItems;
    }

    public OrderResDto checkOut(Long id){
        Order order = orderRepo.findById(id).get();
        return OrderResDto.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .build();
    }



    public OrderResDto payment(Long id){
        Order order = orderRepo.findById(id).get();
        return OrderResDto.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .build();

    }


}
