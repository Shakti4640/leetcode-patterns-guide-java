import java.util.HashSet;
import java.util.Set;

public class LinkedListCycleStart {

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

        @Override
        public String toString() {
            return "Node(" + val + ")";
        }
    }

    // ═══════════════════════════════════════════════
    // SOLUTION 1: TWO-PHASE — Floyd's Cycle Start Detection
    // O(n) time, O(1) space — OPTIMAL
    // ═══════════════════════════════════════════════
    public static ListNode detectCycleStart(ListNode head) {

        // Edge case: empty or single node without cycle
        if (head == null || head.next == null) {
            return null;
        }

        // ═══ PHASE 1: Detect cycle and find meeting point ═══
        ListNode slow = head;
        ListNode fast = head;

        boolean hasCycle = false;

        while (fast != null && fast.next != null) {
            slow = slow.next;           // 1 step
            fast = fast.next.next;      // 2 steps

            if (slow == fast) {
                hasCycle = true;
                break;
            }
        }

        // No cycle found
        if (!hasCycle) {
            return null;
        }

        // ═══ PHASE 2: Find cycle entry point ═══
        // pointer1 from HEAD, pointer2 from MEETING POINT
        // Both move at speed 1 → meet at cycle start
        ListNode pointer1 = head;
        ListNode pointer2 = slow;   // slow is at meeting point

        while (pointer1 != pointer2) {
            pointer1 = pointer1.next;
            pointer2 = pointer2.next;
        }

        return pointer1;   // = pointer2 = cycle start
    }

    // ═══════════════════════════════════════════════
    // SOLUTION 2: HASHSET APPROACH — For comparison
    // O(n) time, O(n) space
    // ═══════════════════════════════════════════════
    public static ListNode detectCycleStartHashSet(ListNode head) {

        Set<ListNode> visited = new HashSet<>();
        ListNode current = head;

        while (current != null) {
            if (visited.contains(current)) {
                return current;     // first revisited node = cycle start
            }
            visited.add(current);
            current = current.next;
        }

        return null;    // no cycle
    }

    // ═══════════════════════════════════════════════
    // UTILITY: Get cycle length (useful for understanding)
    // Must be called AFTER meeting point is found
    // ═══════════════════════════════════════════════
    public static int getCycleLength(ListNode meetingPoint) {

        if (meetingPoint == null) return 0;

        int length = 1;
        ListNode current = meetingPoint.next;

        while (current != meetingPoint) {
            length++;
            current = current.next;
        }

        return length;
    }

    // ═══════════════════════════════════════════════
    // TRACE VERSION — Full visualization
    // ═══════════════════════════════════════════════
    public static ListNode detectCycleStartWithTrace(ListNode head) {

        System.out.println("  ═══ PHASE 1: Find Meeting Point ═══");

        if (head == null || head.next == null) {
            System.out.println("  List too short for cycle → return null");
            return null;
        }

        ListNode slow = head;
        ListNode fast = head;
        int step = 0;
        boolean hasCycle = false;

        System.out.println("  Step " + step + ": slow=" + slow
                + " fast=" + fast);

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            step++;

            System.out.println("  Step " + step + ": slow=" + slow
                    + " fast=" + (fast != null ? fast.toString() : "null"));

            if (slow == fast) {
                hasCycle = true;
                System.out.println("  ★ MEETING POINT FOUND at " + slow
                        + " after " + step + " steps");
                break;
            }
        }

        if (!hasCycle) {
            System.out.println("  No cycle detected → return null");
            return null;
        }

        // Get cycle info for display
        int cycleLen = getCycleLength(slow);
        System.out.println("  Cycle length: " + cycleLen);

        System.out.println();
        System.out.println("  ═══ PHASE 2: Find Cycle Start ═══");

        ListNode p1 = head;
        ListNode p2 = slow;

        System.out.println("  pointer1 starts at HEAD: " + p1);
        System.out.println("  pointer2 starts at MEETING: " + p2);

        step = 0;
        while (p1 != p2) {
            p1 = p1.next;
            p2 = p2.next;
            step++;
            System.out.println("  Step " + step + ": p1=" + p1
                    + " p2=" + p2
                    + (p1 == p2 ? " ← MATCH!" : ""));
        }

        if (step == 0) {
            System.out.println("  Already at same node → cycle starts at HEAD");
        }

        System.out.println("  ★ CYCLE START: " + p1
                + " (found after " + step + " Phase 2 steps)");
        System.out.println("  F (tail length) = " + step);

        return p1;
    }

    // ─────────────────────────────────────────────
    // LIST BUILDERS
    // ─────────────────────────────────────────────

    // Build: 3 → 2 → 0 → -4 → (back to 2)
    static ListNode buildCycleList1() {
        ListNode n0 = new ListNode(3);
        ListNode n1 = new ListNode(2);
        ListNode n2 = new ListNode(0);
        ListNode n3 = new ListNode(-4);

        n0.next = n1;
        n1.next = n2;
        n2.next = n3;
        n3.next = n1;   // cycle back to node(2)

        return n0;
    }

    // Build: 1 → 2 → (back to 1)
    static ListNode buildCycleList2() {
        ListNode n0 = new ListNode(1);
        ListNode n1 = new ListNode(2);

        n0.next = n1;
        n1.next = n0;   // cycle back to node(1)

        return n0;
    }

    // Build: 1 → 2 → 3 → 4 → null (no cycle)
    static ListNode buildNoCycleList() {
        ListNode n0 = new ListNode(1);
        ListNode n1 = new ListNode(2);
        ListNode n2 = new ListNode(3);
        ListNode n3 = new ListNode(4);

        n0.next = n1;
        n1.next = n2;
        n2.next = n3;

        return n0;
    }

    // Build: 1 → (self-cycle)
    static ListNode buildSelfCycle() {
        ListNode n0 = new ListNode(1);
        n0.next = n0;
        return n0;
    }

    // Build: 1 → 2 → 3 → 4 → 5 → 6 → 7 → (back to 3)
    // Long tail, cycle in middle
    static ListNode buildLongTailCycle() {
        ListNode n0 = new ListNode(1);
        ListNode n1 = new ListNode(2);
        ListNode n2 = new ListNode(3);
        ListNode n3 = new ListNode(4);
        ListNode n4 = new ListNode(5);
        ListNode n5 = new ListNode(6);
        ListNode n6 = new ListNode(7);

        n0.next = n1;
        n1.next = n2;
        n2.next = n3;
        n3.next = n4;
        n4.next = n5;
        n5.next = n6;
        n6.next = n2;   // cycle back to node(3)

        return n0;
    }

    // Build: 1 → 2 → 3 → (back to 1) — cycle starts at head
    static ListNode buildCycleAtHead() {
        ListNode n0 = new ListNode(1);
        ListNode n1 = new ListNode(2);
        ListNode n2 = new ListNode(3);

        n0.next = n1;
        n1.next = n2;
        n2.next = n0;   // cycle back to head

        return n0;
    }

    // ─────────────────────────────────────────────
    // LIST PRINTER (handles cycles safely)
    // ─────────────────────────────────────────────
    static void printList(ListNode head, ListNode cycleStart) {
        ListNode current = head;
        int count = 0;
        int maxPrint = 15;
        StringBuilder sb = new StringBuilder("  ");

        while (current != null && count < maxPrint) {
            sb.append(current.val);
            if (current.next != null) {
                if (current.next == cycleStart && count > 0) {
                    sb.append(" → [back to ").append(cycleStart.val).append("]");
                    break;
                }
                sb.append(" → ");
            }
            current = current.next;
            count++;
        }

        if (current == null) {
            sb.append(" → null");
        } else if (count >= maxPrint) {
            sb.append(" → ...");
        }

        System.out.println(sb.toString());
    }

    // ─────────────────────────────────────────────
    // MAIN — Run all tests
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 20: Find Start Node of Linked List Cycle    ║");
        System.out.println("║  Pattern: Fast & Slow → Two-Phase Detection          ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Classic cycle ──
        System.out.println("═══ TEST 1: Classic Cycle — 3 → 2 → 0 → -4 → [back to 2] ═══");
        ListNode list1 = buildCycleList1();
        printList(list1, list1.next);
        System.out.println();
        ListNode result1 = detectCycleStartWithTrace(list1);
        System.out.println("\n  Final: Cycle starts at " + result1);
        System.out.println();

        // ── TEST 2: Two-node cycle ──
        System.out.println("═══ TEST 2: Two-Node Cycle — 1 → 2 → [back to 1] ═══");
        ListNode list2 = buildCycleList2();
        printList(list2, list2);
        System.out.println();
        ListNode result2 = detectCycleStartWithTrace(list2);
        System.out.println("\n  Final: Cycle starts at " + result2);
        System.out.println();

        // ── TEST 3: No cycle ──
        System.out.println("═══ TEST 3: No Cycle — 1 → 2 → 3 → 4 → null ═══");
        ListNode list3 = buildNoCycleList();
        printList(list3, null);
        System.out.println();
        ListNode result3 = detectCycleStartWithTrace(list3);
        System.out.println("\n  Final: " + (result3 == null ? "null (no cycle)" : result3));
        System.out.println();

        // ── TEST 4: Self-cycle ──
        System.out.println("═══ TEST 4: Self-Cycle — 1 → [back to 1] ═══");
        ListNode list4 = buildSelfCycle();
        printList(list4, list4);
        System.out.println();
        ListNode result4 = detectCycleStartWithTrace(list4);
        System.out.println("\n  Final: Cycle starts at " + result4);
        System.out.println();

        // ── TEST 5: Long tail cycle ──
        System.out.println("═══ TEST 5: Long Tail — 1→2→3→4→5→6→7→[back to 3] ═══");
        ListNode list5 = buildLongTailCycle();
        printList(list5, list5.next.next);
        System.out.println();
        ListNode result5 = detectCycleStartWithTrace(list5);
        System.out.println("\n  Final: Cycle starts at " + result5);
        System.out.println();

        // ── TEST 6: Cycle at head ──
        System.out.println("═══ TEST 6: Cycle at Head — 1→2→3→[back to 1] ═══");
        ListNode list6 = buildCycleAtHead();
        printList(list6, list6);
        System.out.println();
        ListNode result6 = detectCycleStartWithTrace(list6);
        System.out.println("\n  Final: Cycle starts at " + result6);
        System.out.println();

        // ── TEST 7: Empty list ──
        System.out.println("═══ TEST 7: Empty List ═══");
        ListNode result7 = detectCycleStart(null);
        System.out.println("  Input: null");
        System.out.println("  Result: " + (result7 == null ? "null (no cycle)" : result7));
        System.out.println();

        // ── TEST 8: Single node, no cycle ──
        System.out.println("═══ TEST 8: Single Node, No Cycle ═══");
        ListNode single = new ListNode(42);
        ListNode result8 = detectCycleStart(single);
        System.out.println("  Input: 42 → null");
        System.out.println("  Result: " + (result8 == null ? "null (no cycle)" : result8));
        System.out.println();

        // ── TEST 9: Verify both approaches agree ──
        System.out.println("═══ TEST 9: Floyd vs HashSet — Verification ═══");
        ListNode[][] testLists = {
            { buildCycleList1() },
            { buildCycleList2() },
            { buildNoCycleList() },
            { buildSelfCycle() },
            { buildLongTailCycle() },
            { buildCycleAtHead() }
        };
        String[] testNames = {
            "Classic cycle",
            "Two-node cycle",
            "No cycle",
            "Self-cycle",
            "Long tail",
            "Cycle at head"
        };

        boolean allMatch = true;
        for (int i = 0; i < testLists.length; i++) {
            // Need fresh lists for each approach since they're the same objects
            // For this verification, we use the same list (non-destructive algorithms)
            ListNode floyd = detectCycleStart(testLists[i][0]);
            ListNode hashSet = detectCycleStartHashSet(testLists[i][0]);
            boolean match = (floyd == hashSet);
            System.out.println("  " + testNames[i] + ": Floyd="
                    + (floyd != null ? floyd.val : "null")
                    + " HashSet="
                    + (hashSet != null ? hashSet.val : "null")
                    + (match ? " ✓" : " ✗ MISMATCH"));
            if (!match) allMatch = false;
        }
        System.out.println("  All match: " + allMatch);
        System.out.println();

        // ── TEST 10: Performance test ──
        System.out.println("═══ TEST 10: Performance — 1,000,000 Node Cycle ═══");
        int size = 1_000_000;
        ListNode perfHead = new ListNode(0);
        ListNode current = perfHead;
        ListNode cycleEntry = null;
        int cycleStartIdx = size / 2;

        for (int i = 1; i < size; i++) {
            current.next = new ListNode(i);
            current = current.next;
            if (i == cycleStartIdx) {
                cycleEntry = current;
            }
        }
        current.next = cycleEntry;  // create cycle at middle

        // Floyd's approach
        long start = System.nanoTime();
        ListNode perfResult1 = detectCycleStart(perfHead);
        long floydTime = System.nanoTime() - start;

        // HashSet approach
        start = System.nanoTime();
        ListNode perfResult2 = detectCycleStartHashSet(perfHead);
        long hashTime = System.nanoTime() - start;

        System.out.println("  List size: " + size
                + " | Cycle starts at node " + cycleStartIdx);
        System.out.println("  Floyd's:  Found node "
                + (perfResult1 != null ? perfResult1.val : "null")
                + " in " + (floydTime / 1_000_000) + " ms");
        System.out.println("  HashSet:  Found node "
                + (perfResult2 != null ? perfResult2.val : "null")
                + " in " + (hashTime / 1_000_000) + " ms");
        System.out.println("  Both correct: " + (perfResult1 == perfResult2));
        System.out.println("  Floyd uses O(1) space, HashSet uses O(n) space");
    }
}