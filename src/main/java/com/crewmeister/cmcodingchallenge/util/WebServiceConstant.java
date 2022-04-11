package com.crewmeister.cmcodingchallenge.util;

public class WebServiceConstant {

    public static final String CURRENCY_URL = "https://api.statistiken.bundesbank.de/rest/metadata/codelist/BBK/CL_BBK_STD_CURRENCY?detail=allstubs&references=children";
    public static final String CURRENCY_TAG = "structure:Code";
    public static final String CURRENCY_TAG_NAME = "id";
    public static final String CURRENCY_LANG_TAG = "xml:lang";
    public static final String CURRENCY_LANG_NAME= "en";

    public static final String EXCHANGE_RATE_BASE_URL = "https://api.statistiken.bundesbank.de/rest/data/BBEX3/";
    public static final String EXCHANGE_RATE_DETAIL = "detail=dataonly";
    public static final String EXCHANGE_RATE_FREQUENCY = "D.";
    public static final String EXCHANGE_RATE_CURRENCY_DENOMINATOR = ".EUR";
    public static final String EXCHANGE_RATE_CONTENT_OF_TIME_SERIES = ".BB";
    public static final String EXCHANGE_RATE_RATE_TYPE = ".AC";
    public static final String EXCHANGE_RATE_SUFFIX = ".000";
    public static final String EXCHANGE_RATE_START_PERIOD = "startPeriod=";
    public static final String EXCHANGE_RATE_END_PERIOD= "endPeriod=";
    public static final String EXCHANGE_RATE_PARAMETER_PREFIX = "?";
    public static final String EXCHANGE_RATE_PARAMETER_AND = "&";

    public static final String EXCHANGE_RATE_TAG_NAME = "generic:Obs";
    public static final String EXCHANGE_RATE_DIMENSION = "generic:ObsDimension";
    public static final String EXCHANGE_RATE_DIMENSION_VALUE = "generic:ObsValue";
    public static final String EXCHANGE_RATE_VALUE = "value";


}
