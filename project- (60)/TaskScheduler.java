import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class TaskScheduler {

    // ─────────────────────────────────────────────────────────────
    // APPROACH 1: MATHEMATICAL FORMULA — O(N) time, O(1) space
    // The optimal solution — derive answer directly
    // ─────────────────────────────────────────────────────────────
    public static int leastInterval(char[] tasks, int n) {
        // Step 1: Count frequency of each task
        int[] freq = new int[26];
        for (char task : tasks) {
            freq[task - 'A']++;
        }

        // Step 2: Find the maximum frequency
        int maxFreq = 0;
        for (int f : freq) {
            maxFreq = Math.max(maxFreq, f);
        }

        // Step 3: Count how many tasks have the maximum frequency
        int maxCount = 0;
        for (int f : freq) {
            if (f == maxFreq) {
                maxCount++;
            }
        }

        // Step 4: Compute frame size and return answer
        // Frame = (maxFreq - 1) gaps of size (n+1) + last chunk of maxCount
        int frameSize = (maxFreq - 1) * (n + 1) + maxCount;

        // Answer = max(frameSize, totalTasks)
        // Because we can't have fewer time units than total tasks
        return Math.max(frameSize, tasks.length);
    }

    // ─────────────────────────────────────────────────────────────
    // APPROACH 2: GREEDY HEAP SIMULATION — O(N) time, O(1) space
    // Process in rounds of (n+1) using max-heap
    // ─────────────────────────────────────────────────────────────
    public static int leastIntervalHeap(char[] tasks, int n) {
        // Step 1: Count frequencies
        int[] freq = new int[26];
        for (char task : tasks) {
            freq[task - 'A']++;
        }

        // Step 2: Build max-heap of frequencies
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        for (int f : freq) {
            if (f > 0) {
                maxHeap.offer(f);
            }
        }

        // Step 3: Process in rounds
        int totalTime = 0;

        while (!maxHeap.isEmpty()) {
            List<Integer> temp = new ArrayList<>();
            int tasksExecuted = 0;

            // Each round has (n+1) slots
            for (int slot = 0; slot <= n; slot++) {
                if (!maxHeap.isEmpty()) {
                    int currentFreq = maxHeap.poll();
                    tasksExecuted++;

                    if (currentFreq - 1 > 0) {
                        temp.add(currentFreq - 1);
                    }
                }
            }

            // Put remaining frequencies back into heap
            maxHeap.addAll(temp);

            // If heap is empty: this was the last round → count only tasks
            // If heap still has tasks: count full round (n+1) including idle
            totalTime += maxHeap.isEmpty() ? tasksExecuted : (n + 1);
        }

        return totalTime;
    }

    // ─────────────────────────────────────────────────────────────
    // APPROACH 3: DETAILED SIMULATION — Shows actual schedule
    // O(answer) time — slower but produces the schedule
    // ─────────────────────────────────────────────────────────────
    public static String[] buildSchedule(char[] tasks, int n) {
        // Count frequencies with task labels
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char task : tasks) {
            freqMap.merge(task, 1, Integer::sum);
        }

        // Max-heap of (frequency, task character)
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>(
            (a, b) -> b[0] != a[0] ? b[0] - a[0] : a[1] - b[1]
        );
        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            maxHeap.offer(new int[]{entry.getValue(), entry.getKey()});
        }

        List<String> schedule = new ArrayList<>();

        while (!maxHeap.isEmpty()) {
            List<int[]> temp = new ArrayList<>();
            int slotsUsed = 0;

            for (int slot = 0; slot <= n; slot++) {
                if (maxHeap.isEmpty() && temp.isEmpty()) break;

                if (!maxHeap.isEmpty()) {
                    int[] current = maxHeap.poll();
                    schedule.add(String.valueOf((char) current[1]));
                    current[0]--;
                    if (current[0] > 0) {
                        temp.add(current);
                    }
                    slotsUsed++;
                } else {
                    // No task available but still in cooldown period
                    // AND there are tasks waiting (temp is not empty)
                    if (!temp.isEmpty()) {
                        schedule.add("idle");
                        slotsUsed++;
                    }
                }
            }

            maxHeap.addAll(temp);
        }

        return schedule.toArray(new String[0]);
    }

    // ─────────────────────────────────────────────────────────────
    // TRACE: Formula approach — Show the frame structure
    // ─────────────────────────────────────────────────────────────
    public static void traceFormula(char[] tasks, int n) {
        // Count frequencies
        int[] freq = new int[26];
        for (char task : tasks) {
            freq[task - 'A']++;
        }

        System.out.println("  Tasks: " + new String(tasks));
        System.out.println("  Cooldown n = " + n);
        System.out.println();

        // Show frequencies
        System.out.println("  ── Frequency Count ──");
        List<int[]> sortedFreqs = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            if (freq[i] > 0) {
                sortedFreqs.add(new int[]{freq[i], i});
                System.out.println("    " + (char)('A' + i)
                    + ": " + freq[i]);
            }
        }
        sortedFreqs.sort((a, b) -> b[0] - a[0]);
        System.out.println();

        // Find maxFreq and maxCount
        int maxFreq = 0;
        for (int f : freq) maxFreq = Math.max(maxFreq, f);

        int maxCount = 0;
        StringBuilder maxTasks = new StringBuilder();
        for (int i = 0; i < 26; i++) {
            if (freq[i] == maxFreq) {
                maxCount++;
                maxTasks.append((char)('A' + i)).append(" ");
            }
        }

        System.out.println("  ── Key Values ──");
        System.out.println("    maxFreq = " + maxFreq
            + " (tasks: " + maxTasks.toString().trim() + ")");
        System.out.println("    maxCount = " + maxCount);
        System.out.println("    totalTasks = " + tasks.length);
        System.out.println();

        // Show frame structure
        System.out.println("  ── Frame Structure ──");
        int gaps = maxFreq - 1;
        int chunkSize = n + 1;
        int frameSize = gaps * chunkSize + maxCount;

        System.out.println("    gaps = maxFreq - 1 = " + gaps);
        System.out.println("    chunk size = n + 1 = " + chunkSize);
        System.out.println("    frameSize = " + gaps + " × " + chunkSize
            + " + " + maxCount + " = " + frameSize);
        System.out.println();

        // Visualize the frame
        System.out.println("  ── Frame Visualization ──");
        System.out.print("    ");
        // Build frame with task letters
        char[][] frame = new char[maxFreq][chunkSize];
        for (char[] row : frame) Arrays.fill(row, '_');

        // Place max-frequency tasks
        int col = 0;
        for (int[] sf : sortedFreqs) {
            int f = sf[0];
            char task = (char)('A' + sf[1]);
            for (int row = 0; row < f; row++) {
                if (row < maxFreq && col < chunkSize) {
                    frame[row][col] = task;
                }
            }
            col++;
            if (col >= chunkSize) break;
        }

        // Print frame
        for (int row = 0; row < maxFreq; row++) {
            System.out.print("    ");
            int printCols = (row == maxFreq - 1) ? maxCount : chunkSize;
            for (int c = 0; c < printCols; c++) {
                System.out.print(frame[row][c] + " ");
            }
            if (row < maxFreq - 1) System.out.print(" |");
            System.out.println();
        }
        System.out.println();

        // Compute answer
        int answer = Math.max(frameSize, tasks.length);
        int idleSlots = Math.max(0, frameSize - tasks.length);

        System.out.println("  ── Result ──");
        System.out.println("    frameSize = " + frameSize);
        System.out.println("    totalTasks = " + tasks.length);
        System.out.println("    answer = max(" + frameSize + ", "
            + tasks.length + ") = " + answer);
        System.out.println("    idle slots = " + idleSlots);
    }

    // ─────────────────────────────────────────────────────────────
    // TRACE: Heap simulation — Show each round
    // ─────────────────────────────────────────────────────────────
    public static void traceHeapSimulation(char[] tasks, int n) {
        int[] freq = new int[26];
        for (char task : tasks) {
            freq[task - 'A']++;
        }

        PriorityQueue<int[]> maxHeap = new PriorityQueue<>(
            (a, b) -> b[0] - a[0]
        );
        for (int i = 0; i < 26; i++) {
            if (freq[i] > 0) {
                maxHeap.offer(new int[]{freq[i], i});
            }
        }

        System.out.println("  Tasks: " + new String(tasks) + ", n = " + n);
        System.out.println();

        int totalTime = 0;
        int round = 1;

        while (!maxHeap.isEmpty()) {
            List<int[]> temp = new ArrayList<>();
            int tasksExecuted = 0;
            StringBuilder roundSchedule = new StringBuilder();

            System.out.print("  Round " + round + ": ");

            for (int slot = 0; slot <= n; slot++) {
                if (!maxHeap.isEmpty()) {
                    int[] current = maxHeap.poll();
                    char taskChar = (char)('A' + current[1]);
                    roundSchedule.append(taskChar).append(" ");
                    tasksExecuted++;

                    if (current[0] - 1 > 0) {
                        temp.add(new int[]{current[0] - 1, current[1]});
                    }
                } else if (!temp.isEmpty()) {
                    roundSchedule.append("_ ");
                }
            }

            maxHeap.addAll(temp);
            int roundTime = maxHeap.isEmpty() ? tasksExecuted : (n + 1);
            totalTime += roundTime;

            System.out.println("[" + roundSchedule.toString().trim() + "]"
                + " → +" + roundTime + " → total=" + totalTime
                + (maxHeap.isEmpty() ? " (last round)" : ""));

            round++;
        }

        System.out.println("  Total Time: " + totalTime);
    }

    // ─────────────────────────────────────────────────────────────
    // IDLE SLOT CALCULATOR — Alternative formulation
    // ─────────────────────────────────────────────────────────────
    public static void analyzeIdleSlots(char[] tasks, int n) {
        int[] freq = new int[26];
        for (char task : tasks) {
            freq[task - 'A']++;
        }

        int maxFreq = 0;
        int maxCount = 0;
        for (int f : freq) {
            if (f > maxFreq) {
                maxFreq = f;
                maxCount = 1;
            } else if (f == maxFreq) {
                maxCount++;
            }
        }

        int totalTasks = tasks.length;
        int gaps = maxFreq - 1;
        int slotsPerGap = n + 1 - maxCount;
        int availableGapSlots = gaps * Math.max(slotsPerGap, 0);
        int otherTasks = totalTasks - maxFreq * maxCount;
        int idleSlots = Math.max(0, availableGapSlots - otherTasks);
        int answer = totalTasks + idleSlots;

        System.out.println("  ── Idle Slot Analysis ──");
        System.out.println("    maxFreq = " + maxFreq);
        System.out.println("    maxCount = " + maxCount);
        System.out.println("    totalTasks = " + totalTasks);
        System.out.println("    gaps = " + gaps);
        System.out.println("    slots per gap (for others) = n+1-maxCount = "
            + Math.max(slotsPerGap, 0));
        System.out.println("    available gap slots = " + availableGapSlots);
        System.out.println("    other tasks (to fill gaps) = " + otherTasks);
        System.out.println("    idle slots = max(0, " + availableGapSlots
            + " - " + otherTasks + ") = " + idleSlots);
        System.out.println("    answer = " + totalTasks + " + " + idleSlots
            + " = " + answer);
    }

    // ─────────────────────────────────────────────────────────────
    // MAIN — Run all examples
    // ─────────────────────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 60: Task Scheduler with Cooldown                ║");
        System.out.println("║  Pattern: Greedy → Frequency Slots → Grand Finale        ║");
        System.out.println("║                                                          ║");
        System.out.println("║  🏆 THE FINAL PROJECT — 60 of 60 — PATTERN MASTERY 🏆   ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Classic Example with Full Trace ──
        System.out.println("═══ TEST 1: Classic Example ═══");
        char[] t1 = {'A','A','A','B','B','B'};
        traceFormula(t1, 2);
        System.out.println();
        traceHeapSimulation(t1, 2);
        System.out.println();
        String[] schedule1 = buildSchedule(t1, 2);
        System.out.println("  Actual Schedule: " + String.join(" → ", schedule1));
        System.out.println();

        // ── TEST 2: No Cooldown ──
        System.out.println("═══ TEST 2: No Cooldown (n=0) ═══");
        char[] t2 = {'A','A','A','B','B','B'};
        traceFormula(t2, 0);
        System.out.println();

        // ── TEST 3: Large Cooldown ──
        System.out.println("═══ TEST 3: Large Cooldown ═══");
        char[] t3 = {'A','A','A','A','B','B','C'};
        traceFormula(t3, 3);
        System.out.println();
        traceHeapSimulation(t3, 3);
        System.out.println();

        // ── TEST 4: Many Distinct Tasks (No Idle) ──
        System.out.println("═══ TEST 4: Many Distinct Tasks — No Idle ═══");
        char[] t4 = {'A','A','A','B','B','B','C','C','C','D','D','D'};
        traceFormula(t4, 2);
        System.out.println();

        // ── TEST 5: All Same Task ──
        System.out.println("═══ TEST 5: All Same Task — Maximum Idle ═══");
        char[] t5 = {'A','A','A','A','A'};
        traceFormula(t5, 3);
        System.out.println();
        analyzeIdleSlots(t5, 3);
        System.out.println();

        // ── TEST 6: Multiple Max Frequency Tasks ──
        System.out.println("═══ TEST 6: Multiple Max Frequency Tasks ═══");
        char[] t6 = {'A','A','A','B','B','B','C','C','C'};
        traceFormula(t6, 2);
        System.out.println();

        // ── TEST 7: Single Task ──
        System.out.println("═══ TEST 7: Single Task ═══");
        char[] t7 = {'A'};
        System.out.println("  Formula: " + leastInterval(t7, 5));
        System.out.println("  Expected: 1");
        System.out.println();

        // ── TEST 8: Idle Slot Analysis ──
        System.out.println("═══ TEST 8: Idle Slot Analysis ═══");
        char[] t8 = {'A','A','A','A','A','A','B','C','D','E','F','G'};
        analyzeIdleSlots(t8, 2);
        System.out.println();
        String[] schedule8 = buildSchedule(t8, 2);
        System.out.println("  Schedule: " + String.join(" → ", schedule8));
        System.out.println();

        // ── TEST 9: Verification — All Approaches Match ──
        System.out.println("═══ TEST 9: Verification — All Approaches ═══");
        char[][] testCases = {
            {'A','A','A','B','B','B'},
            {'A','A','A','B','B','B','C','C','C','D','D','D'},
            {'A','A','A','A','B','B','C'},
            {'A','A','A','A','A'},
            {'A'},
            {'A','B','C','D','E','F'},
            {'A','A','A','B','B','B','C','C','C'},
            {'A','A','A','A','A','A','B','C','D','E','F','G'},
            {'A','A','B','B','C','C','D','D','E','E','F','F',
             'G','G','H','H','I','I','J','J','K','K','L','L',
             'M','M','N','N','O','O','P','P','Q','Q','R','R',
             'S','S','T','T','U','U','V','V','W','W','X','X',
             'Y','Y','Z','Z'}
        };
        int[] cooldowns = {2, 2, 3, 3, 5, 0, 2, 2, 2};

        boolean allPassed = true;
        for (int t = 0; t < testCases.length; t++) {
            int formula = leastInterval(testCases[t], cooldowns[t]);
            int heap = leastIntervalHeap(testCases[t], cooldowns[t]);
            String[] sched = buildSchedule(testCases[t], cooldowns[t]);
            int schedLen = sched.length;

            boolean match = (formula == heap) && (formula == schedLen);

            System.out.printf("  Test %d: Formula=%-3d Heap=%-3d Schedule=%-3d n=%-2d %s%n",
                t + 1, formula, heap, schedLen, cooldowns[t],
                match ? "✓ PASS" : "✗ FAIL");

            if (!match) allPassed = false;
        }
        System.out.println(allPassed
            ? "\n  ✅ ALL TESTS PASSED"
            : "\n  ❌ SOME TESTS FAILED");
        System.out.println();

        // ── TEST 10: Performance ──
        System.out.println("═══ TEST 10: Performance Test ═══");
        int size = 1_000_000;
        char[] largeTasks = new char[size];
        java.util.Random rng = new java.util.Random(42);
        for (int i = 0; i < size; i++) {
            largeTasks[i] = (char)('A' + rng.nextInt(26));
        }
        int largeN = 100;

        long start, elapsed;

        start = System.nanoTime();
        int formulaResult = leastInterval(largeTasks, largeN);
        elapsed = System.nanoTime() - start;
        System.out.println("  Formula:    result=" + formulaResult
            + "  time=" + (elapsed / 1_000_000) + " ms");

        start = System.nanoTime();
        int heapResult = leastIntervalHeap(largeTasks, largeN);
        elapsed = System.nanoTime() - start;
        System.out.println("  Heap:       result=" + heapResult
            + "  time=" + (elapsed / 1_000_000) + " ms");

        System.out.println("  Match: " + (formulaResult == heapResult ? "✓" : "✗"));
    }
}