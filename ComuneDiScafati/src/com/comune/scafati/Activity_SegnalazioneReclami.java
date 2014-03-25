package com.comune.scafati;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/* Questa classe viene visualizzata quando l'utente seleziona
 * il pulsante "Segnalazioni e Reclami" nel MainActivity. */
public class Activity_SegnalazioneReclami extends Activity {

	// Metodo che viene chiamato alla creazione dell'activity.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_segnrec);
		// Recupero il bottone dal layout e setto il listener da invocare quando viene cliccato.
		Button ButtonInvia = (Button) findViewById(R.id.button2);
		ButtonInvia.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
         	 EditText oggetto = (EditText) findViewById(R.id.oggetto);
         	 EditText descrizione = (EditText) findViewById(R.id.descrizione);
         	 // Controllo che tutti i campi siano stati compilati.
         	 if(oggetto.getText().length() != 0 && descrizione.getText().length() != 0)
         	 {
         		finish();
         		Context context = getApplicationContext();
      	    	CharSequence text = "Messaggio Inviato";
      	    	int duration = Toast.LENGTH_SHORT;
      	    	Toast toast = Toast.makeText(context, text, duration);
      	    	toast.show();
         	 }
         	 else
         	 {
         		   	Context context = getApplicationContext();
         	    	CharSequence text = "Completa tutti i campi.";
         	    	int duration = Toast.LENGTH_SHORT;

         	    	Toast toast = Toast.makeText(context, text, duration);
         	    	toast.show();
         	 }
         		 
         	              }
      });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Questo metodo crea la action bar.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	// Metodo settato come listener del pulsante "Reset" che si occupa di resettare i campi di testo.
	public void Reset(View view) {  
		EditText oggetto = (EditText) findViewById(R.id.oggetto);
    	EditText descrizione = (EditText) findViewById(R.id.descrizione);
    	
    	oggetto.setText(null);
    	descrizione.setText(null);
	      }

}