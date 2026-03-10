public class DetectCycleLinkedList {

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

    // ─────────────────────────────────────────────
    // CORE SOLUTION: Floyd's Cycle Detection (Tortoise & Hare)
    // ─────────────────────────────────────────────
    public static boolean hasCycle(ListNode head) {

        // Edge case: empty list
        if (head == null) return false;

        // Initialize both pointers at head
        ListNode slow = head;
        ListNode fast = head;

        // Fast moves 2 steps, slow moves 1 step
        while (fast != null && fast.next != null) {

            // Move pointers FIRST
            slow = slow.next;           // 1 step
            fast = fast.next.next;      // 2 steps

            // THEN compare
            if (slow == fast) {
                return true;            // cycle detected — they met
            }
        }

        // Fast reached null → no cycle
        return false;
    }

    // ─────────────────────────────────────────────
    // BRUTE FORCE: HashSet approach
    // ─────────────────────────────────────────────
    public static boolean hasCycleHashSet(ListNode head) {

        java.util.HashSet<ListNode> visited = new java.util.HashSet<>();
        ListNode current = head;

        while (current != null) {
            if (visited.contains(current)) {
                return true;    // visited before → cycle
            }
            visited.add(current);
            current = current.next;
        }

        return false;   // reached null → no cycle
    }

    // ─────────────────────────────────────────────
    // EXTENSION 1: Find the cycle LENGTH
    // ─────────────────────────────────────────────
    public static int cycleLength(ListNode head) {

        // Step 1: Detect meeting point using fast/slow
        if (head == null) return 0;

        ListNode slow = head;
        ListNode fast = head;
        boolean hasCycle = false;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                hasCycle = true;
                break;
            }
        }

        if (!hasCycle) return 0;

        // Step 2: From meeting point, count nodes until we return
        int length = 1;
        ListNode current = slow.next;
        while (current != slow) {
            length++;
            current = current.next;
        }

        return length;
    }

    // ─────────────────────────────────────────────
    // EXTENSION 2: Find the MIDDLE node (fast/slow variant)
    // (Preview of Project 15 — shows versatility of pattern)
    // ─────────────────────────────────────────────
    public static ListNode findMiddle(ListNode head) {

        if (head == null) return null;

        ListNode slow = head;
        ListNode fast = head;

        // When fast reaches the end, slow is at the middle
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;    // slow is at the middle
    }

    // ─────────────────────────────────────────────
    // EXTENSION 3: Detect cycle in a NUMBER SEQUENCE
    // (Shows pattern works beyond linked lists)
    // ─────────────────────────────────────────────
    public static boolean isHappyNumber(int n) {

        // A happy number eventually reaches 1
        // An unhappy number enters an infinite cycle
        // Use fast/slow to detect the cycle!

        int slow = n;
        int fast = n;

        do {
            slow = sumOfSquaredDigits(slow);           // 1 step
            fast = sumOfSquaredDigits(
                       sumOfSquaredDigits(fast));      // 2 steps
        } while (slow != fast);

        // If they met at 1 → happy number
        // If they met elsewhere → cycle → unhappy
        return slow == 1;
    }

    private static int sumOfSquaredDigits(int n) {
        int sum = 0;
        while (n > 0) {
            int digit = n % 10;
            sum += digit * digit;
            n /= 10;
        }
        return sum;
    }

    // ─────────────────────────────────────────────
    // TRACE FUNCTION — See the tortoise and hare race
    // ─────────────────────────────────────────────
    public static void hasCycleWithTrace(ListNode head, String listName) {

        System.out.println("List: " + listName);
        System.out.println("─────────────────────────────────────────");

        if (head == null) {
            System.out.println("Empty list → no cycle");
            System.out.println();
            return;
        }

        ListNode slow = head;
        ListNode fast = head;
        int step = 0;

        System.out.println("Step " + step + ": slow=Node(" + slow.val
            + ") fast=Node(" + fast.val + ")");

        while (fast != null && fast.next != null) {

            slow = slow.next;
            fast = fast.next.next;
            step++;

            String fastStr = (fast == null) ? "null" : "Node(" + fast.val + ")";
            System.out.println("Step " + step + ": slow=Node(" + slow.val
                + ") fast=" + fastStr);

            if (slow == fast) {
                System.out.println("  → slow == fast → CYCLE DETECTED ✓");

                // Also find cycle length
                int length = 1;
                ListNode current = slow.next;
                while (current != slow) {
                    length++;
                    current = current.next;
                }
                System.out.println("  → Cycle length: " + length);
                System.out.println();
                return;
            }
        }

        System.out.println("  → fast reached null → NO CYCLE ✗");
        System.out.println();
    }

    // ─────────────────────────────────────────────
    // HELPER: Build a linked list from array (no cycle)
    // ─────────────────────────────────────────────
    private static ListNode buildList(int[] values) {
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
    // HELPER: Build a linked list with a cycle
    // cycleIndex = which node the last node points back to
    // -1 = no cycle
    // ─────────────────────────────────────────────
    private static ListNode buildListWithCycle(int[] values, int cycleIndex) {
        if (values.length == 0) return null;

        ListNode head = new ListNode(values[0]);
        ListNode current = head;
        ListNode cycleTarget = (cycleIndex == 0) ? head : null;

        for (int i = 1; i < values.length; i++) {
            current.next = new ListNode(values[i]);
            current = current.next;
            if (i == cycleIndex) {
                cycleTarget = current;
            }
        }

        // Create cycle: last node points back to cycleTarget
        if (cycleIndex >= 0 && cycleTarget != null) {
            current.next = cycleTarget;
        }

        return head;
    }

    // ─────────────────────────────────────────────
    // HELPER: Print list (with cycle protection)
    // ─────────────────────────────────────────────
    private static String listToString(ListNode head, int maxNodes) {
        StringBuilder sb = new StringBuilder();
        ListNode current = head;
        int count = 0;

        while (current != null && count < maxNodes) {
            sb.append(current.val);
            if (current.next != null && count < maxNodes - 1) {
                sb.append(" → ");
            }
            current = current.next;
            count++;
        }

        if (current != null) {
            sb.append(" → ... (cycle)");
        } else {
            sb.append(" → null");
        }

        return sb.toString();
    }

    // ─────────────────────────────────────────────
    // MAIN — Run all examples
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 7: Detect Cycle in a Linked List        ║");
        System.out.println("║  Pattern: Fast & Slow Pointers (Tortoise & Hare) ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Cycle at node index 1 ──
        System.out.println("═══ TEST 1: Cycle (last → node index 1) ═══");
        ListNode list1 = buildListWithCycle(new int[]{3, 2, 0, -4}, 1);
        System.out.println("Structure: 3 → 2 → 0 → -4 → (back to 2)");
        hasCycleWithTrace(list1, "3 → 2 → 0 → -4 → [2]");

        // ── TEST 2: Cycle at node index 0 (head) ──
        System.out.println("═══ TEST 2: Cycle (last → head) ═══");
        ListNode list2 = buildListWithCycle(new int[]{1, 2}, 0);
        System.out.println("Structure: 1 → 2 → (back to 1)");
        hasCycleWithTrace(list2, "1 → 2 → [1]");

        // ── TEST 3: No cycle ──
        System.out.println("═══ TEST 3: No Cycle ═══");
        ListNode list3 = buildList(new int[]{1, 2, 3, 4, 5});
        System.out.println("Structure: " + listToString(list3, 10));
        hasCycleWithTrace(list3, "1 → 2 → 3 → 4 → 5 → null");

        // ── TEST 4: Single node, no cycle ──
        System.out.println("═══ TEST 4: Single Node, No Cycle ═══");
        ListNode list4 = buildList(new int[]{1});
        hasCycleWithTrace(list4, "1 → null");

        // ── TEST 5: Single node, self-loop ──
        System.out.println("═══ TEST 5: Single Node, Self-Loop ═══");
        ListNode list5 = new ListNode(1);
        list5.next = list5;  // self-loop
        System.out.println("Structure: 1 → (back to 1)");
        hasCycleWithTrace(list5, "1 → [1]");

        // ── TEST 6: Empty list ──
        System.out.println("═══ TEST 6: Empty List ═══");
        hasCycleWithTrace(null, "null");

        // ── TEST 7: Long tail, short cycle ──
        System.out.println("═══ TEST 7: Long Tail, Short Cycle ═══");
        ListNode list7 = buildListWithCycle(
            new int[]{1, 2, 3, 4, 5, 6, 7, 8}, 5);
        System.out.println("Structure: 1→2→3→4→5→6→7→8→(back to 6)");
        hasCycleWithTrace(list7, "1→2→3→4→5→6→7→8→[6]");

        // ── TEST 8: Cycle length measurement ──
        System.out.println("═══ TEST 8: Cycle Length Measurement ═══");
        int[][] cycleCases = {
            {3, 2, 0, -4},       // cycle from index 1 → length 3
            {1, 2, 3, 4, 5, 6},  // cycle from index 2 → length 4
            {1, 2},              // cycle from index 0 → length 2
        };
        int[] cycleIndices = {1, 2, 0};

        for (int t = 0; t < cycleCases.length; t++) {
            ListNode testList = buildListWithCycle(cycleCases[t], cycleIndices[t]);
            int length = cycleLength(testList);
            System.out.println("  List with cycle at index " + cycleIndices[t]
                + " → cycle length = " + length);
        }
        System.out.println();

        // ── TEST 9: Happy number detection (pattern beyond linked lists) ──
        System.out.println("═══ TEST 9: Happy Number Detection (Pattern Reuse) ═══");
        int[] happyTests = {19, 7, 2, 4, 23, 28, 100, 89};
        for (int num : happyTests) {
            boolean happy = isHappyNumber(num);
            System.out.println("  " + num + " → "
                + (happy ? "HAPPY ☺" : "NOT HAPPY ☹"));
        }
        System.out.println();

        // ── TEST 10: Find middle node ──
        System.out.println("═══ TEST 10: Find Middle Node (Pattern Variant) ═══");
        int[][] middleTests = {
            {1, 2, 3, 4, 5},       // odd length → middle = 3
            {1, 2, 3, 4, 5, 6},    // even length → middle = 4 (second middle)
            {1},                    // single → middle = 1
            {1, 2}                  // two → middle = 2
        };
        for (int[] test : middleTests) {
            ListNode testList = buildList(test);
            ListNode middle = findMiddle(testList);
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < test.length; i++) {
                sb.append(test[i]);
                if (i < test.length - 1) sb.append(", ");
            }
            sb.append("]");
            System.out.println("  " + sb.toString()
                + " → middle = " + (middle != null ? middle.val : "null"));
        }
        System.out.println();

        // ── TEST 11: Correctness — Fast/Slow vs HashSet ──
        System.out.println("═══ TEST 11: Correctness — Fast/Slow vs HashSet ═══");
        boolean allPassed = true;

        // Test with cycles
        for (int size = 1; size <= 20; size++) {
            int[] values = new int[size];
            for (int i = 0; i < size; i++) values[i] = i;

            // Test with cycle at every possible position
            for (int ci = 0; ci < size; ci++) {
                ListNode testList1 = buildListWithCycle(values, ci);
                ListNode testList2 = buildListWithCycle(values, ci);
                boolean r1 = hasCycle(testList1);
                boolean r2 = hasCycleHashSet(testList2);
                if (r1 != r2) {
                    System.out.println("  MISMATCH at size=" + size
                        + " cycleIdx=" + ci);
                    allPassed = false;
                }
            }

            // Test without cycle
            ListNode noCycleList1 = buildList(values);
            ListNode noCycleList2 = buildList(values);
            boolean r1 = hasCycle(noCycleList1);
            boolean r2 = hasCycleHashSet(noCycleList2);
            if (r1 != r2) {
                System.out.println("  MISMATCH at size=" + size + " no cycle");
                allPassed = false;
            }
        }
        System.out.println("  Tested sizes 1-20, all cycle positions + no-cycle");
        System.out.println("  Overall: "
            + (allPassed ? "ALL TESTS PASSED ✓" : "SOME FAILED ✗"));
        System.out.println();

        // ── TEST 12: Performance comparison ──
        System.out.println("═══ TEST 12: Performance Comparison ═══");
        int perfSize = 2000000;
        int[] perfValues = new int[perfSize];
        for (int i = 0; i < perfSize; i++) perfValues[i] = i;

        // Build list with cycle at the middle
        ListNode perfList1 = buildListWithCycle(perfValues, perfSize / 2);
        long startTime = System.nanoTime();
        boolean r1Result = hasCycle(perfList1);
        long fastSlowTime = System.nanoTime() - startTime;

        ListNode perfList2 = buildListWithCycle(perfValues, perfSize / 2);
        startTime = System.nanoTime();
        boolean r2Result = hasCycleHashSet(perfList2);
        long hashSetTime = System.nanoTime() - startTime;

        System.out.println("List size: " + perfSize + " (cycle at middle)");
        System.out.println("Fast/Slow: " + r1Result + " → "
            + (fastSlowTime / 1_000_000) + " ms");
        System.out.println("HashSet:   " + r2Result + " → "
            + (hashSetTime / 1_000_000) + " ms");
        System.out.println("Note: Fast/Slow uses O(1) space, HashSet uses O(N) space");
        System.out.println();
    }
}