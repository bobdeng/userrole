package cn.bobdeng.userrole;

import cn.bobdeng.userrole.exception.*;
import lombok.extern.java.Log;

import java.util.Arrays;
import java.util.UUID;

@Log
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserConfig userConfig;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        userConfig = new UserConfig();
    }

    @Override
    public User checkLogin(String loginName, String password) {
        User user = userRepository.findByLoginName(loginName).orElseThrow(LoginNameNotfoundException::new);
        if (userRepository.isLocked(user)) {
            throw new TooFastRetryException();
        }
        if (passwordEncoder.passwordRight(password, user.getPassword())) {
            return user;
        } else {
            userRepository.lockUser(user, userConfig.getMinRetry());
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
        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .admin(true)
                .loginName(loginName)
                .password(passwordEncoder.encode(password))
                .roles(Arrays.asList("admin"))
                .human(true)
                .build();
        userRepository.newUser(user);
        return user;
    }

    @Override
    public User newUser(User user) {
        user.setId(UUID.randomUUID().toString());
        user.setAdmin(false);
        if (userRepository.findByLoginName(user.getLoginName()).isPresent()) {
            throw new DuplicateLoginName();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.newUser(user);
        return user;
    }
}
