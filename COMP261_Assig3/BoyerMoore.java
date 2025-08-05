import java.util.*;

public class BoyerMoore{
    /**
     * Searches for the first occurance of the pattern in the given text using Boyer-Moore bad character heuristic 
     */
    public static int search(String pattern, String text) {
        
        if(pattern.isEmpty()){return 0;} // An empty pattern is always found at index 0
        if(pattern.length() > text.length()) {return -1;} // Pattern longer than the text cannot be found
        
        int patternLength = pattern.length();
        int textLength = text.length();
        
        // Bad character heuistic table: Stores the last occurance index of each character in the pattern
        int[] badChar = new int[256]; 
        Arrays.fill(badChar, -1); // Initialize all characters as not present
        
        // Fill the table with the righmost occurance of each character in the pattern
        for(int i = 0; i < patternLength; i++){
            badChar[pattern.charAt(i)] = i;
        }
        
        int[] goodSuffix = buildGoodSuffixTable(pattern); // Build the good suffix table with the pattern
        int shift = 0; // Start comparing from the beginning of the text
        while (shift <= textLength - patternLength){
            int j = patternLength - 1;
            while(j >= 0 && pattern.charAt(j) == text.charAt(shift + j)){
                j--;
            }
            if(j < 0){
                return shift;
            } else {
                char mismatchChar = text.charAt(shift + j);
                int badCharShift = j - badChar[mismatchChar];
                int goodSuffixShift = goodSuffix[j];
                shift += Math.max(1, Math.max(badCharShift, goodSuffixShift));
            }
        }
        return -1;
    }
    /**
     * Builds the good suffix shift table for the Boyer-Moore algorithm
     */
    private static int[] buildGoodSuffixTable(String pattern) {
        int patternLength = pattern.length();
        int[] goodSuffix = new int[patternLength + 1];
        int[] borderPos = new int[patternLength + 1];

        int i = patternLength;
        int j = patternLength + 1;
        borderPos[i] = j;

        // First phase: strong suffix rule
        while (i > 0) {
            while (j <= patternLength && pattern.charAt(i - 1) != pattern.charAt(j - 1)) {
                if (goodSuffix[j] == 0) {
                    goodSuffix[j] = j - i;
                }
                j = borderPos[j];
            }
            i--;
            j--;
            borderPos[i] = j;
        }

        // Second phase: fill in gaps
        j = borderPos[0];
        for (i = 0; i <= patternLength; i++) {
            if (goodSuffix[i] == 0) {
                goodSuffix[i] = j;
            }
            if (i == j) {
                j = borderPos[j];
            }
        }

        // Adjust for correct array size
        return Arrays.copyOfRange(goodSuffix, 1, patternLength + 1);
    }
}