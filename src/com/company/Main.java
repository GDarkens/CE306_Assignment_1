package com.company;

import edu.stanford.nlp.ling.TaggedWord;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.company.DocumentProcessing.*;
import static com.company.TextProcessing.*;

public class Main {
    static Document webpageDoc;
    static String webpageText;



    static ArrayList<String> webpageBulletArray;
    static ArrayList<String> webpageTableArray;
    static ArrayList<String> webpageSentenceArray;
    static ArrayList<String> webpageMetatagArray;

    static List<TaggedWord> taggedSentenceList;
    static List<TaggedWord> taggedBulletList;
    static List<TaggedWord> taggedTableList;
    static List<TaggedWord> taggedMetatagList;

    public static void main(String[] args) {
        webpageSentenceArray = new ArrayList<>();
        webpageMetatagArray = new ArrayList<>();
        webpageBulletArray = new ArrayList<>();
        webpageTableArray = new ArrayList<>();
        Set<String> keywordSet = new HashSet<>();

        //http://www.multimediaeval.org/mediaeval2019/memorability/
        //https://sites.google.com/view/siirh2020/


        webpageDoc = webpageGrabber("http://www.multimediaeval.org/mediaeval2019/memorability/");
        tablesAndBulletExtract(webpageDoc);
        metaKeywordExtract(webpageDoc);

        webpageText = htmlParse(webpageDoc);

        textPreProcessor(webpageText);

        //Just calls partOfSpeechTagging() on all data I have off the webpage
        tagHandler();

        //Calls nounPhraseChunkHandler() tagged data from tagHandler()
        keywordSet.addAll(nounPhraseChunkHandler());




    }

    static Set<String> nounPhraseChunkHandler(){
        Set<String> phraseSet = new HashSet<>();
        phraseSet.addAll(nounPhraseChunkExtract(taggedSentenceList));
        phraseSet.addAll(nounPhraseChunkExtract(taggedBulletList));
        phraseSet.addAll(nounPhraseChunkExtract(taggedMetatagList));
        phraseSet.addAll(nounPhraseChunkExtract(taggedTableList));


        return phraseSet;
    }

    static void tagHandler(){
        taggedSentenceList = partOfSpeechTagging(webpageSentenceArray);
        taggedBulletList = partOfSpeechTagging(webpageBulletArray);
        taggedMetatagList = partOfSpeechTagging(webpageMetatagArray);
        taggedTableList = partOfSpeechTagging(webpageTableArray);
    }
}
