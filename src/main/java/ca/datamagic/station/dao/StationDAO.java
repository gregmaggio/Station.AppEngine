/**
 * 
 */
package ca.datamagic.station.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.univocity.parsers.csv.CsvFormat;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import ca.datamagic.station.dto.StationDTO;

/**
 * @author Greg
 *
 */
public class StationDAO {
	private static final double radiusOfEarthMeters = 6371e3;
	private static StationDAO instance = null;
	private List<StationDTO> stations = new ArrayList<StationDTO>();
	private Map<String, Integer> stationsMap = new HashMap<String, Integer>();
	
	public StationDAO(String csvFileName) throws FileNotFoundException {
		CsvFormat format = new CsvFormat();
		format.setDelimiter(',');
		format.setLineSeparator("\n");
		format.setQuote('\"');
		CsvParserSettings settings = new CsvParserSettings();
		settings.setFormat(format);
		settings.setAutoClosingEnabled(true);
		CsvParser csvParser = new CsvParser(settings);
		List<String[]> lines = csvParser.parseAll(new FileInputStream(csvFileName));
		for (int ii = 1; ii < lines.size(); ii++) {
			String[] currentLineItems = lines.get(ii);
			String stationId = currentLineItems[0].toUpperCase();
			String stationName = currentLineItems[1];
			String state = currentLineItems[2];
			String wfo = currentLineItems[3];
			String radar = currentLineItems[4];
			String timeZoneId = currentLineItems[5];
			double latitude = Double.parseDouble(currentLineItems[6]);
			double longitude = Double.parseDouble(currentLineItems[7]);
			StationDTO station = new StationDTO();
			station.setStationId(stationId);
			station.setStationName(stationName);
			station.setState(state);
			station.setWFO(wfo);
			station.setRadar(radar);
			station.setTimeZoneId(timeZoneId);
			station.setLatitude(latitude);
			station.setLongitude(longitude);
			stations.add(station);
			stationsMap.put(stationId, ii - 1);
		}
	}
	
	public static synchronized StationDAO getInstance() {
		return instance;
	}
	
	public static synchronized void setInstance(StationDAO newVal) {
		instance = newVal;
	}
	
	public List<StationDTO> list() {
		return this.stations;
	}
	
	public StationDTO readById(String stationId) {
		if (stationId != null) {
			stationId = stationId.toUpperCase();
			if (this.stationsMap.containsKey(stationId)) {
				return this.stations.get(this.stationsMap.get(stationId));
			}
		}
		return null;
	}
	
	public StationDTO readByLocation(double latitude, double longitude, double withinMeters) {
		StationDTO nearest = null;
        double nearestDistance = Double.NaN;
		for (int ii = 0; ii < this.stations.size(); ii++) {
			StationDTO station = this.stations.get(ii);
            double distanceToStation = computeDistance(latitude, longitude, station.getLatitude(), station.getLongitude());
            if (distanceToStation <= withinMeters) {
                if (nearest == null) {
                    nearest = station;
                    nearestDistance = distanceToStation;
                } else if (nearestDistance > distanceToStation) {
                    nearest = station;
                    nearestDistance = distanceToStation;
                }
            }
		}
		return nearest;
	}
	
	public static double computeDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
        double deltaLatitude = Math.toRadians(latitude2 - latitude1);
        double deltaLongitude = Math.toRadians(longitude2 - longitude1);
        latitude1 = Math.toRadians(latitude1);
        latitude2 = Math.toRadians(latitude2);
        double sinDeltaLatitudeOverTwo = Math.sin(deltaLatitude / 2);
        double sinDeltaLongitudeOverTwo = Math.sin(deltaLongitude / 2);
        double a = sinDeltaLatitudeOverTwo * sinDeltaLatitudeOverTwo +
                Math.cos(latitude1) * Math.cos(latitude2) *
                        sinDeltaLongitudeOverTwo * sinDeltaLongitudeOverTwo;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = radiusOfEarthMeters * c;
        return distance;
    }
}
