package cn.bobdeng.userrole;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface UserRoleDAO extends CrudRepository<UserDO, String> {
    Optional<UserDO> findByLoginName(String loginName);

    Optional<UserDO> findByOpenId(String openId);

    @Query("from UserDO where admin=1")
    Optional<UserDO> findAdmin();

    @Query("update UserDO set password=:password where id=:id")
    @Transactional
    @Modifying
    void updatePassword(@Param("id") String id, @Param("password") String password);

    Stream<UserDO> findTop20ByLoginNameLike(String loginName);
}
