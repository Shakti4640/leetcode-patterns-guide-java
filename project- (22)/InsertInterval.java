import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InsertInterval {

    // ═══════════════════════════════════════════════
    // SOLUTION 1: THREE-PHASE SINGLE PASS
    // O(n) time, O(n) space for result
    // ═══════════════════════════════════════════════
    public static int[][] insert(int[][] intervals, int[] newInterval) {

        List<int[]> result = new ArrayList<>();
        int i = 0;
        int n = intervals.length;
        int newStart = newInterval[0];
        int newEnd = newInterval[1];

        // ═══ PHASE A: Add all intervals BEFORE the new interval ═══
        // Condition: existing interval ends before new interval starts
        while (i < n && intervals[i][1] < newStart) {
            result.add(intervals[i]);
            i++;
        }

        // ═══ PHASE B: Merge all OVERLAPPING intervals with new interval ═══
        // Condition: existing interval starts before or at new interval's end
        while (i < n && intervals[i][0] <= newEnd) {
            newStart = Math.min(newStart, intervals[i][0]);
            newEnd = Math.max(newEnd, intervals[i][1]);
            i++;
        }

        // Add the merged interval
        result.add(new int[] { newStart, newEnd });

        // ═══ PHASE C: Add all intervals AFTER the merged interval ═══
        while (i < n) {
            result.add(intervals[i]);
            i++;
        }

        return result.toArray(new int[result.size()][]);
    }

    // ═══════════════════════════════════════════════
    // SOLUTION 2: BRUTE FORCE — Add then use Project 14 merge
    // O(n log n) time — for verification
    // ═══════════════════════════════════════════════
    public static int[][] insertBruteForce(int[][] intervals, int[] newInterval) {

        // Create new array with the new interval added
        int[][] combined = new int[intervals.length + 1][2];
        for (int i = 0; i < intervals.length; i++) {
            combined[i] = intervals[i];
        }
        combined[intervals.length] = newInterval;

        // Sort by start time (Project 14 approach)
        Arrays.sort(combined, (a, b) -> a[0] - b[0]);

        // Merge overlapping intervals (Project 14 logic)
        List<int[]> result = new ArrayList<>();
        result.add(combined[0]);

        for (int i = 1; i < combined.length; i++) {
            int[] last = result.get(result.size() - 1);
            if (combined[i][0] <= last[1]) {
                // Overlap → merge
                last[1] = Math.max(last[1], combined[i][1]);
            } else {
                // No overlap → add new
                result.add(combined[i]);
            }
        }

        return result.toArray(new int[result.size()][]);
    }

    // ═══════════════════════════════════════════════
    // TRACE VERSION — Full visualization
    // ═══════════════════════════════════════════════
    public static int[][] insertWithTrace(int[][] intervals, int[] newInterval) {

        List<int[]> result = new ArrayList<>();
        int i = 0;
        int n = intervals.length;
        int newStart = newInterval[0];
        int newEnd = newInterval[1];

        System.out.println("  Existing intervals: " + formatIntervals(intervals));
        System.out.println("  New interval: [" + newStart + ", " + newEnd + "]");
        System.out.println();

        // ═══ PHASE A ═══
        System.out.println("  ═══ PHASE A: Collect intervals BEFORE [" 
                + newStart + "," + newEnd + "] ═══");
        System.out.println("  Condition: existing.end < newStart(" + newStart + ")");

        int phaseACount = 0;
        while (i < n && intervals[i][1] < newStart) {
            System.out.println("    [" + intervals[i][0] + "," + intervals[i][1]
                    + "] → end=" + intervals[i][1] + " < " + newStart
                    + " → BEFORE → add to result");
            result.add(intervals[i]);
            i++;
            phaseACount++;
        }

        if (phaseACount == 0) {
            System.out.println("    (no intervals before new interval)");
        }

        if (i < n) {
            System.out.println("    [" + intervals[i][0] + "," + intervals[i][1]
                    + "] → end=" + intervals[i][1] + " ≥ " + newStart
                    + " → STOP Phase A");
        } else {
            System.out.println("    (reached end of intervals)");
        }

        System.out.println("  Result after Phase A: " + formatList(result));
        System.out.println();

        // ═══ PHASE B ═══
        System.out.println("  ═══ PHASE B: Merge OVERLAPPING intervals ═══");
        System.out.println("  Condition: existing.start ≤ newEnd");

        int phaseBCount = 0;
        while (i < n && intervals[i][0] <= newEnd) {
            int oldStart = newStart;
            int oldEnd = newEnd;
            newStart = Math.min(newStart, intervals[i][0]);
            newEnd = Math.max(newEnd, intervals[i][1]);

            System.out.println("    [" + intervals[i][0] + "," + intervals[i][1]
                    + "] → start=" + intervals[i][0] + " ≤ " + oldEnd
                    + " → OVERLAP");
            System.out.println("      Merge: [" + oldStart + "," + oldEnd
                    + "] + [" + intervals[i][0] + "," + intervals[i][1]
                    + "] → [" + newStart + "," + newEnd + "]");
            i++;
            phaseBCount++;
        }

        if (phaseBCount == 0) {
            System.out.println("    (no overlapping intervals — new interval fits in a gap)");
        }

        if (i < n) {
            System.out.println("    [" + intervals[i][0] + "," + intervals[i][1]
                    + "] → start=" + intervals[i][0] + " > " + newEnd
                    + " → STOP Phase B");
        }

        result.add(new int[] { newStart, newEnd });
        System.out.println("  ★ Add merged interval: [" + newStart + "," + newEnd + "]");
        System.out.println("  Result after Phase B: " + formatList(result));
        System.out.println();

        // ═══ PHASE C ═══
        System.out.println("  ═══ PHASE C: Collect intervals AFTER merged ═══");

        int phaseCCount = 0;
        while (i < n) {
            System.out.println("    [" + intervals[i][0] + "," + intervals[i][1]
                    + "] → AFTER → add to result");
            result.add(intervals[i]);
            i++;
            phaseCCount++;
        }

        if (phaseCCount == 0) {
            System.out.println("    (no intervals after merged interval)");
        }

        int[][] finalResult = result.toArray(new int[result.size()][]);
        System.out.println();
        System.out.println("  ★ FINAL RESULT: " + formatIntervals(finalResult));

        return finalResult;
    }

    // ─────────────────────────────────────────────
    // VISUAL TIMELINE PRINTER
    // ─────────────────────────────────────────────
    public static void printTimeline(int[][] intervals, int[] newInterval, int[][] merged) {

        // Find time range
        int maxTime = 0;
        for (int[] iv : intervals) maxTime = Math.max(maxTime, iv[1]);
        if (newInterval != null) maxTime = Math.max(maxTime, newInterval[1]);
        if (merged != null) {
            for (int[] iv : merged) maxTime = Math.max(maxTime, iv[1]);
        }

        maxTime = Math.min(maxTime + 1, 25);  // cap for display

        // Header
        System.out.print("  Time: ");
        for (int t = 0; t <= maxTime; t++) System.out.printf("%-2d", t);
        System.out.println();

        // Existing intervals
        for (int[] iv : intervals) {
            System.out.printf("  [%d,%d] ", iv[0], iv[1]);
            int padding = 6 - String.format("[%d,%d]", iv[0], iv[1]).length();
            for (int p = 0; p < padding; p++) System.out.print(" ");
            for (int t = 0; t <= maxTime; t++) {
                System.out.print(t >= iv[0] && t < iv[1] ? "░░" : "  ");
            }
            System.out.println();
        }

        // New interval
        if (newInterval != null) {
            System.out.printf("  NEW    ");
            for (int t = 0; t <= maxTime; t++) {
                System.out.print(t >= newInterval[0] && t < newInterval[1] ? "▓▓" : "  ");
            }
            System.out.println("  ← [" + newInterval[0] + "," + newInterval[1] + "]");
        }

        // Merged result
        if (merged != null) {
            System.out.println("  ──────────────────────────────────────");
            for (int[] iv : merged) {
                System.out.printf("  [%d,%d] ", iv[0], iv[1]);
                int padding = 6 - String.format("[%d,%d]", iv[0], iv[1]).length();
                for (int p = 0; p < padding; p++) System.out.print(" ");
                for (int t = 0; t <= maxTime; t++) {
                    System.out.print(t >= iv[0] && t < iv[1] ? "██" : "  ");
                }
                System.out.println();
            }
        }
    }

    // ─────────────────────────────────────────────
    // FORMATTING HELPERS
    // ─────────────────────────────────────────────
    private static String formatIntervals(int[][] intervals) {
        if (intervals.length == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < intervals.length; i++) {
            sb.append("[").append(intervals[i][0]).append(",").append(intervals[i][1]).append("]");
            if (i < intervals.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    private static String formatList(List<int[]> list) {
        if (list.isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append("[").append(list.get(i)[0]).append(",").append(list.get(i)[1]).append("]");
            if (i < list.size() - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    // ─────────────────────────────────────────────
    // MAIN — Run all tests
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 22: Insert a New Interval into Sorted Intervals ║");
        System.out.println("║  Pattern: Merge Intervals → Three-Region Split           ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Basic overlap ──
        System.out.println("═══ TEST 1: Basic Overlap ═══");
        int[][] t1 = {{1,3}, {6,9}};
        int[] n1 = {2,5};
        int[][] r1 = insertWithTrace(t1, n1);
        System.out.println();
        printTimeline(t1, n1, r1);
        System.out.println();

        // ── TEST 2: Multiple merges ──
        System.out.println("═══ TEST 2: Multiple Merges ═══");
        int[][] t2 = {{1,2}, {3,5}, {6,7}, {8,10}, {12,16}};
        int[] n2 = {4,8};
        int[][] r2 = insertWithTrace(t2, n2);
        System.out.println();
        printTimeline(t2, n2, r2);
        System.out.println();

        // ── TEST 3: No overlap — insert after ──
        System.out.println("═══ TEST 3: No Overlap — Insert After ═══");
        int[][] t3 = {{1,5}};
        int[] n3 = {6,8};
        int[][] r3 = insertWithTrace(t3, n3);
        System.out.println();

        // ── TEST 4: No overlap — insert before ──
        System.out.println("═══ TEST 4: No Overlap — Insert Before ═══");
        int[][] t4 = {{3,5}, {7,9}};
        int[] n4 = {0,1};
        int[][] r4 = insertWithTrace(t4, n4);
        System.out.println();

        // ── TEST 5: Insert into gap ──
        System.out.println("═══ TEST 5: Insert Into Gap (No Overlap) ═══");
        int[][] t5 = {{1,3}, {8,10}};
        int[] n5 = {5,6};
        int[][] r5 = insertWithTrace(t5, n5);
        System.out.println();

        // ── TEST 6: New interval swallows everything ──
        System.out.println("═══ TEST 6: New Interval Swallows Everything ═══");
        int[][] t6 = {{2,3}, {4,5}, {6,7}};
        int[] n6 = {1,10};
        int[][] r6 = insertWithTrace(t6, n6);
        System.out.println();

        // ── TEST 7: Empty existing intervals ──
        System.out.println("═══ TEST 7: Empty Existing Intervals ═══");
        int[][] t7 = {};
        int[] n7 = {5,7};
        int[][] r7 = insertWithTrace(t7, n7);
        System.out.println();

        // ── TEST 8: Touching intervals ──
        System.out.println("═══ TEST 8: Touching Intervals (Boundary) ═══");
        int[][] t8 = {{1,3}, {5,7}};
        int[] n8 = {3,5};
        int[][] r8 = insertWithTrace(t8, n8);
        System.out.println();

        // ── TEST 9: New interval inside existing ──
        System.out.println("═══ TEST 9: New Interval Inside Existing ═══");
        int[][] t9 = {{1,10}};
        int[] n9 = {3,5};
        int[][] r9 = insertWithTrace(t9, n9);
        System.out.println();

        // ── TEST 10: Verify both approaches agree ──
        System.out.println("═══ TEST 10: Verification — Optimal vs Brute Force ═══");
        int[][][] existingCases = {
            {{1,3}, {6,9}},
            {{1,2}, {3,5}, {6,7}, {8,10}, {12,16}},
            {{1,5}},
            {{3,5}, {7,9}},
            {{1,3}, {8,10}},
            {{2,3}, {4,5}, {6,7}},
            {},
            {{1,3}, {5,7}},
            {{1,10}}
        };
        int[][] newCases = {
            {2,5}, {4,8}, {6,8}, {0,1}, {5,6},
            {1,10}, {5,7}, {3,5}, {3,5}
        };

        boolean allMatch = true;
        for (int t = 0; t < existingCases.length; t++) {
            int[][] optimal = insert(existingCases[t], newCases[t]);
            int[][] brute = insertBruteForce(existingCases[t], newCases[t]);

            boolean match = Arrays.deepEquals(optimal, brute);
            System.out.println("  Test " + (t + 1)
                    + ": Optimal=" + formatIntervals(optimal)
                    + (match ? " ✓" : " ✗ Brute=" + formatIntervals(brute)));
            if (!match) allMatch = false;
        }
        System.out.println("  All match: " + allMatch);
    }
}