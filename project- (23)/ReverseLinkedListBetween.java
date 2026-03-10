import java.util.ArrayList;
import java.util.List;

public class ReverseLinkedListBetween {

    // ─────────────────────────────────────────────
    // NODE DEFINITION
    // ─────────────────────────────────────────────
    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
            this.next = null;
        }
    }

    // ═══════════════════════════════════════════════
    // SOLUTION 1: MOVE-TO-FRONT TECHNIQUE
    // O(n) time, O(1) space — Optimal
    // ═══════════════════════════════════════════════
    public static ListNode reverseBetween(ListNode head, int left, int right) {

        // Edge case: nothing to reverse
        if (head == null || left == right) {
            return head;
        }

        // Step 1: Create dummy node → handles left=1 edge case
        ListNode dummy = new ListNode(0);
        dummy.next = head;

        // Step 2: Navigate to beforeLeft (position left-1)
        ListNode beforeLeft = dummy;
        for (int i = 0; i < left - 1; i++) {
            beforeLeft = beforeLeft.next;
        }

        // Step 3: Identify leftNode (will become last in reversed portion)
        ListNode leftNode = beforeLeft.next;

        // Step 4: Reverse using "move to front" technique
        // Each iteration moves leftNode.next to the front
        for (int i = 0; i < right - left; i++) {

            // The node to move is always leftNode.next
            ListNode nodeToMove = leftNode.next;

            // Operation A: Detach nodeToMove from current position
            leftNode.next = nodeToMove.next;

            // Operation B: Point nodeToMove to current front
            nodeToMove.next = beforeLeft.next;

            // Operation C: Make nodeToMove the new front
            beforeLeft.next = nodeToMove;
        }

        // Step 5: Return the real head (may have changed if left=1)
        return dummy.next;
    }

    // ═══════════════════════════════════════════════
    // SOLUTION 2: CLASSIC REVERSAL + STITCHING
    // (Alternative approach for understanding)
    // ═══════════════════════════════════════════════
    public static ListNode reverseBetweenClassic(ListNode head, int left, int right) {

        if (head == null || left == right) {
            return head;
        }

        ListNode dummy = new ListNode(0);
        dummy.next = head;

        // Navigate to beforeLeft
        ListNode beforeLeft = dummy;
        for (int i = 0; i < left - 1; i++) {
            beforeLeft = beforeLeft.next;
        }

        // Classic reversal on the sub-portion
        // Similar to Project 16 but limited to (right-left+1) nodes
        ListNode prev = null;
        ListNode curr = beforeLeft.next;  // leftNode
        ListNode leftNode = curr;         // save reference

        for (int i = 0; i <= right - left; i++) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }

        // After classic reversal:
        // prev = rightNode (new front of reversed portion)
        // curr = afterRight (first node after reversed portion)
        // leftNode.next = null (was set to prev which was initially null)

        // Stitch: beforeLeft → reversed front (prev)
        beforeLeft.next = prev;

        // Stitch: leftNode (now last) → afterRight (curr)
        leftNode.next = curr;

        return dummy.next;
    }

    // ═══════════════════════════════════════════════
    // TRACE VERSION — Full step-by-step visualization
    // ═══════════════════════════════════════════════
    public static ListNode reverseBetweenWithTrace(ListNode head, int left, int right) {

        System.out.println("  Input list: " + listToString(head));
        System.out.println("  Reverse positions " + left + " to " + right);
        System.out.println();

        if (head == null || left == right) {
            System.out.println("  Nothing to reverse → return as-is");
            return head;
        }

        // Dummy node
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        System.out.println("  After adding dummy: " + listToStringFromDummy(dummy));

        // Navigate to beforeLeft
        ListNode beforeLeft = dummy;
        for (int i = 0; i < left - 1; i++) {
            beforeLeft = beforeLeft.next;
        }
        System.out.println("  beforeLeft = node(" + beforeLeft.val + ") at position " + (left - 1));

        ListNode leftNode = beforeLeft.next;
        System.out.println("  leftNode = node(" + leftNode.val + ") at position " + left);
        System.out.println();

        // Reversal iterations
        System.out.println("  ═══ Reversal Process (" + (right - left) + " iterations) ═══");

        for (int i = 0; i < right - left; i++) {
            ListNode nodeToMove = leftNode.next;
            System.out.println("  Iteration " + (i + 1) + ":");
            System.out.println("    nodeToMove = node(" + nodeToMove.val + ")");

            // A: Detach
            leftNode.next = nodeToMove.next;
            System.out.println("    A: leftNode(" + leftNode.val + ").next = "
                    + (leftNode.next != null ? "node(" + leftNode.next.val + ")" : "null")
                    + "  [detach nodeToMove]");

            // B: Point to front
            nodeToMove.next = beforeLeft.next;
            System.out.println("    B: nodeToMove(" + nodeToMove.val + ").next = node("
                    + beforeLeft.next.val + ")  [point to current front]");

            // C: Become new front
            beforeLeft.next = nodeToMove;
            System.out.println("    C: beforeLeft(" + beforeLeft.val + ").next = node("
                    + nodeToMove.val + ")  [new front]");

            System.out.println("    State: " + listToStringFromDummy(dummy));
            System.out.println();
        }

        ListNode result = dummy.next;
        System.out.println("  ★ FINAL: " + listToString(result));

        return result;
    }

    // ─────────────────────────────────────────────
    // LIST BUILDER — Create list from array
    // ─────────────────────────────────────────────
    public static ListNode buildList(int[] values) {
        if (values.length == 0) return null;
        ListNode head = new ListNode(values[0]);
        ListNode current = head;
        for (int i = 1; i < values.length; i++) {
            current.next = new ListNode(values[i]);
            current = current.next;
        }
        return head;
    }

    // ─────────────────────────────────────────────
    // LIST TO ARRAY — For comparison
    // ─────────────────────────────────────────────
    public static List<Integer> listToArray(ListNode head) {
        List<Integer> result = new ArrayList<>();
        ListNode curr = head;
        while (curr != null) {
            result.add(curr.val);
            curr = curr.next;
        }
        return result;
    }

    // ─────────────────────────────────────────────
    // LIST PRINTERS
    // ─────────────────────────────────────────────
    public static String listToString(ListNode head) {
        StringBuilder sb = new StringBuilder();
        ListNode curr = head;
        while (curr != null) {
            sb.append(curr.val);
            if (curr.next != null) sb.append(" → ");
            curr = curr.next;
        }
        if (sb.length() == 0) sb.append("(empty)");
        return sb.toString();
    }

    public static String listToStringFromDummy(ListNode dummy) {
        StringBuilder sb = new StringBuilder("dummy(");
        sb.append(dummy.val).append(") → ");
        ListNode curr = dummy.next;
        while (curr != null) {
            sb.append(curr.val);
            if (curr.next != null) sb.append(" → ");
            curr = curr.next;
        }
        return sb.toString();
    }

    // ─────────────────────────────────────────────
    // VISUAL COMPARISON PRINTER
    // ─────────────────────────────────────────────
    public static void printComparison(int[] original, int left, int right,
                                        ListNode result) {
        StringBuilder before = new StringBuilder("  Before: ");
        StringBuilder after = new StringBuilder("  After:  ");

        for (int i = 0; i < original.length; i++) {
            if (i == left - 1) before.append("[");
            before.append(original[i]);
            if (i == right - 1) before.append("]");
            if (i < original.length - 1) before.append(" → ");
        }

        List<Integer> resultArr = listToArray(result);
        for (int i = 0; i < resultArr.size(); i++) {
            if (i == left - 1) after.append("[");
            after.append(resultArr.get(i));
            if (i == right - 1) after.append("]");
            if (i < resultArr.size() - 1) after.append(" → ");
        }

        System.out.println(before.toString());
        System.out.println(after.toString());
    }

    // ─────────────────────────────────────────────
    // MAIN — Run all tests
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔═══════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 23: Reverse a Sub-portion of a Linked List   ║");
        System.out.println("║  Pattern: In-place Reversal → Boundary Stitching      ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Basic middle reversal with trace ──
        System.out.println("═══ TEST 1: Middle Reversal — left=2, right=4 ═══");
        ListNode list1 = buildList(new int[] {1, 2, 3, 4, 5});
        ListNode result1 = reverseBetweenWithTrace(list1, 2, 4);
        System.out.println();

        // ── TEST 2: Full reversal with trace ──
        System.out.println("═══ TEST 2: Full Reversal — left=1, right=5 ═══");
        ListNode list2 = buildList(new int[] {1, 2, 3, 4, 5});
        ListNode result2 = reverseBetweenWithTrace(list2, 1, 5);
        System.out.println();

        // ── TEST 3: Head changes (left=1) with trace ──
        System.out.println("═══ TEST 3: Head Changes — left=1, right=3 ═══");
        ListNode list3 = buildList(new int[] {1, 2, 3, 4, 5});
        ListNode result3 = reverseBetweenWithTrace(list3, 1, 3);
        System.out.println();

        // ── TEST 4: Single node reversal ──
        System.out.println("═══ TEST 4: Single Node Reversal — left=3, right=3 ═══");
        int[] arr4 = {1, 2, 3, 4, 5};
        ListNode list4 = buildList(arr4);
        ListNode result4 = reverseBetween(list4, 3, 3);
        printComparison(arr4, 3, 3, result4);
        System.out.println("  (No change — single node reversal)");
        System.out.println();

        // ── TEST 5: Two-node swap ──
        System.out.println("═══ TEST 5: Two-Node Swap — left=2, right=3 ═══");
        int[] arr5 = {1, 2, 3, 4, 5};
        ListNode list5 = buildList(arr5);
        ListNode result5 = reverseBetween(list5, 2, 3);
        printComparison(arr5, 2, 3, result5);
        System.out.println();

        // ── TEST 6: End of list reversal ──
        System.out.println("═══ TEST 6: End of List — left=3, right=5 ═══");
        int[] arr6 = {1, 2, 3, 4, 5};
        ListNode list6 = buildList(arr6);
        ListNode result6 = reverseBetween(list6, 3, 5);
        printComparison(arr6, 3, 5, result6);
        System.out.println();

        // ── TEST 7: Two-element list ──
        System.out.println("═══ TEST 7: Two-Element List — left=1, right=2 ═══");
        int[] arr7 = {1, 2};
        ListNode list7 = buildList(arr7);
        ListNode result7 = reverseBetween(list7, 1, 2);
        printComparison(arr7, 1, 2, result7);
        System.out.println();

        // ── TEST 8: Single-element list ──
        System.out.println("═══ TEST 8: Single-Element List ═══");
        ListNode list8 = buildList(new int[] {5});
        ListNode result8 = reverseBetween(list8, 1, 1);
        System.out.println("  Before: 5");
        System.out.println("  After:  " + listToString(result8));
        System.out.println();

        // ── TEST 9: Verify both approaches agree ──
        System.out.println("═══ TEST 9: Move-to-Front vs Classic Reversal ═══");
        int[][] testArrays = {
            {1, 2, 3, 4, 5},
            {1, 2, 3, 4, 5},
            {1, 2, 3, 4, 5},
            {1, 2, 3, 4, 5},
            {10, 20, 30},
            {1, 2},
            {7},
            {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}
        };
        int[][] leftRight = {
            {2, 4}, {1, 5}, {1, 3}, {3, 5},
            {1, 2}, {1, 2}, {1, 1}, {3, 8}
        };

        boolean allMatch = true;
        for (int t = 0; t < testArrays.length; t++) {
            ListNode l1 = buildList(testArrays[t]);
            ListNode l2 = buildList(testArrays[t]);

            ListNode r1 = reverseBetween(l1, leftRight[t][0], leftRight[t][1]);
            ListNode r2 = reverseBetweenClassic(l2, leftRight[t][0], leftRight[t][1]);

            List<Integer> arr1 = listToArray(r1);
            List<Integer> arr2 = listToArray(r2);
            boolean match = arr1.equals(arr2);

            System.out.println("  Test " + (t + 1)
                    + " [L=" + leftRight[t][0] + ",R=" + leftRight[t][1] + "]: "
                    + "MoveToFront=" + arr1
                    + " Classic=" + arr2
                    + (match ? " ✓" : " ✗ MISMATCH"));
            if (!match) allMatch = false;
        }
        System.out.println("  All match: " + allMatch);
        System.out.println();

        // ── TEST 10: Larger list performance ──
        System.out.println("═══ TEST 10: Performance — 1,000,000 Nodes ═══");
        int size = 1_000_000;
        int[] largeArr = new int[size];
        for (int i = 0; i < size; i++) {
            largeArr[i] = i + 1;
        }

        // Reverse middle third
        int l = size / 3;
        int r = 2 * size / 3;

        ListNode largeList1 = buildList(largeArr);
        long start = System.nanoTime();
        ListNode largeResult1 = reverseBetween(largeList1, l, r);
        long moveToFrontTime = System.nanoTime() - start;

        ListNode largeList2 = buildList(largeArr);
        start = System.nanoTime();
        ListNode largeResult2 = reverseBetweenClassic(largeList2, l, r);
        long classicTime = System.nanoTime() - start;

        System.out.println("  Nodes: " + size
                + " | Reverse positions " + l + " to " + r);
        System.out.println("  Move-to-front: " + (moveToFrontTime / 1_000_000) + " ms");
        System.out.println("  Classic+Stitch: " + (classicTime / 1_000_000) + " ms");

        // Verify first few and last few nodes
        List<Integer> sample1 = new ArrayList<>();
        ListNode curr = largeResult1;
        for (int i = 0; i < 5 && curr != null; i++) {
            sample1.add(curr.val);
            curr = curr.next;
        }
        System.out.println("  First 5 nodes: " + sample1);
    }
}