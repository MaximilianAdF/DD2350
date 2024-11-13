import java.util.*;

public class gColRed {
    static Kattio io;
    static List<int[]> Edges = new ArrayList<>();
    static Set<Integer> NodeSet = new HashSet<>();
    static HashMap<Integer, Integer> RoleMap = new HashMap<>();
            
        public static void main(String[] args) {
            io = new Kattio(System.in, System.out);
            
            // Läs in antal hörn, Edges och max antal färger
            int V = io.getInt();  // Antal hörn
            int E = io.getInt();  // Antal Edges
            int m = io.getInt();  // Max antal färger (men vi använder högst V skådespelare)
            
            // Lista för att hålla alla Edges
            for (int i = 0; i < E; i++) {
                int u = io.getInt();
                int v = io.getInt();
                NodeSet.add(u);
                NodeSet.add(v);
                Edges.add(new int[]{u, v});
            }

            V = NodeSet.size();
            // Reducera från graffärgningsproblemet till rollbesättningsproblemet
            reduceGraphColoringToCasting(V, E, m, Edges);
            io.close();
    }
        
    public static void reduceGraphColoringToCasting(int V, int E, int m, List<int[]> Edges) {
        // Antal roller (n), antal scener (s), och antal skådespelare (högst V för att undvika storleksproblem)
        int n = V + 3;  // Antal roller motsvarar antal hörn i grafen
        int s = E + 2;  // Antal scener motsvarar antal Edges
        int k = Math.min(m, n) + 2;  // Antal skådespelare, maximerat till V, plus 2 additional

        // Utdata i Kattis-format för rollbesättningsproblemet
        io.println(n);  // Antal roller
        io.println(s);  // Antal scener
        io.println(k);  // Antal skådespelare

        // Typ 1 villkor: Skådespelarna 1 och 2 kan inte spela i samma scene
        io.println("1 1");
        io.println("1 2");
        io.println("1 3");
        // Typ 2 villkor: Varje roll kan spelas av någon av de k skådespelarna
        String temp ="";
        for (int i = 3; i <= k; i++) {  
            temp = temp + " " + i;
        }
        int i = 4;
        for (int num : NodeSet) {
            RoleMap.put(num, i);
            io.println((k-2) + temp);
            i++;
        }

        // Additional constraints between actors in scenes
        io.println("2 1 3");
        io.println("2 2 3");

        // Typ 3 villkor: Scener baserade på kanterna i grafen
        for (int[] edge : Edges) {
            io.println("2 " + (RoleMap.get(edge[0])) + " " + (RoleMap.get(edge[1])));  // Offset each vertex by 2
        }
    }
}
