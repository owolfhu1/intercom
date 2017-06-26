package com.orion;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    boolean existsByUserName(String userName);
    User findOneByUserName(String userName);
}
