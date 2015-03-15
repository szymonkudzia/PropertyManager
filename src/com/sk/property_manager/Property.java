package com.sk.property_manager;

/**
 * Created by Szymon on 2015-03-15.
 */
public class Property {
    public String name;
    public String value;
    public String defaultValue;
    public String description;

    public Property(String name, String value, String defaultValue, String description) {
        this.name = name;
        this.value = value;
        this.defaultValue = defaultValue;
        this.description = description;
    }
}
