package com.example.account.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Account implements Serializable {
    private String id;
    private String customerId;
    private String country;
}
