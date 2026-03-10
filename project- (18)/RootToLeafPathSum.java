import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class RootToLeafPathSum {

    // ─────────────────────────────────────────────
    // TREE NODE DEFINITION (same structure from Project 17)
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
    // SOLUTION 1: RECURSIVE DFS — Subtraction Method
    // ═══════════════════════════════════════════════
    public static boolean hasPathSum(TreeNode node, int targetSum) {

        // BASE CASE 1: Null node → no path here
        if (node == null) {
            return false;
        }

        // BASE CASE 2: Leaf node → check if remaining equals node value
        if (node.left == null && node.right == null) {
            return node.val == targetSum;
        }

        // RECURSIVE CASE: Try left subtree OR right subtree
        // Subtract current node's value → pass reduced target downward
        int remaining = targetSum - node.val;
        return hasPathSum(node.left, remaining) || hasPathSum(node.right, remaining);
    }

    // ═══════════════════════════════════════════════
    // SOLUTION 2: ITERATIVE DFS — Explicit Stack
    // (For when recursion depth is too risky)
    // ═══════════════════════════════════════════════
    public static boolean hasPathSumIterative(TreeNode root, int targetSum) {

        if (root == null) {
            return false;
        }

        // Stack stores PAIRS: (node, remaining sum at that node)
        Stack<Object[]> stack = new Stack<>();
        stack.push(new Object[] { root, targetSum });

        while (!stack.isEmpty()) {
            Object[] current = stack.pop();
            TreeNode node = (TreeNode) current[0];
            int remaining = (int) current[1];

            // Leaf check
            if (node.left == null && node.right == null) {
                if (node.val == remaining) {
                    return true;
                }
                continue;  // This leaf didn't match → try next path
            }

            // Push children with updated remaining
            int newRemaining = remaining - node.val;

            if (node.right != null) {
                stack.push(new Object[] { node.right, newRemaining });
            }
            if (node.left != null) {
                stack.push(new Object[] { node.left, newRemaining });
            }
        }

        return false;
    }

    // ═══════════════════════════════════════════════
    // SOLUTION 3: BFS APPROACH — For Comparison
    // (Uses queue like Project 17, but carries sum state)
    // ═══════════════════════════════════════════════
    public static boolean hasPathSumBFS(TreeNode root, int targetSum) {

        if (root == null) {
            return false;
        }

        Queue<Object[]> queue = new LinkedList<>();
        queue.offer(new Object[] { root, targetSum });

        while (!queue.isEmpty()) {
            Object[] current = queue.poll();
            TreeNode node = (TreeNode) current[0];
            int remaining = (int) current[1];

            // Leaf check
            if (node.left == null && node.right == null) {
                if (node.val == remaining) {
                    return true;
                }
                continue;
            }

            int newRemaining = remaining - node.val;

            if (node.left != null) {
                queue.offer(new Object[] { node.left, newRemaining });
            }
            if (node.right != null) {
                queue.offer(new Object[] { node.right, newRemaining });
            }
        }

        return false;
    }

    // ═══════════════════════════════════════════════
    // TRACE VERSION — See the DFS in action
    // ═══════════════════════════════════════════════
    public static boolean hasPathSumWithTrace(TreeNode node, int targetSum,
                                               String path, int depth) {
        String indent = "  ".repeat(depth);

        if (node == null) {
            System.out.println(indent + "→ null → return false");
            return false;
        }

        String currentPath = path.isEmpty()
            ? String.valueOf(node.val)
            : path + " → " + node.val;

        System.out.println(indent + "Visit node " + node.val
            + " | remaining = " + targetSum
            + " | path so far: [" + currentPath + "]");

        // Leaf check
        if (node.left == null && node.right == null) {
            boolean match = (node.val == targetSum);
            if (match) {
                System.out.println(indent + "  ★ LEAF: " + node.val
                    + " == " + targetSum + " → TRUE → PATH FOUND: ["
                    + currentPath + "]");
            } else {
                System.out.println(indent + "  ✗ LEAF: " + node.val
                    + " ≠ " + targetSum + " → false");
            }
            return match;
        }

        int newRemaining = targetSum - node.val;
        System.out.println(indent + "  Subtract " + node.val
            + " → new remaining = " + newRemaining);

        // Try left
        System.out.println(indent + "  ┌─ Go LEFT:");
        boolean leftResult = hasPathSumWithTrace(
            node.left, newRemaining, currentPath, depth + 1
        );

        if (leftResult) {
            System.out.println(indent + "  Left returned TRUE → propagate up");
            return true;
        }

        // Try right
        System.out.println(indent + "  └─ Go RIGHT:");
        boolean rightResult = hasPathSumWithTrace(
            node.right, newRemaining, currentPath, depth + 1
        );

        if (rightResult) {
            System.out.println(indent + "  Right returned TRUE → propagate up");
        } else {
            System.out.println(indent + "  Both children false → return false");
        }

        return rightResult;
    }

    // ─────────────────────────────────────────────
    // TREE BUILDER HELPER — Build from description
    // ─────────────────────────────────────────────
    public static TreeNode buildTree1() {
        //          5
        //         / \
        //        4   8
        //       /   / \
        //      11  13   4
        //     / \        \
        //    7   2        1

        TreeNode root = new TreeNode(5);
        root.left = new TreeNode(4);
        root.right = new TreeNode(8);
        root.left.left = new TreeNode(11);
        root.right.left = new TreeNode(13);
        root.right.right = new TreeNode(4);
        root.left.left.left = new TreeNode(7);
        root.left.left.right = new TreeNode(2);
        root.right.right.right = new TreeNode(1);
        return root;
    }

    public static TreeNode buildTree2() {
        //        1
        //       / \
        //      2   3
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        return root;
    }

    public static TreeNode buildTree3() {
        //        1
        //         \
        //          2
        TreeNode root = new TreeNode(1);
        root.right = new TreeNode(2);
        return root;
    }

    public static TreeNode buildTreeNegative() {
        //        10
        //       / \
        //     -5   3
        //     /
        //    8
        TreeNode root = new TreeNode(10);
        root.left = new TreeNode(-5);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(8);
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
    // MAIN — Run all tests
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 18: Root-to-Leaf Path Sum            ║");
        System.out.println("║  Pattern: Tree DFS → Recursive Subtraction    ║");
        System.out.println("╚════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Classic example — path exists ──
        System.out.println("═══ TEST 1: Classic Tree — Target 22 ═══");
        TreeNode tree1 = buildTree1();
        printTree(tree1);
        System.out.println();
        System.out.println("Target = 22");
        System.out.println("────────────────────────────────────");
        boolean result1 = hasPathSumWithTrace(tree1, 22, "", 0);
        System.out.println("\nFinal answer: " + result1);
        System.out.println();

        // ── TEST 2: No valid path ──
        System.out.println("═══ TEST 2: Simple Tree — Target 5 (no path) ═══");
        TreeNode tree2 = buildTree2();
        printTree(tree2);
        System.out.println();
        System.out.println("Target = 5");
        System.out.println("────────────────────────────────────");
        boolean result2 = hasPathSumWithTrace(tree2, 5, "", 0);
        System.out.println("\nFinal answer: " + result2);
        System.out.println();

        // ── TEST 3: Valid path exists ──
        System.out.println("═══ TEST 3: Simple Tree — Target 4 (path exists) ═══");
        TreeNode tree2b = buildTree2();
        printTree(tree2b);
        System.out.println();
        System.out.println("Target = 4");
        System.out.println("────────────────────────────────────");
        boolean result3 = hasPathSumWithTrace(tree2b, 4, "", 0);
        System.out.println("\nFinal answer: " + result3);
        System.out.println();

        // ── TEST 4: One-child trap ──
        System.out.println("═══ TEST 4: One-Child Trap — Target 1 ═══");
        TreeNode tree3 = buildTree3();
        printTree(tree3);
        System.out.println();
        System.out.println("Target = 1 (root is NOT a leaf — has right child)");
        System.out.println("────────────────────────────────────");
        boolean result4 = hasPathSumWithTrace(tree3, 1, "", 0);
        System.out.println("\nFinal answer: " + result4);
        System.out.println();

        // ── TEST 5: Negative values ──
        System.out.println("═══ TEST 5: Negative Node Values — Target 13 ═══");
        TreeNode tree4 = buildTreeNegative();
        printTree(tree4);
        System.out.println();
        System.out.println("Target = 13 (path: 10 → -5 → 8 = 13)");
        System.out.println("────────────────────────────────────");
        boolean result5 = hasPathSumWithTrace(tree4, 13, "", 0);
        System.out.println("\nFinal answer: " + result5);
        System.out.println();

        // ── TEST 6: Empty tree ──
        System.out.println("═══ TEST 6: Empty Tree — Target 0 ═══");
        boolean result6 = hasPathSum(null, 0);
        System.out.println("Empty tree, target = 0 → " + result6);
        System.out.println();

        // ── TEST 7: Single node tree ──
        System.out.println("═══ TEST 7: Single Node — Target matches ═══");
        TreeNode single = new TreeNode(7);
        boolean result7a = hasPathSum(single, 7);
        boolean result7b = hasPathSum(single, 5);
        System.out.println("Single node [7], target = 7 → " + result7a);
        System.out.println("Single node [7], target = 5 → " + result7b);
        System.out.println();

        // ── TEST 8: All three solutions agree ──
        System.out.println("═══ TEST 8: Verify All Three Approaches ═══");
        TreeNode tree8 = buildTree1();
        int target8 = 22;
        boolean r1 = hasPathSum(tree8, target8);
        boolean r2 = hasPathSumIterative(tree8, target8);
        boolean r3 = hasPathSumBFS(tree8, target8);
        System.out.println("Recursive DFS:  " + r1);
        System.out.println("Iterative DFS:  " + r2);
        System.out.println("BFS approach:   " + r3);
        System.out.println("All agree: " + (r1 == r2 && r2 == r3));
        System.out.println();

        // ── TEST 9: Another target on same tree ──
        System.out.println("═══ TEST 9: Same Tree — Target 26 ═══");
        System.out.println("Path: 5 → 8 → 13 = 26");
        System.out.println("────────────────────────────────────");
        boolean result9 = hasPathSumWithTrace(tree8, 26, "", 0);
        System.out.println("\nFinal answer: " + result9);
        System.out.println();

        // ── TEST 10: Target 18 → Path 5 → 8 → 4 → 1 = 18 ──
        System.out.println("═══ TEST 10: Same Tree — Target 18 ═══");
        TreeNode tree10 = buildTree1();
        System.out.println("Expected path: 5 → 8 → 4 → 1 = 18");
        System.out.println("────────────────────────────────────");
        boolean result10 = hasPathSumWithTrace(tree10, 18, "", 0);
        System.out.println("\nFinal answer: " + result10);
    }
}