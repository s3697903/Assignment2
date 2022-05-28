package main.java.interfaces;

import main.java.models.Station;

import java.util.List;

public interface IStationViewModel {
    List<Station> getStations();
    boolean hasStation(String stationName);
    Station getStationByName(String stationName);
}
