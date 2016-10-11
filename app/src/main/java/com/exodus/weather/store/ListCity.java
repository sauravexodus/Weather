package com.exodus.weather.store;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.

/**
 * Entity mapped to table "LIST_CITY".
 */
@Entity
public class ListCity {

    @Id
    private Long id;
    private String name;
    private String country;

    @Generated(hash = 1817410379)
    public ListCity() {
    }

    public ListCity(Long id) {
        this.id = id;
    }

    @Generated(hash = 530812461)
    public ListCity(Long id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
