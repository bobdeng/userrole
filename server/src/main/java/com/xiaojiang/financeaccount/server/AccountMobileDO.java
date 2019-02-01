package com.xiaojiang.financeaccount.server;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xiaojiang.financeaccount.domain.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "tb_finance_account_mobile")
public class AccountMobileDO {
    @TableId
    private Integer id;
    private String mobile;
    private String accountId;
}
