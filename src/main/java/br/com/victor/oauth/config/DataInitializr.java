package br.com.victor.oauth.config;


import br.com.victor.oauth.entity.Role;
import br.com.victor.oauth.entity.User;
import br.com.victor.oauth.repository.RoleRepository;
import br.com.victor.oauth.repository.UserRepository;
import br.com.victor.oauth.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializr implements ApplicationListener<ContextRefreshedEvent> {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializr(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent arg0) {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            createUser("Admin", "admin", passwordEncoder.encode("123456"), Constantes.ROLE_ADMIN);
            createUser("Cliente", "cliente", passwordEncoder.encode("123456"), Constantes.ROLE_CLIENT);
        }
    }

    public void createUser(String name, String email, String password, String roleName) {
        Role role = new Role(roleName);
        this.roleRepository.save(role);
        User user = new User(name, email, password, Arrays.asList(role));
        userRepository.save(user);
    }

}