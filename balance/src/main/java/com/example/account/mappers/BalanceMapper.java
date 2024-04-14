package com.example.account.mappers;

import com.example.common.model.Balance;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BalanceMapper {

    @Insert("INSERT INTO balances(account_id, currency, available_amount) VALUES(#{accountId}, #{currency}, #{availableAmount})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertBalance(Balance balance);

    @Update("UPDATE balances SET available_amount = #{availableAmount} WHERE account_id = #{accountId} AND currency = #{currency}")
    void updateBalance(Balance balance);

    @Select("SELECT * FROM balances WHERE account_id = #{accountId}")
    @Results({
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "availableAmount", column = "available_amount")
    })
    List<Balance> findBalancesByAccountId(Long accountId);

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "currency", column = "currency"),
            @Result(property = "availableAmount", column = "available_amount")
    })
    @Select("SELECT id, account_id, currency, available_amount FROM balances WHERE account_id = #{accountId} AND currency = #{currency}")
    Balance findBalanceByAccountIdAndCurrency(Long accountId, String currency);


}

