package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WithMockUser(username = "duke", roles = { "USER", "ADMIN"})
class CurveControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    private CurvePointRepository curvePointRepository;

    @InjectMocks
    private CurveController curvePointController;

    private final String rootURL = "/curvePoint/";

    private static List<CurvePoint> curveListGiven = new ArrayList<>();

    static {
        CurvePoint curvePointGiven1 = new CurvePoint(1, 10d, 20d);
        CurvePoint curvePointGiven2 = new CurvePoint(2, 20d, 40d);
        curveListGiven.add(curvePointGiven1);
        curveListGiven.add(curvePointGiven2);
    }

    private CurvePoint curvePointCreated = new CurvePoint(3, 30d, 60d);
    private CurvePoint curvePointUpdated = new CurvePoint(4, 40d, 90d);

    @BeforeEach
    void setUp() {
        //***********GIVEN*************
        //          Mockito config
        when(curvePointRepository.findAll()).thenReturn(curveListGiven);
        when(curvePointRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(curvePointUpdated));
        when(curvePointRepository.save(curvePointCreated)).thenReturn(curvePointCreated);
        when(curvePointRepository.save(curvePointUpdated)).thenReturn(curvePointUpdated);
    }

    @AfterEach
    void tearDown() {
    }

    @Order(1)
    @Test
    void curveController_MockMVC(){
        assertNotNull(mockMvc);
        assertNotNull(curvePointController);
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
        verify(curvePointRepository, Mockito.never()).findAll();

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/list"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Curve Point List")))
                .andReturn();

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(curvePointRepository, Mockito.times(1)).findAll();
    }

    @DisplayName("Show Add Form")
    @Order(3)
    @Test
    void addCurveForm() throws Exception {
        //***********GIVEN*************
        String uri = "add";
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get(rootURL+ uri)
                .accept(MediaType.TEXT_HTML_VALUE);

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Add New Curve Point")))
                .andReturn();
    }

    @DisplayName("Add - Validate - Ok")
    @Order(4)
    @Test
    void validate_validCurve() throws Exception {
        //***********GIVEN*************
        String uri =  "validate";
        String jsonGiven = convertJavaToJson(curvePointCreated);
        String stringGiven = curvePointCreated.toString();
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
        verify(curvePointRepository, Mockito.never()).save(curvePointCreated);

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                //.andExpect(redirectedUrl(rootURL + urlDestination))
                .andExpect(view().name("curvePoint/list"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Curve Point List")))
                .andExpect(model().attributeExists("curvePoints"))
                .andExpect(model().attribute("curvePoints", curveListGiven))
                .andReturn();

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(curvePointRepository, Mockito.times(1)).save(ArgumentMatchers.any());
        verify(curvePointRepository, Mockito.times(1)).save(ArgumentMatchers.refEq(curvePointCreated));
    }

    @DisplayName("Add - Validate - Error")
    @Order(5)
    @Test
    void validate_errorCurve() throws Exception {
        //***********GIVEN*************
        String uri =  "validate";
        curvePointCreated.setCurveId(null);
        String jsonGiven = convertJavaToJson(curvePointCreated);
        String stringGiven = curvePointCreated.toString();
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
                .andExpect(view().name("curvePoint/add"))
                .andReturn();
        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(curvePointRepository, Mockito.never()).save(curvePointCreated);
    }

    @Order(6)
    @ParameterizedTest
    @CsvSource({"1"})
    void showUpdateForm(String id) throws Exception {
        //***********GIVEN*************
        int curvePointId = Integer.parseInt(id);
        curvePointUpdated.setId(curvePointId);
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
        verify(curvePointRepository, Mockito.never()).findById(curvePointId);

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Update")))
                .andExpect(model().attributeExists("curvePoint"))
                .andExpect(model().attribute("curvePoint", curvePointUpdated))
                .andReturn();

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(curvePointRepository, Mockito.times(1)).findById(curvePointId);
    }

    @Order(7)
    @Test
    void showUpdateForm_error() {
        //***********GIVEN*************
        when(curvePointRepository.findById(anyInt())).thenReturn(java.util.Optional.empty());
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
        verify(curvePointRepository, Mockito.never()).findById(curveIdGiven);

        //**************WHEN-THEN****************************
        Exception exception = assertThrows(NestedServletException.class, () ->
                mockMvc.perform(builder))
                ;
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Invalid curve Id: there is no curve with Id:"));

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(curvePointRepository, Mockito.times(1)).findById(curveIdGiven);
    }

    @Order(8)
    @Test
    void updateCurve() throws Exception {
        //***********GIVEN*************
        curvePointUpdated.setId(5);
        String urlTemplate = String.format("%s%s%s",
                rootURL,
                "update/",
                UriUtils.encode("5", StandardCharsets.UTF_8));
        String jsonGiven = convertJavaToJson(curvePointUpdated);
        String stringGiven = curvePointUpdated.toString();
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
        verify(curvePointRepository, Mockito.never()).save(curvePointUpdated);

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                //.andExpect(redirectedUrl(rootURL + urlDestination))
                .andExpect(view().name("curvePoint/list"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Curve Point List")))
                .andExpect(model().attributeExists("curvePoints"))
                .andExpect(model().attribute("curvePoints", curveListGiven))
                .andReturn();

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(curvePointRepository, Mockito.times(1)).save(ArgumentMatchers.any());
        verify(curvePointRepository, Mockito.times(1)).save(ArgumentMatchers.refEq(curvePointUpdated));
    }

    @Order(9)
    @Test
    void deleteCurve() throws Exception {
        //***********GIVEN*************
        int curvePointId = 5;
        curvePointUpdated.setId(curvePointId);
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
        verify(curvePointRepository, Mockito.never()).findById(curvePointId);

        //**************WHEN-THEN****************************
        MvcResult mvcResult =  mockMvc.perform(builder)//.andDo(print());
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/list"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Curve Point List")))
                .andExpect(model().attributeExists("curvePoints"))
                .andExpect(model().attribute("curvePoints", curveListGiven))
                .andReturn();

        //***********************************************************
        //**************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(curvePointRepository, Mockito.times(1)).findById(curvePointId);
        verify(curvePointRepository, Mockito.times(1)).deleteById(curvePointId);
    }
}