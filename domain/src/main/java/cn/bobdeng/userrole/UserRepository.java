package cn.bobdeng.userrole;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(String id);

    Optional<User> findByLoginName(String loginName);

    void updateUserPassword(User user);

    void newUser(User user);

    Optional<User> findAdmin();

    void lockUser(User user, long minRetry);

    boolean isLocked(User user);
}
