package com.tatoeba.tatoeba;

import android.content.res.Configuration;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.view.View;


import java.util.List;
import java.util.Map;

import android.util.Log;

import com.tatoeba.tatoeba.Api;
import com.tatoeba.tatoeba.SentenceGroup;
import com.tatoeba.tatoeba.Sentence;


public class TatoebaActivity extends Activity {
    private Spinner languageSpinner;
    private ScrollView scrollView;
    private TextView mainSentence;
    private TextView translationsView;
    private Api api = new Api();


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        api = new Api();
		/** Create a new layout to display the view */
        scrollView = new ScrollView(this);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(1);
        
        /**Create the spinner for languages */

        languageSpinner = new Spinner(this);
        ArrayAdapter<String> adapter = new  ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            ((Api)api).getSupportedLanguagesNames()

        );
        languageSpinner.setAdapter(adapter);
        layout.addView(languageSpinner);

        /**Create the "get a random sentence in that language" */
        final Button getRandomButton = new Button(this);
         getRandomButton.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 getAndShowRandomSentence();
             }
         });
        getRandomButton.setText("show an other sentence");
        layout.addView(getRandomButton);



		/** Create a new textview array to display the main sentence */
		mainSentence = new TextView(this);
       
        mainSentence.setText((String)languageSpinner.getSelectedItem());
        layout.addView(mainSentence);

        
		/**
            Create a new textview array to display the translations of
            the random sentence
        */
        translationsView = new TextView(this);
        layout.addView(translationsView);

		/** Set the layout view to display */
        scrollView.addView(layout);
		setContentView(scrollView);

	}
    /**
     *
     */
    public void getAndShowRandomSentence() {
        String isoCode = api.getIsoCodeFromName(
            (String) languageSpinner.getSelectedItem()
        );
        SentenceGroup sentenceGroup = api.getRandomSentenceIn(isoCode);        
        mainSentence.setText(
            isoCode + " " + sentenceGroup.getMainSentenceText()
        );

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<Integer,List<Sentence>> entry : sentenceGroup.getTranslations().entrySet()) {
            sb.append("\n\ndepth :");
            sb.append(entry.getKey());
            sb.append("\n");

            for (Sentence translation : (List<Sentence>)entry.getValue()) {
                sb.append("\n\t");
                sb.append(translation.getLangIsoCode());
                sb.append("- ");
                sb.append(translation.getText());
            }

        }

        translationsView.setText(sb.toString());
    }


    /**
     *
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);
      setContentView(scrollView);
    }


        
}
