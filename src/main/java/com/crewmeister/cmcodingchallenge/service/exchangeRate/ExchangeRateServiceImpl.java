package com.crewmeister.cmcodingchallenge.service.exchangeRate;

import com.crewmeister.cmcodingchallenge.dto.ExchangeRateDTO;
import com.crewmeister.cmcodingchallenge.util.DocumentUtil;
import com.crewmeister.cmcodingchallenge.util.WebServiceConstant;
import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final Logger logger = LoggerFactory.getLogger(ExchangeRateServiceImpl.class);
    private final MessageSource messageSource;
    private final Locale locale = LocaleContextHolder.getLocale();
    @Override
    public List<ExchangeRateDTO> getAllRates(String currency, String startPeriod, String endPeriod) {
        List<ExchangeRateDTO> exchangeRateDTOList = new ArrayList<>();
        try {
            final Document document = DocumentUtil.getDocumentFromRestAsXml(getExchangeRateURL(currency, startPeriod, endPeriod));
            final NodeList nodeList = document.getElementsByTagName(WebServiceConstant.EXCHANGE_RATE_TAG_NAME);
            for (int i = 0; i < nodeList.getLength(); i++) {
                final NodeList valuesNodeList = nodeList.item(i).getChildNodes();
                String date = "";
                String value = "";
                for (int j = 0; j < valuesNodeList.getLength(); j++) {
                    if (valuesNodeList.item(j).getNodeName().equalsIgnoreCase(WebServiceConstant.EXCHANGE_RATE_DIMENSION)) {
                        date = valuesNodeList.item(j).getAttributes().getNamedItem(WebServiceConstant.EXCHANGE_RATE_VALUE).getNodeValue();
                    }
                    if (valuesNodeList.item(j).getNodeName().equalsIgnoreCase(WebServiceConstant.EXCHANGE_RATE_DIMENSION_VALUE)) {
                        value = valuesNodeList.item(j).getAttributes().getNamedItem(WebServiceConstant.EXCHANGE_RATE_VALUE).getNodeValue();
                    }
                }
                if (!Strings.isNullOrEmpty(value)) {
                    exchangeRateDTOList.add(new ExchangeRateDTO(currency, date, value));
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException ex) {
            logger.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, messageSource.getMessage("CannotParseXml",null,locale));
        } catch (HttpClientErrorException ex) {
            logger.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, messageSource.getMessage("CannotGetDataFromService",null,locale));
        }
        return exchangeRateDTOList;
    }

    @Override
    public BigDecimal convertRate(String currency, String date, BigDecimal amount) {
        List<ExchangeRateDTO> exchangeRateDTOList = getAllRates(currency, date, date);
        try {
            if (exchangeRateDTOList != null && exchangeRateDTOList.size() > 0) {
                final String exchangeRate = exchangeRateDTOList.get(0).getRate();
                if (!Strings.isNullOrEmpty(exchangeRate)) {
                    return new BigDecimal(exchangeRate).multiply(amount);
                }
            } else {
                throw new ResponseStatusException(HttpStatus.OK,messageSource.getMessage("CannotFindRate",null,locale));
            }
        } catch (NumberFormatException ex) {
            logger.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,messageSource.getMessage("CannotConvertExchangeRate",null,locale));
        }

        return BigDecimal.ZERO;
    }

    private String getExchangeRateURL(String currency, String startPeriod, String endPeriod) {
        return WebServiceConstant.EXCHANGE_RATE_BASE_URL +
                WebServiceConstant.EXCHANGE_RATE_FREQUENCY +
                currency +
                WebServiceConstant.EXCHANGE_RATE_CURRENCY_DENOMINATOR +
                WebServiceConstant.EXCHANGE_RATE_CONTENT_OF_TIME_SERIES +
                WebServiceConstant.EXCHANGE_RATE_RATE_TYPE +
                WebServiceConstant.EXCHANGE_RATE_SUFFIX +
                WebServiceConstant.EXCHANGE_RATE_PARAMETER_PREFIX +
                (!Strings.isNullOrEmpty(startPeriod) && !startPeriod.equalsIgnoreCase("null") ? (WebServiceConstant.EXCHANGE_RATE_START_PERIOD + startPeriod + WebServiceConstant.EXCHANGE_RATE_PARAMETER_AND) : "") +
                (!Strings.isNullOrEmpty(endPeriod) && !endPeriod.equalsIgnoreCase("null") ? (WebServiceConstant.EXCHANGE_RATE_END_PERIOD + endPeriod + WebServiceConstant.EXCHANGE_RATE_PARAMETER_AND) : "") +
                WebServiceConstant.EXCHANGE_RATE_DETAIL;
    }
}
