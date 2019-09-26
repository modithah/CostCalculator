import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class jsontest {
	public static void main(String[] args) throws JSONException, IOException {

		JSONObject jo = new JSONObject(
				new String(Files.readAllBytes(Paths.get("C:\\data\\catalog.json")), StandardCharsets.UTF_8));

//		System.out.println(jo.get("relationships"));

		JSONArray atoms = jo.getJSONArray("atoms");

		for (int i = 0; i < atoms.length(); i++) {
			JSONObject keyatom = atoms.getJSONObject(i);
			Iterator<String> keys = keyatom.keys();
			while (keys.hasNext()) {
				String key = keys.next(); // name of the class atom (only one)
				System.out.println(key + "-----");
				System.out.println(keyatom.getJSONObject(key).names());
				Iterator<String> keys2 = keyatom.getJSONObject(key).keys();
				String id = "";
				
				while (keys2.hasNext()) { // all other atoms
					String keyn = keys2.next();

					if (keyn.contains("*")) {
						System.out.println(keyn);
					}
					else {
						
					}

				}
			}

		}
	}
}
