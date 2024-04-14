package com.example.common.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Account implements Serializable {
    private Long id;
    private Long customerId;
    private String country;
}
