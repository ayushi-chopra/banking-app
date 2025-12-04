package com.banking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountDto {

    private Long id;
    private String accountHolderName;
    private Double balance;
}


//record does not have no args constructor and model mapper requires no args constructor
//to instantiate so record will give error with model mapper
//if you want to use record then create mapper class that converts models to dto class
//public record AccountDto(Long id,
//                         String accountHolderName,
//                         Double balance) {
//}
