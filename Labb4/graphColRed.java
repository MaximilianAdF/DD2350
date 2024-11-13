import java.util.ArrayList;
import java.util.List;

public class graphColRed {
    Kattio io;

    int V, E, C; // Number of vertices, edges, and colors
    int roles, scenes, actors; // Number of roles, scenes, and actors

    public void readGraphColoring() {
        V = io.getInt();
        E = io.getInt();
        C = io.getInt();
    }

    public void convertToCasting() {
        roles = V + 2;
        actors = C + 2;
        scenes = E + V + 1;
    }

    public void writeCasting() {
        io.println(roles);
        io.println(scenes);
        io.println(actors);

        // Printing the roles for divas p1 and p2 directly
        io.println("1 1");
        io.println("1 2");

        for (int i = 3; i <= roles; i++) {
            io.print(actors-2);
            for (int j = 3; j <= actors; j++) {
                io.print(" " + j);
            }
            io.println();
        }

        // Print all the obligatory scenes
        io.println("2 2 3");
        for (int i = 3; i <= roles; i++) {
            io.println("2 1 " + i);
        }

        // Read edges and print them as scenes
        for (int i = 0; i < E; i++) {
            int x = io.getInt() + 2;
            int y = io.getInt() + 2;
            io.println("2 " + x + " " + y);
        }
    }

    public graphColRed() {
        io = new Kattio(System.in, System.out);
        readGraphColoring();
        convertToCasting();
        writeCasting();
        io.close();
    }

    public static void main(String[] args) {
        new graphColRed();
    }
}
