public class MiddleOfLinkedList {

    // ─────────────────────────────────────────────
    // ListNode Definition
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
    // CORE SOLUTION: Fast & Slow Pointers
    // Returns the SECOND middle for even-length lists
    // ─────────────────────────────────────────────
    public static ListNode middleNode(ListNode head) {
        
        // Edge case: empty list
        if (head == null) return null;
        
        ListNode slow = head;
        ListNode fast = head;
        
        // Move slow 1 step, fast 2 steps
        // When fast reaches end → slow is at middle
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        
        return slow;
    }
    
    // ─────────────────────────────────────────────
    // VARIANT: Return FIRST middle for even-length lists
    // (useful for splitting list for merge sort)
    // ─────────────────────────────────────────────
    public static ListNode firstMiddleNode(ListNode head) {
        
        if (head == null || head.next == null) return head;
        
        ListNode slow = head;
        ListNode fast = head.next; // start fast one step ahead
        
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        
        return slow;
    }
    
    // ─────────────────────────────────────────────
    // TWO-PASS APPROACH: Count then traverse
    // (for comparison — NOT the preferred technique)
    // ─────────────────────────────────────────────
    public static ListNode middleNodeTwoPass(ListNode head) {
        
        if (head == null) return null;
        
        // Pass 1: Count nodes
        int count = 0;
        ListNode current = head;
        while (current != null) {
            count++;
            current = current.next;
        }
        
        // Pass 2: Traverse to middle
        current = head;
        for (int i = 0; i < count / 2; i++) {
            current = current.next;
        }
        
        return current;
    }
    
    // ─────────────────────────────────────────────
    // VARIANT: Return middle VALUE (not node)
    // ─────────────────────────────────────────────
    public static int middleValue(ListNode head) {
        ListNode middle = middleNode(head);
        return (middle != null) ? middle.val : -1;
    }
    
    // ─────────────────────────────────────────────
    // VARIANT: Split list into two halves at middle
    // Returns array of two heads: [firstHalfHead, secondHalfHead]
    // ─────────────────────────────────────────────
    public static ListNode[] splitAtMiddle(ListNode head) {
        
        if (head == null) return new ListNode[] {null, null};
        if (head.next == null) return new ListNode[] {head, null};
        
        // Find the first middle (so first half ends cleanly)
        ListNode slow = head;
        ListNode fast = head.next;
        
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        
        // slow is the last node of the first half
        ListNode secondHalf = slow.next;
        slow.next = null; // cut the link
        
        return new ListNode[] {head, secondHalf};
    }
    
    // ─────────────────────────────────────────────
    // VARIANT: Delete the middle node
    // ─────────────────────────────────────────────
    public static ListNode deleteMiddle(ListNode head) {
        
        // Edge cases
        if (head == null || head.next == null) return null;
        
        ListNode slow = head;
        ListNode fast = head;
        ListNode prev = null; // track node before slow
        
        while (fast != null && fast.next != null) {
            prev = slow;
            slow = slow.next;
            fast = fast.next.next;
        }
        
        // slow is the middle → prev is before middle
        // Skip the middle node
        prev.next = slow.next;
        
        return head;
    }
    
    // ─────────────────────────────────────────────
    // VARIANT: Check if linked list is a palindrome
    // (Uses middle finding as a building block)
    // ─────────────────────────────────────────────
    public static boolean isPalindrome(ListNode head) {
        
        if (head == null || head.next == null) return true;
        
        // Step 1: Find the middle (first middle for even-length)
        ListNode slow = head;
        ListNode fast = head;
        
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        
        // Step 2: Reverse the second half
        // (Reversal technique previewed here — fully covered in Project 16)
        ListNode secondHalfHead = reverseList(slow.next);
        
        // Step 3: Compare first half with reversed second half
        ListNode firstHalf = head;
        ListNode secondHalf = secondHalfHead;
        boolean isPalin = true;
        
        while (secondHalf != null) {
            if (firstHalf.val != secondHalf.val) {
                isPalin = false;
                break;
            }
            firstHalf = firstHalf.next;
            secondHalf = secondHalf.next;
        }
        
        // Step 4: Restore the list (reverse second half back)
        slow.next = reverseList(secondHalfHead);
        
        return isPalin;
    }
    
    // Helper for palindrome check (preview of Project 16)
    private static ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode current = head;
        while (current != null) {
            ListNode next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        return prev;
    }
    
    // ─────────────────────────────────────────────
    // TRACE: Step-by-step visualization
    // ─────────────────────────────────────────────
    public static void traceExecution(ListNode head) {
        
        System.out.print("  List: ");
        printList(head);
        System.out.println("  ─────────────────────────────────────────");
        
        if (head == null) {
            System.out.println("  Empty list → return null");
            System.out.println();
            return;
        }
        
        ListNode slow = head;
        ListNode fast = head;
        int step = 0;
        
        System.out.println("  Step " + step + ": slow=" + slow.val 
            + " fast=" + fast.val);
        
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            step++;
            
            System.out.println("  Step " + step + ": slow=" + slow.val 
                + " fast=" + (fast != null ? String.valueOf(fast.val) : "null"));
        }
        
        System.out.println();
        System.out.println("  Fast reached end → slow is at MIDDLE");
        System.out.println("  ★ Middle node value: " + slow.val);
        System.out.println();
    }
    
    // ─────────────────────────────────────────────
    // VISUAL: Show pointer positions at each step
    // ─────────────────────────────────────────────
    public static void visualTrace(ListNode head) {
        
        // Collect values for display
        java.util.List<Integer> values = new java.util.ArrayList<>();
        ListNode current = head;
        while (current != null) {
            values.add(current.val);
            current = current.next;
        }
        
        int n = values.size();
        if (n == 0) {
            System.out.println("  Empty list");
            return;
        }
        
        System.out.print("  List: ");
        for (int i = 0; i < n; i++) {
            System.out.print(values.get(i));
            if (i < n - 1) System.out.print(" → ");
        }
        System.out.println("  (length=" + n + ")");
        System.out.println("  ─────────────────────────────────────────");
        
        int slowIdx = 0;
        int fastIdx = 0;
        int step = 0;
        
        // Print initial state
        printPointerPositions(values, slowIdx, fastIdx, step);
        
        // Simulate fast/slow movement
        while (fastIdx < n && (fastIdx + 1) < n) {
            slowIdx += 1;
            fastIdx += 2;
            step++;
            printPointerPositions(values, slowIdx, 
                fastIdx < n ? fastIdx : -1, step);
        }
        
        System.out.println("  ★ Middle: node " + slowIdx 
            + " (value=" + values.get(slowIdx) + ")");
        System.out.println();
    }
    
    private static void printPointerPositions(
            java.util.List<Integer> values, int slowIdx, int fastIdx, int step) {
        
        System.out.println("  Step " + step + ":");
        
        // Print nodes
        System.out.print("    ");
        for (int i = 0; i < values.size(); i++) {
            System.out.printf("[%d]", values.get(i));
            if (i < values.size() - 1) System.out.print("→");
        }
        if (fastIdx == -1 || fastIdx >= values.size()) {
            System.out.print("→null");
        }
        System.out.println();
        
        // Print pointer markers
        System.out.print("    ");
        for (int i = 0; i < values.size(); i++) {
            String label = "";
            int width = String.valueOf(values.get(i)).length() + 2; // [X] width
            
            if (i == slowIdx && i == fastIdx) {
                label = "S/F";
            } else if (i == slowIdx) {
                label = " S ";
            } else if (i == fastIdx) {
                label = " F ";
            }
            
            // Center the label under the node
            System.out.print(label);
            for (int p = label.length(); p < width; p++) {
                System.out.print(" ");
            }
            if (i < values.size() - 1) {
                System.out.print(" "); // arrow space
            }
        }
        
        if (fastIdx >= values.size()) {
            System.out.print("  F ");
        }
        System.out.println();
        System.out.println();
    }
    
    // ─────────────────────────────────────────────
    // HELPERS: Build and print linked lists
    // ─────────────────────────────────────────────
    public static ListNode buildList(int[] values) {
        if (values == null || values.length == 0) return null;
        
        ListNode head = new ListNode(values[0]);
        ListNode current = head;
        for (int i = 1; i < values.length; i++) {
            current.next = new ListNode(values[i]);
            current = current.next;
        }
        return head;
    }
    
    public static void printList(ListNode head) {
        ListNode current = head;
        while (current != null) {
            System.out.print(current.val);
            if (current.next != null) System.out.print(" → ");
            current = current.next;
        }
        System.out.println();
    }
    
    public static int listLength(ListNode head) {
        int count = 0;
        while (head != null) {
            count++;
            head = head.next;
        }
        return count;
    }
    
    // ─────────────────────────────────────────────
    // MAIN
    // ─────────────────────────────────────────────
    public static void main(String[] args) {
        
        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 15: Middle of a Linked List              ║");
        System.out.println("║  Pattern: Fast & Slow Pointers → Midpoint Finding ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");
        System.out.println();
        
        // ── TEST 1: Odd length with visual trace ──
        System.out.println("═══ TEST 1: Odd Length (5 nodes) — Visual ═══");
        visualTrace(buildList(new int[] {1, 2, 3, 4, 5}));
        
        // ── TEST 2: Even length with visual trace ──
        System.out.println("═══ TEST 2: Even Length (6 nodes) — Visual ═══");
        visualTrace(buildList(new int[] {1, 2, 3, 4, 5, 6}));
        
        // ── TEST 3: Small lists ──
        System.out.println("═══ TEST 3: Small Lists ═══");
        
        // Single node
        System.out.println("  Single node [1]:");
        traceExecution(buildList(new int[] {1}));
        
        // Two nodes
        System.out.println("  Two nodes [1,2]:");
        traceExecution(buildList(new int[] {1, 2}));
        
        // Three nodes
        System.out.println("  Three nodes [1,2,3]:");
        traceExecution(buildList(new int[] {1, 2, 3}));
        
        // ── TEST 4: First middle vs second middle ──
        System.out.println("═══ TEST 4: First Middle vs Second Middle ═══");
        int[][] evenTests = {
            {1, 2},
            {1, 2, 3, 4},
            {1, 2, 3, 4, 5, 6},
            {1, 2, 3, 4, 5, 6, 7, 8}
        };
        
        for (int[] vals : evenTests) {
            ListNode list = buildList(vals);
            ListNode secondMiddle = middleNode(list);
            
            list = buildList(vals); // rebuild since we might modify
            ListNode firstMid = firstMiddleNode(list);
            
            System.out.print("  List length=" + vals.length);
            System.out.print(" → 1st middle=" + firstMid.val);
            System.out.println(" → 2nd middle=" + secondMiddle.val);
        }
        System.out.println();
        
        // ── TEST 5: Correctness vs two-pass ──
        System.out.println("═══ TEST 5: Fast/Slow vs Two-Pass Correctness ═══");
        boolean allPassed = true;
        for (int len = 1; len <= 20; len++) {
            int[] vals = new int[len];
            for (int i = 0; i < len; i++) vals[i] = i + 1;
            
            ListNode list1 = buildList(vals);
            ListNode list2 = buildList(vals);
            
            int fastSlowResult = middleNode(list1).val;
            int twoPassResult = middleNodeTwoPass(list2).val;
            
            boolean pass = fastSlowResult == twoPassResult;
            if (!pass) {
                System.out.println("  FAIL at length " + len 
                    + ": fast/slow=" + fastSlowResult 
                    + " two-pass=" + twoPassResult);
                allPassed = false;
            }
        }
        System.out.println("  Lengths 1-20: " 
            + (allPassed ? "ALL ✓ PASSED" : "SOME ✗ FAILED"));
        System.out.println();
        
        // ── TEST 6: Split at middle ──
        System.out.println("═══ TEST 6: Split at Middle ═══");
        int[][] splitTests = {
            {1, 2, 3, 4, 5},
            {1, 2, 3, 4, 5, 6},
            {1, 2, 3},
            {1, 2}
        };
        
        for (int[] vals : splitTests) {
            ListNode list = buildList(vals);
            System.out.print("  Original: ");
            printList(list);
            
            list = buildList(vals); // rebuild
            ListNode[] halves = splitAtMiddle(list);
            
            System.out.print("  First half:  ");
            printList(halves[0]);
            System.out.print("  Second half: ");
            printList(halves[1]);
            System.out.println();
        }
        
        // ── TEST 7: Delete middle node ──
        System.out.println("═══ TEST 7: Delete Middle Node ═══");
        int[][] deleteTests = {
            {1, 2, 3, 4, 5},
            {1, 2, 3, 4},
            {1, 2, 3},
            {1, 2}
        };
        
        for (int[] vals : deleteTests) {
            ListNode original = buildList(vals);
            System.out.print("  Before: ");
            printList(original);
            
            ListNode modified = deleteMiddle(buildList(vals));
            System.out.print("  After:  ");
            printList(modified);
            System.out.println();
        }
        
        // ── TEST 8: Palindrome check (uses middle as building block) ──
        System.out.println("═══ TEST 8: Palindrome Check (Middle as Building Block) ═══");
        int[][] palindromeTests = {
            {1, 2, 3, 2, 1},       // true (odd)
            {1, 2, 2, 1},           // true (even)
            {1, 2, 3, 4, 5},       // false
            {1},                     // true (single)
            {1, 1},                  // true
            {1, 2},                  // false
            {1, 2, 3, 3, 2, 1},    // true (even)
            {1, 2, 3, 4, 3, 2, 1}, // true (odd)
        };
        
        for (int[] vals : palindromeTests) {
            ListNode list = buildList(vals);
            boolean result = isPalindrome(list);
            System.out.print("  ");
            printList(buildList(vals));
            System.out.println("  → Palindrome: " + result);
        }
        System.out.println();
        
        // ── TEST 9: Large list performance ──
        System.out.println("═══ TEST 9: Performance Benchmark ═══");
        int size = 10_000_000;
        
        // Build large list
        ListNode largeHead = new ListNode(0);
        ListNode curr = largeHead;
        for (int i = 1; i < size; i++) {
            curr.next = new ListNode(i);
            curr = curr.next;
        }
        
        // Fast/slow
        long start = System.nanoTime();
        ListNode mid1 = middleNode(largeHead);
        long fastSlowTime = System.nanoTime() - start;
        
        // Two-pass
        start = System.nanoTime();
        ListNode mid2 = middleNodeTwoPass(largeHead);
        long twoPassTime = System.nanoTime() - start;
        
        System.out.println("  List size: " + size + " nodes");
        System.out.println("  Fast/Slow: middle=" + mid1.val 
            + " time=" + (fastSlowTime / 1_000_000) + "ms");
        System.out.println("  Two-Pass:  middle=" + mid2.val 
            + " time=" + (twoPassTime / 1_000_000) + "ms");
        System.out.println("  Results match: " + (mid1.val == mid2.val));
        System.out.println();
        
        // ── TEST 10: Summary of all middle positions ──
        System.out.println("═══ TEST 10: Middle Position Summary ═══");
        System.out.println("  ┌────────┬────────────────────────┬────────┐");
        System.out.println("  │ Length │ List                   │ Middle │");
        System.out.println("  ├────────┼────────────────────────┼────────┤");
        for (int len = 1; len <= 8; len++) {
            int[] vals = new int[len];
            for (int i = 0; i < len; i++) vals[i] = i + 1;
            
            ListNode list = buildList(vals);
            int midVal = middleNode(list).val;
            
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < len; i++) {
                sb.append(vals[i]);
                if (i < len - 1) sb.append("→");
            }
            
            System.out.printf("  │   %d    │ %-22s │   %d    │%n", 
                len, sb.toString(), midVal);
        }
        System.out.println("  └────────┴────────────────────────┴────────┘");
        System.out.println();
    }
}