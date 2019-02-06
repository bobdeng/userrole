package cn.bobdeng.userrole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_userrole")
public class UserDO {
    @Id
    @Column(length = 40)
    private String id;
    @Column(length = 20)
    private String name;
    @Column(length = 20)
    private String loginName;
    @Column(length = 100)
    private String password;
    private boolean admin;
    @Column(length = 100)
    private String roles;
    @Column(length = 50)
    private String openId;
    @Column(length = 100)
    private String email;
    @Column(length = 20)
    private String mobile;

    public User toEntity() {
        return User.builder()
                .id(getId())
                .loginName(getLoginName())
                .password(getPassword())
                .admin(isAdmin())
                .roles(Optional.ofNullable(roles).map(roles -> Arrays.asList(roles.split(","))).orElse(Arrays.asList()))
                .email(UserEmail.builder().email(getEmail()).build())
                .weixin(UserWeixin.builder().openId(getOpenId()).build())
                .mobile(UserMobile.builder().mobile(getMobile()).build())
                .name(getName())
                .build();
    }

    public static UserDO fromEntity(User user) {
        return UserDO.builder()
                .id(user.getId())
                .loginName(user.getLoginName())
                .password(user.getPassword())
                .admin(user.isAdmin())
                .name(user.getName())
                .roles(Optional.ofNullable(user.getRoles()).map(list -> list.stream().collect(Collectors.joining(","))).orElse(null))
                .email(Optional.ofNullable(user.getEmail()).map(UserEmail::getEmail).orElse(null))
                .mobile(Optional.ofNullable(user.getMobile()).map(UserMobile::getMobile).orElse(null))
                .openId(Optional.ofNullable(user.getWeixin()).map(UserWeixin::getOpenId).orElse(null))
                .build();
    }

}
