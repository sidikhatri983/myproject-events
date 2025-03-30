package com.even.gestion.services;

import com.even.gestion.models.GlobalEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TicketmasterService {
    private final RestTemplate restTemplate;

    @Value("${ticketmaster.api.key}")
    private String apiKey;

    @Value("${ticketmaster.api.url}")
    private String apiUrl;

    public TicketmasterService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<GlobalEvent> getEventsByCity(String city) {
        String url = apiUrl + "?apikey=" + apiKey + "&city=" + city;

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        if (response.getBody() != null && response.getBody().containsKey("_embedded")) {
            List<Map<String, Object>> eventData = (List<Map<String, Object>>)
                    ((Map<String, Object>) response.getBody().get("_embedded")).get("events");

            return eventData.stream().map(event -> {
                String title = (String) event.get("name");
                String location = "Unknown Location";
                if (event.containsKey("_embedded") && ((Map<String, Object>) event.get("_embedded")).containsKey("venues")) {
                    location = (String) ((List<Map<String, Object>>) ((Map<String, Object>) event.get("_embedded")).get("venues"))
                            .get(0).get("name");
                }
                String dateStart = "Unknown Date";
                if (event.containsKey("dates")) {
                    dateStart = (String) ((Map<String, Object>) ((Map<String, Object>) event.get("dates")).get("start")).get("localDate");
                }
                String urlEvent = (String) event.get("url");

                return new GlobalEvent(title, location, dateStart, dateStart, false, urlEvent);
            }).collect(Collectors.toList());
        }
        return List.of();
    }
}
