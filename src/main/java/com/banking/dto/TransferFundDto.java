package com.banking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferFundDto{
    private Long senderAccountId;
    private Long receiverAccountId;
    private Double amount;
}

//public record TransferFundDto(Long senderAccountId,
//                              Long receiverAccountId,
//                              Double balance) {
//}
