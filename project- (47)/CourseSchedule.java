import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Arrays;

public class CourseSchedule {

    // ─────────────────────────────────────────────────────────
    // COURSE SCHEDULE I: Can Finish All Courses? (LeetCode 207)
    // Kahn's BFS Algorithm
    // ─────────────────────────────────────────────────────────
    public static boolean canFinish(int numCourses, int[][] prerequisites) {
        
        // Edge case: no courses or no prerequisites
        if (numCourses <= 0) return true;
        if (prerequisites == null || prerequisites.length == 0) return true;
        
        // ── Step 1: Build the graph ──
        // Adjacency list: adjList[i] = list of courses that DEPEND on course i
        // In-degree array: inDegree[i] = number of prerequisites for course i
        
        List<List<Integer>> adjList = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            adjList.add(new ArrayList<>());
        }
        
        int[] inDegree = new int[numCourses];
        
        for (int[] prereq : prerequisites) {
            int course = prereq[0];      // course to take
            int prerequisite = prereq[1]; // must be completed first
            
            // Edge: prerequisite → course
            adjList.get(prerequisite).add(course);
            inDegree[course]++;
        }
        
        // ── Step 2: Initialize queue with all in-degree 0 nodes ──
        // These courses have NO prerequisites → can be taken immediately
        
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }
        
        // ── Step 3: BFS — Process courses layer by layer ──
        int processedCount = 0;
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            processedCount++;
            
            // For each course that depends on 'current'
            for (int neighbor : adjList.get(current)) {
                // Remove the edge: current → neighbor (conceptually)
                inDegree[neighbor]--;
                
                // If all prerequisites for neighbor are now met
                if (inDegree[neighbor] == 0) {
                    queue.offer(neighbor);
                }
            }
        }
        
        // ── Step 4: Cycle detection ──
        // If all courses processed → no cycle → can finish
        // If some unprocessed → trapped in cycle → cannot finish
        return processedCount == numCourses;
    }
    
    // ─────────────────────────────────────────────────────────
    // COURSE SCHEDULE II: Return Valid Order (LeetCode 210)
    // Kahn's BFS Algorithm
    // ─────────────────────────────────────────────────────────
    public static int[] findOrder(int numCourses, int[][] prerequisites) {
        
        if (numCourses <= 0) return new int[0];
        
        // ── Step 1: Build graph (same as Course Schedule I) ──
        List<List<Integer>> adjList = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            adjList.add(new ArrayList<>());
        }
        
        int[] inDegree = new int[numCourses];
        
        if (prerequisites != null) {
            for (int[] prereq : prerequisites) {
                int course = prereq[0];
                int prerequisite = prereq[1];
                adjList.get(prerequisite).add(course);
                inDegree[course]++;
            }
        }
        
        // ── Step 2: Initialize queue ──
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }
        
        // ── Step 3: BFS — Build the order ──
        int[] order = new int[numCourses];
        int index = 0;
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            order[index++] = current;  // Record this course in the order
            
            for (int neighbor : adjList.get(current)) {
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0) {
                    queue.offer(neighbor);
                }
            }
        }
        
        // ── Step 4: Return order or empty array ──
        if (index == numCourses) {
            return order;       // Valid topological order
        } else {
            return new int[0];  // Cycle exists → impossible
        }
    }
    
    // ─────────────────────────────────────────────────────────
    // DFS-BASED TOPOLOGICAL SORT (Alternative Approach)
    // Uses 3-state marking: UNVISITED, IN_PROGRESS, COMPLETED
    // ─────────────────────────────────────────────────────────
    
    private static final int UNVISITED = 0;
    private static final int IN_PROGRESS = 1;
    private static final int COMPLETED = 2;
    
    public static boolean canFinishDFS(int numCourses, int[][] prerequisites) {
        
        if (numCourses <= 0) return true;
        
        // Build adjacency list
        List<List<Integer>> adjList = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            adjList.add(new ArrayList<>());
        }
        
        if (prerequisites != null) {
            for (int[] prereq : prerequisites) {
                adjList.get(prereq[1]).add(prereq[0]);
            }
        }
        
        int[] state = new int[numCourses]; // all UNVISITED initially
        
        // Try DFS from every unvisited node
        for (int i = 0; i < numCourses; i++) {
            if (state[i] == UNVISITED) {
                if (hasCycleDFS(i, adjList, state)) {
                    return false; // Cycle found → cannot finish
                }
            }
        }
        
        return true; // No cycle → can finish
    }
    
    private static boolean hasCycleDFS(
            int node, 
            List<List<Integer>> adjList, 
            int[] state) {
        
        // Mark as currently being explored
        state[node] = IN_PROGRESS;
        
        for (int neighbor : adjList.get(node)) {
            if (state[neighbor] == IN_PROGRESS) {
                // Encountered a node in current DFS path → CYCLE!
                return true;
            }
            if (state[neighbor] == UNVISITED) {
                if (hasCycleDFS(neighbor, adjList, state)) {
                    return true;
                }
            }
            // If COMPLETED → already fully explored → skip (no cycle through here)
        }
        
        // All neighbors explored without finding cycle
        state[node] = COMPLETED;
        return false;
    }
    
    // ─────────────────────────────────────────────────────────
    // DFS-BASED ORDER (for Course Schedule II)
    // ─────────────────────────────────────────────────────────
    public static int[] findOrderDFS(int numCourses, int[][] prerequisites) {
        
        if (numCourses <= 0) return new int[0];
        
        List<List<Integer>> adjList = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            adjList.add(new ArrayList<>());
        }
        
        if (prerequisites != null) {
            for (int[] prereq : prerequisites) {
                adjList.get(prereq[1]).add(prereq[0]);
            }
        }
        
        int[] state = new int[numCourses];
        List<Integer> reverseOrder = new ArrayList<>();
        
        for (int i = 0; i < numCourses; i++) {
            if (state[i] == UNVISITED) {
                if (hasCycleDFSWithOrder(i, adjList, state, reverseOrder)) {
                    return new int[0]; // Cycle → impossible
                }
            }
        }
        
        // Reverse the postorder to get topological order
        int[] order = new int[numCourses];
        for (int i = 0; i < numCourses; i++) {
            order[i] = reverseOrder.get(numCourses - 1 - i);
        }
        return order;
    }
    
    private static boolean hasCycleDFSWithOrder(
            int node,
            List<List<Integer>> adjList,
            int[] state,
            List<Integer> reverseOrder) {
        
        state[node] = IN_PROGRESS;
        
        for (int neighbor : adjList.get(node)) {
            if (state[neighbor] == IN_PROGRESS) return true;
            if (state[neighbor] == UNVISITED) {
                if (hasCycleDFSWithOrder(neighbor, adjList, state, reverseOrder)) {
                    return true;
                }
            }
        }
        
        state[node] = COMPLETED;
        reverseOrder.add(node); // Add AFTER all descendants processed (postorder)
        return false;
    }
    
    // ─────────────────────────────────────────────────────────
    // TRACE FUNCTION — Visualize Kahn's BFS
    // ─────────────────────────────────────────────────────────
    public static void topologicalSortWithTrace(int numCourses, int[][] prerequisites) {
        
        System.out.println("Courses: 0 to " + (numCourses - 1));
        System.out.println("Prerequisites:");
        if (prerequisites.length == 0) {
            System.out.println("  (none)");
        }
        for (int[] p : prerequisites) {
            System.out.println("  Course " + p[0] + " requires Course " + p[1]
                + "  (edge: " + p[1] + " → " + p[0] + ")");
        }
        
        // Build graph
        List<List<Integer>> adjList = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            adjList.add(new ArrayList<>());
        }
        
        int[] inDegree = new int[numCourses];
        
        for (int[] prereq : prerequisites) {
            adjList.get(prereq[1]).add(prereq[0]);
            inDegree[prereq[0]]++;
        }
        
        System.out.println("\nAdjacency List:");
        for (int i = 0; i < numCourses; i++) {
            System.out.println("  " + i + " → " + adjList.get(i));
        }
        
        System.out.println("\nIn-degrees: " + Arrays.toString(inDegree));
        
        // Initialize queue
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }
        
        System.out.println("Initial queue (in-degree 0): " + queue);
        System.out.println("\n─── BFS Processing ───");
        
        List<Integer> order = new ArrayList<>();
        int step = 1;
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            order.add(current);
            
            System.out.println("\nStep " + step + ": Process course " + current);
            System.out.println("  Neighbors: " + adjList.get(current));
            
            for (int neighbor : adjList.get(current)) {
                inDegree[neighbor]--;
                System.out.println("  → inDegree[" + neighbor + "] decremented to " 
                    + inDegree[neighbor]);
                
                if (inDegree[neighbor] == 0) {
                    queue.offer(neighbor);
                    System.out.println("    → Course " + neighbor 
                        + " now has in-degree 0 → ENQUEUED");
                }
            }
            
            System.out.println("  Queue: " + queue);
            System.out.println("  Order so far: " + order);
            step++;
        }
        
        System.out.println("\n─── Result ───");
        System.out.println("Processed: " + order.size() + " / " + numCourses);
        
        if (order.size() == numCourses) {
            System.out.println("✓ All courses can be completed!");
            System.out.println("Valid order: " + order);
        } else {
            System.out.println("✗ CYCLE DETECTED — cannot complete all courses!");
            
            // Identify nodes in cycle (still have in-degree > 0)
            List<Integer> cycleNodes = new ArrayList<>();
            for (int i = 0; i < numCourses; i++) {
                if (inDegree[i] > 0) {
                    cycleNodes.add(i);
                }
            }
            System.out.println("Nodes trapped in cycle(s): " + cycleNodes);
        }
    }
    
    // ─────────────────────────────────────────────────────────
    // VALIDATION HELPER — Verify a topological order is correct
    // ─────────────────────────────────────────────────────────
    public static boolean isValidTopologicalOrder(
            int[] order, int numCourses, int[][] prerequisites) {
        
        if (order.length != numCourses) return false;
        
        // Build position map: position[course] = index in order
        int[] position = new int[numCourses];
        boolean[] seen = new boolean[numCourses];
        
        for (int i = 0; i < order.length; i++) {
            if (order[i] < 0 || order[i] >= numCourses) return false;
            if (seen[order[i]]) return false; // duplicate
            seen[order[i]] = true;
            position[order[i]] = i;
        }
        
        // Check all prerequisites are satisfied
        for (int[] prereq : prerequisites) {
            int course = prereq[0];
            int prerequisite = prereq[1];
            
            // prerequisite must appear BEFORE course in the order
            if (position[prerequisite] >= position[course]) {
                return false;
            }
        }
        
        return true;
    }
    
    // ─────────────────────────────────────────────────────────
    // MAIN — Run all examples
    // ─────────────────────────────────────────────────────────
    public static void main(String[] args) {
        
        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 47: Course Schedule (Topological Sort)    ║");
        System.out.println("║  Pattern: In-Degree Counting → Kahn's BFS         ║");
        System.out.println("╚════════════════════════════════════════════════════╝");
        System.out.println();
        
        // ── TEST 1: Standard DAG ──
        System.out.println("═══ TEST 1: Standard DAG ═══");
        int[][] prereqs1 = {{1,0}, {2,0}, {3,1}, {3,2}};
        topologicalSortWithTrace(4, prereqs1);
        System.out.println();
        
        // ── TEST 2: Cycle Detection ──
        System.out.println("═══ TEST 2: Cycle Detection ═══");
        int[][] prereqs2 = {{1,0}, {0,1}};
        topologicalSortWithTrace(2, prereqs2);
        System.out.println();
        
        // ── TEST 3: Disconnected Nodes ──
        System.out.println("═══ TEST 3: Disconnected Nodes ═══");
        int[][] prereqs3 = {{1,0}};
        topologicalSortWithTrace(3, prereqs3);
        System.out.println();
        
        // ── TEST 4: Larger DAG ──
        System.out.println("═══ TEST 4: Larger DAG ═══");
        int[][] prereqs4 = {{2,0}, {3,0}, {4,1}, {4,2}, {5,3}, {5,4}};
        topologicalSortWithTrace(6, prereqs4);
        System.out.println();
        
        // ── TEST 5: Three-node cycle ──
        System.out.println("═══ TEST 5: Three-Node Cycle ═══");
        int[][] prereqs5 = {{1,0}, {2,1}, {0,2}};
        topologicalSortWithTrace(3, prereqs5);
        System.out.println();
        
        // ── TEST 6: No prerequisites ──
        System.out.println("═══ TEST 6: No Prerequisites ═══");
        int[][] prereqs6 = {};
        topologicalSortWithTrace(4, prereqs6);
        System.out.println();
        
        // ── TEST 7: Cross-verification of all approaches ──
        System.out.println("═══ TEST 7: Cross-Verification (All Approaches) ═══");
        
        int[][][] allPrereqs = {
            {{1,0}, {2,0}, {3,1}, {3,2}},       // DAG
            {{1,0}, {0,1}},                       // cycle
            {{1,0}},                              // disconnected
            {{2,0}, {3,0}, {4,1}, {4,2}, {5,3}, {5,4}}, // larger DAG
            {{1,0}, {2,1}, {0,2}},               // 3-node cycle
            {},                                    // no prereqs
            {{1,0}, {2,1}, {3,2}, {4,3}},        // linear chain
        };
        int[] numCoursesList = {4, 2, 3, 6, 3, 4, 5};
        
        for (int t = 0; t < allPrereqs.length; t++) {
            int n = numCoursesList[t];
            int[][] p = allPrereqs[t];
            
            boolean kahns = canFinish(n, p);
            boolean dfs = canFinishDFS(n, p);
            int[] kahnsOrder = findOrder(n, p);
            int[] dfsOrder = findOrderDFS(n, p);
            
            boolean kahnsOrderValid = kahnsOrder.length == 0 
                ? !kahns 
                : isValidTopologicalOrder(kahnsOrder, n, p);
            boolean dfsOrderValid = dfsOrder.length == 0 
                ? !dfs 
                : isValidTopologicalOrder(dfsOrder, n, p);
            
            String status = (kahns == dfs && kahnsOrderValid && dfsOrderValid)
                ? "✓" : "✗";
                
            System.out.println("  Test " + (t + 1) 
                + ": Kahns=" + kahns 
                + "  DFS=" + dfs
                + "  KahnsOrder=" + Arrays.toString(kahnsOrder)
                + "  DFSOrder=" + Arrays.toString(dfsOrder)
                + "  " + status);
        }
        System.out.println();
        
        // ── TEST 8: Mixed — DAG with cycle ──
        System.out.println("═══ TEST 8: Partial Cycle (Some nodes processable) ═══");
        // Courses 0→1 (fine), 2→3→4→2 (cycle)
        int[][] prereqs8 = {{1,0}, {3,2}, {4,3}, {2,4}};
        topologicalSortWithTrace(5, prereqs8);
        System.out.println();
        
        // ── TEST 9: Performance benchmark ──
        System.out.println("═══ TEST 9: Performance Benchmark ═══");
        int numC = 200000;
        List<int[]> prereqList = new ArrayList<>();
        // Build a wide DAG: course i depends on course i/2 (binary tree structure)
        for (int i = 1; i < numC; i++) {
            prereqList.add(new int[]{i, i / 2});
        }
        int[][] largePrereqs = prereqList.toArray(new int[0][]);
        
        long startTime = System.nanoTime();
        boolean result1 = canFinish(numC, largePrereqs);
        long kahnsTime = System.nanoTime() - startTime;
        
        startTime = System.nanoTime();
        boolean result2 = canFinishDFS(numC, largePrereqs);
        long dfsTime = System.nanoTime() - startTime;
        
        startTime = System.nanoTime();
        int[] orderResult = findOrder(numC, largePrereqs);
        long orderTime = System.nanoTime() - startTime;
        
        System.out.println("  Courses: " + numC 
            + "  Prerequisites: " + largePrereqs.length);
        System.out.println("  Kahn's canFinish: " + result1 
            + " → " + (kahnsTime / 1_000_000) + " ms");
        System.out.println("  DFS canFinish:    " + result2 
            + " → " + (dfsTime / 1_000_000) + " ms");
        System.out.println("  Kahn's findOrder: " + (orderResult.length == numC) 
            + " → " + (orderTime / 1_000_000) + " ms");
        System.out.println("  Order valid: " 
            + isValidTopologicalOrder(orderResult, numC, largePrereqs));
    }
}