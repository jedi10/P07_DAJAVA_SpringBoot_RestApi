package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/curvePoint/")
public class CurveController {

    private CurvePointRepository curvePointRepository;

    public CurveController(CurvePointRepository curvePointRepository) {
        this.curvePointRepository = curvePointRepository;
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String home(Model model)
    {
        model.addAttribute("curvePoints", curvePointRepository.findAll());
        return "curvePoint/list";
    }

    @GetMapping(value = "add")
    public String addCurveForm(CurvePoint curve) {

        return "curvePoint/add";
    }

    @PostMapping(value = "validate")
    public String validate(@RequestBody @Valid CurvePoint curvePoint,
                           BindingResult result,
                           Model model) {
        if (result.hasErrors()){
            return "curvePoint/add";
        }
        curvePointRepository.save(curvePoint);
        model.addAttribute("curvePoints", curvePointRepository.findAll());
        return "curvePoint/list";
    }

    @GetMapping(value = "update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id,
                                 Model model) {
        String errorMessage = "Invalid curve Id: there is no curve with Id: ";
        CurvePoint curvePoint = curvePointRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException(errorMessage + id));
        model.addAttribute("curvePoint", curvePoint);
        return "curvePoint/update";
    }

    @PutMapping(value = "update/{id}")
    public String updateCurve(@PathVariable("id") Integer id,
                            @RequestBody @Valid CurvePoint curvePoint,
                            BindingResult result, Model model) {
        if(result.hasErrors()){
            return "curvePoint/update";
        }
        curvePoint.setId(id);
        curvePointRepository.save(curvePoint);
        model.addAttribute("curvePoints", curvePointRepository.findAll());
        return "curvePoint/list";
    }

    @RolesAllowed("ADMIN")
    @DeleteMapping(value = "delete/{id}")
    public String deleteCurve(@PathVariable("id") Integer id,
                            Model model) {
        Optional<CurvePoint> curvePoint = curvePointRepository.findById(id);
        if(curvePoint.isPresent()){
            curvePointRepository.deleteById(id);
        }
        model.addAttribute("curvePoints", curvePointRepository.findAll());
        return "curvePoint/list";
    }
}
