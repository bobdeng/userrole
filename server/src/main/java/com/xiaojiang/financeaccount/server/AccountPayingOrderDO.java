package com.xiaojiang.financeaccount.server;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.xiaojiang.financeaccount.domain.Account;
import com.xiaojiang.financeaccount.domain.PayingOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "tb_finance_account_paying_order")
public class AccountPayingOrderDO {
    @TableId(type = IdType.INPUT)
    private String id;
    private String payingOrder;
    private Date createTime;
    private int balance;

    public static AccountPayingOrderDO from(Account account) {
        return AccountPayingOrderDO.builder()
                .balance(account.getBalance().intValue())
                .id(account.getId())
                .createTime(account.getPayingOrder().getCreateTime())
                .payingOrder(account.getPayingOrder().getPayingOder())
                .build();
    }

    public PayingOrder toEntity() {
        return PayingOrder.builder()
                .payingOder(getPayingOrder())
                .createTime(getCreateTime())
                .balance(getBalance())
                .build();

    }
}
