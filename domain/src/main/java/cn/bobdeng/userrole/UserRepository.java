package cn.bobdeng.userrole;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(String id);

    Optional<User> findByLoginName(String loginName);

    Optional<User> findByOpenId(String openId);

    void updateUserPassword(User user);

    void newUser(User user);

    Optional<User> findAdmin();

    void lockUser(User user, long minRetry);

    boolean isLocked(User user);

    List<User> searchByLoginName(String loginName);
}
