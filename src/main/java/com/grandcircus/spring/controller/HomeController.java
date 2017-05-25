package com.grandcircus.spring.controller;

import com.uber.sdk.rides.client.ServerTokenSession;
import com.uber.sdk.rides.client.SessionConfiguration;
import com.uber.sdk.rides.client.UberRidesApi;
import com.uber.sdk.rides.client.model.*;
import com.uber.sdk.rides.client.services.RidesService;

import com.lyft.networking.ApiConfig;
import com.lyft.networking.LyftApiFactory;
import com.lyft.networking.apiObjects.*;
import com.lyft.networking.apis.LyftPublicApi;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import retrofit2.Call;
import retrofit2.Response;

import com.grandcircus.spring.util.routeMe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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



    @RequestMapping(value = "/summary", method = RequestMethod.POST)
    public String results(Model model, @RequestParam("streetNum") String street,
                          @RequestParam("routee") String routeM,
                          @RequestParam("local") String loc,
                          @RequestParam("postal") String post,
                          @RequestParam("count") String count,
                          @RequestParam("strtN") String strt,
                          @RequestParam("rou") String rout,
                          @RequestParam("loca") String local,
                          @RequestParam("posta") String postal,
                          @RequestParam("userCountry") String userCount ) {
        String fromAdd = street + " " + routeM + " " + loc + " " + post + " " + count;
        String toAdd = strt + " " + rout + " " + local + " " + postal + " " + userCount;
        List <Product> results;
        List <PriceEstimate> prices;
        List <TimeEstimate> duration;
        String id = "";

        try {

            Coordinates results12 = GoogleGeocode.geocode(fromAdd);
            float googleLat = (float)results12.latitude;
            float googleLong = (float)results12.longitude;

            Coordinates results13 = GoogleGeocode.geocode(toAdd);
            float googleLat2 = (float)results13.latitude;
            float googleLong2 = (float)results13.longitude;

            //Uber AppConfig
            SessionConfiguration config = new SessionConfiguration.Builder ()
                    .setClientId ( "8RzoguxuX2ewBwxPa-lWFTbBUpOdsskI" )
                    .setServerToken ( "lmsYmf0NANVZcPTESB5mKYJsAy4nhdYgjgn7rtq1" )
                    .build ();
            ServerTokenSession session = new ServerTokenSession ( config );

            UberRidesApi ride = UberRidesApi.with ( session ).build ();
            RidesService service = ride.createService ();

            //Lyft AppConfig
            ApiConfig apiConfig = new ApiConfig.Builder()
                    .setClientId("mZOUI6oBEYPd")
                    .setClientToken("gAAAAABZH1Z6trZYDn3zSUpGIU6ctNuIDDzaXo0kUJW7Q4jdcCIv2eycPxtRZmic_br1YZfeQWkqurVcEW2t5uL3IVdO1XH9huKDW4tG0-Ya5xyUv_-95eQmHlRGgB8kFSrNxoCa-OQdvSP_ApTngzBZr5yDDkhKx_KIxXRS6E_U46tgc1z9fcM=")
                    .build();

            //Uber ProductType
            Response <ProductsResponse> response = service.getProducts ( googleLat, googleLong ).execute ();
            ProductsResponse products = response.body ();
            results = products.getProducts ();

            //Lyft ProductType
            //WILL ADDRESS WITH STEPHANIE AND REST OF GROUP

            //Uber Price
            Response <PriceEstimatesResponse> respond = service.getPriceEstimates ( googleLat, googleLong,
                    googleLat2, googleLong2 ).execute ();
            PriceEstimatesResponse priceTag = respond.body ();
            prices = priceTag.getPrices ();

            //Lyft Price (Standard and LyftPlus(4+ people)
            try {
                LyftPublicApi lyftPublicApi = new LyftApiFactory(apiConfig).getLyftPublicApi();
                Call<CostEstimateResponse> costEstimateCall = lyftPublicApi.getCosts(results12.latitude, results12.longitude, "lyft", results13.latitude, results13.longitude);
                Response<CostEstimateResponse> lyftResultsStandard = costEstimateCall.execute();
                CostEstimateResponse body = lyftResultsStandard.body();
                List<CostEstimate> pricesLyftStandard = body.cost_estimates;
                String displayPriceMin = "";
                String displayPriceMax = "";

                for (CostEstimate costEstimate : body.cost_estimates) { //tried 'prices' rather than 'body' but didn't like....
                    displayPriceMin = ("$" +(String.valueOf(costEstimate.estimated_cost_cents_min / 100)));
                    displayPriceMax = (String.valueOf(costEstimate.estimated_cost_cents_max / 100));
                }

                model.addAttribute("displayPriceMin", displayPriceMin);
                model.addAttribute("displayPriceMax", displayPriceMax);

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                LyftPublicApi lyftPublicApi = new LyftApiFactory(apiConfig).getLyftPublicApi();
                Call<CostEstimateResponse> costEstimateCall = lyftPublicApi.getCosts(results12.latitude, results12.longitude, "lyft_plus", results13.latitude, results13.longitude);
                Response<CostEstimateResponse> lyftResultsPlus = costEstimateCall.execute();
                CostEstimateResponse body = lyftResultsPlus.body();
                List<CostEstimate> pricesLyftPlus = body.cost_estimates;
                String displayPriceMinPlus = "";
                String displayPriceMaxPlus = "";

                for (CostEstimate costEstimate : body.cost_estimates) { //tried 'prices' rather than 'body' but didn't like....
                    displayPriceMinPlus = ("$" +(String.valueOf(costEstimate.estimated_cost_cents_min / 100)));
                    displayPriceMaxPlus = (String.valueOf(costEstimate.estimated_cost_cents_max / 100));
                }

                model.addAttribute("displayPriceMinPlus", displayPriceMinPlus);
                model.addAttribute("displayPriceMaxPlus", displayPriceMaxPlus);

            } catch (IOException e) {
                e.printStackTrace();
            }


            //Uber Time
            Response <TimeEstimatesResponse> responseTime = service.getPickupTimeEstimate ( googleLat, googleLong,
                    id ).execute ();

            TimeEstimatesResponse time = responseTime.body ();
            duration = time.getTimes ();

            //Lyft Time
            try {

                LyftPublicApi lyftPublicApi = new LyftApiFactory(apiConfig).getLyftPublicApi();
                Call<EtaEstimateResponse> etaCall = lyftPublicApi.getEtas(results12.latitude, results12.longitude, null);

                Response<EtaEstimateResponse> lyftDriverEta = etaCall.execute();
                EtaEstimateResponse body = lyftDriverEta.body();
                List<Eta> lyftTime = body.eta_estimates;
                String displayTime = "";

                for (Eta eta : body.eta_estimates) {
                    displayTime = (String.valueOf(eta.eta_seconds / 60));
                }

                model.addAttribute("driverETA", displayTime);


            } catch (IOException e) {
                e.printStackTrace();
            }


            //Read Uber Data
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
