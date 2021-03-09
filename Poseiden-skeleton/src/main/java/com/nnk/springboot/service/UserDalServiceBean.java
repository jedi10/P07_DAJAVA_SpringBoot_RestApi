package com.nnk.springboot.service;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.web.dto.UserDTO;
import com.nnk.springboot.web.dto.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserDalServiceBean implements IUserDalService {

    private UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    public UserDalServiceBean(UserRepository userRepository){
        super();
        this.userRepository = userRepository;
    }

    /**
     * <b>Spring Security Method for user registration</b>
     * @param user user Data Transfer Object
     * @return User user
     */
    @Override
    public User create(UserDTO user) {
        //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User userResult = UserMapper.INSTANCE.toUser(user);
        userResult.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(userResult);
    }

    /**
     * <b>Spring Security Method to log-in</b>
     * @param name name is used to log in
     * @return UserDetails
     * @throws UsernameNotFoundException spring security exception
     */
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(name);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(
                user.get().getUsername(),
                user.get().getPassword(),
                List.of(new SimpleGrantedAuthority("USER")));
    }


     @Override
     public Collection<UserDTO> findAll() {
         Collection<UserDTO> userDtoList = new ArrayList<>();
         userRepository.findAll().forEach(
                 user -> {
                     UserDTO userResult = UserMapper.INSTANCE.fromUser(user);
                     userDtoList.add(userResult);
                 }
         );
        return userDtoList;
     }

     @Override
     public UserDTO findOne(Integer id) {
        UserDTO userResult = null;
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        if (user != null){
            userResult = UserMapper.INSTANCE.fromUser(user);
        }
        return userResult;
     }

    /**
     * <b>Get a user without Spring Security</b>
     * @param name String
     * @return User if no user is find, return null
     */
     @Override
     public User findByName(String name) {
         User result = null;
         Optional<User> userOptional = userRepository.findByUsername(name);
         if (userOptional.isPresent()){
            result = userOptional.get();
         }
         return result;
     }

     @Override
     public User update(UserDTO userDto, Integer id) {
         User user = new User(userDto.getUsername(), userDto.getFullname(), userDto.getPassword(), userDto.getRole());
        if (id != null) {
            userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + user.getId()));
            user.setId(id);
        }
        //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        if (encodedPassword != null){
            user.setPassword(encodedPassword);
        } else {
           throw new IllegalArgumentException("Invalid user password");
        }
        return userRepository.save(user);
     }

     @Override
     public void delete(Integer id) {
         User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
         if(user != null){
             userRepository.delete(user);
         }
     }

     @Override
     public void deleteAll() {
        userRepository.deleteAll();
     }


}
