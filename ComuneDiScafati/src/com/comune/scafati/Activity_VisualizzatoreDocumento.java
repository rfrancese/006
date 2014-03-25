package com.comune.scafati;

import java.io.InputStream;
import java.net.URL;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
/* Questa classe viene visualizzata quando l'utente seleziona una news o un evento
 * presente nell'Activity_NewsEdEventi, oppure un elemento dell'Amministrazione
 * Comunale nell'Activity_Amministrazione, e serve per visualizzare le informazioni
 * di quel dato elemento. */
public class Activity_VisualizzatoreDocumento extends Activity implements OnItemSelectedListener {

	private String[] indAll;
	private Spinner spinnerAll;
	
	// Metodo che viene chiamato alla creazione dell'activity.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity__visualizzatore_documento);
		Intent intent=getIntent();
		// Recupero le informazioni da visualizzare che sono state passate dall'activity chiamante.
		String tipo = intent.getStringExtra("Tipo");
		setTitle(tipo);
		
		String titolo = intent.getStringExtra("Titolo");
		TextView titoloView=(TextView)findViewById(R.id.titoloDoc);
		titoloView.append(titolo);
		
		String descrizione = intent.getStringExtra("Descrizione");
		TextView descView=(TextView)findViewById(R.id.descrDoc);
		descView.append(descrizione);
		// Avvio i metodi per scaricare e settare l'immagine dell'imageView.
		String immagine = intent.getStringExtra("Immagine");
		if(immagine != null){
		CaricaImmagine caricaImmagineAsync = new CaricaImmagine();
		caricaImmagineAsync.execute(new String[]{immagine});
		}
		
		// Recupero gli allegati (se presenti) e popolo lo Spinner.
		Integer numAll = intent.getIntExtra("NumAllegati",0);
		spinnerAll = (Spinner)findViewById(R.id.allSpin);
		spinnerAll.setOnItemSelectedListener(this);
		
		int numAllindex = numAll+1;
		String[] titoliAll = new String[numAllindex];
		indAll = new String[numAllindex];
		if(numAll==0) titoliAll[0]="Nessun Allegato.";
		else if(numAll==1) titoliAll[0]="E' disponibile "+numAll+" allegato, clicca per visualizzarlo.";
			else titoliAll[0]="Sono disponibili "+numAll+" allegati, clicca per visualizzarli.";
		indAll[0]="";
		int j=0;
		for(int i=1; i<=numAll; i++)
		{
			titoliAll[i] = intent.getStringExtra("TitoloAll"+j);
			indAll[i] = intent.getStringExtra("IndirizzoAll"+j);
			j++;
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this,
				R.layout.riga_spinner_allegati,
				R.id.titoloAll,
				titoliAll
				);
		spinnerAll.setAdapter(adapter);
	}
		
	
	// Metodo che rappresenta il listener dello Spinner e avvia il download di un allegato.
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) { 
		if(pos != 0) {
	    	startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(indAll[pos])));
	    }
	}

    public void onNothingSelected(AdapterView<?> parent) {
        /* Metodo da implementare obbligatoriamente che definisce cosa accade
         * quando non viene selezionato nulla nello Spinner degli allegati. */
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Questo metodo crea la action bar.
		getMenuInflater().inflate(R.menu.activity__visualizzatore_documento,
				menu);
		return true;
	}
	
    // Questo metodo scarica l'immagine in drawable.
    private Drawable creaImmagineDaUrl(String urlImmagine)
    {
         try
         {
             InputStream is = (InputStream) new URL(urlImmagine).getContent();
             Drawable d = Drawable.createFromStream(is, null);
             return d;
         }catch (Exception e) {
             System.out.println("Exc="+e);
             return null;
         }
     }
    
    // Sottoclasse che si occupa di scaricare in maniera asincrona le varie immagini. 
	private class CaricaImmagine extends AsyncTask<String, Void, Drawable>{

		@Override
		protected Drawable doInBackground(String... params) {
			
			Drawable d = creaImmagineDaUrl(params[0]); 
			
			return d;
		}
		
		@Override
		protected void onPostExecute(Drawable result) {
			super.onPostExecute(result);
			/* Quando il Task asincorno termina viene recuperata l'imageView dal layout
			 * e viene settata l'immagine scaricata. */
			ImageView imageView = (ImageView)findViewById(R.id.imgDoc);
			imageView.setImageDrawable(result);
		}
    	
    }
	
}
