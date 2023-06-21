package com.auction.portal.user.controller;

import com.auction.portal.common.dto.UserLoginDto;
import com.auction.portal.user.dto.UserDto;
import com.auction.portal.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;


    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UserLoginDto userLoginDto) throws Exception {
        String token = userService.getToken(userLoginDto);
        return ResponseEntity.ok("Bearer "+token);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @PostMapping("/save")
    public UserDto saveUser(@RequestBody UserDto userDto) {

        return userService.saveUser(userDto);
    }

    @PutMapping("/update")
    public UserDto updateUser(@RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }

    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable int id) {
        return userService.deleteUserById(id);
    }




}
