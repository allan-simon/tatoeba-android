package com.tatoeba.tatoeba;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.util.HashMap;
import java.util.TreeSet;

import com.tatoeba.tatoeba.SentenceGroup;

public class Api {

    private HashMap<String,String> supportedLangs;

    private String apiRootURL = "http://tato.sysko.fr/eng/api";

    public Api() {
        supportedLangs = new HashMap<String,String>();
        

    }

    /**
     *
     */
    public String getApiRootURL() {
        return apiRootURL;
    }

    /**
     *
     */
    public String getIsoCodeFromName(String langName) {
        return (String) supportedLangs.get(langName);
    }
    
    /**
     *
     */
    public String[] getSupportedLanguagesNames() {
        if (supportedLangs.isEmpty()) {
            supportedLangs = getSupportedLanguages();
        }
        return new TreeSet<String>(supportedLangs.keySet()).toArray(new String[0]);
    }

    /**
     *
     */
    public Document getApiCallXMLDocument(String apiCallURL) throws Exception {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();
		try {
            URL url = new URL(
               apiRootURL + apiCallURL
            );

            doc = db.parse(
                new InputSource(url.openStream())
            );
            doc.getDocumentElement().normalize();
		} catch (Exception e) {
            //helloWorld.setText("XML Parsing Exception = " + e);
            //layout.addView(helloWorld);
			System.out.println();
		}

        return doc;

    }
 
    /**
     *
     */
    public HashMap<String,String> getSupportedLanguages() {

        HashMap<String,String> isoToName = new HashMap<String,String>();

		try {
        Document apiCallDoc = getApiCallXMLDocument("/languages/get-langs");
        NodeList languagesList = apiCallDoc.getElementsByTagName("language");

        for (int i = 0; i < languagesList.getLength(); i++) {

            Node node = languagesList.item(i);

            isoToName.put(
                node.getFirstChild().getNodeValue(),
                ((Element)node).getAttribute("isocode")
            );

        }
        } catch (Exception e) {
            //helloWorld.setText("XML Parsing Exception = " + e);
            //layout.addView(helloWorld);
			System.out.println();
		}


        return isoToName;
    }

   
    /**
     *
     */
    public SentenceGroup getRandomSentenceIn(String langIsoCode) {

        
        SentenceGroup sentenceGroup = new SentenceGroup();
		try {
            Document apiCallDoc = getApiCallXMLDocument(
                "/sentences/show-random-in/" + langIsoCode
            );
            sentenceGroup = parseSentenceGroup(
                apiCallDoc.getElementsByTagName("sentencesset").item(0)
            );
        } catch (Exception e) {
            //helloWorld.setText("XML Parsing Exception = " + e);
            //layout.addView(helloWorld);
			System.out.println();
		}

        return sentenceGroup;
    }

    /**
     *
     */
    public SentenceGroup parseSentenceGroup(Node sentenceGroupNode) {
        Node mainSentenceNode = ((Element)sentenceGroupNode).getElementsByTagName("mainsentence").item(0);
        /** Creating group and adding main sentence */
        SentenceGroup sentenceGroup = new SentenceGroup(
            parseMainSentenceNode(mainSentenceNode)
        );
        /** Looping on each depth of translations*/
        NodeList depthsList = ((Element)sentenceGroupNode).getElementsByTagName("depth");

        for (int i = 0; i <  depthsList.getLength(); i++) {

            Node currentDepthNode = depthsList.item(i);
            int currentDepth = Integer.parseInt(
                ((Element)currentDepthNode).getAttribute("value")
            );

            /**
                Add all the translations of a given depth (all direct translations,
                all translations of translations etc.)
            */
            NodeList sentencesNodeList = ((Element)currentDepthNode).getElementsByTagName("sentence");
            for (int j = 0; j <  sentencesNodeList.getLength(); j++) {
                
                Node sentenceNode = sentencesNodeList.item(j);
                sentenceGroup.addTranslation(

                    ((Element)sentenceNode).getElementsByTagName("text").item(0).getFirstChild().getNodeValue(),
                    ((Element)sentenceNode).getAttribute("lang"),
                    Integer.parseInt(
                        ((Element)sentenceNode).getAttribute("tatoid")
                    ),
                    currentDepth
                );
            }
        }
        return sentenceGroup;
    }

    /**
     *
     */
    public Sentence parseMainSentenceNode(Node mainSentenceNode) {
        // for the moment main sentence has only one child 
        // which is the <text> tag
        return new Sentence(
            ((Element)mainSentenceNode).getElementsByTagName("text").item(0).getFirstChild().getNodeValue(),
            ((Element)mainSentenceNode).getAttribute("lang"),
            Integer.parseInt(
                ((Element)mainSentenceNode).getAttribute("tatoid")
            )
        );
    }


}
