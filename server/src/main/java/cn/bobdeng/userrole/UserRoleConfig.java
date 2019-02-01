package cn.bobdeng.userrole;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserRoleConfig {
    @Bean
    UserService userService(UserRepository userRepository,PasswordEncoder passwordEncoder){
        return new UserServiceImpl(userRepository,passwordEncoder);
    }
}
