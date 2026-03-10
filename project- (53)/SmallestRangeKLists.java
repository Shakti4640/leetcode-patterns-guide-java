import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class SmallestRangeKLists {

    // ═══════════════════════════════════════════════════════
    // APPROACH A: Min-Heap + Global Max Tracking (Optimal)
    // ═══════════════════════════════════════════════════════
    
    public static int[] smallestRange(List<List<Integer>> lists) {
        
        int k = lists.size();
        
        // Min-heap: entries are {value, listIndex, elementIndex}
        // Sorted by value (ascending)
        PriorityQueue<int[]> minHeap = new PriorityQueue<>(
            (a, b) -> a[0] - b[0]
        );
        
        // Initialize: push first element from each list
        int currentMax = Integer.MIN_VALUE;
        
        for (int i = 0; i < k; i++) {
            int val = lists.get(i).get(0);
            minHeap.offer(new int[]{val, i, 0});
            currentMax = Math.max(currentMax, val);
        }
        
        // Best range found so far
        int bestStart = minHeap.peek()[0];
        int bestEnd = currentMax;
        
        // Process: always advance the minimum
        while (true) {
            // Poll the minimum element
            int[] minEntry = minHeap.poll();
            int minVal = minEntry[0];
            int listIdx = minEntry[1];
            int elemIdx = minEntry[2];
            
            // Check if this list has a next element
            if (elemIdx + 1 >= lists.get(listIdx).size()) {
                break;  // list exhausted → can't cover all K lists → stop
            }
            
            // Push the next element from the same list
            int nextVal = lists.get(listIdx).get(elemIdx + 1);
            minHeap.offer(new int[]{nextVal, listIdx, elemIdx + 1});
            
            // Update current maximum
            currentMax = Math.max(currentMax, nextVal);
            
            // Check if new range is better
            int newMin = minHeap.peek()[0];
            int newRange = currentMax - newMin;
            int bestRange = bestEnd - bestStart;
            
            if (newRange < bestRange 
                || (newRange == bestRange && newMin < bestStart)) {
                bestStart = newMin;
                bestEnd = currentMax;
            }
        }
        
        return new int[]{bestStart, bestEnd};
    }
    
    // ═══════════════════════════════════════════════════════
    // APPROACH B: Brute Force — Try All Combinations
    // For verification only — O(N^K) time
    // ═══════════════════════════════════════════════════════
    
    public static int[] smallestRangeBrute(List<List<Integer>> lists) {
        
        int k = lists.size();
        int[] indices = new int[k]; // current index in each list
        int bestStart = Integer.MIN_VALUE;
        int bestEnd = Integer.MAX_VALUE;
        boolean found = false;
        
        // Try all combinations using recursive enumeration
        // (only practical for small inputs)
        return bruteHelper(lists, indices, 0, k);
    }
    
    private static int[] bruteHelper(
            List<List<Integer>> lists, int[] indices, int listIdx, int k) {
        
        if (listIdx == k) {
            // All lists have a chosen element → compute range
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;
            for (int i = 0; i < k; i++) {
                int val = lists.get(i).get(indices[i]);
                min = Math.min(min, val);
                max = Math.max(max, val);
            }
            return new int[]{min, max};
        }
        
        int[] bestRange = null;
        
        for (int j = 0; j < lists.get(listIdx).size(); j++) {
            indices[listIdx] = j;
            int[] range = bruteHelper(lists, indices, listIdx + 1, k);
            
            if (bestRange == null 
                || (range[1] - range[0]) < (bestRange[1] - bestRange[0])
                || ((range[1] - range[0]) == (bestRange[1] - bestRange[0]) 
                    && range[0] < bestRange[0])) {
                bestRange = range;
            }
        }
        
        return bestRange;
    }
    
    // ═══════════════════════════════════════════════════════
    // APPROACH C: Merge + Sliding Window
    // Alternative: merge all, then sliding window of K lists
    // ═══════════════════════════════════════════════════════
    
    public static int[] smallestRangeMergeWindow(List<List<Integer>> lists) {
        
        int k = lists.size();
        
        // Merge all elements with their list index
        List<int[]> merged = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            for (int val : lists.get(i)) {
                merged.add(new int[]{val, i});
            }
        }
        
        // Sort by value
        merged.sort((a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]);
        
        // Sliding window: find smallest window containing all K lists
        int[] listCount = new int[k];  // count of each list in current window
        int coveredLists = 0;          // how many distinct lists are covered
        
        int bestStart = merged.get(0)[0];
        int bestEnd = merged.get(merged.size() - 1)[0];
        
        int left = 0;
        
        for (int right = 0; right < merged.size(); right++) {
            int rightList = merged.get(right)[1];
            
            if (listCount[rightList] == 0) {
                coveredLists++;
            }
            listCount[rightList]++;
            
            // Shrink window from left while all K lists are covered
            while (coveredLists == k) {
                int currentRange = merged.get(right)[0] - merged.get(left)[0];
                int bestRange = bestEnd - bestStart;
                
                if (currentRange < bestRange 
                    || (currentRange == bestRange 
                        && merged.get(left)[0] < bestStart)) {
                    bestStart = merged.get(left)[0];
                    bestEnd = merged.get(right)[0];
                }
                
                int leftList = merged.get(left)[1];
                listCount[leftList]--;
                if (listCount[leftList] == 0) {
                    coveredLists--;
                }
                left++;
            }
        }
        
        return new int[]{bestStart, bestEnd};
    }
    
    // ═══════════════════════════════════════════════════════
    // TRACE: Visualize the Min-Heap Approach
    // ═══════════════════════════════════════════════════════
    
    public static void smallestRangeWithTrace(List<List<Integer>> lists) {
        
        int k = lists.size();
        
        System.out.println("Lists (" + k + "):");
        for (int i = 0; i < k; i++) {
            System.out.println("  List " + i + ": " + lists.get(i));
        }
        System.out.println();
        
        PriorityQueue<int[]> minHeap = new PriorityQueue<>(
            (a, b) -> a[0] - b[0]
        );
        
        int currentMax = Integer.MIN_VALUE;
        
        for (int i = 0; i < k; i++) {
            int val = lists.get(i).get(0);
            minHeap.offer(new int[]{val, i, 0});
            currentMax = Math.max(currentMax, val);
        }
        
        int bestStart = minHeap.peek()[0];
        int bestEnd = currentMax;
        
        System.out.println("Initial: min=" + bestStart + " max=" + currentMax
            + " → range [" + bestStart + ", " + bestEnd + "] size=" 
            + (bestEnd - bestStart));
        System.out.println();
        
        int step = 1;
        
        while (true) {
            int[] minEntry = minHeap.poll();
            int minVal = minEntry[0];
            int listIdx = minEntry[1];
            int elemIdx = minEntry[2];
            
            System.out.println("Step " + step + ": Poll min=" + minVal 
                + " (list " + listIdx + ", idx " + elemIdx + ")");
            
            if (elemIdx + 1 >= lists.get(listIdx).size()) {
                System.out.println("  → List " + listIdx 
                    + " EXHAUSTED → stop");
                break;
            }
            
            int nextVal = lists.get(listIdx).get(elemIdx + 1);
            minHeap.offer(new int[]{nextVal, listIdx, elemIdx + 1});
            currentMax = Math.max(currentMax, nextVal);
            
            int newMin = minHeap.peek()[0];
            int newRange = currentMax - newMin;
            int bestRange = bestEnd - bestStart;
            
            System.out.println("  → Push next=" + nextVal 
                + " from list " + listIdx);
            System.out.println("  → Current: min=" + newMin 
                + " max=" + currentMax 
                + " → range [" + newMin + ", " + currentMax + "] size=" 
                + newRange);
            
            if (newRange < bestRange 
                || (newRange == bestRange && newMin < bestStart)) {
                bestStart = newMin;
                bestEnd = currentMax;
                System.out.println("  → ★ NEW BEST: [" + bestStart 
                    + ", " + bestEnd + "] size=" + (bestEnd - bestStart));
            } else {
                System.out.println("  → Best unchanged: [" + bestStart 
                    + ", " + bestEnd + "] size=" + (bestEnd - bestStart));
            }
            
            System.out.println();
            step++;
        }
        
        System.out.println("\n═══ ANSWER: [" + bestStart + ", " + bestEnd 
            + "] (size " + (bestEnd - bestStart) + ") ═══");
    }
    
    // ═══════════════════════════════════════════════════════
    // HELPER: Convert int[][] to List<List<Integer>>
    // ═══════════════════════════════════════════════════════
    
    private static List<List<Integer>> toLists(int[][] arrays) {
        List<List<Integer>> lists = new ArrayList<>();
        for (int[] arr : arrays) {
            List<Integer> list = new ArrayList<>();
            for (int val : arr) list.add(val);
            lists.add(list);
        }
        return lists;
    }
    
    // ═══════════════════════════════════════════════════════
    // MAIN — Run all examples
    // ═══════════════════════════════════════════════════════
    
    public static void main(String[] args) {
        
        System.out.println("╔═══════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 53: Smallest Range Covering K Lists          ║");
        System.out.println("║  Pattern: K-way Merge → Min-Heap + Global Max Track   ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        System.out.println();
        
        // ── TEST 1: Classic example ──
        System.out.println("═══ TEST 1: Classic Example ═══");
        smallestRangeWithTrace(toLists(new int[][]{
            {4, 10, 15, 24, 26},
            {0, 9, 12, 20},
            {5, 18, 22, 30}
        }));
        System.out.println();
        
        // ── TEST 2: Identical lists ──
        System.out.println("═══ TEST 2: Identical Lists ═══");
        smallestRangeWithTrace(toLists(new int[][]{
            {1, 2, 3},
            {1, 2, 3},
            {1, 2, 3}
        }));
        System.out.println();
        
        // ── TEST 3: Single element lists ──
        System.out.println("═══ TEST 3: Single Element Lists ═══");
        smallestRangeWithTrace(toLists(new int[][]{
            {1},
            {2},
            {3}
        }));
        System.out.println();
        
        // ── TEST 4: Two lists ──
        System.out.println("═══ TEST 4: Two Lists ═══");
        smallestRangeWithTrace(toLists(new int[][]{
            {1, 5, 10},
            {3, 7, 12}
        }));
        System.out.println();
        
        // ── TEST 5: Evenly spaced ──
        System.out.println("═══ TEST 5: Evenly Spaced ═══");
        smallestRangeWithTrace(toLists(new int[][]{
            {1, 5, 10},
            {3, 7, 12},
            {2, 6, 11}
        }));
        System.out.println();
        
        // ── TEST 6: Cross-verification ──
        System.out.println("═══ TEST 6: Cross-Verification (All Approaches) ═══");
        
        int[][][] allTests = {
            {{4,10,15,24,26}, {0,9,12,20}, {5,18,22,30}},
            {{1,2,3}, {1,2,3}, {1,2,3}},
            {{1}, {2}, {3}},
            {{1,5,10}, {3,7,12}},
            {{1,5,10}, {3,7,12}, {2,6,11}},
            {{-5,0,5}, {-3,1,4}, {-1,2,3}},
            {{1,3,5,7,9}, {2,4,6,8,10}},
            {{10,20,30}, {1,11,21,31}, {5,15,25,35}},
        };
        
        for (int t = 0; t < allTests.length; t++) {
            List<List<Integer>> lists = toLists(allTests[t]);
            
            int[] heapResult = smallestRange(lists);
            int[] mergeResult = smallestRangeMergeWindow(lists);
            
            // Brute force only for small inputs
            int[] bruteResult = null;
            int totalSize = 0;
            for (List<Integer> l : lists) totalSize += l.size();
            if (totalSize <= 30) {
                bruteResult = smallestRangeBrute(lists);
            }
            
            boolean match = (heapResult[0] == mergeResult[0]) 
                && (heapResult[1] == mergeResult[1]);
            if (bruteResult != null) {
                match = match && (heapResult[1] - heapResult[0]) 
                    == (bruteResult[1] - bruteResult[0]);
            }
            
            String status = match ? "✓" : "✗";
            
            System.out.println("  Test " + (t + 1) 
                + ": Heap=[" + heapResult[0] + "," + heapResult[1] + "]"
                + "  Merge=[" + mergeResult[0] + "," + mergeResult[1] + "]"
                + (bruteResult != null 
                    ? "  Brute=[" + bruteResult[0] + "," + bruteResult[1] + "]" 
                    : "")
                + "  " + status);
        }
        System.out.println();
        
        // ── TEST 7: Performance benchmark ──
        System.out.println("═══ TEST 7: Performance Benchmark ═══");
        java.util.Random rand = new java.util.Random(42);
        
        int[][] benchConfigs = {
            {10, 1000},     // 10 lists × 1000 elements
            {50, 5000},     // 50 lists × 5000 elements
            {100, 10000},   // 100 lists × 10000 elements
            {500, 5000},    // 500 lists × 5000 elements
        };
        
        for (int[] config : benchConfigs) {
            int numLists = config[0];
            int listSize = config[1];
            
            List<List<Integer>> bigLists = new ArrayList<>();
            for (int i = 0; i < numLists; i++) {
                List<Integer> list = new ArrayList<>();
                int val = rand.nextInt(100);
                for (int j = 0; j < listSize; j++) {
                    list.add(val);
                    val += 1 + rand.nextInt(10);
                }
                bigLists.add(list);
            }
            
            long totalElements = (long) numLists * listSize;
            
            // Heap approach
            long startTime = System.nanoTime();
            int[] heapRes = smallestRange(bigLists);
            long heapTime = System.nanoTime() - startTime;
            
            // Merge+Window approach
            startTime = System.nanoTime();
            int[] mergeRes = smallestRangeMergeWindow(bigLists);
            long mergeTime = System.nanoTime() - startTime;
            
            System.out.println("  K=" + numLists + " × n=" + listSize 
                + " (total=" + totalElements + "):");
            System.out.println("    Heap:  [" + heapRes[0] + "," + heapRes[1] 
                + "] → " + (heapTime / 1_000_000) + " ms");
            System.out.println("    Merge: [" + mergeRes[0] + "," + mergeRes[1] 
                + "] → " + (mergeTime / 1_000_000) + " ms");
        }
    }
}