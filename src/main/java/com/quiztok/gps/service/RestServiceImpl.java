package com.quiztok.gps.service;

import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class RestServiceImpl implements  RestService {




    public ResponseEntity getRestGetService(String url , HashMap params ){

        MultiValueMap<String, String> paramsMap= new LinkedMultiValueMap();
        paramsMap.setAll(params);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity request = new HttpEntity(httpHeaders);
        UriComponentsBuilder targetUrl  = UriComponentsBuilder
                .fromUriString(url)
                .queryParams(paramsMap);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> txResult = restTemplate.exchange(targetUrl.toUriString(), HttpMethod.GET,request,Map.class);
        return txResult;
    }

    public ResponseEntity getRestPostService(String url , HashMap params ){

        MultiValueMap<String, String> paramsMap= new LinkedMultiValueMap();
        paramsMap.setAll(params);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        System.out.println("param: "+paramsMap);

        HttpEntity request = new HttpEntity(httpHeaders);
        UriComponentsBuilder targetUrl  = UriComponentsBuilder
                .fromUriString(url)
                .queryParams(paramsMap);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> txResult = restTemplate.exchange(targetUrl.toUriString(), HttpMethod.POST,request,Map.class);

        return txResult;
    }

    public ResponseEntity getRestGetListService(String url , HashMap params ){

        MultiValueMap<String, String> paramsMap= new LinkedMultiValueMap();
        paramsMap.setAll(params);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity request = new HttpEntity(httpHeaders);
        UriComponentsBuilder targetUrl  = UriComponentsBuilder
                .fromUriString(url)
                .queryParams(paramsMap);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> txResult = restTemplate.exchange(targetUrl.toUriString(), HttpMethod.GET,request,Object.class);
        return txResult;
    }

    public ResponseEntity getRestGetHeaderService(String url , MultiValueMap params, HashMap  httpHeaders){

        MultiValueMap<String, String> paramsMap= new LinkedMultiValueMap();
        paramsMap.setAll(params);
        HttpEntity<String> entity = new HttpEntity<String>(setRestHeader(httpHeaders));
        ResponseEntity<Map> response = new RestTemplate().exchange(setUriBuilder(url, params), HttpMethod.GET,
                entity, Map.class);
        return response;
    }

    private String setUriBuilder(String url, MultiValueMap<String, String> params) {
        if (!params.isEmpty()) {
            return UriComponentsBuilder.fromUriString(url).queryParams(params).build(true).toUri().toString();
        }
        return url;
    }
    public ResponseEntity getRestPostHeaderService(String url , HashMap params, HttpHeaders httpHeaders ){

        MultiValueMap<String, String> paramsMap= new LinkedMultiValueMap();
        paramsMap.setAll(params);

        HttpEntity request = new HttpEntity(httpHeaders);
        UriComponentsBuilder targetUrl  = UriComponentsBuilder
                .fromUriString(url)
                .queryParams(paramsMap);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> txResult = restTemplate.exchange(targetUrl.toUriString(), HttpMethod.POST,request,Map.class);

        return txResult;
    }

    public String getRestPostJsonService(String url , JSONObject params ){


        System.out.println(params);;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        HttpEntity<String> request = new HttpEntity<String>(params.toString(), httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        String txResult = restTemplate.postForObject(url, request, String.class);
        System.out.println(txResult);

        return txResult;
    }

    private HttpHeaders setRestHeader(HashMap headerInfo) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        headerInfo.forEach((key, value) -> {
            headers.set(key.toString(), value.toString());
        });
        return headers;
    }



}
