package com.tatoeba.tatoeba;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import com.tatoeba.tatoeba.Sentence;



/**
 *
 */
public class SentenceGroup {
    private Sentence mainSentence;
    private HashMap<Integer, List<Sentence>> translations = new HashMap<Integer, List<Sentence>>();


    /**
     * TODO hack
     */
    public SentenceGroup() {
        mainSentence = new Sentence("", "");
    }

    /**
     *
     */
    public SentenceGroup(String text, String langIsoCode) {
        mainSentence = new Sentence(text, langIsoCode);
    }

    /**
     *
     */
    public SentenceGroup(
        String mainSentenceText,
        String langIsoCode,
        int tatoId
    ) {
        mainSentence = new Sentence(
            mainSentenceText,
            langIsoCode,
            tatoId
        );
    }

    /**
     *
     */
    public SentenceGroup(Sentence mainSentence) {
        this.mainSentence = mainSentence;
    }


    /**
     *
     */
    public void addTranslation(String text, String langIsoCode, Integer depth) {
        if (translations.containsKey(depth)) {
            translations.get(depth).add(
                new Sentence(text, langIsoCode)
            );
        } else {
            ArrayList<Sentence> arrayList = new ArrayList<Sentence>();
            arrayList.add(
                new Sentence(text, langIsoCode)
            );

            translations.put(
                depth,
                arrayList
            );

        }
    }


    /**
     *
     */
    public void addTranslation(
        String text,
        String langIsoCode,
        int tatoId,
        Integer depth
    ) {
        if (translations.containsKey(depth)) {
            translations.get(depth).add(
                new Sentence(text, langIsoCode, tatoId)
            );
        } else {
            ArrayList<Sentence> arrayList = new ArrayList<Sentence>();
            arrayList.add(
                new Sentence(text, langIsoCode, tatoId)
            );

            translations.put(
                depth,
                arrayList
            );
        }
    }

    /**
     *
     */
    public String getMainSentenceText() {
        return mainSentence.getText();
    } 

    /**
     *
     */
    public String getMainSentenceLang() {
        return mainSentence.getLangIsoCode();
    } 

    /**
     *
     */
    public Integer getMainSentenceId() {
        return mainSentence.getTatoId();
    } 

    /**
     *
     */
    public HashMap<Integer, List<Sentence>> getTranslations() {
        return translations;
    }



}

