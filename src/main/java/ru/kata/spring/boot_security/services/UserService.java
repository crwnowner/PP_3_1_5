package ru.kata.spring.boot_security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.entities.Role;
import ru.kata.spring.boot_security.entities.User;
import ru.kata.spring.boot_security.repositories.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id).get();
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void updateUser(Long id, User user) {
        User userFromDb = userRepository.getById(id);
        userFromDb.setAge(user.getAge());
        userFromDb.setEmail(user.getEmail());
        userFromDb.setLastname(user.getLastname());
        userFromDb.setPassword(user.getPassword());
        userFromDb.setUsername(user.getUsername());
        userFromDb.setRoles((Set<Role>) user.getAuthorities());
        userRepository.save(userFromDb);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public boolean hasAdminRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if (authority.getAuthority().equals("ROLE_ADMIN")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found"));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getAuthorities());
    }
}
