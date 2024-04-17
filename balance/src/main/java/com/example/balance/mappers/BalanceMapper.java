package com.example.balance.mappers;

import com.example.common.model.Balance;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BalanceMapper {

    @Insert("INSERT INTO balances (account_id, currency_id, available_amount) " +
            "VALUES (#{accountId}, (SELECT id FROM currencies WHERE code = #{currency}), #{availableAmount})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertBalance(Balance balance);

    @Update("UPDATE balances SET available_amount = #{availableAmount} " +
            "WHERE account_id = #{accountId} AND currency_id = (SELECT id FROM currencies WHERE code = #{currency})")
    void updateBalance(Balance balance);

    @Select("SELECT b.id, b.account_id, c.code as currency, b.available_amount " +
            "FROM balances b JOIN currencies c ON b.currency_id = c.id " +
            "WHERE b.account_id = #{accountId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "currency", column = "currency"),
            @Result(property = "availableAmount", column = "available_amount")
    })
    List<Balance> findBalancesByAccountId(Long accountId);


    @Select("SELECT b.id, b.account_id, c.code as currency, b.available_amount " +
            "FROM balances b JOIN currencies c ON b.currency_id = c.id " +
            "WHERE b.account_id = #{accountId} AND c.code = #{currency} AND c.is_allowed = true")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "currency", column = "currency"),
            @Result(property = "availableAmount", column = "available_amount")
    })
    Balance findBalanceByAccountIdAndCurrency(Long accountId, String currency);


}

