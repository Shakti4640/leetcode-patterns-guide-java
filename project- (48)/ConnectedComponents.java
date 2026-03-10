import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ConnectedComponents {

    // ═══════════════════════════════════════════════════════
    // UNION FIND DATA STRUCTURE — Full Implementation
    // ═══════════════════════════════════════════════════════
    
    static class UnionFind {
        
        private int[] parent;    // parent[i] = parent of node i
        private int[] rank;      // rank[i] = approximate height of tree rooted at i
        private int components;  // number of distinct components
        
        // ── Constructor: Initialize n isolated nodes ──
        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            components = n;
            
            for (int i = 0; i < n; i++) {
                parent[i] = i;  // each node is its own root
                rank[i] = 0;    // each tree has height 0
            }
        }
        
        // ── FIND with Path Compression (Recursive) ──
        // Follows parent pointers to root
        // Then makes every node on the path point directly to root
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // path compression
            }
            return parent[x];
        }
        
        // ── FIND with Path Compression (Iterative) ──
        // Safe for large inputs — no stack overflow risk
        public int findIterative(int x) {
            // Phase 1: Find the root
            int root = x;
            while (parent[root] != root) {
                root = parent[root];
            }
            
            // Phase 2: Path compression — flatten the tree
            while (parent[x] != root) {
                int next = parent[x];
                parent[x] = root;
                x = next;
            }
            
            return root;
        }
        
        // ── UNION by Rank ──
        // Merges the components containing x and y
        // Returns TRUE if a merge happened (were in different components)
        // Returns FALSE if already in the same component
        public boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            // Already in the same component
            if (rootX == rootY) {
                return false;
            }
            
            // Union by rank: attach shorter tree under taller tree
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                // Same rank: pick one, increment rank
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            
            // One fewer component
            components--;
            return true;
        }
        
        // ── Check if two nodes are connected ──
        public boolean connected(int x, int y) {
            return find(x) == find(y);
        }
        
        // ── Get current number of components ──
        public int getComponents() {
            return components;
        }
        
        // ── Debug: Get parent array snapshot ──
        public String getParentState() {
            return Arrays.toString(parent);
        }
        
        // ── Debug: Get rank array snapshot ──
        public String getRankState() {
            return Arrays.toString(rank);
        }
    }
    
    // ═══════════════════════════════════════════════════════
    // VARIANT: Union by SIZE instead of Rank
    // ═══════════════════════════════════════════════════════
    
    static class UnionFindBySize {
        
        private int[] parent;
        private int[] size;      // size[i] = number of nodes in tree rooted at i
        private int components;
        
        public UnionFindBySize(int n) {
            parent = new int[n];
            size = new int[n];
            components = n;
            
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;  // each node is a tree of size 1
            }
        }
        
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }
        
        public boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX == rootY) return false;
            
            // Union by size: attach smaller tree under larger tree
            if (size[rootX] < size[rootY]) {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            }
            
            components--;
            return true;
        }
        
        public int getComponentSize(int x) {
            return size[find(x)];
        }
        
        public int getComponents() {
            return components;
        }
    }
    
    // ═══════════════════════════════════════════════════════
    // SOLUTION: Count Connected Components using Union Find
    // ═══════════════════════════════════════════════════════
    
    public static int countComponents(int n, int[][] edges) {
        
        UnionFind uf = new UnionFind(n);
        
        for (int[] edge : edges) {
            uf.union(edge[0], edge[1]);
        }
        
        return uf.getComponents();
    }
    
    // ═══════════════════════════════════════════════════════
    // COMPARISON: BFS-based Component Counting
    // (from Project 17 knowledge — queue-based BFS)
    // ═══════════════════════════════════════════════════════
    
    public static int countComponentsBFS(int n, int[][] edges) {
        
        // Build adjacency list (undirected → add both directions)
        List<List<Integer>> adjList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adjList.add(new ArrayList<>());
        }
        
        for (int[] edge : edges) {
            adjList.get(edge[0]).add(edge[1]);
            adjList.get(edge[1]).add(edge[0]);
        }
        
        boolean[] visited = new boolean[n];
        int components = 0;
        
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                // New component found — BFS from node i
                components++;
                Queue<Integer> queue = new LinkedList<>();
                queue.offer(i);
                visited[i] = true;
                
                while (!queue.isEmpty()) {
                    int current = queue.poll();
                    for (int neighbor : adjList.get(current)) {
                        if (!visited[neighbor]) {
                            visited[neighbor] = true;
                            queue.offer(neighbor);
                        }
                    }
                }
            }
        }
        
        return components;
    }
    
    // ═══════════════════════════════════════════════════════
    // TRACE: Visualize Union Find Step by Step
    // ═══════════════════════════════════════════════════════
    
    public static void countComponentsWithTrace(int n, int[][] edges) {
        
        System.out.println("Nodes: 0 to " + (n - 1));
        System.out.println("Edges:");
        if (edges.length == 0) {
            System.out.println("  (none)");
        }
        for (int[] e : edges) {
            System.out.println("  " + e[0] + " — " + e[1]);
        }
        
        UnionFind uf = new UnionFind(n);
        
        System.out.println("\nInitial state:");
        System.out.println("  parent: " + uf.getParentState());
        System.out.println("  rank:   " + uf.getRankState());
        System.out.println("  components: " + uf.getComponents());
        System.out.println("\n─── Processing Edges ───");
        
        int step = 1;
        for (int[] edge : edges) {
            int a = edge[0];
            int b = edge[1];
            
            int rootA = uf.find(a);
            int rootB = uf.find(b);
            
            System.out.println("\nStep " + step + ": Edge [" + a + ", " + b + "]");
            System.out.println("  find(" + a + ") = " + rootA);
            System.out.println("  find(" + b + ") = " + rootB);
            
            if (rootA == rootB) {
                System.out.println("  → Same root! Already connected → skip");
            } else {
                uf.union(a, b);
                System.out.println("  → Different roots → MERGE components");
            }
            
            System.out.println("  parent: " + uf.getParentState());
            System.out.println("  rank:   " + uf.getRankState());
            System.out.println("  components: " + uf.getComponents());
            
            step++;
        }
        
        System.out.println("\n═══ ANSWER: " + uf.getComponents() + " connected components ═══");
    }
    
    // ═══════════════════════════════════════════════════════
    // DEMO: Dynamic Connectivity Queries
    // Shows WHY Union Find is powerful beyond simple counting
    // ═══════════════════════════════════════════════════════
    
    public static void dynamicConnectivityDemo() {
        
        System.out.println("─── Dynamic Connectivity Demo ───");
        System.out.println("Simulating a social network with friend connections\n");
        
        String[] names = {"Alice", "Bob", "Charlie", "Diana", "Eve", "Frank"};
        int n = names.length;
        UnionFindBySize uf = new UnionFindBySize(n);
        
        // Initial state
        System.out.println("Initial: " + n + " people, each in their own group");
        
        // Simulate friend connections arriving one by one
        int[][] friendships = {{0,1}, {2,3}, {4,5}, {1,2}, {0,4}};
        String[][] friendNames = {
            {"Alice", "Bob"},
            {"Charlie", "Diana"},
            {"Eve", "Frank"},
            {"Bob", "Charlie"},
            {"Alice", "Eve"}
        };
        
        for (int i = 0; i < friendships.length; i++) {
            int a = friendships[i][0];
            int b = friendships[i][1];
            
            System.out.println("\n→ " + friendNames[i][0] 
                + " befriends " + friendNames[i][1]);
            
            boolean merged = uf.union(a, b);
            
            if (merged) {
                System.out.println("  Groups merged! Components: " 
                    + uf.getComponents());
                System.out.println("  " + friendNames[i][0] + "'s group size: " 
                    + uf.getComponentSize(a));
            } else {
                System.out.println("  Already in same group!");
            }
            
            // Query: Are Alice and Diana connected?
            boolean aliceDiana = uf.find(0) == uf.find(3);
            System.out.println("  Are Alice and Diana connected? " + aliceDiana);
        }
        
        // Final state
        System.out.println("\n─── Final State ───");
        System.out.println("Components: " + uf.getComponents());
        for (int i = 0; i < n; i++) {
            System.out.println("  " + names[i] + " → root: " 
                + names[uf.find(i)] + " (group size: " 
                + uf.getComponentSize(i) + ")");
        }
    }
    
    // ═══════════════════════════════════════════════════════
    // DEMO: Path Compression Visualization
    // ═══════════════════════════════════════════════════════
    
    public static void pathCompressionDemo() {
        
        System.out.println("─── Path Compression Visualization ───\n");
        
        int n = 8;
        UnionFind uf = new UnionFind(n);
        
        // Build a chain: 0←1←2←3←4←5←6←7
        // By carefully controlling union order
        System.out.println("Building a chain: 0 ← 1 ← 2 ← 3 ← 4 ← 5 ← 6 ← 7");
        
        // Directly manipulate parent for demonstration
        // (In practice, union() would handle this)
        int[] parent = {0, 0, 1, 2, 3, 4, 5, 6};
        // This creates: 7→6→5→4→3→2→1→0
        
        System.out.println("Before path compression:");
        System.out.println("  parent: " + Arrays.toString(parent));
        System.out.println("  Tree shape:");
        System.out.println("    0 ← 1 ← 2 ← 3 ← 4 ← 5 ← 6 ← 7");
        System.out.println("  find(7) would traverse: 7→6→5→4→3→2→1→0 (7 hops)\n");
        
        // Simulate find with path compression
        System.out.println("Calling find(7) with path compression...");
        
        // Find root
        int root = 7;
        List<Integer> path = new ArrayList<>();
        while (parent[root] != root) {
            path.add(root);
            root = parent[root];
        }
        path.add(root);
        System.out.println("  Path followed: " + path);
        System.out.println("  Root found: " + root);
        
        // Compress path
        for (int node : path) {
            parent[node] = root;
        }
        
        System.out.println("\nAfter path compression:");
        System.out.println("  parent: " + Arrays.toString(parent));
        System.out.println("  Tree shape:");
        System.out.println("         0");
        System.out.println("       / | \\ \\ \\ \\ \\");
        System.out.println("      1  2  3 4 5 6 7");
        System.out.println("  find(7) now: 7→0 (1 hop!)");
        System.out.println("  find(ANY) now: at most 1 hop!");
    }
    
    // ═══════════════════════════════════════════════════════
    // APPLICATION: Is Graph a Valid Tree?
    // A tree has exactly n-1 edges and is connected (1 component)
    // ═══════════════════════════════════════════════════════
    
    public static boolean isValidTree(int n, int[][] edges) {
        
        // A tree with n nodes has exactly n-1 edges
        if (edges.length != n - 1) return false;
        
        UnionFind uf = new UnionFind(n);
        
        for (int[] edge : edges) {
            // If union returns false → edge connects already-connected nodes → cycle!
            if (!uf.union(edge[0], edge[1])) {
                return false; // cycle detected
            }
        }
        
        // All edges processed, no cycle, exactly n-1 edges → tree
        return uf.getComponents() == 1;
    }
    
    // ═══════════════════════════════════════════════════════
    // APPLICATION: Earliest Time When Everyone Becomes Friends
    // Edges arrive with timestamps — when does one component remain?
    // ═══════════════════════════════════════════════════════
    
    public static int earliestFullConnection(int n, int[][] timestampedEdges) {
        // timestampedEdges[i] = [timestamp, nodeA, nodeB]
        // Already sorted by timestamp
        
        UnionFind uf = new UnionFind(n);
        
        for (int[] edge : timestampedEdges) {
            int time = edge[0];
            uf.union(edge[1], edge[2]);
            
            if (uf.getComponents() == 1) {
                return time; // everyone connected!
            }
        }
        
        return -1; // never fully connected
    }

    // ═══════════════════════════════════════════════════════
    // MAIN — Run all examples
    // ═══════════════════════════════════════════════════════
    
    public static void main(String[] args) {
        
        System.out.println("╔═════════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 48: Number of Connected Components             ║");
        System.out.println("║  Pattern: Union Find → Path Compression → Union by Rank ║");
        System.out.println("╚═════════════════════════════════════════════════════════╝");
        System.out.println();
        
        // ── TEST 1: Two components ──
        System.out.println("═══ TEST 1: Two Components ═══");
        int[][] edges1 = {{0,1}, {1,2}, {3,4}};
        countComponentsWithTrace(5, edges1);
        System.out.println();
        
        // ── TEST 2: One component (chain) ──
        System.out.println("═══ TEST 2: One Component (Chain) ═══");
        int[][] edges2 = {{0,1}, {1,2}, {2,3}, {3,4}};
        countComponentsWithTrace(5, edges2);
        System.out.println();
        
        // ── TEST 3: No edges ──
        System.out.println("═══ TEST 3: No Edges ═══");
        int[][] edges3 = {};
        countComponentsWithTrace(4, edges3);
        System.out.println();
        
        // ── TEST 4: Merging two groups ──
        System.out.println("═══ TEST 4: Merging Two Groups ═══");
        int[][] edges4 = {{0,1}, {2,3}, {0,2}};
        countComponentsWithTrace(5, edges4);
        System.out.println();
        
        // ── TEST 5: Redundant edge (already connected) ──
        System.out.println("═══ TEST 5: Redundant Edge ═══");
        int[][] edges5 = {{0,1}, {1,2}, {0,2}};
        countComponentsWithTrace(3, edges5);
        System.out.println();
        
        // ── TEST 6: Larger graph ──
        System.out.println("═══ TEST 6: Larger Graph ═══");
        int[][] edges6 = {{0,1}, {2,3}, {4,5}, {6,7}, {1,3}, {5,7}, {3,7}};
        countComponentsWithTrace(9, edges6);
        System.out.println();
        
        // ── TEST 7: Cross-verification with BFS ──
        System.out.println("═══ TEST 7: Cross-Verification (Union Find vs BFS) ═══");
        int[][][] allEdges = {
            {{0,1}, {1,2}, {3,4}},
            {{0,1}, {1,2}, {2,3}, {3,4}},
            {},
            {{0,1}, {2,3}, {0,2}},
            {{0,1}, {1,2}, {0,2}},
            {{0,1}, {2,3}, {4,5}, {6,7}, {1,3}, {5,7}, {3,7}},
        };
        int[] nodeCountList = {5, 5, 4, 5, 3, 9};
        
        for (int t = 0; t < allEdges.length; t++) {
            int ufResult = countComponents(nodeCountList[t], allEdges[t]);
            int bfsResult = countComponentsBFS(nodeCountList[t], allEdges[t]);
            String status = (ufResult == bfsResult) ? "✓ MATCH" : "✗ MISMATCH";
            System.out.println("  Test " + (t + 1) 
                + ": UF=" + ufResult 
                + "  BFS=" + bfsResult 
                + "  " + status);
        }
        System.out.println();
        
        // ── TEST 8: Path Compression Demo ──
        System.out.println("═══ TEST 8: Path Compression Demo ═══");
        pathCompressionDemo();
        System.out.println();
        
        // ── TEST 9: Dynamic Connectivity ──
        System.out.println("═══ TEST 9: Dynamic Connectivity (Social Network) ═══");
        dynamicConnectivityDemo();
        System.out.println();
        
        // ── TEST 10: Is Valid Tree? ──
        System.out.println("═══ TEST 10: Is Valid Tree? ═══");
        
        int[][] treeEdges1 = {{0,1}, {0,2}, {0,3}, {1,4}};
        System.out.println("  5 nodes, edges [[0,1],[0,2],[0,3],[1,4]]: " 
            + isValidTree(5, treeEdges1));  // true
        
        int[][] treeEdges2 = {{0,1}, {1,2}, {2,3}, {1,3}, {1,4}};
        System.out.println("  5 nodes, edges [[0,1],[1,2],[2,3],[1,3],[1,4]]: " 
            + isValidTree(5, treeEdges2));  // false (cycle: 1-2-3-1)
        
        int[][] treeEdges3 = {{0,1}, {2,3}};
        System.out.println("  4 nodes, edges [[0,1],[2,3]]: " 
            + isValidTree(4, treeEdges3));  // false (disconnected)
        System.out.println();
        
        // ── TEST 11: Earliest Full Connection ──
        System.out.println("═══ TEST 11: Earliest Full Connection ═══");
        int[][] timestamped = {
            {1, 0, 1}, {2, 2, 3}, {3, 1, 3}, {4, 0, 4}, {5, 3, 4}
        };
        int earliest = earliestFullConnection(5, timestamped);
        System.out.println("  5 people, connections arrive over time");
        System.out.println("  Everyone connected at time: " + earliest);
        System.out.println();
        
        // ── TEST 12: Performance Benchmark ──
        System.out.println("═══ TEST 12: Performance Benchmark ═══");
        int size = 500000;
        int edgeCount = 2000000;
        int[][] largeEdges = new int[edgeCount][2];
        java.util.Random rand = new java.util.Random(42);
        for (int i = 0; i < edgeCount; i++) {
            largeEdges[i][0] = rand.nextInt(size);
            largeEdges[i][1] = rand.nextInt(size);
        }
        
        // Union Find
        long startTime = System.nanoTime();
        int ufResult = countComponents(size, largeEdges);
        long ufTime = System.nanoTime() - startTime;
        
        // BFS
        startTime = System.nanoTime();
        int bfsResult = countComponentsBFS(size, largeEdges);
        long bfsTime = System.nanoTime() - startTime;
        
        System.out.println("  Nodes: " + size + "  Edges: " + edgeCount);
        System.out.println("  Union Find: " + ufResult 
            + " components → " + (ufTime / 1_000_000) + " ms");
        System.out.println("  BFS:        " + bfsResult 
            + " components → " + (bfsTime / 1_000_000) + " ms");
        System.out.println("  Results match: " + (ufResult == bfsResult));
    }
}