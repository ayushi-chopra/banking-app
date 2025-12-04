package com.banking.service;

import com.banking.dto.AccountDto;

import java.util.List;

public interface AccountService {

    AccountDto createAccount(AccountDto accountDto);
    AccountDto getAccountById(Long id);
    AccountDto deposit(long id,Double amount);
    AccountDto withdraw(Long id,Double amount);
    List<AccountDto> getAllAccounts();
    String deleteAccount(Long id);
}
