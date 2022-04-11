package com.crewmeister.cmcodingchallenge.controller;

import com.crewmeister.cmcodingchallenge.dto.CurrencyDTO;
import com.crewmeister.cmcodingchallenge.service.currency.CurrencyService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyControllerTest {
    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private CurrencyController currencyController;

    @Before
    public void init()
    {
        currencyController = new CurrencyController(currencyService);
    }

    @Test
    public void getCurrenciesTest()
    {
        Mockito.doReturn(new ArrayList<>()).when(currencyService).getAllCurrencies();
        ResponseEntity<List<CurrencyDTO>> response =  currencyController.getCurrencies();
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }
    @Test(expected = ResponseStatusException.class)
    public void getCurrenciesExceptionTest()
    {
        Mockito.doThrow(ResponseStatusException.class).when(currencyService).getAllCurrencies();
        currencyController.getCurrencies();
    }
}
