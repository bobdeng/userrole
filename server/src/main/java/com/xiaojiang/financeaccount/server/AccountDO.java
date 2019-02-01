package com.xiaojiang.financeaccount.server;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xiaojiang.financeaccount.domain.Account;
import com.xiaojiang.financeaccount.server.dao.AccountMobileDAO;
import com.xiaojiang.financeaccount.server.dao.AccountPayingOrderDAO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "tb_finance_account")
public class AccountDO {
    @TableId(type = IdType.INPUT)
    private String id;
    private String appCode;
    private Integer balance;
    private String openId;

    public static AccountDO from(Account account) {
        return AccountDO.builder()
                .openId(account.getOpenId())
                .balance(account.getBalance().intValue())
                .id(account.getId())
                .appCode(account.getAppCode())
                .build();
    }

    public Account toEntity(AccountMobileDAO accountMobileDAO, AccountPayingOrderDAO accountPayingOrderDAO) {
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("account_id", id);
        List<AccountMobileDO> mobileList = accountMobileDAO.selectList(entityWrapper);
        List<String> mobiles = Optional.ofNullable(mobileList)
                .map(list -> list.stream().map(AccountMobileDO::getMobile).collect(Collectors.toList()))
                .orElse(null);
        return Account.builder()
                .balance(BigDecimal.valueOf(getBalance()))
                .openId(getOpenId())
                .appCode(getAppCode())
                .payingOrder(Optional.ofNullable(accountPayingOrderDAO.selectById(getId())).map(AccountPayingOrderDO::toEntity).orElse(null))
                .mobiles(mobiles)
                .id(getId())
                .build();

    }
}
