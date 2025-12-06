package com.banking.dto;

import com.banking.constant.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto{
    private Long id;
    private Long accountId;
    private Double amount;
    private TransactionType type;
    private LocalDateTime localDateTime;
}
//public record TransactionDto(Long id,
//                             Long accountId,
//                             Double balance,
//                             TransactionType type,
//                             LocalDateTime localDateTime) {
//}
