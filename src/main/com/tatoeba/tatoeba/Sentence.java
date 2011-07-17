package com.tatoeba.tatoeba;

import java.lang.Object;


/**
 *
 */
public class Sentence extends Object {
    private String text;
    private String langIsoCode;
    private Integer tatoId = new Integer(0);

    /**
     *
     */
    public Sentence(String text, String langIsoCode) {
        this.text = text;
        this.langIsoCode = langIsoCode;
    }

    /**
     *
     */
    public Sentence(String text, String langIsoCode, Integer tatoId ) {
        this.text = text;
        this.langIsoCode = langIsoCode;
        this.tatoId = tatoId;
    }

    /**
     *
     */
    public String getText() {
        return text;
    }

    /**
     *
     */
    public String getLangIsoCode() {
        return langIsoCode;
    }

    /**
     *
     */
    public Integer getTatoId() {
        return tatoId;
    }




}

