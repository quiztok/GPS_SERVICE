package com.quiztok.gps.service;

import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;

public interface RestService {
    public ResponseEntity getRestGetService(String url , HashMap params );
    public ResponseEntity getRestPostService(String url , HashMap params );
    public ResponseEntity getRestGetListService(String url , HashMap params );
    public ResponseEntity getRestGetHeaderService(String url , MultiValueMap params, HashMap  httpHeaders);
    public ResponseEntity getRestPostHeaderService(String url , HashMap params, HttpHeaders httpHeaders );

    public String getRestPostJsonService(String url , JSONObject params );

}
