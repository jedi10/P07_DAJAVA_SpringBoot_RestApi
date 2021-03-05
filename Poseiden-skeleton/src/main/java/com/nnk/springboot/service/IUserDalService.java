package com.nnk.springboot.service;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.web.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;

public interface IUserDalService extends UserDetailsService {

    User create(UserDTO user);
    Collection<User> findAll();
    User findOne(Integer id);
    User findByName(String name);
    User update(UserDTO user, Integer id);
    void delete(Integer id);
    void deleteAll();

}
