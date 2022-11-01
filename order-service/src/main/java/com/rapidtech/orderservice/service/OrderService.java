package com.rapidtech.orderservice.service;

import com.rapidtech.orderservice.dto.*;
import com.rapidtech.orderservice.model.Order;
import com.rapidtech.orderservice.model.OrderLineItems;
import com.rapidtech.orderservice.repository.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepo orderRepo;
    private final WebClient.Builder webClientBuilder;
    public void placeOrder(OrderReq orderReq){
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
            orderRepo.save(order);
        }else {
            throw new IllegalArgumentException("Stok Product tidak mencukupi....");
        }

        //cek saldo di wallet-service

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto){
        OrderLineItems orderLineItems = new OrderLineItems();
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
        Order orderOptional = orderRepo.findById(id).get();

        OrderResDto orders = OrderResDto.builder()
                    .id(orderOptional.getId())
                    .orderNumber(orderOptional.getOrderNumber())
                    .build();
        return orders;
    }


}
