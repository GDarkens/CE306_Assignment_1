package com.darkens;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.SentenceUtils;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.text.BreakIterator;
import java.util.*;

import static java.lang.Math.log;


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

    static void textPreProcessor(PipelineObject workingDoc, String webpageText){
        /*
            Splits sentences on punctuation - determined by BreakIterator
            so it is less likely to break on titles like "Dr." or other
            punctuation.
            Case fold all words.
        */
        BreakIterator breakIterator = BreakIterator.getSentenceInstance(Locale.ENGLISH);
        workingDoc.webpageSentenceArray = new ArrayList<>();


        breakIterator.setText(webpageText);
        int start = breakIterator.first();

        //For each sentence, before adding make it lower case (case fold)
        for(int finish = breakIterator.next(); finish != BreakIterator.DONE; start = finish, finish = breakIterator.next()) {
            workingDoc.webpageSentenceArray.add(
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

        for(String arrayEntry : arrayToTag){
            untaggedWordsList = SentenceUtils.toWordList(arrayEntry.split(" "));
            taggedWordsListTemp = speechTagger.tagSentence(untaggedWordsList);
            taggedWordsListFinal.addAll(taggedWordsListTemp);
        }



        return taggedWordsListFinal;
    }
    static Set<String> nounPhraseChunkExtract(List<TaggedWord> taggedWordList){
        /*

            Given a list of tagged words, this function will read through and extract
            noun-phrase chunks (NPs).
            These NPs will be used as keywords for the document.

            Helpful list of POS tags https://www.ling.upenn.edu/courses/Fall_2003/ling001/penn_treebank_pos.html

        */
        boolean phraseBuildStatus = false;
        StringBuilder phrase = new StringBuilder();
        Set<String> phraseSet = new HashSet<>();

        for(TaggedWord word : taggedWordList){
            if (!phraseBuildStatus) {
                //Looking for a Determiner (a, the, those...) or just any type of Adjective - JJS = comparative Adjective & JJR = superlative Adjective
                        //Previously considered starting with an optional Determiner (a, the, those) but lead to too many bad keywords
                if (word.tag().startsWith("JJ")) {
                    phraseBuildStatus = true;
                    phrase.append(word.word()).append(" ");
                }

            } else {
                //Looking for any of the 3 types of Adjective - JJS = comparative Adjective & JJR = superlative Adjective
                if (word.tag().equals("JJ") || word.tag().equals("JJS")|| word.tag().equals("JJR")) {
                    phrase.append(word.word()).append(" ");

                    /*
                    If there is a Noun of any kind, then close the phrase.
                        Nouns are either NN, NNS (plural), NNP (proper singular) or NNPS (proper plural), this is why I use .startsWith("NN")
                    */
                } else if (word.tag().startsWith("NN")) {
                    phrase.append(word.word()).append(" ");
                    phraseBuildStatus = false;
                    phraseSet.add(phrase.toString().replaceAll("[^\\w\\s-]", "")); //Removes punctuation etc but keeps hyphens
                    phrase = new StringBuilder();

                } else {
                    //Bail from creating phrase - not a NP chunk - reset loop and clear phrase buffer
                    phraseBuildStatus = false;
                    phrase = new StringBuilder();
                }
            }
        }
        return phraseSet;
    }

    static double tfIdf(List<String> document, String term, List<PipelineObject> corpus){
        /*
            Calls the termFreq and inverseDocFreq sub functions to
            calculate the tf*idf of a term across all documents in corpus
         */
        double tf = termFreq(document, term);
        double idf = inverseDocFreq(corpus, term);

        return tf*idf;
    }

    static double termFreq(List<String> document, String term){
        /*
            Given a document and a term, the function will count the amount of
            times that it appears in a given document and return that value.
         */
        double freq = 0;
        double docSize = 0;
        term = term.toLowerCase();

        double tf = 0;

        //Iterating over each sentence, then each word
        for (String sentence : document) {
            for(String word : sentence.split(" ")){
                docSize ++;
                if (term.equals(word))
                    freq++;
            }
        }

        tf = freq / docSize;

        return tf;
    }

    static double inverseDocFreq(List<PipelineObject> corpus, String term){
        /*
            Given a List of PipelineObjects as the document corpus and a term,
            the function will calculate the IDF of the term across the corpus
         */
        double freq = 0;
        term = term.toLowerCase();


        //Iterating over each document, then each word in it
        for(PipelineObject document: corpus){
            for(String word : document.webpageText.split(" ")){
                if(term.equals(word)){
                    freq++;
                    break; //If the doc has the phrase, no point continuing for IDF
                }
            }
        }

        return log(corpus.size() / freq);
    }

    static void keywordRanking(List<PipelineObject> corpus){
        String bestKeyword = "";
        double bestKeywordVal = 0;

        for(PipelineObject document: corpus){
            for(String keyword : document.keywordSet){
                if(tfIdf(document.webpageSentenceArray, keyword, corpus) > bestKeywordVal){
                    bestKeyword = keyword;
                    bestKeywordVal = tfIdf(document.webpageSentenceArray, keyword, corpus);
                }
            }
        }

        System.out.println("Best keyword: " + bestKeyword);
        System.out.println("Value: " + bestKeywordVal);
    }
}