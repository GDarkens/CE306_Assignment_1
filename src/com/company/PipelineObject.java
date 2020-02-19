package com.company;

import edu.stanford.nlp.ling.TaggedWord;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.company.DocumentProcessing.*;
import static com.company.TextProcessing.*;

class PipelineObject {
    Document webpageDoc;
    String webpageText;

    ArrayList<String> webpageBulletArray;
    ArrayList<String> webpageTableArray;
    ArrayList<String> webpageSentenceArray;
    ArrayList<String> webpageMetatagArray;

    List<TaggedWord> taggedSentenceList;
    List<TaggedWord> taggedBulletList;
    List<TaggedWord> taggedTableList;
    List<TaggedWord> taggedMetatagList;

    Set<String> keywordSet = new HashSet<>();

    String theUrl;

    PipelineObject(String URL){
        webpageSentenceArray = new ArrayList<>();
        webpageMetatagArray = new ArrayList<>();
        webpageBulletArray = new ArrayList<>();
        webpageTableArray = new ArrayList<>();
        keywordSet = new HashSet<>();

        theUrl = URL;


        webpageDoc = webpageGrabber(URL);

        tablesAndBulletExtract(this, webpageDoc);
        metaKeywordExtract(this,webpageDoc);

        webpageText = htmlParse(webpageDoc);

        textPreProcessor(this,webpageText);

        //Just calls partOfSpeechTagging() on all data I have off the webpage
        tagHandler();

        //Calls nounPhraseChunkHandler() tagged data from tagHandler()
        keywordSet.addAll(nounPhraseChunkHandler());

        //tfIdf(webpageSentenceArray, "and");

    }

    Set<String> nounPhraseChunkHandler(){
        Set<String> phraseSet = new HashSet<>();
        phraseSet.addAll(nounPhraseChunkExtract(taggedSentenceList));
        phraseSet.addAll(nounPhraseChunkExtract(taggedBulletList));
        phraseSet.addAll(nounPhraseChunkExtract(taggedMetatagList));
        phraseSet.addAll(nounPhraseChunkExtract(taggedTableList));


        return phraseSet;
    }

    void tagHandler(){
        taggedSentenceList = partOfSpeechTagging(webpageSentenceArray);
        taggedBulletList = partOfSpeechTagging(webpageBulletArray);
        taggedMetatagList = partOfSpeechTagging(webpageMetatagArray);
        taggedTableList = partOfSpeechTagging(webpageTableArray);
    }

}


