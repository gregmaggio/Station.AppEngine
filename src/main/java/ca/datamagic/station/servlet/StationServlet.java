/**
 * 
 */
package ca.datamagic.station.servlet;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import ca.datamagic.station.dao.StationDAO;
import ca.datamagic.station.dto.StationDTO;

/**
 * @author Greg
 *
 */
public class StationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(StationServlet.class.getName());
	private static final Pattern readByCoordinatesPattern = Pattern.compile("/(?<latitude>[+-]?([0-9]*[.])?[0-9]+)/(?<longitude>[+-]?([0-9]*[.])?[0-9]+)/coordinates", Pattern.CASE_INSENSITIVE);
	private static final Pattern listPattern = Pattern.compile("/list", Pattern.CASE_INSENSITIVE);
	private static final Pattern readPattern = Pattern.compile("/(?<identifier>\\w+)", Pattern.CASE_INSENSITIVE);
	private static final double withinMeters = 10000;
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String pathInfo = request.getPathInfo();
			logger.info("pathInfo: " + pathInfo);
			Matcher readByCoordinatesMatcher = readByCoordinatesPattern.matcher(pathInfo);
			if (readByCoordinatesMatcher.find()) {
				logger.info("readByCoordinates");
				String latitude = readByCoordinatesMatcher.group("latitude");
				logger.info("latitude: " + latitude);
				String longitude = readByCoordinatesMatcher.group("longitude");
				logger.info("longitude: " + longitude);
				double doubleLatitude = Double.parseDouble(latitude);
				double doubleLongitude = Double.parseDouble(longitude);
				StationDAO dao = StationDAO.getInstance();
				StationDTO station = dao.readByLocation(doubleLatitude, doubleLongitude, withinMeters);
				Gson gson = new Gson();
				String json = gson.toJson(station);
				response.setContentType("application/json");
				response.getWriter().println(json);
				return;
			}
			Matcher listMatcher = listPattern.matcher(pathInfo);
			if (listMatcher.find()) {
				logger.info("list");
				StationDAO dao = StationDAO.getInstance();
				List<StationDTO> stations = dao.list();
				Gson gson = new Gson();
				String json = gson.toJson(stations);
				response.setContentType("application/json");
				response.getWriter().println(json);
				return;
			}
			Matcher readMatcher = readPattern.matcher(pathInfo);
			if (readMatcher.find()) {
				logger.info("read");
				String identifier = readMatcher.group("identifier");
				logger.info("identifier: " + identifier);
				StationDAO dao = StationDAO.getInstance();
				StationDTO station = dao.readById(identifier);
				Gson gson = new Gson();
				String json = gson.toJson(station);
				response.setContentType("application/json");
				response.getWriter().println(json);
				return;
			}
			response.sendError(403);
		} catch (Throwable t) {
			logger.log(Level.SEVERE, "Throwable", t);
			throw new IOException("Exception", t);
		}
	}
}
