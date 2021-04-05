package com.nnk.springboot.service;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.web.dto.UserDTO;
import com.nnk.springboot.web.dto.mapper.UserMapper;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDalServiceBeanTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserDalServiceBean userService;

    private List<User> userListGiven = new ArrayList<>();
    private List<UserDTO> userDTOListGiven = new ArrayList<>();

    private UserDTO userCreatedDto;
    private User userCreated;
    private UserDTO userUpdatedDto;
    private User userUpdated;

    private String passwordEncodedGiven = "AZERTY10XSB";

    @BeforeAll
    void setUpAll() {
        //GIVEN
        User userGiven1 = new User("toto", "XXX", "Aspic", "USER");
        User userGiven2 = new User("seaWolf", "XXX", "Dread", "ADMIN");
        userListGiven.add(userGiven1);
        userListGiven.add(userGiven2);
        userDTOListGiven.add(UserMapper.INSTANCE.fromUser(userGiven1));
        userDTOListGiven.add(UserMapper.INSTANCE.fromUser(userGiven2));
        userCreated = new User("titi", "XXX", "Inspector", "USER");
        userUpdated = new User("tictac", "XXX", "Bread", "USER");
        userCreatedDto = UserMapper.INSTANCE.fromUser(userCreated);
        userUpdatedDto=  UserMapper.INSTANCE.fromUser(userUpdated);

        //Mockito config
        when(passwordEncoder.encode(anyString())).thenReturn(passwordEncodedGiven);

        when(userRepository.findAll()).thenReturn(userListGiven);
        when(userRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(userUpdated));
        when(userRepository.save(userCreated)).thenReturn(userCreated);
        when(userRepository.save(userUpdated)).thenReturn(userUpdated);
    }

    @AfterEach
    void tearDown() {
    }

    @DisplayName("Create")
    @Order(1)
    @Test
    void create() {
        //WHEN
        User userResult = userService.create(userCreatedDto);

        //THEN
        assertNotNull(userResult);
        assertEquals(userCreated, userResult);
        verify(userRepository, Mockito.times(1)).save(userCreated);
    }

    @DisplayName("Load User by UserName (Spring Security)")
    @Order(2)
    @Test
    void loadUserByUsername() {
        //GIVEN
        when(userRepository.findByUsername(anyString())).thenReturn(java.util.Optional.ofNullable(userCreated));

        //WHEN
        UserDetails userResult = userService.loadUserByUsername(userCreated.getUsername());

        //THEN
        assertNotNull(userResult);
        assertEquals(userCreated.getUsername(), userResult.getUsername());
        verify(userRepository, Mockito.times(1)).findByUsername(userCreated.getUsername());
    }

    @DisplayName("Load by UserName Exception: User Not Found (Spring Security)")
    @Order(3)
    @Test
    void loadUserByUsername_Exception() {
        //GIVEN
        when(userRepository.findByUsername(anyString())).thenReturn(java.util.Optional.empty());

        //WHEN
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(userCreated.getUsername());
        });

        //THEN
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Invalid username or password"));
        verify(userRepository, Mockito.times(1)).findByUsername(userCreated.getUsername());
    }

    @DisplayName("Load User by UserName")
    @Order(4)
    @Test
    void findByName() {
        //Given
        when(userRepository.findByUsername(anyString())).thenReturn(java.util.Optional.ofNullable(userCreated));

        //WHEN
        User userResult = userService.findByName(userCreated.getUsername());

        //THEN
        assertNotNull(userResult);
        assertEquals(userCreated.getUsername(), userResult.getUsername());
        verify(userRepository, Mockito.times(1)).findByUsername(userCreated.getUsername());
    }

    @DisplayName("Find All User")
    @Order(5)
    @Test
    void findAll() {
        //GIVEN
        when(userRepository.findAll()).thenReturn(userListGiven);

        //WHEN
        Collection<UserDTO> userListResult = userService.findAll();

        //THEN
        assertNotNull(userListResult);
        assertEquals(userDTOListGiven, userListResult);
        verify(userRepository, Mockito.times(1)).findAll();
    }

    @DisplayName("Find One User")
    @Order(6)
    @Test
    void findOne() {
        //GIVEN
        when(userRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(userUpdated));

        //WHEN
        UserDTO userResult = userService.findOne(5);

        //THEN
        assertNotNull(userResult);
        assertEquals(userUpdatedDto, userResult);
        verify(userRepository, Mockito.times(1)).findById(5);
    }

    @DisplayName("Find One User: Exception Not Found")
    @Order(7)
    @Test
    void findOne_Exception() {
        //GIVEN
        User userGiven = userListGiven.get(0);
        userGiven.setId(1);
        when(userRepository.findById(userGiven.getId())).thenReturn(java.util.Optional.empty());

        //WHEN
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                userService.findOne(userGiven.getId());
        });

        //THEN
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Invalid user Id"));
    }


    @DisplayName("Update User")
    @Order(8)
    @Test
    void update() {
        //Given
        UserDTO userGiven = userUpdatedDto;
        userGiven.setPassword(userUpdated.getPassword());
        when(userRepository.save(userUpdated)).thenReturn(userUpdated);
        when(userRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(userUpdated));
        when(passwordEncoder.encode(anyString())).thenReturn(passwordEncodedGiven);

        //WHEN
        User userResult = userService.update(userGiven, 1);

        //THEN
        assertNotNull(userResult);
        assertEquals(userUpdated, userResult);
        verify(userRepository, Mockito.times(1)).save(userUpdated);
        verify(passwordEncoder, Mockito.times(1)).encode(userUpdated.getPassword());
        assertEquals(userUpdated.getPassword(), userResult.getPassword());
    }

    @DisplayName("Update: Exception User Password Null")
    @Order(9)
    @Test
    void update_exception_passwordNull() {
        //Given
        UserDTO userGiven = userUpdatedDto;
        userGiven.setPassword(null);
        when(userRepository.save(userUpdated)).thenReturn(userUpdated);
        when(userRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(userUpdated));
        when(passwordEncoder.encode(anyString())).thenReturn(passwordEncodedGiven);

        //WHEN
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.update(userGiven, 1);
        });

        //THEN
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Invalid user password"));
        verify(passwordEncoder, Mockito.times(1)).encode(null);
        verify(passwordEncoder, Mockito.never()).encode(anyString());
    }

    @DisplayName("Update: Exception User Not Found")
    @Order(10)
    @Test
    void update_exception_userNotFound() {
        //Given
        User userGiven = userUpdated;
        userGiven.setId(1);
        when(userRepository.findById(userGiven.getId())).thenReturn(java.util.Optional.empty());

        //WHEN
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.update(userUpdatedDto, 1);
        });

        //THEN
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Invalid user Id:"));
    }

    @DisplayName("Delete")
    @Order(11)
    @Test
    void delete() {
        //GIVEN
        when(userRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(userUpdated));

        //WHEN
        userService.delete(1);

        //THEN
        verify(userRepository, Mockito.times(1)).delete(userUpdated);
        verify(userRepository, Mockito.times(1)).findById(1);
    }

    @DisplayName("Delete: Exception User Not Found")
    @Order(12)
    @Test
    void delete_exception() {
        //GIVEN
        when(userRepository.findById(anyInt())).thenReturn(java.util.Optional.empty());

        //WHEN
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.delete(1);
        });

        //THEN
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Invalid user Id:"));
        verify(userRepository, Mockito.never()).delete(userUpdated);
        verify(userRepository, Mockito.times(1)).findById(1);
    }

    @DisplayName("Delete All User")
    @Order(13)
    @Test
    void deleteAll() {
        //WHEN
        userService.deleteAll();

        //THEN
        verify(userRepository, Mockito.times(1)).deleteAll();
    }
}