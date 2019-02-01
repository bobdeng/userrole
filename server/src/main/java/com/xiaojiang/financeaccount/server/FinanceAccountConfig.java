package com.xiaojiang.financeaccount.server;

import com.xiaojiang.financeaccount.domain.AccountService;
import com.xiaojiang.financeaccount.domain.AccountServiceImpl;
import com.xiaojiang.financeaccount.domain.WithdrawConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.xiaojiang.financeaccount.server.dao")
public class FinanceAccountConfig {
    @Bean
    WithdrawConfig withdrawConfig(){
        return WithdrawConfig.builder()
                .from(6*3600)
                .end(18*3600)
                .maxPerUser(500000)
                .maxPerDay(10000000)
                .maxTimesPerUser(10)
                .build();
    }
    @Bean
    AccountService financeAccountService(FinanceAccountRepository financeAccountRepository, WithdrawConfig withdrawConfig) {
        AccountServiceImpl accountService = new AccountServiceImpl(financeAccountRepository);
        accountService.setWithdrawConfig(withdrawConfig);
        return accountService;
    }
}
