package com.company;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.SentenceUtils;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.text.BreakIterator;
import java.util.*;

import static com.company.Main.webpageSentenceArray;

class TextProcessing {

    static String stopWordRemoval(String textString){
        /*
            https://gist.github.com/sebleier/554280 - Natural Language Toolkit (NLTK) Stopwords
            The list of stopwords here is a slightly modified version of the above link.

            Any String passed in will be split, have stop words removed, recombined and
            returned back.
        */
        List<String> stopWords = Arrays.asList("i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
                "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its", "itself",
                "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that", "these", "those",
                "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having", "do", "does", "did",
                "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while", "of", "at", "by", "for",
                "with", "about", "against", "between", "into", "through", "during", "before", "after", "above", "below", "to",
                "from", "up", "down", "in", "out", "on", "off", "over", "under", "again", "further", "then", "once", "here",
                "there", "when", "where", "why", "how", "all", "any", "both", "each", "few", "more", "most", "other", "some",
                "such", "no", "nor", "not", "only", "own", "same", "so", "than", "too", "very", "s", "t", "can", "will", "just",
                "don", "should", "now");

        ArrayList<String> stringCollection = new ArrayList<>();
        Collections.addAll(stringCollection, textString.split(" "));

        stringCollection.removeAll(stopWords);

        //Recombine split sentences back after the .split() earlier
        StringJoiner stringJoiner = new StringJoiner(" ");
        for(String string : stringCollection){
            stringJoiner.add(string);
        }

        return stringJoiner.toString();
    }

    static void textPreProcessor(String webpageText){
        /*
            Splits sentences on punctuation - determined by BreakIterator
            so it is less likely to break on titles like "Dr." or other
            punctuation.
            Case fold all words.
        */
        BreakIterator breakIterator = BreakIterator.getSentenceInstance(Locale.ENGLISH);
        webpageSentenceArray = new ArrayList<>();


        breakIterator.setText(webpageText);
        int start = breakIterator.first();

        //For each sentence, before adding - remove stop words and make it lower case (case fold)
        for(int finish = breakIterator.next(); finish != BreakIterator.DONE; start = finish, finish = breakIterator.next()) {
            webpageSentenceArray.add(
                    webpageText.substring(start, finish).toLowerCase(Locale.ENGLISH)
            );
        }
    }

    static List<TaggedWord> partOfSpeechTagging(ArrayList<String> arrayToTag){
        /*
            https://nlp.stanford.edu/software/tagger.shtml - "Stanford Log-linear Part-Of-Speech Tagger"
            Given an array, this method will take each String entry and feed it to the POS tagger, which
            will return the tagged words in a new LinkedList.
        */

        //There are other modelFile options, but I found this trained model to be sufficiently fast and accurate
        MaxentTagger speechTagger = new MaxentTagger("./assets/english-left3words-distsim.tagger");
        List<HasWord> untaggedWordsList;
        List<TaggedWord> taggedWordsListTemp;
        List<TaggedWord> taggedWordsListFinal = new LinkedList<>();

        for(String sentence : arrayToTag){
            untaggedWordsList = SentenceUtils.toWordList(sentence.split(" "));
            taggedWordsListTemp = speechTagger.tagSentence(untaggedWordsList);
            taggedWordsListFinal.addAll(taggedWordsListTemp);
        }


        return taggedWordsListFinal;
    }


}
