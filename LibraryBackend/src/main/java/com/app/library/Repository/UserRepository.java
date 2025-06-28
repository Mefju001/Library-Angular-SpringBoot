package com.app.library.Repository;

import com.app.library.Entity.Role;
import com.app.library.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.rola = :roleName")
    List<User> findUsersByRole(@Param("roleName") String roleName);
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    long countUsersByRolesIs(Set<Role> role);

    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.rola = :roleName")
    Long countUsersByRoleName(@Param("roleName") String roleName);


}
