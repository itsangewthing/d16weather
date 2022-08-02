package vttp2022.ssf.day16weather.services;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.ssf.models.Weather;

@Service
public class WeatherService {
    // CREATE URL WITH QUERY STRING
        private static final String URL = "https://api.openweathermap.org/data/2.5/weather?";
    // INJECT KEY
        @Value("${API_KEY}")
        private String key;

    public List<Weather> getWeather(String city){

        String url = UriComponentsBuilder.fromUriString(URL)
            .queryParam("q", city)
            .queryParam("appid",key)
            .toUriString();
        

// CREATE GET REQUEST, GET URL

// IF ITS A GET METHOD , WHAT'S THE QUERY PARAMETERS?

        RequestEntity<Void> req = RequestEntity.get(url).build();

// MAKE THE CALL TO OPENWEATHERMAP.ORG        
        RestTemplate template = new RestTemplate();

        // CHECK TEMPLATE, RETURN A STRING --> 
        ResponseEntity<String> resp = template.exchange(req, String.class);
        
// CHECK ERROR STATUS CODE         
        if(resp.getStatusCodeValue() !=200){
            System.err.println("Error status code is not 200");
            return Collections.emptyList();
        }

        // GET THE PAYLOAD AND DO SOMETHING ELSE WITH IT      
        String payload = resp.getBody();
        System.out.println("payload: " + payload);

//CONVERT PAYLOAD TO JSON-OBJECT()
// CONVERT THE STRING TO A READER 
    // S1 - CONSTRUCT A READER <-- convert the string to a reader
    Reader stringReader = new StringReader(payload);

    // create JSONREADER FROM READER
        JsonReader jsonReader = Json.createReader(stringReader);
    // READ THE PAYLOAD AS A JSON OBJECT 
        JsonObject weatherResult = jsonReader.readObject();

        JsonArray cities = weatherResult.getJsonArray("weather");
        

    // READ ARRAY: weather [0]    
        List<Weather> list = new LinkedList<>();
        for (int i = 0; i < cities.size(); i++) {
            JsonObject jo = cities.getJsonObject(i);
            Weather w = new Weather();
            w.setMain(jo.getString("main"));
            w.setDescription(jo.getString("description"));
            w.setIcon(jo.getString("icon"));
    
            list.add(w);

        }
        return list;
       }
    
}
