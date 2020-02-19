package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import static com.company.TextProcessing.stopWordRemoval;

class DocumentProcessing {

    static Document webpageGrabber(String url){
        /*
            Given a URL, attempt to return a JSoup Document of
            the page - unaltered and in full.
        */
        Document webpageDoc = null;

        try{
            webpageDoc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error in grabbing given webpage with JSoup");
        }

        //Return the page /or/ it will return NULL if there's an issue.
        return webpageDoc;
    }

    static String htmlParse(Document webpage){
        /*
            Given a JSoup type Document, it this returns only
            the text of the webpage - all markdown info removed,
            essentially in the format of one large string.
         */
        String webpageText;
        Parser htmlParser = Parser.htmlParser();

        webpageText = webpage.parser(htmlParser)
                .text();

        return webpageText;
    }

    static void tablesAndBulletExtract(PipelineObject workingDoc, Document webpageDoc){
        /*
            From a given Document, select Table and Bullet Point
            elements and place them in an array after removing markdown.
            Then - remove those elements from the main Document.

            Also checks against the stopWordRemoval() method before adding
                (Note it checks against it but does not apply at this point,
                 lines with stop words only are not added)
         */
        Elements docElements;
        workingDoc.webpageTableArray = new ArrayList<>();
        workingDoc.webpageBulletArray = new ArrayList<>();

        //Table extraction
        docElements = webpageDoc.getElementsByTag("table");

        for(String line : docElements.toString().split("</tr>")){
            line = Jsoup.parse(line).text();
            if(!(stopWordRemoval(line).equals(""))){
                workingDoc.webpageTableArray.add(line
                        .toLowerCase());
            }
        }
        //Remove all table entries from the main document now I have grabbed them
        webpageDoc.select("table").remove();



        //List (bullet point) extraction
        docElements = webpageDoc.getElementsByTag("li");

        for(String line : docElements.toString().split("</li>")){
            line = Jsoup.parse(line).text();
            if(!(stopWordRemoval(line).equals(""))){
                workingDoc.webpageBulletArray.add(line
                        .toLowerCase());
            }
        }
        //Remove all list (ordered or unordered) entries from the main document now I have grabbed them
        webpageDoc.select("li").remove();
    }

    static void metaKeywordExtract(PipelineObject workingDoc, Document webpageDoc){
        /*
            Will attempt to extract meta tags on the webpage that are
            relevant. Meta tags of type "keyword" used to identify the page
            are extracted and put in array. The two example docs given dont use useful metatags,
            but other pages do.
        */
        try {
            String keywords = webpageDoc
                    .select("meta[name=keywords]")
                    .first()
                    .attr("content");

            String[] keywordsList = keywords.split(",");

            for (String keyword : keywordsList) {
                keyword = keyword
                        .toLowerCase()
                        .stripTrailing()
                        .stripLeading();

                workingDoc.webpageMetatagArray.add(keyword);
            }
        }catch (Exception e){
            System.out.println("Exception in meta tag extraction. Likely no meta tags of type \"keyword\" on webpage.");
        }
    }
}
