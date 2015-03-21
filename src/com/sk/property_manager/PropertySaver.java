package com.sk.property_manager;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Szymon on 2015-03-16.
 */
public class PropertySaver {
    private Stage stage;
    private List<Property> properties = new ArrayList<Property>();

    public PropertySaver(Stage stage) {
        this.stage = stage;
    }

    public void add(String name, String value, String defaulValue, String description) {
        properties.add(new Property(name, value, defaulValue, description));
    }

    public void save() {
        FileChooser fileChooser = new FileChooser();

        File file = fileChooser.showSaveDialog(stage);
        if (file == null) return;

        String content = writeToString();
        try {
            Files.write(file.toPath(), content.getBytes(), StandardOpenOption.CREATE);

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            properties.clear();
        }

    }


    private String writeToString() {
        StringBuilder content = new StringBuilder();

        int max = 0;
        for (Property property : properties) {
            max = Math.max(max, property.name.length());
        }

        for (Property property : properties) {
            content
                .append(StringUtils.rightPad(property.name, max + 1, " "))
                .append("=")
                .append(property.value)
                .append(System.lineSeparator())
                .append(System.lineSeparator());
        }
        return content.toString();
    }
}
