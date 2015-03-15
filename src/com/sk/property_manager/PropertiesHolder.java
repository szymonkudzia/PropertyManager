package com.sk.property_manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Szymon on 2015-03-15.
 */
public class PropertiesHolder {
    private List<Property> propertyList = new ArrayList<Property>();

    public void clear() { propertyList.clear(); }
    public void addAll(Collection<Property> properties) { propertyList.addAll(properties); }
    public void add(Property property) {
        propertyList.add(property);
    }

    public Property[] properties() {
        return propertyList.toArray(new Property[propertyList.size()]);
    }
}
