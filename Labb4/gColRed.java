//noder = alla i rollerna i senerna
//sammanhängande komplett komponent = en scen
//färgning = skådespelare



//noder = sener
//kanter = rollerna
//färger =skodespelare



// sedan kolla vilka roller som behövs för att lösa



//r1 = p1 ,p2
//r2 = p3
//r3 = p1,p2
//r4 = p4
//r5 = p1,p2



//s1 = r1, r2
//s2 = r2 ,r3
//s3 = r2, r4
//s4 = r4, r3
//s5 = r4, r5

import java.util.HashMap;
import java.util.HashSet;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;


public class gColRed {
    Kattio io;

    HashMap<Integer, HashSet<Integer>> Edges = new HashMap<>();
    ArrayList<ArrayList<Integer>> scenes = new ArrayList<>();
    HashMap<Integer, Integer> Colors = new HashMap<>();
    int V;
    int E;
    int m;

    public void readGraphColoring() {
        V = io.getInt();
        E = io.getInt();
        m = io.getInt();

        for (int i = 0; i < E; i++) {
            int x = io.getInt();
            int y = io.getInt();
            Edges.computeIfAbsent(x, v -> new HashSet<>()).add(y);
            Edges.computeIfPresent(x, (k, v) -> { v.add(y); return v; });

            Edges.computeIfAbsent(y, v -> new HashSet<>()).add(x);
            Edges.computeIfPresent(y, (k, v) -> { v.add(x); return v; });
        }
    }

    private void colorNode(int node) { 
        HashSet<Integer> neighbors = Edges.get(node);
        HashSet<Integer> taken = new HashSet<>();

        for (int neighbor : neighbors) {
            if (Colors.containsKey(neighbor)) {     // if neighbor has a color
                taken.add(Colors.get(neighbor));    // add the color to the list
            }
        }

        for (int i = 1; i <= V; i++) {
            if (!taken.contains(i)) {
                Colors.put(node, i);
                break;
            }
        } 
    }


    private void colorGraph() {
        for (int i = 1; i <= V; i++) {
            colorNode(i);
        }
    }

    private void findScenes() {
        ArrayList<ArrayList<Integer>> pairs = new ArrayList<>();
        scenes = new ArrayList<>();

        for (int i = 1; i <= V; i++){
            for(int neighbor: Edges.get(i)){
                ArrayList<Integer> pair1 = new ArrayList<>(Arrays.asList(Colors.get(i),Colors.get( neighbor)));
                ArrayList<Integer> pair2 = new ArrayList<>(Arrays.asList(Colors.get(neighbor), Colors.get(i)));
                if(!pairs.contains(pair1) && !pairs.contains(pair2)){
                    pairs.add(pair1);
                    scenes.add(new ArrayList<>(Arrays.asList(i,neighbor)));
                }
            }
        }
    }
    
    public void translate() {
        HashMap<Integer, Integer> colorFrequency = new HashMap<>();

        for (int color : Colors.values()) {
            colorFrequency.put(color, colorFrequency.getOrDefault(color, 0) + 1);
        }

        int maxColor = -1;
        int maxCount = -1;

        for (int color : colorFrequency.keySet()) {
            if (colorFrequency.get(color) > maxCount) {
            maxCount = colorFrequency.get(color);
            maxColor = color;
            }
        }

        io.println(V);
        io.println(scenes.size());
        io.println(colorFrequency.keySet().size() + 1);


        int skodespelare = 2;

        for (int i = 1; i <= V; i++) {
            if(Colors.get(i) == maxColor){
                io.println(2 + " " + 1 + " " + 2);

            }else{
                skodespelare++;
                io.println(1 + " " + skodespelare);
            }
            
        }

        for (ArrayList<Integer> scene : scenes) {
            io.println(2 + " " + scene.get(0) + " " + scene.get(1));
        }
        io.close(); 
    }

    public gColRed() {
        io = new Kattio(System.in, System.out);
        readGraphColoring();
        colorGraph();
        findScenes();
        translate();
    }

    public static void main(String[] args) {
        new gColRed();
    }
}