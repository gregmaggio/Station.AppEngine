/**
 * 
 */
package ca.datamagic.station.servlet;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import ca.datamagic.station.dao.StationDAO;

/**
 * @author Greg
 *
 */
public class StationContextListener implements ServletContextListener {
	private static final Logger logger = Logger.getLogger(StationContextListener.class.getName());
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			String realPath = sce.getServletContext().getRealPath("/");
			String dataPath = MessageFormat.format("{0}/WEB-INF/data", realPath);
			String csvFileName = MessageFormat.format("{0}/stations.csv", dataPath);
			StationDAO.setInstance(new StationDAO(csvFileName));
		} catch (Throwable t) {
			logger.log(Level.SEVERE, "Throwable", t);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}
}
