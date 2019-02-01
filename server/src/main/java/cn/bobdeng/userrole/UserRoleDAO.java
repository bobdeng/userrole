package cn.bobdeng.userrole;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRoleDAO extends CrudRepository<UserDO,String> {
    Optional<UserDO> findByLoginName(String loginName);

    Optional<UserDO> findByOpenId(String openId);

    Optional<UserDO> findAdmin();

    void updatePassword(String id, String password);
}
