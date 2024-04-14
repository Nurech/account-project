package com.example.account.mappers;

import com.example.common.model.Balance;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BalanceMapper {
    @Insert("INSERT INTO balances(account_id, currency, available_amount) VALUES(#{accountId}, #{currency}, #{availableAmount})")
    Balance insertBalance(Balance balance);

    @Update("UPDATE balances SET available_amount = #{availableAmount} WHERE account_id = #{accountId} AND currency = #{currency}")
    Balance updateBalance(Balance balance);

    @Select("SELECT * FROM balances WHERE account_id = #{accountId}")
    List<Balance> findBalancesByAccountId(Long accountId);

    @Select("SELECT code FROM currencies WHERE is_allowed = TRUE")
    List<String> findAllowedCurrencies();
}
