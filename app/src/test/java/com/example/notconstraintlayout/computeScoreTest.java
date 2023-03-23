package com.example.notconstraintlayout;

import junit.framework.TestCase;

public class computeScoreTest extends TestCase {
    public void testComputeScore() {
        computeScoreTest tester = new computeScoreTest();

        // Test case 1: Test with a simple string value
        int score1 = tester.computeScore("Simple String");
        assertEquals(0, score1);

        // Test case 2: Test with a string value containing repeated hexadecimal digits
        int score2 = tester.computeScore("abccba");
        assertEquals(20, score2);

        // Test case 3: Test with a string value containing only repeated zeros
        int score3 = tester.computeScore("0000");
        assertEquals(8000, score3);

        // Test case 4: Test with a string value containing only repeated ones
        int score4 = tester.computeScore("111111");
        assertEquals(6, score4);

        // Test case 5: Test with a string value containing repeated non-zero hexadecimal digits
        int score5 = tester.computeScore("1f2f1f");
        assertEquals(0, score5);
    }


    public int computeScore(String Value) {
        if (Value.matches("^0+$")) {
            return 8000;
        } else if (Value.matches("^1+$")) {
            return 6;
        } else if (Value.matches("^[2-9a-fA-F]+$")) {
            return 20;
        } else if (Value.matches("^([1-9a-fA-F])\\1+$")) {
            return 3;
        }
        return 0;
    }
}