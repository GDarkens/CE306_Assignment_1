package com.company;

import org.jsoup.nodes.Document;

import java.util.ArrayList;

import static com.company.DocumentProcessing.*;
import static com.company.TextProcessing.partOfSpeechTagging;
import static com.company.TextProcessing.textPreProcessor;

public class Main {
    static Document webpageDoc;
    static String webpageText;



    static ArrayList<String> webpageBulletArray;
    static ArrayList<String> webpageTableArray;
    static ArrayList<String> webpageSentenceArray;
    static ArrayList<String> webpageMetatagArray;

    public static void main(String[] args) {
        webpageSentenceArray = new ArrayList<>();
        webpageMetatagArray = new ArrayList<>();
        webpageBulletArray = new ArrayList<>();
        webpageTableArray = new ArrayList<>();


        webpageDoc = webpageGrabber("https://sites.google.com/view/siirh2020/");
        tablesAndBulletExtract(webpageDoc);
        metaKeywordExtract(webpageDoc);

        webpageText = htmlParse(webpageDoc);

        textPreProcessor(webpageText);


        partOfSpeechTagging(webpageSentenceArray);
    }
}
