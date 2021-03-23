package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
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
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WithMockUser(username = "duke", roles = { "USER", "ADMIN"})
class RatingControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    private RatingRepository ratingRepository;

    @InjectMocks
    private RatingController ratingController;

    private final String rootURL = "/rating/";

    private static List<Rating> ratingListGiven = new ArrayList<>();

    static {
        Rating ratingGiven1 = new Rating("Moodys Rating", "Sand PRating",
                "Fitch Rating", 10);
        Rating ratingGiven2 = new Rating("ABC Rating", "Sand PRating 2",
                "Fitch Rating 2", 20);
        ratingListGiven.add(ratingGiven1);
        ratingListGiven.add(ratingGiven2);
    }

    private Rating ratingCreated = new Rating("Create Rating", "Sand PRating 3", "Fitch Rating 3", 30);
    private Rating ratingUpdated = new Rating("Update Rating", "Sand PRating 4", "Fitch Rating 4", 40);

    @BeforeEach
    void setUp() {
        //***********GIVEN*************
        //          Mockito config
        when(ratingRepository.findAll()).thenReturn(ratingListGiven);
        when(ratingRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(ratingUpdated));
        when(ratingRepository.save(ratingCreated)).thenReturn(ratingCreated);
        when(ratingRepository.save(ratingUpdated)).thenReturn(ratingUpdated);
    }

    @AfterEach
    void tearDown() {
    }

    @Order(1)
    @Test
    void ruleNameController_MockMVC(){
        assertNotNull(mockMvc);
        assertNotNull(ratingController);
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
        verify(ratingRepository, Mockito.never()).findAll();

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Rating List")))
                .andExpect(view().name("rating/list"))
                .andExpect(model().attributeExists("ratings"))
                .andExpect(model().attribute("ratings", ratingListGiven))
                .andReturn();

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(ratingRepository, Mockito.times(1)).findAll();
    }

    @DisplayName("Show Add Form")
    @Order(3)
    @Test
    void addRatingForm() throws Exception {
        //***********GIVEN*************
        String uri = "add";
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get(rootURL+ uri)
                .accept(MediaType.TEXT_HTML_VALUE);

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Add New Rating")))
                .andReturn();
    }

    @Disabled
    @DisplayName("Add - Validate - Ok")
    @Order(4)
    @Test
    void validate_validRating() throws Exception {
        //***********GIVEN*************
        String uri =  "validate";
        String jsonGiven = convertJavaToJson(ratingCreated);
        String stringGiven = ratingCreated.toString();
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
        verify(ratingRepository, Mockito.never()).save(ratingCreated);

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                //.andExpect(redirectedUrl(rootURL + urlDestination))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Rating List")))
                .andExpect(view().name("rating/list"))
                .andExpect(model().attributeExists("ratings"))
                .andExpect(model().attribute("ratings", ratingListGiven))
                .andReturn();

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(ratingRepository, Mockito.times(1)).save(ArgumentMatchers.any());
        verify(ratingRepository, Mockito.times(1)).save(ArgumentMatchers.refEq(ratingCreated));
    }

    @DisplayName("Add - Validate - Error")
    @Order(5)
    @Test
    void validate_errorRating() throws Exception {
        //***********GIVEN*************
        String uri =  "validate";
        ratingCreated.setOrderNumber(null);
        String jsonGiven = convertJavaToJson(ratingCreated);
        String stringGiven = ratingCreated.toString();
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post(rootURL + uri)
                .with(SecurityMockMvcRequestPostProcessors.user("duke").roles("USER", "ADMIN"))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonGiven);

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())//.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(view().name("rating/add"))
                .andReturn();
        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(ratingRepository, Mockito.never()).save(ratingCreated);
    }

    @Order(6)
    @ParameterizedTest
    @CsvSource({"1"})
    void showUpdateForm(String id) throws Exception {
        //***********GIVEN*************
        int ratingId = Integer.parseInt(id);
        ratingUpdated.setId(ratingId);
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
        verify(ratingRepository, Mockito.never()).findById(ratingId);

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Update")))
                .andExpect(model().attributeExists("rating"))
                .andExpect(model().attribute("rating", ratingUpdated))
                .andReturn();

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(ratingRepository, Mockito.times(1)).findById(ratingId);
    }

    @Order(7)
    @Test
    void showUpdateForm_error() {
        //***********GIVEN*************
        when(ratingRepository.findById(anyInt())).thenReturn(java.util.Optional.empty());
        int ratingIdGiven = 5;
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
        verify(ratingRepository, Mockito.never()).findById(ratingIdGiven);

        //**************WHEN-THEN****************************
        Exception exception = assertThrows(NestedServletException.class, () ->
                mockMvc.perform(builder))
                ;
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Invalid rating Id: there is no rating with Id:"));

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(ratingRepository, Mockito.times(1)).findById(ratingIdGiven);
    }

    @Disabled
    @Order(8)
    @Test
    void updateRating() throws Exception {
        //***********GIVEN*************
        ratingUpdated.setId(5);
        String urlTemplate = String.format("%s%s%s",
                rootURL,
                "update/",
                UriUtils.encode("5", StandardCharsets.UTF_8));
        String jsonGiven = convertJavaToJson(ratingUpdated);
        String stringGiven =ratingUpdated.toString();
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
        verify(ratingRepository, Mockito.never()).save(ratingUpdated);

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                //.andExpect(redirectedUrl(rootURL + urlDestination))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Rating List")))
                .andExpect(view().name("rating/list"))
                .andExpect(model().attributeExists("ratings"))
                .andExpect(model().attribute("ratings", ratingListGiven))
                .andReturn();

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(ratingRepository, Mockito.times(1)).save(ArgumentMatchers.any());
        verify(ratingRepository, Mockito.times(1)).save(ArgumentMatchers.refEq(ratingUpdated));
    }

    @DisplayName("Update - Validate - Error")
    @Order(9)
    @Test
    void update_errorRating() throws Exception {
        //***********GIVEN*************
        ratingCreated.setOrderNumber(null);
        String urlTemplate = String.format("%s%s%s",
                rootURL,
                "update/",
                UriUtils.encode("5", StandardCharsets.UTF_8));
        String jsonGiven = convertJavaToJson(ratingCreated);
        String stringGiven = ratingCreated.toString();
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post(urlTemplate)
                .with(SecurityMockMvcRequestPostProcessors.user("duke").roles("USER", "ADMIN"))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonGiven);

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())//.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(view().name("rating/update"))
                .andReturn();
        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(ratingRepository, Mockito.never()).save(ratingCreated);
    }

    @Order(10)
    @Test
    void deleteRating() throws Exception {
        //***********GIVEN*************
        int ratingId = 5;
        ratingUpdated.setId(ratingId);
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
        verify(ratingRepository, Mockito.never()).findById(ratingId);

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Rating List")))
                .andExpect(view().name("rating/list"))
                .andExpect(model().attributeExists("ratings"))
                .andExpect(model().attribute("ratings", ratingListGiven))
                .andReturn();

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(ratingRepository, Mockito.times(1)).findById(ratingId);
        verify(ratingRepository, Mockito.times(1)).deleteById(ratingId);
    }

    @Order(10)
    @Test
    void deleteRating_errorAccess() throws Exception {
        String urlTemplate = String.format("%s%s%s",
                rootURL,
                "delete/",
                UriUtils.encode("5", StandardCharsets.UTF_8));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .delete(urlTemplate)
                .with(SecurityMockMvcRequestPostProcessors.user("duke").roles("USER"))
                .characterEncoding("UTF-8")
                .accept(MediaType.TEXT_HTML_VALUE);

        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isForbidden())
                .andReturn();
        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(ratingRepository, Mockito.never()).findById(any());
        verify(ratingRepository, Mockito.never()).deleteById(any());
    }
}