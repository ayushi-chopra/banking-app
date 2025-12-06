package com.banking.dto;


public record TransferFundDto(Long senderAccountId,
                              Long receiverAccountId,
                              Double balance) {
}
