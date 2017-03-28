package com.csahmad.moodcloud;

import java.text.SimpleDateFormat;
import java.util.Calendar;
//mwschafe commented unused import statement
//import io.searchbox.annotations.JestId;

/** A mood event. */
public class Post extends ElasticSearchObject {

    public static final Class type = Post.class;
    public static final String typeName = "post";

    private String text;
    private int mood;
    private String triggerText;
    private String triggerImage;
    private int context;
    private String posterId;
    private Calendar date;
    private String dateString;

    /** The location of the Post in the form {latitude, longitude, altitude} */
    private double[] location;

    private GeoPoint geoPoint;

    public Post(String text, int mood, String triggerText, String triggerImage,
                int context, String posterId, double[] location, Calendar date) {

        this.text = text;
        this.mood = mood;
        this.triggerText = triggerText;
        this.triggerImage = triggerImage;
        this.context = context;
        this.posterId = posterId;
        this.setLocation(location);
        this.setDate(date);
    }

    private static String makeDateString(Calendar date) {

        SimpleDateFormat format = new SimpleDateFormat(StringFormats.dateFormat);
        return format.format(date.getTime());
    }

    @Override
    public String toString() {

        return "[" + NullTools.toString(this.id) + "] " + NullTools.toString(this.text);
    }

    @Override
    public String getTypeName() {

        return Post.typeName;
    }

    public String getText() {

        return this.text;
    }

    public void setText(String text) {

        this.text = text;
    }

    public int getMood() {

        return this.mood;
    }

    public void setMood(int mood) {

        this.mood = mood;
    }

    public String getTriggerText() {

        return this.triggerText;
    }

    public void setTriggerText(String triggerText) {

        this.triggerText = triggerText;
    }

    public String getTriggerImage() {

        return this.triggerImage;
    }

    public void setTriggerImage(String triggerImage) {

        this.triggerImage = triggerImage;
    }

    public int getContext() {

        return this.context;
    }

    public void setContext(int context) {

        this.context = context;
    }

    public String getPosterId() {

        return this.posterId;
    }

    public void setPosterId(String posterId) {

        this.posterId = posterId;
    }

    public double[] getLocation() {

        return this.location;
    }

    public void setLocation(double[] location) {

        this.location = location;

        if (location != null)
            this.geoPoint = new GeoPoint(location[0], location[1]);
    }

    public Calendar getDate() {

        return this.date;
    }

    public void setDate(Calendar date) {

        this.date = date;
        this.dateString = Post.makeDateString(date);
    }
}
