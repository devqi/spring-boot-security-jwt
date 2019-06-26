package org.qizhang.playground.dao;

import org.qizhang.playground.models.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * This is interface that extends the Spring Framework class CrudRepository. 
 * CrudRepository class is a generic and takes the following two parameters as arguments:
 * User and id (Integer).
 * That's the only configuration required for the repository class. The required operation 
 * of inserting user details in database will now be handled.
 * 
 * @author Qi
 *
 */
@Repository
public interface UserDAO extends CrudRepository<UserEntity, Integer> {

	UserEntity findByUsername(String username); 
}
