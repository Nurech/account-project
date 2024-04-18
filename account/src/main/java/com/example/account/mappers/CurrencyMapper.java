package com.example.account.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper

public interface CurrencyMapper {

    @Select("SELECT code FROM currencies WHERE is_allowed = TRUE")
    List<String> findAllowedCurrencies();

}
