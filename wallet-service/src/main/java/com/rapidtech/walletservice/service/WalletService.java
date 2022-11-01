package com.rapidtech.walletservice.service;

import com.rapidtech.walletservice.dto.WalletReq;
import com.rapidtech.walletservice.model.Wallet;
import com.rapidtech.walletservice.repository.WalletRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletService {
    private final WalletRepo walletRepo;

    public void createWallet(WalletReq walletReq){
        Wallet wallet = Wallet.builder()
                .userName(walletReq.getUserName())
                .currentBalance(walletReq.getCurrentBalance())
                .build();
        walletRepo.save(wallet);
        log.info("Wallet {} is saved",wallet.getId());
    }
}
