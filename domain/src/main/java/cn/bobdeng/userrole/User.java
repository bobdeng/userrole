package cn.bobdeng.userrole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;
    private String name;
    private String loginName;
    private String password;
    private boolean admin;
    private boolean human;
    private UserEmail email;
    private UserWeixin weixin;
    private UserMobile mobile;
    private List<String> roles;
}
