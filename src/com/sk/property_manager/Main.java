package com.sk.property_manager;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private WebView webView = new WebView();

    private PropertyFilesFinder propertyFilesFinder = new PropertyFilesFinder();
    private PropertiesExtractor propertiesExtractor = new PropertiesExtractor();

    private PropertiesHolder propertiesHolder = new PropertiesHolder();

    @Override
    public void start(final Stage primaryStage) throws Exception {
        VBox vBox = new VBox();
        Scene scene = new Scene(vBox);
        scene.getStylesheets().addAll(this.getClass().getResource("/view/css/javafx.css").toExternalForm());
        MenuBar menuBar = new MenuBar();

        MenuItem openDirectory = new MenuItem("Open directory");
        openDirectory.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        openDirectory.setOnAction(openDirectoryEvent(primaryStage));

        MenuItem openFile = new MenuItem("Open file");
        openFile.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN));
        openFile.setOnAction(openFileEvent(primaryStage));


        // --- Menu File
        Menu menuFile = new Menu("File");
        menuFile.getItems().addAll(openDirectory, openFile);

        menuBar.getMenus().addAll(menuFile);


        webView.getEngine().load(this.getClass().getResource("/view/index.html").toExternalForm());
        bindToJs("propertySaver", new PropertySaver(primaryStage));


        vBox.getChildren().addAll(menuBar, webView);
        VBox.setVgrow(webView, Priority.ALWAYS);

        primaryStage.setTitle("Property Manager");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private EventHandler<ActionEvent> openFileEvent(final Stage primaryStage) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters()
                        .addAll(
                            new FileChooser.ExtensionFilter("All supported file types", "*.properties", "*.war", "*.jar", "*.zip", "*.txt"),
                            new FileChooser.ExtensionFilter("Property file", "*.properties"),
                            new FileChooser.ExtensionFilter("WAR file", "*.war"),
                            new FileChooser.ExtensionFilter("JAR file", "*.jar")
                        );

                File file = fileChooser.showOpenDialog(primaryStage);
                if (file == null) return;

                try {
                    List<File> files = new ArrayList<File>();
                    files.add(file);

                    reloadProperties(files);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private EventHandler<ActionEvent> openDirectoryEvent(final Stage primaryStage) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Choose directory");

                File directory = directoryChooser.showDialog(primaryStage);
                if (directory == null) return;

                try {
                    List<File> propertyFiles = propertyFilesFinder.find(directory);
                    reloadProperties(propertyFiles);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }


    private void reloadProperties(List<File> propertyFiles) throws IOException {
        List<Property> properties = propertiesExtractor.extract(propertyFiles);

        propertiesHolder.clear();
        propertiesHolder.addAll(properties);

        bindToJs("propertiesHolder", propertiesHolder);
        webView.getEngine().executeScript("update()");

    }

    private void bindToJs(String name, Object object) {
        JSObject window = (JSObject) webView.getEngine().executeScript("window");
        window.setMember(name, object);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
