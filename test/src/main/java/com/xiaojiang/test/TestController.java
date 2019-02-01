package com.xiaojiang.test;

import com.xiaojiang.financeaccount.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    public static final String OPEN_ID = "open_008";
    public static final String APP_CODE = "00001";
    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;
    @GetMapping("/test_deposit")
    @Transactional
    public void testDeposit(){
        Account account = accountService.getOrCreateAccountByOpenId(APP_CODE,OPEN_ID);
        accountService.deposit(account,DepositForm.builder()
                .amount(1000000)
                .appCode(APP_CODE)
                .comment("hello")
                .orderId("order_012")
                .build());
    }
    @GetMapping("/test_withdraw")
    @Transactional
    public void testWithdraw(@RequestParam("amount")int amount){
        Account account = accountService.getOrCreateAccountByOpenId(APP_CODE,OPEN_ID);
        Account account1 = accountService.getOrCreatePaying(account, amount*100);
        accountService.confirmPayingOrder(account1);
    }
}
