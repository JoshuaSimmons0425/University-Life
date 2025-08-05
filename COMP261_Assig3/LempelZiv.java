
import java.util.*;

public class LempelZiv {
    /**
     * Take uncompressed input as a text string, compress it, and return it as a
     * text string.
     */
    public static String compress(String input) {
        String compressedText = "";
        int windowSize = 100;
        int lookAhead = 8;
        int currentIndex = 0;
        
        // Continue until the end of the input string is reached
        while(currentIndex < input.length()){
            // Define the bounds of the sliding search window
            int searchStart = Math.max(0, currentIndex - windowSize);
            String searchWindow = input.substring(searchStart, currentIndex);
            
            int maxMatchLength = 0; // Longest match length found
            int bestOffset = 0; // Offset of the best match
            
            // Try to find the longest match within the lookahead buffer
            for(int i = 1; i <= lookAhead; i++){
                if(currentIndex + i > input.length()){
                    break;
                }
                String currentMatch = input.substring(currentIndex, currentIndex + i);
                int matchIndex = searchWindow.indexOf(currentMatch);
                if(matchIndex != -1){ // Update best match if found in search window
                    maxMatchLength = i;
                    bestOffset = currentIndex - (searchStart + matchIndex);
                }
                else{
                    break; // No longer matchable - stop checking longer substrings
                }
            }
            
            // Determine the next unmatched character after the match
            char nextChar;
            if(currentIndex + maxMatchLength < input.length()){
                nextChar = input.charAt(currentIndex + maxMatchLength);
            }
            else{
                nextChar = '\0'; // End-of-input sentinel
            }
            
            // Append the encoded tuple to the compressed string
            //compressedText.append("[").append(bestOffset).append("|").append(maxMatchLength).append("|").append(nextChar).append("]");
            compressedText += "[" + bestOffset + "|" + maxMatchLength + "|" + nextChar + "]"; 
            // Advance the current index past the match and next character
            currentIndex += maxMatchLength + 1;
        }
        
        // Return the compressed string
        return compressedText;
    }

    /**
     * Take compressed input as a text string, decompress it, and return it as a
     * text string.
     */
    public static String decompress(String compressed) {
        String result = "";
        int currentIndex = 0;
        
        // Iterate through the compressed string and extract each tuple
        while (currentIndex < compressed.length()) {
            // Find the next tuple by locating the brackets enclosing the info
            int start = compressed.indexOf('[', currentIndex);
            int end = compressed.indexOf(']', start);

            if (start == -1 || end == -1) break; // Malformed or end of input
            
            // Extract the tuple content and split into components
            String tuple = compressed.substring(start + 1, end);
            String[] parts = tuple.split("\\|");

            int offset = Integer.parseInt(parts[0]);
            int length = Integer.parseInt(parts[1]);
            char nextChar = parts[2].charAt(0);
            
            // Copy the referenced substring from the decoded result
            int matchStart = result.length() - offset;
            if (offset > 0 && length > 0 && matchStart >= 0) {
                for (int i = 0; i < length; i++) {
                    result += result.charAt(matchStart + i);
                }
            }
            
            // Append the nect unmatched character if not a sentinel
            if (nextChar != '\0') {
                result = result + nextChar;
            }
            
            // Move to the next tuple
            currentIndex = end + 1;
        }
        
        // Return the decompressed string
        return result;
    }

    /**
     * The getInformation method is here for your convenience, you don't need to
     * fill it in if you don't want to. It is called on every run and its return
     * value is displayed on-screen. You can use this to print out any relevant
     * information from your compression.
     */
    public String getInformation() {
        return "";
    }
}
