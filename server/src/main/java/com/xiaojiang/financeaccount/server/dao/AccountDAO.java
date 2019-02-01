package com.xiaojiang.financeaccount.server.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xiaojiang.financeaccount.server.AccountDO;
import org.apache.ibatis.annotations.Param;

public interface AccountDAO extends BaseMapper<AccountDO> {
    void updateAccountBalance(@Param("id") String id,@Param("value") int value);
}
