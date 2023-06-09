package backend.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Slf4j
public class BuildUrl {

    private String sidoName;
    private String endPoint;
    private String serviceKey;

    //ParticulateMatter에서 사용할 buildUrl 생성자
    public static String buildUrl(String serviceKey, String endPoint, String sidoName) throws UnsupportedEncodingException {

        StringBuilder urlBuilder = new StringBuilder(endPoint); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("sidoName","UTF-8") + "=" + URLEncoder.encode("서울", "UTF-8")); /*sidoName*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("returnType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("serviceKey","UTF-8") + "=" + serviceKey); /*serviceKey*/
        urlBuilder.append("&" + URLEncoder.encode("ver","UTF-8") + "=" + URLEncoder.encode("1.0", "UTF-8")); /*version*/
        return urlBuilder.toString();
    }

    //Weather에서 사용할 buildUrl 생성자
    public static String buildUrl(String serviceKey, String endPoint, String xCode, String yCode) throws UnsupportedEncodingException {

        String baseDate = getTimeForWeatherApi.getTodayDate();
        log.info("BuildUrl/buildUrl/baseDate = {} ", baseDate);

        StringBuilder urlBuilder = new StringBuilder(endPoint); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); /*발표일*/
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode("0600", "UTF-8")); /*06시 발표(정시단위) */
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(xCode, "UTF-8")); /*예보지점의 X 좌표값*/
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(yCode, "UTF-8")); /*예보지점의 Y 좌표값*/
        return urlBuilder.toString();
    }

    //미세먼지 데이터 빌더
    public static String buildPredictDataUrl(String serviceKey, String endPoint) throws UnsupportedEncodingException {

        String searchDate = getTimeForWeatherApi.getParSearchDate();
        log.info("BuildUrl/buildPredictDataUrl/searchDate = {} ", searchDate);

        StringBuilder urlBuilder = new StringBuilder(endPoint); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("100", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("returnType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("searchDate","UTF-8") + "=" + URLEncoder.encode(searchDate, "UTF-8"));
        return urlBuilder.toString();
    }

}
