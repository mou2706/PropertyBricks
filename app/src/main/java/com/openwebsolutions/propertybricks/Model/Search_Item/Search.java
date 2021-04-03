package com.openwebsolutions.propertybricks.Model.Search_Item;

public class Search {

    private Integer id;
    private String name;
    private String location;
    private String type;

    /**
     * No args constructor for use in serialization
     *
     */
    public Search() {
    }

    /**
     *
     * @param id
     * @param name
     * @param type
     */
    public Search(Integer id, String name, String type) {
        super();
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
