import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeKSortedLists {

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

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    // ─────────────────────────────────────────────
    // SOLUTION 1: Min-Heap of K Heads (OPTIMAL)
    // ─────────────────────────────────────────────
    public static ListNode mergeKLists(ListNode[] lists) {

        // Edge case: empty or null input
        if (lists == null || lists.length == 0) {
            return null;
        }

        // Min-heap ordered by node value
        PriorityQueue<ListNode> minHeap = new PriorityQueue<>(
            (a, b) -> Integer.compare(a.val, b.val)
        );

        // Step 1: Add head of each non-null list
        for (ListNode head : lists) {
            if (head != null) {
                minHeap.offer(head);
            }
        }

        // Step 2: Dummy head for result list (technique from Project 16)
        ListNode dummy = new ListNode(-1);
        ListNode tail = dummy;

        // Step 3: Extract min → append → offer successor
        while (!minHeap.isEmpty()) {

            // Extract global minimum
            ListNode minNode = minHeap.poll();

            // Append to merged list
            tail.next = minNode;
            tail = tail.next;

            // Offer successor from same list (if exists)
            if (minNode.next != null) {
                minHeap.offer(minNode.next);
            }
        }

        // Step 4: Clean termination
        tail.next = null;

        return dummy.next;
    }

    // ─────────────────────────────────────────────
    // SOLUTION 2: Brute Force — Collect + Sort + Build
    // ─────────────────────────────────────────────
    public static ListNode mergeKListsBruteForce(ListNode[] lists) {

        if (lists == null || lists.length == 0) return null;

        // Collect all values
        List<Integer> allValues = new ArrayList<>();
        for (ListNode head : lists) {
            ListNode curr = head;
            while (curr != null) {
                allValues.add(curr.val);
                curr = curr.next;
            }
        }

        if (allValues.isEmpty()) return null;

        // Sort
        allValues.sort(null);

        // Build new list
        ListNode dummy = new ListNode(-1);
        ListNode tail = dummy;
        for (int val : allValues) {
            tail.next = new ListNode(val);
            tail = tail.next;
        }

        return dummy.next;
    }

    // ─────────────────────────────────────────────
    // SOLUTION 3: Sequential Pairwise Merge
    // ─────────────────────────────────────────────
    public static ListNode mergeKListsSequential(ListNode[] lists) {

        if (lists == null || lists.length == 0) return null;

        ListNode merged = lists[0];
        for (int i = 1; i < lists.length; i++) {
            merged = mergeTwoLists(merged, lists[i]);
        }

        return merged;
    }

    // ─────────────────────────────────────────────
    // SOLUTION 4: Divide and Conquer Merge
    // ─────────────────────────────────────────────
    public static ListNode mergeKListsDivideConquer(ListNode[] lists) {

        if (lists == null || lists.length == 0) return null;
        return divideAndMerge(lists, 0, lists.length - 1);
    }

    private static ListNode divideAndMerge(ListNode[] lists, int lo, int hi) {

        if (lo == hi) return lists[lo];
        if (lo + 1 == hi) return mergeTwoLists(lists[lo], lists[hi]);

        int mid = lo + (hi - lo) / 2;
        ListNode left = divideAndMerge(lists, lo, mid);
        ListNode right = divideAndMerge(lists, mid + 1, hi);
        return mergeTwoLists(left, right);
    }

    // Helper: merge two sorted linked lists
    private static ListNode mergeTwoLists(ListNode l1, ListNode l2) {

        ListNode dummy = new ListNode(-1);
        ListNode tail = dummy;

        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                tail.next = l1;
                l1 = l1.next;
            } else {
                tail.next = l2;
                l2 = l2.next;
            }
            tail = tail.next;
        }

        tail.next = (l1 != null) ? l1 : l2;
        return dummy.next;
    }

    // ─────────────────────────────────────────────
    // TRACE — Watch the heap-based merge step by step
    // ─────────────────────────────────────────────
    public static void mergeKListsWithTrace(ListNode[] lists) {

        System.out.println("Input lists:");
        for (int i = 0; i < lists.length; i++) {
            System.out.println("  L" + i + ": " + listToString(lists[i]));
        }
        System.out.println("─────────────────────────────────");

        if (lists == null || lists.length == 0) {
            System.out.println("Empty input → null");
            return;
        }

        PriorityQueue<ListNode> minHeap = new PriorityQueue<>(
            (a, b) -> Integer.compare(a.val, b.val)
        );

        for (ListNode head : lists) {
            if (head != null) {
                minHeap.offer(head);
            }
        }

        System.out.print("Initial heap: [");
        printHeapValues(minHeap);
        System.out.println("]");
        System.out.println();

        ListNode dummy = new ListNode(-1);
        ListNode tail = dummy;
        int step = 1;

        while (!minHeap.isEmpty()) {
            ListNode minNode = minHeap.poll();

            System.out.println("Step " + step + ": Poll min = " + minNode.val);

            tail.next = minNode;
            tail = tail.next;

            if (minNode.next != null) {
                System.out.println("  → Successor = " + minNode.next.val + " → offer to heap");
                minHeap.offer(minNode.next);
            } else {
                System.out.println("  → No successor (list exhausted)");
            }

            System.out.print("  → Heap: [");
            printHeapValues(minHeap);
            System.out.println("]  (size=" + minHeap.size() + ")");

            System.out.print("  → Merged so far: ");
            tail.next = null; // temp null for printing
            System.out.println(listToString(dummy.next));
            System.out.println();

            step++;
        }

        tail.next = null;

        System.out.println("═══════════════════════════════════");
        System.out.println("FINAL MERGED: " + listToString(dummy.next));
        System.out.println("═══════════════════════════════════");
    }

    private static void printHeapValues(PriorityQueue<ListNode> heap) {
        // Create sorted copy for display
        List<Integer> vals = new ArrayList<>();
        for (ListNode node : heap) {
            vals.add(node.val);
        }
        vals.sort(null);
        for (int i = 0; i < vals.size(); i++) {
            System.out.print(vals.get(i));
            if (i < vals.size() - 1) System.out.print(", ");
        }
    }

    // ─────────────────────────────────────────────
    // VARIANT: Merge K Sorted Arrays (not linked lists)
    // ─────────────────────────────────────────────
    public static int[] mergeKSortedArrays(int[][] arrays) {

        if (arrays == null || arrays.length == 0) return new int[0];

        // Heap stores: [value, arrayIndex, elementIndex]
        PriorityQueue<int[]> minHeap = new PriorityQueue<>(
            (a, b) -> Integer.compare(a[0], b[0])
        );

        // Count total elements and initialize heap
        int totalSize = 0;
        for (int i = 0; i < arrays.length; i++) {
            if (arrays[i] != null && arrays[i].length > 0) {
                minHeap.offer(new int[] { arrays[i][0], i, 0 });
                totalSize += arrays[i].length;
            }
        }

        int[] result = new int[totalSize];
        int idx = 0;

        while (!minHeap.isEmpty()) {
            int[] curr = minHeap.poll();
            int value = curr[0];
            int arrIdx = curr[1];
            int elemIdx = curr[2];

            result[idx++] = value;

            // Offer successor from same array
            if (elemIdx + 1 < arrays[arrIdx].length) {
                minHeap.offer(new int[] {
                    arrays[arrIdx][elemIdx + 1], arrIdx, elemIdx + 1
                });
            }
        }

        return result;
    }

    // ─────────────────────────────────────────────
    // HELPERS: Build and display linked lists
    // ─────────────────────────────────────────────
    public static ListNode buildList(int[] values) {
        if (values == null || values.length == 0) return null;
        ListNode head = new ListNode(values[0]);
        ListNode curr = head;
        for (int i = 1; i < values.length; i++) {
            curr.next = new ListNode(values[i]);
            curr = curr.next;
        }
        return head;
    }

    public static String listToString(ListNode head) {
        if (head == null) return "null";
        StringBuilder sb = new StringBuilder();
        ListNode curr = head;
        while (curr != null) {
            sb.append(curr.val);
            if (curr.next != null) sb.append(" → ");
            curr = curr.next;
        }
        return sb.toString();
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

        System.out.println("╔═══════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 40: Merge K Sorted Lists                     ║");
        System.out.println("║  Pattern: K-way Merge → Min-Heap of List Heads        ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Classic Example with Trace ──
        System.out.println("═══ TEST 1: Classic Example with Full Trace ═══");
        ListNode[] lists1 = {
            buildList(new int[] {1, 4, 5}),
            buildList(new int[] {1, 3, 4}),
            buildList(new int[] {2, 6})
        };
        mergeKListsWithTrace(lists1);
        System.out.println();

        // ── TEST 2: Negative Numbers ──
        System.out.println("═══ TEST 2: Negative Numbers ═══");
        ListNode[] lists2 = {
            buildList(new int[] {-2, 1, 4}),
            buildList(new int[] {-3, 2, 5}),
            buildList(new int[] {-1, 0, 3})
        };
        mergeKListsWithTrace(lists2);
        System.out.println();

        // ── TEST 3: Single Element Lists ──
        System.out.println("═══ TEST 3: Single Element Lists ═══");
        ListNode[] lists3 = {
            buildList(new int[] {5}),
            buildList(new int[] {2}),
            buildList(new int[] {8}),
            buildList(new int[] {1})
        };
        ListNode merged3 = mergeKLists(lists3);
        System.out.println("Result: " + listToString(merged3));
        System.out.println();

        // ── TEST 4: With Null Lists ──
        System.out.println("═══ TEST 4: With Null Lists ═══");
        ListNode[] lists4 = {
            null,
            buildList(new int[] {1, 2}),
            null,
            buildList(new int[] {3, 4})
        };
        ListNode merged4 = mergeKLists(lists4);
        System.out.println("Input: [null, 1→2, null, 3→4]");
        System.out.println("Result: " + listToString(merged4));
        System.out.println();

        // ── TEST 5: All Solutions Agree ──
        System.out.println("═══ TEST 5: Verify All Solutions Agree ═══");
        // Need to rebuild lists since they get modified
        ListNode[] listsA = {
            buildList(new int[] {1, 4, 7}),
            buildList(new int[] {2, 5, 8}),
            buildList(new int[] {3, 6, 9})
        };
        ListNode[] listsB = {
            buildList(new int[] {1, 4, 7}),
            buildList(new int[] {2, 5, 8}),
            buildList(new int[] {3, 6, 9})
        };
        ListNode[] listsC = {
            buildList(new int[] {1, 4, 7}),
            buildList(new int[] {2, 5, 8}),
            buildList(new int[] {3, 6, 9})
        };
        ListNode[] listsD = {
            buildList(new int[] {1, 4, 7}),
            buildList(new int[] {2, 5, 8}),
            buildList(new int[] {3, 6, 9})
        };

        System.out.println("Input: [1→4→7, 2→5→8, 3→6→9]");
        System.out.println("  Heap approach:         " 
            + listToString(mergeKLists(listsA)));
        System.out.println("  Brute force:           " 
            + listToString(mergeKListsBruteForce(listsB)));
        System.out.println("  Sequential merge:      " 
            + listToString(mergeKListsSequential(listsC)));
        System.out.println("  Divide and conquer:    " 
            + listToString(mergeKListsDivideConquer(listsD)));
        System.out.println();

        // ── TEST 6: K Sorted Arrays Variant ──
        System.out.println("═══ TEST 6: Merge K Sorted Arrays ═══");
        int[][] arrays = {
            {1, 5, 9},
            {2, 6, 10},
            {3, 7, 11},
            {4, 8, 12}
        };
        System.out.println("Input arrays:");
        for (int[] arr : arrays) {
            System.out.println("  " + Arrays.toString(arr));
        }
        int[] mergedArr = mergeKSortedArrays(arrays);
        System.out.println("Merged: " + Arrays.toString(mergedArr));
        System.out.println();

        // ── TEST 7: Performance Comparison ──
        System.out.println("═══ TEST 7: Performance Comparison ═══");
        int K = 500;
        int elementsPerList = 2000;
        int N = K * elementsPerList;
        System.out.println("K=" + K + " lists, " + elementsPerList 
            + " elements each, N=" + N + " total");

        // Build K sorted lists
        ListNode[][] allListSets = new ListNode[4][K];
        for (int set = 0; set < 4; set++) {
            for (int i = 0; i < K; i++) {
                int[] vals = new int[elementsPerList];
                int base = i; // offset so lists interleave
                for (int j = 0; j < elementsPerList; j++) {
                    vals[j] = base + j * K;
                }
                allListSets[set][i] = buildList(vals);
            }
        }

        // Heap approach
        long start = System.nanoTime();
        ListNode resHeap = mergeKLists(allListSets[0]);
        long heapTime = System.nanoTime() - start;
        System.out.println("  Heap:                " + (heapTime / 1_000_000) + " ms"
            + " (length=" + listLength(resHeap) + ")");

        // Brute force
        start = System.nanoTime();
        ListNode resBrute = mergeKListsBruteForce(allListSets[1]);
        long bruteTime = System.nanoTime() - start;
        System.out.println("  Brute (collect+sort): " + (bruteTime / 1_000_000) + " ms"
            + " (length=" + listLength(resBrute) + ")");

        // Sequential
        start = System.nanoTime();
        ListNode resSeq = mergeKListsSequential(allListSets[2]);
        long seqTime = System.nanoTime() - start;
        System.out.println("  Sequential merge:    " + (seqTime / 1_000_000) + " ms"
            + " (length=" + listLength(resSeq) + ")");

        // Divide and conquer
        start = System.nanoTime();
        ListNode resDC = mergeKListsDivideConquer(allListSets[3]);
        long dcTime = System.nanoTime() - start;
        System.out.println("  Divide & conquer:    " + (dcTime / 1_000_000) + " ms"
            + " (length=" + listLength(resDC) + ")");

        System.out.println();
        System.out.println("  Heap vs Sequential: " 
            + String.format("%.1f", (double) seqTime / heapTime) + "x faster");
        System.out.println("  Heap vs Brute:      " 
            + String.format("%.1f", (double) bruteTime / heapTime) + "x faster");

        // ── TEST 8: Edge Cases ──
        System.out.println();
        System.out.println("═══ TEST 8: Edge Cases ═══");

        // Empty input
        System.out.println("Empty array []: " 
            + listToString(mergeKLists(new ListNode[] {})));

        // All nulls
        System.out.println("All nulls [null, null]: " 
            + listToString(mergeKLists(new ListNode[] {null, null})));

        // Single list
        System.out.println("Single list [1→2→3]: " 
            + listToString(mergeKLists(new ListNode[] {buildList(new int[] {1, 2, 3})})));

        // Two lists (degenerate K=2)
        ListNode[] twoLists = {
            buildList(new int[] {1, 3, 5}),
            buildList(new int[] {2, 4, 6})
        };
        System.out.println("Two lists [1→3→5, 2→4→6]: " 
            + listToString(mergeKLists(twoLists)));

        // Lists of very different lengths
        ListNode[] unevenLists = {
            buildList(new int[] {1}),
            buildList(new int[] {2, 3, 4, 5, 6, 7, 8, 9, 10}),
            buildList(new int[] {5, 20})
        };
        System.out.println("Uneven lengths [1], [2→...→10], [5→20]: " 
            + listToString(mergeKLists(unevenLists)));
    }
}