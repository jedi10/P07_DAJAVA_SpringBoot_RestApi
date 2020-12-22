package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.nnk.springboot.tools.Jackson.convertJavaToJson;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest()
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WithMockUser(username = "duke", roles = { "USER", "ADMIN"})
class BidListControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private BidListRepository bidListRepository;

    @InjectMocks
    private BidListController bidListController;

    private final String rootURL = "/bidList/";

    private static List<BidList> bidListGiven = new ArrayList<>();

    static {
        BidList bidList1 = new BidList("Account Test", "Type Test", 10d);
        BidList bidList2 = new BidList("Account Test2", "Type Test2", 20d);
        bidListGiven.add(bidList1);
        bidListGiven.add(bidList2);
    }

    private BidList bidListCreated = new BidList("Account Created", "Type Created", 30d);
    private BidList bidListUpdated = new BidList("Account Updated", "Type Updated", 40d);

    @BeforeEach
    void setUp() {
        //***********GIVEN*************
        //          Mockito config
        when(bidListRepository.findAll()).thenReturn(bidListGiven);
        when(bidListRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(bidListUpdated));
        when(bidListRepository.save(bidListCreated)).thenReturn(bidListCreated);
        when(bidListRepository.save(bidListUpdated)).thenReturn(bidListUpdated);

        //bidListController = new BidListController(bidListRepository);
    }

    @AfterEach
    void tearDown() {
    }

    @Order(1)
    @Test
    void bidListController_MockMVC(){
        assertNotNull(mockMvc);
        assertNotNull(bidListController);
    }

    @DisplayName("Home Url")
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
        verify(bidListRepository, Mockito.never()).findAll();

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/list"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Bid List")))
                .andExpect(model().attributeExists("bids"))
                .andExpect(model().attribute("bids", bidListGiven))
                .andReturn();

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(bidListRepository, Mockito.times(1)).findAll();

    }

    @DisplayName("Show Add Form")
    @Order(3)
    @Test
    void addBidForm() throws Exception {
        //***********GIVEN*************
        String uri = "add";
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get(rootURL+ uri)
                .accept(MediaType.TEXT_HTML_VALUE);

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Add New Bid")))
                .andReturn();
    }

    @DisplayName("Add - Validate - Ok")
    @Order(4)
    @Test
    void validate_validBid() throws Exception {
        //***********GIVEN*************
        String uri =  "validate";
        String jsonGiven = convertJavaToJson(bidListCreated);
        String stringGiven = bidListCreated.toString();
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
        verify(bidListRepository, Mockito.never()).save(bidListCreated);

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                //.andExpect(redirectedUrl(rootURL + urlDestination))
                .andExpect(view().name("bidList/list"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Bid List")))
                .andExpect(model().attributeExists("bids"))
                .andExpect(model().attribute("bids", bidListGiven))
                .andReturn();

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(bidListRepository, Mockito.times(1)).save(ArgumentMatchers.any());
        verify(bidListRepository, Mockito.times(1)).save(ArgumentMatchers.refEq(bidListCreated));
    }

    @DisplayName("Add - Validate - Error")
    @Order(5)
    @Test
    void validate_errorBid() throws Exception {
        //***********GIVEN*************
        String uri =  "validate";
        bidListCreated.setAccount(null);
        String jsonGiven = convertJavaToJson(bidListCreated);
        String stringGiven = bidListCreated.toString();
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
                .andExpect(view().name("bidList/add"))
                .andReturn();
        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(bidListRepository, Mockito.never()).save(bidListCreated);
    }

    @Order(6)
    @ParameterizedTest
    @CsvSource({"1"})
    void showUpdateForm(String id) throws Exception {
        //***********GIVEN*************
        int bidListId = Integer.parseInt(id);
        bidListUpdated.setId(bidListId);
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
        verify(bidListRepository, Mockito.never()).findById(bidListId);

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Update")))
                .andExpect(model().attributeExists("bid"))
                .andExpect(model().attribute("bid", bidListUpdated))
                .andReturn();

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(bidListRepository, Mockito.times(1)).findById(bidListId);
    }

    @Order(7)
    @Test
    void showUpdateForm_error() throws Exception {
        //***********GIVEN*************
        when(bidListRepository.findById(anyInt())).thenReturn(java.util.Optional.empty());
        int bidListIdGiven = 5;
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
        verify(bidListRepository, Mockito.never()).findById(bidListIdGiven);

        //**************WHEN-THEN****************************
        Exception exception = assertThrows(NestedServletException.class, () ->
                 mockMvc.perform(builder))
        ;
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Invalid bid Id: there is no bid with Id:"));

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(bidListRepository, Mockito.times(1)).findById(bidListIdGiven);
    }

    @Order(8)
    @Test
    void updateBid() throws Exception {
        //***********GIVEN*************
        bidListUpdated.setId(5);
        String urlTemplate = String.format("%s%s%s",
                rootURL,
                "update/",
                UriUtils.encode("5", StandardCharsets.UTF_8));
        String jsonGiven = convertJavaToJson(bidListUpdated);
        String stringGiven = bidListUpdated.toString();
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .put(urlTemplate)
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
        verify(bidListRepository, Mockito.never()).save(bidListUpdated);

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                //.andExpect(redirectedUrl(rootURL + urlDestination))
                .andExpect(view().name("bidList/list"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Bid List")))
                .andExpect(model().attributeExists("bids"))
                .andExpect(model().attribute("bids", bidListGiven))
                .andReturn();

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(bidListRepository, Mockito.times(1)).save(ArgumentMatchers.any());
        verify(bidListRepository, Mockito.times(1)).save(ArgumentMatchers.refEq(bidListUpdated));
    }

    @Order(9)
    @Test
    void deleteBid() throws Exception {
        //***********GIVEN*************
        int bidListId = 5;
        bidListUpdated.setId(bidListId);
        String urlTemplate = String.format("%s%s%s",
                rootURL,
                "delete/",
                UriUtils.encode("5", StandardCharsets.UTF_8));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .delete(urlTemplate)
                .with(SecurityMockMvcRequestPostProcessors.user("duke").roles("ADMIN"))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .accept(MediaType.TEXT_HTML_VALUE);
        //***********************************************************
        //**************CHECK MOCK INVOCATION at start***************
        //***********************************************************
        verify(bidListRepository, Mockito.never()).findById(bidListId);

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/list"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Bid List")))
                .andExpect(model().attributeExists("bids"))
                .andExpect(model().attribute("bids", bidListGiven))
                .andReturn();

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(bidListRepository, Mockito.times(1)).findById(bidListId);
        verify(bidListRepository, Mockito.times(1)).deleteById(bidListId);
    }
}



//Test Validation
//https://www.baeldung.com/spring-boot-bean-validation

//Authentication in Test
//https://www.youtube.com/watch?v=oLtXe1wgSC8
//https://rieckpil.de/guide-to-testing-spring-boot-applications-with-mockmvc/

//Test Thymeleaf
//https://www.youtube.com/watch?v=d7TDoGSZCoc

// https://stackoverflow.com/questions/24999469/how-to-unit-test-a-secured-controller-which-uses-thymeleaf-without-getting-temp
// Collection<GrantedAuthority> authorities = new HashSet<>();
// authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
// Authentication authToken = new UsernamePasswordAuthenticationToken("azeckoski", "password", authorities);
