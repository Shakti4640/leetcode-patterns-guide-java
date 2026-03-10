import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AccountsMerge {

    // ─────────────────────────────────────────────────────────────
    // UNION FIND — Map-based for String keys
    // ─────────────────────────────────────────────────────────────
    static Map<String, String> parent;
    static Map<String, Integer> rank;

    static void init(String x) {
        if (!parent.containsKey(x)) {
            parent.put(x, x);
            rank.put(x, 0);
        }
    }

    static String find(String x) {
        if (!parent.get(x).equals(x)) {
            // Path compression: point directly to root
            parent.put(x, find(parent.get(x)));
        }
        return parent.get(x);
    }

    static void union(String x, String y) {
        String rootX = find(x);
        String rootY = find(y);

        if (rootX.equals(rootY)) return;  // Already in same group

        // Union by rank: attach shorter tree under taller tree
        int rankX = rank.get(rootX);
        int rankY = rank.get(rootY);

        if (rankX < rankY) {
            parent.put(rootX, rootY);
        } else if (rankX > rankY) {
            parent.put(rootY, rootX);
        } else {
            parent.put(rootY, rootX);
            rank.put(rootX, rankX + 1);
        }
    }

    // ─────────────────────────────────────────────────────────────
    // MAIN SOLUTION: Accounts Merge using Union Find
    // ─────────────────────────────────────────────────────────────
    public static List<List<String>> accountsMerge(List<List<String>> accounts) {
        parent = new HashMap<>();
        rank   = new HashMap<>();
        Map<String, String> emailToName = new HashMap<>();

        // ── PHASE 1: Build Union Find relationships ──
        for (List<String> account : accounts) {
            String name = account.get(0);
            String firstEmail = account.get(1);

            for (int i = 1; i < account.size(); i++) {
                String email = account.get(i);
                init(email);
                emailToName.put(email, name);

                // Union every email with the first email (star topology)
                if (i > 1) {
                    union(firstEmail, email);
                }
            }
        }

        // ── PHASE 2: Group emails by root ──
        Map<String, List<String>> components = new HashMap<>();

        for (String email : parent.keySet()) {
            String root = find(email);
            components.computeIfAbsent(root, k -> new ArrayList<>()).add(email);
        }

        // ── PHASE 3: Build result ──
        List<List<String>> result = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : components.entrySet()) {
            String root = entry.getKey();
            List<String> emails = entry.getValue();

            // Sort emails alphabetically
            Collections.sort(emails);

            // Build merged account: [name, email1, email2, ...]
            List<String> mergedAccount = new ArrayList<>();
            mergedAccount.add(emailToName.get(root));
            mergedAccount.addAll(emails);

            result.add(mergedAccount);
        }

        return result;
    }

    // ─────────────────────────────────────────────────────────────
    // ALTERNATIVE: BFS Approach (for comparison)
    // ─────────────────────────────────────────────────────────────
    public static List<List<String>> accountsMergeBFS(List<List<String>> accounts) {
        // Build adjacency list: email → set of connected emails
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, String> emailToName = new HashMap<>();

        for (List<String> account : accounts) {
            String name = account.get(0);
            String firstEmail = account.get(1);

            for (int i = 1; i < account.size(); i++) {
                String email = account.get(i);
                emailToName.put(email, name);
                graph.computeIfAbsent(email, k -> new ArrayList<>());

                if (i > 1) {
                    // Bidirectional edge between first email and current email
                    graph.get(firstEmail).add(email);
                    graph.get(email).add(firstEmail);
                }
            }
        }

        // BFS to find connected components
        java.util.Set<String> visited = new java.util.HashSet<>();
        List<List<String>> result = new ArrayList<>();

        for (String email : graph.keySet()) {
            if (visited.contains(email)) continue;

            // BFS from this email
            List<String> component = new ArrayList<>();
            java.util.Queue<String> queue = new java.util.ArrayDeque<>();
            queue.offer(email);
            visited.add(email);

            while (!queue.isEmpty()) {
                String current = queue.poll();
                component.add(current);

                for (String neighbor : graph.get(current)) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.offer(neighbor);
                    }
                }
            }

            // Sort and build merged account
            Collections.sort(component);
            List<String> mergedAccount = new ArrayList<>();
            mergedAccount.add(emailToName.get(component.get(0)));
            mergedAccount.addAll(component);
            result.add(mergedAccount);
        }

        return result;
    }

    // ─────────────────────────────────────────────────────────────
    // TRACE FUNCTION — Show Union Find operations step by step
    // ─────────────────────────────────────────────────────────────
    public static List<List<String>> accountsMergeWithTrace(List<List<String>> accounts) {
        parent = new HashMap<>();
        rank   = new HashMap<>();
        Map<String, String> emailToName = new HashMap<>();

        System.out.println("  ── INPUT ACCOUNTS ──");
        for (int i = 0; i < accounts.size(); i++) {
            System.out.println("    Account " + i + ": " + accounts.get(i));
        }
        System.out.println();

        // Phase 1: Build relationships
        System.out.println("  ── PHASE 1: Union Operations ──");

        for (int i = 0; i < accounts.size(); i++) {
            List<String> account = accounts.get(i);
            String name = account.get(0);
            String firstEmail = account.get(1);

            System.out.println("    Processing Account " + i
                + " (name=\"" + name + "\"):");

            for (int j = 1; j < account.size(); j++) {
                String email = account.get(j);
                boolean isNew = !parent.containsKey(email);
                init(email);
                emailToName.put(email, name);

                System.out.println("      email: " + email
                    + (isNew ? " (NEW)" : " (EXISTS)"));

                if (j > 1) {
                    String rootBefore1 = find(firstEmail);
                    String rootBefore2 = find(email);
                    boolean sameGroup = rootBefore1.equals(rootBefore2);

                    union(firstEmail, email);

                    if (!sameGroup) {
                        System.out.println("      → union(" + shortEmail(firstEmail)
                            + ", " + shortEmail(email) + ") → MERGED");
                    } else {
                        System.out.println("      → union(" + shortEmail(firstEmail)
                            + ", " + shortEmail(email) + ") → already same group");
                    }
                }
            }
            System.out.println();
        }

        // Phase 2: Group
        System.out.println("  ── PHASE 2: Group by Root ──");
        Map<String, List<String>> components = new HashMap<>();

        for (String email : parent.keySet()) {
            String root = find(email);
            components.computeIfAbsent(root, k -> new ArrayList<>()).add(email);
        }

        for (Map.Entry<String, List<String>> entry : components.entrySet()) {
            System.out.println("    Root: " + shortEmail(entry.getKey())
                + " → Group: " + shortenList(entry.getValue()));
        }
        System.out.println();

        // Phase 3: Build result
        System.out.println("  ── PHASE 3: Build Result ──");
        List<List<String>> result = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : components.entrySet()) {
            String root = entry.getKey();
            List<String> emails = entry.getValue();
            Collections.sort(emails);

            List<String> mergedAccount = new ArrayList<>();
            mergedAccount.add(emailToName.get(root));
            mergedAccount.addAll(emails);
            result.add(mergedAccount);

            System.out.println("    [" + emailToName.get(root) + ", "
                + shortenList(emails) + "]");
        }

        return result;
    }

    // ─────────────────────────────────────────────────────────────
    // UNION FIND STATE VISUALIZER
    // ─────────────────────────────────────────────────────────────
    public static void visualizeUnionFind() {
        System.out.println("  ── Union Find State ──");
        System.out.println("    ┌─────────────────────┬─────────────────────┬──────┐");
        System.out.println("    │ Email               │ Parent              │ Rank │");
        System.out.println("    ├─────────────────────┼─────────────────────┼──────┤");

        // Use TreeMap for sorted output
        Map<String, String> sortedParent = new TreeMap<>(parent);
        for (Map.Entry<String, String> entry : sortedParent.entrySet()) {
            String email = entry.getKey();
            String par = entry.getValue();
            int r = rank.getOrDefault(email, 0);
            boolean isRoot = email.equals(par);
            System.out.printf("    │ %-19s │ %-19s │  %d   │%s%n",
                shortEmail(email),
                shortEmail(par),
                r,
                isRoot ? " ← ROOT" : "");
        }
        System.out.println("    └─────────────────────┴─────────────────────┴──────┘");
    }

    // Helper: shorten email for display
    private static String shortEmail(String email) {
        if (email.length() > 19) return email.substring(0, 16) + "...";
        return email;
    }

    // Helper: shorten list for display
    private static String shortenList(List<String> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(shortEmail(list.get(i)));
        }
        sb.append("]");
        return sb.toString();
    }

    // ─────────────────────────────────────────────────────────────
    // MAIN — Run all examples
    // ─────────────────────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 57: Accounts Merge                          ║");
        System.out.println("║  Pattern: Union Find → String Keys → Component Group ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Classic Example ──
        System.out.println("═══ TEST 1: Classic Example ═══");
        List<List<String>> acc1 = new ArrayList<>();
        acc1.add(List.of("John", "john@mail.com", "john_work@mail.com"));
        acc1.add(List.of("John", "john@mail.com", "john00@mail.com"));
        acc1.add(List.of("Mary", "mary@mail.com"));
        acc1.add(List.of("John", "johnny@mail.com"));
        List<List<String>> res1 = accountsMergeWithTrace(acc1);
        System.out.println();

        // ── TEST 2: Transitive Merge ──
        System.out.println("═══ TEST 2: Transitive Merge (Chain) ═══");
        List<List<String>> acc2 = new ArrayList<>();
        acc2.add(List.of("Alice", "a@mail.com", "b@mail.com"));
        acc2.add(List.of("Alice", "b@mail.com", "c@mail.com"));
        acc2.add(List.of("Alice", "c@mail.com", "d@mail.com"));
        List<List<String>> res2 = accountsMergeWithTrace(acc2);
        System.out.println();

        // ── TEST 3: No Merge Needed ──
        System.out.println("═══ TEST 3: No Merge Needed ═══");
        List<List<String>> acc3 = new ArrayList<>();
        acc3.add(List.of("Alice", "alice@mail.com"));
        acc3.add(List.of("Bob", "bob@mail.com"));
        acc3.add(List.of("Charlie", "charlie@mail.com"));
        List<List<String>> res3 = accountsMergeWithTrace(acc3);
        System.out.println();

        // ── TEST 4: All Merge into One ──
        System.out.println("═══ TEST 4: All Accounts Same Person ═══");
        List<List<String>> acc4 = new ArrayList<>();
        acc4.add(List.of("Dan", "x@mail.com", "y@mail.com"));
        acc4.add(List.of("Dan", "y@mail.com", "z@mail.com"));
        acc4.add(List.of("Dan", "z@mail.com", "w@mail.com"));
        List<List<String>> res4 = accountsMergeWithTrace(acc4);
        System.out.println();

        // ── TEST 5: Same Name Different People ──
        System.out.println("═══ TEST 5: Same Name, Different People ═══");
        List<List<String>> acc5 = new ArrayList<>();
        acc5.add(List.of("John", "john1@mail.com"));
        acc5.add(List.of("John", "john2@mail.com"));
        acc5.add(List.of("John", "john3@mail.com"));
        List<List<String>> res5 = accountsMergeWithTrace(acc5);
        System.out.println();

        // ── TEST 6: Complex Web of Connections ──
        System.out.println("═══ TEST 6: Complex Web ═══");
        List<List<String>> acc6 = new ArrayList<>();
        acc6.add(List.of("Alex", "a1@", "a2@", "a3@"));
        acc6.add(List.of("Alex", "a4@", "a5@"));
        acc6.add(List.of("Alex", "a3@", "a4@"));  // bridges group 1 and 2
        acc6.add(List.of("Bob",  "b1@", "b2@"));
        List<List<String>> res6 = accountsMergeWithTrace(acc6);
        System.out.println();

        // ── TEST 7: Verify Union Find vs BFS ──
        System.out.println("═══ TEST 7: Verification — Union Find vs BFS ═══");

        List<List<List<String>>> allTests = List.of(acc1, acc2, acc3, acc4, acc5, acc6);
        boolean allPassed = true;

        for (int t = 0; t < allTests.size(); t++) {
            List<List<String>> ufResult  = accountsMerge(allTests.get(t));
            List<List<String>> bfsResult = accountsMergeBFS(allTests.get(t));

            // Normalize both results for comparison
            List<String> ufNorm  = normalize(ufResult);
            List<String> bfsNorm = normalize(bfsResult);

            boolean match = ufNorm.equals(bfsNorm);
            System.out.println("  Test " + (t + 1) + ": UF groups="
                + ufResult.size() + " BFS groups="
                + bfsResult.size()
                + (match ? " ✓ MATCH" : " ✗ MISMATCH"));

            if (!match) allPassed = false;
        }
        System.out.println(allPassed
            ? "\n  ✅ ALL TESTS PASSED"
            : "\n  ❌ SOME TESTS FAILED");
        System.out.println();

        // ── TEST 8: Performance ──
        System.out.println("═══ TEST 8: Performance Test ═══");
        int numAccounts = 10000;
        int emailsPerAccount = 5;
        List<List<String>> largePerfAccounts = new ArrayList<>();
        java.util.Random rng = new java.util.Random(42);

        for (int i = 0; i < numAccounts; i++) {
            List<String> account = new ArrayList<>();
            account.add("User" + (i % 100));
            for (int j = 0; j < emailsPerAccount; j++) {
                // Some emails shared across accounts to create merges
                int emailId = i * emailsPerAccount + j;
                if (rng.nextDouble() < 0.1 && i > 0) {
                    // 10% chance to reuse an email from earlier account
                    emailId = rng.nextInt(i * emailsPerAccount);
                }
                account.add("email" + emailId + "@mail.com");
            }
            largePerfAccounts.add(account);
        }

        long start, elapsed;

        start = System.nanoTime();
        List<List<String>> ufRes = accountsMerge(largePerfAccounts);
        elapsed = System.nanoTime() - start;
        System.out.println("  Union Find: " + ufRes.size()
            + " groups → " + (elapsed / 1_000_000) + " ms");

        start = System.nanoTime();
        List<List<String>> bfsRes = accountsMergeBFS(largePerfAccounts);
        elapsed = System.nanoTime() - start;
        System.out.println("  BFS:        " + bfsRes.size()
            + " groups → " + (elapsed / 1_000_000) + " ms");
    }

    // Helper: Normalize results for comparison
    private static List<String> normalize(List<List<String>> result) {
        List<String> normalized = new ArrayList<>();
        for (List<String> account : result) {
            List<String> sorted = new ArrayList<>(account.subList(1, account.size()));
            Collections.sort(sorted);
            normalized.add(account.get(0) + ":" + String.join(",", sorted));
        }
        Collections.sort(normalized);
        return normalized;
    }
}