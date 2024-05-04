package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.Account;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;

import java.math.BigDecimal;
import java.util.List;

public class AccountService {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;

    }
    public Account findAccountByUserId(int userId){

        Account account = null;

        try{
            ResponseEntity<Account> response = restTemplate.exchange(API_BASE_URL +
                    "account/" + userId, HttpMethod.GET, makeAuthEntity(), Account.class);
            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException ex){
            BasicLogger.log(ex.getMessage());
        }
            return account;
    }






    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }











}
