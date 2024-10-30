import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;

public class bipMatch {
    Kattio io;

    List<List<Integer>> adjList;
    char[][] graph;
    char[][] flow;

    int X, Y, E, V;
    int maxFlow;
    int source;
    int sink;

    public void readBipartite() {
        X = io.getInt();
        Y = io.getInt();
        E = io.getInt();
        V = X + Y + 2;

        // Initialize the graph matrix with '0' (no edge)
        graph = new char[V][V];
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                graph[i][j] = '0';
            }
        }

        // Read edges and set in the graph matrix
        for (int i = 0; i < E; i++) {
            int x = io.getInt();
            int y = io.getInt();
            graph[x][y] = '1';  // Set edge from x to y
        }
    }

    public void convertToFlow() {
        source = 0;
        sink = V - 1;

        adjList = new ArrayList<>(V);
        for (int i = 0; i < V; i++) {
            adjList.add(new ArrayList<>());
        }

        // Add edges from source to X nodes in the graph matrix
        for (int i = 1; i <= X; i++) {
            graph[source][i] = '1';
            adjList.get(i).add(source);
            adjList.get(source).add(i);
        }

        // Add edges between X and Y nodes based on input graph matrix
        for (int i = 1; i <= X; i++) {
            for (int j = X + 1; j <= X + Y; j++) {
                if (graph[i][j] == '1') {  // If edge exists in the input graph
                    adjList.get(i).add(j);
                    adjList.get(j).add(i);
                }
            }
        }

        // Add edges from Y nodes to the sink in the graph matrix
        for (int i = X + 1; i <= X + Y; i++) {
            graph[i][sink] = '1';
            adjList.get(i).add(sink);
            adjList.get(sink).add(i);
        }

        E += X + Y;
    }

    public boolean pathSourceSink(int[] parent) {
        Queue<Integer> q = new LinkedList<>();
        boolean[] visited = new boolean[V];

        visited[source] = true;
        parent[source] = -1;
        q.add(source);

        while (!q.isEmpty()) {
            int u = q.poll();

            for (int v : adjList.get(u)) {
                if (!visited[v] && graph[u][v] == '1') {  // Check if path exists
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
        flow = new char[V][V];
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                flow[i][j] = '0';  // Initialize flow matrix with '0'
            }
        }

        int[] parent = new int[V];
        maxFlow = 0;

        // Augment flow while there is a path from source to sink
        while (pathSourceSink(parent)) {
            int pathFlow = Integer.MAX_VALUE;
            int v = sink;

            // Find maximum flow through the path found
            while (v != source) {
                int u = parent[v];
                pathFlow = Math.min(pathFlow, graph[u][v] - '0');
                v = parent[v];
            }

            // Update residual capacities of the edges and reverse edges
            v = sink;
            while (v != source) {
                int u = parent[v];
                flow[u][v] = (char) (flow[u][v] + pathFlow);  // Update flow in the flow matrix
                flow[v][u] = (char) (flow[v][u] - pathFlow);
                graph[u][v] = (char) (graph[u][v] - pathFlow);  // Decrease capacity in the graph matrix
                graph[v][u] = (char) (graph[v][u] + pathFlow);  // Increase capacity for reverse edge
                v = parent[v];
            }

            maxFlow += pathFlow;
        }
    }

    public void flowToMatching() {
        int matching = 0;

        for (int i = 1; i <= X; i++) {
            for (int j = X + 1; j <= X + Y; j++) {
                if (flow[i][j] == '1') {  // Check if flow exists from i to j
                    matching++;
                }
            }
        }

        io.println(X + " " + Y);
        io.println(matching);

        for (int i = 1; i <= X; i++) {
            for (int j = X + 1; j <= X + Y; j++) {
                if (flow[i][j] == '1') {
                    io.println(i + " " + j);
                }
            }
        }

        io.flush();
    }

    public bipMatch() {
        io = new Kattio(System.in, System.out);
        readBipartite();
        convertToFlow();
        solveMaxFlow();
        flowToMatching();

        io.close();
    }

    public static void main(String[] args) {
        new bipMatch();
    }
}
