package com.grandcircus.spring.controller;

/**
 * Created by PAS8 on 5/24/2017.
 */
public class Main {
    public static void main(String[] args) {

        Coordinates results = GoogleGeocode.geocode("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA");
        System.out.println("Results =" + results);
        System.out.println("Lng =" + results.longitude);
        System.out.println("Lat =" + results.latitude);
    }
}
