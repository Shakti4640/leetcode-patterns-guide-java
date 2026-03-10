import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ZigzagLevelOrder {

    // ─────────────────────────────────────────────
    // TREE NODE DEFINITION
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

    // ═══════════════════════════════════════════════
    // SOLUTION 1: BFS + addFirst/addLast (OPTIMAL)
    // O(n) time, O(n) space
    // ═══════════════════════════════════════════════
    public static List<List<Integer>> zigzagLevelOrder(TreeNode root) {

        List<List<Integer>> result = new ArrayList<>();

        if (root == null) {
            return result;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        boolean leftToRight = true;

        while (!queue.isEmpty()) {

            int levelSize = queue.size();
            LinkedList<Integer> levelList = new LinkedList<>();

            for (int i = 0; i < levelSize; i++) {

                TreeNode node = queue.poll();

                // Direction-aware insertion
                if (leftToRight) {
                    levelList.addLast(node.val);
                } else {
                    levelList.addFirst(node.val);
                }

                // Enqueue children — ALWAYS left then right
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }

            result.add(levelList);
            leftToRight = !leftToRight;   // Toggle direction
        }

        return result;
    }

    // ═══════════════════════════════════════════════
    // SOLUTION 2: BFS + Reverse on Odd Levels
    // (Strategy A — simpler to understand)
    // ═══════════════════════════════════════════════
    public static List<List<Integer>> zigzagLevelOrderReverse(TreeNode root) {

        List<List<Integer>> result = new ArrayList<>();

        if (root == null) {
            return result;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int level = 0;

        while (!queue.isEmpty()) {

            int levelSize = queue.size();
            List<Integer> levelList = new ArrayList<>();

            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                levelList.add(node.val);    // Always addLast

                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }

            // Reverse on odd levels
            if (level % 2 == 1) {
                Collections.reverse(levelList);
            }

            result.add(levelList);
            level++;
        }

        return result;
    }

    // ═══════════════════════════════════════════════
    // SOLUTION 3: DFS APPROACH (for comparison)
    // Uses recursion + level number
    // ═══════════════════════════════════════════════
    public static List<List<Integer>> zigzagLevelOrderDFS(TreeNode root) {

        List<List<Integer>> result = new ArrayList<>();

        if (root == null) {
            return result;
        }

        dfsHelper(root, 0, result);
        return result;
    }

    private static void dfsHelper(TreeNode node, int level,
                                   List<List<Integer>> result) {
        if (node == null) {
            return;
        }

        // Ensure list exists for this level
        if (level >= result.size()) {
            result.add(new LinkedList<>());
        }

        // Direction-aware insertion
        LinkedList<Integer> levelList = (LinkedList<Integer>) result.get(level);
        if (level % 2 == 0) {
            levelList.addLast(node.val);
        } else {
            levelList.addFirst(node.val);
        }

        // Always recurse left then right
        dfsHelper(node.left, level + 1, result);
        dfsHelper(node.right, level + 1, result);
    }

    // ═══════════════════════════════════════════════
    // STANDARD BFS (Project 17 — for comparison)
    // ═══════════════════════════════════════════════
    public static List<List<Integer>> standardLevelOrder(TreeNode root) {

        List<List<Integer>> result = new ArrayList<>();

        if (root == null) {
            return result;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<Integer> levelList = new ArrayList<>();

            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                levelList.add(node.val);

                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }

            result.add(levelList);
        }

        return result;
    }

    // ═══════════════════════════════════════════════
    // TRACE VERSION — Full visualization
    // ═══════════════════════════════════════════════
    public static List<List<Integer>> zigzagWithTrace(TreeNode root) {

        List<List<Integer>> result = new ArrayList<>();

        if (root == null) {
            System.out.println("  Empty tree → return []");
            return result;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        boolean leftToRight = true;

        while (!queue.isEmpty()) {

            int levelSize = queue.size();
            int level = result.size();
            LinkedList<Integer> levelList = new LinkedList<>();

            String direction = leftToRight ? "L→R (addLast)" : "R→L (addFirst)";
            System.out.println("  ═══ LEVEL " + level + " (" + direction
                    + ") — " + levelSize + " nodes ═══");

            // Show queue contents
            System.out.print("  Queue: [");
            int idx = 0;
            for (TreeNode n : queue) {
                System.out.print(n.val);
                if (idx < queue.size() - 1) System.out.print(", ");
                idx++;
            }
            System.out.println("]");

            for (int i = 0; i < levelSize; i++) {

                TreeNode node = queue.poll();

                if (leftToRight) {
                    levelList.addLast(node.val);
                    System.out.println("    Process node(" + node.val
                            + ") → addLast → levelList = " + levelList);
                } else {
                    levelList.addFirst(node.val);
                    System.out.println("    Process node(" + node.val
                            + ") → addFirst → levelList = " + levelList);
                }

                if (node.left != null) {
                    queue.offer(node.left);
                    System.out.println("      Enqueue left child: " + node.left.val);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                    System.out.println("      Enqueue right child: " + node.right.val);
                }
            }

            result.add(levelList);
            System.out.println("  Level " + level + " result: " + levelList);
            System.out.println("  Direction toggle: " + leftToRight
                    + " → " + !leftToRight);
            leftToRight = !leftToRight;
            System.out.println();
        }

        return result;
    }

    // ─────────────────────────────────────────────
    // TREE BUILDERS
    // ─────────────────────────────────────────────
    public static TreeNode buildTree1() {
        //          3
        //         / \
        //        9   20
        //           / \
        //          15   7
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);
        return root;
    }

    public static TreeNode buildTree2() {
        //          1
        //         / \
        //        2   3
        //       / \   \
        //      4   5   6
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        root.right.right = new TreeNode(6);
        return root;
    }

    public static TreeNode buildTree3() {
        //              1
        //             / \
        //            2   3
        //           / \ / \
        //          4  5 6  7
        //         /        \
        //        8          9
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        root.right.left = new TreeNode(6);
        root.right.right = new TreeNode(7);
        root.left.left.left = new TreeNode(8);
        root.right.right.right = new TreeNode(9);
        return root;
    }

    public static TreeNode buildPerfectTree(int levels) {
        // Build a perfect binary tree with given number of levels
        // Values: 1, 2, 3, ... (level order)
        if (levels <= 0) return null;

        TreeNode root = new TreeNode(1);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int val = 2;

        for (int l = 1; l < levels; l++) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                node.left = new TreeNode(val++);
                node.right = new TreeNode(val++);
                queue.offer(node.left);
                queue.offer(node.right);
            }
        }

        return root;
    }

    // ─────────────────────────────────────────────
    // VISUAL TREE PRINTER
    // ─────────────────────────────────────────────
    public static void printTree(TreeNode root) {
        if (root == null) {
            System.out.println("  (empty tree)");
            return;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int level = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();
            boolean allNull = true;
            StringBuilder sb = new StringBuilder();
            sb.append("  Level ").append(level).append(": ");

            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                if (node != null) {
                    sb.append(node.val).append("  ");
                    allNull = false;
                    queue.offer(node.left);
                    queue.offer(node.right);
                } else {
                    sb.append("·  ");
                    queue.offer(null);
                    queue.offer(null);
                }
            }

            if (allNull) break;
            System.out.println(sb.toString().trim());
            level++;
        }
    }

    // ─────────────────────────────────────────────
    // ZIGZAG VISUALIZER
    // ─────────────────────────────────────────────
    public static void printZigzagVisualization(List<List<Integer>> zigzag) {

        System.out.println("  Zigzag visualization:");

        for (int level = 0; level < zigzag.size(); level++) {
            List<Integer> row = zigzag.get(level);
            String dir = (level % 2 == 0) ? "→→→" : "←←←";
            StringBuilder sb = new StringBuilder();

            sb.append("    Level ").append(level).append(" ").append(dir).append(" : [");
            for (int i = 0; i < row.size(); i++) {
                sb.append(row.get(i));
                if (i < row.size() - 1) sb.append(", ");
            }
            sb.append("]");

            System.out.println(sb.toString());
        }
    }

    // ─────────────────────────────────────────────
    // SIDE-BY-SIDE COMPARISON
    // ─────────────────────────────────────────────
    public static void printSideBySide(List<List<Integer>> standard,
                                        List<List<Integer>> zigzag) {

        System.out.println("  Standard BFS vs Zigzag BFS:");
        System.out.println("  ┌───────────────────────┬───────────────────────┐");
        System.out.println("  │ Standard (L→R always) │ Zigzag (alternating)  │");
        System.out.println("  ├───────────────────────┼───────────────────────┤");

        int maxLevels = Math.max(standard.size(), zigzag.size());
        for (int i = 0; i < maxLevels; i++) {
            String stdStr = i < standard.size() ? standard.get(i).toString() : "—";
            String zigStr = i < zigzag.size() ? zigzag.get(i).toString() : "—";
            String dir = (i % 2 == 0) ? "→" : "←";

            System.out.printf("  │ L%d: %-17s│ L%d%s: %-16s│%n",
                    i, stdStr, i, dir, zigStr);
        }

        System.out.println("  └───────────────────────┴───────────────────────┘");
    }

    // ─────────────────────────────────────────────
    // MAIN — Run all tests
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 24: Binary Tree Zigzag Level Order Traversal     ║");
        System.out.println("║  Pattern: Tree BFS → Alternating Direction Flag           ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Classic example with trace ──
        System.out.println("═══ TEST 1: Classic Tree — Full Trace ═══");
        TreeNode tree1 = buildTree1();
        printTree(tree1);
        System.out.println();
        List<List<Integer>> result1 = zigzagWithTrace(tree1);
        System.out.println("  ★ FINAL: " + result1);
        System.out.println();
        printZigzagVisualization(result1);
        System.out.println();

        // ── TEST 2: Asymmetric tree with trace ──
        System.out.println("═══ TEST 2: Asymmetric Tree — Full Trace ═══");
        TreeNode tree2 = buildTree2();
        printTree(tree2);
        System.out.println();
        List<List<Integer>> result2 = zigzagWithTrace(tree2);
        System.out.println("  ★ FINAL: " + result2);
        System.out.println();

        // ── TEST 3: 4-level tree ──
        System.out.println("═══ TEST 3: Four-Level Tree ═══");
        TreeNode tree3 = buildTree3();
        printTree(tree3);
        System.out.println();
        List<List<Integer>> result3 = zigzagWithTrace(tree3);
        System.out.println("  ★ FINAL: " + result3);
        System.out.println();

        // ── TEST 4: Side-by-side comparison ──
        System.out.println("═══ TEST 4: Standard vs Zigzag — Side by Side ═══");
        TreeNode tree4 = buildTree3();
        List<List<Integer>> standard4 = standardLevelOrder(tree4);
        TreeNode tree4b = buildTree3();
        List<List<Integer>> zigzag4 = zigzagLevelOrder(tree4b);
        printSideBySide(standard4, zigzag4);
        System.out.println();

        // ── TEST 5: Empty tree ──
        System.out.println("═══ TEST 5: Empty Tree ═══");
        List<List<Integer>> result5 = zigzagLevelOrder(null);
        System.out.println("  Input: null");
        System.out.println("  Output: " + result5);
        System.out.println();

        // ── TEST 6: Single node ──
        System.out.println("═══ TEST 6: Single Node ═══");
        TreeNode single = new TreeNode(42);
        List<List<Integer>> result6 = zigzagLevelOrder(single);
        System.out.println("  Input: [42]");
        System.out.println("  Output: " + result6);
        System.out.println();

        // ── TEST 7: Left-skewed tree ──
        System.out.println("═══ TEST 7: Left-Skewed Tree ═══");
        TreeNode skewed = new TreeNode(1);
        skewed.left = new TreeNode(2);
        skewed.left.left = new TreeNode(3);
        skewed.left.left.left = new TreeNode(4);
        printTree(skewed);
        List<List<Integer>> result7 = zigzagLevelOrder(skewed);
        System.out.println("  Zigzag: " + result7);
        System.out.println("  (Each level has 1 node → direction doesn't matter)");
        System.out.println();

        // ── TEST 8: Perfect binary tree ──
        System.out.println("═══ TEST 8: Perfect Binary Tree (4 levels) ═══");
        TreeNode perfect = buildPerfectTree(4);
        printTree(perfect);
        System.out.println();

        List<List<Integer>> standardPerfect = standardLevelOrder(buildPerfectTree(4));
        List<List<Integer>> zigzagPerfect = zigzagLevelOrder(perfect);
        printSideBySide(standardPerfect, zigzagPerfect);
        System.out.println();
        printZigzagVisualization(zigzagPerfect);
        System.out.println();

        // ── TEST 9: All three approaches agree ──
        System.out.println("═══ TEST 9: Verify All Approaches Agree ═══");
        TreeNode[][] trees = {
            { buildTree1() }, { buildTree1() }, { buildTree1() },
            { buildTree2() }, { buildTree2() }, { buildTree2() },
            { buildTree3() }, { buildTree3() }, { buildTree3() }
        };

        boolean allMatch = true;
        for (int t = 0; t < 3; t++) {
            TreeNode treeA, treeB, treeC;
            switch (t) {
                case 0:
                    treeA = buildTree1(); treeB = buildTree1(); treeC = buildTree1();
                    break;
                case 1:
                    treeA = buildTree2(); treeB = buildTree2(); treeC = buildTree2();
                    break;
                default:
                    treeA = buildTree3(); treeB = buildTree3(); treeC = buildTree3();
            }

            List<List<Integer>> r1 = zigzagLevelOrder(treeA);
            List<List<Integer>> r2 = zigzagLevelOrderReverse(treeB);
            List<List<Integer>> r3 = zigzagLevelOrderDFS(treeC);

            boolean match = r1.equals(r2) && r2.equals(r3);
            System.out.println("  Tree " + (t + 1)
                    + ": addFirst=" + r1
                    + (match ? " ✓ (all 3 agree)" : " ✗ MISMATCH"));
            if (!match) {
                System.out.println("    Reverse:  " + r2);
                System.out.println("    DFS:      " + r3);
                allMatch = false;
            }
        }
        System.out.println("  All match: " + allMatch);
        System.out.println();

        // ── TEST 10: Performance comparison ──
        System.out.println("═══ TEST 10: Performance — Perfect Tree (17 levels) ═══");
        int levels = 17;
        int nodeCount = (1 << levels) - 1;
        System.out.println("  Levels: " + levels + " | Nodes: " + nodeCount);

        // Strategy B: addFirst/addLast
        TreeNode perfTree1 = buildPerfectTree(levels);
        long start = System.nanoTime();
        List<List<Integer>> perf1 = zigzagLevelOrder(perfTree1);
        long time1 = System.nanoTime() - start;

        // Strategy A: Reverse on odd levels
        TreeNode perfTree2 = buildPerfectTree(levels);
        start = System.nanoTime();
        List<List<Integer>> perf2 = zigzagLevelOrderReverse(perfTree2);
        long time2 = System.nanoTime() - start;

        // Strategy C: DFS
        TreeNode perfTree3 = buildPerfectTree(levels);
        start = System.nanoTime();
        List<List<Integer>> perf3 = zigzagLevelOrderDFS(perfTree3);
        long time3 = System.nanoTime() - start;

        System.out.println("  addFirst/addLast: " + (time1 / 1_000_000) + " ms");
        System.out.println("  Reverse on odd:   " + (time2 / 1_000_000) + " ms");
        System.out.println("  DFS approach:     " + (time3 / 1_000_000) + " ms");
        System.out.println("  Results match: " + (perf1.equals(perf2) && perf2.equals(perf3)));
        System.out.println("  Levels in output: " + perf1.size());
        System.out.println("  Last level size: " + perf1.get(perf1.size() - 1).size());
    }
}