import java.util.*;

public class betygA {
    // Find two roles that are never together in a scene | Assign them to actors 1 and 2
    // Start with the largest scene and assign actors to the roles greedily
    // Continue in descending order of scene size until all roles have been assigned an actor   
    Kattio io;


    HashMap<Integer, ArrayList<Integer>> assignedRoles; // Key: Actor, Value: Roles
    HashMap<Integer, HashSet<Integer>> rollpairs; // Key: roll, value rolles:
    HashMap<Integer, ArrayList<Integer>> actors; // Key: Role, Value: Actors
    ArrayList<Scene> scenes;                     // List of all scenes


    int div1;
    int div2;
    int n; // Number of roles
    int s; // Number of scenes
    int k; // Number of actors
    int kstart;

    public void readCasting() {
        n = io.getInt();
        s = io.getInt();
        k = io.getInt();
        kstart = k;

        assignedRoles = new HashMap<Integer, ArrayList<Integer>>();
        rollpairs = new HashMap<Integer, HashSet<Integer>>();
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

        for (Scene scene : scenes) {
            ArrayList<Integer> rolesInScene = scene.getScene();
            for (int role : rolesInScene) {
                rollpairs.computeIfAbsent(role, k -> new HashSet<>());
                for (int otherRole : rolesInScene) {
                    if (role != otherRole) {
                        rollpairs.get(role).add(otherRole);
                    }
                }
            }
        }
    }
    

    public void simAnn(int iterations, double initialTemperature, double coolingRate) {
        double temperature = initialTemperature;
    
        for (int iter = 0; iter < iterations; iter++) {
            // Choose two actors at random
            int actorA = getRandomActor();
            int actorB = getRandomActor();
            while (actorA == actorB) {
                actorB = getRandomActor();
            }
    
            // Attempt to transfer or swap roles
            boolean successfulMove = transferRoles(actorA, actorB);
    
            if (!successfulMove) {
                // Evaluate "bad" move if roles can't be transferred
                double currentCost = evaluateSolutionCost();
                performBadMove(actorA, actorB);
                double newCost = evaluateSolutionCost();
    
                if (newCost < currentCost || Math.random() < Math.exp((currentCost - newCost) / temperature)) {
                    // Accept bad move
                } else {
                    // Revert bad move
                    revertLastMove();
                }
            }
    
            // Cool down
            temperature *= coolingRate;
        }
    }
    
    private int getRandomActor() {
        return (int) (Math.random() * k) + 1;
    }
    
    private double evaluateSolutionCost() {
        // Define a heuristic for the "cost" of a solution (e.g., number of unused actors, overlapping roles)
        return 0; // Placeholder
    }
    
    public boolean transferRoles(int actorA, int actorB){
        boolean transferred = false;
        if (assignedRoles.containsKey(actorA)) {
            List<Integer> rolesToTransfer = new ArrayList<>();
            Iterator<Integer> iterator = assignedRoles.get(actorA).iterator();
            while (iterator.hasNext()) {
                int role = iterator.next();
                if (canHave(actorB, role) && actors.get(role).contains(actorB)) {
                    rolesToTransfer.add(role);
                    iterator.remove();
                    transferred = true;
                }
            }
            assignedRoles.computeIfAbsent(actorB, k -> new ArrayList<>());
            assignedRoles.get(actorB).addAll(rolesToTransfer);
        }
        return transferred;
    }

    
    public boolean canHave(int actor, int roll){// check if actor can play the roll
            if (actor == 1 && assignedRoles.containsKey(2)) {
                for (int r : assignedRoles.get(2)) {
                    if (rollpairs.get(roll).contains(r)) {
                        return false;
                    }
                }
            }
            if (actor == 2 && assignedRoles.containsKey(1)) {
                for (int r : assignedRoles.get(1)) {
                    if (rollpairs.get(roll).contains(r)) {
                        return false;
                    }
                }
            }

            if (assignedRoles.containsKey(actor)){
                for (int role : assignedRoles.get(actor)) {
                    if (rollpairs.get(roll).contains(role)) { // Actor already has a role in the scene
                        return false;
                    }
                }
            } else {
                return true;
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

    outerLoop:
    for (int role1 : allowedRolesForActor1) {
        for (int role2 : allowedRolesForActor2) {
            if (role1 == role2) continue; // Skip if both roles are the same

            // Check if these roles are ever together using rollpairs
            if (!rollpairs.get(role1).contains(role2) && !rollpairs.get(role2).contains(role1)) {
                roleForActor1 = role1;
                roleForActor2 = role2;
                break outerLoop;
            }
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

    public betygA() {
        io = new Kattio(System.in, System.out);
        readCasting();
        giveDivasRolls();
        giveRolls();
        simAnn(100, 50, 0.95);
        print();
        io.close();
    }   

    public static void main(String[] args) {
        new betygA();
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
