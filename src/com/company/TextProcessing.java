package com.company;

import java.text.BreakIterator;
import java.util.*;

import static com.company.Main.*;

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

}
