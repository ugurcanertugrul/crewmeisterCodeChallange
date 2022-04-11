package com.crewmeister.cmcodingchallenge.controller;

import com.crewmeister.cmcodingchallenge.dto.ExchangeRateDTO;
import com.crewmeister.cmcodingchallenge.service.exchangeRate.ExchangeRateService;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ExchangeRateControllerTest {
    @Mock
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private ExchangeRateController exchangeRateController;

    @Before
    public void init()
    {
        exchangeRateController = new ExchangeRateController(exchangeRateService);
    }

    @Test
    public void getExchangeRatesTest()
    {
        Mockito.doReturn(new ArrayList<>()).when(exchangeRateService).getAllRates(Mockito.any(),Mockito.any(),Mockito.any());
        ResponseEntity<List<ExchangeRateDTO>> response = exchangeRateController.getExchangeRates("USD","2020-03-01","2022-03-01");
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void convertRateTest()
    {
        Mockito.doReturn(BigDecimal.ONE).when(exchangeRateService).convertRate(Mockito.any(),Mockito.any(),Mockito.any());
        ResponseEntity<BigDecimal> response = exchangeRateController.convertRate("USD","2020-03-01", BigDecimal.ONE);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test(expected = ResponseStatusException.class)
    public void getExchangeRatesExceptionTest()
    {
        Mockito.doThrow(ResponseStatusException.class).when(exchangeRateService).getAllRates(Mockito.any(),Mockito.any(),Mockito.any());
       exchangeRateController.getExchangeRates("USD","2020-03-01","2022-03-01");

    }

    @Test(expected = ResponseStatusException.class)
    public void convertRateExceptionTest()
    {
        Mockito.doThrow(ResponseStatusException.class).when(exchangeRateService).convertRate(Mockito.any(),Mockito.any(),Mockito.any());
        exchangeRateController.convertRate("USD","2020-03-01", BigDecimal.ONE);
    }
}
