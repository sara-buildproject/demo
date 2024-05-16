package com.buildsessions.stockmarketanalyzer.service;

import com.buildsessions.stockmarketanalyzer.entity.CustomUser;
import com.buildsessions.stockmarketanalyzer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<CustomUser> maybeUser = userRepository.findCustomUserByUsername(username);
        if (maybeUser.isPresent()) {
            CustomUser user = maybeUser.get();
            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(getRoles(user))
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    public CustomUser addUser(CustomUser user) {
        return userRepository.save(user);
    }

    private String[] getRoles(CustomUser user) {
        if(user.getRoles() == null) {
            return new String[]{"USER"};
        }
        return user.getRoles().split(",");
    }
}
