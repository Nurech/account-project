package com.example.account.mappers;

import com.example.account.model.Account;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AccountMapper {

    @Select("SELECT * FROM accounts WHERE id = #{id}")
    Account findAccountById(@Param("id") Long id);

    @Insert("INSERT INTO accounts(customer_id, country) VALUES(#{customerId}, #{country})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertAccount(Account account);

}
