package com.xiaojiang.financeaccount.server;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xiaojiang.financeaccount.domain.AccountBillLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "tb_account_bill_log")
public class AccountBillLogDO {
    @TableId
    private long id;
    private String appCode;
    private String accountId;
    private int balance;
    private String orderId;
    private Date createTime;
    private String comment;

    public static AccountBillLogDO from(AccountBillLog accountBillLog) {
        return AccountBillLogDO.builder()
                .id(accountBillLog.getId())
                .orderId(accountBillLog.getOrderId())
                .appCode(accountBillLog.getAppCode())
                .balance(accountBillLog.getBalance().intValue())
                .createTime(accountBillLog.getCreateTime())
                .comment(accountBillLog.getComment())
                .accountId(accountBillLog.getAccountId())
                .build();
    }

    public AccountBillLog toEntity() {
        return AccountBillLog.builder()
                .orderId(getOrderId())
                .balance(BigDecimal.valueOf(getBalance()))
                .comment(getComment())
                .appCode(getAppCode())
                .createTime(getCreateTime())
                .accountId(getAccountId())
                .build();
    }
}
