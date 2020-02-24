package com.darkens;

import edu.stanford.nlp.ling.TaggedWord;
import org.jsoup.nodes.Document;

import java.util.*;

import static com.darkens.DocumentProcessing.*;
import static com.darkens.TextProcessing.*;

class PipelineObject {
    Document webpageDoc;
    String webpageText;

    ArrayList<String> webpageBulletArray;
    ArrayList<String> webpageTableArray;
    ArrayList<String> webpageSentenceArray;
    ArrayList<String> webpageMetatagArray;
    ArrayList<String> stemmedProcessedDoc;

    List<TaggedWord> taggedSentenceList;
    List<TaggedWord> taggedBulletList;
    List<TaggedWord> taggedTableList;
    List<TaggedWord> taggedMetatagList;

    Set<String> keywordSet;
    Map<String, Double> topKeywords;

    String theUrl;

    PipelineObject(String URL){
        /*
            This object represents a document and contains all of its values
            throughout the process.
            Each method called here (e.g. textPreProcessor, webpageDoc) has a description
            of what it does where it is, and is not explained here.
         */
        webpageSentenceArray = new ArrayList<>();
        webpageMetatagArray = new ArrayList<>();
        webpageBulletArray = new ArrayList<>();
        webpageTableArray = new ArrayList<>();
        stemmedProcessedDoc = new ArrayList<>();
        keywordSet = new HashSet<>();

        theUrl = URL;

        webpageDoc = webpageGrabber(URL);

        tablesAndBulletExtract(this, webpageDoc);

        metaKeywordExtract(this, webpageDoc);

        webpageText = htmlParse(webpageDoc);

        textPreProcessor(this, webpageText);

        //Just calls partOfSpeechTagging() on all data I have off the webpage
        tagHandler();
        //Calls nounPhraseChunkHandler() tagged data from tagHandler()
        keywordSet.addAll(nounPhraseChunkHandler());

        documentProcessor(this);

        stemmedProcessedDoc = TextProcessing.documentProcessor(this);

    }

    Set<String> nounPhraseChunkHandler(){
        /*
            Placed in separate method only for neatness
         */
        Set<String> phraseSet = new HashSet<>();
        phraseSet.addAll(nounPhraseChunkExtract(taggedSentenceList));
        phraseSet.addAll(nounPhraseChunkExtract(taggedBulletList));
        phraseSet.addAll(nounPhraseChunkExtract(taggedTableList));
        return phraseSet;
    }

    void tagHandler(){
        /*
            Placed in separate method only for neatness
         */
        taggedSentenceList = partOfSpeechTagging(webpageSentenceArray);
        taggedBulletList = partOfSpeechTagging(webpageBulletArray);
        taggedTableList = partOfSpeechTagging(webpageTableArray);
    }
}


