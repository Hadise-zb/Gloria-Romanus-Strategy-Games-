package unsw.gloriaromanus.backend;

import java.util.*; 
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import unsw.gloriaromanus.*;
import org.json.*;

public class DPQ { 

    private int dist[][]; 

    private Set<Integer> settled; 

    private PriorityQueue<Node> pq; 

    private int V; // Number of vertices 

    List<List<Node> > adj; 

  

    public DPQ(int V) 

    { 

        this.V = V; 

        dist = new int[V]; 

        settled = new HashSet<Integer>(); 

        pq = new PriorityQueue<Node>(V, new Node()); 

    } 

  

    // Function for Dijkstra's Algorithm 

    public void dijkstra(List<List<Node> > adj, int src) 

    { 

        this.adj = adj; 

  

        for (int i = 0; i < V; i++) 

            dist[i] = Integer.MAX_VALUE; 

  

        // Add source node to the priority queue 

        pq.add(new Node(src, 0)); 

  

        // Distance to the source is 0 

        dist[src] = 0; 

        while (settled.size() != V) { 

  

            // remove the minimum distance node  

            // from the priority queue  

            int u = pq.remove().node; 

  

            // adding the node whose distance is 

            // finalized 

            settled.add(u); 

  

            e_Neighbours(u); 

        } 

    } 

  

    // Function to process all the neighbours  

    // of the passed node 

    private void e_Neighbours(int u) 

    { 

        int edgeDistance = -1; 

        int newDistance = -1; 

  

        // All the neighbors of v 

        for (int i = 0; i < adj.get(u).size(); i++) { 

            Node v = adj.get(u).get(i); 

  

            // If current node hasn't already been processed 

            if (!settled.contains(v.node)) { 

                edgeDistance = v.cost; 

                newDistance = dist[u] + edgeDistance; 

  

                // If new distance is cheaper in cost 

                if (newDistance < dist[v.node]) 

                    dist[v.node] = newDistance; 

  

                // Add the current node to the queue 

                pq.add(new Node(v.node, dist[v.node])); 

            } 

        } 

    } 

    public void movement(String start) {
        try {
            JSONObject matrix = new JSONObject(
                Files.readString(Paths.get("src/unsw/gloriaromanus/province_adjacency_matrix.json")));
            
            int count = 0;
            for (String key : matrix.keySet()) {
                count++;
            }

            for (int i = 0; i < count; i++) 
                for (int j = 0; j < count; j++) 
                    dist[i][j] = Integer.MAX_VALUE;
            
            
            for (String key : matrix.keySet()) {
                JSONArray ja = matrix.getJSONArray(key);
                for (int i = 0; i < ja.length(); i++) {
                    String value = ja.getString(i);
                    
                }
                
            }

            JSONObject province = matrix.getJSONObject(start);
            //this.category = province.getString("category");

            

            JSONArray key = matrix.names ();
            for (int i = 0; i < key.length (); i++) {
                String keys = key.getString (i); 
                String adjacent = matrix.getString (keys);

            }

            /*
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
            */

            for (int i = 0; i < V; i++) 
                dist[i] = Integer.MAX_VALUE;
            
            pq.add(new Node(start, 0));
            
            dist[start] = 0;

            JSONArray key = province.names ();
            for (int i = 0; i < key.length (); i++) {
                String keys = key.getString (i); 
                String adjacent = province.getString (keys);

            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    // Driver code 

    public static void main(String arg[]) 

    { 

        int V = 5; 

        int source = 0; 

  

        // Adjacency list representation of the  

        // connected edges 

        List<List<Node> > adj = new ArrayList<List<Node> >(); 

  

        // Initialize list for every node 

        for (int i = 0; i < V; i++) { 

            List<Node> item = new ArrayList<Node>(); 

            adj.add(item); 

        } 

        
    


        // Inputs for the DPQ graph 

        adj.get(0).add(new Node(1, 9)); 

        adj.get(0).add(new Node(2, 6)); 

        adj.get(0).add(new Node(3, 5)); 

        adj.get(0).add(new Node(4, 3)); 

  

        adj.get(2).add(new Node(1, 2)); 

        adj.get(2).add(new Node(3, 4)); 

  

        // Calculate the single source shortest path 

        DPQ dpq = new DPQ(V); 

        dpq.dijkstra(adj, source); 

  

        // Print the shortest path to all the nodes 

        // from the source node 

        //System.out.println("The shorted path from node :"); 

        for (int i = 0; i < dpq.dist.length; i++); 

            //System.out.println(source + " to " + i + " is "

                               //+ dpq.dist[i]); 

    } 
} 
