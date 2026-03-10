public class ReverseLinkedList {

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
    // CORE SOLUTION: Iterative — Prev/Curr/Next Dance
    // ─────────────────────────────────────────────
    public static ListNode reverseList(ListNode head) {
        
        ListNode prev = null;
        ListNode curr = head;
        
        while (curr != null) {
            ListNode next = curr.next;   // 1. SAVE next
            curr.next = prev;            // 2. REDIRECT current pointer backward
            prev = curr;                 // 3. ADVANCE prev
            curr = next;                 // 4. ADVANCE curr
        }
        
        return prev; // prev is the new head
    }
    
    // ─────────────────────────────────────────────
    // RECURSIVE SOLUTION
    // ─────────────────────────────────────────────
    public static ListNode reverseListRecursive(ListNode head) {
        
        // Base case: empty list or single node
        if (head == null || head.next == null) {
            return head;
        }
        
        // Recursively reverse the rest of the list
        ListNode newHead = reverseListRecursive(head.next);
        
        // Make the next node point back to current node
        head.next.next = head;
        
        // Break the original forward link (prevent cycle)
        head.next = null;
        
        // Return the new head (propagated from the base case)
        return newHead;
    }
    
    // ─────────────────────────────────────────────
    // VARIANT: Reverse first N nodes only
    // ─────────────────────────────────────────────
    public static ListNode reverseFirstN(ListNode head, int n) {
        
        if (head == null || n <= 1) return head;
        
        ListNode prev = null;
        ListNode curr = head;
        ListNode originalHead = head; // will become tail of reversed portion
        
        for (int i = 0; i < n && curr != null; i++) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        
        // Connect the tail of reversed portion to the remaining list
        originalHead.next = curr;
        
        return prev; // new head
    }
    
    // ─────────────────────────────────────────────
    // VARIANT: Reverse in groups of K
    // ─────────────────────────────────────────────
    public static ListNode reverseInGroupsOfK(ListNode head, int k) {
        
        if (head == null || k <= 1) return head;
        
        // Check if there are at least k nodes remaining
        ListNode check = head;
        int count = 0;
        while (check != null && count < k) {
            check = check.next;
            count++;
        }
        
        if (count < k) {
            // Fewer than k nodes remaining → don't reverse this group
            return head;
        }
        
        // Reverse first k nodes
        ListNode prev = null;
        ListNode curr = head;
        
        for (int i = 0; i < k; i++) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        
        // head is now the tail of this reversed group
        // curr is the start of the remaining list
        // Recursively reverse the rest and connect
        head.next = reverseInGroupsOfK(curr, k);
        
        return prev; // new head of this group
    }
    
    // ─────────────────────────────────────────────
    // VARIANT: Check if reverse produces same list (palindrome shortcut)
    // ─────────────────────────────────────────────
    public static boolean isReverseIdentical(ListNode head) {
        
        // Collect values
        java.util.List<Integer> original = new java.util.ArrayList<>();
        ListNode curr = head;
        while (curr != null) {
            original.add(curr.val);
            curr = curr.next;
        }
        
        // Reverse the list
        ListNode reversed = reverseList(head);
        
        // Compare with original values
        curr = reversed;
        int idx = 0;
        while (curr != null) {
            if (curr.val != original.get(original.size() - 1 - idx)) {
                // Restore list before returning
                reverseList(reversed);
                return false;
            }
            curr = curr.next;
            idx++;
        }
        
        // Restore the list
        reverseList(reversed);
        return true;
    }
    
    // ─────────────────────────────────────────────
    // TRACE: Step-by-step iterative visualization
    // ─────────────────────────────────────────────
    public static void traceIterative(ListNode head) {
        
        System.out.print("  Original: ");
        printList(head);
        System.out.println("  ─────────────────────────────────────────");
        
        // Work on a clone so we can show the result without losing original
        ListNode cloneHead = cloneList(head);
        
        ListNode prev = null;
        ListNode curr = cloneHead;
        int step = 0;
        
        System.out.println("  INIT: prev=null, curr=" 
            + (curr != null ? curr.val : "null"));
        System.out.println();
        
        while (curr != null) {
            step++;
            ListNode next = curr.next;
            
            System.out.println("  STEP " + step + " (curr=" + curr.val + "):");
            System.out.println("    A. Save:     next = " 
                + (next != null ? next.val : "null"));
            
            curr.next = prev;
            System.out.println("    B. Redirect: " + curr.val + ".next = " 
                + (prev != null ? prev.val : "null"));
            
            prev = curr;
            System.out.println("    C. Advance:  prev = " + prev.val);
            
            curr = next;
            System.out.println("    D. Advance:  curr = " 
                + (curr != null ? curr.val : "null"));
            
            // Show current state
            System.out.print("    State: reversed=[");
            ListNode temp = prev;
            boolean first = true;
            while (temp != null) {
                if (!first) System.out.print("←");
                System.out.print(temp.val);
                first = false;
                temp = temp.next;
            }
            System.out.print("] remaining=[");
            temp = curr;
            first = true;
            while (temp != null) {
                if (!first) System.out.print("→");
                System.out.print(temp.val);
                first = false;
                temp = temp.next;
            }
            System.out.println("]");
            System.out.println();
        }
        
        System.out.print("  ★ Reversed: ");
        printList(prev);
        System.out.println();
    }
    
    // ─────────────────────────────────────────────
    // TRACE: Step-by-step recursive visualization
    // ─────────────────────────────────────────────
    public static ListNode traceRecursive(ListNode head, int depth) {
        
        String indent = "  " + "  ".repeat(depth);
        
        if (head == null || head.next == null) {
            System.out.println(indent + "BASE CASE: node=" 
                + (head != null ? head.val : "null") 
                + " → return as new head");
            return head;
        }
        
        System.out.println(indent + "RECURSE: node=" + head.val 
            + " → going deeper with node " + head.next.val);
        
        ListNode newHead = traceRecursive(head.next, depth + 1);
        
        System.out.println(indent + "RETURN to node=" + head.val + ":");
        System.out.println(indent + "  " + head.next.val 
            + ".next = " + head.val + " (backward link)");
        head.next.next = head;
        
        System.out.println(indent + "  " + head.val 
            + ".next = null (break forward link)");
        head.next = null;
        
        System.out.println(indent + "  new head remains: " + newHead.val);
        
        return newHead;
    }
    
    // ─────────────────────────────────────────────
    // VISUAL: ASCII diagram of reversal process
    // ─────────────────────────────────────────────
    public static void visualReversal(int[] values) {
        
        System.out.println("  ┌─── REVERSAL VISUALIZATION ───┐");
        System.out.println();
        
        // Show original
        System.out.print("  ORIGINAL: ");
        for (int i = 0; i < values.length; i++) {
            System.out.print("[" + values[i] + "]");
            if (i < values.length - 1) System.out.print("→");
        }
        System.out.println("→null");
        System.out.println();
        
        // Show each step
        for (int step = 1; step <= values.length; step++) {
            System.out.print("  STEP " + step + ":   ");
            
            // Reversed portion (first 'step' elements, in reverse)
            System.out.print("null");
            for (int i = step - 1; i >= 0; i--) {
                System.out.print("←[" + values[i] + "]");
            }
            
            // Separator
            System.out.print("  ");
            
            // Remaining portion
            for (int i = step; i < values.length; i++) {
                System.out.print("[" + values[i] + "]");
                if (i < values.length - 1) System.out.print("→");
                else System.out.print("→null");
            }
            if (step == values.length) {
                // No remaining
            }
            
            System.out.println();
        }
        
        System.out.println();
        
        // Show final
        System.out.print("  REVERSED: ");
        for (int i = values.length - 1; i >= 0; i--) {
            System.out.print("[" + values[i] + "]");
            if (i > 0) System.out.print("→");
        }
        System.out.println("→null");
        
        System.out.println();
        System.out.println("  └────────────────────────────────┘");
        System.out.println();
    }
    
    // ─────────────────────────────────────────────
    // HELPERS
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
    
    public static ListNode cloneList(ListNode head) {
        if (head == null) return null;
        ListNode newHead = new ListNode(head.val);
        ListNode curr = newHead;
        ListNode orig = head.next;
        while (orig != null) {
            curr.next = new ListNode(orig.val);
            curr = curr.next;
            orig = orig.next;
        }
        return newHead;
    }
    
    public static void printList(ListNode head) {
        ListNode curr = head;
        while (curr != null) {
            System.out.print(curr.val);
            if (curr.next != null) System.out.print(" → ");
            curr = curr.next;
        }
        System.out.println();
    }
    
    public static String listToString(ListNode head) {
        StringBuilder sb = new StringBuilder();
        ListNode curr = head;
        while (curr != null) {
            sb.append(curr.val);
            if (curr.next != null) sb.append(" → ");
            curr = curr.next;
        }
        return sb.toString();
    }
    
    public static boolean listsEqual(ListNode a, ListNode b) {
        while (a != null && b != null) {
            if (a.val != b.val) return false;
            a = a.next;
            b = b.next;
        }
        return a == null && b == null;
    }
    
    // ─────────────────────────────────────────────
    // MAIN
    // ─────────────────────────────────────────────
    public static void main(String[] args) {
        
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 16: Reverse a Linked List                   ║");
        System.out.println("║  Pattern: In-place Reversal → Prev/Curr/Next Dance   ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println();
        
        // ── TEST 1: Visual overview ──
        System.out.println("═══ TEST 1: Visual Reversal Overview ═══");
        visualReversal(new int[] {1, 2, 3, 4, 5});
        
        // ── TEST 2: Iterative trace ──
        System.out.println("═══ TEST 2: Iterative Trace (5 nodes) ═══");
        traceIterative(buildList(new int[] {1, 2, 3, 4, 5}));
        
        // ── TEST 3: Iterative trace (3 nodes) ──
        System.out.println("═══ TEST 3: Iterative Trace (3 nodes) ═══");
        traceIterative(buildList(new int[] {10, 20, 30}));
        
        // ── TEST 4: Recursive trace ──
        System.out.println("═══ TEST 4: Recursive Trace ═══");
        ListNode recList = buildList(new int[] {1, 2, 3, 4});
        System.out.print("  Original: ");
        printList(cloneList(recList));
        System.out.println();
        ListNode recResult = traceRecursive(recList, 0);
        System.out.println();
        System.out.print("  ★ Reversed: ");
        printList(recResult);
        System.out.println();
        
        // ── TEST 5: Edge cases ──
        System.out.println("═══ TEST 5: Edge Cases ═══");
        
        // Null list
        ListNode nullResult = reverseList(null);
        System.out.println("  null → " + (nullResult == null ? "null" : nullResult.val));
        
        // Single node
        ListNode singleResult = reverseList(buildList(new int[] {42}));
        System.out.print("  [42] → ");
        printList(singleResult);
        
        // Two nodes
        ListNode twoResult = reverseList(buildList(new int[] {1, 2}));
        System.out.print("  [1,2] → ");
        printList(twoResult);
        
        // All same values
        ListNode sameResult = reverseList(buildList(new int[] {5, 5, 5, 5}));
        System.out.print("  [5,5,5,5] → ");
        printList(sameResult);
        System.out.println();
        
        // ── TEST 6: Iterative vs Recursive correctness ──
        System.out.println("═══ TEST 6: Iterative vs Recursive Correctness ═══");
        int[][] testCases = {
            {1, 2, 3, 4, 5},
            {10, 20},
            {7},
            {1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
            {-3, -2, -1, 0, 1, 2, 3}
        };
        
        boolean allPassed = true;
        for (int[] vals : testCases) {
            ListNode iterResult = reverseList(buildList(vals));
            ListNode recurResult = reverseListRecursive(buildList(vals));
            
            boolean match = listsEqual(iterResult, recurResult);
            
            System.out.print("  [");
            for (int i = 0; i < vals.length; i++) {
                System.out.print(vals[i]);
                if (i < vals.length - 1) System.out.print(",");
            }
            System.out.print("] → ");
            System.out.print(listToString(iterResult));
            System.out.println("  " + (match ? "✓" : "✗"));
            
            if (!match) allPassed = false;
        }
        System.out.println("  All match: " + allPassed);
        System.out.println();
        
        // ── TEST 7: Double reverse = original ──
        System.out.println("═══ TEST 7: Double Reverse = Original ═══");
        int[][] doubleTests = {
            {1, 2, 3, 4, 5},
            {10, 20, 30},
            {7},
            {1, 2}
        };
        
        boolean allRestored = true;
        for (int[] vals : doubleTests) {
            ListNode original = buildList(vals);
            ListNode reversed = reverseList(cloneList(original));
            ListNode doubleReversed = reverseList(reversed);
            
            boolean match = listsEqual(original, doubleReversed);
            
            System.out.print("  ");
            System.out.print(listToString(buildList(vals)));
            System.out.print(" → reverse → reverse → ");
            System.out.print(listToString(doubleReversed));
            System.out.println("  " + (match ? "✓" : "✗"));
            
            if (!match) allRestored = false;
        }
        System.out.println("  All restored: " + allRestored);
        System.out.println();
        
        // ── TEST 8: Reverse first N nodes ──
        System.out.println("═══ TEST 8: Reverse First N Nodes ═══");
        int[] baseList = {1, 2, 3, 4, 5};
        for (int n = 1; n <= 5; n++) {
            ListNode result = reverseFirstN(buildList(baseList), n);
            System.out.print("  Reverse first " + n + ": ");
            printList(result);
        }
        System.out.println();
        
        // ── TEST 9: Reverse in groups of K ──
        System.out.println("═══ TEST 9: Reverse in Groups of K ═══");
        int[] groupList = {1, 2, 3, 4, 5, 6, 7, 8};
        System.out.print("  Original: ");
        printList(buildList(groupList));
        
        for (int k = 2; k <= 4; k++) {
            ListNode result = reverseInGroupsOfK(buildList(groupList), k);
            System.out.print("  K=" + k + ": ");
            printList(result);
        }
        System.out.println();
        
        // ── TEST 10: Performance benchmark ──
        System.out.println("═══ TEST 10: Performance Benchmark ═══");
        int size = 10_000_000;
        
        // Build large list
        ListNode largeHead = new ListNode(0);
        ListNode curr = largeHead;
        for (int i = 1; i < size; i++) {
            curr.next = new ListNode(i);
            curr = curr.next;
        }
        
        // Iterative reversal
        long start = System.nanoTime();
        ListNode reversedLarge = reverseList(largeHead);
        long iterTime = System.nanoTime() - start;
        
        System.out.println("  List size: " + size + " nodes");
        System.out.println("  Iterative reversal: " + (iterTime / 1_000_000) + " ms");
        System.out.println("  New head value: " + reversedLarge.val 
            + " (expected: " + (size - 1) + ")");
        System.out.println("  Correct: " + (reversedLarge.val == size - 1));
        
        // Note: recursive would StackOverflow at this size
        System.out.println("  Recursive: SKIPPED (would StackOverflow at n=" 
            + size + ")");
        System.out.println();
    }
}