package com.sk.property_manager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Szymon on 2015-03-15.
 */
public class PropertiesExtractor {
    public List<Property> extract(List<File> propertyFiles) {
        List<Property> properties = new ArrayList<Property>();

        for (File propertyFile : propertyFiles) {
            System.out.println("Extracting from file: " + propertyFile.getAbsolutePath());

            List<Property> props = extractProperties(propertyFile);

            properties.addAll(props);
        }

        return properties;
    }

    private List<Property> extractProperties(File propertyFile) {
        List<Property> properties = new ArrayList<Property>();

        try {
            List<String> lines = Files.readAllLines(propertyFile.toPath());

            for (String line : lines) {
                System.out.println("Current line: " + line);
                String[] values = line.split("=", -1);

                properties.add(new Property(values[0], values[1], "", "none"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }
}
