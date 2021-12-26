package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.Cab;
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
import java.util.List;

// @Stateless annotation implies that the conversational state with the client shall not be maintained.
@Stateless
public class CabFacade extends AbstractFacade<Cab> {

    @PersistenceContext(unitName = "RideNowPU")
    private EntityManager entityManager;

    /*
    This constructor method invokes its parent AbstractFacade's constructor method,
    which in turn initializes its entity class type T and entityClass instance variable.
     */
    public CabFacade() {
        super(Cab.class);
    }

    // Obtain the object reference of the EntityManager instance in charge of
    // managing the entities in the persistence context identified above.
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    // get makes from the API
    public List<String> getMakesFromAPI() throws IOException, InterruptedException {
        List<String> makes = new ArrayList<String>();

        HttpClient httpClient = HttpClient.newHttpClient();
        String requestUri = "https://vpic.nhtsa.dot.gov/api/vehicles/getallmakes?format=json";

        HttpRequest getMakesReq = HttpRequest.newBuilder().GET().uri(URI.create(requestUri))
                .timeout(Duration.ofMillis(50000)).build();

        HttpResponse<String> getMakesRes = httpClient.send(getMakesReq, HttpResponse.BodyHandlers.ofString());

        String res = getMakesRes.body();

        JSONObject resultsJsonObject = new JSONObject(res);

        JSONArray arrMakes = resultsJsonObject.getJSONArray("Results");

        int sz = arrMakes.length();
        if (sz > 50) sz = 50;

        for(int i=0; i < sz ;i++){
            JSONObject aMakeObj = arrMakes.getJSONObject(i);
            String make = aMakeObj.getString("Make_Name");
            makes.add(make);
        }

        return makes;
    }

    // Get Models for make
    public List<String> getModelsForMake(String make) throws IOException, InterruptedException {
        List<String> models = new ArrayList<String>();
        HttpClient httpClient = HttpClient.newHttpClient();

        make = URLEncoder.encode(make, StandardCharsets.UTF_8).replace("+", "%20");
        String requestUri = "https://vpic.nhtsa.dot.gov/api/vehicles/getmodelsformake/" + make + "?format=json";

        HttpRequest getModelsReq = HttpRequest.newBuilder().GET().uri(URI.create(requestUri))
                .timeout(Duration.ofMillis(50000)).build();

        HttpResponse<String> getModelsRes = httpClient.send(getModelsReq, HttpResponse.BodyHandlers.ofString());

        String res = getModelsRes.body();

        JSONObject resultsJsonObject = new JSONObject(res);

        JSONArray arrModels = resultsJsonObject.getJSONArray("Results");

        int sz = arrModels.length();
        if (sz > 10) sz = 10;

        for(int i=0; i < sz; i++){
            JSONObject aModelObj = arrModels.getJSONObject(i);
            String model = aModelObj.getString("Model_Name");
            models.add(model);
        }

        return models;
    }

}
