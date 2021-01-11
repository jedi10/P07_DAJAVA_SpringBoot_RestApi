package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/trade/")
public class TradeController {

    private TradeRepository tradeRepository;

    public TradeController(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String home(Model model)
    {
        model.addAttribute("trades", tradeRepository.findAll());
        return "trade/list";
    }

    @GetMapping(value = "add")
    public String addUser(Trade bid, Model model)
    {
        model.addAttribute("trade", new Trade());
        return "trade/add";
    }

    @PostMapping(value = "validate")
    public String validate(@Valid Trade trade,
                           BindingResult result,
                           Model model) {
        if (result.hasErrors()) {
            return "trade/add";
        }
        tradeRepository.save(trade);
        model.addAttribute("trades", tradeRepository.findAll());
        return "trade/list";
    }

    @GetMapping(value = "update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id,
                                 Model model) {
        String errorMessage = "Invalid trade Id: there is no trade with Id: ";

        Trade trade = tradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(errorMessage + id));
        model.addAttribute("trade", trade);
        return "trade/update";
    }

    @PostMapping(value = "update/{id}")
    public String updateTrade(@PathVariable("id") Integer id,
                              @Valid Trade trade,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "trade/update";
        }
        trade.setId(id);
        tradeRepository.save(trade);
        model.addAttribute("trades", tradeRepository.findAll());
        return "trade/list";
    }

    @GetMapping(value = "delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        Optional<Trade> bidOptional = tradeRepository.findById(id);
        if(bidOptional.isPresent()){
            tradeRepository.deleteById(id);
        }
        model.addAttribute("trades", tradeRepository.findAll());
        return "trade/list";
    }
}
