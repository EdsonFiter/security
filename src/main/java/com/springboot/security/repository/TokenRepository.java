package com.springboot.security.repository;

import com.springboot.security.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("""
        select t from Token t inner join User u on u.id = t.user.id
        where u.id = :userId and (t.revoked = false or t.expired = false)
    """)
    List<Token> findAllValidUserToken(Long userId);
    @Query("""
        select t from Token t inner join User u on u.id = t.user.id
        where u.id = :userId
        """)
    List<Token> findAllUserToken(Long userId);
    Optional<Token> findByToken(String token);
}
