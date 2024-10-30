import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class maxFlow {
    Kattio io;
    
    List<List<Integer>> adjList;
    int[][] restFlow;
    int[][] flow;
    int maxFlow;
    int source;
    int sink;
    int V;
    int E;

    public void readFlow() {
        V = io.getInt();
        source = io.getInt() - 1; // 0-indexed
        sink = io.getInt() - 1;   // 0-indexed
        E = io.getInt();

        restFlow = new int[V][V];
        adjList = new ArrayList<>(V);
        for (int i = 0; i < V; i++) {
            adjList.add(new ArrayList<>()); // Initialize adjacency list for each node
        }

        for (int i = 0; i < E; i++) {
            int a = io.getInt() - 1;
            int b = io.getInt() - 1;
            int c = io.getInt();
            restFlow[a][b] = c;
            adjList.get(a).add(b);  // Bidirectional so add both ways
            adjList.get(b).add(a);
        }
    }

    public boolean pathSourceSink(int[][] restFlow, int[] parent) {
        Queue<Integer> q = new LinkedList<>();
        boolean[] visited = new boolean[V];

        visited[source] = true;
        parent[source] = -1;
        q.add(source);

        while (!q.isEmpty()) {
            int u = q.poll();
            
            for (int v : adjList.get(u)) { //skip uselss nodes not adjacent to u
                if (!visited[v] && restFlow[u][v] > 0) { // Check rem. capacity
                    visited[v] = true;
                    parent[v] = u;
                    q.add(v);
                    
                    if (v == sink) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void solveMaxFlow() {
        int[] parent = new int[V];
        flow = new int[V][V];
        maxFlow = 0;       

        while (pathSourceSink(restFlow, parent)) {
            int pathFlow = Integer.MAX_VALUE;
            int v = sink;

            while (v != source) {
                int u = parent[v];
                pathFlow = Math.min(pathFlow, restFlow[u][v]);
                v = parent[v];
            }

            v = sink;
            while (v != source) {
                int u = parent[v];
                flow[u][v] += pathFlow;
                flow[v][u] -= pathFlow;
                restFlow[u][v] -= pathFlow;
                restFlow[v][u] += pathFlow;
                v = parent[v];
            }
            maxFlow += pathFlow;
        }
    }

    public void writeFlow() {
        io.println(V);
        io.println(source + 1 + " " + (sink + 1) + " " + maxFlow);

        E = 0;
        for (int a = 0; a < V; a++) {
            for (int b = 0; b < V; b++) {
                if (flow[a][b] > 0) {
                    E++;
                }
            }
        }

        io.println(E);
        for (int a = 0; a < V; a++) {
            for (int b = 0; b < V; b++) {
                if (flow[a][b] > 0) {
                    io.println((a + 1) + " " + (b + 1) + " " + flow[a][b]);
                }
            }
        }
        io.flush();
    }
    
    public maxFlow() {
        io = new Kattio(System.in, System.out);
        readFlow();
        solveMaxFlow();
        writeFlow();

        io.close();
    }

    public static void main(String[] args) {
        new maxFlow();
    }
}
