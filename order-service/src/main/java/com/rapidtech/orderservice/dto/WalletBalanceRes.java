package com.rapidtech.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalletBalanceRes {
    private String userName;
    private boolean isSaldoAvail;
}
