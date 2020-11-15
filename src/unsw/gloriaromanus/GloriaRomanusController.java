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
  private String enermyFaction;

  private Feature currentlySelectedHumanProvince;
  private Feature currentlySelectedEnemyProvince;

  private Feature from_human_province;
  private Feature next_human_province;

  private Feature from_enermy_province;
  private Feature next_enermy_province;

  private FeatureLayer featureLayer_provinces;

  private Faction human_faction;
  private Faction enermy_faction;
  private String Humansfaction;
  private String Enermysfaction;

  private Systemcontrol system;

  private String moved_unit = "";

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
    enermyFaction = "Gaul";

    // my fix here
    //String humansfaction = ((BasicMenuController)controllerParentPairs.get(0).getKey()).get_human_unit();
    //String enermysfaction = ((BasicMenuController)controllerParentPairs.get(0).getKey()).get_human_unit();
    
    // my fix here
    Faction my_faction = new Faction("Rome");
    this.human_faction = my_faction;

    Faction enermy_faction = new Faction("Gaul");
    this.enermy_faction = enermy_faction;

    Systemcontrol new_system = new Systemcontrol(human_faction, enermy_faction);
    this.system = new_system;
    //

    currentlySelectedHumanProvince = null;
    currentlySelectedEnemyProvince = null;

    String []menus = {"basic_menu.fxml", "invasion_menu.fxml"};
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

    // my fix here
    //this.Humansfaction = ((BasicMenuController)controllerParentPairs.get(0).getKey()).get_human_unit();
    //this.Enermysfaction = ((BasicMenuController)controllerParentPairs.get(0).getKey()).get_human_unit();
    //addAllPointGraphics();
    //
  }
  
  //my fix here
  public void start_game() throws JsonParseException, JsonMappingException, IOException {
    this.Humansfaction = ((BasicMenuController)controllerParentPairs.get(0).getKey()).get_human_unit();
    this.Enermysfaction = ((BasicMenuController)controllerParentPairs.get(0).getKey()).get_enermy_unit();
    
    addAllPointGraphics();
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
  public String recuit_unit(String new_unit) throws JsonParseException, JsonMappingException, IOException {
    //TODO, fix below code
    String whose_turn = ((InvasionMenuController)controllerParentPairs.get(0).getKey()).judge_turn();
    if (whose_turn.equals("human")) {
      Unit unit = new Unit(new_unit, humanFaction, "Artillery");
      String humanProvince = (String)currentlySelectedHumanProvince.getAttributes().get("name");
      //System.out.println(humanProvince);
      for (Province p : system.get_myfaction().getProvinces()) {
        if (p.get_name().equals(humanProvince)) {          
            p.get_units().add(unit);
            break;
        }
      }
      addAllPointGraphics();
      return humanProvince;
    } else {
      Unit unit = new Unit(new_unit, enermyFaction, "Artillery");
      String enemyProvince = (String)currentlySelectedHumanProvince.getAttributes().get("name");
      for (Province p : system.get_enermyfaction().getProvinces()) {
        if (p.get_name().equals(enemyProvince)) {          
            p.get_units().add(unit);
            break;
        }
      }
      addAllPointGraphics();
      return enemyProvince;
    }
  }

  public void set_moved_unit(String unit) {
    this.moved_unit = unit;
  }

  public void clickedmoveButton(ActionEvent e) throws IOException {
    //if (currentlySelectedHumanProvince != null && currentlySelectedEnemyProvince != null){
    //  String humanProvince = (String)currentlySelectedHumanProvince.getAttributes().get("name");
    //  String enemyProvince = (String)currentlySelectedEnemyProvince.getAttributes().get("name");
    //movement(my_troop, humanProvince, enemyProvince);
    //}
    //my fix here 
    String whose_turn = ((InvasionMenuController)controllerParentPairs.get(0).getKey()).judge_turn();
    Boolean accept = false;
    if (whose_turn.equals("human")) {
      String fromhumanProvince = (String)from_human_province.getAttributes().get("name");
      String nexthumanProvince = (String)next_human_province.getAttributes().get("name");
      accept = system.human_move(moved_unit, fromhumanProvince, nexthumanProvince);
    } else {
      String fromenermyProvince = (String)from_enermy_province.getAttributes().get("name");
      String nextenermyProvince = (String)next_enermy_province.getAttributes().get("name");
      accept = system.human_move(moved_unit, fromenermyProvince, nextenermyProvince);
    }
    if (accept == false) {
      printMessageToTerminal("Move request can't be accepted!");
    } else {
      addAllPointGraphics();
    }
  }
  
  public void clickedInvadeButton(ActionEvent e) throws IOException {
    //System.out.println(currentlySelectedHumanProvince == null);
    //System.out.println(currentlySelectedEnemyProvince == null);
    String whose_turn = ((InvasionMenuController)controllerParentPairs.get(0).getKey()).judge_turn();
    
    if (currentlySelectedHumanProvince != null && currentlySelectedEnemyProvince != null){
      String humanProvince = (String)currentlySelectedHumanProvince.getAttributes().get("name");
      String enemyProvince = (String)currentlySelectedEnemyProvince.getAttributes().get("name");
      
      //my fix here
      if (whose_turn.equals("enermy")) {
        String temp = enemyProvince;
        enemyProvince = humanProvince;
        humanProvince = temp;
      } 
      
      System.out.println(humanProvince + "  " + enemyProvince);
      //
      if (confirmIfProvincesConnected(humanProvince, enemyProvince)){
        // TODO = have better battle resolution than 50% chance of winning
        Random r = new Random();
        int choice = r.nextInt(2);
        if (choice == 0){
          // human won. Transfer 40% of troops of human over. No casualties by human, but enemy loses all troops
          if (whose_turn.equals("human")) {
            int numTroopsToTransfer = provinceToNumberTroopsMap.get(humanProvince)*2/5;
            provinceToNumberTroopsMap.put(enemyProvince, numTroopsToTransfer);
            provinceToNumberTroopsMap.put(humanProvince, provinceToNumberTroopsMap.get(humanProvince)-numTroopsToTransfer);
            provinceToOwningFactionMap.put(enemyProvince, humanFaction);
            printMessageToTerminal("Won battle!");
          } else {
            int numTroopsToTransfer = provinceToNumberTroopsMap.get(humanProvince)*2/5;
            provinceToNumberTroopsMap.put(enemyProvince, numTroopsToTransfer);
            provinceToNumberTroopsMap.put(humanProvince, provinceToNumberTroopsMap.get(humanProvince)-numTroopsToTransfer);
            provinceToOwningFactionMap.put(enemyProvince, enermyFaction);
            printMessageToTerminal("Won battle!");
          }
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

    //addAllPointGraphics();
  }

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
        String province = (String) f.getProperty("name");
        String faction = provinceToOwningFactionMap.get(province);
        //Original code
        //TextSymbol t = new TextSymbol(10,
        //    faction + "\n" + province + "\n" + provinceToNumberTroopsMap.get(province), 0xFFFF0000,
        //    HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);


        //my fix here 
        //String whose_turn = ((InvasionMenuController)controllerParentPairs.get(0).getKey()).judge_turn();
        //Faction new_faction = null;
        //if (whose_turn.equals("human")) {
        //  new_faction = system.get_myfaction();
        //} else {
        //  new_faction = system.get_enermyfaction();
        //}
        //System.out.println(system == null);
        //System.out.println(system.get_myfaction() == null);
        //System.out.println(system.get_myfaction().getProvinces() == null);
        for (Province pro : system.get_myfaction().getProvinces()) {
          if (pro.get_name().equals(province)) {
            //System.out.println("yes");
            for (Unit u : pro.get_units()) {
              if (u.get_name().equals("pikemen")) {
                PictureMarkerSymbol s3 = null;
                s3 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Pikeman/Pikeman_NB.png");
                s3.setOffsetX(-100);
                Graphic gPic3 = new Graphic(curPoint, s3);

                TextSymbol text = new TextSymbol(10,
            this.Humansfaction + "\n" + province + "\n" + pro.numofUnit(u), 0xFFFF0000,
            HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
                text.setHaloColor(0xFFFFFFFF);
                text.setHaloWidth(2);
                text.setOffsetX(-100);
                text.setOffsetY(+50);
    
                Graphic gText = new Graphic(curPoint, text);
                
                graphicsOverlay.getGraphics().add(gText);
                //graphicsOverlay.getGraphics().add(gPic3);


                graphicsOverlay.getGraphics().add(gPic3);
              } else if (u.get_name().equals("hoplite")){
                PictureMarkerSymbol s3 = null;
                s3 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Hoplite/Hoplite_NB.png");
                s3.setOffsetY(100);
                Graphic gPic4 = new Graphic(curPoint, s3);

                TextSymbol text = new TextSymbol(10,
                this.Humansfaction + "\n" + province + "\n" + pro.numofUnit(u), 0xFFFF0000,
                HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
                text.setHaloColor(0xFFFFFFFF);
                text.setHaloWidth(2);
                //text.setOffsetX(-100);
                text.setOffsetY(+170);
    
                Graphic gText = new Graphic(curPoint, text);
                
                graphicsOverlay.getGraphics().add(gText);

                graphicsOverlay.getGraphics().add(gPic4);
              } else if (u.get_name().equals("elephant")){
                PictureMarkerSymbol s3 = null;
                s3 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Elephant/Elephant_Archers");
                s3.setOffsetY(-100);
                Graphic gPic5 = new Graphic(curPoint, s3);

                TextSymbol text = new TextSymbol(10,
                this.Humansfaction + "\n" + province + "\n" + pro.numofUnit(u), 0xFFFF0000,
                HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
                text.setHaloColor(0xFFFFFFFF);
                text.setHaloWidth(2);
                //text.setOffsetX(-100);
                text.setOffsetY(-50);
    
                Graphic gText = new Graphic(curPoint, text);
                
                graphicsOverlay.getGraphics().add(gText);

                graphicsOverlay.getGraphics().add(gPic5);
              } else if (u.get_name().equals("horse archer")){
                PictureMarkerSymbol s3 = null;
                s3 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Horse/Horse_Archer/Horse_Archer_NB.png");
                s3.setOffsetX(100);
                s3.setOffsetY(100);
                Graphic gPic6 = new Graphic(curPoint, s3);

                TextSymbol text = new TextSymbol(10,
                this.Humansfaction + "\n" + province + "\n" + pro.numofUnit(u), 0xFFFF0000,
                HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
                text.setHaloColor(0xFFFFFFFF);
                text.setHaloWidth(2);
                text.setOffsetX(100);
                text.setOffsetY(+190);
    
                Graphic gText = new Graphic(curPoint, text);
                
                graphicsOverlay.getGraphics().add(gText);

                graphicsOverlay.getGraphics().add(gPic6);
              } else if (u.get_name().equals("druid")){
                PictureMarkerSymbol s3 = null;
                s3 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Druid/Celtic_Druid_NB.png");
                s3.setOffsetX(70);
                s3.setOffsetY(-100);
                Graphic gPic7 = new Graphic(curPoint, s3);

                TextSymbol text = new TextSymbol(10,
                this.Humansfaction + "\n" + province + "\n" + pro.numofUnit(u), 0xFFFF0000,
                HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
                text.setHaloColor(0xFFFFFFFF);
                text.setHaloWidth(2);
                text.setOffsetX(70);
                text.setOffsetY(-50);
    
                Graphic gText = new Graphic(curPoint, text);
                
                graphicsOverlay.getGraphics().add(gText);

                graphicsOverlay.getGraphics().add(gPic7);
            
              } else if (u.get_name().equals("Egyptian Archer")) {
                PictureMarkerSymbol s3 = null;
                s3 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/ArcherMan/Egyptian/Egyptian_Archer_NB.png");
                s3.setOffsetX(-70);
                s3.setOffsetY(-100);
                Graphic gPic6 = new Graphic(curPoint, s3);

                TextSymbol text = new TextSymbol(10,
                this.Humansfaction + "\n" + province + "\n" + pro.numofUnit(u), 0xFFFF0000,
                HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
                text.setHaloColor(0xFFFFFFFF);
                text.setHaloWidth(2);
                text.setOffsetX(-70);
                text.setOffsetY(-50);
    
                Graphic gText = new Graphic(curPoint, text);
                
                graphicsOverlay.getGraphics().add(gText);

                graphicsOverlay.getGraphics().add(gPic6);
              } else if (u.get_name().equals("Archer Man")) {
                PictureMarkerSymbol s3 = null;
                s3 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/ArcherMan/Archer_Man_NB.png");
                s3.setOffsetX(50);
                s3.setOffsetY(50);
                Graphic gPic6 = new Graphic(curPoint, s3);

                TextSymbol text = new TextSymbol(10,
                this.Humansfaction + "\n" + province + "\n" + pro.numofUnit(u), 0xFFFF0000,
                HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
                text.setHaloColor(0xFFFFFFFF);
                text.setHaloWidth(2);
                text.setOffsetX(50);
                text.setOffsetY(100);
    
                Graphic gText = new Graphic(curPoint, text);
                
                graphicsOverlay.getGraphics().add(gText);

                graphicsOverlay.getGraphics().add(gPic6);
              } else if (u.get_name().equals("CamelArcher")) {
                PictureMarkerSymbol s3 = null;
                s3 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Camel/CamelArcher/CamelArcher_NB.png");
                s3.setOffsetX(-300);
                s3.setOffsetY(300);
                Graphic gPic6 = new Graphic(curPoint, s3);

                TextSymbol text = new TextSymbol(10,
                this.Humansfaction + "\n" + province + "\n" + pro.numofUnit(u), 0xFFFF0000,
                HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
                text.setHaloColor(0xFFFFFFFF);
                text.setHaloWidth(2);
                text.setOffsetX(-300);
                text.setOffsetY(370);
    
                Graphic gText = new Graphic(curPoint, text);
                
                graphicsOverlay.getGraphics().add(gText);

                graphicsOverlay.getGraphics().add(gPic6);
              } else if (u.get_name().equals("Cannon")) {
                PictureMarkerSymbol s3 = null;
                s3 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Cannon/Cannon_NB.png");
                s3.setOffsetX(-30);
                s3.setOffsetY(30);
                Graphic gPic6 = new Graphic(curPoint, s3);

                TextSymbol text = new TextSymbol(10,
                this.Humansfaction + "\n" + province + "\n" + pro.numofUnit(u), 0xFFFF0000,
                HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
                text.setHaloColor(0xFFFFFFFF);
                text.setHaloWidth(2);
                text.setOffsetX(-30);
                text.setOffsetY(700);
    
                Graphic gText = new Graphic(curPoint, text);
                
                graphicsOverlay.getGraphics().add(gText);

                graphicsOverlay.getGraphics().add(gPic6);
              } else if (u.get_name().equals("Chariot")) {
                PictureMarkerSymbol s3 = null;
                s3 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Chariot/Chariot_NB.png");
                s3.setOffsetX(120);
                s3.setOffsetY(120);
                Graphic gPic6 = new Graphic(curPoint, s3);

                TextSymbol text = new TextSymbol(10,
                this.Humansfaction + "\n" + province + "\n" + pro.numofUnit(u), 0xFFFF0000,
                HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
                text.setHaloColor(0xFFFFFFFF);
                text.setHaloWidth(2);
                text.setOffsetX(120);
                text.setOffsetY(170);
    
                Graphic gText = new Graphic(curPoint, text);
                
                graphicsOverlay.getGraphics().add(gText);

                graphicsOverlay.getGraphics().add(gPic6);
              } else if (u.get_name().equals("Horse Lancer")) {
                PictureMarkerSymbol s3 = null;
                s3 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Horse/Horse_Lancer/Horse_Lancer_NB.png");
                s3.setOffsetX(-30);
                s3.setOffsetY(-100);
                Graphic gPic6 = new Graphic(curPoint, s3);

                TextSymbol text = new TextSymbol(10,
                this.Humansfaction + "\n" + province + "\n" + pro.numofUnit(u), 0xFFFF0000,
                HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
                text.setHaloColor(0xFFFFFFFF);
                text.setHaloWidth(2);
                text.setOffsetX(-30);
                text.setOffsetY(-50);
    
                Graphic gText = new Graphic(curPoint, text);
                
                graphicsOverlay.getGraphics().add(gText);

                graphicsOverlay.getGraphics().add(gPic6);
              } else if (u.get_name().equals("Horse Heavy Cavalry")) {
                PictureMarkerSymbol s3 = null;
                s3 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Horse/Horse_Heavy_Cavalry/Horse_Heavy_Cavalry_NB.png");
                s3.setOffsetX(300);
                s3.setOffsetY(-300);
                Graphic gPic6 = new Graphic(curPoint, s3);

                TextSymbol text = new TextSymbol(10,
                this.Humansfaction + "\n" + province + "\n" + pro.numofUnit(u), 0xFFFF0000,
                HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
                text.setHaloColor(0xFFFFFFFF);
                text.setHaloWidth(2);
                text.setOffsetX(300);
                text.setOffsetY(-220);
    
                Graphic gText = new Graphic(curPoint, text);
                
                graphicsOverlay.getGraphics().add(gText);

                graphicsOverlay.getGraphics().add(gPic6);
              } else if (u.get_name().equals("Slinger Man")) {
                PictureMarkerSymbol s3 = null;
                s3 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Slingerman/Slinger_Man_NB.png");
                s3.setOffsetX(50);
                s3.setOffsetY(-50);
                Graphic gPic6 = new Graphic(curPoint, s3);

                TextSymbol text = new TextSymbol(10,
                this.Humansfaction + "\n" + province + "\n" + pro.numofUnit(u), 0xFFFF0000,
                HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
                text.setHaloColor(0xFFFFFFFF);
                text.setHaloWidth(2);
                text.setOffsetX(50);
                //text.setOffsetY(0);
    
                Graphic gText = new Graphic(curPoint, text);
                
                graphicsOverlay.getGraphics().add(gText);

                graphicsOverlay.getGraphics().add(gPic6);
            
              } else if (u.get_name().equals("Spearman")) {
                PictureMarkerSymbol s3 = null;
                s3 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Spearman/Spearman_NB.png");
                s3.setOffsetX(30);
                s3.setOffsetY(-30);
                Graphic gPic6 = new Graphic(curPoint, s3);

                TextSymbol text = new TextSymbol(10,
                this.Humansfaction + "\n" + province + "\n" + pro.numofUnit(u), 0xFFFF0000,
                HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
                text.setHaloColor(0xFFFFFFFF);
                text.setHaloWidth(2);
                text.setOffsetX(30);
                text.setOffsetY(60);
    
                Graphic gText = new Graphic(curPoint, text);
                
                graphicsOverlay.getGraphics().add(gText);

                graphicsOverlay.getGraphics().add(gPic6);
              } else if (u.get_name().equals("Swordsman")) {
                PictureMarkerSymbol s3 = null;
                s3 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Swordsman/Swordsman_NB.png");
                s3.setOffsetX(40);
                s3.setOffsetY(-40);
                Graphic gPic6 = new Graphic(curPoint, s3);

                TextSymbol text = new TextSymbol(10,
                this.Humansfaction + "\n" + province + "\n" + pro.numofUnit(u), 0xFFFF0000,
                HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
                text.setHaloColor(0xFFFFFFFF);
                text.setHaloWidth(2);
                text.setOffsetX(40);
                text.setOffsetY(30);

                Graphic gText = new Graphic(curPoint, text);
                
                graphicsOverlay.getGraphics().add(gText);

                graphicsOverlay.getGraphics().add(gPic6);
              }
            } 
          }
        }
        for (Province pro : system.get_enermyfaction().getProvinces()) {
          if (pro.get_name().equals(province)) {
            //System.out.println("yes");
            for (Unit u : pro.get_units()) {
              if (u.get_name().equals("pikemen")) {
                PictureMarkerSymbol s3 = null;
                s3 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Pikeman/Pikeman_NB.png");
                s3.setOffsetX(-100);
                Graphic gPic3 = new Graphic(curPoint, s3);

                TextSymbol text = new TextSymbol(10,
                this.Enermysfaction + "\n" + province + "\n" + pro.numofUnit(u), 0xFFFF0000,
                HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
                text.setHaloColor(0xFFFFFFFF);
                text.setHaloWidth(2);
                text.setOffsetX(-100);
                text.setOffsetY(+50);
    
                Graphic gText = new Graphic(curPoint, text);
                
                graphicsOverlay.getGraphics().add(gText);
                //graphicsOverlay.getGraphics().add(gPic3);


                graphicsOverlay.getGraphics().add(gPic3);
              } else if (u.get_name().equals("hoplite")){
                PictureMarkerSymbol s3 = null;
                s3 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Hoplite/Hoplite_NB.png");
                s3.setOffsetY(100);
                Graphic gPic4 = new Graphic(curPoint, s3);

                TextSymbol text = new TextSymbol(10,
                this.Enermysfaction + "\n" + province + "\n" + pro.numofUnit(u), 0xFFFF0000,
                HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
                text.setHaloColor(0xFFFFFFFF);
                text.setHaloWidth(2);
                //text.setOffsetX(-100);
                text.setOffsetY(+170);
    
                Graphic gText = new Graphic(curPoint, text);
                
                graphicsOverlay.getGraphics().add(gText);

                graphicsOverlay.getGraphics().add(gPic4);
              } else if (u.get_name().equals("elephant")){
                PictureMarkerSymbol s3 = null;
                s3 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Elephant/Elephant_Archers");
                s3.setOffsetY(-100);
                Graphic gPic5 = new Graphic(curPoint, s3);

                TextSymbol text = new TextSymbol(10,
                this.Enermysfaction + "\n" + province + "\n" + pro.numofUnit(u), 0xFFFF0000,
                HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
                text.setHaloColor(0xFFFFFFFF);
                text.setHaloWidth(2);
                //text.setOffsetX(-100);
                text.setOffsetY(-50);
    
                Graphic gText = new Graphic(curPoint, text);
                
                graphicsOverlay.getGraphics().add(gText);

                graphicsOverlay.getGraphics().add(gPic5);
              } else if (u.get_name().equals("horse archer")){
                PictureMarkerSymbol s3 = null;
                s3 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Horse/Horse_Archer/Horse_Archer_NB.png");
                s3.setOffsetX(100);
                s3.setOffsetY(100);
                Graphic gPic6 = new Graphic(curPoint, s3);

                TextSymbol text = new TextSymbol(10,
                this.Enermysfaction + "\n" + province + "\n" + pro.numofUnit(u), 0xFFFF0000,
                HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
                text.setHaloColor(0xFFFFFFFF);
                text.setHaloWidth(2);
                text.setOffsetX(100);
                text.setOffsetY(+190);
    
                Graphic gText = new Graphic(curPoint, text);
                
                graphicsOverlay.getGraphics().add(gText);

                graphicsOverlay.getGraphics().add(gPic6);
              } else if (u.get_name().equals("druid")){
                PictureMarkerSymbol s3 = null;
                s3 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Druid/Celtic_Druid_NB.png");
                s3.setOffsetX(70);
                s3.setOffsetY(-100);
                Graphic gPic7 = new Graphic(curPoint, s3);

                TextSymbol text = new TextSymbol(10,
                this.Enermysfaction + "\n" + province + "\n" + pro.numofUnit(u), 0xFFFF0000,
                HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
                text.setHaloColor(0xFFFFFFFF);
                text.setHaloWidth(2);
                text.setOffsetX(70);
                text.setOffsetY(-50);
    
                Graphic gText = new Graphic(curPoint, text);
                
                graphicsOverlay.getGraphics().add(gText);

                graphicsOverlay.getGraphics().add(gPic7);
            
              } else if (u.get_name().equals("Egyptian Archer")) {
                PictureMarkerSymbol s3 = null;
                s3 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/ArcherMan/Egyptian/Egyptian_Archer_NB.png");
                s3.setOffsetX(-70);
                s3.setOffsetY(-100);
                Graphic gPic6 = new Graphic(curPoint, s3);

                TextSymbol text = new TextSymbol(10,
                this.Enermysfaction + "\n" + province + "\n" + pro.numofUnit(u), 0xFFFF0000,
                HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
                text.setHaloColor(0xFFFFFFFF);
                text.setHaloWidth(2);
                text.setOffsetX(-70);
                text.setOffsetY(-50);
    
                Graphic gText = new Graphic(curPoint, text);
                
                graphicsOverlay.getGraphics().add(gText);

                graphicsOverlay.getGraphics().add(gPic6);
              } else if (u.get_name().equals("Archer Man")) {
                PictureMarkerSymbol s3 = null;
                s3 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/ArcherMan/Archer_Man_NB.png");
                s3.setOffsetX(50);
                s3.setOffsetY(50);
                Graphic gPic6 = new Graphic(curPoint, s3);

                TextSymbol text = new TextSymbol(10,
                this.Enermysfaction + "\n" + province + "\n" + pro.numofUnit(u), 0xFFFF0000,
                HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
                text.setHaloColor(0xFFFFFFFF);
                text.setHaloWidth(2);
                text.setOffsetX(50);
                text.setOffsetY(100);
    
                Graphic gText = new Graphic(curPoint, text);
                
                graphicsOverlay.getGraphics().add(gText);

                graphicsOverlay.getGraphics().add(gPic6);
              } else if (u.get_name().equals("CamelArcher")) {
                PictureMarkerSymbol s3 = null;
                s3 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Camel/CamelArcher/CamelArcher_NB.png");
                s3.setOffsetX(-300);
                s3.setOffsetY(300);
                Graphic gPic6 = new Graphic(curPoint, s3);

                TextSymbol text = new TextSymbol(10,
                this.Enermysfaction + "\n" + province + "\n" + pro.numofUnit(u), 0xFFFF0000,
                HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
                text.setHaloColor(0xFFFFFFFF);
                text.setHaloWidth(2);
                text.setOffsetX(-300);
                text.setOffsetY(370);
    
                Graphic gText = new Graphic(curPoint, text);
                
                graphicsOverlay.getGraphics().add(gText);

                graphicsOverlay.getGraphics().add(gPic6);
              } else if (u.get_name().equals("Cannon")) {
                PictureMarkerSymbol s3 = null;
                s3 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Cannon/Cannon_NB.png");
                s3.setOffsetX(-30);
                s3.setOffsetY(30);
                Graphic gPic6 = new Graphic(curPoint, s3);

                TextSymbol text = new TextSymbol(10,
                this.Enermysfaction + "\n" + province + "\n" + pro.numofUnit(u), 0xFFFF0000,
                HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
                text.setHaloColor(0xFFFFFFFF);
                text.setHaloWidth(2);
                text.setOffsetX(-30);
                text.setOffsetY(700);
    
                Graphic gText = new Graphic(curPoint, text);
                
                graphicsOverlay.getGraphics().add(gText);

                graphicsOverlay.getGraphics().add(gPic6);
              } else if (u.get_name().equals("Chariot")) {
                PictureMarkerSymbol s3 = null;
                s3 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Chariot/Chariot_NB.png");
                s3.setOffsetX(120);
                s3.setOffsetY(120);
                Graphic gPic6 = new Graphic(curPoint, s3);

                TextSymbol text = new TextSymbol(10,
                this.Enermysfaction + "\n" + province + "\n" + pro.numofUnit(u), 0xFFFF0000,
                HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
                text.setHaloColor(0xFFFFFFFF);
                text.setHaloWidth(2);
                text.setOffsetX(120);
                text.setOffsetY(170);
    
                Graphic gText = new Graphic(curPoint, text);
                
                graphicsOverlay.getGraphics().add(gText);

                graphicsOverlay.getGraphics().add(gPic6);
              } else if (u.get_name().equals("Horse Lancer")) {
                PictureMarkerSymbol s3 = null;
                s3 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Horse/Horse_Lancer/Horse_Lancer_NB.png");
                s3.setOffsetX(-30);
                s3.setOffsetY(-100);
                Graphic gPic6 = new Graphic(curPoint, s3);

                TextSymbol text = new TextSymbol(10,
                this.Enermysfaction + "\n" + province + "\n" + pro.numofUnit(u), 0xFFFF0000,
                HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
                text.setHaloColor(0xFFFFFFFF);
                text.setHaloWidth(2);
                text.setOffsetX(-30);
                text.setOffsetY(-50);
    
                Graphic gText = new Graphic(curPoint, text);
                
                graphicsOverlay.getGraphics().add(gText);

                graphicsOverlay.getGraphics().add(gPic6);
              } else if (u.get_name().equals("Horse Heavy Cavalry")) {
                PictureMarkerSymbol s3 = null;
                s3 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Horse/Horse_Heavy_Cavalry/Horse_Heavy_Cavalry_NB.png");
                s3.setOffsetX(300);
                s3.setOffsetY(-300);
                Graphic gPic6 = new Graphic(curPoint, s3);

                TextSymbol text = new TextSymbol(10,
                this.Enermysfaction + "\n" + province + "\n" + pro.numofUnit(u), 0xFFFF0000,
                HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
                text.setHaloColor(0xFFFFFFFF);
                text.setHaloWidth(2);
                text.setOffsetX(300);
                text.setOffsetY(-220);
    
                Graphic gText = new Graphic(curPoint, text);
                
                graphicsOverlay.getGraphics().add(gText);

                graphicsOverlay.getGraphics().add(gPic6);
              } else if (u.get_name().equals("Slinger Man")) {
                PictureMarkerSymbol s3 = null;
                s3 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Slingerman/Slinger_Man_NB.png");
                s3.setOffsetX(50);
                s3.setOffsetY(-50);
                Graphic gPic6 = new Graphic(curPoint, s3);

                TextSymbol text = new TextSymbol(10,
                this.Enermysfaction + "\n" + province + "\n" + pro.numofUnit(u), 0xFFFF0000,
                HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
                text.setHaloColor(0xFFFFFFFF);
                text.setHaloWidth(2);
                text.setOffsetX(50);
                //text.setOffsetY(0);
    
                Graphic gText = new Graphic(curPoint, text);
                
                graphicsOverlay.getGraphics().add(gText);

                graphicsOverlay.getGraphics().add(gPic6);
            
              } else if (u.get_name().equals("Spearman")) {
                PictureMarkerSymbol s3 = null;
                s3 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Spearman/Spearman_NB.png");
                s3.setOffsetX(30);
                s3.setOffsetY(-30);
                Graphic gPic6 = new Graphic(curPoint, s3);

                TextSymbol text = new TextSymbol(10,
                this.Enermysfaction + "\n" + province + "\n" + pro.numofUnit(u), 0xFFFF0000,
                HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
                text.setHaloColor(0xFFFFFFFF);
                text.setHaloWidth(2);
                text.setOffsetX(30);
                text.setOffsetY(60);
    
                Graphic gText = new Graphic(curPoint, text);
                
                graphicsOverlay.getGraphics().add(gText);

                graphicsOverlay.getGraphics().add(gPic6);
              } else if (u.get_name().equals("Swordsman")) {
                PictureMarkerSymbol s3 = null;
                s3 = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Swordsman/Swordsman_NB.png");
                s3.setOffsetX(40);
                s3.setOffsetY(-40);
                Graphic gPic6 = new Graphic(curPoint, s3);

                TextSymbol text = new TextSymbol(10,
                this.Enermysfaction + "\n" + province + "\n" + pro.numofUnit(u), 0xFFFF0000,
                HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
                text.setHaloColor(0xFFFFFFFF);
                text.setHaloWidth(2);
                text.setOffsetX(40);
                text.setOffsetY(30);

                Graphic gText = new Graphic(curPoint, text);
                
                graphicsOverlay.getGraphics().add(gText);

                graphicsOverlay.getGraphics().add(gPic6);
              }
            } 
          }
        }
        //
        //System.out.println(faction);
        //
        switch (faction) {
          case "Gaul":
            // note can instantiate a PictureMarkerSymbol using the JavaFX Image class - so could
            // construct it with custom-produced BufferedImages stored in Ram
            // http://jens-na.github.io/2013/11/06/java-how-to-concat-buffered-images/
            // then you could convert it to JavaFX image https://stackoverflow.com/a/30970114

            // you can pass in a filename to create a PictureMarkerSymbol...
            TextSymbol t = new TextSymbol(10,
            this.Enermysfaction + "\n" + province + "\n" + provinceToNumberTroopsMap.get(province), 0xFFFF0000,
              HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);

            s = new PictureMarkerSymbol(new Image((new File("images/Celtic_Druid.png")).toURI().toString()));
            //s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Flags/CelticFlag.png");

            t.setHaloColor(0xFFFFFFFF);
            t.setHaloWidth(2);

            Graphic gPic = new Graphic(curPoint, s);
            Graphic gText = new Graphic(curPoint, t);
            graphicsOverlay.getGraphics().add(gPic);
            graphicsOverlay.getGraphics().add(gText);
            break;
          case "Rome":
            // you can also pass in a javafx Image to create a PictureMarkerSymbol (different to BufferedImage)
            t = new TextSymbol(10,
            this.Humansfaction + "\n" + province + "\n" + provinceToNumberTroopsMap.get(province), 0xFFFF0000,
              HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);

            s = new PictureMarkerSymbol("images/legionary.png");

            t.setHaloColor(0xFFFFFFFF);
            t.setHaloWidth(2);

            gPic = new Graphic(curPoint, s);
            gText = new Graphic(curPoint, t);
            graphicsOverlay.getGraphics().add(gPic);
            graphicsOverlay.getGraphics().add(gText);

            break;
        
          // TODO = handle all faction names, and find a better structure...
          case "Carthaginian":
            s = new PictureMarkerSymbol("images/legionary.png");
            break;
        }
        //original code
        //t.setHaloColor(0xFFFFFFFF);
        //t.setHaloWidth(2);

        //
        //System.out.println(curPoint == null);
        //System.out.println(s == null);
        //

        //Graphic gPic = new Graphic(curPoint, s);
        //Graphic gText = new Graphic(curPoint, t);
        //graphicsOverlay.getGraphics().add(gPic);
        //graphicsOverlay.getGraphics().add(gText);
      } else {
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

                //my fix here
                //if now is human turn
                //((InvasionMenuController)controllerParentPairs.get(0).getKey()).set_moving_unit();

                String whose_turn = ((InvasionMenuController)controllerParentPairs.get(0).getKey()).judge_turn();
                if (whose_turn.equals("human")) {
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
                    
                    //my fix here
                    //boolean judge = false;
                    //((InvasionMenuController)controllerParentPairs.get(0).getKey()).unit_remove();
                    //((InvasionMenuController)controllerParentPairs.get(0).getKey()).unit_add_enermy(province);

                    if (currentlySelectedEnemyProvince != null){
                      //my fix
                      //judge = true;
                      next_enermy_province = f;
                      //

                      featureLayer.unselectFeature(currentlySelectedEnemyProvince);
                    }
                    currentlySelectedEnemyProvince = f;
                    //my fix
                    from_enermy_province = f;
                    if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController){
                      ((InvasionMenuController)controllerParentPairs.get(0).getKey()).setOpponentProvince(province);
                    }
                  }
                } else {
                  if (!provinceToOwningFactionMap.get(province).equals(humanFaction)){
                    
                    // my fix here    
                    ((InvasionMenuController)controllerParentPairs.get(0).getKey()).unit_remove();
                    ((InvasionMenuController)controllerParentPairs.get(0).getKey()).unit_add_enermy(province);

                    boolean judge = false;
                    // province owned by human
                    if (currentlySelectedHumanProvince != null){
                      //my fix
                      judge = true;
                      next_enermy_province = f;
                      if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController){
                        ((InvasionMenuController)controllerParentPairs.get(0).getKey()).sethumannextProvince(province);
                      }
                      
                      featureLayer.unselectFeature(currentlySelectedHumanProvince);
                      //my fix
                      //featureLayer.unselectFeature(next_human_province);
                    }
                    currentlySelectedEnemyProvince = f;
                    //my fix
                    if (judge == false) {
                      from_enermy_province = f;
                      if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController){
                        ((InvasionMenuController)controllerParentPairs.get(0).getKey()).sethumancurrentProvince(province);
                      }
                    }

                    if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController){
                      ((InvasionMenuController)controllerParentPairs.get(0).getKey()).setInvadingProvince(province);
                    }

                  } else {
                    
                    //my fix here
                    //boolean judge = false;
                    //((InvasionMenuController)controllerParentPairs.get(0).getKey()).unit_remove();
                    //((InvasionMenuController)controllerParentPairs.get(0).getKey()).unit_add_enermy(province);

                    if (currentlySelectedEnemyProvince != null){
                      //my fix
                      //judge = true;
                      next_enermy_province = f;
                      //

                      featureLayer.unselectFeature(currentlySelectedEnemyProvince);
                    }
                    currentlySelectedEnemyProvince = f;
                    //my fix
                    from_enermy_province = f;
                    if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController){
                      ((InvasionMenuController)controllerParentPairs.get(0).getKey()).setOpponentProvince(province);
                    }
                  }
                  Feature temp = currentlySelectedEnemyProvince;
                  currentlySelectedEnemyProvince = currentlySelectedHumanProvince;
                  currentlySelectedHumanProvince = temp;
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

  public void clear_feature() {
    //FeatureLayer featureLayer = (FeatureLayer) identifyLayerResult.getLayerContent();
    //featureLayer.unselectFeature(currentlySelectedHumanProvince);
    //featureLayer.unselectFeature(currentlySelectedEnemyProvince);
    if (currentlySelectedEnemyProvince != null) {
      featureLayer_provinces.unselectFeatures(Arrays.asList(currentlySelectedEnemyProvince));
    }
    if (currentlySelectedHumanProvince != null) {
      featureLayer_provinces.unselectFeatures(Arrays.asList(currentlySelectedHumanProvince));
    }
    //featureLayer_provinces.unselectFeatures(Arrays.asList(currentlySelectedEnemyProvince, currentlySelectedHumanProvince));
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

  public void EndTurn() {
    system.endTurn();
  }

  public void click_save() {
    system.saveProgress();
  }

  public void click_load() {
    system.continueProgress();
  }

}
