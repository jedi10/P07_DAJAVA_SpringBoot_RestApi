package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
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
class TradeControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    private TradeRepository tradeRepository;

    @InjectMocks
    private TradeController tradeController;

    private final String rootURL = "/trade/";

    private static List<Trade> tradeListGiven = new ArrayList<>();

    static {
        Trade tradeGiven1 = new Trade("Trade Account", "Type");
        Trade tradeGiven2 = new Trade("Trade Account2", "Type2");
        tradeListGiven.add(tradeGiven1);
        tradeListGiven.add(tradeGiven2);
    }

    private Trade tradeCreated = new Trade("Trade Account3", "Type3");
    private Trade tradeUpdated = new Trade("Trade Account4", "Type4");


    @BeforeEach
    void setUp() {
        //***********GIVEN*************
        //          Mockito config
        when(tradeRepository.findAll()).thenReturn(tradeListGiven);
        when(tradeRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(tradeUpdated));
        when(tradeRepository.save(tradeCreated)).thenReturn(tradeCreated);
        when(tradeRepository.save(tradeUpdated)).thenReturn(tradeUpdated);
    }

    @AfterEach
    void tearDown() {
    }

    @Order(1)
    @Test
    void tradeController_MockMVC(){
        assertNotNull(mockMvc);
        assertNotNull(tradeController);
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
        verify(tradeRepository, Mockito.never()).findAll();

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Trade List")))
                .andExpect(model().attributeExists("trades"))
                .andExpect(model().attribute("trades", tradeListGiven))
                .andReturn();

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(tradeRepository, Mockito.times(1)).findAll();
    }

    @DisplayName("Show Add Form")
    @Order(3)
    @Test
    void addTradeForm() throws Exception {
        //***********GIVEN*************
        String uri = "add";
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get(rootURL+ uri)
                .accept(MediaType.TEXT_HTML_VALUE);

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Add New Trade")))
                .andReturn();
    }

    @DisplayName("Add - Validate - Ok")
    @Order(4)
    @Test
    void validate_validTrade() throws Exception {
        //***********GIVEN*************
        String uri =  "validate";
        String jsonGiven = convertJavaToJson(tradeCreated);
        String stringGiven = tradeCreated.toString();
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
        verify(tradeRepository, Mockito.never()).save(tradeCreated);

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                //.andExpect(redirectedUrl(rootURL + urlDestination))
                .andExpect(view().name("trade/list"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Trade List")))
                .andExpect(model().attributeExists("trades"))
                .andExpect(model().attribute("trades", tradeListGiven))
                .andReturn();

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(tradeRepository, Mockito.times(1)).save(any());
        verify(tradeRepository, Mockito.times(1)).save(ArgumentMatchers.refEq(tradeCreated));
    }

    @DisplayName("Add - Validate - Error")
    @Order(5)
    @Test
    void validate_errorTrade() throws Exception {
        //***********GIVEN*************
        String uri =  "validate";
        tradeCreated.setAccount(null);
        String jsonGiven = convertJavaToJson(tradeCreated);
        String stringGiven = tradeCreated.toString();
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
                .andExpect(view().name("trade/add"))
                .andReturn();
        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(tradeRepository, Mockito.never()).save(tradeCreated);
    }

    @Order(6)
    @ParameterizedTest
    @CsvSource({"1"})
    void showUpdateForm(String id) throws Exception {
        //***********GIVEN*************
        int tradeId = Integer.parseInt(id);
        tradeUpdated.setId(tradeId);
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
        verify(tradeRepository, Mockito.never()).findById(tradeId);

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Update")))
                .andExpect(model().attributeExists("trade"))
                .andExpect(model().attribute("trade", tradeUpdated))
                .andReturn();

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(tradeRepository, Mockito.times(1)).findById(tradeId);
    }

    @Order(7)
    @Test
    void showUpdateForm_error() {
        //***********GIVEN*************
        when(tradeRepository.findById(anyInt())).thenReturn(java.util.Optional.empty());
        int tradeIdGiven = 5;
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
        verify(tradeRepository, Mockito.never()).findById(tradeIdGiven);

        //**************WHEN-THEN****************************
        Exception exception = assertThrows(NestedServletException.class, () ->
                mockMvc.perform(builder))
                ;
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Invalid trade Id: there is no trade with Id:"));

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(tradeRepository, Mockito.times(1)).findById(tradeIdGiven);
    }

    @Order(8)
    @Test
    void updateTrade() throws Exception {
        //***********GIVEN*************
        tradeUpdated.setId(5);
        String urlTemplate = String.format("%s%s%s",
                rootURL,
                "update/",
                UriUtils.encode("5", StandardCharsets.UTF_8));
        String jsonGiven = convertJavaToJson(tradeUpdated);
        String stringGiven = tradeUpdated.toString();
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
        verify(tradeRepository, Mockito.never()).save(tradeUpdated);

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                //.andExpect(redirectedUrl(rootURL + urlDestination))
                .andExpect(view().name("trade/list"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Trade List")))
                .andExpect(model().attributeExists("trades"))
                .andExpect(model().attribute("trades", tradeListGiven))
                .andReturn();

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(tradeRepository, Mockito.times(1)).save(any());
        verify(tradeRepository, Mockito.times(1)).save(ArgumentMatchers.refEq(tradeUpdated));
    }

    @Order(9)
    @Test
    void deleteTrade() throws Exception {
        //***********GIVEN*************
        int tradeId = 5;
        tradeUpdated.setId(tradeId);
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
        verify(tradeRepository, Mockito.never()).findById(tradeId);

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Trade List")))
                .andExpect(model().attributeExists("trades"))
                .andExpect(model().attribute("trades", tradeListGiven))
                .andReturn();

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(tradeRepository, Mockito.times(1)).findById(tradeId);
        verify(tradeRepository, Mockito.times(1)).deleteById(tradeId);
    }

    @Order(10)
    @Test
    void deleteTrade_errorAccess() throws Exception {
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
        verify(tradeRepository, Mockito.never()).findById(any());
        verify(tradeRepository, Mockito.never()).deleteById(any());
    }
}