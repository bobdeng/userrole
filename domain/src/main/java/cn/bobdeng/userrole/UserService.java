package cn.bobdeng.userrole;

public interface UserService {
    User initAdmin(String loginName, String password);

    User newUser(User user);

    User checkLogin(String loginName, String password);

    void changePassword(String loginName, String password, String newPassword);
}
