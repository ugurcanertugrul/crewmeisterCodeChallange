package com.crewmeister.cmcodingchallenge.service.currency;

import com.crewmeister.cmcodingchallenge.dto.CurrencyDTO;

import java.util.List;

public interface CurrencyService {

    List<CurrencyDTO> getAllCurrencies();
}
