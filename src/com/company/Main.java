package com.company;

import edu.stanford.nlp.ling.TaggedWord;

import java.util.*;

public class Main {
    static ArrayList<String> webpageBulletArray;
    static ArrayList<String> webpageTableArray;
    static ArrayList<String> webpageSentenceArray;
    static ArrayList<String> webpageMetatagArray;

    static List<TaggedWord> taggedSentenceList;
    static List<TaggedWord> taggedBulletList;
    static List<TaggedWord> taggedTableList;
    static List<TaggedWord> taggedMetatagList;

    static List<String> CORPUS_URLS = new LinkedList<>();


    public static void main(String[] args) {
        webpageSentenceArray = new ArrayList<>();
        webpageMetatagArray = new ArrayList<>();
        webpageBulletArray = new ArrayList<>();
        webpageTableArray = new ArrayList<>();
        Set<String> keywordSet = new HashSet<>();

        CORPUS_URLS.add("http://www.multimediaeval.org/mediaeval2019/memorability/");
        CORPUS_URLS.add("https://sites.google.com/view/siirh2020/");


        List<PipelineObject> documentList = new LinkedList<>();
        for(String URL : CORPUS_URLS){
            documentList.add(new PipelineObject(URL));
        }



        for(PipelineObject doc : documentList){
            System.out.println(doc.theUrl);
        }

    }
}


