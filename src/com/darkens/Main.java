package com.darkens;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Main {
    static List<String> CORPUS_URLS = new LinkedList<>();
    static List<PipelineObject> documentList = new LinkedList<>();
    static int CORPUS_SIZE;

    public static void main(String[] args) throws IOException {
        //Given corpus
        CORPUS_URLS.add("http://www.multimediaeval.org/mediaeval2019/memorability/");
        CORPUS_URLS.add("https://sites.google.com/view/siirh2020/");


        /*
        Additional similar corpus documents (tf*idf can't function to rank with a corpus of 2 documents)
        (tf*idf can't function to rank with a corpus of 2 documents since log(corpusSize * 2) will be log(2/2) which = 0

            COMMENT OUT IF NOT WANTED TO BE INCLUDED
            SEE "IMPORTANT NOTE" IN DOCUMENTATION
         */
        CORPUS_URLS.add("http://www.multimediaeval.org/why/");
        CORPUS_URLS.add("https://ecir2020.org");
        CORPUS_URLS.add("http://www.multimediaeval.org/mediaeval2018/");
        CORPUS_URLS.add("http://www.multimediaeval.org/mediaeval2017/");


        //For each document, start the pipeline process in a new object
        for(String URL : CORPUS_URLS){
            documentList.add(
                    new PipelineObject(URL)
            );
            CORPUS_SIZE++;
        }

        //Set top keywords for each document. Done in Main() since all documents need
        //to have finished processing before this can be done (tf*idf)
        for(PipelineObject doc : documentList){
            doc.topKeywords = TextProcessing.keywordRanking(documentList, doc);
        }

        //Print data to a txt document each
        int i = 0;
        for(PipelineObject doc : documentList){
            Main.outputToFile("Output# " + i, doc);
            i++;
        }

    }

    static void outputToFile(String name,PipelineObject doc) throws IOException {
        /*
            Simply a method that prints out various parts of data
            from the program in a readable format.
            In a real program, this would be likely be stored in a
            specifically designed and setup database
         */

        File file = new File(name + ".txt");
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

        bufferedWriter.write("#### URL to HTML for this document:  " + doc.theUrl);
        bufferedWriter.newLine();
        bufferedWriter.newLine();

        //Print meta tags supplied by HTML page if there are any
        if(!(doc.webpageMetatagArray.size() == 0)){
            for(String metaTag : doc.webpageMetatagArray){
                bufferedWriter.write(metaTag);
                bufferedWriter.newLine();
            }
        }else{
            bufferedWriter.write("#### Document provided no meta tags in it's HEAD.");
            bufferedWriter.newLine();
            bufferedWriter.newLine();
        }


        //Print top keywords and TFIDF weighting
        bufferedWriter.write("#### Top [20] keywords (and stemmed variants) and weights from tf*idf "); bufferedWriter.newLine();

        for(Map.Entry keyword : doc.topKeywords.entrySet()){
            bufferedWriter.write(TextProcessing.stemmer(keyword.getKey().toString()) + "  (" + keyword.getKey().toString() + ") " + "  - Weight(" + keyword.getValue() + ")");
            bufferedWriter.newLine();
        }

        bufferedWriter.newLine();
        //Print phrase chunks taken from document
        bufferedWriter.write("#### Phrase chunks extracted from document"); bufferedWriter.newLine();
        StringBuilder phraseString = new StringBuilder();
        int i = 0;
        for(String keyPhrase : doc.keywordSet){
            phraseString.append(keyPhrase).append(", ");
            i++;
            if(i == 6){
                bufferedWriter.write(String.valueOf(phraseString));
                phraseString = new StringBuilder();
                bufferedWriter.newLine();
                i = 0;
            }
        }
        bufferedWriter.write(String.valueOf(phraseString));


        bufferedWriter.close();
    }
}


