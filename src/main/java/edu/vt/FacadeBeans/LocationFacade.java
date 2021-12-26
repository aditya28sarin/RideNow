package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.Location;
import edu.vt.globals.Constants;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;

// @Stateless annotation implies that the conversational state with the client shall not be maintained.
@Stateless
public class LocationFacade extends AbstractFacade<Location> {

    @PersistenceContext(unitName = "RideNowPU")
    private EntityManager entityManager;

    /*
    This constructor method invokes its parent AbstractFacade's constructor method,
    which in turn initializes its entity class type T and entityClass instance variable.
     */
    public LocationFacade() {
        super(Location.class);
    }

    // Obtain the object reference of the EntityManager instance in charge of
    // managing the entities in the persistence context identified above.
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }


    // generate Static Map Image Url
    public String generateStaticMapImageUrl(String startLocation, String destinationLocation) {
        String apiKey = Constants.GOOGLE_MAPS_API_KEY;
        String imageUrl = "https://maps.googleapis.com/maps/api/staticmap?";

        startLocation = URLEncoder.encode(startLocation, StandardCharsets.UTF_8);
        destinationLocation = URLEncoder.encode(destinationLocation, StandardCharsets.UTF_8);
//        imageUrl = imageUrl + "path=color:0xff0000ff|8th+Avenue+%26+34th+St,New+York,NY|8th+Avenue+%26+42nd+St,New+York,NY|Park+Ave+%26+42nd+St,New+York,NY,NY|Park+Ave+%26+34th+St,New+York,NY,NY&size=512x512&key=" + apiKey;

        imageUrl = imageUrl + "path=color:0xff0000ff|" + startLocation + "|" + destinationLocation + "&size=512x512&key=" + apiKey;
        return imageUrl;
    }

    // Calculate Distance
    public ArrayList<String> calculateDistance(String startLocation, String destinationLocation) throws IOException, InterruptedException {

        String response = GeocodeSync(startLocation, destinationLocation);

        // Instantiate a JSON object from the JSON data obtained
        JSONObject resultsJsonObject = new JSONObject(response);

        // Obtain a JSONArray of recipe objects (Each recipe is represented as a JSONObject)
        JSONArray jsonArrayRows = resultsJsonObject.getJSONArray("rows");

        JSONObject element = jsonArrayRows.getJSONObject(0);

        JSONArray jsonArrayElements = element.getJSONArray("elements");

        ArrayList<String> result = new ArrayList<String>();

        for (int i = 0; i < jsonArrayElements.length(); i++) {
            JSONObject jsonObject = jsonArrayElements.getJSONObject(i);

            JSONObject foundDistance = jsonObject.getJSONObject("distance");
            String distance = foundDistance.optString("text");
            result.add(distance);

            JSONObject foundDuration = jsonObject.getJSONObject("duration");
            String duration = foundDuration.optString("text");
            result.add(duration);
        }

        return result;
    }

    // Geocode Sync Function
    public String GeocodeSync(String startLocation, String destinationLocation) throws IOException, InterruptedException {

        HttpClient httpClient = HttpClient.newHttpClient();
        String geocodingAPIURL = "https://maps.googleapis.com/maps/api/distancematrix/json";

        startLocation = URLEncoder.encode(startLocation, StandardCharsets.UTF_8);
        destinationLocation = URLEncoder.encode(destinationLocation, StandardCharsets.UTF_8);

// url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=Boston%2CMA%7CCharlestown%2CMA&destinations=Lexington%2CMA%7CConcord%2CMA&departure_time=now&key=YOUR_API_KEY"
        String requestUri = geocodingAPIURL + "?origins=" + startLocation + "&destinations=" + destinationLocation + "&departure_time=now&key=" + Constants.GOOGLE_MAPS_API_KEY;

        HttpRequest geocodingRequest = HttpRequest.newBuilder().GET().uri(URI.create(requestUri))
                .timeout(Duration.ofMillis(5000)).build();

        HttpResponse<String> geocodingResponse = httpClient.send(geocodingRequest, HttpResponse.BodyHandlers.ofString());

        return geocodingResponse.body();
    }

}
