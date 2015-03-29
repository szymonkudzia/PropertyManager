package com.sk.property_manager;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * Created by Szymon on 2015-03-15.
 */
public class PropertiesExtractor {
    private static final String DEFAULT_VALUE_INDICATOR = "@default";

    public List<Property> extract(List<File> propertyFiles) throws IOException {
        List<Property> properties = new ArrayList<Property>();

        for (File propertyFile : propertyFiles) {
            System.out.println("Extracting from file: " + propertyFile.getAbsolutePath());

            List<String> lines;

            if (isZipFile(propertyFile)) {
                lines = readAllLinesFromZip(propertyFile);
            } else {
                lines = Files.readAllLines(propertyFile.toPath());
            }

            List<Property> props = extractProperties(lines);

            properties.addAll(props);
        }

        return properties;
    }

    private List<String> readAllLinesFromZip(File propertyFile) throws IOException {
        List<String> lines = new ArrayList<String>();
        ZipFile zip = new ZipFile(propertyFile);

        Enumeration<? extends ZipEntry> entries = zip.entries();

        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();

            if (!entry.getName().endsWith(".properties"))
                continue;

            InputStream stream = zip.getInputStream(entry);
            lines.addAll(readAllLines(stream));
        }

        return lines;
    }

    private boolean isZipFile(File propertyFile) {
        String name = propertyFile.getName();

        return
                name.endsWith(".zip") ||
                name.endsWith(".war") ||
                name.endsWith(".jar");
    }


    private List<Property> extractProperties(List<String> lines) {
        List<Property> properties = new ArrayList<Property>();

        StringBuilder description = new StringBuilder();
        boolean isPDoc = false;

        for (String line : lines) {
            System.out.println("Current line: " + line);
            line = line.trim();

            if (line.startsWith("#") && !isPDoc) {
                isPDoc = true;
                description
                        .append(line.substring(1))
                        .append("\n");

            } else if (line.startsWith("#") && isPDoc) {
                description
                        .append(line.substring(1))
                        .append("\n");

            } else if (!StringUtils.isBlank(line) && line.contains("=")) {
                isPDoc = false;

                String defaultValue = "";

                int defaultStart = description.indexOf(DEFAULT_VALUE_INDICATOR) + DEFAULT_VALUE_INDICATOR.length();
                if (defaultStart >= 8) {
                    int lineEnd = description.indexOf("\n", defaultStart);

                    defaultValue = description.substring(defaultStart, lineEnd);
                }

                String[] values = line.split("=", -1);
                properties.add(new Property(values[0], values[1], defaultValue, description.toString()));

                description.setLength(0);
            }

        }


        return properties;
    }


    private List<String> readAllLines(InputStream inputStream) {
        List<String> lines = new ArrayList<String>();
        Scanner scanner = new Scanner(inputStream);

        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }

        return lines;
    }
}
