package com.company;

import org.jsoup.nodes.Document;
import java.util.*;

import static com.company.DocumentProcessing.*;
import static com.company.TextProcessing.*;

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


        for(String s : webpageBulletArray){
            System.out.println(s);
        }
        for(String s : webpageTableArray){
            System.out.println(s);
        }
        for(String s : webpageSentenceArray){
            System.out.println(s);
        }
        for(String s : webpageMetatagArray){
            System.out.println(s);
        }

    }
}
