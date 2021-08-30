package project.covidtracker.services;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.jmx.export.naming.IdentityNamingStrategy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import project.covidtracker.models.LocationData;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CovidDataService {

    private static final String COVID_DATA = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    private int vietnamCase;
    private List<LocationData> allData = new ArrayList<>();
    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchCovidData() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(COVID_DATA))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        StringReader csvDataReader = new StringReader(response.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvDataReader);

        List<LocationData> newData = new ArrayList<>();
        for(CSVRecord record : records){
            LocationData currentData = new LocationData();
            currentData.setProvince(record.get("Province/State"));
            currentData.setCountry(record.get("Country/Region"));
            currentData.setLatestCaseNumber(Integer.parseInt(record.get(record.size()-1)));
            if(currentData.getCountry().equals("Vietnam")){
                vietnamCase = currentData.getLatestCaseNumber();
            }
            newData.add(currentData);
        }
        this.allData=newData;

    }

    public int getVietnamCase() {
        return vietnamCase;
    }

    public List<LocationData> getAllData() {
        return allData;
    }
}
