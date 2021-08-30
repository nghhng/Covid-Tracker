package project.covidtracker.models;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LocationData {

    private String province;

    private String country;

    private int latestCaseNumber;



}
