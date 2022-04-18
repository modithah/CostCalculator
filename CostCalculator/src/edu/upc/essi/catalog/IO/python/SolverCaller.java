package edu.upc.essi.catalog.IO.python;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SolverCaller {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
	public static JSONObject getMissRates() {
		JSONObject jsonObject =null;
		try {

			URL url = new URL("http://127.0.0.1:5000/api/v1/resources/books/all");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				logger.error("Failed : HTTP error code : "
						+ conn.getResponseCode());
				return jsonObject;
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

			String output;
//			logger.info("Output from Server .... \n");
			String x = br.lines().collect(Collectors.joining());
			
			
//			logger.info(x);

			 jsonObject = new JSONObject(x);
			conn.disconnect();
			
			

		  } catch (MalformedURLException e) {

			e.printStackTrace();

		  } catch (IOException e) {

			e.printStackTrace();

		  } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonObject;
	}

}
