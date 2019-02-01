package cn.bobdeng.userrole;

public interface PasswordEncoder {
    String encode(String rawPassword);

    boolean passwordRight(String rawPassword, String encodePassword);
}
