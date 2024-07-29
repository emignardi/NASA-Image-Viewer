package com.example.nasaimageviewer.model;

import java.util.ArrayList;


/** This class represents an image from NASA's API.
 * @author Eric Mignardi
 * @version 1.0
 */
public class NasaImage {

    /**
     * The ArrayList used to store objects of this class
     */
    public static ArrayList<NasaImage> nasaImages = new ArrayList<>();

    /**
     * The id of the NasaImage
     */
    private int id;
    /**
     * The date of the NasaImage
     */
    private String date;
    /**
     * The hdurl of the NasaImage
     */
    private String hdurl;
    /**
     * The url of the NasaImage
     */
    private String url;

    /**
     * No-Args Constructor
     */
    public NasaImage () {

    }

    /**
     * Multiple-Args Constructor without the ID parameter
     */
    public NasaImage(String date, String hdurl, String url) {
        this.date = date;
        this.hdurl = hdurl;
        this.url = url;
    }

    /**
     * All-Args Constructor
     */
    public NasaImage(int id, String date, String hdurl, String url) {
        this.id = id;
        this.date = date;
        this.hdurl = hdurl;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHdurl() {
        return hdurl;
    }

    public void setHdurl(String hdurl) {
        this.hdurl = hdurl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Overriding the default toString method
     * @return A readable output containing the objects date, hdurl, and url
     */
    @Override
    public String toString() {
        return "DATE: " + date;
    }

}