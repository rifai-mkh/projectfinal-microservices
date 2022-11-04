package com.rapidtech.walletservice.repository;

import com.rapidtech.walletservice.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletRepo extends JpaRepository<Wallet,Long> {
    List<Wallet> findByuserNameIn(List<String> userName);

    Wallet findByUserName(String userName);
}
