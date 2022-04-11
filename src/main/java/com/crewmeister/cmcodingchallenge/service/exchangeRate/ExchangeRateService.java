package com.crewmeister.cmcodingchallenge.service.exchangeRate;

import com.crewmeister.cmcodingchallenge.dto.ExchangeRateDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ExchangeRateService {
    List<ExchangeRateDTO> getAllRates(String currency, String startPeriod, String endPeriod);

    BigDecimal convertRate(String currency, String date, BigDecimal amount);
}
