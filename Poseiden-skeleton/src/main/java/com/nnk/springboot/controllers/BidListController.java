package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Api(tags = {"BidList Controller"})
@Tag(name = "BidList Controller", description = "Private Resources")
@Slf4j
@Controller
@RequestMapping("/bidList/")
public class BidListController {

    private BidListRepository bidListRepository;

    public BidListController(BidListRepository bidListRepository) {
        this.bidListRepository = bidListRepository;
    }

    @ApiOperation(value = "Get List of Bid")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String home(Model model, HttpServletRequest request, HttpServletResponse response)
    {
        log.info("Get bid List on URI: '{}' : RESPONSE STATUS: '{}'",
                request.getRequestURI(),
                response.getStatus());
        model.addAttribute("bids", bidListRepository.findAll());
        return "bidList/list";
    }

    @ApiOperation(value = "Go to Creation Bid Form")
    @GetMapping(value = "add")
    public String addBidForm(BidList bid, Model model,
            HttpServletRequest request, HttpServletResponse response) {
        model.addAttribute("bid", new BidList());
        log.info("Go to Creation Bid Form on URI: '{}' : RESPONSE STATUS: '{}'",
                request.getRequestURI(),
                response.getStatus());
        return "bidList/add";
    }

    @ApiOperation(value = "Bid Creation Validation")
    @PostMapping(value = "validate") // consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE+";charset=UTF-8"})//"application/x-www-form-urlencoded")
    public String validate(@Valid  @ModelAttribute("bid") BidList bid,
                           BindingResult result,
                           Model model,
                           HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            log.warn("Bid Creation Error on URI: '{}': Error Field(s): '{}' : RESPONSE STATUS: '{}'",
                    request.getRequestURI(),
                    result.getFieldErrors().stream()
                            .map(e-> e.getField().toUpperCase())
                            .distinct()
                            .collect(Collectors.joining(", ")),
                    response.getStatus());
            return "bidList/add";
        }
        BidList bidCreated = bidListRepository.save(bid);
        log.info("Bid Creation on URI: '{}' : Bid Created '{}' : RESPONSE STATUS: '{}'",
                request.getRequestURI(),
                bidCreated.getId() + " " + bidCreated.getAccount(),
                response.getStatus());
        model.addAttribute("bids", bidListRepository.findAll());
        return "bidList/list";
    }

    @ApiOperation(value = "Go to Update Bid Form")
    @GetMapping(value = "update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id,
                                 Model model,
                                 HttpServletRequest request, HttpServletResponse response) {
        String errorMessage = "Invalid bid Id: there is no bid with Id: ";

        BidList bid = bidListRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(errorMessage + id));
        log.info("Go to Update Bid Form on URI: '{}': RESPONSE STATUS: '{}'",
                request.getRequestURI(),
                response.getStatus());
        model.addAttribute("bid", bid);
        return "bidList/update";
    }

    @ApiOperation(value = "Bid Update Validation")
    @PostMapping(value = "update/{id}")
    public String updateBid(@PathVariable("id") Integer id,
                            @Valid  @ModelAttribute("bid") BidList bidList,
                            BindingResult result, Model model,
                            HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            log.warn("Update Bid Error on URI: '{}': Error Field(s):'{}' : RESPONSE STATUS: '{}'",
                    request.getRequestURI(),
                    result.getFieldErrors().stream()
                            .map(e-> e.getField().toUpperCase())
                            .distinct()
                            .collect(Collectors.joining(", ")),
                    response.getStatus());
            return "bidList/update";
        }
        bidList.setId(id);
        BidList bidUpdated = bidListRepository.save(bidList);
        log.info("Bid Update on URI: '{}' : Bid Updated '{}' : RESPONSE STATUS: '{}'",
                request.getRequestURI(),
                bidUpdated.getId() + " " + bidUpdated.getAccount(),
                response.getStatus());
        model.addAttribute("bids", bidListRepository.findAll());
        return "bidList/list";
    }

    @ApiOperation(value = "Delete Bid")
    @RolesAllowed("ADMIN")
    @GetMapping(value = "delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model,
                            HttpServletRequest request, HttpServletResponse response) {
        Optional<BidList> bidOptional = bidListRepository.findById(id);
        if(bidOptional.isPresent()){
            bidListRepository.deleteById(id);
            log.info("Delete Bid on URI: '{}' : RESPONSE STATUS: '{}'",
                    request.getRequestURI(),
                    response.getStatus());
        } else {
            log.warn("No Bid was deleted on URI: '{}' : RESPONSE STATUS: '{}'",
                    request.getRequestURI(),
                    response.getStatus());
        }

        model.addAttribute("bids", bidListRepository.findAll());
        return "bidList/list";
    }
}
