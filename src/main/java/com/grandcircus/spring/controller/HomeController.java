package com.grandcircus.spring.controller;

import com.uber.sdk.rides.client.ServerTokenSession;
import com.uber.sdk.rides.client.SessionConfiguration;
import com.uber.sdk.rides.client.UberRidesApi;
import com.uber.sdk.rides.client.model.*;
import com.uber.sdk.rides.client.services.RidesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

@Controller
public class HomeController {
/*
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public String redirect() {
        return "redirect:results";
    }
*/

    @RequestMapping({"/"})
    public String displayForm() {
        return "welcome";
    }

    @RequestMapping(value = {"/result"},method = {RequestMethod.POST}
    )
    public ModelAndView route(@RequestParam("streetNum") String street, @RequestParam("routee") String routeM, @RequestParam("local") String loc, @RequestParam("postal") String post, @RequestParam("count") String count, @RequestParam("strtN") String strt, @RequestParam("rou") String rout, @RequestParam("loca") String local, @RequestParam("posta") String postal, @RequestParam("userCountry") String userCount) {
        String user = street + " " + routeM + " " + loc + " " + post + " " + count;
        String info = strt + " " + rout + " " + local + " " + postal + " " + userCount;
        return new ModelAndView("result", "addStuff", user + " " + info);
    }

    @RequestMapping(value = "/results")
    public String results(Model model) {
        List <Product> results;
        List <PriceEstimate> prices;
        List <TimeEstimate> duration;
        String id = "";

        try {
            //GOOGLE'S GEOCODE
/*                HttpClient http = HttpClientBuilder.create().build();
                HttpHost host = new HttpHost("maps.googleapis.com", 443, "https");
                HttpGet getPage = new HttpGet("/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&" +
                        "key=AIzaSyDCxhWezLy106rEfJyq-R6iPIYY0tK6lLw");
                HttpResponse resp = http.execute(host, getPage);

                String jsonString = EntityUtils.toString(resp.getEntity());

                JSONObject json = new JSONObject(jsonString);
                String out1 = json.get("results").toString();
                JSONObject ar = json.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").
                        getJSONObject("location");

                String lat = ar.get("lat").toString();
                Double latDouble = Double.valueOf(ar.get("lat").toString());

                String lng = ar.get("lng").toString();
                Double lngDouble = Double.valueOf(ar.get("lng").toString());*/
            //GOOGLE'S GEOCODE

            Coordinates results12 = GoogleGeocode.geocode("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA");

            //System.out.println("Results =" + results);
            //System.out.println("Lng =" + results12.longitude);
            //System.out.println("Lat =" + results.latitude);

            SessionConfiguration config = new SessionConfiguration.Builder ()
                    .setClientId ( "8RzoguxuX2ewBwxPa-lWFTbBUpOdsskI" )
                    .setServerToken ( "lmsYmf0NANVZcPTESB5mKYJsAy4nhdYgjgn7rtq1" )
                    .build ();
            ServerTokenSession session = new ServerTokenSession ( config );

            UberRidesApi ride = UberRidesApi.with ( session ).build ();
            RidesService service = ride.createService ();

            //Product
            Response <ProductsResponse> response = service.getProducts ( 42.335734f, -83.050031f ).execute ();
            //Response <ProductsResponse> response = service.getProducts ( 42.335734f, -83.050031f ).execute ();
            ProductsResponse products = response.body ();
            results = products.getProducts ();

            //Price
            Response <PriceEstimatesResponse> respond = service.getPriceEstimates ( 42.335734f, -83.050031f,
                    42.462633f, -82.891155f ).execute ();
            PriceEstimatesResponse priceTag = respond.body ();
            prices = priceTag.getPrices ();

            //Time
            Response <TimeEstimatesResponse> responseTime = service.getPickupTimeEstimate ( 42.335734f, -83.050031f,
                    id ).execute ();

            TimeEstimatesResponse time = responseTime.body ();
            duration = time.getTimes ();


            String displayName = results.get ( 0 ).getDisplayName ();
            String discript = results.get ( 0 ).getDescription ();
            int cap = results.get ( 0 ).getCapacity ();

            String priceEst = prices.get ( 0 ).getEstimate () + " " + prices.get ( 0 ).getCurrencyCode ();
            Float distance = prices.get ( 0 ).getDistance ();

            int seconds = duration.get ( 0 ).getEstimate ();
            int eta = (seconds % 3600) / 60;

            model.addAttribute ( "product", displayName );
            model.addAttribute ( "descrip", discript );
            model.addAttribute ( "cap", cap );
            model.addAttribute ( "price", priceEst );
            model.addAttribute ( "mile", distance );
            model.addAttribute ( "time", eta );

        } catch (IOException e) {
            e.printStackTrace ();
            //} catch (JSONException e) {
            //    e.printStackTrace();
        }

        return "summary";
    }
}
