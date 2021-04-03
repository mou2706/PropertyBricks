package com.openwebsolutions.propertybricks.Model.Search_Item;

import java.util.ArrayList;

public class Searchitem {

    private ArrayList<Search> search = null;
    private String message;
    private Boolean success;

    /**
     * No args constructor for use in serialization
     *
     */
    public Searchitem() {
    }

    /**
     *
     * @param message
     * @param search
     * @param success
     */
    public Searchitem(ArrayList<Search> search, String message, Boolean success) {
        super();
        this.search = search;
        this.message = message;
        this.success = success;
    }

    public ArrayList<Search> getSearch() {
        return search;
    }

    public void setSearch(ArrayList<Search> search) {
        this.search = search;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}