import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BinaryTreeLevelOrder {

    // ─────────────────────────────────────────────
    // TreeNode Definition
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
    // CORE SOLUTION: BFS with Queue + Level Size Snapshot
    // ─────────────────────────────────────────────
    public static List<List<Integer>> levelOrder(TreeNode root) {
        
        List<List<Integer>> result = new ArrayList<>();
        
        // Edge case: empty tree
        if (root == null) return result;
        
        // Initialize queue with root
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        // Process level by level
        while (!queue.isEmpty()) {
            
            // Snapshot: how many nodes in THIS level
            int levelSize = queue.size();
            
            // Collect this level's values
            List<Integer> currentLevel = new ArrayList<>();
            
            for (int i = 0; i < levelSize; i++) {
                
                // Dequeue front node
                TreeNode node = queue.poll();
                
                // Record its value
                currentLevel.add(node.val);
                
                // Enqueue children (for NEXT level)
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            
            // Save this level
            result.add(currentLevel);
        }
        
        return result;
    }
    
    // ─────────────────────────────────────────────
    // VARIANT: Bottom-up level order (reverse levels)
    // ─────────────────────────────────────────────
    public static List<List<Integer>> levelOrderBottom(TreeNode root) {
        
        // Get normal level order
        List<List<Integer>> result = levelOrder(root);
        
        // Reverse the list of levels
        List<List<Integer>> reversed = new ArrayList<>();
        for (int i = result.size() - 1; i >= 0; i--) {
            reversed.add(result.get(i));
        }
        
        return reversed;
    }
    
    // ─────────────────────────────────────────────
    // VARIANT: Right side view (last node of each level)
    // ─────────────────────────────────────────────
    public static List<Integer> rightSideView(TreeNode root) {
        
        List<Integer> result = new ArrayList<>();
        
        if (root == null) return result;
        
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                
                // Only add the LAST node of each level
                if (i == levelSize - 1) {
                    result.add(node.val);
                }
                
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
        }
        
        return result;
    }
    
    // ─────────────────────────────────────────────
    // VARIANT: Left side view (first node of each level)
    // ─────────────────────────────────────────────
    public static List<Integer> leftSideView(TreeNode root) {
        
        List<Integer> result = new ArrayList<>();
        
        if (root == null) return result;
        
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                
                // Only add the FIRST node of each level
                if (i == 0) {
                    result.add(node.val);
                }
                
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
        }
        
        return result;
    }
    
    // ─────────────────────────────────────────────
    // VARIANT: Average of each level
    // ─────────────────────────────────────────────
    public static List<Double> averageOfLevels(TreeNode root) {
        
        List<Double> result = new ArrayList<>();
        
        if (root == null) return result;
        
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            double levelSum = 0;
            
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                levelSum += node.val;
                
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
            
            result.add(levelSum / levelSize);
        }
        
        return result;
    }
    
    // ─────────────────────────────────────────────
    // VARIANT: Largest value in each level
    // ─────────────────────────────────────────────
    public static List<Integer> largestValues(TreeNode root) {
        
        List<Integer> result = new ArrayList<>();
        
        if (root == null) return result;
        
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            int levelMax = Integer.MIN_VALUE;
            
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                levelMax = Math.max(levelMax, node.val);
                
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
            
            result.add(levelMax);
        }
        
        return result;
    }
    
    // ─────────────────────────────────────────────
    // UTILITY: Tree depth via BFS
    // ─────────────────────────────────────────────
    public static int maxDepth(TreeNode root) {
        
        if (root == null) return 0;
        
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int depth = 0;
        
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            depth++;
            
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
        }
        
        return depth;
    }
    
    // ─────────────────────────────────────────────
    // UTILITY: Maximum width of tree
    // ─────────────────────────────────────────────
    public static int maxWidth(TreeNode root) {
        
        if (root == null) return 0;
        
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int maxW = 0;
        
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            maxW = Math.max(maxW, levelSize);
            
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
        }
        
        return maxW;
    }
    
    // ─────────────────────────────────────────────
    // TRACE: Step-by-step visualization
    // ─────────────────────────────────────────────
    public static void traceExecution(TreeNode root, String treeName) {
        
        System.out.println("  Tree: " + treeName);
        printTree(root, "  ");
        System.out.println("  ─────────────────────────────────────────");
        
        if (root == null) {
            System.out.println("  Empty tree → return []");
            System.out.println();
            return;
        }
        
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        List<List<Integer>> result = new ArrayList<>();
        int levelNum = 0;
        
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<Integer> currentLevel = new ArrayList<>();
            
            // Show queue state
            System.out.print("  Level " + levelNum + " (size=" + levelSize + "): queue=[");
            Queue<TreeNode> tempQueue = new LinkedList<>(queue);
            boolean first = true;
            while (!tempQueue.isEmpty()) {
                if (!first) System.out.print(", ");
                System.out.print(tempQueue.poll().val);
                first = false;
            }
            System.out.println("]");
            
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                currentLevel.add(node.val);
                
                String children = "";
                if (node.left != null) {
                    queue.offer(node.left);
                    children += "L=" + node.left.val;
                }
                if (node.right != null) {
                    queue.offer(node.right);
                    if (!children.isEmpty()) children += ", ";
                    children += "R=" + node.right.val;
                }
                if (children.isEmpty()) children = "no children";
                
                System.out.println("    Process " + node.val 
                    + " → enqueue: " + children);
            }
            
            result.add(currentLevel);
            System.out.println("    Level " + levelNum + " result: " + currentLevel);
            System.out.println();
            levelNum++;
        }
        
        System.out.println("  ★ FINAL RESULT: " + result);
        System.out.println();
    }
    
    // ─────────────────────────────────────────────
    // HELPER: Print tree visually
    // ─────────────────────────────────────────────
    public static void printTree(TreeNode root, String prefix) {
        if (root == null) {
            System.out.println(prefix + "(empty)");
            return;
        }
        printTreeHelper(root, prefix, "", "");
    }
    
    private static void printTreeHelper(TreeNode node, String prefix, 
            String childPrefix, String connector) {
        if (node == null) return;
        
        System.out.println(prefix + connector + node.val);
        
        String newPrefix = prefix + childPrefix;
        
        if (node.left != null || node.right != null) {
            if (node.right != null) {
                printTreeHelper(node.right, newPrefix, 
                    node.left != null ? "│   " : "    ", "├── R:");
            }
            if (node.left != null) {
                printTreeHelper(node.left, newPrefix, "    ", "└── L:");
            }
        }
    }
    
    // ─────────────────────────────────────────────
    // HELPER: Build tree from level-order array
    // null values represent missing nodes
    // ─────────────────────────────────────────────
    public static TreeNode buildTree(Integer[] values) {
        if (values == null || values.length == 0 || values[0] == null) {
            return null;
        }
        
        TreeNode root = new TreeNode(values[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        int i = 1;
        while (!queue.isEmpty() && i < values.length) {
            TreeNode current = queue.poll();
            
            // Left child
            if (i < values.length && values[i] != null) {
                current.left = new TreeNode(values[i]);
                queue.offer(current.left);
            }
            i++;
            
            // Right child
            if (i < values.length && values[i] != null) {
                current.right = new TreeNode(values[i]);
                queue.offer(current.right);
            }
            i++;
        }
        
        return root;
    }
    
    // ─────────────────────────────────────────────
    // MAIN
    // ─────────────────────────────────────────────
    public static void main(String[] args) {
        
        System.out.println("╔═══════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 17: Binary Tree Level Order Traversal        ║");
        System.out.println("║  Pattern: Tree BFS → Queue + Level Size Snapshot      ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        System.out.println();
        
        // ── Build test trees ──
        //        3
        //       / \
        //      9   20
        //         /  \
        //        15    7
        TreeNode tree1 = buildTree(new Integer[] {3, 9, 20, null, null, 15, 7});
        
        //        1
        //       / \
        //      2    3
        //     / \    \
        //    4   5    6
        TreeNode tree2 = buildTree(new Integer[] {1, 2, 3, 4, 5, null, 6});
        
        //        1
        //       /
        //      2
        //     /
        //    3
        //   /
        //  4
        TreeNode tree3 = new TreeNode(1);
        tree3.left = new TreeNode(2);
        tree3.left.left = new TreeNode(3);
        tree3.left.left.left = new TreeNode(4);
        
        //    1
        TreeNode tree4 = new TreeNode(1);
        
        // ── TEST 1: Classic example with trace ──
        System.out.println("═══ TEST 1: Classic Example (Traced) ═══");
        traceExecution(tree1, "Classic");
        
        // ── TEST 2: Full binary tree with trace ──
        System.out.println("═══ TEST 2: Fuller Tree (Traced) ═══");
        traceExecution(tree2, "Fuller");
        
        // ── TEST 3: Skewed tree ──
        System.out.println("═══ TEST 3: Skewed Tree (Traced) ═══");
        traceExecution(tree3, "Skewed Left");
        
        // ── TEST 4: Edge cases ──
        System.out.println("═══ TEST 4: Edge Cases ═══");
        System.out.println("  Single node: " + levelOrder(tree4));
        System.out.println("  Null tree:   " + levelOrder(null));
        
        //    1
        //   / \
        //  2   3
        TreeNode tree5 = buildTree(new Integer[] {1, 2, 3});
        System.out.println("  Three nodes: " + levelOrder(tree5));
        System.out.println();
        
        // ── TEST 5: Bottom-up level order ──
        System.out.println("═══ TEST 5: Bottom-Up Level Order ═══");
        System.out.println("  Tree 1 normal:    " + levelOrder(tree1));
        System.out.println("  Tree 1 bottom-up: " + levelOrderBottom(tree1));
        System.out.println("  Tree 2 normal:    " + levelOrder(tree2));
        System.out.println("  Tree 2 bottom-up: " + levelOrderBottom(tree2));
        System.out.println();
        
        // ── TEST 6: Right side view ──
        System.out.println("═══ TEST 6: Right Side View ═══");
        printTree(tree1, "  ");
        System.out.println("  Right view: " + rightSideView(tree1));
        System.out.println();
        printTree(tree2, "  ");
        System.out.println("  Right view: " + rightSideView(tree2));
        System.out.println();
        printTree(tree3, "  ");
        System.out.println("  Right view: " + rightSideView(tree3));
        System.out.println();
        
        // ── TEST 7: Left side view ──
        System.out.println("═══ TEST 7: Left Side View ═══");
        System.out.println("  Tree 1 left view:  " + leftSideView(tree1));
        System.out.println("  Tree 2 left view:  " + leftSideView(tree2));
        System.out.println("  Skewed left view:  " + leftSideView(tree3));
        System.out.println();
        
        // ── TEST 8: Average of levels ──
        System.out.println("═══ TEST 8: Average of Each Level ═══");
        System.out.println("  Tree 1 levels:   " + levelOrder(tree1));
        System.out.println("  Tree 1 averages: " + averageOfLevels(tree1));
        System.out.println("  Tree 2 levels:   " + levelOrder(tree2));
        System.out.println("  Tree 2 averages: " + averageOfLevels(tree2));
        System.out.println();
        
        // ── TEST 9: Largest value in each level ──
        System.out.println("═══ TEST 9: Largest Value in Each Level ═══");
        System.out.println("  Tree 1 levels:  " + levelOrder(tree1));
        System.out.println("  Tree 1 largest: " + largestValues(tree1));
        System.out.println("  Tree 2 levels:  " + levelOrder(tree2));
        System.out.println("  Tree 2 largest: " + largestValues(tree2));
        System.out.println();
        
        // ── TEST 10: Depth and width ──
        System.out.println("═══ TEST 10: Tree Depth and Width ═══");
        TreeNode[] allTrees = {tree1, tree2, tree3, tree4};
        String[] treeNames = {"Classic", "Fuller", "Skewed", "Single"};
        
        for (int t = 0; t < allTrees.length; t++) {
            System.out.println("  " + treeNames[t] + ":"
                + " depth=" + maxDepth(allTrees[t])
                + " width=" + maxWidth(allTrees[t])
                + " levels=" + levelOrder(allTrees[t]));
        }
        System.out.println();
        
        // ── TEST 11: Larger tree ──
        System.out.println("═══ TEST 11: Larger Complete Tree ═══");
        //           1
        //        /     \
        //       2        3
        //      / \      / \
        //     4   5    6   7
        //    /\ /\   /\  /\
        //   8 9 10 11 12 13 14 15
        TreeNode largeTree = buildTree(new Integer[] {
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15
        });
        
        System.out.println("  Level order: " + levelOrder(largeTree));
        System.out.println("  Right view:  " + rightSideView(largeTree));
        System.out.println("  Left view:   " + leftSideView(largeTree));
        System.out.println("  Depth:       " + maxDepth(largeTree));
        System.out.println("  Max width:   " + maxWidth(largeTree));
        System.out.println("  Averages:    " + averageOfLevels(largeTree));
        System.out.println("  Largest:     " + largestValues(largeTree));
        System.out.println();
        
        // ── TEST 12: BFS template demonstration ──
        System.out.println("═══ TEST 12: BFS Template Summary ═══");
        System.out.println("  ┌─────────────────────────────────────────────┐");
        System.out.println("  │  BFS TEMPLATE (reusable for all variants): │");
        System.out.println("  │                                             │");
        System.out.println("  │  Queue<TreeNode> q = new LinkedList<>();    │");
        System.out.println("  │  q.offer(root);                            │");
        System.out.println("  │                                             │");
        System.out.println("  │  while (!q.isEmpty()) {                    │");
        System.out.println("  │      int size = q.size(); // SNAPSHOT      │");
        System.out.println("  │      for (int i = 0; i < size; i++) {      │");
        System.out.println("  │          TreeNode node = q.poll();         │");
        System.out.println("  │          // ← PROCESS node here           │");
        System.out.println("  │          if (node.left != null)            │");
        System.out.println("  │              q.offer(node.left);           │");
        System.out.println("  │          if (node.right != null)           │");
        System.out.println("  │              q.offer(node.right);          │");
        System.out.println("  │      }                                     │");
        System.out.println("  │      // ← LEVEL COMPLETE here             │");
        System.out.println("  │  }                                         │");
        System.out.println("  │                                             │");
        System.out.println("  │  Change only PROCESS and LEVEL COMPLETE    │");
        System.out.println("  │  to solve different BFS problems.          │");
        System.out.println("  └─────────────────────────────────────────────┘");
        System.out.println();
        
        // ── TEST 13: Performance benchmark ──
        System.out.println("═══ TEST 13: Performance Benchmark ═══");
        
        // Build a complete binary tree of depth 20 (~1M nodes)
        int depth = 20;
        int totalNodes = (1 << (depth + 1)) - 1; // 2^21 - 1
        Integer[] bigValues = new Integer[totalNodes];
        for (int i = 0; i < totalNodes; i++) {
            bigValues[i] = i + 1;
        }
        
        TreeNode bigTree = buildTree(bigValues);
        
        long start = System.nanoTime();
        List<List<Integer>> bigResult = levelOrder(bigTree);
        long time = System.nanoTime() - start;
        
        System.out.println("  Complete binary tree depth: " + depth);
        System.out.println("  Total nodes: " + totalNodes);
        System.out.println("  Levels in result: " + bigResult.size());
        System.out.println("  Last level size: " 
            + bigResult.get(bigResult.size() - 1).size());
        System.out.println("  Time: " + (time / 1_000_000) + " ms");
        System.out.println();
    }
}