import java.util.LinkedList;
import java.util.Queue;
import java.util.Arrays;

public class SerializeDeserializeBinaryTree {

    // ─────────────────────────────────────────────
    // TreeNode definition (same as Project 26)
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
    // CORE SOLUTION: SERIALIZATION (Tree → String)
    // ═══════════════════════════════════════════════
    public static String serialize(TreeNode root) {

        StringBuilder sb = new StringBuilder();
        serializeHelper(root, sb);

        // Remove trailing comma
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == ',') {
            sb.setLength(sb.length() - 1);
        }

        return sb.toString();
    }

    private static void serializeHelper(TreeNode node, StringBuilder sb) {

        // Base case: null node → write null marker
        if (node == null) {
            sb.append("null,");
            return;
        }

        // ── PREORDER: Root first ──
        sb.append(node.val).append(",");

        // ── Then left subtree ──
        serializeHelper(node.left, sb);

        // ── Then right subtree ──
        serializeHelper(node.right, sb);
    }

    // ═══════════════════════════════════════════════
    // CORE SOLUTION: DESERIALIZATION (String → Tree)
    // ═══════════════════════════════════════════════
    public static TreeNode deserialize(String data) {

        if (data == null || data.isEmpty()) {
            return null;
        }

        // Split into tokens and load into queue
        String[] tokens = data.split(",");
        Queue<String> queue = new LinkedList<>(Arrays.asList(tokens));

        return deserializeHelper(queue);
    }

    private static TreeNode deserializeHelper(Queue<String> queue) {

        // Read next token
        String token = queue.poll();

        // Base case: null marker → this subtree is empty
        if (token == null || token.equals("null")) {
            return null;
        }

        // ── PREORDER: Create root node first ──
        TreeNode node = new TreeNode(Integer.parseInt(token));

        // ── Recursively build left subtree ──
        // This consumes ALL tokens belonging to the left subtree
        node.left = deserializeHelper(queue);

        // ── Recursively build right subtree ──
        // Queue now points to the first token of right subtree
        node.right = deserializeHelper(queue);

        return node;
    }

    // ─────────────────────────────────────────────
    // TRACE VERSION: Serialization with step-by-step output
    // ─────────────────────────────────────────────
    public static String serializeWithTrace(TreeNode root) {

        StringBuilder sb = new StringBuilder();
        System.out.println("─── SERIALIZATION TRACE ───");
        serializeTraceHelper(root, sb, 0);

        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == ',') {
            sb.setLength(sb.length() - 1);
        }

        System.out.println("Final encoding: " + sb.toString());
        return sb.toString();
    }

    private static void serializeTraceHelper(TreeNode node, StringBuilder sb, int depth) {

        String indent = "  ".repeat(depth);

        if (node == null) {
            sb.append("null,");
            System.out.println(indent + "→ null (marker written)");
            return;
        }

        sb.append(node.val).append(",");
        System.out.println(indent + "→ WRITE node " + node.val
                + " | encoding so far: " + sb.toString());

        System.out.println(indent + "  [go LEFT of " + node.val + "]");
        serializeTraceHelper(node.left, sb, depth + 1);

        System.out.println(indent + "  [go RIGHT of " + node.val + "]");
        serializeTraceHelper(node.right, sb, depth + 1);
    }

    // ─────────────────────────────────────────────
    // TRACE VERSION: Deserialization with step-by-step output
    // ─────────────────────────────────────────────
    public static TreeNode deserializeWithTrace(String data) {

        if (data == null || data.isEmpty()) return null;

        String[] tokens = data.split(",");
        Queue<String> queue = new LinkedList<>(Arrays.asList(tokens));

        System.out.println("─── DESERIALIZATION TRACE ───");
        System.out.println("Tokens: " + Arrays.toString(tokens));
        System.out.println();

        return deserializeTraceHelper(queue, 0);
    }

    private static TreeNode deserializeTraceHelper(Queue<String> queue, int depth) {

        String indent = "  ".repeat(depth);
        String token = queue.poll();

        if (token == null || token.equals("null")) {
            System.out.println(indent + "→ Read 'null' → return null"
                    + " | remaining: " + queue);
            return null;
        }

        int val = Integer.parseInt(token);
        System.out.println(indent + "→ Read '" + val + "' → CREATE node(" + val + ")"
                + " | remaining: " + queue);

        TreeNode node = new TreeNode(val);

        System.out.println(indent + "  [build LEFT child of " + val + "]");
        node.left = deserializeTraceHelper(queue, depth + 1);

        System.out.println(indent + "  [build RIGHT child of " + val + "]");
        node.right = deserializeTraceHelper(queue, depth + 1);

        System.out.println(indent + "  ✓ node(" + val + ") complete"
                + " | left=" + (node.left == null ? "null" : node.left.val)
                + " | right=" + (node.right == null ? "null" : node.right.val));

        return node;
    }

    // ─────────────────────────────────────────────
    // VARIANT: Using int[] index instead of Queue
    // ─────────────────────────────────────────────
    public static TreeNode deserializeWithIndex(String data) {

        if (data == null || data.isEmpty()) return null;

        String[] tokens = data.split(",");
        int[] index = {0};  // mutable container — shared across recursion

        return buildWithIndex(tokens, index);
    }

    private static TreeNode buildWithIndex(String[] tokens, int[] index) {

        if (index[0] >= tokens.length) return null;

        String token = tokens[index[0]];
        index[0]++;  // advance the shared index

        if (token.equals("null")) return null;

        TreeNode node = new TreeNode(Integer.parseInt(token));
        node.left = buildWithIndex(tokens, index);
        node.right = buildWithIndex(tokens, index);

        return node;
    }

    // ─────────────────────────────────────────────
    // VARIANT: Level-Order (BFS) Serialization
    // This is what LeetCode uses internally
    // ─────────────────────────────────────────────
    public static String serializeBFS(TreeNode root) {

        if (root == null) return "null";

        StringBuilder sb = new StringBuilder();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();

            if (node == null) {
                sb.append("null,");
            } else {
                sb.append(node.val).append(",");
                queue.offer(node.left);   // even if null — we record it
                queue.offer(node.right);  // even if null — we record it
            }
        }

        // Remove trailing comma
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == ',') {
            sb.setLength(sb.length() - 1);
        }

        return sb.toString();
    }

    public static TreeNode deserializeBFS(String data) {

        if (data == null || data.equals("null")) return null;

        String[] tokens = data.split(",");
        TreeNode root = new TreeNode(Integer.parseInt(tokens[0]));
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        int i = 1;

        while (!queue.isEmpty() && i < tokens.length) {
            TreeNode parent = queue.poll();

            // Left child
            if (i < tokens.length) {
                if (!tokens[i].equals("null")) {
                    parent.left = new TreeNode(Integer.parseInt(tokens[i]));
                    queue.offer(parent.left);
                }
                i++;
            }

            // Right child
            if (i < tokens.length) {
                if (!tokens[i].equals("null")) {
                    parent.right = new TreeNode(Integer.parseInt(tokens[i]));
                    queue.offer(parent.right);
                }
                i++;
            }
        }

        return root;
    }

    // ─────────────────────────────────────────────
    // UTILITY: Compare two trees for structural equality
    // ─────────────────────────────────────────────
    public static boolean treesEqual(TreeNode a, TreeNode b) {

        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        if (a.val != b.val) return false;

        return treesEqual(a.left, b.left) && treesEqual(a.right, b.right);
    }

    // ─────────────────────────────────────────────
    // UTILITY: Print tree structure
    // ─────────────────────────────────────────────
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

        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 27: Serialize & Deserialize Binary Tree ║");
        System.out.println("║  Pattern: Tree DFS → Preorder Encoding →         ║");
        System.out.println("║           Null Markers → Reconstruction          ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println();

        // ── Build test tree ──
        //          1
        //         / \
        //        2    3
        //            / \
        //           4   5
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.right.left = new TreeNode(4);
        root.right.right = new TreeNode(5);

        System.out.println("═══ ORIGINAL TREE ═══");
        printTree(root, "", false);
        System.out.println();

        // ── TEST 1: Basic serialization ──
        System.out.println("═══ TEST 1: Basic Serialization ═══");
        String encoded = serialize(root);
        System.out.println("Encoded: " + encoded);
        System.out.println();

        // ── TEST 2: Basic deserialization ──
        System.out.println("═══ TEST 2: Basic Deserialization ═══");
        TreeNode decoded = deserialize(encoded);
        System.out.println("Decoded tree:");
        printTree(decoded, "", false);
        System.out.println("Trees equal? " + treesEqual(root, decoded));
        System.out.println();

        // ── TEST 3: Traced serialization ──
        System.out.println("═══ TEST 3: Traced Serialization ═══");
        serializeWithTrace(root);
        System.out.println();

        // ── TEST 4: Traced deserialization ──
        System.out.println("═══ TEST 4: Traced Deserialization ═══");
        TreeNode tracedResult = deserializeWithTrace(encoded);
        System.out.println("\nReconstructed tree:");
        printTree(tracedResult, "", false);
        System.out.println();

        // ── TEST 5: Round-trip verification ──
        System.out.println("═══ TEST 5: Round-Trip Verification ═══");
        String reEncoded = serialize(tracedResult);
        System.out.println("Original encoding:    " + encoded);
        System.out.println("Re-encoded:           " + reEncoded);
        System.out.println("Encodings match?      " + encoded.equals(reEncoded));
        System.out.println();

        // ── TEST 6: Null tree ──
        System.out.println("═══ TEST 6: Null Tree ═══");
        String nullEncoded = serialize(null);
        System.out.println("Encoded null tree: \"" + nullEncoded + "\"");
        TreeNode nullDecoded = deserialize(nullEncoded);
        System.out.println("Decoded: " + nullDecoded);
        System.out.println();

        // ── TEST 7: Single node ──
        System.out.println("═══ TEST 7: Single Node Tree ═══");
        TreeNode single = new TreeNode(42);
        String singleEncoded = serialize(single);
        System.out.println("Encoded: " + singleEncoded);
        TreeNode singleDecoded = deserialize(singleEncoded);
        System.out.println("Decoded val: " + singleDecoded.val);
        System.out.println("Left: " + singleDecoded.left + " Right: " + singleDecoded.right);
        System.out.println();

        // ── TEST 8: Skewed tree (all left) ──
        System.out.println("═══ TEST 8: Left-Skewed Tree ═══");
        TreeNode skewed = new TreeNode(1);
        skewed.left = new TreeNode(2);
        skewed.left.left = new TreeNode(3);
        skewed.left.left.left = new TreeNode(4);

        System.out.println("Original:");
        printTree(skewed, "", false);
        String skewedEncoded = serialize(skewed);
        System.out.println("Encoded: " + skewedEncoded);
        TreeNode skewedDecoded = deserialize(skewedEncoded);
        System.out.println("Round-trip match? " + treesEqual(skewed, skewedDecoded));
        System.out.println();

        // ── TEST 9: Negative values ──
        System.out.println("═══ TEST 9: Negative Values ═══");
        TreeNode negTree = new TreeNode(-1);
        negTree.left = new TreeNode(-5);
        negTree.right = new TreeNode(3);
        negTree.left.right = new TreeNode(-8);

        System.out.println("Original:");
        printTree(negTree, "", false);
        String negEncoded = serialize(negTree);
        System.out.println("Encoded: " + negEncoded);
        TreeNode negDecoded = deserialize(negEncoded);
        System.out.println("Round-trip match? " + treesEqual(negTree, negDecoded));
        System.out.println();

        // ── TEST 10: int[] index variant ──
        System.out.println("═══ TEST 10: Index-Based Deserialization ═══");
        TreeNode indexResult = deserializeWithIndex(encoded);
        System.out.println("Original encoding: " + encoded);
        System.out.println("Index-based decoded tree:");
        printTree(indexResult, "", false);
        System.out.println("Match with Queue-based? " + treesEqual(decoded, indexResult));
        System.out.println();

        // ── TEST 11: BFS Serialization Comparison ──
        System.out.println("═══ TEST 11: BFS vs DFS Serialization ═══");
        String dfsEncoded = serialize(root);
        String bfsEncoded = serializeBFS(root);
        System.out.println("DFS (preorder): " + dfsEncoded);
        System.out.println("BFS (level):    " + bfsEncoded);
        System.out.println("Different encodings, same tree:");

        TreeNode fromDFS = deserialize(dfsEncoded);
        TreeNode fromBFS = deserializeBFS(bfsEncoded);
        System.out.println("DFS decoded == BFS decoded? " + treesEqual(fromDFS, fromBFS));
        System.out.println();

        // ── TEST 12: Larger tree stress test ──
        System.out.println("═══ TEST 12: Stress Test ═══");
        TreeNode big = buildPerfectTree(1, 10);  // depth 10 → 1023 nodes
        String bigEncoded = serialize(big);
        TreeNode bigDecoded = deserialize(bigEncoded);
        System.out.println("Nodes in tree: 1023 (perfect tree depth 10)");
        System.out.println("Encoding length: " + bigEncoded.length() + " characters");
        System.out.println("Round-trip match? " + treesEqual(big, bigDecoded));
    }

    // Helper: build a perfect binary tree with values 1..n
    static TreeNode buildPerfectTree(int val, int depth) {
        if (depth == 0) return null;
        TreeNode node = new TreeNode(val);
        node.left = buildPerfectTree(val * 2, depth - 1);
        node.right = buildPerfectTree(val * 2 + 1, depth - 1);
        return node;
    }
}