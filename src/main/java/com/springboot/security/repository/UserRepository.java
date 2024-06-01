package com.springboot.security.repository;

import com.springboot.security.domain.Role;
import com.springboot.security.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Query("""
    select count(u.id) from User u where u.role = :role
    """)
    long countUserByRole(Role role);

    List<User> findUsersByRole(Role role);

    @Query("""
    select u from User u inner join Token t on t.user.id=u.id
    where (t.revoked = false and t.expired = false) and u.role=:userRole
    """)
    List<User> findAllUserWhoLoggedInByRole(Role userRole);

}
