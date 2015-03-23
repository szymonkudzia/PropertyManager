package com.sk.property_manager;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Szymon on 2015-03-15.
 */
public class PropertiesExtractor {
    private static final String DEFAULT = "@default";

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

            StringBuilder description = new StringBuilder();
            boolean isPDoc = false;

            for (String line : lines) {
                System.out.println("Current line: " + line);
                line = line.trim();

                if (line.startsWith("##")) {
                    isPDoc = true;
                    description
                            .append(line.substring(2))
                            .append("\n");

                } else if (line.startsWith("#") && isPDoc) {
                    description
                            .append(line.substring(1))
                            .append("\n");

                } else if (!StringUtils.isBlank(line)) {
                    isPDoc = false;

                    String defaultValue = "";

                    int defaultStart = description.indexOf(DEFAULT) + DEFAULT.length();
                    if (defaultStart >= 8) {
                        int lineEnd = description.indexOf("\n", defaultStart);

                        defaultValue = description.substring(defaultStart, lineEnd);
                    }

                    String[] values = line.split("=", -1);
                    properties.add(new Property(values[0], values[1], defaultValue, description.toString()));

                    description.setLength(0);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }
}
