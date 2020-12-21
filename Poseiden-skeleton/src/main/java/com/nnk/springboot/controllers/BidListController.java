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
    public String addBidForm(BidList bid) {
        return "bidList/add";
    }

    @PostMapping(value = "validate")
    public String validate(@RequestBody @Valid BidList bid,
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
        Optional<BidList> bidOptional = bidListRepository.findById(id);
        if(bidOptional.isPresent()){
            model.addAttribute("bid", bidOptional.get());
        } else {
            throw new IllegalArgumentException("Invalid bid Id: there is no bid with Id:" + id);
        }
        return "bidList/update";
    }

    @PutMapping(value = "update/{id}")
    public String updateBid(@PathVariable("id") Integer id,
                            @RequestBody @Valid BidList bidList,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/bidList/update";
        }

        bidList.setId(id);
        bidListRepository.save(bidList);
        model.addAttribute("bids", bidListRepository.findAll());
        return "bidList/list";
    }

    @RolesAllowed("ADMIN")
    @DeleteMapping(value = "delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        Optional<BidList> bidOptional = bidListRepository.findById(id);
        if(bidOptional.isPresent()){
            bidListRepository.deleteById(id);
        }
        model.addAttribute("bids", bidListRepository.findAll());
        return "bidList/list";
    }
}
