package cn.bobdeng.userrole;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class UserRepositoryImpl implements UserRepository {
    @Autowired
    UserRoleDAO userRoleDAO;
    @Autowired
    RedissonClient redissonClient;

    @Override
    public Optional<User> findById(String id) {
        return userRoleDAO.findById(id).map(UserDO::toEntity);
    }

    @Override
    public Optional<User> findByLoginName(String loginName) {
        return userRoleDAO.findByLoginName(loginName).map(UserDO::toEntity);
    }

    @Override
    public Optional<User> findByOpenId(String openId) {
        return userRoleDAO.findByOpenId(openId).map(UserDO::toEntity);
    }

    @Override
    public void updateUserPassword(User user) {
        userRoleDAO.updatePassword(user.getId(), user.getPassword());
    }

    @Override
    public void newUser(User user) {
        userRoleDAO.save(UserDO.fromEntity(user));
    }

    @Override
    public Optional<User> findAdmin() {
        return userRoleDAO.findAdmin().map(UserDO::toEntity);
    }

    @Override
    public void lockUser(User user, long minRetry) {
        redissonClient.getBucket("user-login-lock-" + user.getId()).trySet("" + System.currentTimeMillis(), minRetry, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean isLocked(User user) {
        return redissonClient.getBucket("user-login-lock-" + user.getId()).get() != null;
    }

    @Override
    public List<User> searchByLoginName(String loginName) {
        return userRoleDAO.findTop20ByLoginNameLike(loginName.replace("%", "") + "%")
                .map(UserDO::toEntity)
                .collect(Collectors.toList());
    }
}
