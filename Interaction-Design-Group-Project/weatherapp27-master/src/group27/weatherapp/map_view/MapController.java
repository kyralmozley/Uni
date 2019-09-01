package group27.weatherapp.map_view;
/* IMPORT OTHER CLASSES */
import group27.weatherapp.AbstractController;
import group27.weatherapp.datasources.location.NameToCoords;
import group27.weatherapp.datasources.MountainRange;
import group27.weatherapp.datasources.location.NameToCoords;
import group27.weatherapp.datasources.location.NominatimSearchResponse;
import group27.weatherapp.weather_view.WeatherSceneFactory;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.MapComponentInitializedListener;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import netscape.javascript.JSObject;

//This class is the controller for the map scene.

public class MapController implements Initializable, MapComponentInitializedListener, AbstractController {

    //coordinates from a search input
    public double searchLat;
    public double searchLong;

    //coordinates from a clicked pin on map
    public double clickedLat;
    public double clickedLong;

    protected StringProperty search = new SimpleStringProperty();
    public String searchInput; //the search input received from text field

    //adding elements from FXML
    @FXML
    protected TextField searchTextField;

    @FXML
    protected GoogleMapView mapView;

    @FXML
    private Button locationButton;

    @FXML
    private Button searchButton;

    @FXML
    BorderPane headerPane;


    /**
     *
     * @param event: Search fields action. Gets the input from the searchfield in FXML.
     *
     *
     */
    @FXML
    private void searchTextFieldAction(ActionEvent event) {

        searchInput = search.get();
        System.out.println("Searching for weather at " + searchInput);

        // Check if name matches a region name exactly and return representing point
        for (MountainRange range : MountainRange.values()) {
            if (searchInput.equals(range.name().replace('_', ' '))) {
                changeToWeatherScene(event, range.lat, range.lon);
                return;
            }
        }

        try {
            /**
             * We then try to parse it into two doubles incase the user has inputed latlong
             * If this fails, they entered a string then we call the nameToCoords funciton to return the coorrdinates
             * If there is nothing matching then it may return null so we must test for that.
             * If this is the case then we pop up an alert to the user
             * then call changeToWeatherScene with the latlong
             */
            String[] latlong =  searchInput.split(",");
            searchLat = Double.parseDouble(latlong[0]);
            searchLong = Double.parseDouble(latlong[1]);


        } catch (Exception e) {
            NominatimSearchResponse searchResponse = NameToCoords.nameToCoords(searchInput);
            if (searchResponse == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "No Results found for " + searchInput, ButtonType.CANCEL);
                alert.showAndWait();
                return;
            }
            searchLat = searchResponse.lat;
            searchLong = searchResponse.lon;
        }
        changeToWeatherScene(event, searchLat, searchLong);
    }

    /**
     *
     * @param event: event in the scene
     * @param lat: latitude
     * @param lng: longitude
     */
    private void changeToWeatherScene(ActionEvent event, double lat, double lng) {
        Scene curScene = ((Node)event.getSource()).getScene();
        changeToWeatherScene(curScene, lat, lng);
    }

    /**
     *
     * @param s: the current scene
     * @param lat: latitude
     * @param lng: longitude
     */
    private void changeToWeatherScene(Scene s, double lat, double lng){
        Stage stage = (Stage) s.getWindow();
        try {
            WeatherSceneFactory sceneFactory = new WeatherSceneFactory();
            sceneFactory.setDimensions(s);
            sceneFactory.setLatLong(lat, lng);
            stage.setScene(sceneFactory.loadScene());
            //create a new weather scene with the latlong passed in
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initalise the map, adding options, markers etc.
     */
    @Override
    public void mapInitialized() {

        MapOptions options = new MapOptions();
        options.center(new LatLong(53.8423, -2.93))
                .zoomControl(false)
                .zoom(6)
                .overviewMapControl(false)
                .mapType(MapTypeIdEnum.TERRAIN)
                .mapTypeControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false);

        GoogleMap map = mapView.createMap(options);


        /**
         * Adding met office mountain regions for the pin points for the map
         * For each point then add a marker option and the markers
         */

        String[] mountain_regions = {"northwest_highlands", "north_grampian", "south_grampian", "southwest_highlands", "mourne_mountains",
                "lake_district", "yorkshire_dales", "peak_district", "snowdonia", "brecon_beacons" };

        Map<Integer, String> mountains = new HashMap<>();
        Map<Integer, LatLong> mountainCoord = new HashMap<>();
        Map<Integer, MarkerOptions> markerOptionsMap = new HashMap<>();
        Map<Integer, Marker> markerMap = new HashMap<>();

        for(int i=0; i<mountain_regions.length; i++) {
            mountains.put(i, mountain_regions[i]);
        }

        for (MountainRange range : MountainRange.values()) {
            mountainCoord.put(range.ordinal(), new LatLong(range.lat, range.lon));
        }

        for(int i=0; i<mountain_regions.length; i++) {
            markerOptionsMap.put(i, new MarkerOptions().position(mountainCoord.get(i)));
        }

        for(int i=0; i<mountain_regions.length; i++) {
            markerMap.put(i, new Marker(markerOptionsMap.get(i)));
            map.addMarker(markerMap.get(i));
        }

        for (int i=0; i<mountain_regions.length; i++) {
            map.addUIEventHandler(markerMap.get(i), UIEventType.click, (JSObject obj) -> {
                LatLong ll = new LatLong((JSObject) obj.getMember("latLng"));
                clickedLat = ll.getLatitude();
                clickedLong = ll.getLongitude();
                System.out.println("lat: " + clickedLat + "lng:" + clickedLong);
                changeToWeatherScene(mapView.getScene(), clickedLat, clickedLong);
                /**
                 * On click of a marker, call the change to weather scene
                 */
            });
        }

    }


    /**
     *
     * @param location:  The location used to resolve relative paths for the root object, or
     *      {@code null} if the location is not known.
     * @param resources: The resources used to localize the root object, or {@code null} if
     *      the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /**
         * Setting heights, width, padding etc. for all the objects in the map scene
         */

        headerPane.setPadding(new Insets(15, 10, 15, 10));
        headerPane.centerProperty();

        searchTextField.setAlignment(Pos.TOP_LEFT);
        searchTextField.setPrefSize(340, 30);

        searchButton.maxWidth(27);
        searchButton.setAlignment(Pos.TOP_CENTER);
        searchButton.setPrefHeight(27);
        searchButton.setPadding(new Insets(-1, 30,0,5));

        locationButton.maxWidth(27);
        locationButton.setAlignment(Pos.TOP_RIGHT);
        locationButton.setPadding(new Insets(0.5, 0,0,10));


        try {
            Image image = new Image(ClassLoader.getSystemResource("res/location.png").openStream());
            ImageView imageView = new ImageView(image);

            imageView.setFitWidth(27);
            imageView.setFitHeight(27);


            locationButton.setGraphic(imageView);
            locationButton.setStyle("-fx-background-color: none");
            locationButton.setOnAction((actionEvent) -> {
                changeToWeatherScene(actionEvent, 52.211, 0.0971); //Computer Lab Location
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            Image image = new Image(ClassLoader.getSystemResource("res/search.png").openStream());
            ImageView imageView = new ImageView(image);

            imageView.setFitWidth(27);
            imageView.setFitHeight(27);


            searchButton.setGraphic(imageView);
            searchButton.setStyle("-fx-background-color: none");
            searchButton.setOnAction((actionEvent) -> {
                searchTextFieldAction(actionEvent);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.setMaxWidth(10000);
        mapView.setMaxHeight(10000);
        mapView.addMapInializedListener(this);
        //mapView.setKey("GOOGLE API KEY"); 

        search.bindBidirectional(searchTextField.textProperty());


    }
}
