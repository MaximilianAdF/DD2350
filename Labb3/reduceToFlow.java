import java.util.ArrayList;

public class reduceToFlow {
    Kattio io;

    int[][] graph; // Edge list
    int X; // Nodes on the left
    int Y; // Nodes on the right
    int V; // Total nodes
    int E; // Edge count

    public void readBipartite() {
        X = io.getInt();
        Y = io.getInt();
        E = io.getInt();
        V = X + Y + 2;

        graph = new int[E][2];
        for (int i = 0; i < E; i++) {
            int x = io.getInt();
            int y = io.getInt();
            graph[i][0] = x;
            graph[i][1] = y;
        }
    }

    public void convertToFlow() {
        int[][] cap = new int[V][V];

        int source = 0; // Source idx
        int sink = V - 1; // Sink idx

        //Connect source to all nodes in X with capacity 1
        for (int i = 1; i <= X; i++) {
            cap[source][i] = 1; 
        }

        //Read edges X, Y and connect them
        for (int i = 0; i < E; i++) {
            int x = graph[i][0];
            int y = graph[i][1];
            cap[x][y] = 1;
        }

        //Connect all nodes in Y to sink with capacity 1
        for (int i = X + 1; i <= X + Y; i++) {
            cap[i][sink] = 1;
        }

        graph = cap;
        E += X + Y;
    }

    public void writeFlow() {
        io.println(V);
        io.println(1 + " " + V);
        io.println(E);

        for (int a = 0; a < V; a++) {
            for (int b = 0; b < V; b++) {
                if (graph[a][b] > 0) {
                    io.println((a+1) + " " + (b+1) + " " + graph[a][b]);
                }
            }
        }

        io.flush();
    }

    public void readMaxFlow() {
        V = io.getInt();
        int source = io.getInt();
        int sink = io.getInt();
        int flow = io.getInt();
        E = io.getInt();

        graph = new int[V][V];
        for (int i = 0; i < E; i++) {
            int a = io.getInt();
            int b = io.getInt();
            int f = io.getInt();
            graph[a-1][b-1] = f;
        }
        graph[source-1][sink-1] = flow;
    }

    public void flowToMatching() {
        ArrayList<int[]> matching = new ArrayList<>();

        for (int i = 1; i <= X; i++) {
            for (int j = X + 1; j <= X + Y; j++) {
                if (graph[i][j] == 1) {
                    matching.add(new int[]{i, j});
                }
            }
        }

        io.println(X + " " + Y);
        io.println(matching.size());

        // Print the matching edges
        for (int[] edge : matching) {
            io.println(edge[0] + " " + edge[1]);
        }
    }

    public reduceToFlow() {
        io = new Kattio(System.in, System.out);
        readBipartite();
        convertToFlow();
        writeFlow();
        //Kattis löser med edmonds karps
        readMaxFlow();
        flowToMatching();

        io.close();
    }


    public static void main(String[] args) {
        new reduceToFlow();
    }
}