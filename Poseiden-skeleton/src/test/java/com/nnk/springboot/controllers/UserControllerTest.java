package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.service.UserDalServiceBean;
import com.nnk.springboot.web.dto.UserDTO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.nnk.springboot.tools.Jackson.convertJavaToJson;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WithMockUser(username = "duke", roles = { "USER", "ADMIN"})
class UserControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    private UserDalServiceBean userService;

    @InjectMocks
    private UserController userController;

    private final String rootURL = "/user/";

    private static List<User> userListGiven = new ArrayList<>();

    static {
        User userGiven1 = new User("toto", "XXX", "Aspic", "USER");
        User userGiven2 = new User("seaWolf", "XXX", "Dread", "ADMIN");
        userListGiven.add(userGiven1);
        userListGiven.add(userGiven2);
    }

    private UserDTO userCreatedDto = new UserDTO("titi", "XXX", "Inspector", "USER");
    private User userCreated = new User("titi", "XXX", "Inspector", "USER");
    private UserDTO userUpdatedDto = new UserDTO("tictac", "XXX", "Bread", "USER");
    private User userUpdated= new User("tictac", "XXX", "Bread", "USER");

    @BeforeEach
    void setUp() {
        //***********GIVEN*************
        //          Mockito config
        when(userService.findAll()).thenReturn(userListGiven);
        when(userService.findOne(anyInt())).thenReturn(userUpdated);//java.util.Optional.ofNullable(userUpdated)
        when(userService.create(userCreatedDto)).thenReturn(userCreated);
        when(userService.update(userUpdatedDto, 5)).thenReturn(userUpdated);
    }

    @AfterEach
    void tearDown() {
    }

    @Order(1)
    @Test
    void userController_MockMVC(){
        assertNotNull(mockMvc);
        assertNotNull(userController);
    }

    @Order(2)
    @Test
    void home() throws Exception {
        //***********GIVEN*************
        String uri = "list";
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get(rootURL+ uri)
                .accept(MediaType.TEXT_HTML_VALUE);

        //***********************************************************
        //**************CHECK MOCK INVOCATION at start***************
        //***********************************************************
        verify(userService, Mockito.never()).findAll();

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("User List")))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", userListGiven))
                .andReturn();

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(userService, Mockito.times(1)).findAll();
    }

    @DisplayName("Show Add Form")
    @Order(3)
    @Test
    void addUserForm() throws Exception {
        //***********GIVEN*************
        String uri = "add";
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get(rootURL+ uri)
                .accept(MediaType.TEXT_HTML_VALUE);

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Add New User")))
                .andReturn();
    }

    @Disabled
    @DisplayName("Add - Validate - Ok")
    @Order(4)
    @Test
    void validate_validUser() throws Exception {
        //***********GIVEN*************
        String uri =  "validate";
        String jsonGiven = convertJavaToJson(userCreated);
        String stringGiven = userCreated.toString();
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post(rootURL+ uri)
                .with(SecurityMockMvcRequestPostProcessors.user("duke").roles("USER", "ADMIN"))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonGiven)
                .accept(MediaType.TEXT_HTML_VALUE);
        //String urlDestination =  UriUtils.encode("list", "UTF-8");
        //***********************************************************
        //**************CHECK MOCK INVOCATION at start***************
        //***********************************************************
        verify(userService, Mockito.never()).create(userCreatedDto);

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                //.andExpect(redirectedUrl(rootURL + urlDestination))
                .andExpect(view().name("user/list"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("User List")))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", userListGiven))
                .andReturn();

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(userService, Mockito.times(1)).create(any());
        verify(userService, Mockito.times(1))
                .create(ArgumentMatchers.refEq(userCreatedDto, "password"));
        verify(userService, Mockito.times(1)).findAll();
    }

    @DisplayName("Add - Validate - Error")
    @Order(5)
    @Test
    void validate_error() throws Exception {
        //***********GIVEN*************
        String uri =  "validate";
        userCreatedDto.setUsername(null);
        String jsonGiven = convertJavaToJson(userCreatedDto);
        //String stringGiven = userCreatedDto.toString();
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post(rootURL + uri)
                .with(SecurityMockMvcRequestPostProcessors.user("duke").roles("USER", "ADMIN"))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonGiven);
        //String urlDestination =  UriUtils.encode("add", "UTF-8");

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())//.andExpect(MockMvcResultMatchers.status().isBadRequest())
                //.andExpect(redirectedUrl(rootURL + urlDestination))
                .andExpect(view().name("user/add"))
                .andReturn();
        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(userService, Mockito.never()).create(userCreatedDto);
        verify(userService, Mockito.never()).findAll();
    }

    @Order(6)
    @ParameterizedTest
    @CsvSource({"1"})
    void showUpdateForm(String id) throws Exception {
        //***********GIVEN*************
        int userId = Integer.parseInt(id);
        //userUpdated.setId(userId);
        String urlTemplate = String.format("%s%s%s",
                rootURL,
                "update/",
                UriUtils.encode(id, StandardCharsets.UTF_8));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get(urlTemplate)
                .accept(MediaType.TEXT_HTML_VALUE);
        //***********************************************************
        //**************CHECK MOCK INVOCATION at start***************
        //***********************************************************
        verify(userService, Mockito.never()).findOne(userId);

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Update")))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", userUpdated))
                .andReturn();

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(userService, Mockito.times(1)).findOne(userId);
    }

    @Order(7)
    @Test
    void showUpdateForm_error() {
        //***********GIVEN*************
        int userIdGiven = 5;
        when(userService.findOne(anyInt())).thenThrow(new IllegalArgumentException("Invalid user Id:" + userIdGiven));//.thenReturn(java.util.Optional.empty())

        String urlTemplate = String.format("%s%s%s",
                rootURL,
                "update/",
                UriUtils.encode("5", StandardCharsets.UTF_8));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get(urlTemplate)
                .accept(MediaType.TEXT_HTML_VALUE);
        //***********************************************************
        //**************CHECK MOCK INVOCATION at start***************
        //***********************************************************
        verify(userService, Mockito.never()).findOne(userIdGiven);

        //**************WHEN-THEN****************************
        Exception exception = assertThrows(NestedServletException.class, () ->
                mockMvc.perform(builder))
                ;
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Invalid user Id:"));
        //assertEquals("java.lang.NullPointerException", exception.getCause().toString());//When Mock return null

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(userService, Mockito.times(1)).findOne(userIdGiven);
    }

    @Disabled
    @Order(8)
    @Test
    void updateUser() throws Exception {
        //***********GIVEN*************
        userUpdated.setId(5);
        String urlTemplate = String.format("%s%s%s",
                rootURL,
                "update/",
                UriUtils.encode("5", StandardCharsets.UTF_8));
        String jsonGiven = convertJavaToJson(userUpdated);
        String stringGiven = userUpdated.toString();
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post(urlTemplate)
                .with(SecurityMockMvcRequestPostProcessors.user("duke").roles("USER", "ADMIN"))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonGiven)
                .accept(MediaType.TEXT_HTML_VALUE);
        //String urlDestination =  UriUtils.encode("list", "UTF-8");
        //***********************************************************
        //**************CHECK MOCK INVOCATION at start***************
        //***********************************************************
        verify(userService, Mockito.never()).update(userUpdatedDto, 5);

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                //.andExpect(redirectedUrl(rootURL + urlDestination))
                .andExpect(view().name("user/list"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("User List")))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", userListGiven))
                .andReturn();

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(userService, Mockito.times(1)).update(any(), any());
        verify(userService, Mockito.times(1))
                .update(ArgumentMatchers.refEq(userUpdatedDto, "password"), 5);
        verify(userService, Mockito.times(1)).findAll();
    }

    @Order(9)
    @Test
    void deleteUser() throws Exception {
        //***********GIVEN*************
        int userId = 5;
        userUpdated.setId(userId);
        String urlTemplate = String.format("%s%s%s",
                rootURL,
                "delete/",
                UriUtils.encode("5", StandardCharsets.UTF_8));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get(urlTemplate)
                .with(SecurityMockMvcRequestPostProcessors.user("duke").roles("ADMIN"))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .accept(MediaType.TEXT_HTML_VALUE);
        //***********************************************************
        //**************CHECK MOCK INVOCATION at start***************
        //***********************************************************
        verify(userService, Mockito.never()).findOne(userId);

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("User List")))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", userListGiven))
                .andReturn();

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(userService, Mockito.never()).findOne(userId);
        verify(userService, Mockito.times(1)).delete(userUpdated.getId());
    }

    @Order(10)
    @Test
    void deleteUser_errorAccess() throws Exception {
        String urlTemplate = String.format("%s%s%s",
                rootURL,
                "delete/",
                UriUtils.encode("5", StandardCharsets.UTF_8));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .delete(urlTemplate)
                .with(SecurityMockMvcRequestPostProcessors.user("charlie").roles("USER"))
                .characterEncoding("UTF-8")
                .accept(MediaType.TEXT_HTML_VALUE);

        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isForbidden())
                .andReturn();
        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(userService, Mockito.never()).findOne(any());
        verify(userService, Mockito.never()).delete(any());
    }
}