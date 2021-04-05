package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Api(tags = {"RuleName Controller"})
@Tag(name = "RuleName Controller", description = "Private Resources")
@Slf4j
@Controller
@RequestMapping("/ruleName/")
public class RuleNameController {

    private RuleNameRepository ruleNameRepository;

    private String dataName = "RuleName";

    public RuleNameController(RuleNameRepository ruleNameRepository) {
        this.ruleNameRepository = ruleNameRepository;
    }

    @ApiOperation(value = "Get List of RuleName")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String home(Model model, HttpServletRequest request, HttpServletResponse response)
    {
        log.info("Get {} List on URI: '{}' : RESPONSE STATUS: '{}'",
                dataName,
                request.getRequestURI(),
                response.getStatus());
        model.addAttribute("ruleNames", ruleNameRepository.findAll());
        return "ruleName/list";
    }

    @ApiOperation(value = "Go to Creation RuleName Form")
    @GetMapping(value = "add")
    public String addRuleForm(RuleName ruleName, Model model,
                              HttpServletRequest request, HttpServletResponse response) {
        log.info("Go to {} Creation Form on URI: '{}' : RESPONSE STATUS: '{}'",
                dataName,
                request.getRequestURI(),
                response.getStatus());
        model.addAttribute("ruleName", new RuleName());
        return "ruleName/add";
    }

    @ApiOperation(value = "RuleName Creation Validation")
    @PostMapping(value = "validate")
    public String validate(@Valid @ModelAttribute("ruleName") RuleName ruleName,
                           BindingResult result,
                           Model model,
                           HttpServletRequest request, HttpServletResponse response) {
        if(result.hasErrors()){
            log.warn("{} Creation Error on URI: '{}': Error Field(s): '{}' : RESPONSE STATUS: '{}'",
                    dataName,
                    request.getRequestURI(),
                    result.getFieldErrors().stream()
                            .map(e-> e.getField().toUpperCase())
                            .distinct()
                            .collect(Collectors.joining(", ")),
                    response.getStatus());
            return "ruleName/add";
        }
        RuleName ruleCreated = ruleNameRepository.save(ruleName);
        log.info("{} Creation on URI: '{}' : {} Created '{}' : RESPONSE STATUS: '{}'",
                dataName,
                request.getRequestURI(),
                dataName,
                ruleCreated.getId() + " " + ruleCreated.getName(),
                response.getStatus());
        model.addAttribute("ruleNames", ruleNameRepository.findAll());
        return "ruleName/list";
    }

    @ApiOperation(value = "Go to Update RuleName Form")
    @GetMapping(value = "update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id,
                                 Model model,
                                 HttpServletRequest request, HttpServletResponse response) {
        String errorMessage = "Invalid ruleName Id: there is no ruleName with Id: ";
        RuleName ruleName = ruleNameRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException(errorMessage + id));
        log.info("Go to {} Update Form on URI: '{}': RESPONSE STATUS: '{}'",
                dataName,
                request.getRequestURI(),
                response.getStatus());
        model.addAttribute("ruleName", ruleName);
        return "ruleName/update";
    }

    @ApiOperation(value = "RuleName Update Validation")
    @PostMapping(value = "update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id,
                                 @Valid @ModelAttribute("ruleName") RuleName ruleName,
                                 BindingResult result,
                                 Model model,
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
            return "ruleName/update";
        }
        ruleName.setId(id);
        RuleName ruleUpdated = ruleNameRepository.save(ruleName);
        log.info("{} Update on URI: '{}' : {} Updated '{}' : RESPONSE STATUS: '{}'",
                dataName,
                request.getRequestURI(),
                dataName,
                ruleUpdated.getId() + " " + ruleUpdated.getName(),
                response.getStatus());
        model.addAttribute("ruleNames", ruleNameRepository.findAll());
        return "ruleName/list";
    }

    @ApiOperation(value = "Delete RuleName")
    @RolesAllowed("ADMIN")
    @GetMapping(value = "delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id,
                                 Model model,
                                 HttpServletRequest request, HttpServletResponse response) {
        Optional<RuleName> ruleName = ruleNameRepository.findById(id);
        if(ruleName.isPresent()){
            ruleNameRepository.deleteById(id);
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
        model.addAttribute("ruleNames", ruleNameRepository.findAll());
        return "ruleName/list";
    }
}
