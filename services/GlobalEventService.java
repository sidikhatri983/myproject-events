package com.even.gestion.services;

import com.even.gestion.models.GlobalEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.util.*;

@Service
public class GlobalEventService {

    @Value("${eventbrite.token}")
    private String eventbriteToken;

    private final RestTemplate restTemplate;

    private final String EVENTBRITE_URL = "https://www.eventbriteapi.com/v3/events/search/";

    public GlobalEventService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<GlobalEvent> getEventsByCity(String city) {
        List<GlobalEvent> events = new ArrayList<>();
        try {
            String uri = UriComponentsBuilder.fromHttpUrl(EVENTBRITE_URL)
                    .queryParam("location.address", city)
                    .queryParam("expand", "venue")
                    .toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + eventbriteToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class).getBody();

            JSONObject jsonObject = new JSONObject(response);
            JSONArray eventsArray = jsonObject.getJSONArray("events");

            for (int i = 0; i < eventsArray.length(); i++) {
                JSONObject e = eventsArray.getJSONObject(i);
                String title = e.getJSONObject("name").optString("text", "No Title");
                String description = e.getJSONObject("description").optString("text", "");
                String start = e.getJSONObject("start").optString("local", "");
                String end = e.getJSONObject("end").optString("local", "");

                String location = "Global";
                if (e.has("venue") && !e.isNull("venue")) {
                    JSONObject venue = e.getJSONObject("venue");
                    if (venue.has("address") && !venue.isNull("address")) {
                        JSONObject address = venue.getJSONObject("address");
                        location = address.optString("localized_address_display", "Global");
                    }
                }

                events.add(new GlobalEvent(title, description, start, end, false));
            }

        } catch (Exception e) {
            System.out.println("Error fetching events: " + e.getMessage());
        }
        return events;
    }
}
