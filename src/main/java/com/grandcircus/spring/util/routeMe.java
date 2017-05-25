package com.grandcircus.spring.util;

/**
 * Created by nickk on 5/23/2017.
 */
public class routeMe {
        private String locationField1;
        private String locationField2;
        private String destinationLoc;
        private String currentLoc;

    public String getDestinationLoc() {
        return destinationLoc;
    }

    public void setDestinationLoc(String destinationLoc) {
        this.destinationLoc = destinationLoc;
    }

    public String getCurrentLoc() {
        return currentLoc;
    }

    public void setCurrentLoc(String currentLoc) {
        this.currentLoc = currentLoc;
    }

    public String getLocationField1() {
        return locationField1;
    }

    public void setLocationField1(String locationField1) {
        this.locationField1 = locationField1;
    }

    public String getLocationField2() {
        return locationField2;
    }

    public void setLocationField2(String locationField2) {
        this.locationField2 = locationField2;
    }
}
