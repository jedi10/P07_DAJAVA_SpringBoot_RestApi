package com.nnk.springboot.service;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.web.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
        User userResult = new User(user.getUsername(),
                passwordEncoder.encode(user.getPassword()),
                user.getFullname(),
                user.getRole());
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
     public Collection<User> findAll() {
        return userRepository.findAll();
     }

     @Override
     public User findOne(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        return user;
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
     public User update(User user) {
        if (user != null) {
            userRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + user.getId()));
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
