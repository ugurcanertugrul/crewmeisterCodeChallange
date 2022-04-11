package com.crewmeister.cmcodingchallenge.controller;

import com.crewmeister.cmcodingchallenge.dto.ExchangeRateDTO;
import com.crewmeister.cmcodingchallenge.service.exchangeRate.ExchangeRateService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController()
@RequestMapping("/api")
@AllArgsConstructor
public class ExchangeRateController {

    @Autowired
    private ExchangeRateService exchangeRateService;

    @GetMapping("/getRates")
    public ResponseEntity<List<ExchangeRateDTO>> getExchangeRates(@RequestParam String currency,@RequestParam(required = false)String startPeriod, @RequestParam(required = false)String endPeriod) {
        List<ExchangeRateDTO> exchangeRateDTOList = exchangeRateService.getAllRates(currency,startPeriod,endPeriod);
        return new ResponseEntity<>(exchangeRateDTOList, HttpStatus.OK);
    }

    @GetMapping("/convertRate")
    public ResponseEntity<BigDecimal> convertRate(@RequestParam String currency, @RequestParam String date, @RequestParam BigDecimal amount) {
        BigDecimal convertedAmount = exchangeRateService.convertRate(currency,date,amount);
        return new ResponseEntity<>(convertedAmount, HttpStatus.OK);
    }
}
