/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList;
    private HashSet<String> wordSet;
    private HashMap<Integer, ArrayList<String>> sizeToWords;
    private HashMap<String, ArrayList<String>> lettersToWord;
    private int wordLength = DEFAULT_WORD_LENGTH;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        wordList = new ArrayList<>();
        wordSet = new HashSet<>();
        sizeToWords = new HashMap<>();
        lettersToWord = new HashMap<>();

        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            String sorted = sortLetters(word);
            if (lettersToWord.containsKey(sorted)) {
                lettersToWord.get(sorted).add(word);
            }
            else {
                ArrayList<String> vals = new ArrayList<>();
                vals.add(word);
                lettersToWord.put(sorted, vals);
            }

            int l = sorted.length();
            if (sizeToWords.containsKey(l)) {
                sizeToWords.get(l).add(word);
            }
            else {
                ArrayList<String> temp = new ArrayList<>();
                temp.add(word);
                sizeToWords.put(l, temp);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && !word.contains(base);
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String sorted = sortLetters(targetWord);
        for (String s : wordList) {
            if (sorted.equals(sortLetters(s))) {
                result.add(s);
            }
        }
        return result;
    }

    public String sortLetters(String word) {
        char[] tempArray = word.toCharArray();
        Arrays.sort(tempArray);
        return new String(tempArray);
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String added;
        for (char i = 'a'; i <= 'z'; i++) {
            added = sortLetters(word.concat("" + i));
            if (lettersToWord.containsKey(added)) {
                result.addAll(lettersToWord.get(added));
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        ArrayList<String> t = sizeToWords.get(wordLength);

        if(wordLength < MAX_WORD_LENGTH) {
            wordLength++;
        }

        while (true) {
            String w = t.get(random.nextInt(t.size()));
            if(getAnagramsWithOneMoreLetter(w).size() >= MIN_NUM_ANAGRAMS) {
                return w;
            }
        }
    }
}
