package com.rapidtech.walletservice.controller;

import com.rapidtech.walletservice.dto.WalletReq;
import com.rapidtech.walletservice.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody WalletReq walletReq){
        walletService.createWallet(walletReq);
    }
}
