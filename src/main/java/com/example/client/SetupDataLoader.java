package com.example.client;

import com.example.client.model.Role;
import com.example.client.model.Roles;
import com.example.client.repo.PrivilegeRepository;
import com.example.client.repo.RoleRepository;
import com.example.client.repo.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SetupDataLoader
    implements ApplicationListener<ContextRefreshedEvent>{
    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Transactional
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
//        Role anon  = new Role(Roles.ANONYMOUS.name());
//        roleRepository.save(anon);
//        Role user  = new Role(Roles.USER.name());
//        roleRepository.save(user);

    }
}
