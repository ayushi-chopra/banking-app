package com.banking.constant;

public enum TransactionType {
    DEPOSIT("DEP"),
    WITHDRAW("WT"),
    TRANSFER_SENT("TS"),
    TRANSFER_RECEIVED("TR");

    private final String code;
    public String getCode(){
        return code;
    }
    TransactionType(String code){
        this.code=code;
    }

}
