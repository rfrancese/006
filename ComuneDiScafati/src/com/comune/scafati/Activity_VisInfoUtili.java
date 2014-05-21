package com.comune.scafati;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/* Questa classe viene visualizzata quando l'utente seleziona un elemento
 * della lista presente nell'Activity_InformazioniUtili, e serve per
 * visualizzare le informazioni di quel dato elemento. */
public class Activity_VisInfoUtili extends Activity {
	
	// Metodo che viene chiamato alla creazione dell'activity.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity__visualizzatore_info);
		Intent intent=getIntent();
		
		ImageButton ButtonChiamata;
		ImageButton ButtonMappa;
		ImageButton ButtonPreferiti;
		
		String tipo = intent.getStringExtra("Tipo");
		
		setTitle(tipo);
		
		// Recupero l'imageView dal Layout e il nome dell'immagine che verrˆ poi settata.
		ImageView imgView=(ImageView)findViewById(R.id.imgInfo);
		String immagine = "icn_"+tipo.replace(" ", "").toLowerCase();

		imgView.setImageResource(getResources().getIdentifier(immagine, "drawable", getPackageName()));
		
		TextView titolo =(TextView)findViewById(R.id.titoloInfo);
		titolo.setText(tipo);
		
		// Apro il database
		DBAdapter db = new DBAdapter(this);
		db.open();
		// Recupero dal database le informazioni utili del tipo richiesto.
		Cursor c = db.getIU(tipo);
		int numInfo = c.getCount();
		
		// Formatto e visualizzo i dati prelevati dal database.
		TextView testo =(TextView)findViewById(R.id.descrInfo)
				;
		ButtonMappa = (ImageButton)findViewById(R.id.ButtonMappa);
		
		for(int i=0;i<numInfo;i++)
		{
			c.moveToPosition(i);
			testo.append("\n"+c.getString(0)+"\n"+c.getString(1));
				 
			testo.append("\n\n-------------------\n");
		}
		db.close();
	}
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Questo metodo crea la action bar.
		getMenuInflater().inflate(R.menu.activity__visualizzatore_documento,
				menu);
		return true;
	}	
	
}
