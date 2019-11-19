package com.aps.qrc.model;

import lombok.Data;

@Data
public class Merchant {

    private int qrVersion;

    private String qrType;

    private String merchantCompanyName;

    private String merchantCategory;

    private String bankAccount;


}
