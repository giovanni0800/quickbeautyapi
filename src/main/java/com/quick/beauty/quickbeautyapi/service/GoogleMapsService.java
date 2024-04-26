package com.quick.beauty.quickbeautyapi.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class GoogleMapsService {

    private final String apiKey = "AIzaSyARFYwMbB8xeGIgmukHDjcvQZFSct5EgX0";
    private final String url = "https://maps.googleapis.com/maps/api/distancematrix/json";

    private final RestTemplate restTemplate;

    public GoogleMapsService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String getMapsObject(String origins, String destinations) {
        String requestUrl = String.format("%s?origins=%s&destinations=%s&units=imperial&key=%s", url, origins, destinations, apiKey);
        ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
        return response.getBody();
    }
    
    public String getDistance(String responseBody) {
    	
    	try {
    		ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree( responseBody );
            
            JsonNode elementsNode = rootNode.path("rows").get(0).path("elements");
            JsonNode distanceNode = elementsNode.get(0).path("distance");
            
            String distanceText = distanceNode.get("text").asText();
            int distanceValue = distanceNode.get("value").asInt();
            
            System.out.println(distanceValue);
            
            return distanceText;
    	
    	} catch(Exception ex) {
    		return "We had a problem... " + ex.getMessage();
    	}
    	
    }
    
    //GETOBJECT
}
