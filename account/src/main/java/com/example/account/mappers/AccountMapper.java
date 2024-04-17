package com.example.account.mappers;

import com.example.common.model.Account;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AccountMapper {

    @Insert("INSERT INTO accounts(customer_id, country) VALUES(#{customerId}, #{country})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertAccount(Account account);

    @Select("SELECT id, customer_id AS customerId, country FROM accounts WHERE customer_id = #{id}")
    Account findAccountByCustomerId(Long id);

}
