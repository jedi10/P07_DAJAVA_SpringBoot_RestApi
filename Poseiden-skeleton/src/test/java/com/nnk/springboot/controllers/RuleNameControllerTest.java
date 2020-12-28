package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
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
class RuleNameControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    private RuleNameRepository ruleNameRepository;

    @InjectMocks
    private RuleNameController ruleNameController;

    private final String rootURL = "/ruleName/";

    private static List<RuleName> ruleNameListGiven = new ArrayList<>();

    static {
        RuleName ruleGiven1 = new RuleName("Rule Name", "Description", "Json",
                "Template", "SQL", "SQL Part");
        RuleName ruleGiven2 = new RuleName("Rule Name2", "Description2", "Json2",
                "Template2", "SQL2", "SQL Part2");
        ruleNameListGiven.add(ruleGiven1);
        ruleNameListGiven.add(ruleGiven2);
    }

    private RuleName ruleNameCreated = new RuleName("Rule Name3", "Description3", "Json3",
            "Template3", "SQL3", "SQL Part3");

    private RuleName ruleNameUpdated = new RuleName("Rule Name4", "Description4", "Json4",
            "Template4", "SQL4", "SQL Part4");

    @BeforeEach
    void setUp() {
        //***********GIVEN*************
        //          Mockito config
        when(ruleNameRepository.findAll()).thenReturn(ruleNameListGiven);
        when(ruleNameRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(ruleNameUpdated));
        when(ruleNameRepository.save(ruleNameCreated)).thenReturn(ruleNameCreated);
        when(ruleNameRepository.save(ruleNameUpdated)).thenReturn(ruleNameUpdated);
    }

    @AfterEach
    void tearDown() {
    }

    @Order(1)
    @Test
    void ruleNameController_MockMVC(){
        assertNotNull(mockMvc);
        assertNotNull(ruleNameController);
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
        verify(ruleNameRepository, Mockito.never()).findAll();

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/list"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Rule List")))
                .andExpect(model().attributeExists("ruleNames"))
                .andExpect(model().attribute("ruleNames", ruleNameListGiven))
                .andReturn();

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(ruleNameRepository, Mockito.times(1)).findAll();
    }

    @DisplayName("Show Add Form")
    @Order(3)
    @Test
    void addRuleForm() throws Exception {
        //***********GIVEN*************
        String uri = "add";
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get(rootURL+ uri)
                .accept(MediaType.TEXT_HTML_VALUE);

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Add New Rule")))
                .andReturn();
    }

    @DisplayName("Add - Validate - Ok")
    @Order(4)
    @Test
    void validate_validRule() throws Exception {
        //***********GIVEN*************
        String uri =  "validate";
        String jsonGiven = convertJavaToJson(ruleNameCreated);
        String stringGiven = ruleNameCreated.toString();
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
        verify(ruleNameRepository, Mockito.never()).save(ruleNameCreated);

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                //.andExpect(redirectedUrl(rootURL + urlDestination))
                .andExpect(view().name("ruleName/list"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Rule List")))
                .andExpect(model().attributeExists("ruleNames"))
                .andExpect(model().attribute("ruleNames", ruleNameListGiven))
                .andReturn();

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(ruleNameRepository, Mockito.times(1)).save(ArgumentMatchers.any());
        verify(ruleNameRepository, Mockito.times(1)).save(ArgumentMatchers.refEq(ruleNameCreated));
    }

    @DisplayName("Add - Validate - Error")
    @Order(5)
    @Test
    void validate_errorRule() throws Exception {
        //***********GIVEN*************
        String uri =  "validate";
        ruleNameCreated.setName(null);
        String jsonGiven = convertJavaToJson(ruleNameCreated);
        String stringGiven = ruleNameCreated.toString();
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
                .andExpect(view().name("ruleName/add"))
                .andReturn();
        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(ruleNameRepository, Mockito.never()).save(ruleNameCreated);
    }

    @Order(6)
    @ParameterizedTest
    @CsvSource({"1"})
    void showUpdateForm(String id) throws Exception {
        //***********GIVEN*************
        int ruleNameId = Integer.parseInt(id);
        ruleNameUpdated.setId(ruleNameId);
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
        verify(ruleNameRepository, Mockito.never()).findById(ruleNameId);

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Update")))
                .andExpect(model().attributeExists("ruleName"))
                .andExpect(model().attribute("ruleName", ruleNameUpdated))
                .andReturn();

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(ruleNameRepository, Mockito.times(1)).findById(ruleNameId);
    }

    @Order(7)
    @Test
    void showUpdateForm_error() {
        //***********GIVEN*************
        when(ruleNameRepository.findById(anyInt())).thenReturn(java.util.Optional.empty());
        int curveIdGiven = 5;
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
        verify(ruleNameRepository, Mockito.never()).findById(curveIdGiven);

        //**************WHEN-THEN****************************
        Exception exception = assertThrows(NestedServletException.class, () ->
                mockMvc.perform(builder))
                ;
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Invalid ruleName Id: there is no ruleName with Id:"));

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(ruleNameRepository, Mockito.times(1)).findById(curveIdGiven);
    }

    @Order(8)
    @Test
    void updateCurve() throws Exception {
        //***********GIVEN*************
        ruleNameUpdated.setId(5);
        String urlTemplate = String.format("%s%s%s",
                rootURL,
                "update/",
                UriUtils.encode("5", StandardCharsets.UTF_8));
        String jsonGiven = convertJavaToJson(ruleNameUpdated);
        String stringGiven = ruleNameUpdated.toString();
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
        verify(ruleNameRepository, Mockito.never()).save(ruleNameUpdated);

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                //.andExpect(redirectedUrl(rootURL + urlDestination))
                .andExpect(view().name("ruleName/list"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Rule List")))
                .andExpect(model().attributeExists("ruleNames"))
                .andExpect(model().attribute("ruleNames", ruleNameListGiven))
                .andReturn();

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(ruleNameRepository, Mockito.times(1)).save(ArgumentMatchers.any());
        verify(ruleNameRepository, Mockito.times(1)).save(ArgumentMatchers.refEq(ruleNameUpdated));
    }

    @Order(9)
    @Test
    void deleteRuleName() throws Exception {
        //***********GIVEN*************
        int ruleNameId = 5;
        ruleNameUpdated.setId(ruleNameId);
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
        verify(ruleNameRepository, Mockito.never()).findById(ruleNameId);

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/list"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Rule List")))
                .andExpect(model().attributeExists("ruleNames"))
                .andExpect(model().attribute("ruleNames", ruleNameListGiven))
                .andReturn();

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(ruleNameRepository, Mockito.times(1)).findById(ruleNameId);
        verify(ruleNameRepository, Mockito.times(1)).deleteById(ruleNameId);
    }

    @Order(10)
    @Test
    void deleteRuleName_errorAccess() throws Exception {
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
        verify(ruleNameRepository, Mockito.never()).findById(any());
        verify(ruleNameRepository, Mockito.never()).deleteById(any());
    }
}