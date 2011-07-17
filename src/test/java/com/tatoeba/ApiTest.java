package com.tatoeba;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runner.RunWith;
import org.junit.Before;

import com.tatoeba.tatoeba.Api;
import com.tatoeba.tatoeba.Sentence;
import com.tatoeba.tatoeba.SentenceGroup;

import android.R;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.xtremelabs.robolectric.RobolectricTestRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;




@RunWith(RobolectricTestRunner.class)
public class ApiTest {
    @Test
    public void shouldHaveApiRootURL() throws Exception {
        String apiRootURL = new Api().getApiRootURL();
        assertThat(apiRootURL, equalTo("http://tato.sysko.fr/eng/api"));
    }

    @Test
    public void apiXMLRootShouldbeCorrect() throws Exception {
        Api api = new Api();
        Document apiCallDoc =api.getApiCallXMLDocument("/languages/get-langs");

        /**check encoding*/
        String encoding =  apiCallDoc.getInputEncoding();
        assertThat(encoding, equalTo("UTF-8"));

        //TODO rather validate using a XSD
        /**check root node*/
        
        String rootNodeName =  apiCallDoc.getFirstChild().getNodeName();
        assertThat(rootNodeName, equalTo("tatoeba"));
    }

    @Test
    public void shouldParseCorrectlyMainSentence() throws Exception {
        Api api = new Api();
        Document apiCallDoc = api.getApiCallXMLDocument(
            "/sentences/show/34534"
        );

        Node mainSentenceNode = apiCallDoc.getElementsByTagName("mainsentence").item(0);
        Sentence mainSentence = api.parseMainSentenceNode(mainSentenceNode);
        assertThat(mainSentence.getText(), equalTo("Bill was crazy for a motorbike."));
    } 


    @Test
    public void shouldParseCorrectlySentenceGroup() throws Exception {
        Api api = new Api();
        Document apiCallDoc = api.getApiCallXMLDocument(
            "/sentences/show/34534"
        );

        Node sentenceGroupNode = apiCallDoc.getElementsByTagName("sentencesset").item(0);
        // check the tag name
        assertThat(sentenceGroupNode.getNodeName(), equalTo("sentencesset"));

        // check that the sentencesset has a mainsentence tag
        Node mainSentenceNode = ((Element)sentenceGroupNode).getElementsByTagName("mainsentence").item(0);
        assertThat(mainSentenceNode.getNodeName(), equalTo("mainsentence"));

        // check if the mainsentence text is correctly present ...
        Node textOfMainSentenceNode = ((Element)mainSentenceNode).getElementsByTagName("text").item(0);
        assertThat(textOfMainSentenceNode.getNodeName(), equalTo("text"));
        // ... and contain the correct content
        Node textNode = textOfMainSentenceNode.getFirstChild();
        assertThat(textNode.getNodeValue(), equalTo("Bill was crazy for a motorbike."));

        // check that we can parse completly a sentencesset
        //(actually should work if the other test has succeeded)
        SentenceGroup sentenceGroup = api.parseSentenceGroup(sentenceGroupNode);
        assertThat(
            sentenceGroup.getMainSentenceText(),
            equalTo("Bill was crazy for a motorbike.")
        );

    } 


}
