package com.rapidtech.walletservice.service;

import com.rapidtech.walletservice.dto.WalletBalanceRes;
import com.rapidtech.walletservice.dto.WalletReq;
import com.rapidtech.walletservice.dto.WalletRes;
import com.rapidtech.walletservice.model.Wallet;
import com.rapidtech.walletservice.repository.WalletRepo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class WalletService {
    @Autowired
    private WalletRepo walletRepo;

    public void createWallet(WalletReq walletReq){
        Wallet wallet = Wallet.builder()
                .userName(walletReq.getUserName())
                .currentBalance(walletReq.getCurrentBalance())
                .build();
        walletRepo.save(wallet);
        log.info("Wallet {} is saved",wallet.getId());
    }

    public List<WalletRes> getAllWallets(){
        List<Wallet> wallets = walletRepo.findAll();
        return wallets.stream().map(this::mapToWalletRes).toList();
    }

    private WalletRes mapToWalletRes(Wallet wallet) {
        return WalletRes.builder()
                .id(wallet.getId())
                .userName(wallet.getUserName())
                .currentBalance(wallet.getCurrentBalance())
                .build();
    }

    public Wallet getById(Long id) {
        Wallet wallets = new Wallet();
        Wallet wallet = walletRepo.findById(id).orElse(new Wallet());
        wallets.setId(wallet.getId());
        wallets.setUserName(wallet.getUserName());
        wallets.setCurrentBalance(wallet.getCurrentBalance());
        return  wallets;
    }
    public WalletReq findByusername(String userName) {
        WalletReq walletRequest = new WalletReq();
        Wallet wallet = walletRepo.findByUserName(userName);
        walletRequest.setUserName(wallet.getUserName());
        walletRequest.setCurrentBalance(wallet.getCurrentBalance());
        return walletRequest;
    }

    public String topUp(Wallet wallet, Double jumlah) {
        Double updatedBalance = wallet.getCurrentBalance() + jumlah;
        wallet.setCurrentBalance(updatedBalance);
        walletRepo.save(wallet);
        return "UserName : " + wallet.getUserName() + " berhasil top up sebesar " + jumlah;
    }
    @SneakyThrows
    public List<WalletBalanceRes> isSaldoAvail(List<String> userName){
        //log.info("Mulai menunggu");
        //Thread.sleep(10000);
        log.info("Selesai menunggu");
        return walletRepo.findByuserNameIn(userName).stream()
                .map(wallet ->
                        WalletBalanceRes.builder()
                                .userName(wallet.getUserName())
                                .isSaldoAvail(wallet.getCurrentBalance()>0)
                                .build()).toList();
    }
}
