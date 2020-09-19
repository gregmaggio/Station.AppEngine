package ca.datamagic.station.dao;

import java.util.List;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

import ca.datamagic.station.dto.StationDTO;

public class StationDAOTester {
	private static final Logger logger = Logger.getLogger(StationDAOTester.class.getName());
	private static final Gson gson = new Gson();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		StationDAO.setInstance(new StationDAO("src/test/resources/data/stations.csv"));
	}

	@Test
	public void testList() throws Exception {
		StationDAO dao = StationDAO.getInstance();
		List<StationDTO> stations = dao.list();
		logger.info("stations: " + gson.toJson(stations));
		Assert.assertEquals(2349, stations.size());
	}

	@Test
	public void testReadById() throws Exception {
		StationDAO dao = StationDAO.getInstance();
		StationDTO station = dao.readById("KCGS");
		Assert.assertNotNull(station);
		logger.info("station: " + gson.toJson(station));
		Assert.assertEquals("KCGS", station.getStationId());
	}

	@Test
	public void testReadByLocation() throws Exception {
		StationDAO dao = StationDAO.getInstance();
		StationDTO station = dao.readByLocation(38.9806,-76.9223, 10000);
		Assert.assertNotNull(station);
		logger.info("station: " + gson.toJson(station));
		Assert.assertEquals("KCGS", station.getStationId());
	}
}
