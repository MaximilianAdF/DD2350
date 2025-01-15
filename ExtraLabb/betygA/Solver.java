import java.io.*;
import java.util.*;

public class Solver {
    private Instance currSolution;
    private Instance bestSolution;
    private FastIO io;

    // Problem-specific parameters
    private boolean[][] roleCollisions;
    private HashMap<Integer, ArrayList<Integer>> actorsForRole; // Key: Role, Value: Actors that can play the role
    private HashMap<Integer, ArrayList<Integer>> rolesForActor; // Key: Actor, Value: Roles that the actor can play
    private int n;  // Number of roles
    private int s;  // Number of scenes
    private int k;  // Max number of normal actors

    // Simulated annealing constants
    private static final double INITIAL_TEMPERATURE = 1.0;
    private static final double MIN_TEMPERATURE = 0.0000000000001;
    private static final double COOLING_RATE = 0.93;

    // Simulated annealing variables
    private double temperature;
    private Random random;

    public void readCasting() {
        n = io.getInt();
        s = io.getInt();
        k = io.getInt();

        roleCollisions = new boolean[n+1][n+1];
        actorsForRole = new HashMap<>();
        rolesForActor = new HashMap<>();

        for (int role = 1; role <= n; role++) {
            actorsForRole.put(role, new ArrayList<>());
            int actorCount = io.getInt();
            for (int i = 0; i < actorCount; i++) {
                int actor = io.getInt();
                actorsForRole.get(role).add(actor);
                rolesForActor.computeIfAbsent(actor, x -> new ArrayList<>()).add(role);
            }
        }

        for (int i = 0; i < s; i++) {
            ArrayList<Integer> rolesInScene = new ArrayList<>();
            int roleCount = io.getInt();

            for (int j = 0; j < roleCount; j++) {
                rolesInScene.add(io.getInt());
            }

            for (int role : rolesInScene) {
                for (int otherRole : rolesInScene) {
                    if (role != otherRole) {
                        roleCollisions[role][otherRole] = true;
                    }
                }
            }
        }
    }

    public void outputCasting() {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(System.out);
            bos.write((bestSolution.assignedRolesForActor.size() + "\n").getBytes());

            for (Map.Entry<Integer, HashSet<Integer>> entry : bestSolution.assignedRolesForActor.entrySet()) {
                int actor = entry.getKey();
                HashSet<Integer> roles = entry.getValue();

                bos.write((actor + " " + roles.size() + " ").getBytes());
                for (int role : roles) {
                    bos.write((role + " ").getBytes());
                }
                bos.write("\n".getBytes());
            }

            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean canPlay(int actor, int role, Instance solution) {
        // Check that the divas don't play in the same scenes
        if (actor == 1 || actor == 2) {
            int otherDiva = actor == 1 ? 2 : 1;
            for (int otherRole : solution.getRolesForActor(otherDiva)) {
                if (roleCollisions[role][otherRole]) {
                    return false;
                }
            }
        }

        // Check if the role assignment doesn't cause actor to play two roles in the same scene
        for (int r : solution.getRolesForActor(actor)) {
            if (roleCollisions[role][r]) {
                return false;
            }
        }
        return true;
    }

    public Instance genInitialSolution() {
        Instance initialSolution = new Instance();
        int divaOne = -1;
        int divaTwo = -1;

        outerLoop:
        for (int role1 : rolesForActor.get(1)) {
            for (int role2 : rolesForActor.get(2)) {
                if (role1 == role2) continue;

                if (!roleCollisions[role1][role2] && !roleCollisions[role2][role1]) {
                    initialSolution.addRoleForActor(1, role1);
                    initialSolution.addRoleForActor(2, role2);
                    divaOne = role1;
                    divaTwo = role2;
                    break outerLoop;
                }
            }
        }

        PriorityQueue<Integer> rolesByActorsCount = new PriorityQueue<>(
        Comparator.comparingInt(role -> actorsForRole.get(role).size()));
        rolesByActorsCount.addAll(actorsForRole.keySet());
    
        while (!rolesByActorsCount.isEmpty()) {
            int role = rolesByActorsCount.poll();
            if (role == divaOne || role == divaTwo) continue;
            boolean assigned = false;
    
            for (int actor : actorsForRole.get(role)) {
                if (canPlay(actor, role, initialSolution)) {
                    initialSolution.addRoleForActor(actor, role);
                    assigned = true;
                    break;
                }
            }
    
            if (!assigned) {
                int superActor = initialSolution.getSuperActorCount() + k + 1;
                initialSolution.addRoleForActor(superActor, role);
                initialSolution.incSuperActorCount();
            }
        }
        return initialSolution;
    }

    public Instance generateNeighbor(Instance solution) {
        Instance neighbor = new Instance(solution);
        int randomRole = random.nextInt(n) + 1;
        int currActor = neighbor.getActorForRole(randomRole);

        // Randomly select a role and reassign it
        while (currActor == 1 || currActor == 2) {
            randomRole = random.nextInt(n) + 1;
            currActor = neighbor.getActorForRole(randomRole);
        }

        ArrayList<Integer> potentialActors = actorsForRole.get(randomRole);
        potentialActors.sort((a, b) -> neighbor.getRolesForActor(b).size() - neighbor.getRolesForActor(a).size());

        for (int newActor : potentialActors) {
            if (newActor != currActor && canPlay(newActor, randomRole, neighbor)) {
                neighbor.removeRoleForActor(currActor, randomRole);
                neighbor.addRoleForActor(newActor, randomRole);
                if (currActor > k) {
                    neighbor.decSuperActorCount();
                }
                break;
            }
        }

        int superActorCount = neighbor.getSuperActorCount();
        // Go through super actors and see if any of them can be removed
        for (int superActor = k + 1; superActor <= superActorCount + k + 1; superActor++) {
            HashSet<Integer> roles = neighbor.getRolesForActor(superActor);
            if (roles.size() == 0) {
                continue;
            }
            int role = roles.iterator().next();
            for (int actor : actorsForRole.get(role)) {
                if (canPlay(actor, role, neighbor)) {
                    neighbor.removeRoleForActor(superActor, role);
                    neighbor.addRoleForActor(actor, role);
                    neighbor.decSuperActorCount();
                    break;
                }
            }
        }
        return neighbor;
    }

    public Solver() {
        io = new FastIO(System.in);
        random = new Random();
        readCasting();

        temperature = INITIAL_TEMPERATURE;
        currSolution = genInitialSolution();
        bestSolution = new Instance(currSolution);

        while (temperature > MIN_TEMPERATURE) {
            for (int i = 0; i < 43; i++) {
                // Generate a new neighbor solution
                Instance neighbor = generateNeighbor(currSolution);

                // Calculate the change in cost/energy
                double deltaEnergy = neighbor.getTotalActors() - currSolution.getTotalActors();
                double superActorPenalty = (1/temperature) * (neighbor.getSuperActorCount() - currSolution.getSuperActorCount());
                deltaEnergy += superActorPenalty;

                // Always accept a better solution
                if (deltaEnergy < 0) {
                    currSolution = neighbor;
                    if (currSolution.getTotalActors() < bestSolution.getTotalActors()) {
                        bestSolution = currSolution;  // Update best solution
                    }
                } else {
                    // Accept a worse solution based on probability
                    double acceptanceProbability = Math.exp(-deltaEnergy / temperature);
                    if (Math.random() < acceptanceProbability) {
                        currSolution = neighbor;
                    }
                }
            }
            temperature *= COOLING_RATE;
        }
        outputCasting();
    }

    public static void main(String[] args) {
        new Solver();
    }
}
