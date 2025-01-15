import java.util.*;

public class heurRole {
    Kattio io;

    HashMap<Integer, ArrayList<Integer>> assignedRoles; // Key: Actor, Value: Roles
    HashMap<Integer, ArrayList<Integer>> actors; // Key: Role, Value: Actors
    ArrayList<Scene> scenes;                     // List of all scenes

    int div1;
    int div2;
    int n; // Number of roles
    int s; // Number of scenes
    int k; // Number of actors

    public void readCasting() {
        n = io.getInt();
        s = io.getInt();
        k = io.getInt();

        assignedRoles = new HashMap<Integer, ArrayList<Integer>>();
        actors = new HashMap<Integer, ArrayList<Integer>>();
        scenes = new ArrayList<Scene>(s);

        for (int role = 1; role <= n; role++) {
            int actorCount = io.getInt();
            actors.put(role, new ArrayList<Integer>(actorCount));
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
            scenes.add(scene);
        }
    }

    public boolean canHave(int actor, int roll){// check if actor can play the roll
        for (Scene scene : scenes) {
            if (scene.getScene().contains(roll)) {
                if (actor == 1 && assignedRoles.containsKey(2)) {
                    for (int r : assignedRoles.get(2)) {
                        if (scene.getScene().contains(r)) {
                            return false;
                        }
                    }
                }
                if (actor == 2 && assignedRoles.containsKey(1)) {
                    for (int r : assignedRoles.get(1)) {
                        if (scene.getScene().contains(r)) {
                            return false;
                        }
                    }
                }
                
                for (int role : scene.getScene()) {
                    if (roll != role && assignedRoles.containsKey(actor) && assignedRoles.get(actor).contains(role)) { // Actor already has a role in the scene
                        return false;
                    }
                }
            }
        }
        return true;
    }
public void giveDivasRolls() {
    // Step 1: Retrieve allowed roles for actors 1 and 2
    ArrayList<Integer> allowedRolesForActor1 = new ArrayList<>();
    ArrayList<Integer> allowedRolesForActor2 = new ArrayList<>();

    for (Map.Entry<Integer, ArrayList<Integer>> entry : actors.entrySet()) {
        int role = entry.getKey();
        ArrayList<Integer> allowedActors = entry.getValue();

        if (allowedActors.contains(1)) {
            allowedRolesForActor1.add(role);
        }
        if (allowedActors.contains(2)) {
            allowedRolesForActor2.add(role);
        }
    }

    // Step 2: Find two roles that are valid and never appear together in a scene
    int roleForActor1 = -1, roleForActor2 = -1;

    for (int role1 : allowedRolesForActor1) {
        for (int role2 : allowedRolesForActor2) {
            if (role1 == role2) continue; // Skip if both roles are the same

            boolean neverTogether = true;

            // Check all scenes to see if these roles are ever together
            for (Scene scene : scenes) {
                if (scene.getScene().contains(role1) && scene.getScene().contains(role2)) {
                    neverTogether = false;
                    break;
                }
            }

            // If valid pair is found, assign roles and exit loops
            if (neverTogether) {
                roleForActor1 = role1;
                roleForActor2 = role2;
                break;
            }
        }
        if (roleForActor1 != -1 && roleForActor2 != -1) {
            break;
        }
    }


    // Step 2: Assign the found roles to actors 1 and 2
    if (roleForActor1 != -1 && roleForActor2 != -1) {
        assignedRoles.computeIfAbsent(1, k -> new ArrayList<>());
        assignedRoles.get(1).add(roleForActor1);
        div1= roleForActor1;

        assignedRoles.computeIfAbsent(2, k -> new ArrayList<>());
        assignedRoles.get(2).add(roleForActor2);
        div2 = roleForActor2;
    }
}



    public void giveRolls(){

        for (int roll = 1; roll <= n; roll++) {//kollar alla roller
            if (roll == div1 || roll == div2) {
                continue;
            }
            boolean given = false;
            for (int actor : actors.get(roll)) {//kollar de möjliga actors
                if(canHave(actor,roll)){
                    assignedRoles.computeIfAbsent(actor, k -> new ArrayList<Integer>());
                    assignedRoles.get(actor).add(roll);
                    given = true;
                    break;
                }
            }
            if (!given){ // skapar en ny om inget av de givna fungerar
                assignedRoles.put(k + 1, new ArrayList<Integer>());
                assignedRoles.get(k + 1).add(roll);
                k++;
            }
        }
    }

    public void print() {
        io.println(assignedRoles.size());
 
        for (Map.Entry<Integer, ArrayList<Integer>> entry : assignedRoles.entrySet()) {
            io.print(entry.getKey() + " ");
            io.print(entry.getValue().size() + " ");
            for (int role : entry.getValue()) {
                io.print(role + " ");
            }
            io.println();
        }
    }

    public heurRole() {
        io = new Kattio(System.in, System.out);
        readCasting();
        giveDivasRolls();
        giveRolls();
        print();
        io.close();
    }   

    public static void main(String[] args) {
        new heurRole();
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
