import java.util.*;

public class Instance {
    public HashMap<Integer, HashSet<Integer>> assignedRolesForActor;  // Key: Actor, Value: Roles[]
    public HashMap<Integer, Integer> assignedActorForRole;            // Key: Role, Value: Actor
    public int superActorCount;                                       // Number of superactors


    // Constructor
    public Instance() {
        this.assignedRolesForActor = new HashMap<>();
        this.assignedActorForRole = new HashMap<>();
        this.superActorCount = 0;
    }


    // Copy constructor
    public Instance(Instance instance) {
        this.assignedRolesForActor = new HashMap<>();
        for (Map.Entry<Integer, HashSet<Integer>> entry : instance.assignedRolesForActor.entrySet()) {
            this.assignedRolesForActor.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }
        this.assignedActorForRole = new HashMap<>(instance.assignedActorForRole);
        this.setSuperActorCount(instance.getSuperActorCount());
    }


    // Getters and setters
    public HashSet<Integer> getRolesForActor(int actor) {
        return assignedRolesForActor.getOrDefault(actor, new HashSet<>());
    }

    public int getActorForRole(int role) {
        return assignedActorForRole.getOrDefault(role, -1);
    }

    public int getTotalActors() {
        return assignedRolesForActor.size();
    }

    public int getSuperActorCount() {
        return superActorCount;
    }

    public int getActorWithLeastRoles() {
        int actorWithLeastRoles = -1;
        int minRoles = Integer.MAX_VALUE;
        for (Map.Entry<Integer, HashSet<Integer>> entry : assignedRolesForActor.entrySet()) {
            if (entry.getValue().size() < minRoles) {
                actorWithLeastRoles = entry.getKey();
                minRoles = entry.getValue().size();
            }
        }
        return actorWithLeastRoles;
    }

    
    public void addRoleForActor(int actor, int role) {
        this.assignedRolesForActor.computeIfAbsent(actor, k -> new HashSet<>()).add(role);
        this.assignedActorForRole.put(role, actor);
    }

    public void removeRoleForActor(int actor, int role) {
        this.assignedRolesForActor.get(actor).remove(role);
        if (this.assignedRolesForActor.get(actor).isEmpty()) {
            this.assignedRolesForActor.remove(actor);
        }
        this.assignedActorForRole.remove(role);
    }
    
    public void setSuperActorCount(int superActorCount) {
        this.superActorCount = superActorCount;
    }

    public void incSuperActorCount() {
        this.superActorCount++;
    }

    public void decSuperActorCount() {
        this.superActorCount--;
    }
}
