package com.assignment.bfhl.service.impl;

import com.assignment.bfhl.dto.BfhlRequest;
import com.assignment.bfhl.dto.BfhlResponse;
import com.assignment.bfhl.service.BfhlService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BfhlServiceImpl implements BfhlService {

    // TODO: Replace with your actual details
    private static final String USER_ID     = "Vansh";   
    private static final String EMAIL       = "vansh1562.be23@chitkarauniversity.edu.in";
    private static final String ROLL_NUMBER = "2311981562";

    @Override
    public BfhlResponse processData(BfhlRequest request) {
        List<String> data = request.getData();

        List<String> oddNumbers       = new ArrayList<>();
        List<String> evenNumbers      = new ArrayList<>();
        List<String> alphabets        = new ArrayList<>();
        List<String> specialChars     = new ArrayList<>();
        long         numericalSum     = 0;

        for (String item : data) {
            if (isNumeric(item)) {
                long value = Long.parseLong(item);
                numericalSum += value;
                if (value % 2 == 0) {
                    evenNumbers.add(item);
                } else {
                    oddNumbers.add(item);
                }
            } else if (isAlphabetic(item)) {
                alphabets.add(item.toUpperCase());
            } else {
                specialChars.add(item);
            }
        }

        String sum          = String.valueOf(numericalSum);
        String concatString = buildConcatString(alphabets);

        return BfhlResponse.builder()
                .isSuccess(true)
                .userId(USER_ID)
                .email(EMAIL)
                .rollNumber(ROLL_NUMBER)
                .oddNumbers(oddNumbers)
                .evenNumbers(evenNumbers)
                .alphabets(alphabets)
                .specialCharacters(specialChars)
                .sum(sum)
                .concatString(concatString)
                .build();
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    /**
     * Returns true when every character in the token is a digit (supports
     * multi-digit numbers like "334").
     */
    boolean isNumeric(String token) {
        if (token == null || token.isEmpty()) return false;
        for (char c : token.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    /**
     * Returns true when every character in the token is a letter (a-z / A-Z).
     * Handles multi-character alphabet strings.
     */
    boolean isAlphabetic(String token) {
        if (token == null || token.isEmpty()) return false;
        for (char c : token.toCharArray()) {
            if (!Character.isLetter(c)) return false;
        }
        return true;
    }

    /**
     * Builds the concat_string:
     * 1. Collect every individual alphabetic character from the (already
     *    upper-cased) alphabet list.
     * 2. Reverse the resulting character sequence.
     * 3. Apply alternating caps: index 0 → Upper, index 1 → Lower, index 2 →
     *    Upper, …
     *
     * Example: alphabets = ["A", "R"]  →  chars = ['A','R']  →  reversed =
     * ['R','A']  →  alternating = "Ra"
     */
    String buildConcatString(List<String> upperCasedAlphabets) {
        // 1. Flatten all characters
        StringBuilder allChars = new StringBuilder();
        for (String word : upperCasedAlphabets) {
            allChars.append(word);   // already upper-cased by caller
        }

        // 2. Reverse
        String reversed = allChars.reverse().toString();

        // 3. Alternating caps
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < reversed.length(); i++) {
            char c = reversed.charAt(i);
            result.append(i % 2 == 0
                    ? Character.toUpperCase(c)
                    : Character.toLowerCase(c));
        }

        return result.toString();
    }
}
