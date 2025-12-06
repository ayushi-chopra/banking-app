package com.banking.service.impl;

import com.banking.constant.TransactionType;
import com.banking.dto.AccountDto;
import com.banking.dto.TransactionDto;
import com.banking.dto.TransferFundDto;
import com.banking.entity.Account;
import com.banking.entity.Transaction;
import com.banking.exception.ResourceNotFoundException;
import com.banking.repository.AccountRepository;
import com.banking.repository.TransactionRepository;
import com.banking.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    @Autowired
    public AccountServiceImpl (AccountRepository accountRepository,
                               TransactionRepository transactionRepository){
        this.accountRepository=accountRepository;
        this.transactionRepository=transactionRepository;
    }

    @Autowired
    private ModelMapper mapper;

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account= mapper.map(accountDto, Account.class);
        Account savedAccount= accountRepository.save(account);
        AccountDto accountDto1=mapper.map(savedAccount,AccountDto.class);
        Transaction transaction=new Transaction();
        transaction.setAccountId(savedAccount.getId());
        transaction.setAmount(accountDto1.getBalance());
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setLocalDateTime(LocalDateTime.now());
        transactionRepository.save(transaction);
        return accountDto1;
    }

    @Override
    public AccountDto getAccountById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Account not found with given id"));
        return mapper.map(account,AccountDto.class);
    }

    @Override
    public AccountDto deposit(long id, Double amount) {
        Account account = accountRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Account not found with given id"));
        Double currentBalance = Optional.ofNullable(account.getBalance()).orElse(0.0);
        Double total =currentBalance+amount;
        account.setBalance(total);
        Account savedAccount = accountRepository.save(account);
        Transaction transaction=new Transaction();
        transaction.setAccountId(id);
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setLocalDateTime(LocalDateTime.now());
        transactionRepository.save(transaction);
        return mapper.map(savedAccount,AccountDto.class);
    }

    @Override
    public AccountDto withdraw(Long id, Double amount) {
        Account account = accountRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Account not found with given id"));
        Double currentBalance = Optional.ofNullable(account.getBalance()).orElse(0.0);
        if(currentBalance<amount){
            throw new RuntimeException("Insufficient balance");
        }
        Double leftAmount=currentBalance-amount;
        account.setBalance(leftAmount);
        Account saved = accountRepository.save(account);
        Transaction transaction=new Transaction();
        transaction.setAccountId(id);
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.WITHDRAW);
        transaction.setLocalDateTime(LocalDateTime.now());
        transactionRepository.save(transaction);
        return mapper.map(saved,AccountDto.class);
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
//        List<AccountDto> accountDtos=new ArrayList<>();
//        for(Account account:accounts){
//            AccountDto accountDto = mapper.map(account, AccountDto.class);
//            accountDtos.add(accountDto);
//        }
        List<AccountDto> accountDtos =
                accounts.stream().
                        map(account -> mapper.map(account, AccountDto.class))
                        .collect(Collectors.toList());
        return accountDtos;
    }

    @Override
    public String deleteAccount(Long id) {
        Account account= accountRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Account not present with " +
                "given id"));
        accountRepository.delete(account);
        return "Account deleted successfully";
    }

    @Override
    public void transferFunds(TransferFundDto transferFundDto) {
        Account sender = accountRepository.findById(transferFundDto.getSenderAccountId()).
                orElseThrow(() -> new ResourceNotFoundException("senders Id does not exist"));
        Account reciever = accountRepository.findById(transferFundDto.getReceiverAccountId()).
                orElseThrow(()->new ResourceNotFoundException("Receivers id does not exists"));
        if(sender.getBalance()!=null && sender.getBalance()>transferFundDto.getAmount()){
            sender.setBalance(sender.getBalance()-transferFundDto.getAmount());
            accountRepository.save(sender);
            reciever.setBalance(reciever.getBalance()+transferFundDto.getAmount());
            accountRepository.save(reciever);
        }else{
            throw new RuntimeException("Insufficient balance to send");
        }
        Transaction transactionSent=new Transaction();
        transactionSent.setAccountId(sender.getId());
        transactionSent.setAmount(transferFundDto.getAmount());
        transactionSent.setTransactionType(TransactionType.TRANSFER_SENT);
        transactionSent.setLocalDateTime(LocalDateTime.now());
        transactionRepository.save(transactionSent);

        Transaction transactionReceived=new Transaction();
        transactionReceived.setAccountId(reciever.getId());
        transactionReceived.setAmount(transferFundDto.getAmount());
        transactionReceived.setTransactionType(TransactionType.TRANSFER_RECEIVED);
        transactionReceived.setLocalDateTime(LocalDateTime.now());
        transactionRepository.save(transactionReceived);
    }

    @Override
    public List<TransactionDto> getAllTransactions(Long accountId) {
        List<Transaction> transactions = transactionRepository.
                findByAccountIdOrderByLocalDateTimeDesc(accountId);
        List<TransactionDto> transactionDtos = transactions.stream().map(t -> mapper.map(t, TransactionDto.class)).collect(Collectors.toList());
        return transactionDtos;
    }

}
