package com.xiaojiang.financeaccount.server;

import com.xiaojiang.financeaccount.domain.*;
import com.xiaojiang.financeaccount.server.dao.AccountBillLogDAO;
import com.xiaojiang.financeaccount.server.dao.AccountDAO;
import com.xiaojiang.financeaccount.server.dao.AccountMobileDAO;
import com.xiaojiang.financeaccount.server.dao.AccountPayingOrderDAO;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
public class FinanceAccountRepository implements AccountRepository {
    @Autowired
    AccountDAO accountDAO;
    @Autowired
    AccountBillLogDAO accountBillLogDAO;
    @Autowired
    AccountMobileDAO accountMobileDAO;
    @Autowired
    RedissonClient redissonClient;
    @Autowired
    AccountPayingOrderDAO accountPayingOrderDAO;

    @Override
    public Optional<Account> findAccountByOpenId(String appCode, String openId) {
        AccountDO query = AccountDO.builder().openId(openId).appCode(appCode).build();
        return Optional.ofNullable(accountDAO.selectOne(query)).map(accountDO -> accountDO.toEntity(accountMobileDAO, accountPayingOrderDAO));
    }


    @Override
    public Account newAccount(Account account) {
        account.setId(UUID.randomUUID().toString());
        accountDAO.insert(AccountDO.from(account));
        Optional.ofNullable(account.getMobiles())
                .ifPresent(mobiles -> mobiles.stream()
                        .forEach(mobile -> this.addMobile(account, mobile)));
        return account;
    }

    private void addMobile(Account account, String mobile) {
        accountMobileDAO.insert(AccountMobileDO.builder()
                .accountId(account.getId())
                .mobile(mobile)
                .build());
    }

    @Override
    public void updateAccountPayingOrder(Account account) {
        AccountPayingOrderDO accountPayingOrderDO = accountPayingOrderDAO.selectById(account.getId());
        if (accountPayingOrderDO == null) {
            accountPayingOrderDAO.insert(AccountPayingOrderDO.from(account));
        } else {
            accountPayingOrderDAO.updateAllColumnById(AccountPayingOrderDO.from(account));
        }

    }

    @Override
    public void removePayingOrder(Account account) {
        accountPayingOrderDAO.deleteById(account.getId());
    }

    @Override
    public void updateAccountBalance(String id, BigDecimal balance) {
        accountDAO.updateAccountBalance(id, balance.intValue());
    }

    @Override
    public void addAccountBillLog(AccountBillLog accountBillLog) {
        accountBillLogDAO.insert(AccountBillLogDO.from(accountBillLog));
    }

    @Override
    public boolean tryGetWithdrawLock(String accountId) {
        return redissonClient.getBucket("account-withdraw-lock-" + accountId)
                .trySet(Long.valueOf(System.currentTimeMillis()), 1, TimeUnit.MINUTES);
    }

    @Override
    public boolean tryGetConfirmWithdrawLock(String accountId) {
        return redissonClient.getBucket("account-confirm-lock-" + accountId)
                .trySet(Long.valueOf(System.currentTimeMillis()), 1, TimeUnit.MINUTES);
    }

    @Override
    public WithdrawLimit getWithdrawLimit(String openId) {
        return WithdrawLimit.builder()
                .totalByUser(Optional.ofNullable(redissonClient.getAtomicLong(getBalanceKey(openId)).get()).map(Long::intValue).orElse(0))
                .total(Optional.ofNullable(redissonClient.getAtomicLong(getTotalBalanceKey()).get()).map(Long::intValue).orElse(0))
                .timesByUser(Optional.ofNullable(redissonClient.getAtomicLong(getTimesKey(openId)).get()).map(Long::intValue).orElse(0))
                .build();
    }

    @Override
    public void addPaying(String openId, int balance) {
        redissonClient.getAtomicLong(getBalanceKey(openId)).expire(24,TimeUnit.HOURS);
        redissonClient.getAtomicLong(getTimesKey(openId)).expire(24,TimeUnit.HOURS);
        redissonClient.getAtomicLong(getTotalBalanceKey()).expire(24,TimeUnit.HOURS);

        redissonClient.getAtomicLong(getBalanceKey(openId)).addAndGet(balance);
        redissonClient.getAtomicLong(getTimesKey(openId)).addAndGet(1);
        redissonClient.getAtomicLong(getTotalBalanceKey()).addAndGet(balance);
    }

    private String getBalanceKey(String openId) {
        return "account-paying-total-" + getTimeInDay() + openId;
    }

    private String getTotalBalanceKey() {
        return "account-paying-total-" + getTimeInDay();
    }

    private String getTimesKey(String openId) {
        return "account-paying-times-" + getTimeInDay() + openId;
    }

    private String getTimeInDay() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        simpleDateFormat.setTimeZone(Constant.timeZone);
        return simpleDateFormat.format(new Date());
    }

    @Override
    public <T> T tryLockWithdraw(Supplier<T> supplier) {
        RLock lock = redissonClient.getLock("finance-account-withdraw-lock");
        lock.lock(1, TimeUnit.MINUTES);
        try {
            return supplier.get();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public long getCurrentTime() {
        return System.currentTimeMillis();
    }

    @Override
    public Optional<AccountBillLog> findLogByOrder(String appCode, String orderId) {
        AccountBillLogDO query = AccountBillLogDO.builder().appCode(appCode).orderId(orderId).build();
        return Optional.ofNullable(accountBillLogDAO.selectOne(query)).map(AccountBillLogDO::toEntity);
    }

    @Override
    public boolean tryGetDepositLock(String orderId) {
        return redissonClient.getBucket("account-deposit-lock-" + orderId)
                .trySet(Long.valueOf(System.currentTimeMillis()), 5, TimeUnit.MINUTES);
    }

    @Override
    public void releaseDepositeLock(String orderId) {
        redissonClient.getBucket("account-deposit-lock-" + orderId).delete();
    }
}
