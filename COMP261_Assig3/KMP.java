/**
 * A new KMP instance is created for every substring search performed. Both the
 * pattern and the text are passed to the constructor and the search method. You
 * could, for example, use the constructor to create the match table and the
 * search method to perform the search itself.
 */
public class KMP {

    /**
     * Perform KMP substring search on the given text with the given pattern.
     * 
     * This should return the starting index of the first substring match if it
     * exists, or -1 if it doesn't.
     */
    public static int search(String pattern, String text) {
        if (pattern.isEmpty()) return 0; // Empty pattern -> matches at index 0
        int[] lps = computeLPS(pattern); // Preprocess the pattern
        int textIndex = 0, patternIndex = 0; // Index declarations for text and pattern
        
        // Traverse the text
        while (textIndex < text.length()) {
            if (text.charAt(textIndex) == pattern.charAt(patternIndex)) { // if both indexes match in char, increment pointers
                textIndex++;
                patternIndex++;
            }
            
            if (patternIndex == pattern.length()) { 
                return textIndex - patternIndex; // found full match 
            } // Mismatch after j matches
            else if (textIndex < text.length() && text.charAt(textIndex) != pattern.charAt(patternIndex)) {
                if (patternIndex != 0) { 
                    patternIndex = lps[patternIndex - 1]; // Use LPS to avoid unnecessary comparisons
                } else {
                    textIndex++; // No prefix to fall back on, increment text index
                }
            }
        }
        return -1; // Match not found
    }
    /**
     * Compute the Longest Prefix Suffix (LPS) array used to skip unnecessary comparisons
     */
    public static int[] computeLPS(String pattern){
        int[] lps = new int[pattern.length()];
        int length = 0; // length of the previous longest prefix suffix
        int index = 1;
        
        // Build the LPS array
        while (index < pattern.length()) {
            if (pattern.charAt(index) == pattern.charAt(length)) {
                length++;
                lps[index] = length;
                index++;
            } else {
                // Fallback if mismatch occurs after some matches
                if (length != 0) {
                    length = lps[length - 1];
                } else {
                    // No match found; set lps[i] to 0 and move on
                    lps[index] = 0;
                    index++;
                }
            }
        }
        return lps;
    }
}
