package edu.upc.essi.catalog.IO.python;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;

public final class SolverCaller {

	public static JSONObject getMissRates() {
		JSONObject jsonObject =null;
		try {

			URL url = new URL("http://127.0.0.1:5000/api/v1/resources/books/all");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

			String output;
//			System.out.println("Output from Server .... \n");
			String x = br.lines().collect(Collectors.joining());
			
			
//			System.out.println(x);

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
