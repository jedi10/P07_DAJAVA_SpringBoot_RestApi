package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/rating/")
public class RatingController {

    private RatingRepository ratingRepository;

    public RatingController(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String home(Model model)
    {
        model.addAttribute("ratings", ratingRepository.findAll());
        return "rating/list";
    }

    @GetMapping(value = "add")
    public String addRatingForm(Rating rating, Model model)
    {
        model.addAttribute("rating", new Rating());
        return "rating/add";
    }

    @PostMapping(value = "validate")
    public String validate(@Valid @ModelAttribute("rating") Rating rating,
                           BindingResult result,
                           Model model) {
        if(result.hasErrors()){
            return "rating/add";
        }
        ratingRepository.save(rating);
        model.addAttribute("ratings", ratingRepository.findAll());
        return "rating/list";
    }

    @GetMapping(value= "update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id,
                                 Model model) {
        String errorMessage = "Invalid rating Id: there is no rating with Id: ";
        Rating rating = ratingRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException(errorMessage + id));
        model.addAttribute("rating", rating);
        return "rating/update";
    }

    @PostMapping(value = "update/{id}")
    public String updateRating(@PathVariable("id") Integer id,
                               @Valid @ModelAttribute("rating") Rating rating,
                               BindingResult result,
                               Model model) {
        if(result.hasErrors()){
            return "rating/update";
        }
        rating.setId(id);
        ratingRepository.save(rating);
        model.addAttribute("ratings", ratingRepository.findAll());
        return "rating/list";
    }

    @RolesAllowed("ADMIN")
    @GetMapping(value ="delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        Optional<Rating> rating = ratingRepository.findById(id);
        if(rating.isPresent()){
            ratingRepository.deleteById(id);
        }
        model.addAttribute("ratings", ratingRepository.findAll());
        return "rating/list";
    }
}
