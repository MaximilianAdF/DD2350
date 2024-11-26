public class heurRole {
    // Find two roles that are never together in a scene | Assign them to actors 1 and 2
    // Start with the largest scene and assign actors to the roles greedily
    // Continue in descending order of scene size until all roles have been assigned an actor   
    Kattio io;

    HashMap<Integer, ArrayList<Integer>> actors; // Key: Role, Value: Actors
    ArrayList<Scene> scenes;                     // List of all scenes
    int n; // Number of roles
    int s; // Number of scenes
    int k; // Number of actors

    public void readCasting() {
        n = io.getInt();
        s = io.getInt();
        k = io.getInt();

        actors = new HashMap<Integer, ArrayList<Integer>>();
        scenes = new ArrayList<Scene>(s);

        for (int role = 1; role <= n; role++) {
            int actorCount = io.getInt();
            actors.put(role, new ArrayList<Integer>(actorsCount));
            for (int i = 0; i < actorCount; i++) {
                actors.get(role).add(io.getInt());
            }
        }

        for (int i = 0; i < s; i++) {
            int roleCount = io.getInt();
            Scene scene = new Scene(roleCount);
            for (int j = 0; j < roleCount; j++) {
                scene.addRole(io.getInt());
            }
        }
    }


    public heurRole() {
        io = new Kattio(System.in, System.out);
        readCasting();
    }   

    public static void main(String[] args) {

    }
}

class Scene {
    ArrayList<Integer> roles;
    int size;

    public Scene(int size) {
        this.size = size;
        roles = new ArrayList<Integer>(size);
    }

    public void addRole(int role) {
        roles.add(role);
    }

    public ArrayList<Integer> getScene() {
        return roles;
    }
}
