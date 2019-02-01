package cn.bobdeng.userrole;

import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserRepositoryImpl implements UserRepository {
    @Override
    public Optional<User> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByLoginName(String loginName) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByOpenId(String openId) {
        return Optional.empty();
    }

    @Override
    public void updateUserPassword(User user) {

    }

    @Override
    public void newUser(User user) {

    }

    @Override
    public Optional<User> findAdmin() {
        return Optional.empty();
    }

    @Override
    public void lockUser(User user, long minRetry) {

    }

    @Override
    public boolean isLocked(User user) {
        return false;
    }
}
