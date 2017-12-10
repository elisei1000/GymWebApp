package com.gymwebapp.config;

import com.gymwebapp.domain.Administrator;
import com.gymwebapp.domain.Client;
import com.gymwebapp.domain.Coach;
import com.gymwebapp.domain.User;
import com.gymwebapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * @author foldv on 29.11.2017.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.get(username);


        if (user != null) {
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            if (user.getClass() == Coach.class)
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_COACH"));
            if(user.getClass()== Administrator.class)
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            if(user.getClass()==Client.class)
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_CLIENT"));
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
        }
        throw new UsernameNotFoundException("Could not find user with name '" + username + "'");

    }
}