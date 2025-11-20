package com.app.library.Service;

import com.app.library.Entity.Role;
import com.app.library.Repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findRoleByName(String roleName){
        return roleRepository.findByRola(roleName).orElseThrow(EntityNotFoundException::new);
    }
}
