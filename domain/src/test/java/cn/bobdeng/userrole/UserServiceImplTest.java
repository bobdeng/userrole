package cn.bobdeng.userrole;

import cn.bobdeng.userrole.exception.LoginNameNotfoundException;
import cn.bobdeng.userrole.exception.OnlyOneAdminException;
import cn.bobdeng.userrole.exception.TooFastRetryException;
import cn.bobdeng.userrole.exception.WrongPasswordException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class UserServiceImplTest {
    private UserServiceImpl userService;
    private UserRepositoryImpl userRepository;

    @Before
    public void setup() {
        userRepository = new UserRepositoryImpl();
        PasswordEncoder passwordEncoder = new PasswordEncoder() {
            @Override
            public String encode(String rawPassword) {
                return "encode:" + rawPassword;
            }

            @Override
            public boolean passwordRight(String rawPassword, String encodePassword) {
                return encodePassword.contains(rawPassword);
            }
        };
        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    public void checkLogin_admin() {
        User admin = userService.initAdmin("admin", "123455");
        User checkLogin = userService.checkLogin("admin", "123455");
        checkLogin = userService.checkLogin("admin", "123455");
        assertEquals(admin.getId(), checkLogin.getId());
        User user = userService.newUser(User.builder()
                .roles(Arrays.asList("user"))
                .password("123333")
                .loginName("bobdeng")
                .build());
        User userCheckLogin = userService.checkLogin("bobdeng", "123333");
        assertEquals(user.getId(), userCheckLogin.getId());
    }

    @Test(expected = WrongPasswordException.class)
    public void checkLogin_wrongPass() {
        User admin = userService.initAdmin("admin", "123455");
        User checkLogin = userService.checkLogin("admin", "123456");
    }
    @Test(expected = TooFastRetryException.class)
    public void checkLogin_retryTooFast() {
        User admin = userService.initAdmin("admin", "123455");
        try{
            userService.checkLogin("admin", "123456");
        }catch (Exception e){}
        userService.checkLogin("admin", "123456");
    }

    @Test(expected = LoginNameNotfoundException.class)
    public void checkLogin_not_found() {
        User checkLogin = userService.checkLogin("admin", "123456");
    }

    @Test(expected = OnlyOneAdminException.class)
    public void checkLogin_two_admin() {
        userService.initAdmin("admin", "123455");
        userService.initAdmin("admin", "123455");
    }

    @Test
    public void changePassword(){
        userService.initAdmin("admin", "123455");
        userService.changePassword("admin","123455","123456");
        userService.checkLogin("admin", "123456");
    }
    @Test(expected = WrongPasswordException.class)
    public void changePassword_wrongPass(){
        userService.initAdmin("admin", "123455");
        userService.changePassword("admin","1234551","123456");
        userService.checkLogin("admin", "123456");
    }
}