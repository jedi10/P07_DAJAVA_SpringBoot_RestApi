package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/ruleName/")
public class RuleNameController {

    private RuleNameRepository ruleNameRepository;

    public RuleNameController(RuleNameRepository ruleNameRepository) {
        this.ruleNameRepository = ruleNameRepository;
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String home(Model model)
    {
        model.addAttribute("ruleNames", ruleNameRepository.findAll());
        return "ruleName/list";
    }

    @GetMapping(value = "add")
    public String addRuleForm(RuleName ruleName, Model model) {
        model.addAttribute("ruleName", new RuleName());
        return "ruleName/add";
    }

    @PostMapping(value = "validate")
    public String validate(@Valid @ModelAttribute("ruleName") RuleName ruleName,
                           BindingResult result,
                           Model model) {
        if(result.hasErrors()){
            return "ruleName/add";
        }
        ruleNameRepository.save(ruleName);
        model.addAttribute("ruleNames", ruleNameRepository.findAll());
        return "ruleName/list";
    }

    @GetMapping(value = "update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id,
                                 Model model) {
        String errorMessage = "Invalid ruleName Id: there is no ruleName with Id: ";
        RuleName ruleName = ruleNameRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException(errorMessage + id));
        model.addAttribute("ruleName", ruleName);
        return "ruleName/update";
    }

    @PostMapping(value = "update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id,
                                 @Valid @ModelAttribute("ruleName") RuleName ruleName,
                                 BindingResult result,
                                 Model model) {
        if(result.hasErrors()){
            return "ruleName/update";
        }
        ruleName.setId(id);
        ruleNameRepository.save(ruleName);
        model.addAttribute("ruleNames", ruleNameRepository.findAll());
        return "ruleName/list";
    }

    @RolesAllowed("ADMIN")
    @GetMapping(value = "delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id,
                                 Model model) {
        Optional<RuleName> ruleName = ruleNameRepository.findById(id);
        if(ruleName.isPresent()){
            ruleNameRepository.deleteById(id);
        }
        model.addAttribute("ruleNames", ruleNameRepository.findAll());
        return "ruleName/list";
    }
}
