package com.springsecurity.client.repositry;

import com.springsecurity.client.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByFirstName(String firstName);

    User findByEmail(String email);
}
