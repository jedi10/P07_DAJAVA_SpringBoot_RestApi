package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.Optional;


@Controller
@RequestMapping("/bidList/")
public class BidListController {

    private BidListRepository bidListRepository;

    public BidListController(BidListRepository bidListRepository) {
        this.bidListRepository = bidListRepository;
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String home(Model model)
    {
        model.addAttribute("bids", bidListRepository.findAll());
        return "bidList/list";
    }

    @GetMapping(value = "add")
    public String addBidForm(BidList bid, Model model) {
        model.addAttribute("bid", new BidList());
        return "bidList/add";
    }

    @PostMapping(value = "validate", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE+";charset=UTF-8"})//"application/x-www-form-urlencoded")
    public String validate(@Valid BidList bid,
                           BindingResult result,
                           Model model) {
        if (result.hasErrors()) {
            return "bidList/add";
        }
        bidListRepository.save(bid);
        model.addAttribute("bids", bidListRepository.findAll());
        return "bidList/list";
    }

    @GetMapping(value = "update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id,
                                 Model model) {
        String errorMessage = "Invalid bid Id: there is no bid with Id: ";

        BidList bid = bidListRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(errorMessage + id));
        model.addAttribute("bid", bid);
        return "bidList/update";
    }

    @PostMapping(value = "update/{id}")
    public String updateBid(@PathVariable("id") Integer id,
                            @Valid BidList bidList,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "bidList/update";
        }

        bidList.setId(id);
        bidListRepository.save(bidList);
        model.addAttribute("bids", bidListRepository.findAll());
        return "bidList/list";
    }

    @RolesAllowed("ADMIN")
    @GetMapping(value = "delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        Optional<BidList> bidOptional = bidListRepository.findById(id);
        if(bidOptional.isPresent()){
            bidListRepository.deleteById(id);
        }
        model.addAttribute("bids", bidListRepository.findAll());
        return "bidList/list";
    }
}
