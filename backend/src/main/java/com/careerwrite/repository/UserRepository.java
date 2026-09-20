package com.careerwrite.repository;

import com.careerwrite.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Spring Data JPA generates the implementation of this interface at
// runtime. We get save(), findById(), findAll(), delete() etc for free,
// and we only need to declare the extra query methods we actually need.
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
