package com.rapidtech.orderservice.controller;

import com.rapidtech.orderservice.dto.OrderDto;
import com.rapidtech.orderservice.dto.OrderReq;
import com.rapidtech.orderservice.dto.OrderResDto;
import com.rapidtech.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory"/*,fallbackMethod = "fallbackMethod"*/)
    @TimeLimiter(name = "inventory")
    @Retry(name = "inventory")
    public CompletableFuture<String> placeOrder(@RequestBody OrderReq orderReq){
        return CompletableFuture.supplyAsync(()->orderService.placeOrder(orderReq));
    }

    public CompletableFuture<String> fallbackMethod(OrderReq orderReq, RuntimeException runtimeException){
        return CompletableFuture.supplyAsync(()->"Terjadi kesalahan, silahkan untuk order kembali beberapa saat lagi...");
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
