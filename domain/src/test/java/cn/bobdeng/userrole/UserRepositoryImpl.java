package cn.bobdeng.userrole;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    private Map<String,User> userMap=new HashMap<>();
    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public Optional<User> findByLoginName(String loginName) {
        return userMap.values()
                .stream()
                .filter(user -> user.getLoginName().equals(loginName))
                .findAny();
    }

    @Override
    public void updateUserPassword(User user) {

    }

    @Override
    public void newUser(User user) {
        userMap.put(user.getId(),user);
    }

    @Override
    public Optional<User> findAdmin() {
        return userMap.values()
                .stream()
                .filter(User::isAdmin)
                .findAny();
    }
}
