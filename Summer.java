/**
 * Created by Wezley on 1/23/16.
 *
 * This algorithm is designed to find key words in a piece of text, and then summarize it.
 * The algorithm gets smarter as it summarizes more pages, and learns which words/sentences to avoid.
 *
 */

import java.util.*;

/* This still needs some work. Make it more accurate, and make the TAG_NUMBER and
 * MIN_WORD_LENGTH variable based on the text size. Also, make this open ended so an nlp library
 * can be incorporated at some point to help return better summaries.
 */

public class Summer {

    int TAG_NUMBER = 5;
    int MIN_WORD_LENGTH = 2;

    Map<String, Integer> tagWordRepeats = new HashMap<>();
    List<String> tagWordsToAvoid = new ArrayList<>();

    public String summarizeText(String text)
    {
        String returnText;
        Map<String, Integer> wordsCounted = countWords(text);
        Map<String, Integer> wordsRepeatsRemoved = removeRepeatedTags(wordsCounted);
        String[] mostCommon = findMostCommonWords(wordsRepeatsRemoved);
        findTagWordsToAvoid();
        returnText = summarizeText(text, mostCommon);
        return returnText;
    }

    private String summarizeText(String text, String[] tagWords)
    {
        String returnText = "";
        String[] sentences = text.split("(?<=[.?!])\\s+(?=[a-zA-Z])");
        Map<String, Integer> sentenceSort = sortSentences(sentences, tagWords);
        int sizeOfSummary = 4;
        for(int i = 0; i < sizeOfSummary; i++)
        {
            int maxSumVal = Collections.max(sentenceSort.values());
            for(String key : sentenceSort.keySet())
            {
                if(sentenceSort.get(key) == maxSumVal)
                {
                    returnText += key + " ";
                }
            }
        }
        return returnText;
    }

    private Map<String, Integer> sortSentences(String[] sentences, String[] tagWords)
    {
        Map<String, Integer> sentenceSorted = new HashMap<>();
        for(String sen : sentences)
        {
            sentenceSorted.put(sen, 0);
            for(String word : tagWords)
            {
                if(sen.contains(word))
                {
                    int val = sentenceSorted.get(sen);
                    sentenceSorted.put(sen, val+1);
                }
            }
        }
        return sentenceSorted;
    }

    // Searches through the 'words' map to find the most common words.
    private String[] findMostCommonWords(Map<String, Integer> words)
    {
        String[] tagWords = new String[TAG_NUMBER];
        int curIndex = 0;

        for (int i = 0; i < TAG_NUMBER; i++)
        {
            int maxVal = Collections.max(words.values());
            for (String key : words.keySet())
            {
                if (words.get(key) == maxVal)
                {
                    tagWords[curIndex] = key;
                    words.put(key, 0);
                    addWordToRepeats(key);
                }
            }
            curIndex++;

        }
        return tagWords;
    }

    // Counts how many times each word repeats in the text.
    private Map countWords(String text)
    {
        Map<String, Integer>  wordsCounted = new HashMap<>();
        String[] words = text.split("\\s+");
        for(String word: words)
        {
            if(word.length() > 1)
            {
                if (wordsCounted.containsKey(word) && word.length() > MIN_WORD_LENGTH)
                {
                    int curIndex = wordsCounted.get(word);
                    wordsCounted.put(word, curIndex + 1);
                }
                else
                {
                    wordsCounted.put(word, 1);
                }
            }
        }
        return wordsCounted;
    }

    // Removed the repeated tags from the text body.
    private Map removeRepeatedTags(Map<String, Integer> words)
    {
        Map<String, Integer> returnWords = words;
        for (int i = 0; i < tagWordsToAvoid.size(); i++)
        {
            if (returnWords.containsKey(tagWordsToAvoid.get(i)))
            {
                returnWords.remove(tagWordsToAvoid.get(i));
            }
        }
        return returnWords;
    }

    // Adds tag words to 'avoid' list.
    private void addWordToRepeats(String word)
    {
        if(tagWordRepeats.containsKey(word))
        {
            int curIndex = tagWordRepeats.get(word);
            tagWordRepeats.put(word, curIndex + 1);
        }
        else
        {
            tagWordRepeats.put(word, 1);
        }
    }

    private void findTagWordsToAvoid()
    {
        int maxVal = Collections.max(tagWordRepeats.values());
        int highestVal = tagWordRepeats.size()/maxVal;
        for(String key : tagWordRepeats.keySet())
        {
            if(tagWordRepeats.get(key) >= highestVal)
            {
                if(!tagWordsToAvoid.contains(key))
                {
                    tagWordsToAvoid.add(key);
                }
            }
        }
    }
}
