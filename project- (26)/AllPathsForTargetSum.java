import java.util.ArrayList;
import java.util.List;

public class AllPathsForTargetSum {

    // ─────────────────────────────────────────────
    // TreeNode definition
    // ─────────────────────────────────────────────
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
            this.left = null;
            this.right = null;
        }
    }

    // ─────────────────────────────────────────────
    // CORE SOLUTION: DFS + Path Accumulation + Backtracking
    // ─────────────────────────────────────────────
    public static List<List<Integer>> pathSum(TreeNode root, int targetSum) {

        List<List<Integer>> results = new ArrayList<>();
        List<Integer> currentPath = new ArrayList<>();

        dfs(root, targetSum, currentPath, results);

        return results;
    }

    private static void dfs(TreeNode node,
                            int remaining,
                            List<Integer> currentPath,
                            List<List<Integer>> results) {

        // Base case: null node → dead end
        if (node == null) {
            return;
        }

        // ── STEP 1: ADD current node to path ──
        currentPath.add(node.val);

        // ── STEP 2: Update remaining sum ──
        int newRemaining = remaining - node.val;

        // ── STEP 3: Check if LEAF with exact target ──
        boolean isLeaf = (node.left == null && node.right == null);

        if (isLeaf && newRemaining == 0) {
            // Found a valid path → save a DEEP COPY
            results.add(new ArrayList<>(currentPath));
        }

        // ── STEP 4: Recurse into children (even if leaf — they'll hit null base case) ──
        if (!isLeaf) {
            dfs(node.left, newRemaining, currentPath, results);
            dfs(node.right, newRemaining, currentPath, results);
        }

        // ── STEP 5: BACKTRACK — remove current node from path ──
        // This is the CRITICAL step that makes everything work
        currentPath.remove(currentPath.size() - 1);
    }

    // ─────────────────────────────────────────────
    // TRACE VERSION — See backtracking in action
    // ─────────────────────────────────────────────
    public static List<List<Integer>> pathSumWithTrace(TreeNode root, int targetSum) {

        List<List<Integer>> results = new ArrayList<>();
        List<Integer> currentPath = new ArrayList<>();

        System.out.println("Target Sum: " + targetSum);
        System.out.println("────────────────────────────────────");

        dfsTrace(root, targetSum, currentPath, results, 0);

        return results;
    }

    private static void dfsTrace(TreeNode node,
                                 int remaining,
                                 List<Integer> currentPath,
                                 List<List<Integer>> results,
                                 int depth) {

        if (node == null) {
            return;
        }

        String indent = "  ".repeat(depth);

        // ADD to path
        currentPath.add(node.val);
        int newRemaining = remaining - node.val;

        System.out.println(indent + "→ ENTER node " + node.val
                + " | path=" + currentPath
                + " | remaining=" + newRemaining);

        boolean isLeaf = (node.left == null && node.right == null);

        if (isLeaf && newRemaining == 0) {
            System.out.println(indent + "  ✅ VALID PATH FOUND: " + currentPath);
            results.add(new ArrayList<>(currentPath));
        } else if (isLeaf) {
            System.out.println(indent + "  ✗ Leaf but remaining=" + newRemaining + " ≠ 0");
        }

        if (!isLeaf) {
            dfsTrace(node.left, newRemaining, currentPath, results, depth + 1);
            dfsTrace(node.right, newRemaining, currentPath, results, depth + 1);
        }

        // BACKTRACK
        currentPath.remove(currentPath.size() - 1);
        System.out.println(indent + "← EXIT node " + node.val
                + " | path after backtrack=" + currentPath);
    }

    // ─────────────────────────────────────────────
    // VARIANT: Path Sum with path count only (no backtracking needed)
    // Shows the DIFFERENCE when you only need count vs actual paths
    // ─────────────────────────────────────────────
    public static int pathSumCount(TreeNode root, int targetSum) {

        if (root == null) return 0;

        int remaining = targetSum - root.val;
        boolean isLeaf = (root.left == null && root.right == null);

        if (isLeaf && remaining == 0) return 1;
        if (isLeaf) return 0;

        // No path tracking needed → no backtracking needed
        // Just count from left + count from right
        return pathSumCount(root.left, remaining)
             + pathSumCount(root.right, remaining);
    }

    // ─────────────────────────────────────────────
    // VARIANT: Find path with MAXIMUM sum (root-to-leaf)
    // Uses same DFS pattern but different collection logic
    // ─────────────────────────────────────────────
    public static List<Integer> maxSumPath(TreeNode root) {

        List<Integer> bestPath = new ArrayList<>();
        List<Integer> currentPath = new ArrayList<>();
        int[] maxSum = {Integer.MIN_VALUE};  // array to allow mutation in recursion

        dfsMaxPath(root, 0, currentPath, bestPath, maxSum);

        return bestPath;
    }

    private static void dfsMaxPath(TreeNode node,
                                   int currentSum,
                                   List<Integer> currentPath,
                                   List<Integer> bestPath,
                                   int[] maxSum) {

        if (node == null) return;

        currentPath.add(node.val);
        currentSum += node.val;

        boolean isLeaf = (node.left == null && node.right == null);

        if (isLeaf && currentSum > maxSum[0]) {
            maxSum[0] = currentSum;
            bestPath.clear();
            bestPath.addAll(currentPath);  // deep copy best so far
        }

        if (!isLeaf) {
            dfsMaxPath(node.left, currentSum, currentPath, bestPath, maxSum);
            dfsMaxPath(node.right, currentSum, currentPath, bestPath, maxSum);
        }

        // BACKTRACK
        currentPath.remove(currentPath.size() - 1);
    }

    // ─────────────────────────────────────────────
    // VARIANT: All root-to-leaf paths (regardless of sum)
    // Pure path enumeration with backtracking
    // ─────────────────────────────────────────────
    public static List<List<Integer>> allRootToLeafPaths(TreeNode root) {

        List<List<Integer>> results = new ArrayList<>();
        List<Integer> currentPath = new ArrayList<>();

        dfsAllPaths(root, currentPath, results);

        return results;
    }

    private static void dfsAllPaths(TreeNode node,
                                    List<Integer> currentPath,
                                    List<List<Integer>> results) {

        if (node == null) return;

        currentPath.add(node.val);

        boolean isLeaf = (node.left == null && node.right == null);

        if (isLeaf) {
            results.add(new ArrayList<>(currentPath));
        } else {
            dfsAllPaths(node.left, currentPath, results);
            dfsAllPaths(node.right, currentPath, results);
        }

        // BACKTRACK — always, whether leaf or not
        currentPath.remove(currentPath.size() - 1);
    }

    // ─────────────────────────────────────────────
    // ANTI-PATTERN: What happens WITHOUT deep copy
    // ─────────────────────────────────────────────
    public static List<List<Integer>> pathSumBroken(TreeNode root, int targetSum) {

        List<List<Integer>> results = new ArrayList<>();
        List<Integer> currentPath = new ArrayList<>();

        dfsBroken(root, targetSum, currentPath, results);

        return results;
    }

    private static void dfsBroken(TreeNode node,
                                  int remaining,
                                  List<Integer> currentPath,
                                  List<List<Integer>> results) {

        if (node == null) return;

        currentPath.add(node.val);
        int newRemaining = remaining - node.val;
        boolean isLeaf = (node.left == null && node.right == null);

        if (isLeaf && newRemaining == 0) {
            // ❌ BUG: saving reference instead of copy
            results.add(currentPath);  // ALL entries share SAME list!
        }

        if (!isLeaf) {
            dfsBroken(node.left, newRemaining, currentPath, results);
            dfsBroken(node.right, newRemaining, currentPath, results);
        }

        currentPath.remove(currentPath.size() - 1);
    }

    // ─────────────────────────────────────────────
    // TREE BUILDER — Helper to construct test trees
    // ─────────────────────────────────────────────
    static TreeNode buildMainTree() {
        //          5
        //         / \
        //        4    8
        //       /    / \
        //      11   13   4
        //     / \       / \
        //    7   2     5   1

        TreeNode root = new TreeNode(5);
        root.left = new TreeNode(4);
        root.right = new TreeNode(8);
        root.left.left = new TreeNode(11);
        root.right.left = new TreeNode(13);
        root.right.right = new TreeNode(4);
        root.left.left.left = new TreeNode(7);
        root.left.left.right = new TreeNode(2);
        root.right.right.left = new TreeNode(5);
        root.right.right.right = new TreeNode(1);

        return root;
    }

    static void printTree(TreeNode root, String prefix, boolean isLeft) {
        if (root == null) return;
        System.out.println(prefix + (isLeft ? "├── " : "└── ") + root.val);
        printTree(root.left, prefix + (isLeft ? "│   " : "    "), true);
        printTree(root.right, prefix + (isLeft ? "│   " : "    "), false);
    }

    // ─────────────────────────────────────────────
    // MAIN — Run all tests
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 26: All Root-to-Leaf Paths for Target   ║");
        System.out.println("║  Pattern: Tree DFS → Path Accumulation →         ║");
        System.out.println("║           Backtracking on DFS Return             ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");
        System.out.println();

        TreeNode mainTree = buildMainTree();

        // ── Print tree structure ──
        System.out.println("═══ TREE STRUCTURE ═══");
        printTree(mainTree, "", false);
        System.out.println();

        // ── TEST 1: Core solution ──
        System.out.println("═══ TEST 1: Find all paths summing to 22 ═══");
        List<List<Integer>> paths = pathSum(mainTree, 22);
        System.out.println("Result: " + paths);
        System.out.println();

        // ── TEST 2: Traced execution ──
        System.out.println("═══ TEST 2: Traced DFS (target=22) ═══");
        List<List<Integer>> tracedPaths = pathSumWithTrace(mainTree, 22);
        System.out.println("\nFinal result: " + tracedPaths);
        System.out.println();

        // ── TEST 3: No valid paths ──
        System.out.println("═══ TEST 3: No valid paths (target=100) ═══");
        List<List<Integer>> noPaths = pathSum(mainTree, 100);
        System.out.println("Result: " + noPaths);
        System.out.println("Empty? " + noPaths.isEmpty());
        System.out.println();

        // ── TEST 4: Count only (no backtracking needed) ──
        System.out.println("═══ TEST 4: Count paths only (target=22) ═══");
        int count = pathSumCount(mainTree, 22);
        System.out.println("Number of valid paths: " + count);
        System.out.println();

        // ── TEST 5: All root-to-leaf paths ──
        System.out.println("═══ TEST 5: All root-to-leaf paths (any sum) ═══");
        List<List<Integer>> allPaths = allRootToLeafPaths(mainTree);
        for (List<Integer> p : allPaths) {
            int sum = p.stream().mapToInt(Integer::intValue).sum();
            System.out.println("  Path: " + p + " → sum=" + sum);
        }
        System.out.println();

        // ── TEST 6: Max sum path ──
        System.out.println("═══ TEST 6: Maximum sum root-to-leaf path ═══");
        List<Integer> maxPath = maxSumPath(mainTree);
        int maxPathSum = maxPath.stream().mapToInt(Integer::intValue).sum();
        System.out.println("Max path: " + maxPath + " → sum=" + maxPathSum);
        System.out.println();

        // ── TEST 7: Demonstrate the reference bug ──
        System.out.println("═══ TEST 7: Reference Bug Demonstration ═══");
        List<List<Integer>> brokenResult = pathSumBroken(mainTree, 22);
        System.out.println("Broken result (should be [[5,4,11,2],[5,8,4,5]]):");
        System.out.println("  Actual: " + brokenResult);
        System.out.println("  All entries are same empty list due to reference sharing!");
        System.out.println();

        // ── TEST 8: Single node tree ──
        System.out.println("═══ TEST 8: Single node tree ═══");
        TreeNode single = new TreeNode(5);
        System.out.println("Target=5: " + pathSum(single, 5));
        System.out.println("Target=3: " + pathSum(single, 3));
        System.out.println();

        // ── TEST 9: Tree with negative values ──
        System.out.println("═══ TEST 9: Tree with negative values ═══");
        //       10
        //      /  \
        //    -5    15
        //    / \
        //   3   7
        TreeNode negTree = new TreeNode(10);
        negTree.left = new TreeNode(-5);
        negTree.right = new TreeNode(15);
        negTree.left.left = new TreeNode(3);
        negTree.left.right = new TreeNode(7);

        System.out.println("Tree:");
        printTree(negTree, "", false);
        System.out.println("Target=8:  " + pathSum(negTree, 8));
        System.out.println("Target=12: " + pathSum(negTree, 12));
        System.out.println("Target=25: " + pathSum(negTree, 25));
        System.out.println();

        // ── TEST 10: Skewed tree (linked list shape) ──
        System.out.println("═══ TEST 10: Skewed tree (all left children) ═══");
        //  1
        //   \
        //    2
        //     \
        //      3
        //       \
        //        4
        TreeNode skewed = new TreeNode(1);
        skewed.right = new TreeNode(2);
        skewed.right.right = new TreeNode(3);
        skewed.right.right.right = new TreeNode(4);

        System.out.println("Target=10: " + pathSum(skewed, 10));
        System.out.println("Target=6:  " + pathSum(skewed, 6));
        System.out.println("All paths: " + allRootToLeafPaths(skewed));
    }
}