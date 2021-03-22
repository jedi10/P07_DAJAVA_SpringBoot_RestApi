package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/curvePoint/")
public class CurveController {

    private CurvePointRepository curvePointRepository;

    private String dataName = "CurvePoint";

    public CurveController(CurvePointRepository curvePointRepository) {
        this.curvePointRepository = curvePointRepository;
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String home(Model model, HttpServletRequest request, HttpServletResponse response)
    {
        log.info("Get {} List on URI: '{}' : RESPONSE STATUS: '{}'",
                dataName,
                request.getRequestURI(),
                response.getStatus());
        model.addAttribute("curvePoints", curvePointRepository.findAll());
        return "curvePoint/list";
    }

    @GetMapping(value = "add")
    public String addCurveForm(CurvePoint curve, Model model,
                               HttpServletRequest request, HttpServletResponse response) {
        log.info("Go to {} Creation Form on URI: '{}' : RESPONSE STATUS: '{}'",
                dataName,
                request.getRequestURI(),
                response.getStatus());
        model.addAttribute("curvePoint", new CurvePoint());
        return "curvePoint/add";
    }

    @PostMapping(value = "validate")
    public String validate(@Valid @ModelAttribute("curvePoint") CurvePoint curvePoint,
                           BindingResult result,
                           Model model,
                           HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()){
            log.warn("{} Creation Error on URI: '{}': Error Field(s): '{}' : RESPONSE STATUS: '{}'",
                    dataName,
                    request.getRequestURI(),
                    result.getFieldErrors().stream()
                            .map(e-> e.getField().toUpperCase())
                            .distinct()
                            .collect(Collectors.joining(", ")),
                    response.getStatus());
            return "curvePoint/add";
        }
        CurvePoint curveCreated =  curvePointRepository.save(curvePoint);
        log.info("{} Creation on URI: '{}' : {} Created '{}' : RESPONSE STATUS: '{}'",
                dataName,
                request.getRequestURI(),
                dataName,
                curveCreated.getId() + " " + curveCreated.getTerm(),
                response.getStatus());
        model.addAttribute("curvePoints", curvePointRepository.findAll());
        return "curvePoint/list";
    }

    @GetMapping(value = "update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id,
                                 Model model,
                                 HttpServletRequest request, HttpServletResponse response) {
        String errorMessage = "Invalid curve Id: there is no curve with Id: ";
        CurvePoint curvePoint = curvePointRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException(errorMessage + id));
        log.info("Go to {} Update Form on URI: '{}': RESPONSE STATUS: '{}'",
                dataName,
                request.getRequestURI(),
                response.getStatus());
        model.addAttribute("curvePoint", curvePoint);
        return "curvePoint/update";
    }

    @PostMapping(value = "update/{id}")
    public String updateCurve(@PathVariable("id") Integer id,
                            @Valid @ModelAttribute("curvePoint") CurvePoint curvePoint,
                            BindingResult result, Model model,
                            HttpServletRequest request, HttpServletResponse response) {
        if(result.hasErrors()){
            log.warn("{} Update Error on URI: '{}': Error Field(s):'{}' : RESPONSE STATUS: '{}'",
                    dataName,
                    request.getRequestURI(),
                    result.getFieldErrors().stream()
                            .map(e-> e.getField().toUpperCase())
                            .distinct()
                            .collect(Collectors.joining(", ")),
                    response.getStatus());
            return "curvePoint/update";
        }
        curvePoint.setId(id);
        CurvePoint curveUpdated = curvePointRepository.save(curvePoint);
        log.info("{} Update on URI: '{}' : {} Updated '{}' : RESPONSE STATUS: '{}'",
                dataName,
                request.getRequestURI(),
                dataName,
                curveUpdated.getId() + " " + curveUpdated.getTerm(),
                response.getStatus());
        model.addAttribute("curvePoints", curvePointRepository.findAll());
        return "curvePoint/list";
    }

    @RolesAllowed("ADMIN")
    @GetMapping(value = "delete/{id}")
    public String deleteCurve(@PathVariable("id") Integer id,
                            Model model,
                            HttpServletRequest request, HttpServletResponse response) {
        Optional<CurvePoint> curvePoint = curvePointRepository.findById(id);
        if(curvePoint.isPresent()){
            curvePointRepository.deleteById(id);
            log.info("Delete {} on URI: '{}' : RESPONSE STATUS: '{}'",
                    dataName,
                    request.getRequestURI(),
                    response.getStatus());
        } else {
            log.warn("No {} was deleted on URI: '{}' : RESPONSE STATUS: '{}'",
                    dataName,
                    request.getRequestURI(),
                    response.getStatus());
        }
        model.addAttribute("curvePoints", curvePointRepository.findAll());
        return "curvePoint/list";
    }
}
