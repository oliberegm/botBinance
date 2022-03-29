package com.olibersystem.bot.binance.botbinance.controller;

import com.olibersystem.bot.binance.botbinance.dto.response.SymbolDto;
import com.olibersystem.bot.binance.botbinance.model.SymbolBO;
import com.olibersystem.bot.binance.botbinance.service.MotorDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/info")
public class InfoController {
    private final MotorDataService motorDataService;

    public InfoController(MotorDataService motorDataService) {
        this.motorDataService = motorDataService;
    }
    @GetMapping("/")
    public ResponseEntity<List<SymbolDto>> getAll() {
        return new ResponseEntity(motorDataService.getAllOrder(), HttpStatus.OK);
    }
}
