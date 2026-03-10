import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ConnectLevelOrderSiblings {

    // ─────────────────────────────────────────────
    // TREE NODE WITH NEXT POINTER
    // ─────────────────────────────────────────────
    static class Node {
        int val;
        Node left;
        Node right;
        Node next;   // ← THE NEW POINTER

        Node(int val) {
            this.val = val;
            this.left = null;
            this.right = null;
            this.next = null;
        }
    }

    // ═══════════════════════════════════════════════
    // SOLUTION 1: BFS + PREV TRACKING (General — Any Tree)
    // O(n) time, O(w) space
    // ═══════════════════════════════════════════════
    public static Node connectBFS(Node root) {

        if (root == null) {
            return null;
        }

        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {

            int levelSize = queue.size();
            Node prev = null;    // Reset for each level

            for (int i = 0; i < levelSize; i++) {

                Node node = queue.poll();

                // Link previous node to current
                if (prev != null) {
                    prev.next = node;
                }

                // Current becomes previous for next iteration
                prev = node;

                // Enqueue children (always left then right)
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }

            // prev is now the last node of this level
            // prev.next is already null (default) → correct
        }

        return root;
    }

    // ═══════════════════════════════════════════════
    // SOLUTION 2: O(1) SPACE — Perfect Binary Tree
    // Uses established next pointers to traverse levels
    // ═══════════════════════════════════════════════
    public static Node connectPerfectTree(Node root) {

        if (root == null) {
            return null;
        }

        // leftmost tracks the first node of the current level
        Node leftmost = root;

        // Process while next level exists
        // For perfect tree: if leftmost has a left child → next level exists
        while (leftmost.left != null) {

            // Traverse current level using next pointers
            Node curr = leftmost;

            while (curr != null) {

                // CONNECTION 1: Link left child → right child
                // These are children of the SAME parent
                curr.left.next = curr.right;

                // CONNECTION 2: Link right child → next node's left child
                // These are COUSINS (children of adjacent parents)
                if (curr.next != null) {
                    curr.right.next = curr.next.left;
                }

                // Move to next node at current level
                curr = curr.next;
            }

            // Move down to next level
            leftmost = leftmost.left;
        }

        return root;
    }

    // ═══════════════════════════════════════════════
    // SOLUTION 3: O(1) SPACE — Arbitrary Binary Tree
    // Uses dummy node to build next-level linked list
    // ═══════════════════════════════════════════════
    public static Node connectArbitraryTree(Node root) {

        if (root == null) {
            return null;
        }

        Node curr = root;  // Start of current level

        while (curr != null) {

            // Dummy head for next level's linked list
            Node dummy = new Node(0);
            Node tail = dummy;

            // Traverse current level using next pointers
            Node temp = curr;
            while (temp != null) {

                // Add left child to next level's list
                if (temp.left != null) {
                    tail.next = temp.left;
                    tail = tail.next;
                }

                // Add right child to next level's list
                if (temp.right != null) {
                    tail.next = temp.right;
                    tail = tail.next;
                }

                // Move across current level
                temp = temp.next;
            }

            // Move to next level
            curr = dummy.next;
        }

        return root;
    }

    // ═══════════════════════════════════════════════
    // TRACE VERSION — BFS with full visualization
    // ═══════════════════════════════════════════════
    public static Node connectWithTrace(Node root) {

        if (root == null) {
            System.out.println("  Empty tree → return null");
            return null;
        }

        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);
        int level = 0;

        while (!queue.isEmpty()) {

            int levelSize = queue.size();
            Node prev = null;

            System.out.println("  ═══ LEVEL " + level + " (" + levelSize + " nodes) ═══");

            // Show queue
            System.out.print("  Queue: [");
            int idx = 0;
            for (Node n : queue) {
                System.out.print(n.val);
                if (idx < queue.size() - 1) System.out.print(", ");
                idx++;
            }
            System.out.println("]");

            for (int i = 0; i < levelSize; i++) {

                Node node = queue.poll();

                if (prev != null) {
                    prev.next = node;
                    System.out.println("    Process node(" + node.val
                            + ") → link: node(" + prev.val
                            + ").next = node(" + node.val + ")");
                } else {
                    System.out.println("    Process node(" + node.val
                            + ") → first in level → no left neighbor");
                }

                prev = node;

                if (node.left != null) {
                    queue.offer(node.left);
                    System.out.println("      Enqueue left child: " + node.left.val);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                    System.out.println("      Enqueue right child: " + node.right.val);
                }
            }

            // Print the chain for this level
            System.out.print("  Level " + level + " chain: ");
            // Find first node of this level (it was prev at start... 
            // but we need to traverse from the first node we processed)
            // Actually, we can traverse from the first node we set prev to
            // Let's reconstruct from prev going backwards... or just print from trace

            // Simple: traverse the next pointers from level start
            // For the trace, we'll verify after all levels are done
            System.out.println("(verified after processing)");
            System.out.println();
            level++;
        }

        // Verify and print all level chains
        System.out.println("  ═══ VERIFICATION — Next Pointer Chains ═══");
        printNextChains(root);

        return root;
    }

    // ─────────────────────────────────────────────
    // VERIFICATION: Print next pointer chains
    // ─────────────────────────────────────────────
    public static void printNextChains(Node root) {

        if (root == null) {
            System.out.println("  (empty tree)");
            return;
        }

        // BFS to find first node of each level
        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);
        int level = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();
            Node firstInLevel = null;

            for (int i = 0; i < size; i++) {
                Node node = queue.poll();
                if (i == 0) firstInLevel = node;
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }

            // Traverse next pointers from first node
            StringBuilder chain = new StringBuilder("  Level " + level + ": ");
            Node curr = firstInLevel;
            while (curr != null) {
                chain.append(curr.val).append(" → ");
                curr = curr.next;
            }
            chain.append("null");
            System.out.println(chain.toString());

            level++;
        }
    }

    // ─────────────────────────────────────────────
    // VISUAL TREE PRINTER (with next pointers shown)
    // ─────────────────────────────────────────────
    public static void printTreeWithNext(Node root) {

        if (root == null) {
            System.out.println("  (empty tree)");
            return;
        }

        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);
        int level = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();
            StringBuilder nodes = new StringBuilder();
            StringBuilder nexts = new StringBuilder();
            boolean hasChildren = false;

            nodes.append("  Level ").append(level).append(" nodes:  ");
            nexts.append("  Level ").append(level).append(" next:   ");

            for (int i = 0; i < size; i++) {
                Node node = queue.poll();
                nodes.append(String.format("%-4d", node.val));
                nexts.append(node.next != null
                        ? "→" + node.next.val + "  "
                        : "→N  ");

                if (node.left != null) {
                    queue.offer(node.left);
                    hasChildren = true;
                }
                if (node.right != null) {
                    queue.offer(node.right);
                    hasChildren = true;
                }
            }

            System.out.println(nodes.toString());
            System.out.println(nexts.toString());

            if (!hasChildren && queue.isEmpty()) break;
            level++;
        }
    }

    // ─────────────────────────────────────────────
    // TREE BUILDERS
    // ─────────────────────────────────────────────

    // Perfect tree:    1
    //                 / \
    //                2   3
    //               / \ / \
    //              4  5 6  7
    public static Node buildPerfectTree() {
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.left.right = new Node(5);
        root.right.left = new Node(6);
        root.right.right = new Node(7);
        return root;
    }

    // Arbitrary tree:  1
    //                 / \
    //                2   3
    //               / \   \
    //              4   5   7
    public static Node buildArbitraryTree() {
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.left.right = new Node(5);
        root.right.right = new Node(7);
        return root;
    }

    // Skewed tree:  1
    //              / \
    //             2   3
    //            /     \
    //           4       7
    //          /         \
    //         8           9
    public static Node buildSkewedTree() {
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.right.right = new Node(7);
        root.left.left.left = new Node(8);
        root.right.right.right = new Node(9);
        return root;
    }

    // Large perfect tree for performance testing
    public static Node buildLargePerfectTree(int levels) {
        if (levels <= 0) return null;
        Node root = new Node(1);
        Queue<Node> q = new LinkedList<>();
        q.offer(root);
        int val = 2;
        for (int l = 1; l < levels; l++) {
            int sz = q.size();
            for (int i = 0; i < sz; i++) {
                Node node = q.poll();
                node.left = new Node(val++);
                node.right = new Node(val++);
                q.offer(node.left);
                q.offer(node.right);
            }
        }
        return root;
    }

    // Deep copy a tree (for testing multiple approaches on same structure)
    public static Node deepCopy(Node root) {
        if (root == null) return null;
        Node copy = new Node(root.val);
        copy.left = deepCopy(root.left);
        copy.right = deepCopy(root.right);
        return copy;
    }

    // ─────────────────────────────────────────────
    // COMPARISON HELPER: Extract chains for verification
    // ─────────────────────────────────────────────
    public static List<List<Integer>> extractChains(Node root) {
        List<List<Integer>> chains = new ArrayList<>();
        if (root == null) return chains;

        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int size = queue.size();
            Node firstInLevel = null;

            for (int i = 0; i < size; i++) {
                Node node = queue.poll();
                if (i == 0) firstInLevel = node;
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }

            // Traverse chain via next
            List<Integer> chain = new ArrayList<>();
            Node curr = firstInLevel;
            while (curr != null) {
                chain.add(curr.val);
                curr = curr.next;
            }
            chains.add(chain);
        }

        return chains;
    }

    // ─────────────────────────────────────────────
    // MAIN — Run all tests
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 25: Connect Level Order Siblings (Next Ptr)   ║");
        System.out.println("║  Pattern: Tree BFS → Sibling Linking → Queue Awareness ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Perfect tree with trace ──
        System.out.println("═══ TEST 1: Perfect Binary Tree — Full Trace ═══");
        Node tree1 = buildPerfectTree();
        System.out.println("  BEFORE:");
        printNextChains(tree1);
        System.out.println();
        connectWithTrace(tree1);
        System.out.println();

        // ── TEST 2: Arbitrary tree with trace ──
        System.out.println("═══ TEST 2: Arbitrary Tree — Full Trace ═══");
        Node tree2 = buildArbitraryTree();
        System.out.println("  BEFORE:");
        printNextChains(tree2);
        System.out.println();
        connectWithTrace(tree2);
        System.out.println();

        // ── TEST 3: Skewed tree ──
        System.out.println("═══ TEST 3: Skewed Tree — Sparse Levels ═══");
        Node tree3 = buildSkewedTree();
        connectBFS(tree3);
        System.out.println("  After connecting:");
        printNextChains(tree3);
        System.out.println();

        // ── TEST 4: Single node ──
        System.out.println("═══ TEST 4: Single Node ═══");
        Node single = new Node(42);
        connectBFS(single);
        printNextChains(single);
        System.out.println();

        // ── TEST 5: Empty tree ──
        System.out.println("═══ TEST 5: Empty Tree ═══");
        Node result5 = connectBFS(null);
        System.out.println("  Result: " + (result5 == null ? "null" : result5.val));
        System.out.println();

        // ── TEST 6: Two-level tree ──
        System.out.println("═══ TEST 6: Two-Level Tree ═══");
        Node twoLevel = new Node(1);
        twoLevel.left = new Node(2);
        twoLevel.right = new Node(3);
        connectBFS(twoLevel);
        printNextChains(twoLevel);
        System.out.println();

        // ── TEST 7: Left-only chain ──
        System.out.println("═══ TEST 7: Left-Only Chain ═══");
        Node leftOnly = new Node(1);
        leftOnly.left = new Node(2);
        leftOnly.left.left = new Node(3);
        leftOnly.left.left.left = new Node(4);
        connectBFS(leftOnly);
        printNextChains(leftOnly);
        System.out.println("  (Each level has 1 node → all next = null)");
        System.out.println();

        // ── TEST 8: Visual before/after comparison ──
        System.out.println("═══ TEST 8: Visual Before/After — Perfect Tree ═══");
        Node tree8 = buildPerfectTree();
        System.out.println("  BEFORE connecting:");
        printTreeWithNext(tree8);
        System.out.println();
        connectBFS(tree8);
        System.out.println("  AFTER connecting:");
        printTreeWithNext(tree8);
        System.out.println();

        // ── TEST 9: All three approaches agree ──
        System.out.println("═══ TEST 9: Verify All Approaches Agree ═══");

        // Perfect tree — all 3 approaches
        System.out.println("  Perfect Tree:");
        Node pt1 = buildPerfectTree();
        Node pt2 = deepCopy(pt1);
        Node pt3 = deepCopy(pt1);

        connectBFS(pt1);
        connectPerfectTree(pt2);
        connectArbitraryTree(pt3);

        List<List<Integer>> chains1 = extractChains(pt1);
        List<List<Integer>> chains2 = extractChains(pt2);
        List<List<Integer>> chains3 = extractChains(pt3);

        boolean perfectMatch = chains1.equals(chains2) && chains2.equals(chains3);
        System.out.println("    BFS:       " + chains1);
        System.out.println("    O(1) Perf: " + chains2);
        System.out.println("    O(1) Arb:  " + chains3);
        System.out.println("    All match: " + perfectMatch);

        // Arbitrary tree — BFS vs O(1) arbitrary
        System.out.println("  Arbitrary Tree:");
        Node at1 = buildArbitraryTree();
        Node at2 = deepCopy(at1);

        connectBFS(at1);
        connectArbitraryTree(at2);

        List<List<Integer>> aChains1 = extractChains(at1);
        List<List<Integer>> aChains2 = extractChains(at2);

        boolean arbMatch = aChains1.equals(aChains2);
        System.out.println("    BFS:       " + aChains1);
        System.out.println("    O(1) Arb:  " + aChains2);
        System.out.println("    Match: " + arbMatch);

        // Skewed tree
        System.out.println("  Skewed Tree:");
        Node st1 = buildSkewedTree();
        Node st2 = deepCopy(st1);

        connectBFS(st1);
        connectArbitraryTree(st2);

        List<List<Integer>> sChains1 = extractChains(st1);
        List<List<Integer>> sChains2 = extractChains(st2);

        boolean skewMatch = sChains1.equals(sChains2);
        System.out.println("    BFS:       " + sChains1);
        System.out.println("    O(1) Arb:  " + sChains2);
        System.out.println("    Match: " + skewMatch);

        System.out.println("  ALL TESTS PASS: " + (perfectMatch && arbMatch && skewMatch));
        System.out.println();

        // ── TEST 10: Performance comparison ──
        System.out.println("═══ TEST 10: Performance — Perfect Tree (18 levels) ═══");
        int levels = 18;
        int nodeCount = (1 << levels) - 1;
        System.out.println("  Levels: " + levels + " | Nodes: " + nodeCount);

        // BFS approach
        Node perfTree1 = buildLargePerfectTree(levels);
        long start = System.nanoTime();
        connectBFS(perfTree1);
        long bfsTime = System.nanoTime() - start;

        // O(1) perfect tree approach
        Node perfTree2 = buildLargePerfectTree(levels);
        start = System.nanoTime();
        connectPerfectTree(perfTree2);
        long perfectTime = System.nanoTime() - start;

        // O(1) arbitrary tree approach
        Node perfTree3 = buildLargePerfectTree(levels);
        start = System.nanoTime();
        connectArbitraryTree(perfTree3);
        long arbTime = System.nanoTime() - start;

        System.out.println("  BFS (queue):          " + (bfsTime / 1_000_000) + " ms");
        System.out.println("  O(1) perfect tree:    " + (perfectTime / 1_000_000) + " ms");
        System.out.println("  O(1) arbitrary (dummy):" + (arbTime / 1_000_000) + " ms");

        // Verify all produce same result
        List<List<Integer>> pChains1 = extractChains(perfTree1);
        List<List<Integer>> pChains2 = extractChains(perfTree2);
        List<List<Integer>> pChains3 = extractChains(perfTree3);
        System.out.println("  All results match: "
                + (pChains1.equals(pChains2) && pChains2.equals(pChains3)));
        System.out.println("  Last level chain length: "
                + pChains1.get(pChains1.size() - 1).size());
    }
}