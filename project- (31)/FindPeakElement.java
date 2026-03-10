import java.util.ArrayList;
import java.util.List;

public class FindPeakElement {

    // ─────────────────────────────────────────────
    // CORE SOLUTION: Gradient-Based Binary Search (Iterative)
    // ─────────────────────────────────────────────
    public static int findPeakElement(int[] nums) {

        // Edge case: single element is always a peak
        // Loop won't execute → returns index 0 → correct
        int left = 0;
        int right = nums.length - 1;

        // Converge until one element remains
        // WHY left < right (not <=)?
        //   → Guarantees mid + 1 is in bounds
        //   → Termination IS the answer (no separate "found" check)
        while (left < right) {

            // Overflow-safe midpoint (covered in Project 9)
            int mid = left + (right - left) / 2;

            // GRADIENT CHECK: compare mid with its right neighbor
            if (nums[mid] < nums[mid + 1]) {
                // ↗ Ascending slope → peak is to the RIGHT
                // mid is NOT a peak (right neighbor is bigger)
                // Safe to exclude mid
                left = mid + 1;
            } else {
                // ↘ Descending slope → peak is at mid or to the LEFT
                // mid COULD be the peak
                // Must include mid in search space
                right = mid;
            }
        }

        // left == right → this index IS a peak
        return left;
    }

    // ─────────────────────────────────────────────
    // RECURSIVE SOLUTION (same logic, different structure)
    // ─────────────────────────────────────────────
    public static int findPe[REDACTED:AWS_ACCESS_KEY](int[] nums) {
        return binarySearchPeak(nums, 0, nums.length - 1);
    }

    private static int binarySearchPeak(int[] nums, int left, int right) {

        // Base case: single element → it's the peak
        if (left == right) {
            return left;
        }

        int mid = left + (right - left) / 2;

        if (nums[mid] < nums[mid + 1]) {
            // Ascending → search right half (exclude mid)
            return binarySearchPeak(nums, mid + 1, right);
        } else {
            // Descending → search left half (include mid)
            return binarySearchPeak(nums, left, mid);
        }
    }

    // ─────────────────────────────────────────────
    // BRUTE FORCE: Linear Scan (for comparison)
    // ─────────────────────────────────────────────
    public static int findPeakLinear(int[] nums) {

        int n = nums.length;

        for (int i = 0; i < n; i++) {
            boolean leftOk = (i == 0) || (nums[i] > nums[i - 1]);
            boolean rightOk = (i == n - 1) || (nums[i] > nums[i + 1]);

            if (leftOk && rightOk) {
                return i;
            }
        }

        return -1; // Should never reach here if input is valid
    }

    // ─────────────────────────────────────────────
    // FIND ALL PEAKS (Linear — no shortcut for this)
    // ─────────────────────────────────────────────
    public static List<Integer> findAllPeaks(int[] nums) {
        List<Integer> peaks = new ArrayList<>();
        int n = nums.length;

        for (int i = 0; i < n; i++) {
            boolean leftOk = (i == 0) || (nums[i] > nums[i - 1]);
            boolean rightOk = (i == n - 1) || (nums[i] > nums[i + 1]);

            if (leftOk && rightOk) {
                peaks.add(i);
            }
        }

        return peaks;
    }

    // ─────────────────────────────────────────────
    // TRACE FUNCTION: Visualize the gradient search
    // ─────────────────────────────────────────────
    public static void findPeakWithTrace(int[] nums) {
        System.out.print("Array: [");
        for (int i = 0; i < nums.length; i++) {
            System.out.print(nums[i]);
            if (i < nums.length - 1) System.out.print(", ");
        }
        System.out.println("]");

        // Show all actual peaks for reference
        List<Integer> allPeaks = findAllPeaks(nums);
        System.out.print("All peaks: ");
        for (int idx : allPeaks) {
            System.out.print("index " + idx + " (val=" + nums[idx] + ")  ");
        }
        System.out.println();
        System.out.println("─────────────────────────────────────");

        int left = 0;
        int right = nums.length - 1;
        int step = 1;

        while (left < right) {
            int mid = left + (right - left) / 2;

            System.out.println("Step " + step + ":");
            System.out.println("  left=" + left
                    + "  right=" + right
                    + "  mid=" + mid);
            System.out.println("  nums[mid]=" + nums[mid]
                    + "  nums[mid+1]=" + nums[mid + 1]);

            if (nums[mid] < nums[mid + 1]) {
                System.out.println("  " + nums[mid] + " < " + nums[mid + 1]
                        + " → ↗ ASCENDING → peak is RIGHT → left = " + (mid + 1));
                left = mid + 1;
            } else {
                System.out.println("  " + nums[mid] + " > " + nums[mid + 1]
                        + " → ↘ DESCENDING → peak is LEFT/MID → right = " + mid);
                right = mid;
            }

            // Show remaining search window
            System.out.print("  Window: [");
            for (int i = left; i <= right; i++) {
                System.out.print(nums[i]);
                if (i < right) System.out.print(", ");
            }
            System.out.println("]");
            System.out.println();

            step++;
        }

        System.out.println("CONVERGED: left=right=" + left
                + " → Peak at index " + left
                + " (value=" + nums[left] + ")");

        // Verify it's actually a peak
        boolean isLeftOk = (left == 0) || (nums[left] > nums[left - 1]);
        boolean isRightOk = (left == nums.length - 1) || (nums[left] > nums[left + 1]);
        System.out.println("Verification: "
                + "left_neighbor=" + (left > 0 ? String.valueOf(nums[left - 1]) : "-∞")
                + "  peak=" + nums[left]
                + "  right_neighbor=" + (left < nums.length - 1 ? String.valueOf(nums[left + 1]) : "-∞")
                + "  → Valid peak? " + (isLeftOk && isRightOk ? "✓ YES" : "✗ NO"));
    }

    // ─────────────────────────────────────────────
    // VARIANT: Find Peak in Mountain Array
    //   → Array strictly increases then strictly decreases
    //   → Exactly ONE peak exists → find it
    // ─────────────────────────────────────────────
    public static int peakOfMountainArray(int[] arr) {

        // Same gradient logic — but here guaranteed exactly one peak
        int left = 0;
        int right = arr.length - 1;

        while (left < right) {
            int mid = left + (right - left) / 2;

            if (arr[mid] < arr[mid + 1]) {
                // Still ascending the mountain
                left = mid + 1;
            } else {
                // Descending or at the peak
                right = mid;
            }
        }

        return left;
    }

    // ─────────────────────────────────────────────
    // VARIANT: Find Valley Element (local minimum)
    //   → Element smaller than both neighbors
    //   → Assume nums[-1] = nums[n] = +∞
    // ─────────────────────────────────────────────
    public static int findValleyElement(int[] nums) {

        int left = 0;
        int right = nums.length - 1;

        while (left < right) {
            int mid = left + (right - left) / 2;

            // REVERSED gradient logic:
            // If descending (going down to right) → valley is to the RIGHT
            // If ascending (going up to right) → valley is to the LEFT
            if (nums[mid] > nums[mid + 1]) {
                // ↘ Descending → valley is to the RIGHT
                left = mid + 1;
            } else {
                // ↗ Ascending → valley is at mid or to the LEFT
                right = mid;
            }
        }

        return left;
    }

    // ─────────────────────────────────────────────
    // MAIN — Run all examples
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 31: Find Peak Element in an Array       ║");
        System.out.println("║  Pattern: Gradient-Based Binary Search           ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Clear single peak ──
        System.out.println("═══ TEST 1: Clear Single Peak ═══");
        findPeakWithTrace(new int[]{1, 3, 20, 4, 1, 0});
        System.out.println();

        // ── TEST 2: Multiple peaks ──
        System.out.println("═══ TEST 2: Multiple Peaks ═══");
        findPeakWithTrace(new int[]{1, 2, 1, 3, 5, 6, 4});
        System.out.println();

        // ── TEST 3: Monotonically decreasing ──
        System.out.println("═══ TEST 3: Monotonically Decreasing ═══");
        findPeakWithTrace(new int[]{5, 4, 3, 2, 1});
        System.out.println();

        // ── TEST 4: Monotonically increasing ──
        System.out.println("═══ TEST 4: Monotonically Increasing ═══");
        findPeakWithTrace(new int[]{1, 2, 3, 4, 5});
        System.out.println();

        // ── TEST 5: Two elements ──
        System.out.println("═══ TEST 5: Two Elements ═══");
        findPeakWithTrace(new int[]{1, 2});
        System.out.println();

        // ── TEST 6: Single element ──
        System.out.println("═══ TEST 6: Single Element ═══");
        findPeakWithTrace(new int[]{42});
        System.out.println();

        // ── TEST 7: Zigzag pattern ──
        System.out.println("═══ TEST 7: Zigzag Pattern (Many Peaks) ═══");
        findPeakWithTrace(new int[]{1, 5, 1, 5, 1, 5, 1, 5, 1});
        System.out.println();

        // ── TEST 8: Mountain Array Variant ──
        System.out.println("═══ TEST 8: Mountain Array Variant ═══");
        int[] mountain = {1, 3, 5, 8, 12, 15, 11, 7, 2};
        int mountainPeak = peakOfMountainArray(mountain);
        System.out.print("Mountain: [");
        for (int i = 0; i < mountain.length; i++) {
            System.out.print(mountain[i]);
            if (i < mountain.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("Peak of mountain at index " + mountainPeak
                + " (value=" + mountain[mountainPeak] + ")");
        System.out.println();

        // ── TEST 9: Valley Element Variant ──
        System.out.println("═══ TEST 9: Valley Element Variant ═══");
        int[] valleyArr = {5, 3, 1, 4, 7};
        int valley = findValleyElement(valleyArr);
        System.out.print("Array: [");
        for (int i = 0; i < valleyArr.length; i++) {
            System.out.print(valleyArr[i]);
            if (i < valleyArr.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("Valley at index " + valley
                + " (value=" + valleyArr[valley] + ")");
        System.out.println();

        // ── TEST 10: Performance Comparison ──
        System.out.println("═══ TEST 10: Performance Comparison ═══");
        int size = 10_000_000;
        int[] largeArr = new int[size];

        // Create a large array with a peak at 3/4 position
        int peakPos = size * 3 / 4;
        for (int i = 0; i <= peakPos; i++) {
            largeArr[i] = i;
        }
        for (int i = peakPos + 1; i < size; i++) {
            largeArr[i] = 2 * peakPos - i;
        }

        System.out.println("Array size: " + size);
        System.out.println("Actual peak at index: " + peakPos);

        // Binary Search
        long startTime = System.nanoTime();
        int bsResult = findPeakElement(largeArr);
        long bsTime = System.nanoTime() - startTime;

        // Linear Scan
        startTime = System.nanoTime();
        int linResult = findPeakLinear(largeArr);
        long linTime = System.nanoTime() - startTime;

        System.out.println("Binary Search: index=" + bsResult
                + " → " + (bsTime / 1000) + " microseconds");
        System.out.println("Linear Scan:   index=" + linResult
                + " → " + (linTime / 1000) + " microseconds");
        System.out.println("Speedup: " + (linTime / Math.max(bsTime, 1)) + "x faster");
        System.out.println();

        // ── STEP COUNT DEMONSTRATION ──
        System.out.println("═══ Step Count vs Array Size ═══");
        int[] testSizes = {10, 100, 1000, 10000, 100000, 1000000};
        for (int s : testSizes) {
            int[] testArr = new int[s];
            for (int i = 0; i < s / 2; i++) testArr[i] = i;
            for (int i = s / 2; i < s; i++) testArr[i] = s - i;

            int steps = 0;
            int l = 0, r = s - 1;
            while (l < r) {
                int m = l + (r - l) / 2;
                if (testArr[m] < testArr[m + 1]) l = m + 1;
                else r = m;
                steps++;
            }

            System.out.println("  n=" + String.format("%,10d", s)
                    + " → " + steps + " steps"
                    + "  (log₂=" + String.format("%.1f", Math.log(s) / Math.log(2)) + ")");
        }
    }
}