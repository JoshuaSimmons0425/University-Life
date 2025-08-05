/**
 * A new instance of HuffmanCoding is created for every run. The constructor is
 * passed the full text to be encoded or decoded, so this is a good place to
 * construct the tree. You should store this tree in a field and then use it in
 * the encode and decode methods.
 */

import java.util.*;

class Node{
    char character; // Character stored in the node (only for leaf nodes)
    int frequency; // Frequency of the character
    Node left; // Left child (represents '0' in Huffman encoding)
    Node right; // Right child (represents '1' in Huffman encoding)
    
    // Constructor to initialize a node with character and frequency
    Node(char character, int frequency){
        this.character = character; this.frequency = frequency; this.left = null; this.right = null;
    }
}

public class HuffmanCoding {
    /**
     * This would be a good place to compute and store the tree.
     */
    private Node root;   // Root of the Huffman Tree
    private Map<Character, String> charToCode;  // Map from character to its binary Huffman code

    public HuffmanCoding(String text) {
        if(text.isEmpty() || text == null) return;
        
        // Step 1: Build frequency map of each character in input text
        Map<Character, Integer> freqMap = new HashMap<>();
        for (int i = 0; i < text.length(); i++){
            char c = text.charAt(i);
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }
        
        // Step 2: Create a priority queue (min-heap) to build the Huffman Tree
        PriorityQueue<Node> heap = new PriorityQueue<>((a, b) -> {
            if (a.frequency != b.frequency) {
                return a.frequency - b.frequency; // Smaller frequency has higher priority
            } else {
                return Character.compare(a.character, b.character); // Tie-break alphabetically
            }
        });
        
        // Step 3: Create leaf nodes and add to priority queue
        for(Character c: freqMap.keySet()){
            heap.add(new Node(c, freqMap.get(c)));
        }
        
        // Step 4: Build the Huffman Tree by combining two lowest-frequency nodes each time
        while(heap.size() > 1){
            Node left = heap.poll();
            Node right = heap.poll();
            // Merged node gets the sum of frequencies and lowest character for tie-breaking
            Node merged = new Node((char) Math.min(left.character, right.character), left.frequency + right.frequency);
            merged.left = left;
            merged.right = right;
            heap.add(merged);
        }
        
        // Final node is the root of the Huffman Tree
        this.root = heap.poll();
        
        // Step 5: Generate binary codes for each character from the tree
        charToCode = new HashMap<>();
        buildCodeMap(root, "");
    }
    
     /**
     * Recursive method to build the character-to-code map from the Huffman Tree.
     */
    public void buildCodeMap(Node node, String codeSoFar){
        if (node.left == null && node.right == null){  // Base case: if this is a leaf node, store its code
            charToCode.put(node.character, codeSoFar);
            return;
        }
        if (node.left != null){ // Traverse left with '0'
            buildCodeMap(node.left, codeSoFar + "0");
        }
        if(node.right != null){ // Traverse right with '1'
            buildCodeMap(node.right, codeSoFar + "1");
        }
    }

    /**
     * Take an input string, text, and encode it with the stored tree. Should
     * return the encoded text as a binary string, that is, a string containing
     * only 1 and 0.
     */
    public String encode(String text) {
        if (root == null){
            return "";
        }
        String encoded = "";
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            encoded += charToCode.get(c); // Lookup code for each character
        }
        return encoded;
    }

    /**
     * Take encoded input as a binary string, decode it using the stored tree,
     * and return the decoded text as a text string.
     */
    public String decode(String encoded) {
        if(root == null){
            return "";
        }
        String decoded = "";
        Node current = root;
        
        // Traverse the tree according to each bit in the encoded string
        for (int i = 0; i < encoded.length(); i++) {
            char bit = encoded.charAt(i);
            current = bit == '0' ? current.left : current.right;
            
            // If we reach a leaf node, append the character and reset to root
            if (current.left == null && current.right == null) {
                decoded += current.character;
                current = root;
            }
        }
        return decoded;
    }

    /**
     * The getInformation method is here for your convenience, you don't need to
     * fill it in if you don't wan to. It is called on every run and its return
     * value is displayed on-screen. You could use this, for example, to print
     * out the encoding tree.
     */
    public String getInformation() {
        return "";
    }
}
