package com.crewmeister.cmcodingchallenge.service.currency;

import com.crewmeister.cmcodingchallenge.dto.CurrencyDTO;
import com.crewmeister.cmcodingchallenge.util.DocumentUtil;
import com.crewmeister.cmcodingchallenge.util.WebServiceConstant;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyServiceImpl.class);
    private final MessageSource messageSource;
    private final Locale locale = LocaleContextHolder.getLocale();

    @Override
    public List<CurrencyDTO> getAllCurrencies() {
        List<CurrencyDTO> currencyDTOList = new ArrayList<>();

        try {
            final Document xmlDocument = DocumentUtil.getDocumentFromRestAsXml(WebServiceConstant.CURRENCY_URL);
            if (xmlDocument != null) {
                NodeList currencyNodes = xmlDocument.getElementsByTagName(WebServiceConstant.CURRENCY_TAG);
                for (int curIndex = 0; curIndex < currencyNodes.getLength(); curIndex++) {
                    final Node currencyNode = currencyNodes.item(curIndex);
                    final String currencyCode = currencyNode.getAttributes().getNamedItem(WebServiceConstant.CURRENCY_TAG_NAME).getNodeValue();
                    if (currencyCode.length() == 3) {
                        final NodeList langNodes = currencyNode.getChildNodes();
                        String currencyCommonName = "";
                        for (int langIndex = 0; langIndex < langNodes.getLength(); langIndex++) {
                            if (langNodes.item(langIndex).getAttributes().getNamedItem(WebServiceConstant.CURRENCY_LANG_TAG).getNodeValue().equalsIgnoreCase(WebServiceConstant.CURRENCY_LANG_NAME)) {
                                currencyCommonName = langNodes.item(langIndex).getTextContent();
                            }
                        }
                        currencyDTOList.add(new CurrencyDTO(currencyCode, currencyCommonName));
                    }
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException ex) {
            logger.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, messageSource.getMessage("CannotParseXml", null, locale));
        } catch (HttpClientErrorException ex) {
            logger.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, messageSource.getMessage("CannotGetDataFromService", null, locale));
        }
        return currencyDTOList;
    }


}
