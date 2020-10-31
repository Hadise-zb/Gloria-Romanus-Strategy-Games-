package unsw.gloriaromanus.backend;

import java.util.*; 
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import unsw.gloriaromanus.*;
//import org.json.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class DPQ { 

    private int dist[];

    private int passed[] = {-1};

    private int adjacent[][];
    
    private List<String> provinces = new ArrayList<String>();

    private Set<Integer> settled; 

    private PriorityQueue<Node> pq; 

    private int V; // Number of vertices 

    List<List<Node> > adj; 

    private ArrayList<String> path;

  
    // Function for Dijkstra's Algorithm 

    

    public int movement(String start, String end, ArrayList<Faction> enemry) {
        ArrayList<String> enermy_pro = new ArrayList<String>();
        for (Faction f : enemry) {
            for (Province p : f.getProvinces()) {
                enermy_pro.add(p.get_name());
            }
            
        }
        
        try {
            JSONObject matrix = new JSONObject(
                Files.readString(Paths.get("src/unsw/gloriaromanus/province_adjacency_matrix.json")));
            

            int count = 0;
            int src = 0;
            boolean find = false;
            for (String key : matrix.keySet()) {
                //dist.put(key, Integer.MAX_VALUE);
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

            adjacent[src][src] = 0;

            JSONArray key = matrix.names ();
            for (int i = 0; i < key.length (); ++i) {
                String keys = key.getString (i); 
                JSONObject pro = matrix.getJSONObject(keys);
                JSONArray province = pro.names ();
                for (int j = 0; j < province.length (); ++j) {
                    String prov = province.getString (i);
                    boolean judge = pro.getBoolean(prov);
                    if (judge == true) {
                        for (int k = 0; k < provinces.size(); k++) {
                            if (provinces.get(k).equals(prov)) {
                                adjacent[i][k] = 1;
                                break;
                            }
                        }
                        
                    } 
                }
            }

            for (int i = 0; i < V; i++) 
                dist[i] = Integer.MAX_VALUE; 

            
        
            dist[src] = 0; 
            passed[src] = 0;
            int k = 0;
            for (int i = 1; i <= this.V; i++) {
                int min = Integer.MAX_VALUE;
                for (int j = 1; j <= this.V; j++) {
                    if (passed[j] == -1 && dist[j] < min && !enermy_pro.contains(provinces.get(j))) {
                        min = dist[j];
                        k = j;
                    }
                }
                passed[k] = 1;
                for (int j = 1; j <= this.V; j++) {
                    if(adjacent[k][j]!=0 && passed[j] == -1 && dist[j]>dist[k]+adjacent[k][j] && !enermy_pro.contains(provinces.get(j)))
                        dist[j]=dist[k]+adjacent[k][j];
                }
            }

            for(int i=1;i<V;i++) {
                if (provinces.get(i).equals(end)) {
                    return dist[i];
                }
            }
           
        } catch (JSONException | IOException e) {
            //e.printStackTrace();
        }
    }

} 
