package main.java.viewmodels;

import main.java.interfaces.IStationViewModel;
import main.java.models.Station;
import main.java.models.ZoneType;

import java.util.*;

public class StationViewModel implements IStationViewModel {
    private Map<String, Station> stations;

    public StationViewModel() {
        this.stations = new HashMap();
        this.initViewModel();
    }

    public List<Station> getStations(){
        List<Station> result = new ArrayList();
        this.stations.values().forEach((station) -> {
            result.add(station);
        });

        return result;
    }

    public boolean hasStation(String stationName) {
        return this.stations.containsKey(stationName);
    }

    public Station getStationByName(String stationName){
        return this.stations.get(stationName);
    }

    private void initViewModel(){
        Station station = new Station(1,"Central", ZoneType.ZONE1);
        this.stations.put(station.getName(), station);

        station = new Station(2, "Flagstaff", ZoneType.ZONE1);
        this.stations.put(station.getName(), station);

        station = new Station(3, "Richmond", ZoneType.ZONE1);
        this.stations.put(station.getName(), station);

        station = new Station(4, "Lilydale", ZoneType.ZONE1_2);
        this.stations.put(station.getName(), station);

        station = new Station(5, "Epping", ZoneType.ZONE1_2);
        this.stations.put(station.getName(), station);
    }
}
