package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;
import java.util.stream.Collectors;

@Api(tags = {"Trade Controller"})
@Tag(name = "Trade Controller", description = "Private Resources")
@Slf4j
@Controller
@RequestMapping("/trade/")
public class TradeController {

    private TradeRepository tradeRepository;

    private String dataName = "Trade";

    public TradeController(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    @ApiOperation(value = "Get List of Trade")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String home(Model model, HttpServletRequest request, HttpServletResponse response)
    {
        log.info("Get {} List on URI: '{}' : RESPONSE STATUS: '{}'",
                dataName,
                request.getRequestURI(),
                response.getStatus());
        model.addAttribute("trades", tradeRepository.findAll());
        return "trade/list";
    }

    @ApiOperation(value = "Go to Creation Trade Form")
    @GetMapping(value = "add")
    public String addUser(Trade bid, Model model,
                          HttpServletRequest request, HttpServletResponse response)
    {
        log.info("Go to {} Creation Form on URI: '{}' : RESPONSE STATUS: '{}'",
                dataName,
                request.getRequestURI(),
                response.getStatus());
        model.addAttribute("trade", new Trade());
        return "trade/add";
    }

    @ApiOperation(value = "Trade Creation Validation")
    @PostMapping(value = "validate")
    public String validate(@Valid @ModelAttribute("trade") Trade trade,
                           BindingResult result,
                           Model model,
                           HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            log.warn("{} Creation Error on URI: '{}': Error Field(s): '{}' : RESPONSE STATUS: '{}'",
                    dataName,
                    request.getRequestURI(),
                    result.getFieldErrors().stream()
                            .map(e-> e.getField().toUpperCase())
                            .distinct()
                            .collect(Collectors.joining(", ")),
                    response.getStatus());
            return "trade/add";
        }
        Trade tradeCreated = tradeRepository.save(trade);
        log.info("{} Creation on URI: '{}' : {} Created '{}' : RESPONSE STATUS: '{}'",
                dataName,
                request.getRequestURI(),
                dataName,
                tradeCreated.getId() + " " + tradeCreated.getAccount(),
                response.getStatus());
        model.addAttribute("trades", tradeRepository.findAll());
        return "trade/list";
    }

    @ApiOperation(value = "Go to Update Trade Form")
    @GetMapping(value = "update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id,
                                 Model model,
                                 HttpServletRequest request, HttpServletResponse response) {
        String errorMessage = "Invalid trade Id: there is no trade with Id: ";

        Trade trade = tradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(errorMessage + id));
        log.info("Go to {} Update Form on URI: '{}': RESPONSE STATUS: '{}'",
                dataName,
                request.getRequestURI(),
                response.getStatus());
        model.addAttribute("trade", trade);
        return "trade/update";
    }

    @ApiOperation(value = "Trade Update Validation")
    @PostMapping(value = "update/{id}")
    public String updateTrade(@PathVariable("id") Integer id,
                              @Valid @ModelAttribute("trade") Trade trade,
                              BindingResult result, Model model,
                              HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            log.warn("{} Update Error on URI: '{}': Error Field(s):'{}' : RESPONSE STATUS: '{}'",
                    dataName,
                    request.getRequestURI(),
                    result.getFieldErrors().stream()
                            .map(e-> e.getField().toUpperCase())
                            .distinct()
                            .collect(Collectors.joining(", ")),
                    response.getStatus());
            return "trade/update";
        }
        trade.setId(id);
        Trade tradeUpdated = tradeRepository.save(trade);
        log.info("{} Update on URI: '{}' : {} Updated '{}' : RESPONSE STATUS: '{}'",
                dataName,
                request.getRequestURI(),
                dataName,
                tradeUpdated.getId() + " " + tradeUpdated.getAccount(),
                response.getStatus());
        model.addAttribute("trades", tradeRepository.findAll());
        return "trade/list";
    }

    @ApiOperation(value = "Delete Trade")
    @GetMapping(value = "delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model,
                              HttpServletRequest request, HttpServletResponse response) {
        Optional<Trade> bidOptional = tradeRepository.findById(id);
        if(bidOptional.isPresent()){
            tradeRepository.deleteById(id);
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
        model.addAttribute("trades", tradeRepository.findAll());
        return "trade/list";
    }
}
