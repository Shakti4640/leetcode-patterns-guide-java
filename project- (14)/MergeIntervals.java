import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeIntervals {

    // ─────────────────────────────────────────────
    // CORE SOLUTION: Sort + Linear Scan
    // ─────────────────────────────────────────────
    public static int[][] merge(int[][] intervals) {
        
        // Edge case
        if (intervals == null || intervals.length <= 1) {
            return intervals;
        }
        
        // Step 1: Sort by start time
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        
        // Step 2: Scan and merge
        List<int[]> merged = new ArrayList<>();
        int[] currentInterval = intervals[0];
        merged.add(currentInterval);
        
        for (int i = 1; i < intervals.length; i++) {
            int currentEnd = currentInterval[1];
            int nextStart = intervals[i][0];
            int nextEnd = intervals[i][1];
            
            if (currentEnd >= nextStart) {
                // Overlap → extend current interval's end
                currentInterval[1] = Math.max(currentEnd, nextEnd);
            } else {
                // No overlap → save current, start new
                currentInterval = intervals[i];
                merged.add(currentInterval);
            }
        }
        
        // Convert list to array
        return merged.toArray(new int[merged.size()][]);
    }
    
    // ─────────────────────────────────────────────
    // ALTERNATIVE: Using last element of result list
    // ─────────────────────────────────────────────
    public static int[][] mergeAlternative(int[][] intervals) {
        
        if (intervals == null || intervals.length <= 1) {
            return intervals;
        }
        
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        
        List<int[]> merged = new ArrayList<>();
        
        for (int[] interval : intervals) {
            // If merged list is empty OR no overlap with last merged interval
            if (merged.isEmpty() || merged.get(merged.size() - 1)[1] < interval[0]) {
                // No overlap → add as new interval
                merged.add(new int[] {interval[0], interval[1]});
            } else {
                // Overlap → extend the last merged interval's end
                merged.get(merged.size() - 1)[1] = 
                    Math.max(merged.get(merged.size() - 1)[1], interval[1]);
            }
        }
        
        return merged.toArray(new int[merged.size()][]);
    }
    
    // ─────────────────────────────────────────────
    // VARIANT: Count number of merged intervals
    // (useful when you only need the count, not the intervals)
    // ─────────────────────────────────────────────
    public static int countMergedIntervals(int[][] intervals) {
        
        if (intervals == null || intervals.length == 0) return 0;
        if (intervals.length == 1) return 1;
        
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        
        int count = 1; // first interval always exists
        int currentEnd = intervals[0][1];
        
        for (int i = 1; i < intervals.length; i++) {
            if (currentEnd >= intervals[i][0]) {
                // Overlap → merge (just update end, don't count new)
                currentEnd = Math.max(currentEnd, intervals[i][1]);
            } else {
                // No overlap → new interval
                count++;
                currentEnd = intervals[i][1];
            }
        }
        
        return count;
    }
    
    // ─────────────────────────────────────────────
    // VARIANT: Find total covered length
    // (how many points are covered by at least one interval)
    // ─────────────────────────────────────────────
    public static int totalCoveredLength(int[][] intervals) {
        
        if (intervals == null || intervals.length == 0) return 0;
        
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        
        int totalLength = 0;
        int currentStart = intervals[0][0];
        int currentEnd = intervals[0][1];
        
        for (int i = 1; i < intervals.length; i++) {
            if (currentEnd >= intervals[i][0]) {
                // Overlap → extend
                currentEnd = Math.max(currentEnd, intervals[i][1]);
            } else {
                // No overlap → finalize current segment
                totalLength += (currentEnd - currentStart);
                currentStart = intervals[i][0];
                currentEnd = intervals[i][1];
            }
        }
        
        // Add last segment
        totalLength += (currentEnd - currentStart);
        
        return totalLength;
    }
    
    // ─────────────────────────────────────────────
    // VARIANT: Find gaps between intervals
    // ─────────────────────────────────────────────
    public static List<int[]> findGaps(int[][] intervals) {
        
        List<int[]> gaps = new ArrayList<>();
        
        if (intervals == null || intervals.length <= 1) return gaps;
        
        // Merge first, then find gaps between merged intervals
        int[][] mergedIntervals = merge(intervals);
        
        for (int i = 1; i < mergedIntervals.length; i++) {
            int gapStart = mergedIntervals[i - 1][1];
            int gapEnd = mergedIntervals[i][0];
            if (gapStart < gapEnd) {
                gaps.add(new int[] {gapStart, gapEnd});
            }
        }
        
        return gaps;
    }
    
    // ─────────────────────────────────────────────
    // BRUTE FORCE: Mark all points on a number line
    // (for small ranges only — for correctness verification)
    // ─────────────────────────────────────────────
    public static int[][] mergeBruteForce(int[][] intervals) {
        
        if (intervals == null || intervals.length <= 1) return intervals;
        
        // Find the range
        int minVal = Integer.MAX_VALUE;
        int maxVal = Integer.MIN_VALUE;
        for (int[] interval : intervals) {
            minVal = Math.min(minVal, interval[0]);
            maxVal = Math.max(maxVal, interval[1]);
        }
        
        // Mark all covered points
        boolean[] covered = new boolean[maxVal - minVal + 2];
        for (int[] interval : intervals) {
            for (int j = interval[0] - minVal; j <= interval[1] - minVal; j++) {
                covered[j] = true;
            }
        }
        
        // Extract merged intervals from covered array
        List<int[]> result = new ArrayList<>();
        int i = 0;
        while (i < covered.length) {
            if (covered[i]) {
                int start = i + minVal;
                while (i < covered.length && covered[i]) i++;
                int end = i - 1 + minVal;
                result.add(new int[] {start, end});
            } else {
                i++;
            }
        }
        
        return result.toArray(new int[result.size()][]);
    }
    
    // ─────────────────────────────────────────────
    // TRACE: Step-by-step visualization
    // ─────────────────────────────────────────────
    public static void traceExecution(int[][] intervals) {
        
        System.out.print("  Input:  ");
        printIntervals(intervals);
        
        if (intervals == null || intervals.length <= 1) {
            System.out.println("  → Single or empty → return as-is");
            System.out.println();
            return;
        }
        
        // Sort
        int[][] sorted = new int[intervals.length][];
        for (int i = 0; i < intervals.length; i++) {
            sorted[i] = intervals[i].clone();
        }
        Arrays.sort(sorted, (a, b) -> Integer.compare(a[0], b[0]));
        
        System.out.print("  Sorted: ");
        printIntervals(sorted);
        System.out.println("  ─────────────────────────────────────────");
        
        List<int[]> merged = new ArrayList<>();
        int[] current = sorted[0].clone();
        merged.add(current);
        
        System.out.println("  Initialize current = [" 
            + current[0] + ", " + current[1] + "]");
        System.out.println();
        
        for (int i = 1; i < sorted.length; i++) {
            int[] next = sorted[i];
            
            System.out.println("  Compare current=[" + current[0] + "," + current[1] 
                + "] with next=[" + next[0] + "," + next[1] + "]");
            
            if (current[1] >= next[0]) {
                int oldEnd = current[1];
                current[1] = Math.max(current[1], next[1]);
                System.out.println("  → OVERLAP: " + current[1 - 1] + " ≥ " + next[0]
                    + " → extend end: max(" + oldEnd + "," + next[1] + ")=" + current[1]);
                System.out.println("  → current = [" + current[0] + ", " + current[1] + "]");
            } else {
                System.out.println("  → NO OVERLAP: " + current[1] + " < " + next[0]
                    + " → save current, start new");
                current = next.clone();
                merged.add(current);
                System.out.println("  → current = [" + current[0] + ", " + current[1] + "]");
            }
            System.out.println();
        }
        
        System.out.print("  ★ RESULT: ");
        printIntervals(merged.toArray(new int[merged.size()][]));
        System.out.println();
    }
    
    // ─────────────────────────────────────────────
    // VISUAL: ASCII art interval diagram
    // ─────────────────────────────────────────────
    public static void visualizeIntervals(int[][] intervals, String label) {
        
        if (intervals == null || intervals.length == 0) return;
        
        // Find range
        int minVal = Integer.MAX_VALUE;
        int maxVal = Integer.MIN_VALUE;
        for (int[] interval : intervals) {
            minVal = Math.min(minVal, interval[0]);
            maxVal = Math.max(maxVal, interval[1]);
        }
        
        System.out.println("  " + label + ":");
        
        // Print number line
        System.out.print("    ");
        for (int i = minVal; i <= maxVal; i++) {
            System.out.printf("%-3d", i);
        }
        System.out.println();
        
        // Print each interval as a bar
        for (int[] interval : intervals) {
            System.out.print("    ");
            for (int i = minVal; i <= maxVal; i++) {
                if (i >= interval[0] && i <= interval[1]) {
                    System.out.print("███");
                } else {
                    System.out.print("   ");
                }
            }
            System.out.println(" [" + interval[0] + "," + interval[1] + "]");
        }
        System.out.println();
    }
    
    // ─────────────────────────────────────────────
    // HELPER: Print intervals array
    // ─────────────────────────────────────────────
    public static void printIntervals(int[][] intervals) {
        System.out.print("[");
        for (int i = 0; i < intervals.length; i++) {
            System.out.print("[" + intervals[i][0] + "," + intervals[i][1] + "]");
            if (i < intervals.length - 1) System.out.print(", ");
        }
        System.out.println("]");
    }
    
    // ─────────────────────────────────────────────
    // MAIN
    // ─────────────────────────────────────────────
    public static void main(String[] args) {
        
        System.out.println("╔═════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 14: Merge Overlapping Intervals        ║");
        System.out.println("║  Pattern: Sort-then-Scan → Overlap Detection    ║");
        System.out.println("╚═════════════════════════════════════════════════╝");
        System.out.println();
        
        // ── TEST 1: Classic example with visualization ──
        System.out.println("═══ TEST 1: Classic Example ═══");
        int[][] test1 = {{1,3}, {2,6}, {8,10}, {15,18}};
        visualizeIntervals(test1, "BEFORE merge");
        traceExecution(test1);
        int[][] result1 = merge(new int[][] {{1,3}, {2,6}, {8,10}, {15,18}});
        visualizeIntervals(result1, "AFTER merge");
        
        // ── TEST 2: Touching intervals ──
        System.out.println("═══ TEST 2: Touching Intervals ═══");
        int[][] test2 = {{1,4}, {4,5}};
        traceExecution(test2);
        
        // ── TEST 3: Complete containment ──
        System.out.println("═══ TEST 3: Complete Containment ═══");
        int[][] test3 = {{1,4}, {2,3}};
        visualizeIntervals(test3, "BEFORE");
        traceExecution(test3);
        
        // ── TEST 4: Unsorted input ──
        System.out.println("═══ TEST 4: Unsorted Input ═══");
        int[][] test4 = {{6,8}, {1,3}, {2,4}, {9,12}};
        traceExecution(test4);
        
        // ── TEST 5: All overlapping (chain merge) ──
        System.out.println("═══ TEST 5: Chain Merge (All Overlapping) ═══");
        int[][] test5 = {{1,4}, {2,5}, {3,7}, {6,9}};
        visualizeIntervals(test5, "BEFORE");
        traceExecution(test5);
        int[][] result5 = merge(new int[][] {{1,4}, {2,5}, {3,7}, {6,9}});
        visualizeIntervals(result5, "AFTER");
        
        // ── TEST 6: No overlapping ──
        System.out.println("═══ TEST 6: No Overlapping ═══");
        int[][] test6 = {{1,2}, {4,5}, {7,8}};
        traceExecution(test6);
        
        // ── TEST 7: Edge cases ──
        System.out.println("═══ TEST 7: Edge Cases ═══");
        
        // Single interval
        int[][] single = {{5, 10}};
        System.out.print("  Single interval: ");
        printIntervals(merge(single));
        
        // All same intervals
        int[][] allSame = {{1,5}, {1,5}, {1,5}};
        System.out.print("  All same [1,5]x3: ");
        printIntervals(merge(allSame));
        
        // Nested intervals
        int[][] nested = {{1,10}, {2,5}, {3,7}, {4,6}};
        System.out.print("  Nested intervals: ");
        printIntervals(merge(nested));
        
        // Two separate groups
        int[][] twoGroups = {{1,3}, {2,4}, {8,10}, {9,11}};
        System.out.print("  Two groups: ");
        printIntervals(merge(twoGroups));
        System.out.println();
        
        // ── TEST 8: Variants ──
        System.out.println("═══ TEST 8: Variants ═══");
        
        int[][] variantInput = {{1,3}, {2,6}, {8,10}, {15,18}};
        
        System.out.println("  Count of merged intervals: " 
            + countMergedIntervals(
                new int[][] {{1,3}, {2,6}, {8,10}, {15,18}}));
        
        System.out.println("  Total covered length: " 
            + totalCoveredLength(
                new int[][] {{1,3}, {2,6}, {8,10}, {15,18}}));
        
        List<int[]> gaps = findGaps(new int[][] {{1,3}, {2,6}, {8,10}, {15,18}});
        System.out.print("  Gaps between intervals: ");
        System.out.print("[");
        for (int i = 0; i < gaps.size(); i++) {
            System.out.print("[" + gaps.get(i)[0] + "," + gaps.get(i)[1] + "]");
            if (i < gaps.size() - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println();
        
        // ── TEST 9: Correctness verification ──
        System.out.println("═══ TEST 9: Correctness Verification ═══");
        int[][][] testCases = {
            {{1,3}, {2,6}, {8,10}, {15,18}},
            {{1,4}, {4,5}},
            {{1,4}, {2,3}},
            {{1,4}, {0,4}},
            {{1,10}, {2,3}, {4,5}, {6,7}},
            {{1,2}, {3,4}, {5,6}},
            {{1,5}, {2,3}, {4,8}, {7,10}},
        };
        
        boolean allPassed = true;
        for (int[][] testCase : testCases) {
            int[][] optimal = merge(deepClone(testCase));
            int[][] brute = mergeBruteForce(deepClone(testCase));
            
            boolean pass = arraysEqual(optimal, brute);
            
            System.out.print("  ");
            printIntervalsInline(testCase);
            System.out.print(" → ");
            printIntervalsInline(optimal);
            System.out.println(" " + (pass ? "✓" : "✗"));
            
            if (!pass) {
                System.out.print("    Expected: ");
                printIntervalsInline(brute);
                System.out.println();
                allPassed = false;
            }
        }
        System.out.println("  All passed: " + allPassed);
        System.out.println();
        
        // ── TEST 10: Performance benchmark ──
        System.out.println("═══ TEST 10: Performance Benchmark ═══");
        int size = 1_000_000;
        int[][] largeInput = new int[size][2];
        java.util.Random rand = new java.util.Random(42);
        for (int i = 0; i < size; i++) {
            int start = rand.nextInt(10_000_000);
            int end = start + rand.nextInt(100);
            largeInput[i] = new int[] {start, end};
        }
        
        long start = System.nanoTime();
        int[][] largeResult = merge(largeInput);
        long time = System.nanoTime() - start;
        
        System.out.println("  Input intervals: " + size);
        System.out.println("  Merged intervals: " + largeResult.length);
        System.out.println("  Time: " + (time / 1_000_000) + " ms");
        System.out.println("  Reduction: " 
            + String.format("%.1f", (1 - (double) largeResult.length / size) * 100) 
            + "% intervals merged");
        System.out.println();
    }
    
    // ─────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────
    private static int[][] deepClone(int[][] arr) {
        int[][] clone = new int[arr.length][];
        for (int i = 0; i < arr.length; i++) {
            clone[i] = arr[i].clone();
        }
        return clone;
    }
    
    private static boolean arraysEqual(int[][] a, int[][] b) {
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++) {
            if (a[i][0] != b[i][0] || a[i][1] != b[i][1]) return false;
        }
        return true;
    }
    
    private static void printIntervalsInline(int[][] intervals) {
        System.out.print("[");
        for (int i = 0; i < intervals.length; i++) {
            System.out.print("[" + intervals[i][0] + "," + intervals[i][1] + "]");
            if (i < intervals.length - 1) System.out.print(",");
        }
        System.out.print("]");
    }
}