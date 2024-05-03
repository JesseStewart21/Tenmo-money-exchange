package com.techelevator.tenmo.services;



import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TransferService {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public List<Transfer> findAllById(int accountId) {
        List<Transfer> transfers = new ArrayList<>();

        try {
            ResponseEntity<List<Transfer>> response = restTemplate.exchange(API_BASE_URL +
                    "/transfer/" + accountId, HttpMethod.GET, makeAuthEntity(), new ParameterizedTypeReference<List<Transfer>>() {

            });
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

public int createTransfer(Transfer transfer){
        int transferId = 0;

        try{
            ResponseEntity<Integer> response = restTemplate.exchange(API_BASE_URL +
                    "/transfer/new", HttpMethod.POST, makeAuthEntity(), Integer.class);
            transferId = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        } return transferId;
}







    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

    }
