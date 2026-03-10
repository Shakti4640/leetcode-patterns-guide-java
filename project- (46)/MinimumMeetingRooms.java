import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.List;

public class MinimumMeetingRooms {

    // ─────────────────────────────────────────────────────────
    // APPROACH A: Min-Heap of End Times (Interview Standard)
    // ─────────────────────────────────────────────────────────
    public static int minMeetingRooms(int[][] intervals) {
        
        // Edge case: no meetings → 0 rooms
        if (intervals == null || intervals.length == 0) {
            return 0;
        }
        
        // Step 1: Sort by START time ascending
        //   → Process meetings in chronological order
        //   → Ensures heap reflects true state of room usage
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        
        // Step 2: Min-heap stores END TIMES of active meetings
        //   → Top of heap = earliest ending room
        //   → Heap size = number of rooms currently in use
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        
        // Step 3: Process each meeting in start-time order
        for (int[] meeting : intervals) {
            int start = meeting[0];
            int end = meeting[1];
            
            // Can we reuse the earliest-ending room?
            //   → If that room's end time <= current meeting's start
            //   → Room is free → reuse it (poll removes it from heap)
            if (!minHeap.isEmpty() && minHeap.peek() <= start) {
                minHeap.poll();
            }
            
            // Occupy a room (either reused or newly allocated)
            //   → Push this meeting's end time
            minHeap.offer(end);
        }
        
        // Step 4: Heap size = total rooms allocated = answer
        //   → Size only grows or stays same (never shrinks below previous)
        //   → Final size = peak concurrent rooms
        return minHeap.size();
    }

    // ─────────────────────────────────────────────────────────
    // APPROACH B: Event Timeline (Chronological Ordering)
    // ─────────────────────────────────────────────────────────
    public static int minMeetingRoomsTimeline(int[][] intervals) {
        
        if (intervals == null || intervals.length == 0) {
            return 0;
        }
        
        int n = intervals.length;
        
        // Step 1: Separate starts and ends into two arrays
        int[] starts = new int[n];
        int[] ends = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = intervals[i][0];
            ends[i] = intervals[i][1];
        }
        
        // Step 2: Sort BOTH arrays independently
        //   → We don't care which start pairs with which end
        //   → We only care about the CHRONOLOGICAL sequence of events
        Arrays.sort(starts);
        Arrays.sort(ends);
        
        // Step 3: Two-pointer sweep
        //   → sPtr walks through starts
        //   → ePtr walks through ends
        //   → When a start comes before an end → room needed → rooms++
        //   → When an end comes → room freed → rooms-- and advance ePtr
        int sPtr = 0;
        int ePtr = 0;
        int roomsInUse = 0;
        int maxRooms = 0;
        
        while (sPtr < n) {
            if (starts[sPtr] < ends[ePtr]) {
                // A meeting starts before the next one ends
                //   → Need an additional room
                roomsInUse++;
                sPtr++;
            } else {
                // A meeting ends before/at the next start
                //   → A room is freed
                roomsInUse--;
                ePtr++;
            }
            maxRooms = Math.max(maxRooms, roomsInUse);
        }
        
        return maxRooms;
    }

    // ─────────────────────────────────────────────────────────
    // APPROACH C: Brute Force — Sweep Line with Array
    //   (for understanding only — NOT for interviews)
    // ─────────────────────────────────────────────────────────
    public static int minMeetingRoomsBrute(int[][] intervals) {
        
        if (intervals == null || intervals.length == 0) {
            return 0;
        }
        
        // Find the time range
        int maxTime = 0;
        for (int[] interval : intervals) {
            maxTime = Math.max(maxTime, interval[1]);
        }
        
        // Count overlap at each time point
        //   → Difference array technique
        int[] timeline = new int[maxTime + 2];
        for (int[] interval : intervals) {
            timeline[interval[0]]++;    // meeting starts: +1
            timeline[interval[1]]--;    // meeting ends: -1
        }
        
        // Prefix sum to get concurrent meetings at each time
        int maxRooms = 0;
        int current = 0;
        for (int t = 0; t <= maxTime; t++) {
            current += timeline[t];
            maxRooms = Math.max(maxRooms, current);
        }
        
        return maxRooms;
    }

    // ─────────────────────────────────────────────────────────
    // TRACE FUNCTION — Visualize the Min-Heap Approach
    // ─────────────────────────────────────────────────────────
    public static void minMeetingRoomsWithTrace(int[][] intervals) {
        
        if (intervals == null || intervals.length == 0) {
            System.out.println("No meetings → 0 rooms needed");
            return;
        }
        
        System.out.println("Original meetings:");
        for (int[] m : intervals) {
            System.out.println("  [" + m[0] + ", " + m[1] + ")");
        }
        
        // Sort by start time
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        
        System.out.println("\nSorted by start time:");
        for (int[] m : intervals) {
            System.out.println("  [" + m[0] + ", " + m[1] + ")");
        }
        System.out.println("\n─── Processing ───");
        
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        int step = 1;
        
        for (int[] meeting : intervals) {
            int start = meeting[0];
            int end = meeting[1];
            
            System.out.println("\nStep " + step + ": Meeting [" + start + ", " + end + ")");
            System.out.println("  Heap before: " + heapToString(minHeap));
            
            if (!minHeap.isEmpty() && minHeap.peek() <= start) {
                int freed = minHeap.poll();
                System.out.println("  → Earliest end = " + freed 
                    + " ≤ start " + start + " → REUSE room (poll " + freed + ")");
            } else if (!minHeap.isEmpty()) {
                System.out.println("  → Earliest end = " + minHeap.peek() 
                    + " > start " + start + " → All rooms busy → NEW room");
            } else {
                System.out.println("  → Heap empty → First room allocated");
            }
            
            minHeap.offer(end);
            System.out.println("  → Push end time " + end);
            System.out.println("  Heap after: " + heapToString(minHeap));
            System.out.println("  Rooms in use: " + minHeap.size());
            
            step++;
        }
        
        System.out.println("\n═══ ANSWER: " + minHeap.size() + " rooms needed ═══");
    }
    
    // ─────────────────────────────────────────────────────────
    // TRACE FUNCTION — Visualize the Event Timeline Approach
    // ─────────────────────────────────────────────────────────
    public static void minMeetingRoomsTimelineWithTrace(int[][] intervals) {
        
        if (intervals == null || intervals.length == 0) {
            System.out.println("No meetings → 0 rooms needed");
            return;
        }
        
        int n = intervals.length;
        int[] starts = new int[n];
        int[] ends = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = intervals[i][0];
            ends[i] = intervals[i][1];
        }
        
        Arrays.sort(starts);
        Arrays.sort(ends);
        
        System.out.println("Sorted starts: " + Arrays.toString(starts));
        System.out.println("Sorted ends:   " + Arrays.toString(ends));
        System.out.println("\n─── Sweeping ───");
        
        int sPtr = 0;
        int ePtr = 0;
        int roomsInUse = 0;
        int maxRooms = 0;
        int step = 1;
        
        while (sPtr < n) {
            System.out.println("\nStep " + step + ":");
            System.out.println("  sPtr=" + sPtr + " (start=" + starts[sPtr] 
                + ")  ePtr=" + ePtr + " (end=" + ends[ePtr] + ")");
            
            if (starts[sPtr] < ends[ePtr]) {
                roomsInUse++;
                System.out.println("  → start " + starts[sPtr] + " < end " 
                    + ends[ePtr] + " → meeting starts → rooms=" + roomsInUse);
                sPtr++;
            } else {
                roomsInUse--;
                System.out.println("  → start " + starts[sPtr] + " ≥ end " 
                    + ends[ePtr] + " → meeting ends → rooms=" + roomsInUse);
                ePtr++;
            }
            
            maxRooms = Math.max(maxRooms, roomsInUse);
            System.out.println("  Max rooms so far: " + maxRooms);
            step++;
        }
        
        System.out.println("\n═══ ANSWER: " + maxRooms + " rooms needed ═══");
    }
    
    // Helper: Print heap contents
    private static String heapToString(PriorityQueue<Integer> heap) {
        if (heap.isEmpty()) return "[]";
        // Copy to avoid modifying original
        List<Integer> items = new ArrayList<>(heap);
        items.sort(null);
        return items.toString();
    }

    // ─────────────────────────────────────────────────────────
    // MAIN — Run all examples
    // ─────────────────────────────────────────────────────────
    public static void main(String[] args) {
        
        System.out.println("╔═══════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 46: Minimum Meeting Rooms Required   ║");
        System.out.println("║  Pattern: Intervals + Min-Heap Overlap Count  ║");
        System.out.println("╚═══════════════════════════════════════════════╝");
        System.out.println();
        
        // ── TEST 1: Classic example ──
        System.out.println("═══ TEST 1: Classic Example (Heap Approach) ═══");
        int[][] test1 = {{0, 30}, {5, 10}, {15, 20}};
        minMeetingRoomsWithTrace(test1);
        System.out.println();
        
        // ── TEST 2: No overlap ──
        System.out.println("═══ TEST 2: No Overlap ═══");
        int[][] test2 = {{7, 10}, {2, 4}};
        minMeetingRoomsWithTrace(test2);
        System.out.println();
        
        // ── TEST 3: Full overlap ──
        System.out.println("═══ TEST 3: Full Overlap — All Concurrent ═══");
        int[][] test3 = {{1, 5}, {2, 6}, {3, 7}, {4, 8}};
        minMeetingRoomsWithTrace(test3);
        System.out.println();
        
        // ── TEST 4: Chain — sequential meetings ──
        System.out.println("═══ TEST 4: Chain — Sequential Meetings ═══");
        int[][] test4 = {{0, 5}, {5, 10}, {10, 15}, {15, 20}};
        minMeetingRoomsWithTrace(test4);
        System.out.println();
        
        // ── TEST 5: Timeline approach ──
        System.out.println("═══ TEST 5: Timeline Approach ═══");
        int[][] test5 = {{0, 30}, {5, 10}, {15, 20}};
        minMeetingRoomsTimelineWithTrace(test5);
        System.out.println();
        
        // ── TEST 6: Complex scenario ──
        System.out.println("═══ TEST 6: Complex Scenario ═══");
        int[][] test6 = {{1, 10}, {2, 7}, {3, 19}, {8, 12}, {10, 20}, {11, 30}};
        minMeetingRoomsWithTrace(test6);
        System.out.println();
        
        // ── TEST 7: Verify all three approaches agree ──
        System.out.println("═══ TEST 7: Cross-Verification ═══");
        int[][][] allTests = {
            {{0, 30}, {5, 10}, {15, 20}},
            {{7, 10}, {2, 4}},
            {{1, 5}, {2, 6}, {3, 7}, {4, 8}},
            {{0, 5}, {5, 10}, {10, 15}, {15, 20}},
            {{1, 10}, {2, 7}, {3, 19}, {8, 12}, {10, 20}, {11, 30}},
            {{9, 10}, {4, 9}, {4, 17}},
            {{2, 15}, {36, 45}, {9, 29}, {16, 23}, {4, 9}}
        };
        
        for (int t = 0; t < allTests.length; t++) {
            // Deep copy because sorting modifies array
            int[][] copyA = deepCopy(allTests[t]);
            int[][] copyB = deepCopy(allTests[t]);
            int[][] copyC = deepCopy(allTests[t]);
            
            int heapResult = minMeetingRooms(copyA);
            int timelineResult = minMeetingRoomsTimeline(copyB);
            int bruteResult = minMeetingRoomsBrute(copyC);
            
            String status = (heapResult == timelineResult && timelineResult == bruteResult) 
                ? "✓ MATCH" : "✗ MISMATCH";
            
            System.out.println("  Test " + (t + 1) 
                + ": Heap=" + heapResult 
                + "  Timeline=" + timelineResult 
                + "  Brute=" + bruteResult 
                + "  " + status);
        }
        System.out.println();
        
        // ── TEST 8: Performance benchmark ──
        System.out.println("═══ TEST 8: Performance Benchmark ═══");
        int size = 500000;
        int[][] largeMeetings = new int[size][2];
        java.util.Random rand = new java.util.Random(42);
        for (int i = 0; i < size; i++) {
            int s = rand.nextInt(1000000);
            int e = s + 1 + rand.nextInt(100);
            largeMeetings[i] = new int[]{s, e};
        }
        
        int[][] copyHeap = deepCopy(largeMeetings);
        int[][] copyTimeline = deepCopy(largeMeetings);
        
        long startTime = System.nanoTime();
        int result1 = minMeetingRooms(copyHeap);
        long heapTime = System.nanoTime() - startTime;
        
        startTime = System.nanoTime();
        int result2 = minMeetingRoomsTimeline(copyTimeline);
        long timelineTime = System.nanoTime() - startTime;
        
        System.out.println("  n = " + size);
        System.out.println("  Heap approach:     " + result1 
            + " rooms → " + (heapTime / 1_000_000) + " ms");
        System.out.println("  Timeline approach:  " + result2 
            + " rooms → " + (timelineTime / 1_000_000) + " ms");
    }
    
    // Helper: Deep copy 2D array
    private static int[][] deepCopy(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }
}