package unsw.gloriaromanus.backend;

import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import unsw.gloriaromanus.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import java.util.Iterator;

public class DPQ { 

    //private int dist[];

    //private int adjacent[][] = {{-1}};
    
    private List<String> provinces = new ArrayList<String>();

    private Set<Integer> settled; 

    private int V; // Number of vertices 

    private ArrayList<String> path;

  
    // Function for Dijkstra's Algorithm 

    public int movement(String start, String end, Faction enermy) {
        ArrayList<String> enermy_pro = new ArrayList<String>();
        
        for (Province p : enermy.getProvinces()) {
            enermy_pro.add(p.get_name());
        }
        
        try {
            JSONObject matrix = new JSONObject(
                Files.readString(Paths.get("src/unsw/gloriaromanus/province_adjacency_matrix_fully_connected.json")));
            //testfile file = new testfile();
            //String testfile = file.testfile();
            //JSONObject matrix = new JSONObject(testfile);
            int des = 0;
            int count = 0;
            int src = 0;
            boolean find = false;
            for (String key : matrix.keySet()) {
                //dist.put(key, Integer.MAX_VALUE);
                //System.out.println(key);
                if (key.equals(end)) {
                    des = count;
                    System.out.println(des);
                }
                provinces.add(key);
                if (start.equals(key)) {
                    find = true;
                }
                count++;
                if (find == false) {
                    src++;
                }
            }
            
            this.V = count;

            //private int passed[] = {-1};
            //int adjacent[count][count] = {{-1}};
            int[][] adjacent = new int[count + 1][count + 1];
            int[] passed = new int[count + 1];
            int[] dist = new int[count + 1];

            for (int i = 0; i < count + 1; i++) {
                for (int j = 0; j < count + 1; j++) {
                    adjacent[i][j] = Integer.MAX_VALUE;
                }
            }
            adjacent[src][src] = 0;

            JSONArray key = matrix.names ();
            for (int i = 0; i < key.length (); i++) {
                String keys = key.getString (i); 
                JSONObject pro = matrix.getJSONObject(keys);
                JSONArray province = pro.names ();
                //if (keys.equals(start)) {
                System.out.println(keys);
                for (int j = 0; j < province.length (); j++) {
                    
                    String prov = province.getString (j);
                    boolean judge = pro.getBoolean(prov);
                    
                    //if (judge == true) {
                    //    System.out.println(judge);
                    //}
                    //System.out.println(judge);
                    //System.out.println(prov);
                    if (judge == true && (!enermy_pro.contains(prov))) {
                        //System.out.println(prov);
                        for (int k = 0; k < provinces.size(); k++) {
                            if (provinces.get(k).equals(prov)) {
                                //System.out.println("aaa");
                                adjacent[k][i] = 1;
                                //break;
                            }
                        }
                        
                    } 
                }
                //}
            }
        
            for (int i = 0; i <= V; i++) {
                dist[i] = Integer.MAX_VALUE;
                passed[i] = -1; 
            }

            dist[src] = 0; 
            passed[src] = 1;
            int k = 0;
            for (int i = 0; i <= this.V; i++) {
                int min = Integer.MAX_VALUE;
                for (int j = 0; j <= this.V; j++) {
                    if (passed[j] == -1 && dist[j] < min) {
                        min = dist[j];
                        k = j;
                    }
                }
                passed[k] = 1;
                for (int j = 0; j <= this.V; j++) {
                    if(adjacent[k][j]!=0 && passed[j] == -1 && dist[j]>dist[k]+adjacent[k][j]) {
                        dist[j]=dist[k]+adjacent[k][j];
                    }
                }
            }
            //return dist[des];
            
            for(int i=0;i < this.V;i++) {
                System.out.println(dist[i]);
                if (provinces.get(i).equals(end)) {
                    System.out.println("haha");
                    return dist[i];
                }
            }
            
            
        } catch (JSONException | IOException e) {
            ((Throwable) e).printStackTrace();
        }
        return 0;
    }

    
    

} 
