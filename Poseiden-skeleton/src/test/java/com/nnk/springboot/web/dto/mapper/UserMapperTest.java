package com.nnk.springboot.web.dto.mapper;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.web.dto.UserDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WithMockUser(username = "duke", roles = { "USER", "ADMIN"})
class UserMapperTest {

    @Autowired
    public MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @DisplayName("User To UserDTO")
    @Order(1)
    @Test
    void fromUser() {
        //Given
        User user = new User("nono","passXXX","Mr Nono Bellami", "USER");

        //When
        UserDTO userDto = UserMapper.INSTANCE.fromUser(user);

        //THEN
        assertNotNull(userDto);
        assertEquals(userDto.getUsername(), user.getUsername());
        assertNull(userDto.getPassword());
        assertEquals(userDto.getFullname(), user.getFullname());
        assertEquals(userDto.getRole(), user.getRole());
    }

    @DisplayName("User To UserDTO: Null Case")
    @Order(2)
    @Test
    void fromUser_NullCase() {
        //Given
        User user = null;

        //When
        UserDTO userDto = UserMapper.INSTANCE.fromUser(user);

        //THEN
        assertNull(userDto);
    }

    @DisplayName("UserDTO To User")
    @Order(3)
    @Test
    void toUser() {
        //Given
        UserDTO userDTO = new UserDTO(1,"nono","passXXX","Mr Nono Bellami", "USER");

        //When
        User user = UserMapper.INSTANCE.toUser(userDTO);

        //THEN
        assertNotNull(user);
        assertEquals(user.getUsername(), userDTO.getUsername());
        assertEquals(user.getPassword(), userDTO.getPassword());
        assertEquals(user.getFullname(), userDTO.getFullname());
        assertEquals(user.getRole(), userDTO.getRole());
    }

    @DisplayName("UserDTO To User: Null Case")
    @Order(4)
    @Test
    void toUser_NullCase() {
        //Given
        UserDTO userDTO = null;

        //When
        User user = UserMapper.INSTANCE.toUser(userDTO);

        //THEN
        assertNull(user);
    }
}