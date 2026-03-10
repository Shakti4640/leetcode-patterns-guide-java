import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivitySelection {

    // ─────────────────────────────────────────────
    // ACTIVITY REPRESENTATION
    // ─────────────────────────────────────────────
    static class Activity {
        int start;
        int end;
        int index;  // original index for reference

        Activity(int start, int end, int index) {
            this.start = start;
            this.end = end;
            this.index = index;
        }

        @Override
        public String toString() {
            return "[" + start + "," + end + "]";
        }
    }

    // ═══════════════════════════════════════════════
    // SOLUTION 1: GREEDY — Return count only
    // ═══════════════════════════════════════════════
    public static int maxActivities(int[][] intervals) {

        // Edge case
        if (intervals == null || intervals.length == 0) {
            return 0;
        }

        // Step 1: Sort by END TIME
        // If end times are equal → sort by start time (secondary)
        Arrays.sort(intervals, (a, b) -> {
            if (a[1] != b[1]) return a[1] - b[1];
            return a[0] - b[0];
        });

        // Step 2: Greedy selection
        int count = 1;                    // always pick the first activity
        int lastEndTime = intervals[0][1]; // track when last selected ends

        // Step 3: Scan remaining activities
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] >= lastEndTime) {
                // Compatible → select it
                count++;
                lastEndTime = intervals[i][1];
            }
            // else: overlaps → skip
        }

        return count;
    }

    // ═══════════════════════════════════════════════
    // SOLUTION 2: GREEDY — Return actual selected activities
    // ═══════════════════════════════════════════════
    public static List<int[]> selectActivities(int[][] intervals) {

        List<int[]> selected = new ArrayList<>();

        if (intervals == null || intervals.length == 0) {
            return selected;
        }

        // Create Activity objects to preserve original indices
        Activity[] activities = new Activity[intervals.length];
        for (int i = 0; i < intervals.length; i++) {
            activities[i] = new Activity(intervals[i][0], intervals[i][1], i);
        }

        // Sort by end time
        Arrays.sort(activities, (a, b) -> {
            if (a.end != b.end) return a.end - b.end;
            return a.start - b.start;
        });

        // Greedy selection
        selected.add(new int[] { activities[0].start, activities[0].end });
        int lastEndTime = activities[0].end;

        for (int i = 1; i < activities.length; i++) {
            if (activities[i].start >= lastEndTime) {
                selected.add(new int[] { activities[i].start, activities[i].end });
                lastEndTime = activities[i].end;
            }
        }

        return selected;
    }

    // ═══════════════════════════════════════════════
    // SOLUTION 3: BRUTE FORCE — For verification
    // (Try all subsets → check non-overlapping → find max count)
    // Only practical for small inputs (n ≤ 20)
    // ═══════════════════════════════════════════════
    public static int maxActivitiesBruteForce(int[][] intervals) {

        if (intervals == null || intervals.length == 0) {
            return 0;
        }

        int n = intervals.length;
        int maxCount = 0;

        // Try all 2^n subsets
        for (int mask = 0; mask < (1 << n); mask++) {

            // Collect selected activities for this subset
            List<int[]> subset = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    subset.add(intervals[i]);
                }
            }

            // Check if all selected activities are non-overlapping
            if (isNonOverlapping(subset)) {
                maxCount = Math.max(maxCount, subset.size());
            }
        }

        return maxCount;
    }

    private static boolean isNonOverlapping(List<int[]> activities) {
        for (int i = 0; i < activities.size(); i++) {
            for (int j = i + 1; j < activities.size(); j++) {
                // Two intervals overlap if: a.start < b.end AND b.start < a.end
                if (activities.get(i)[0] < activities.get(j)[1]
                        && activities.get(j)[0] < activities.get(i)[1]) {
                    return false;
                }
            }
        }
        return true;
    }

    // ═══════════════════════════════════════════════
    // TRACE VERSION — See the greedy decisions
    // ═══════════════════════════════════════════════
    public static void selectActivitiesWithTrace(int[][] intervals) {

        if (intervals == null || intervals.length == 0) {
            System.out.println("  No activities to select.");
            return;
        }

        // Show original
        System.out.println("  Original activities:");
        for (int i = 0; i < intervals.length; i++) {
            System.out.println("    Activity " + i + ": ["
                    + intervals[i][0] + ", " + intervals[i][1] + "]");
        }

        // Create copies for sorting (don't modify original)
        int[][] sorted = new int[intervals.length][2];
        for (int i = 0; i < intervals.length; i++) {
            sorted[i][0] = intervals[i][0];
            sorted[i][1] = intervals[i][1];
        }

        // Sort by end time
        Arrays.sort(sorted, (a, b) -> {
            if (a[1] != b[1]) return a[1] - b[1];
            return a[0] - b[0];
        });

        System.out.println("\n  After sorting by END TIME:");
        for (int i = 0; i < sorted.length; i++) {
            System.out.println("    [" + sorted[i][0] + ", " + sorted[i][1] + "]");
        }

        // Greedy scan
        System.out.println("\n  Greedy Selection Process:");
        System.out.println("  ─────────────────────────────────");

        List<int[]> selected = new ArrayList<>();
        selected.add(sorted[0]);
        int lastEnd = sorted[0][1];

        System.out.println("  ✓ SELECT [" + sorted[0][0] + ", " + sorted[0][1]
                + "] → first activity → lastEnd = " + lastEnd);

        for (int i = 1; i < sorted.length; i++) {
            int start = sorted[i][0];
            int end = sorted[i][1];

            if (start >= lastEnd) {
                selected.add(sorted[i]);
                lastEnd = end;
                System.out.println("  ✓ SELECT [" + start + ", " + end
                        + "] → start(" + start + ") >= lastEnd(" + (lastEnd - end + start)
                        + "... → actually start(" + start + ") >= prevLastEnd → compatible"
                        + " → new lastEnd = " + lastEnd);
            } else {
                System.out.println("  ✗ SKIP   [" + start + ", " + end
                        + "] → start(" + start + ") < lastEnd(" + lastEnd
                        + ") → OVERLAPS");
            }
        }

        System.out.println("\n  Result: " + selected.size() + " activities selected");
        System.out.print("  Selected: ");
        for (int[] act : selected) {
            System.out.print("[" + act[0] + "," + act[1] + "] ");
        }
        System.out.println();

        // Visual timeline
        printTimeline(intervals, selected);
    }

    // ─────────────────────────────────────────────
    // VISUAL TIMELINE PRINTER
    // ─────────────────────────────────────────────
    private static void printTimeline(int[][] all, List<int[]> selected) {

        // Find time range
        int maxTime = 0;
        for (int[] act : all) {
            maxTime = Math.max(maxTime, act[1]);
        }

        if (maxTime > 30) {
            maxTime = 30;  // cap for display
        }

        System.out.println("\n  Timeline Visualization:");
        System.out.print("  Time: ");
        for (int t = 0; t <= maxTime; t++) {
            System.out.printf("%-2d", t);
        }
        System.out.println();

        System.out.print("        ");
        for (int t = 0; t <= maxTime; t++) {
            System.out.print("| ");
        }
        System.out.println();

        // Print all activities
        for (int[] act : all) {
            boolean isSelected = false;
            for (int[] sel : selected) {
                if (sel[0] == act[0] && sel[1] == act[1]) {
                    isSelected = true;
                    break;
                }
            }

            System.out.print("  ");
            String label = isSelected ? "✓ " : "  ";
            System.out.printf("%-2s", label);
            System.out.print("[" + act[0] + "," + act[1] + "]");

            // Pad to align timeline
            int padding = 8 - ("[" + act[0] + "," + act[1] + "]").length();
            for (int p = 0; p < padding; p++) System.out.print(" ");

            for (int t = 0; t <= maxTime; t++) {
                if (t >= act[0] && t < act[1]) {
                    System.out.print(isSelected ? "██" : "░░");
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }

    // ═══════════════════════════════════════════════
    // COMPARISON: Why sorting by start time fails
    // ═══════════════════════════════════════════════
    public static int greedyByStartTime(int[][] intervals) {

        if (intervals == null || intervals.length == 0) {
            return 0;
        }

        int[][] sorted = intervals.clone();
        Arrays.sort(sorted, (a, b) -> a[0] - b[0]);  // sort by START

        int count = 1;
        int lastEnd = sorted[0][1];

        for (int i = 1; i < sorted.length; i++) {
            if (sorted[i][0] >= lastEnd) {
                count++;
                lastEnd = sorted[i][1];
            }
        }

        return count;
    }

    // ─────────────────────────────────────────────
    // MAIN — Run all tests
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 19: Activity Selection Problem            ║");
        System.out.println("║  Pattern: Greedy → Sort by End Time → Select Max   ║");
        System.out.println("╚════════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Classic example ──
        System.out.println("═══ TEST 1: Classic Example ═══");
        int[][] test1 = { {1,3}, {2,5}, {0,6}, {5,7}, {8,9}, {5,9} };
        selectActivitiesWithTrace(test1);
        System.out.println();

        // ── TEST 2: All compatible (chain) ──
        System.out.println("═══ TEST 2: All Compatible — Sequential Chain ═══");
        int[][] test2 = { {1,2}, {2,3}, {3,4}, {4,5} };
        selectActivitiesWithTrace(test2);
        System.out.println();

        // ── TEST 3: One long activity blocks others ──
        System.out.println("═══ TEST 3: Long Activity vs Many Short ═══");
        int[][] test3 = { {1,10}, {2,3}, {4,5}, {6,7} };
        selectActivitiesWithTrace(test3);
        System.out.println();

        // ── TEST 4: All overlapping ──
        System.out.println("═══ TEST 4: All Overlapping — Pick Only One ═══");
        int[][] test4 = { {1,5}, {2,6}, {3,7}, {4,8} };
        selectActivitiesWithTrace(test4);
        System.out.println();

        // ── TEST 5: Single activity ──
        System.out.println("═══ TEST 5: Single Activity ═══");
        int[][] test5 = { {3,7} };
        int result5 = maxActivities(test5);
        System.out.println("  Activities: [[3,7]]");
        System.out.println("  Max activities: " + result5);
        System.out.println();

        // ── TEST 6: Empty input ──
        System.out.println("═══ TEST 6: Empty Input ═══");
        int[][] test6 = {};
        int result6 = maxActivities(test6);
        System.out.println("  Activities: []");
        System.out.println("  Max activities: " + result6);
        System.out.println();

        // ── TEST 7: Verify greedy vs brute force ──
        System.out.println("═══ TEST 7: Greedy vs Brute Force Verification ═══");
        int[][][] testCases = {
            { {1,3}, {2,5}, {0,6}, {5,7}, {8,9}, {5,9} },
            { {1,2}, {2,3}, {3,4}, {4,5} },
            { {1,10}, {2,3}, {4,5}, {6,7} },
            { {1,5}, {2,6}, {3,7}, {4,8} },
            { {0,1}, {1,2}, {0,2}, {2,3}, {1,3}, {3,4} },
            { {1,4}, {2,3}, {3,5}, {0,6}, {5,7}, {3,8}, {5,9}, {6,10},
              {8,11}, {8,12}, {2,13}, {12,14} }
        };

        boolean allMatch = true;
        for (int t = 0; t < testCases.length; t++) {
            int greedy = maxActivities(testCases[t]);
            int brute = maxActivitiesBruteForce(testCases[t]);
            boolean match = (greedy == brute);
            System.out.println("  Test " + (t + 1) + ": Greedy=" + greedy
                    + " BruteForce=" + brute
                    + (match ? " ✓" : " ✗ MISMATCH"));
            if (!match) allMatch = false;
        }
        System.out.println("  All match: " + allMatch);
        System.out.println();

        // ── TEST 8: Why sort-by-start-time fails ──
        System.out.println("═══ TEST 8: Sort by Start Time vs End Time ═══");
        int[][] test8 = { {1,10}, {2,3}, {4,5}, {6,7} };
        int byEnd = maxActivities(test8);
        int byStart = greedyByStartTime(test8);
        System.out.println("  Activities: [[1,10], [2,3], [4,5], [6,7]]");
        System.out.println("  Greedy by END time:   " + byEnd + " ← CORRECT");
        System.out.println("  Greedy by START time: " + byStart + " ← WRONG");
        System.out.println();

        // ── TEST 9: Boundary — end equals start ──
        System.out.println("═══ TEST 9: Boundary — End Meets Start ═══");
        int[][] test9 = { {1,3}, {3,5}, {5,7} };
        selectActivitiesWithTrace(test9);
        System.out.println();

        // ── TEST 10: Performance test ──
        System.out.println("═══ TEST 10: Performance — 1,000,000 Activities ═══");
        int n = 1_000_000;
        int[][] largTest = new int[n][2];
        for (int i = 0; i < n; i++) {
            largTest[i][0] = i;
            largTest[i][1] = i + 1 + (i % 3);  // varying lengths
        }

        long startTime = System.nanoTime();
        int largeResult = maxActivities(largTest);
        long elapsed = System.nanoTime() - startTime;

        System.out.println("  Activities: " + n);
        System.out.println("  Max non-overlapping: " + largeResult);
        System.out.println("  Time: " + (elapsed / 1_000_000) + " ms");
    }
}