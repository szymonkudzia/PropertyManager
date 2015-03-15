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
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.io.File;
import java.util.List;

public class Main extends Application {
    private WebView webView = new WebView();

    private PropertyFilesFinder propertyFilesFinder = new PropertyFilesFinder();
    private PropertiesExtractor propertiesExtractor = new PropertiesExtractor();

    private PropertiesHolder propertiesHolder = new PropertiesHolder();

    @Override
    public void start(final Stage primaryStage) throws Exception{
        VBox vBox = new VBox();
        Scene scene = new Scene(vBox, 800, 600);
        scene.getStylesheets().addAll(this.getClass().getResource("/view/css/javafx.css").toExternalForm());
        MenuBar menuBar = new MenuBar();

        MenuItem openDirectory = new MenuItem("Open directory");
        openDirectory.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        openDirectory.setOnAction(openDirectoryEvent(primaryStage));

        // --- Menu File
        Menu menuFile = new Menu("File");
        menuFile.getItems().addAll(openDirectory);

        menuBar.getMenus().addAll(menuFile);




        webView.getEngine().load(this.getClass().getResource("/view/index.html").toExternalForm());



        vBox.getChildren().addAll(menuBar, webView);


        primaryStage.setTitle("Property Manager");
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    private EventHandler<ActionEvent> openDirectoryEvent(final Stage primaryStage) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser directoryChooser =new DirectoryChooser();
                directoryChooser.setTitle("Choose directory");

                File directory = directoryChooser.showDialog(primaryStage);
                if (directory == null) return;

                List<File> propertyFiles = propertyFilesFinder.find(directory);
                List<Property> properties = propertiesExtractor.extract(propertyFiles);

                propertiesHolder.clear();
                propertiesHolder.addAll(properties);

//                webView.getEngine().reload();
                JSObject window = (JSObject) webView.getEngine().executeScript("window");
                window.setMember("propertiesHolder", propertiesHolder);
                String r = (String) webView.getEngine().executeScript("update()");
                System.out.println(r);

            }
        };
    }





    public static void main(String[] args) {
        launch(args);
    }
}
