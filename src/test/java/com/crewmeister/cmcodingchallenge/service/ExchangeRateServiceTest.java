package com.crewmeister.cmcodingchallenge.service;

import com.crewmeister.cmcodingchallenge.dto.ExchangeRateDTO;
import com.crewmeister.cmcodingchallenge.service.exchangeRate.ExchangeRateServiceImpl;
import com.crewmeister.cmcodingchallenge.util.DocumentUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ExchangeRateServiceTest {

    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private ExchangeRateServiceImpl exchangeRateService;

    @Before
    public void init() {
        exchangeRateService = new ExchangeRateServiceImpl(messageSource);
    }
    @Test
    public void getAllRatesTest()
    {
        try (MockedStatic<DocumentUtil> utilities = Mockito.mockStatic(DocumentUtil.class)) {
            utilities.when(() -> DocumentUtil.getDocumentFromRestAsXml(Mockito.any()))
                    .thenReturn(createDocumentForExchangeRates());
            List<ExchangeRateDTO> exchangeRateDTOList = exchangeRateService.getAllRates("USD",null,null);
            Assert.assertEquals(exchangeRateDTOList.size(), 6);
        }
    }

    @Test
    public void convertRateTest()
    {
        try (MockedStatic<DocumentUtil> utilities = Mockito.mockStatic(DocumentUtil.class)) {
            utilities.when(() -> DocumentUtil.getDocumentFromRestAsXml(Mockito.any()))
                    .thenReturn(createDocumentForConvertRate());
            BigDecimal converted = exchangeRateService.convertRate("USD","2022-03-01", BigDecimal.ONE);
            Assert.assertEquals(converted, BigDecimal.ONE);
        }
    }

    @Test(expected = ResponseStatusException.class)
    public void getAllRatesException1Test()
    {
        try (MockedStatic<DocumentUtil> utilities = Mockito.mockStatic(DocumentUtil.class)) {
            utilities.when(() -> DocumentUtil.getDocumentFromRestAsXml(Mockito.any()))
                    .thenThrow(HttpClientErrorException.class);
            exchangeRateService.getAllRates("USD",null,null);

        }
    }

    @Test(expected = ResponseStatusException.class)
    public void getAllRatesException2Test()
    {
        try (MockedStatic<DocumentUtil> utilities = Mockito.mockStatic(DocumentUtil.class)) {
            utilities.when(() -> DocumentUtil.getDocumentFromRestAsXml(Mockito.any()))
                    .thenThrow(ParserConfigurationException.class);
            exchangeRateService.getAllRates("USD",null,null);

        }
    }

    @Test(expected = ResponseStatusException.class)
    public void getAllRatesException3Test()
    {
        try (MockedStatic<DocumentUtil> utilities = Mockito.mockStatic(DocumentUtil.class)) {
            utilities.when(() -> DocumentUtil.getDocumentFromRestAsXml(Mockito.any()))
                    .thenThrow(IOException.class);
            exchangeRateService.getAllRates("USD",null,null);

        }
    }

    @Test(expected = ResponseStatusException.class)
    public void getAllRatesException4Test()
    {
        try (MockedStatic<DocumentUtil> utilities = Mockito.mockStatic(DocumentUtil.class)) {
            utilities.when(() -> DocumentUtil.getDocumentFromRestAsXml(Mockito.any()))
                    .thenThrow(SAXException.class);
            exchangeRateService.getAllRates("USD",null,null);

        }
    }

    @Test(expected = ResponseStatusException.class)
    public void convertRateException1Test()
    {
        try (MockedStatic<DocumentUtil> utilities = Mockito.mockStatic(DocumentUtil.class)) {
            utilities.when(() -> DocumentUtil.getDocumentFromRestAsXml(Mockito.any()))
                    .thenReturn(createDocumentForConvertRateNumberFormatExeption());
                    exchangeRateService.convertRate("USD","2022-03-01", BigDecimal.ONE);

        }
    }

    @Test(expected = ResponseStatusException.class)
    public void convertRateException2Test()
    {
        try (MockedStatic<DocumentUtil> utilities = Mockito.mockStatic(DocumentUtil.class)) {
            utilities.when(() -> DocumentUtil.getDocumentFromRestAsXml(Mockito.any()))
                    .thenReturn(createDocumentForConvertRateEmptyValueException());
            exchangeRateService.convertRate("USD","2022-03-01", BigDecimal.ONE);

        }
    }

    private Document createDocumentForExchangeRates() {
        try {
            String xmlFile = "<?xml version=\"1.0\" ?><message:GenericData xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:generic=\"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/data/generic\" xmlns:message=\"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/message\" xmlns:common=\"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common\" xsi:schemaLocation=\"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common https://registry.sdmx.org/schemas/v2_1/SDMXCommon.xsd http://www.sdmx.org/resources/sdmxml/schemas/v2_1/message https://registry.sdmx.org/schemas/v2_1/SDMXMessage.xsd http://www.sdmx.org/resources/sdmxml/schemas/v2_1/data/generic https://registry.sdmx.org/schemas/v2_1/SDMXDataGeneric.xsd\"><message:Header><message:ID>BBEX3_D_USD_EUR_BB_AC_000</message:ID><message:Test>false</message:Test><message:Prepared>2022-04-08T15:59:50+02:00</message:Prepared><message:Sender id=\"BBK\"><common:Name xml:lang=\"de\">Deutsche Bundesbank</common:Name><message:Contact><message:Email>presse-information@bundesbank.de</message:Email></message:Contact></message:Sender><message:Structure structureID=\"BBK_ERX\" dimensionAtObservation=\"TIME_PERIOD\"><common:Structure><URN>urn:sdmx:org.sdmx.infomodel.datastructure.DataStructure=BBK:BBK_ERX(1.0)</URN></common:Structure></message:Structure></message:Header><message:DataSet structureRef=\"BBK_ERX\" setID=\"BBEX3\" action=\"Replace\" validFromDate=\"2022-04-08T15:59:50.688+02:00\"><generic:Series><generic:SeriesKey><generic:Value id=\"BBK_STD_FREQ\" value=\"D\"></generic:Value><generic:Value id=\"BBK_STD_CURRENCY\" value=\"USD\"></generic:Value><generic:Value id=\"BBK_ERX_PARTNER_CURRENCY\" value=\"EUR\"></generic:Value><generic:Value id=\"BBK_ERX_SERIES_TYPE\" value=\"BB\"></generic:Value><generic:Value id=\"BBK_ERX_RATE_TYPE\" value=\"AC\"></generic:Value><generic:Value id=\"BBK_ERX_SUFFIX\" value=\"000\"></generic:Value></generic:SeriesKey><generic:Obs><generic:ObsDimension value=\"2016-07-25\"></generic:ObsDimension><generic:ObsValue value=\"1.0982\"></generic:ObsValue><generic:Attributes><generic:Value id=\"BBK_DIFF\" value=\"-0.3\"></generic:Value></generic:Attributes></generic:Obs><generic:Obs><generic:ObsDimension value=\"2016-07-26\"></generic:ObsDimension><generic:ObsValue value=\"1.0997\"></generic:ObsValue><generic:Attributes><generic:Value id=\"BBK_DIFF\" value=\"0.1\"></generic:Value></generic:Attributes></generic:Obs><generic:Obs><generic:ObsDimension value=\"2016-07-27\"></generic:ObsDimension><generic:ObsValue value=\"1.0991\"></generic:ObsValue><generic:Attributes><generic:Value id=\"BBK_DIFF\" value=\"-0.1\"></generic:Value></generic:Attributes></generic:Obs><generic:Obs><generic:ObsDimension value=\"2016-07-28\"></generic:ObsDimension><generic:ObsValue value=\"1.1090\"></generic:ObsValue><generic:Attributes><generic:Value id=\"BBK_DIFF\" value=\"0.9\"></generic:Value></generic:Attributes></generic:Obs><generic:Obs><generic:ObsDimension value=\"2016-07-29\"></generic:ObsDimension><generic:ObsValue value=\"1.1113\"></generic:ObsValue><generic:Attributes><generic:Value id=\"BBK_DIFF\" value=\"0.2\"></generic:Value></generic:Attributes></generic:Obs><generic:Obs><generic:ObsDimension value=\"2016-07-30\"></generic:ObsDimension><generic:Attributes><generic:Value id=\"OBS_STATUS\" value=\"K\"></generic:Value></generic:Attributes></generic:Obs><generic:Obs><generic:ObsDimension value=\"2016-07-31\"></generic:ObsDimension><generic:Attributes><generic:Value id=\"OBS_STATUS\" value=\"K\"></generic:Value></generic:Attributes></generic:Obs><generic:Obs><generic:ObsDimension value=\"2016-08-01\"></generic:ObsDimension><generic:ObsValue value=\"1.1164\"></generic:ObsValue><generic:Attributes><generic:Value id=\"BBK_DIFF\" value=\"0.5\"></generic:Value></generic:Attributes></generic:Obs></generic:Series></message:DataSet></message:GenericData>";
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final InputSource is = new InputSource(new StringReader(xmlFile));
            return builder.parse(is);
        } catch (Exception ex) {
        }
        return null;
    }

    private Document createDocumentForConvertRate() {
        try {
            String xmlFile = "<?xml version=\"1.0\" ?><message:GenericData xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:generic=\"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/data/generic\" xmlns:message=\"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/message\" xmlns:common=\"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common\" xsi:schemaLocation=\"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common https://registry.sdmx.org/schemas/v2_1/SDMXCommon.xsd http://www.sdmx.org/resources/sdmxml/schemas/v2_1/message https://registry.sdmx.org/schemas/v2_1/SDMXMessage.xsd http://www.sdmx.org/resources/sdmxml/schemas/v2_1/data/generic https://registry.sdmx.org/schemas/v2_1/SDMXDataGeneric.xsd\"><message:Header><message:ID>BBEX3_D_TRY_EUR_BB_AC_000</message:ID><message:Test>false</message:Test><message:Prepared>2022-04-08T15:59:50+02:00</message:Prepared><message:Sender id=\"BBK\"><common:Name xml:lang=\"de\">Deutsche Bundesbank</common:Name><message:Contact><message:Email>presse-information@bundesbank.de</message:Email></message:Contact></message:Sender><message:Structure structureID=\"BBK_ERX\" dimensionAtObservation=\"TIME_PERIOD\"><common:Structure><URN>urn:sdmx:org.sdmx.infomodel.datastructure.DataStructure=BBK:BBK_ERX(1.0)</URN></common:Structure></message:Structure></message:Header><message:DataSet structureRef=\"BBK_ERX\" setID=\"BBEX3\" action=\"Replace\" validFromDate=\"2022-04-08T15:59:50.535+02:00\"><generic:Series><generic:SeriesKey><generic:Value id=\"BBK_STD_FREQ\" value=\"D\"/><generic:Value id=\"BBK_STD_CURRENCY\" value=\"TRY\"/><generic:Value id=\"BBK_ERX_PARTNER_CURRENCY\" value=\"EUR\"/><generic:Value id=\"BBK_ERX_SERIES_TYPE\" value=\"BB\"/><generic:Value id=\"BBK_ERX_RATE_TYPE\" value=\"AC\"/><generic:Value id=\"BBK_ERX_SUFFIX\" value=\"000\"/></generic:SeriesKey><generic:Obs><generic:ObsDimension value=\"2022-03-01\"/><generic:ObsValue value=\"1\"/><generic:Attributes><generic:Value id=\"BBK_DIFF\" value=\"0.6\"/></generic:Attributes></generic:Obs></generic:Series></message:DataSet></message:GenericData>";
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final InputSource is = new InputSource(new StringReader(xmlFile));
            return builder.parse(is);
        } catch (Exception ex) {
        }
        return null;
    }

    private Document createDocumentForConvertRateNumberFormatExeption() {
        try {
            String xmlFile = "<?xml version=\"1.0\" ?><message:GenericData xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:generic=\"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/data/generic\" xmlns:message=\"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/message\" xmlns:common=\"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common\" xsi:schemaLocation=\"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common https://registry.sdmx.org/schemas/v2_1/SDMXCommon.xsd http://www.sdmx.org/resources/sdmxml/schemas/v2_1/message https://registry.sdmx.org/schemas/v2_1/SDMXMessage.xsd http://www.sdmx.org/resources/sdmxml/schemas/v2_1/data/generic https://registry.sdmx.org/schemas/v2_1/SDMXDataGeneric.xsd\"><message:Header><message:ID>BBEX3_D_TRY_EUR_BB_AC_000</message:ID><message:Test>false</message:Test><message:Prepared>2022-04-08T15:59:50+02:00</message:Prepared><message:Sender id=\"BBK\"><common:Name xml:lang=\"de\">Deutsche Bundesbank</common:Name><message:Contact><message:Email>presse-information@bundesbank.de</message:Email></message:Contact></message:Sender><message:Structure structureID=\"BBK_ERX\" dimensionAtObservation=\"TIME_PERIOD\"><common:Structure><URN>urn:sdmx:org.sdmx.infomodel.datastructure.DataStructure=BBK:BBK_ERX(1.0)</URN></common:Structure></message:Structure></message:Header><message:DataSet structureRef=\"BBK_ERX\" setID=\"BBEX3\" action=\"Replace\" validFromDate=\"2022-04-08T15:59:50.535+02:00\"><generic:Series><generic:SeriesKey><generic:Value id=\"BBK_STD_FREQ\" value=\"D\"/><generic:Value id=\"BBK_STD_CURRENCY\" value=\"TRY\"/><generic:Value id=\"BBK_ERX_PARTNER_CURRENCY\" value=\"EUR\"/><generic:Value id=\"BBK_ERX_SERIES_TYPE\" value=\"BB\"/><generic:Value id=\"BBK_ERX_RATE_TYPE\" value=\"AC\"/><generic:Value id=\"BBK_ERX_SUFFIX\" value=\"000\"/></generic:SeriesKey><generic:Obs><generic:ObsDimension value=\"2022-03-01\"/><generic:ObsValue value=\"s\"/><generic:Attributes><generic:Value id=\"BBK_DIFF\" value=\"0.6\"/></generic:Attributes></generic:Obs></generic:Series></message:DataSet></message:GenericData>";
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final InputSource is = new InputSource(new StringReader(xmlFile));
            return builder.parse(is);
        } catch (Exception ex) {
        }
        return null;
    }

    private Document createDocumentForConvertRateEmptyValueException() {
        try {
            String xmlFile = "<?xml version=\"1.0\" ?><message:GenericData xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:generic=\"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/data/generic\" xmlns:message=\"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/message\" xmlns:common=\"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common\" xsi:schemaLocation=\"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common https://registry.sdmx.org/schemas/v2_1/SDMXCommon.xsd http://www.sdmx.org/resources/sdmxml/schemas/v2_1/message https://registry.sdmx.org/schemas/v2_1/SDMXMessage.xsd http://www.sdmx.org/resources/sdmxml/schemas/v2_1/data/generic https://registry.sdmx.org/schemas/v2_1/SDMXDataGeneric.xsd\"><message:Header><message:ID>BBEX3_D_TRY_EUR_BB_AC_000</message:ID><message:Test>false</message:Test><message:Prepared>2022-04-08T15:59:50+02:00</message:Prepared><message:Sender id=\"BBK\"><common:Name xml:lang=\"de\">Deutsche Bundesbank</common:Name><message:Contact><message:Email>presse-information@bundesbank.de</message:Email></message:Contact></message:Sender><message:Structure structureID=\"BBK_ERX\" dimensionAtObservation=\"TIME_PERIOD\"><common:Structure><URN>urn:sdmx:org.sdmx.infomodel.datastructure.DataStructure=BBK:BBK_ERX(1.0)</URN></common:Structure></message:Structure></message:Header><message:DataSet structureRef=\"BBK_ERX\" setID=\"BBEX3\" action=\"Replace\" validFromDate=\"2022-04-08T15:59:50.535+02:00\"><generic:Series><generic:SeriesKey><generic:Value id=\"BBK_STD_FREQ\" value=\"D\"/><generic:Value id=\"BBK_STD_CURRENCY\" value=\"TRY\"/><generic:Value id=\"BBK_ERX_PARTNER_CURRENCY\" value=\"EUR\"/><generic:Value id=\"BBK_ERX_SERIES_TYPE\" value=\"BB\"/><generic:Value id=\"BBK_ERX_RATE_TYPE\" value=\"AC\"/><generic:Value id=\"BBK_ERX_SUFFIX\" value=\"000\"/></generic:SeriesKey><generic:Obs><generic:ObsDimension value=\"2022-03-01\"/><generic:Attributes><generic:Value id=\"BBK_DIFF\" value=\"0.6\"/></generic:Attributes></generic:Obs></generic:Series></message:DataSet></message:GenericData>";
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final InputSource is = new InputSource(new StringReader(xmlFile));
            return builder.parse(is);
        } catch (Exception ex) {
        }
        return null;
    }

}
