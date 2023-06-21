package com.auction.portal.user.service;

import com.auction.portal.user.entity.User;
import com.auction.portal.user.entity.UserDetailsGroup;
import com.auction.portal.user.repository.UserRepository;
import com.auction.portal.utils.ErrorCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCodes.USER_NOT_FOUND));
        return new UserDetailsGroup(user);
    }
}
