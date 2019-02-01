package cn.bobdeng.userrole;

import org.springframework.data.repository.CrudRepository;

public interface UserRoleDAO extends CrudRepository<UserDO,String> {
}
