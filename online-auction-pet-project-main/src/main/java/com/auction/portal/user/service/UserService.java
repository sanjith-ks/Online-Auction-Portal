package com.auction.portal.user.service;

import com.auction.portal.common.dto.UserLoginDto;
import com.auction.portal.user.dto.UserDto;

import java.util.List;


public interface UserService {
    public List<UserDto> getAllUsers();

    public UserDto getUserById(int id);

    public UserDto saveUser(UserDto userDto);

    public UserDto updateUser(UserDto userDto);

    public String deleteUserById(int id);

    String getToken(UserLoginDto userLoginDto);
}




