package com.sk.property_manager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * Created by Szymon on 2015-03-15.
 */
public class PropertyFilesFinder {
    public List<File> find(File directory) {
        List<File> properties = new ArrayList<File>();

        Stack<File> filesToCheck = new Stack<File>();
        filesToCheck.addAll(Arrays.asList(directory.listFiles()));

        while (!filesToCheck.empty()) {
            File file = filesToCheck.pop();

            if (file.isFile()) {
                boolean propertyFile = file.getName().endsWith(".properties");

                if (propertyFile) {
                    properties.add(file);
                }
            } else {
                filesToCheck.addAll(Arrays.asList(file.listFiles()));
            }
        }

        return properties;
    }
}
