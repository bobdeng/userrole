package cn.bobdeng.userrole;

import cn.bobdeng.userrole.exception.LoginNameNotfoundException;
import cn.bobdeng.userrole.exception.OnlyOneAdminException;
import cn.bobdeng.userrole.exception.WrongPasswordException;
import lombok.extern.java.Log;

import java.util.Arrays;
import java.util.UUID;

@Log
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User checkLogin(String loginName, String password) {
        User user = userRepository.findByLoginName(loginName).orElseThrow(LoginNameNotfoundException::new);
        if (passwordEncoder.passwordRight(password, user.getPassword())) {
            return user;
        } else {
            throw new WrongPasswordException();
        }
    }

    @Override
    public void changePassword(String loginName, String password, String newPassword) {
        User user = checkLogin(loginName, password);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.updateUserPassword(user);
    }

    @Override
    public User initAdmin(String loginName, String password) {
        if (userRepository.findAdmin().isPresent()) {
            throw new OnlyOneAdminException();
        }
        User user=User.builder()
                .id(UUID.randomUUID().toString())
                .admin(true)
                .loginName(loginName)
                .password(passwordEncoder.encode(password))
                .roles(Arrays.asList("admin"))
                .build();
        userRepository.newUser(user);
        return user;
    }

    @Override
    public User newUser(User user) {
        user.setId(UUID.randomUUID().toString());
        user.setAdmin(false);
        userRepository.newUser(user);
        return user;
    }
}
