package com.rapidtech.orderservice.controller;

import com.rapidtech.orderservice.dto.OrderDto;
import com.rapidtech.orderservice.dto.OrderReq;
import com.rapidtech.orderservice.dto.OrderResDto;
import com.rapidtech.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderReq orderReq){
        orderService.placeOrder(orderReq);
        return "Produk berhasil dibeli.";
    }

    @GetMapping("/{id}")
    public OrderResDto getByOrderId(@PathVariable("id") Long id){
        return orderService.checkOut(id);
    }


    @GetMapping("/payment/{id}")
    public OrderResDto updateStudent(@PathVariable("id") Long id){
        return orderService.payment(id);
    }
}
