package com.auction.portal.user.service;

import com.auction.portal.auction.service.AuctionService;
import com.auction.portal.bid.service.BidService;
import com.auction.portal.common.dto.UserLoginDto;
import com.auction.portal.user.dto.UserDto;
import com.auction.portal.user.entity.User;
import com.auction.portal.user.repository.UserRepository;
import com.auction.portal.utils.ErrorCodes;
import com.auction.portal.utils.JwtUtils;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private BidService bidService;

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = users.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
        userDtos.stream().map(user -> {
            user.setAuctions(auctionService.getAuctionsByUser(user.getId()));
            user.setBids(bidService.getAllBidsByUser(user.getId()));
            return user;
        }).collect(Collectors.toList());
        return userDtos;
    }

    @Override
    public UserDto getUserById(int id) {
        User user = userRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException(ErrorCodes.USER_NOT_FOUND));

        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setAuctions(auctionService.getAuctionsByUser(userDto.getId()));
        userDto.setBids(bidService.getAllBidsByUser(userDto.getId()));
        return userDto;
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        Boolean isValid = validateUserCredentials(userDto);
        if (!isValid) throw new IllegalArgumentException(ErrorCodes.EMAIL_INVALID);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encodedPassword);
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException(ErrorCodes.EMAIL_ALREADY_REGISTERED);
        }
        User user = modelMapper.map(userDto, User.class);
        User savedUser = userRepository.save(user);

        UserDto userDto1 = modelMapper.map(savedUser, UserDto.class);
        userDto1.setAuctions(auctionService.getAuctionsByUser(userDto1.getId()));
        userDto1.setBids(bidService.getAllBidsByUser(userDto1.getId()));
        return userDto1;

    }

    private Boolean validateUserCredentials(UserDto userDto) {
        String regexEmail = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        if (!Pattern.compile(regexEmail, Pattern.CASE_INSENSITIVE).matcher((userDto.getEmail())).find()) {
            throw new IllegalArgumentException(ErrorCodes.EMAIL_INVALID);
        }
        String password = userDto.getPassword();
        String regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
        if (!Pattern.compile(regexp).matcher(password).find()) {
            throw new IllegalArgumentException(ErrorCodes.PASSWORD_INVALID + "Minimum eight characters," +
                    " at least one uppercase letter, " +
                    "one lowercase letter" +
                    " and one number:");
        }
        return true;
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        userRepository.findById(userDto.getId()).
                orElseThrow(() -> new IllegalArgumentException(ErrorCodes.USER_NOT_FOUND));

        User user = modelMapper.map(userDto, User.class);
        User updatedUser = userRepository.save(user);
        UserDto userDto1 = modelMapper.map(updatedUser, UserDto.class);
        userDto1.setAuctions(auctionService.getAuctionsByUser(userDto1.getId()));
        userDto1.setBids(bidService.getAllBidsByUser(userDto1.getId()));
        return userDto1;
    }

    @Override
    public String deleteUserById(int id) {
        User user = userRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException(ErrorCodes.USER_NOT_FOUND));
        user.setDeletedAt(LocalDate.now());
        userRepository.saveAndFlush(user);
        return "User with ID : " + id + " has been Deleted Successfully";
    }

    @Override
    @SneakyThrows
    public String getToken(UserLoginDto userLoginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginDto.getEmail(),
                        userLoginDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenUtil.generateToken(authentication);
        return token;
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception(ErrorCodes.FORBIDDEN, e);
        } catch (BadCredentialsException e) {
            throw new Exception(ErrorCodes.BAD_CREDENTIALS, e);
        }
    }
}
