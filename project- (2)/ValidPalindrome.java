public class ValidPalindrome {

    // ─────────────────────────────────────────────
    // CORE SOLUTION: Two Pointers with Skip Logic
    // ─────────────────────────────────────────────
    public static boolean isPalindrome(String s) {

        // Step 1: Initialize pointers at both ends
        int left = 0;
        int right = s.length() - 1;

        // Step 2: Converge inward (same structure as Project 1)
        while (left < right) {

            // Phase 1: SKIP non-alphanumeric from left
            while (left < right && !Character.isLetterOrDigit(s.charAt(left))) {
                left++;
            }

            // Phase 1: SKIP non-alphanumeric from right
            while (left < right && !Character.isLetterOrDigit(s.charAt(right))) {
                right--;
            }

            // Phase 2: COMPARE characters (case-insensitive)
            char leftChar = Character.toLowerCase(s.charAt(left));
            char rightChar = Character.toLowerCase(s.charAt(right));

            if (leftChar != rightChar) {
                // MISMATCH → not a palindrome → early exit
                return false;
            }

            // Phase 3: ADVANCE both pointers inward
            left++;
            right--;
        }

        // All pairs matched → palindrome confirmed
        return true;
    }

    // ─────────────────────────────────────────────
    // BRUTE FORCE: Clean → Reverse → Compare
    // ─────────────────────────────────────────────
    public static boolean isPalindromeBruteForce(String s) {

        // Step 1: Build cleaned string
        StringBuilder cleaned = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                cleaned.append(Character.toLowerCase(c));
            }
        }

        // Step 2: Reverse the cleaned string
        String original = cleaned.toString();
        String reversed = cleaned.reverse().toString();

        // Step 3: Compare
        return original.equals(reversed);
    }

    // ─────────────────────────────────────────────
    // VARIANT: Valid Palindrome II (at most 1 deletion)
    // ─────────────────────────────────────────────
    public static boolean isPalindromeWithOneDeletion(String s) {

        int left = 0;
        int right = s.length() - 1;

        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                // Mismatch found → try skipping left OR skipping right
                // If EITHER remaining substring is palindrome → true
                return isPalindromeRange(s, left + 1, right)
                    || isPalindromeRange(s, left, right - 1);
            }
            left++;
            right--;
        }

        return true;
    }

    // Helper: check if substring s[left..right] is palindrome
    private static boolean isPalindromeRange(String s, int left, int right) {
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    // ─────────────────────────────────────────────
    // VARIANT: Palindrome Number (no string conversion)
    // ─────────────────────────────────────────────
    public static boolean isPalindromeNumber(int x) {

        // Negative numbers are never palindromes
        if (x < 0) return false;

        // Numbers ending in 0 are not palindromes (except 0 itself)
        if (x != 0 && x % 10 == 0) return false;

        // Reverse the SECOND HALF of the number
        int reversedHalf = 0;
        while (x > reversedHalf) {
            reversedHalf = reversedHalf * 10 + x % 10;
            x /= 10;
        }

        // Compare first half with reversed second half
        // x == reversedHalf → even number of digits
        // x == reversedHalf / 10 → odd number of digits (middle digit ignored)
        return x == reversedHalf || x == reversedHalf / 10;
    }

    // ─────────────────────────────────────────────
    // TRACE FUNCTION — See the convergence happen
    // ─────────────────────────────────────────────
    public static void isPalindromeWithTrace(String s) {

        System.out.println("Input: \"" + s + "\"");
        System.out.println("─────────────────────────────");

        int left = 0;
        int right = s.length() - 1;
        int step = 1;

        while (left < right) {

            // Skip non-alphanumeric from left
            int skippedLeft = 0;
            while (left < right && !Character.isLetterOrDigit(s.charAt(left))) {
                left++;
                skippedLeft++;
            }

            // Skip non-alphanumeric from right
            int skippedRight = 0;
            while (left < right && !Character.isLetterOrDigit(s.charAt(right))) {
                right--;
                skippedRight++;
            }

            // Guard: after skipping, pointers may have crossed
            if (left >= right) {
                System.out.println("Step " + step 
                    + ": Pointers crossed after skipping → all matched");
                break;
            }

            char leftChar = Character.toLowerCase(s.charAt(left));
            char rightChar = Character.toLowerCase(s.charAt(right));

            System.out.println("Step " + step + ":");
            if (skippedLeft > 0) {
                System.out.println("  Skipped " + skippedLeft 
                    + " non-alphanumeric char(s) from left");
            }
            if (skippedRight > 0) {
                System.out.println("  Skipped " + skippedRight 
                    + " non-alphanumeric char(s) from right");
            }
            System.out.println("  left=" + left + " ('" + s.charAt(left) 
                + "' → '" + leftChar + "')"
                + "  right=" + right + " ('" + s.charAt(right) 
                + "' → '" + rightChar + "')");

            if (leftChar != rightChar) {
                System.out.println("  → MISMATCH: '" + leftChar 
                    + "' != '" + rightChar + "' → NOT a palindrome");
                System.out.println("Result: false");
                System.out.println();
                return;
            }

            System.out.println("  → MATCH: '" + leftChar 
                + "' == '" + rightChar + "' ✓");
            left++;
            right--;
            step++;
        }

        System.out.println("All pairs matched → PALINDROME ✓");
        System.out.println("Result: true");
        System.out.println();
    }

    // ─────────────────────────────────────────────
    // MAIN — Run all examples
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 2: Valid Palindrome Check            ║");
        System.out.println("║  Pattern: Two Pointers — Inward + Skip Logic  ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Classic palindrome with noise ──
        System.out.println("═══ TEST 1: Classic Palindrome with Noise ═══");
        isPalindromeWithTrace("A man, a plan, a canal: Panama");

        // ── TEST 2: Not a palindrome ──
        System.out.println("═══ TEST 2: Not a Palindrome ═══");
        isPalindromeWithTrace("race a car");

        // ── TEST 3: Another palindrome ──
        System.out.println("═══ TEST 3: Sentence Palindrome ═══");
        isPalindromeWithTrace("Was it a car or a cat I saw?");

        // ── TEST 4: Empty / whitespace ──
        System.out.println("═══ TEST 4: Empty String ═══");
        isPalindromeWithTrace(" ");

        // ── TEST 5: Single character ──
        System.out.println("═══ TEST 5: Single Character ═══");
        isPalindromeWithTrace("a");

        // ── TEST 6: All non-alphanumeric ──
        System.out.println("═══ TEST 6: All Non-Alphanumeric ═══");
        isPalindromeWithTrace("!@#$%");

        // ── TEST 7: With digits ──
        System.out.println("═══ TEST 7: With Digits ═══");
        isPalindromeWithTrace("1b1");

        // ── TEST 8: Palindrome with one deletion ──
        System.out.println("═══ TEST 8: Palindrome with At Most 1 Deletion ═══");
        String[] deletionTests = {"abca", "abc", "aba", "deeee"};
        for (String test : deletionTests) {
            boolean result = isPalindromeWithOneDeletion(test);
            System.out.println("  \"" + test + "\" → " + result);
        }
        System.out.println();

        // ── TEST 9: Palindrome numbers ──
        System.out.println("═══ TEST 9: Palindrome Numbers ═══");
        int[] numbers = {121, -121, 10, 12321, 0, 1234321, 123};
        for (int num : numbers) {
            boolean result = isPalindromeNumber(num);
            System.out.println("  " + num + " → " + result);
        }
        System.out.println();

        // ── TEST 10: Performance comparison ──
        System.out.println("═══ TEST 10: Performance Comparison ═══");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 500000; i++) {
            sb.append("ab ,! ");
        }
        // Make it a palindrome by appending reverse
        String half = sb.toString();
        String fullPalindrome = half + new StringBuilder(half).reverse().toString();

        long startTime = System.nanoTime();
        boolean r1 = isPalindrome(fullPalindrome);
        long twoPointerTime = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        boolean r2 = isPalindromeBruteForce(fullPalindrome);
        long bruteTime = System.nanoTime() - startTime;

        System.out.println("String length: " + fullPalindrome.length());
        System.out.println("Two Pointers: " + r1 + " → " 
            + (twoPointerTime / 1_000_000) + " ms");
        System.out.println("Brute Force:  " + r2 + " → " 
            + (bruteTime / 1_000_000) + " ms");
        System.out.println();
    }
}