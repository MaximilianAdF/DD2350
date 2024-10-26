import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
import java.util.Map;

public class bipMatch {
    Kattio io;

    Map<Integer, Map<Integer, Integer>> graph;
    Map<Integer, Map<Integer, Integer>> flow;
    List<List<Integer>> adjList;
    int X, Y, E, V;

    int maxFlow;    
    int source;
    int sink;

    public void readBipartite() {
        X = io.getInt();
        Y = io.getInt();
        E = io.getInt();
        V = X + Y + 2;

        graph = new HashMap<>();
        for (int i = 0; i < E; i++) {
            int x = io.getInt();
            int y = io.getInt();
            graph.computeIfAbsent(x, k -> new HashMap<>()).put(y, 1);
        }
    }

    public void convertToFlow() {
        source = 0;
        sink = V - 1;

        adjList = new ArrayList<>(V);
        for (int i = 0; i < V; i++) {
            adjList.add(new ArrayList<>());
        }

        for (int i = 1; i <= X; i++) {
            graph.computeIfAbsent(source, k -> new HashMap<>()).put(i, 1);
            adjList.get(i).add(source);
            adjList.get(source).add(i);
        }

        for (int i = 1; i <= X; i++) {
            for (int j = X + 1; j <= X + Y; j++) {
                if (graph.containsKey(i) && graph.get(i).getOrDefault(j, 0) == 1) {
                    adjList.get(i).add(j);
                    adjList.get(j).add(i);
                }
            }
        }

        for (int i = X + 1; i <= X + Y; i++) {
            graph.computeIfAbsent(i, k -> new HashMap<>()).put(sink, 1);
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
                if (!visited[v] && graph.getOrDefault(u, new HashMap<>()).getOrDefault(v, 0) > 0) {
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
        flow = new HashMap<>();
        int[] parent = new int[V];
        maxFlow = 0;

        while (pathSourceSink(parent)) {
            int pathFlow = Integer.MAX_VALUE;
            int v = sink;

            while (v != source) {
                int u = parent[v];
                pathFlow = Math.min(pathFlow, graph.get(u).get(v));
                v = parent[v];
            }

            v = sink;
            while (v != source) {
                int u = parent[v];
                graph.get(u).put(v, graph.get(u).get(v) - pathFlow);
                graph.computeIfAbsent(v, k -> new HashMap<>()).put(u, graph.get(v).getOrDefault(u, 0) + pathFlow);
                flow.computeIfAbsent(u, k -> new HashMap<>()).put(v, flow.getOrDefault(u, new HashMap<>()).getOrDefault(v, 0) + pathFlow);
                flow.computeIfAbsent(v, k -> new HashMap<>()).put(u, flow.getOrDefault(v, new HashMap<>()).getOrDefault(u, 0) - pathFlow);
                v = parent[v];
            }

            maxFlow += pathFlow;
        }
    }

    public void flowToMatching() {
        int matching = 0;

        for (int i = 1; i <= X; i++) {
            for (int j = X + 1; j <= X + Y; j++) {
                if (flow.getOrDefault(i, new HashMap<>()).getOrDefault(j, 0) == 1) {
                    matching++;
                }
            }
        }

        io.println(X + " " + Y);
        io.println(matching);

        for (int i = 1; i <= X; i++) {
            for (int j = X + 1; j <= X + Y; j++) {
                if (flow.getOrDefault(i, new HashMap<>()).getOrDefault(j, 0) == 1) {
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
