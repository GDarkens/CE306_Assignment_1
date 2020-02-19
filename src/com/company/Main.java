package com.company;

import java.util.LinkedList;
import java.util.List;

public class Main {
    static List<String> CORPUS_URLS = new LinkedList<>();
    static List<PipelineObject> documentList = new LinkedList<>();

    public static void main(String[] args) {
        CORPUS_URLS.add("http://www.multimediaeval.org/mediaeval2019/memorability/");
        CORPUS_URLS.add("https://sites.google.com/view/siirh2020/");



        for(String URL : CORPUS_URLS){
            documentList.add(
                    new PipelineObject(URL)
            );
        }

        for(PipelineObject doc : documentList){
            System.out.println(doc.theUrl);
        }

    }
}


