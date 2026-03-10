import java.util.PriorityQueue;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class KClosestPoints {

    // ─────────────────────────────────────────────
    // SOLUTION 1: Max-Heap of Size K (OPTIMAL — Gatekeeper)
    // ─────────────────────────────────────────────
    public static int[][] kClosest(int[][] points, int k) {

        // Max-heap by squared distance (REVERSED comparator)
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> {
            long distA = (long) a[0] * a[0] + (long) a[1] * a[1];
            long distB = (long) b[0] * b[0] + (long) b[1] * b[1];
            return Long.compare(distB, distA); // max-heap: larger distance = higher priority
        });

        for (int[] point : points) {

            long dist = (long) point[0] * point[0] + (long) point[1] * point[1];

            if (maxHeap.size() < k) {
                // Club not full → admit
                maxHeap.offer(point);
            } 
            else {
                // Compute gatekeeper's distance
                int[] root = maxHeap.peek();
                long rootDist = (long) root[0] * root[0] + (long) root[1] * root[1];

                if (dist < rootDist) {
                    // Closer than gatekeeper → swap in
                    maxHeap.poll();
                    maxHeap.offer(point);
                }
                // else: farther or equal → rejected
            }
        }

        // Drain heap into result
        int[][] result = new int[k][2];
        for (int i = 0; i < k; i++) {
            result[i] = maxHeap.poll();
        }
        return result;
    }

    // ─────────────────────────────────────────────
    // SOLUTION 2: Simplified (Add then Trim)
    // ─────────────────────────────────────────────
    public static int[][] kClosestSimplified(int[][] points, int k) {

        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> {
            int distA = a[0] * a[0] + a[1] * a[1];
            int distB = b[0] * b[0] + b[1] * b[1];
            return Integer.compare(distB, distA);
        });

        for (int[] point : points) {
            maxHeap.offer(point);
            if (maxHeap.size() > k) {
                maxHeap.poll(); // remove farthest
            }
        }

        int[][] result = new int[k][2];
        for (int i = 0; i < k; i++) {
            result[i] = maxHeap.poll();
        }
        return result;
    }

    // ─────────────────────────────────────────────
    // SOLUTION 3: Sorting Approach (for comparison)
    // ─────────────────────────────────────────────
    public static int[][] kClosestBySort(int[][] points, int k) {

        Arrays.sort(points, (a, b) -> {
            int distA = a[0] * a[0] + a[1] * a[1];
            int distB = b[0] * b[0] + b[1] * b[1];
            return Integer.compare(distA, distB);
        });

        return Arrays.copyOfRange(points, 0, k);
    }

    // ─────────────────────────────────────────────
    // SOLUTION 4: Min-Heap of ALL points, extract K (naive heap)
    // ─────────────────────────────────────────────
    public static int[][] kClosestMinHeapAll(int[][] points, int k) {

        // Min-heap → closest at root
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> {
            int distA = a[0] * a[0] + a[1] * a[1];
            int distB = b[0] * b[0] + b[1] * b[1];
            return Integer.compare(distA, distB);
        });

        // Add ALL points
        for (int[] point : points) {
            minHeap.offer(point);
        }

        // Extract K closest
        int[][] result = new int[k][2];
        for (int i = 0; i < k; i++) {
            result[i] = minHeap.poll();
        }
        return result;
    }

    // ─────────────────────────────────────────────
    // VARIANT: K Closest to arbitrary point P
    // ─────────────────────────────────────────────
    public static int[][] kClosestToPoint(int[][] points, int k, int px, int py) {

        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> {
            long distA = (long)(a[0] - px) * (a[0] - px) + (long)(a[1] - py) * (a[1] - py);
            long distB = (long)(b[0] - px) * (b[0] - px) + (long)(b[1] - py) * (b[1] - py);
            return Long.compare(distB, distA);
        });

        for (int[] point : points) {
            long dist = (long)(point[0] - px) * (point[0] - px) 
                      + (long)(point[1] - py) * (point[1] - py);

            if (maxHeap.size() < k) {
                maxHeap.offer(point);
            } 
            else {
                int[] root = maxHeap.peek();
                long rootDist = (long)(root[0] - px) * (root[0] - px) 
                              + (long)(root[1] - py) * (root[1] - py);
                if (dist < rootDist) {
                    maxHeap.poll();
                    maxHeap.offer(point);
                }
            }
        }

        int[][] result = new int[k][2];
        for (int i = 0; i < k; i++) {
            result[i] = maxHeap.poll();
        }
        return result;
    }

    // ─────────────────────────────────────────────
    // VARIANT: K Farthest Points from Origin
    // (flip back to MIN-heap — like Project 38's K Largest)
    // ─────────────────────────────────────────────
    public static int[][] kFarthest(int[][] points, int k) {

        // MIN-heap by distance → root = closest of K farthest = gatekeeper
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> {
            int distA = a[0] * a[0] + a[1] * a[1];
            int distB = b[0] * b[0] + b[1] * b[1];
            return Integer.compare(distA, distB);
        });

        for (int[] point : points) {
            minHeap.offer(point);
            if (minHeap.size() > k) {
                minHeap.poll(); // remove closest (least far)
            }
        }

        int[][] result = new int[k][2];
        for (int i = 0; i < k; i++) {
            result[i] = minHeap.poll();
        }
        return result;
    }

    // ─────────────────────────────────────────────
    // TRACE — Watch the max-heap gatekeeper evolve
    // ─────────────────────────────────────────────
    public static void kClosestWithTrace(int[][] points, int k) {

        System.out.println("Points: " + formatPoints(points));
        System.out.println("K = " + k);
        System.out.println("─────────────────────────────────");

        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> {
            int distA = a[0] * a[0] + a[1] * a[1];
            int distB = b[0] * b[0] + b[1] * b[1];
            return Integer.compare(distB, distA);
        });

        int step = 1;

        for (int[] point : points) {
            int dist = point[0] * point[0] + point[1] * point[1];
            System.out.println("Step " + step + ": Process " + formatPoint(point) 
                + "  dist²=" + dist);

            if (maxHeap.size() < k) {
                maxHeap.offer(point);
                System.out.println("  → Heap not full (size " + maxHeap.size() 
                    + " ≤ " + k + ") → ADD");
            } 
            else {
                int[] root = maxHeap.peek();
                int rootDist = root[0] * root[0] + root[1] * root[1];

                if (dist < rootDist) {
                    maxHeap.poll();
                    maxHeap.offer(point);
                    System.out.println("  → dist²(" + dist + ") < gatekeeper(" 
                        + rootDist + " at " + formatPoint(root) + ") → SWAP");
                } 
                else {
                    System.out.println("  → dist²(" + dist + ") ≥ gatekeeper(" 
                        + rootDist + " at " + formatPoint(root) + ") → REJECTED");
                }
            }

            // Show heap contents
            System.out.print("  → Heap: {");
            int[][] heapArr = maxHeap.toArray(new int[0][]);
            Arrays.sort(heapArr, (a, b) -> {
                int dA = a[0] * a[0] + a[1] * a[1];
                int dB = b[0] * b[0] + b[1] * b[1];
                return Integer.compare(dA, dB);
            });
            for (int i = 0; i < heapArr.length; i++) {
                int d = heapArr[i][0] * heapArr[i][0] + heapArr[i][1] * heapArr[i][1];
                System.out.print(formatPoint(heapArr[i]) + "(d²=" + d + ")");
                if (i < heapArr.length - 1) System.out.print(", ");
            }
            System.out.println("}");

            int[] currRoot = maxHeap.peek();
            int currRootDist = currRoot[0] * currRoot[0] + currRoot[1] * currRoot[1];
            System.out.println("  → Gatekeeper: " + formatPoint(currRoot) 
                + " dist²=" + currRootDist);
            System.out.println();

            step++;
        }

        // Drain result
        System.out.println("═══════════════════════════════════");
        System.out.print("ANSWER: K=" + k + " closest → [");
        int[][] result = new int[k][2];
        for (int i = 0; i < k; i++) {
            result[i] = maxHeap.poll();
        }
        // Sort result by distance for readable output
        Arrays.sort(result, (a, b) -> {
            int dA = a[0] * a[0] + a[1] * a[1];
            int dB = b[0] * b[0] + b[1] * b[1];
            return Integer.compare(dA, dB);
        });
        for (int i = 0; i < result.length; i++) {
            System.out.print(formatPoint(result[i]));
            if (i < result.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("═══════════════════════════════════");
    }

    // ─────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────
    private static String formatPoint(int[] p) {
        return "[" + p[0] + "," + p[1] + "]";
    }

    private static String formatPoints(int[][] pts) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < pts.length; i++) {
            sb.append(formatPoint(pts[i]));
            if (i < pts.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    // ─────────────────────────────────────────────
    // MAIN
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 39: K Closest Points to Origin           ║");
        System.out.println("║  Pattern: Top K → Max-Heap + Distance Comparator  ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Basic Example with Trace ──
        System.out.println("═══ TEST 1: Basic Example with Trace ═══");
        int[][] pts1 = {{3, 3}, {5, -1}, {-2, 4}, {1, 1}, {0, 2}};
        kClosestWithTrace(pts1, 3);
        System.out.println();

        // ── TEST 2: LeetCode Example 1 ──
        System.out.println("═══ TEST 2: LeetCode Example 1 ═══");
        int[][] pts2 = {{1, 3}, {-2, 2}};
        kClosestWithTrace(pts2, 1);
        System.out.println();

        // ── TEST 3: Ties in Distance ──
        System.out.println("═══ TEST 3: Ties in Distance ═══");
        int[][] pts3 = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {2, 2}};
        kClosestWithTrace(pts3, 3);
        System.out.println();

        // ── TEST 4: All Solutions Agree ──
        System.out.println("═══ TEST 4: Verify All Solutions Agree ═══");
        int[][] pts4 = {{3, 3}, {5, -1}, {-2, 4}, {1, 1}, {0, 2}};
        int k4 = 2;
        System.out.println("Points: " + formatPoints(pts4) + ", K=" + k4);

        int[][] r1 = kClosest(pts4.clone(), k4);
        int[][] r2 = kClosestSimplified(pts4.clone(), k4);
        int[][] r3 = kClosestBySort(pts4.clone(), k4);
        int[][] r4 = kClosestMinHeapAll(pts4.clone(), k4);

        // Sort each result by distance for comparison
        Comparator<int[]> distComp = (a, b) -> Integer.compare(
            a[0] * a[0] + a[1] * a[1], b[0] * b[0] + b[1] * b[1]);
        Arrays.sort(r1, distComp);
        Arrays.sort(r2, distComp);
        Arrays.sort(r3, distComp);
        Arrays.sort(r4, distComp);

        System.out.println("  Max-Heap (Gatekeeper): " + formatPoints(r1));
        System.out.println("  Max-Heap (Simplified): " + formatPoints(r2));
        System.out.println("  Sorting Approach:      " + formatPoints(r3));
        System.out.println("  Min-Heap (All n):      " + formatPoints(r4));
        System.out.println();

        // ── TEST 5: K Closest to Arbitrary Point ──
        System.out.println("═══ TEST 5: K Closest to Point (3, 3) ═══");
        int[][] pts5 = {{1, 1}, {5, 5}, {3, 4}, {2, 2}, {6, 1}};
        int[][] closestToP = kClosestToPoint(pts5, 2, 3, 3);
        Arrays.sort(closestToP, distComp);
        System.out.println("Points: " + formatPoints(pts5));
        System.out.println("2 closest to (3,3): " + formatPoints(closestToP));
        System.out.println();

        // ── TEST 6: K Farthest (Flipped Pattern) ──
        System.out.println("═══ TEST 6: K Farthest from Origin ═══");
        int[][] pts6 = {{1, 1}, {5, 5}, {3, 4}, {2, 2}, {6, 1}};
        int[][] farthest = kFarthest(pts6, 2);
        Arrays.sort(farthest, (a, b) -> Integer.compare(
            b[0] * b[0] + b[1] * b[1], a[0] * a[0] + a[1] * a[1]));
        System.out.println("Points: " + formatPoints(pts6));
        System.out.println("2 farthest: " + formatPoints(farthest));
        System.out.println();

        // ── TEST 7: Performance Comparison ──
        System.out.println("═══ TEST 7: Performance Comparison ═══");
        int size = 2_000_000;
        int kPerf = 50;
        Random rand = new Random(42);
        int[][] largePts = new int[size][2];
        for (int i = 0; i < size; i++) {
            largePts[i] = new int[] {
                rand.nextInt(20001) - 10000,
                rand.nextInt(20001) - 10000
            };
        }
        System.out.println("Points: " + size + ", K: " + kPerf);

        // Max-Heap K
        int[][] copy1 = deepCopy(largePts);
        long start = System.nanoTime();
        kClosest(copy1, kPerf);
        long heapTime = System.nanoTime() - start;
        System.out.println("  Max-Heap (K):     " + (heapTime / 1_000_000) + " ms");

        // Sorting
        int[][] copy2 = deepCopy(largePts);
        start = System.nanoTime();
        kClosestBySort(copy2, kPerf);
        long sortTime = System.nanoTime() - start;
        System.out.println("  Sorting:          " + (sortTime / 1_000_000) + " ms");

        // Min-Heap All
        int[][] copy3 = deepCopy(largePts);
        start = System.nanoTime();
        kClosestMinHeapAll(copy3, kPerf);
        long minHeapAllTime = System.nanoTime() - start;
        System.out.println("  Min-Heap (all n): " + (minHeapAllTime / 1_000_000) + " ms");

        System.out.println("  Heap speedup vs Sort: " 
            + String.format("%.1f", (double) sortTime / heapTime) + "x");
        System.out.println("  Heap speedup vs Min-Heap-All: " 
            + String.format("%.1f", (double) minHeapAllTime / heapTime) + "x");

        // ── TEST 8: Edge Cases ──
        System.out.println();
        System.out.println("═══ TEST 8: Edge Cases ═══");

        // Single point
        int[][] single = {{5, 7}};
        int[][] resSingle = kClosest(single, 1);
        System.out.println("Single point [[5,7]], K=1: " + formatPoints(resSingle));

        // All same point
        int[][] same = {{1, 1}, {1, 1}, {1, 1}};
        int[][] resSame = kClosest(same, 2);
        System.out.println("All same [[1,1],[1,1],[1,1]], K=2: " + formatPoints(resSame));

        // Origin in array
        int[][] withOrigin = {{0, 0}, {1, 1}, {2, 2}};
        int[][] resOrigin = kClosest(withOrigin, 1);
        System.out.println("With origin [[0,0],[1,1],[2,2]], K=1: " + formatPoints(resOrigin));

        // Negative coordinates
        int[][] neg = {{-3, -4}, {1, 1}, {-1, -1}};
        int[][] resNeg = kClosest(neg, 2);
        Arrays.sort(resNeg, distComp);
        System.out.println("Negatives [[-3,-4],[1,1],[-1,-1]], K=2: " + formatPoints(resNeg));
    }

    private static int[][] deepCopy(int[][] arr) {
        int[][] copy = new int[arr.length][2];
        for (int i = 0; i < arr.length; i++) {
            copy[i][0] = arr[i][0];
            copy[i][1] = arr[i][1];
        }
        return copy;
    }
}