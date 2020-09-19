package ca.datamagic.station.servlet;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ca.datamagic.station.dao.StationDAO;
import ca.datamagic.station.dto.StationDTO;

public class StationServletTester {
	private static final Logger logger = Logger.getLogger(StationServletTester.class.getName());
	private static final Gson gson = new Gson();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		StationDAO.setInstance(new StationDAO("src/test/resources/data/stations.csv"));
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testList() throws Exception {
		String pathInfo = "/list";
		StringWriter textWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(textWriter);
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		Mockito.doAnswer(new Answer<Object>() {
		    @Override
		    public Object answer(InvocationOnMock invocation) throws Throwable {
		        return pathInfo;
		    }
		}).when(request).getPathInfo();
		Mockito.doAnswer(new Answer<Object>() {
		    @Override
		    public Object answer(InvocationOnMock invocation) throws Throwable {
		        return printWriter;
		    }
		}).when(response).getWriter();
		StationServlet servlet = new StationServlet();
		servlet.doGet(request, response);
		String responseText = textWriter.toString();
		logger.info("responseText: " + responseText);
		TypeToken<List<StationDTO>> typeToken = new TypeToken<List<StationDTO>>(){};
		List<StationDTO> stations = gson.fromJson(responseText, typeToken.getType());
		Assert.assertEquals(2349, stations.size());
	}
	
	@Test
	public void testReadById() throws Exception {
		String pathInfo = "/KCGS";
		StringWriter textWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(textWriter);
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		Mockito.doAnswer(new Answer<Object>() {
		    @Override
		    public Object answer(InvocationOnMock invocation) throws Throwable {
		        return pathInfo;
		    }
		}).when(request).getPathInfo();
		Mockito.doAnswer(new Answer<Object>() {
		    @Override
		    public Object answer(InvocationOnMock invocation) throws Throwable {
		        return printWriter;
		    }
		}).when(response).getWriter();
		StationServlet servlet = new StationServlet();
		servlet.doGet(request, response);
		String responseText = textWriter.toString();
		logger.info("responseText: " + responseText);
		StationDTO station = gson.fromJson(responseText, StationDTO.class);
		Assert.assertEquals("KCGS", station.getStationId());
	}
	
	@Test
	public void testReadByLocation() throws Exception {
		String pathInfo = "/38.9806/-76.9223/coordinates";
		StringWriter textWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(textWriter);
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		Mockito.doAnswer(new Answer<Object>() {
		    @Override
		    public Object answer(InvocationOnMock invocation) throws Throwable {
		        return pathInfo;
		    }
		}).when(request).getPathInfo();
		Mockito.doAnswer(new Answer<Object>() {
		    @Override
		    public Object answer(InvocationOnMock invocation) throws Throwable {
		        return printWriter;
		    }
		}).when(response).getWriter();
		StationServlet servlet = new StationServlet();
		servlet.doGet(request, response);
		String responseText = textWriter.toString();
		logger.info("responseText: " + responseText);
		StationDTO station = gson.fromJson(responseText, StationDTO.class);
		Assert.assertEquals("KCGS", station.getStationId());
	}
}
