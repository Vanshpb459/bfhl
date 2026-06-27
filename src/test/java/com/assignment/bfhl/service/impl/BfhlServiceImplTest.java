package com.assignment.bfhl.service.impl;

import com.assignment.bfhl.dto.BfhlRequest;
import com.assignment.bfhl.dto.BfhlResponse;
import com.assignment.bfhl.service.impl.BfhlServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BfhlServiceImplTest {

    private BfhlServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new BfhlServiceImpl();
    }

    // -------------------------------------------------------------------------
    // processData — happy path
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("processData: categorises numbers, alphabets, and special chars correctly")
    void processData_fullMixedInput() {
        BfhlRequest request = new BfhlRequest();
        request.setData(Arrays.asList("a", "1", "334", "4", "R", "$"));

        BfhlResponse response = service.processData(request);

        assertThat(response.isSuccess()).isTrue();

        // Numbers
        assertThat(response.getOddNumbers()).containsExactly("1");
        assertThat(response.getEvenNumbers()).containsExactlyInAnyOrder("334", "4");

        // Alphabets — returned as uppercase
        assertThat(response.getAlphabets()).containsExactlyInAnyOrder("A", "R");

        // Special chars
        assertThat(response.getSpecialCharacters()).containsExactly("$");

        // Sum: 1 + 334 + 4 = 339
        assertThat(response.getSum()).isEqualTo("339");
    }

    @Test
    @DisplayName("processData: returns is_success true")
    void processData_alwaysReturnsSuccess() {
        BfhlRequest request = new BfhlRequest();
        request.setData(Collections.singletonList("1"));

        BfhlResponse response = service.processData(request);

        assertThat(response.isSuccess()).isTrue();
    }

    @Test
    @DisplayName("processData: empty data list produces zero sum and empty categories")
    void processData_emptyList() {
        BfhlRequest request = new BfhlRequest();
        request.setData(Collections.emptyList());

        BfhlResponse response = service.processData(request);

        assertThat(response.getSum()).isEqualTo("0");
        assertThat(response.getOddNumbers()).isEmpty();
        assertThat(response.getEvenNumbers()).isEmpty();
        assertThat(response.getAlphabets()).isEmpty();
        assertThat(response.getSpecialCharacters()).isEmpty();
        assertThat(response.getConcatString()).isEmpty();
    }

    // -------------------------------------------------------------------------
    // isNumeric
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("isNumeric: single-digit strings")
    void isNumeric_singleDigit() {
        assertThat(service.isNumeric("0")).isTrue();
        assertThat(service.isNumeric("9")).isTrue();
    }

    @Test
    @DisplayName("isNumeric: multi-digit strings")
    void isNumeric_multiDigit() {
        assertThat(service.isNumeric("334")).isTrue();
        assertThat(service.isNumeric("1000")).isTrue();
    }

    @Test
    @DisplayName("isNumeric: non-numeric strings")
    void isNumeric_nonNumeric() {
        assertThat(service.isNumeric("a")).isFalse();
        assertThat(service.isNumeric("$")).isFalse();
        assertThat(service.isNumeric("1a")).isFalse();
        assertThat(service.isNumeric("")).isFalse();
        assertThat(service.isNumeric(null)).isFalse();
    }

    // -------------------------------------------------------------------------
    // isAlphabetic
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("isAlphabetic: single letters")
    void isAlphabetic_singleLetter() {
        assertThat(service.isAlphabetic("a")).isTrue();
        assertThat(service.isAlphabetic("Z")).isTrue();
    }

    @Test
    @DisplayName("isAlphabetic: multi-char alpha strings")
    void isAlphabetic_multiChar() {
        assertThat(service.isAlphabetic("hello")).isTrue();
        assertThat(service.isAlphabetic("WORLD")).isTrue();
    }

    @Test
    @DisplayName("isAlphabetic: non-alpha strings")
    void isAlphabetic_nonAlpha() {
        assertThat(service.isAlphabetic("1")).isFalse();
        assertThat(service.isAlphabetic("$")).isFalse();
        assertThat(service.isAlphabetic("a1")).isFalse();
        assertThat(service.isAlphabetic("")).isFalse();
        assertThat(service.isAlphabetic(null)).isFalse();
    }

    // -------------------------------------------------------------------------
    // buildConcatString
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("buildConcatString: reverses chars then applies alternating caps")
    void buildConcatString_basicCase() {
        // alphabets (already upper-cased): ["A", "R"]
        // chars in order: A, R  → reversed: R, A
        // alternating:  index 0 → Upper(R)='R', index 1 → Lower(A)='a'  → "Ra"
        String result = service.buildConcatString(Arrays.asList("A", "R"));
        assertThat(result).isEqualTo("Ra");
    }

    @Test
    @DisplayName("buildConcatString: empty list returns empty string")
    void buildConcatString_emptyList() {
        assertThat(service.buildConcatString(Collections.emptyList())).isEmpty();
    }

    @Test
    @DisplayName("buildConcatString: single character returns uppercase")
    void buildConcatString_singleChar() {
        // index 0 → Upper
        assertThat(service.buildConcatString(List.of("B"))).isEqualTo("B");
    }

    @Test
    @DisplayName("buildConcatString: multi-char word in one entry")
    void buildConcatString_multiCharEntry() {
        // alphabets: ["ABC"]  → chars: A,B,C  → reversed: C,B,A
        // alternating: C(upper), b(lower), A(upper) → "CbA"
        String result = service.buildConcatString(List.of("ABC"));
        assertThat(result).isEqualTo("CbA");
    }

    @Test
    @DisplayName("buildConcatString: even number of characters")
    void buildConcatString_evenChars() {
        // alphabets: ["AB", "CD"]  → chars: A,B,C,D  → reversed: D,C,B,A
        // alternating: D,c,B,a → "DcBa"
        String result = service.buildConcatString(List.of("AB", "CD"));
        assertThat(result).isEqualTo("DcBa");
    }

    // -------------------------------------------------------------------------
    // Sum edge cases
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("processData: sum of only odd numbers")
    void processData_onlyOddNumbers() {
        BfhlRequest request = new BfhlRequest();
        request.setData(Arrays.asList("1", "3", "5"));

        BfhlResponse response = service.processData(request);

        assertThat(response.getSum()).isEqualTo("9");
        assertThat(response.getOddNumbers()).containsExactlyInAnyOrder("1", "3", "5");
        assertThat(response.getEvenNumbers()).isEmpty();
    }

    @Test
    @DisplayName("processData: only special characters produce zero sum")
    void processData_onlySpecialChars() {
        BfhlRequest request = new BfhlRequest();
        request.setData(Arrays.asList("$", "@", "!"));

        BfhlResponse response = service.processData(request);

        assertThat(response.getSum()).isEqualTo("0");
        assertThat(response.getSpecialCharacters()).containsExactlyInAnyOrder("$", "@", "!");
    }
}
