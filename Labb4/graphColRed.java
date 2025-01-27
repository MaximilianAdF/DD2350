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
        actors = Math.min(roles, C + 2); // Can never have a need for more actors than roles
        scenes = E + V + 1;
    }

    public void writeCasting() {
        io.println(roles);
        io.println(scenes);
        io.println(actors);

    // Vilkor typ 1
        // Printing the roles for divas p1 and p2 directly
        io.println("1 1");
        io.println("1 2");

        // Create actor string
        String str = "";
        for (int j = 3; j <= actors; j++) {
            str += " " + j;
        }

        // Print all actor assignments
        for (int i = 3; i <= roles; i++) {
            io.println((actors-2) + str);
        }

    // Vilkor typ 2
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
