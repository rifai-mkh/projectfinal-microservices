package com.rapidtech.walletservice.controller;

import com.rapidtech.walletservice.dto.WalletBalanceRes;
import com.rapidtech.walletservice.dto.WalletReq;
import com.rapidtech.walletservice.dto.WalletRes;
import com.rapidtech.walletservice.model.Wallet;
import com.rapidtech.walletservice.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;
    @GetMapping("/getAll")
    @ResponseStatus(HttpStatus.OK)
    public List<WalletRes> getAllWallets(){
        return walletService.getAllWallets();
    }


    @GetMapping("{userName}")
    @ResponseStatus(HttpStatus.OK)
    public WalletReq findByusername(@PathVariable("userName") String userName){
        return walletService.findByusername(userName);
    }

    @GetMapping("/getbyId/{id}")
    public Wallet getById(@PathVariable("id") Long id) {
        return walletService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody WalletReq walletReq){
        walletService.createWallet(walletReq);
    }

    @PutMapping("/topUp/{id}/jumlah/{jumlah}")
    public String topUp(@PathVariable("id") Long id, @PathVariable("jumlah") Double jumlah) {
        Wallet wallet = getById(id);
        return walletService.topUp(wallet, jumlah);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<WalletBalanceRes> isSaldoAvail(@RequestParam String userName) {
        return walletService.isSaldoAvail(userName);
    }
}
