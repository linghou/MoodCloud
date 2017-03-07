package com.csahmad.moodcloud;

import java.util.ArrayList;

/**
 * Created by oahmad on 2017-03-06.
 */

public class SearchFilter {

    public int from = 0;

    private ArrayList<String> keywords;
    private ArrayList<String> keywordFields;

    private Integer maxTimeUnitsAgo;
    private String timeUnits = "w";
    private String dateField = "date";

    private Integer maxDistance;
    private SimpleLocation location;
    private String distanceUnits = "km";
    private String locationField = "location";

    public boolean hasKeywords() {

        return this.keywords != null;
    }

    public boolean hasTimeUnitsAgo() {

        return this.maxTimeUnitsAgo != null;
    }

    public boolean hasMaxDistance() {

        return this.maxDistance != null;
    }

    public ArrayList<String> getKeywords() {
        
        return this.keywords;
    }

    public SearchFilter setKeywords(ArrayList<String> keywords) {
        
        this.keywords = keywords;
        return this;
    }

    public ArrayList<String> getKeywordFields() {
        
        return this.keywordFields;
    }

    public SearchFilter setKeywordFields(ArrayList<String> keywordFields) {
        
        this.keywordFields = keywordFields;
        return this;
    }

    public Integer getMaxTimeUnitsAgo() {
        
        return this.maxTimeUnitsAgo;
    }

    public SearchFilter setMaxTimeUnitsAgo(Integer maxTimeUnitsAgo) {
        
        this.maxTimeUnitsAgo = maxTimeUnitsAgo;
        return this;
    }

    public String getTimeUnits() {

        return this.timeUnits;
    }

    public SearchFilter setTimeUnits(String timeUnits) {

        this.timeUnits = timeUnits;
        return this;
    }

    public String getDateField() {
        
        return this.dateField;
    }

    public SearchFilter setDateField(String dateField) {
        
        this.dateField = dateField;
        return this;
    }

    public Integer getMaxDistance() {
        
        return this.maxDistance;
    }

    public SearchFilter setMaxDistance(Integer maxDistance) {
        
        this.maxDistance = maxDistance;
        return this;
    }

    public SimpleLocation getLocation() {
        
        return this.location;
    }

    public SearchFilter setLocation(SimpleLocation location) {
        
        this.location = location;
        return this;
    }

    public String getDistanceUnits() {

        return this.distanceUnits;
    }

    public void setDistanceUnits(String distanceUnits) {

        this.distanceUnits = distanceUnits;
    }

    public String getLocationField() {
        
        return this.locationField;
    }

    public SearchFilter setLocationField(String locationField) {
        
        this.locationField = locationField;
        return this;
    }
}