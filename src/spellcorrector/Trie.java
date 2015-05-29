/*
 * The MIT License
 *
 * Copyright 2015 Sidharth Manohar.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package spellcorrector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An implementation of Trie Datastructure
 *
 * @author Sidharth Manohar
 */
public class Trie {

    final private Node root;

    /**
     * Constructor initializes root node of Trie with dummy character '$'
     */
    public Trie() {
        this.root = new Node('$');
    }

    /**
     * Adds the given word to the Trie Datastructure.
     *
     * @param word
     */
    public void addWord(String word) {
        addWord(root, word);
    }

    /**
     * 
     * Helper method that performs the actual addition of word to Trie 
     * datastructure.
     * 
     */
    private void addWord(Node root, String word) {
        if (word.length() == 0) {
            root.endOfWord = true;
            return;
        }

        //create a new node for the first letter of word if its not already 
        //present
        if (!root.next.containsKey(word.charAt(0))) {
            root.next.put(word.charAt(0), new Node((word.charAt(0))));
        }

        //recursively add remaining letters in the word
        addWord(root.next.get(word.charAt(0)), word.substring(1));
    }

    /**
     * Checks whether the given word is contained in the Trie Datastructure
     * 
     * @param word The word to be searched for.
     * @return True if this Trie contains word. False otherwise.
     */
    public boolean contains(String word) {
        return contains(root, word);
    }

    private boolean contains(Node root, String word) {

        if (word.length() == 0) {
            return root.endOfWord;
        }

        if (root.next.containsKey(word.charAt(0))) {
            return contains(root.next.get(word.charAt(0)), word.substring(1));
        }
        return false;
    }

    /**
     *
     * @param word
     * @param maxMistakes
     * @return
     */
    public List<String> suggestCorrectSpellings(String word, int maxMistakes) {
        List<String> correctWords = new ArrayList<>();
        suggestCorrectSpellings(correctWords, root, word, "",
                maxMistakes);
        return correctWords;
    }

    private void suggestCorrectSpellings(List<String> correctWords, Node root,
            String actualWord, String currentWord, int maxMistakes) {
        if (maxMistakes < 0) {
            return;
        }
        if (actualWord.length() == 0) {
            if (root.endOfWord) {
                correctWords.add(currentWord);
            }
            return;
        }

        root.next.keySet().stream().forEach((c) -> {
            int mistakesRemaining = maxMistakes;
            if (c != actualWord.charAt(0)) {
                mistakesRemaining--;
            }
            suggestCorrectSpellings(correctWords,
                    root.next.get(c),
                    actualWord.substring(1),
                    currentWord + c,
                    mistakesRemaining
            );
        });
    }

    /**
     * A class to represents the nodes of Trie Datastructure
     */
    private class Node {

        public Map<Character, Node> next;
        public Character c;
        public boolean endOfWord;

        Node(char c) {
            this.next = new HashMap<>();
            this.endOfWord = false;
            this.c = c;
        }
    }
}
