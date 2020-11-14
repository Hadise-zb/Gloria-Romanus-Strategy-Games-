package unsw.gloriaromanus;

import unsw.gloriaromanus.backend.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.GeoPackage;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol.HorizontalAlignment;
import com.esri.arcgisruntime.symbology.TextSymbol.VerticalAlignment;
import com.esri.arcgisruntime.data.Feature;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.util.Pair;

//import javafx.scene.control.ChoiceBox;

public class GloriaRomanusController{

  @FXML
  private MapView mapView;

  @FXML
  private StackPane stackPaneMain;

  //@FXML
  //private ChoiceBox <String> troop_choice;

  // could use ControllerFactory?
  private ArrayList<Pair<MenuController, VBox>> controllerParentPairs;

  private ArcGISMap map;

  private Map<String, String> provinceToOwningFactionMap;

  private Map<String, Integer> provinceToNumberTroopsMap;

  private String humanFaction;

  private Feature currentlySelectedHumanProvince;
  private Feature currentlySelectedEnemyProvince;

  private Feature from_human_province;
  private Feature next_human_province;

  private Feature from_enermy_province;
  private Feature next_enermy_province;

  private FeatureLayer featureLayer_provinces;

  private Faction human_faction;
  private Faction enermy_faction;
  private Systemcontrol system;

  @FXML
  private void initialize() throws JsonParseException, JsonMappingException, IOException, InterruptedException {
    // TODO = you should rely on an object oriented design to determine ownership
    
    provinceToOwningFactionMap = getProvinceToOwningFactionMap();

    provinceToNumberTroopsMap = new HashMap<String, Integer>();
    Random r = new Random();
    for (String provinceName : provinceToOwningFactionMap.keySet()) {
      provinceToNumberTroopsMap.put(provinceName, r.nextInt(500));
    }

    // TODO = load this from a configuration file you create (user should be able to
    // select in loading screen)
    humanFaction = "Rome";

    // my fix here
    Faction my_faction = new Faction("Rome");
    this.human_faction = my_faction;

    Systemcontrol new_system = new Systemcontrol(human_faction);
    this.system = new_system;
    //

    currentlySelectedHumanProvince = null;
    currentlySelectedEnemyProvince = null;

    String []menus = {"invasion_menu.fxml", "basic_menu.fxml"};
    controllerParentPairs = new ArrayList<Pair<MenuController, VBox>>();
    for (String fxmlName: menus){
      System.out.println(fxmlName);
      FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName));
      VBox root = (VBox)loader.load();
      MenuController menuController = (MenuController)loader.getController();
      menuController.setParent(this);
      controllerParentPairs.add(new Pair<MenuController, VBox>(menuController, root));
    }

    stackPaneMain.getChildren().add(controllerParentPairs.get(0).getValue());

    initializeProvinceLayers();

  }
  
  /*
  public void clickedrecuitbutton(ActionEvent e) throws IOException {
    List<String> new_list = new ArrayList<String>();
    String a = "A";
    String b = "B";
    String c = "C";
    new_list.add(a);
    new_list.add(b);
    new_list.add(c);
    troop_choice.getItems().addAll(new_list);
  }
  */
  public void recuit_unit(ActionEvent e, String new_unit) throws JsonParseException, JsonMappingException, IOException {
    //TODO, fix below code
    Unit unit = new Unit(new_unit, humanFaction, "Artillery");
    String humanProvince = (String)currentlySelectedHumanProvince.getAttributes().get("name");
    for (Province p : system.get_myfaction().getProvinces()) {
      if (p.get_name().equals(humanProvince)) {          
          p.get_units().add(unit);
          break;
      }
    }
    addAllPointGraphics();
  }



  public void clickedmoveButton(ActionEvent e, String my_troop) throws IOException {
    if (currentlySelectedHumanProvince != null && currentlySelectedEnemyProvince != null){
      String humanProvince = (String)currentlySelectedHumanProvince.getAttributes().get("name");
      String enemyProvince = (String)currentlySelectedEnemyProvince.getAttributes().get("name");
      //movement(my_troop, humanProvince, enemyProvince);
    }
  }
  
  public void clickedInvadeButton(ActionEvent e) throws IOException {
    if (currentlySelectedHumanProvince != null && currentlySelectedEnemyProvince != null){
      String humanProvince = (String)currentlySelectedHumanProvince.getAttributes().get("name");
      String enemyProvince = (String)currentlySelectedEnemyProvince.getAttributes().get("name");
      if (confirmIfProvincesConnected(humanProvince, enemyProvince)){
        // TODO = have better battle resolution than 50% chance of winning
        Random r = new Random();
        int choice = r.nextInt(2);
        if (choice == 0){
          // human won. Transfer 40% of troops of human over. No casualties by human, but enemy loses all troops
          int numTroopsToTransfer = provinceToNumberTroopsMap.get(humanProvince)*2/5;
          provinceToNumberTroopsMap.put(enemyProvince, numTroopsToTransfer);
          provinceToNumberTroopsMap.put(humanProvince, provinceToNumberTroopsMap.get(humanProvince)-numTroopsToTransfer);
          provinceToOwningFactionMap.put(enemyProvince, humanFaction);
          printMessageToTerminal("Won battle!");
        }
        else{
          // enemy won. Human loses 60% of soldiers in the province
          int numTroopsLost = provinceToNumberTroopsMap.get(humanProvince)*3/5;
          provinceToNumberTroopsMap.put(humanProvince, provinceToNumberTroopsMap.get(humanProvince)-numTroopsLost);
          printMessageToTerminal("Lost battle!");
        }
        resetSelections();  // reset selections in UI
        addAllPointGraphics(); // reset graphics
      }
      else{
        printMessageToTerminal("Provinces not adjacent, cannot invade!");
      }
    }
  }


  /**
   * run this initially to update province owner, change feature in each
   * FeatureLayer to be visible/invisible depending on owner. Can also update
   * graphics initially
   */
  private void initializeProvinceLayers() throws JsonParseException, JsonMappingException, IOException {

    Basemap myBasemap = Basemap.createImagery();
    // myBasemap.getReferenceLayers().remove(0);
    map = new ArcGISMap(myBasemap);
    mapView.setMap(map);

    // note - tried having different FeatureLayers for AI and human provinces to
    // allow different selection colors, but deprecated setSelectionColor method
    // does nothing
    // so forced to only have 1 selection color (unless construct graphics overlays
    // to give color highlighting)
    GeoPackage gpkg_provinces = new GeoPackage("src/unsw/gloriaromanus/provinces_right_hand_fixed.gpkg");
    gpkg_provinces.loadAsync();
    gpkg_provinces.addDoneLoadingListener(() -> {
      if (gpkg_provinces.getLoadStatus() == LoadStatus.LOADED) {
        // create province border feature
        featureLayer_provinces = createFeatureLayer(gpkg_provinces);
        map.getOperationalLayers().add(featureLayer_provinces);

      } else {
        System.out.println("load failure");
      }
    });

    addAllPointGraphics();
  }
              // note can instantiate a PictureMarkerSymbol using the JavaFX Image class - so could
            // construct it with custom-produced BufferedImages stored in Ram
            // http://jens-na.github.io/2013/11/06/java-how-to-concat-buffered-images/
            // then you could convert it to JavaFX image https://stackoverflow.com/a/30970114

  private void addAllPointGraphics() throws JsonParseException, JsonMappingException, IOException {
    mapView.getGraphicsOverlays().clear();

    InputStream inputStream = new FileInputStream(new File("src/unsw/gloriaromanus/provinces_label.geojson"));
    FeatureCollection fc = new ObjectMapper().readValue(inputStream, FeatureCollection.class);

    GraphicsOverlay graphicsOverlay = new GraphicsOverlay();

    for (org.geojson.Feature f : fc.getFeatures()) {
      if (f.getGeometry() instanceof org.geojson.Point) {
        org.geojson.Point p = (org.geojson.Point) f.getGeometry();
        LngLatAlt coor = p.getCoordinates();
        Point curPoint = new Point(coor.getLongitude(), coor.getLatitude(), SpatialReferences.getWgs84());
        PictureMarkerSymbol s = null;
        PictureMarkerSymbol s2 = null;
        PictureMarkerSymbol s3 = null;
        PictureMarkerSymbol s4 = null;
        PictureMarkerSymbol s5 = null;
        PictureMarkerSymbol s6 = null;
        PictureMarkerSymbol s7 = null;
        PictureMarkerSymbol s8 = null;
        String province = (String) f.getProperty("name");
        String faction = provinceToOwningFactionMap.get(province);

        TextSymbol t = new TextSymbol(10,
            faction + "\n" + province + "\n" + provinceToNumberTroopsMap.get(province), 0xFFFF0000,
            HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);

        switch (faction) {
          case "Gaul":
            for (Province m : system.getEnermyFaction().getProvinces()) {
              System.out.println(m.get_units());
              for (Unit n: m.get_units()){
                if (n.get_name().equals("druid")){
                  s = new PictureMarkerSymbol(new Image((new File("images/Celtic_Druid.png")).toURI().toString()));
                  Graphic gPic = new Graphic(curPoint, s);
                  graphicsOverlay.getGraphics().add(gPic);
                }
                if (n.get_name().equals("legionary")){
                  s2 = new PictureMarkerSymbol("images/legionary.png");
                  s2.setOffsetX(100);
                  Graphic gPic2 = new Graphic(curPoint, s2);
                  graphicsOverlay.getGraphics().add(gPic2);
                }

                if (n.get_name().equals("pikemen")){
                  s3 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Pikeman/Pikeman_NB.png");
                  s3.setOffsetX(-100);
                  Graphic gPic3 = new Graphic(curPoint, s3);
                  graphicsOverlay.getGraphics().add(gPic3);
                }
                
                if (n.get_name().equals("hoplite")){
                  s4 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Hoplite/Hoplite_NB.png");
                  s4.setOffsetY(100);
                  Graphic gPic4 = new Graphic(curPoint, s4);
                  graphicsOverlay.getGraphics().add(gPic4);
                }

                if (n.get_name().equals("elephant")){
                  s5 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Elephant/Alone/Elephant_Alone_NB.png");
                  s5.setOffsetY(-100);
                  Graphic gPic5 = new Graphic(curPoint, s5);
                  graphicsOverlay.getGraphics().add(gPic5);
                }

                if (n.get_name().equals("horse archer")){
                  s6 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Horse/Horse_Archer/Horse_Archer_NB.png");
                  s6.setOffsetX(100);
                  s6.setOffsetY(100);
                  Graphic gPic6 = new Graphic(curPoint, s6);
                  graphicsOverlay.getGraphics().add(gPic6);
                }

                if (n.get_name().equals("melee infantry")){
                  s7 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Horse/Horse_Heavy_Cavalry/Horse_Heavy_Cavalry_NB.png");
                  s7.setOffsetX(-100);
                  s7.setOffsetY(-100);
                  Graphic gPic7 = new Graphic(curPoint, s7);
                  graphicsOverlay.getGraphics().add(gPic7);
                } 
              }
            }
            break;
          
            
          case "Rome":
          for (Province m : system.get_myfaction().getProvinces()) {
            // if (p.get_name().equals(humanProvince)) {          
            //     p.get_units().add(unit);
            //
            for (Unit n: m.get_units()){
              if (n.get_name().equals("druid")){
                s = new PictureMarkerSymbol(new Image((new File("images/Celtic_Druid.png")).toURI().toString()));
                Graphic gPic = new Graphic(curPoint, s);
                graphicsOverlay.getGraphics().add(gPic);
              }
              if (n.get_name().equals("legionary")){
                s2 = new PictureMarkerSymbol("images/legionary.png");
                s2.setOffsetX(100);
                Graphic gPic2 = new Graphic(curPoint, s2);
                graphicsOverlay.getGraphics().add(gPic2);
              }

              if (n.get_name().equals("pikemen")){
                s3 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Pikeman/Pikeman_NB.png");
                s3.setOffsetX(-100);
                Graphic gPic3 = new Graphic(curPoint, s3);
                graphicsOverlay.getGraphics().add(gPic3);
              }
              
              if (n.get_name().equals("hoplite")){
                s4 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Hoplite/Hoplite_NB.png");
                s4.setOffsetY(100);
                Graphic gPic4 = new Graphic(curPoint, s4);
                graphicsOverlay.getGraphics().add(gPic4);
              }

              if (n.get_name().equals("elephant")){
                s5 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Elephant_Archers/Elephant_Archers_NB.png");
                s5.setOffsetY(-100);
                Graphic gPic5 = new Graphic(curPoint, s5);
                graphicsOverlay.getGraphics().add(gPic5);
              }

              if (n.get_name().equals("horse archer")){
                s6 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Horse/Horse_Archer/Horse_Archer_NB.png");
                s6.setOffsetX(100);
                s6.setOffsetY(100);
                Graphic gPic6 = new Graphic(curPoint, s6);
                graphicsOverlay.getGraphics().add(gPic6);
              }

              if (n.get_name().equals("melee infantry")){
                s7 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Horse/Horse_Heavy_Cavalry/Horse_Heavy_Cavalry_NB.png");
                s7.setOffsetX(-100);
                s7.setOffsetY(-100);
                Graphic gPic7 = new Graphic(curPoint, s7);
                graphicsOverlay.getGraphics().add(gPic7);
              } 
            }
          }
            break;
        
          // TODO = handle all faction names, and find a better structure...
        }
        t.setHaloColor(0xFFFFFFFF);
        t.setHaloWidth(2);
        System.out.println("Non-point geo json object in file");
      }

    }

    inputStream.close();
    mapView.getGraphicsOverlays().add(graphicsOverlay);
  }

  private FeatureLayer createFeatureLayer(GeoPackage gpkg_provinces) {
    FeatureTable geoPackageTable_provinces = gpkg_provinces.getGeoPackageFeatureTables().get(0);

    // Make sure a feature table was found in the package
    if (geoPackageTable_provinces == null) {
      System.out.println("no geoPackageTable found");
      return null;
    }

    // Create a layer to show the feature table
    FeatureLayer flp = new FeatureLayer(geoPackageTable_provinces);

    // https://developers.arcgis.com/java/latest/guide/identify-features.htm
    // listen to the mouse clicked event on the map view
    mapView.setOnMouseClicked(e -> {
      // was the main button pressed?
      if (e.getButton() == MouseButton.PRIMARY) {
        // get the screen point where the user clicked or tapped
        Point2D screenPoint = new Point2D(e.getX(), e.getY());

        // specifying the layer to identify, where to identify, tolerance around point,
        // to return pop-ups only, and
        // maximum results
        // note - if select right on border, even with 0 tolerance, can select multiple
        // features - so have to check length of result when handling it
        final ListenableFuture<IdentifyLayerResult> identifyFuture = mapView.identifyLayerAsync(flp,
            screenPoint, 0, false, 25);

        // add a listener to the future
        identifyFuture.addDoneListener(() -> {
          try {
            // get the identify results from the future - returns when the operation is
            // complete
            IdentifyLayerResult identifyLayerResult = identifyFuture.get();
            // a reference to the feature layer can be used, for example, to select
            // identified features
            if (identifyLayerResult.getLayerContent() instanceof FeatureLayer) {
              FeatureLayer featureLayer = (FeatureLayer) identifyLayerResult.getLayerContent();
              // select all features that were identified
              List<Feature> features = identifyLayerResult.getElements().stream().map(f -> (Feature) f).collect(Collectors.toList());

              if (features.size() > 1){
                printMessageToTerminal("Have more than 1 element - you might have clicked on boundary!");
              }
              else if (features.size() == 1){
                // note maybe best to track whether selected...
                Feature f = features.get(0);
                String province = (String)f.getAttributes().get("name");

                if (provinceToOwningFactionMap.get(province).equals(humanFaction)){
                  // my fix here
          
                  ((InvasionMenuController)controllerParentPairs.get(0).getKey()).unit_remove();
                  ((InvasionMenuController)controllerParentPairs.get(0).getKey()).unit_add(province);



                  boolean judge = false;
                  // province owned by human
                  if (currentlySelectedHumanProvince != null){
                    //my fix
                    judge = true;
                    next_human_province = f;
                    if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController){
                      ((InvasionMenuController)controllerParentPairs.get(0).getKey()).sethumannextProvince(province);
                    }
                    
                    featureLayer.unselectFeature(currentlySelectedHumanProvince);
                    //my fix
                    //featureLayer.unselectFeature(next_human_province);
                  }
                  currentlySelectedHumanProvince = f;
                  //my fix
                  if (judge == false) {
                    from_human_province = f;
                    if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController){
                      ((InvasionMenuController)controllerParentPairs.get(0).getKey()).sethumancurrentProvince(province);
                    }
                  }

                  if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController){
                    ((InvasionMenuController)controllerParentPairs.get(0).getKey()).setInvadingProvince(province);
                  }

                }
                else{
                  if (currentlySelectedEnemyProvince != null){
                    //my fix
                    next_enermy_province = f;

                    featureLayer.unselectFeature(currentlySelectedEnemyProvince);
                  }
                  currentlySelectedEnemyProvince = f;
                  //my fix
                  from_enermy_province = f;
                  if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController){
                    ((InvasionMenuController)controllerParentPairs.get(0).getKey()).setOpponentProvince(province);
                  }
                }

                featureLayer.selectFeature(f);                
              }
              

            }
          } catch (InterruptedException | ExecutionException ex) {
            // ... must deal with checked exceptions thrown from the async identify
            // operation
            System.out.println("InterruptedException occurred");
          }
        });
      }
    });
    return flp;
  }

  private Map<String, String> getProvinceToOwningFactionMap() throws IOException {
    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
    JSONObject ownership = new JSONObject(content);
    Map<String, String> m = new HashMap<String, String>();
    for (String key : ownership.keySet()) {
      // key will be the faction name
      JSONArray ja = ownership.getJSONArray(key);
      // value is province name
      for (int i = 0; i < ja.length(); i++) {
        String value = ja.getString(i);
        m.put(value, key);
      }
    }
    return m;
  }

  private ArrayList<String> getHumanProvincesList() throws IOException {
    // https://developers.arcgis.com/labs/java/query-a-feature-layer/

    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
    JSONObject ownership = new JSONObject(content);
    return ArrayUtil.convert(ownership.getJSONArray(humanFaction));
  }

  /**
   * returns query for arcgis to get features representing human provinces can
   * apply this to FeatureTable.queryFeaturesAsync() pass string to
   * QueryParameters.setWhereClause() as the query string
   */
  private String getHumanProvincesQuery() throws IOException {
    LinkedList<String> l = new LinkedList<String>();
    for (String hp : getHumanProvincesList()) {
      l.add("name='" + hp + "'");
    }
    return "(" + String.join(" OR ", l) + ")";
  }

  private boolean confirmIfProvincesConnected(String province1, String province2) throws IOException {
    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/province_adjacency_matrix_fully_connected.json"));
    JSONObject provinceAdjacencyMatrix = new JSONObject(content);
    return provinceAdjacencyMatrix.getJSONObject(province1).getBoolean(province2);
  }

  private void resetSelections(){
    featureLayer_provinces.unselectFeatures(Arrays.asList(currentlySelectedEnemyProvince, currentlySelectedHumanProvince));
    currentlySelectedEnemyProvince = null;
    currentlySelectedHumanProvince = null;
    if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController){
      ((InvasionMenuController)controllerParentPairs.get(0).getKey()).setInvadingProvince("");
      ((InvasionMenuController)controllerParentPairs.get(0).getKey()).setOpponentProvince("");
    }
    //my fix
    if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController){
      ((InvasionMenuController)controllerParentPairs.get(0).getKey()).sethumannextProvince("");
      ((InvasionMenuController)controllerParentPairs.get(0).getKey()).sethumancurrentProvince("");
    }
  }

  private void printMessageToTerminal(String message){
    if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController){
      ((InvasionMenuController)controllerParentPairs.get(0).getKey()).appendToTerminal(message);
    }
  }

  /**
   * Stops and releases all resources used in application.
   */
  void terminate() {

    if (mapView != null) {
      mapView.dispose();
    }
  }

  public void switchMenu() throws JsonParseException, JsonMappingException, IOException {
    System.out.println("trying to switch menu");
    stackPaneMain.getChildren().remove(controllerParentPairs.get(0).getValue());
    Collections.reverse(controllerParentPairs);
    stackPaneMain.getChildren().add(controllerParentPairs.get(0).getValue());
  }

  public Systemcontrol get_system() {
    return this.system;
  }
}
