package com.example.common.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Currency implements Serializable {
    private Long id;
    private String code;
    private boolean isAllowed;
}
