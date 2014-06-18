package com.comune.scafati;

import java.util.ArrayList;
import java.util.HashMap;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
		
		ListView listViewVIS=(ListView)findViewById(R.id.listVisInfoUtili);
		
		ArrayList<HashMap<String, String>> listavisinfoutili = new ArrayList<HashMap<String, String>>();
		listavisinfoutili=riempiLista(listavisinfoutili,numInfo,c);
		
	
		//Costruzione adapter
		String[] from = { "Titolo","Descrizione","Chiamata","ID", "Mappa"};
		
		int[] to={R.id.titolodescr,R.id.descrInfo,R.id.ButtonChiamata,R.id.ButtonPreferito,R.id.ButtonMappa};
		
		SimpleAdapter adapter=new SimpleAdapter(
				 this,
				 listavisinfoutili,
                 R.layout.riga_lista_visinfoutili,
                 from,
                 to
                );
		
		listViewVIS.setAdapter(adapter);
		System.out.print("\n"+numInfo+"\n");
		db.close();
	}
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Questo metodo crea la action bar.
		getMenuInflater().inflate(R.menu.activity__visualizzatore_documento,
				menu);
		return true;
	}	
	
	private ArrayList<HashMap<String, String>> riempiLista(ArrayList<HashMap<String, String>> listavisinfoutili, int numInfo, Cursor c) {
		for(int i=0;i<numInfo;i++)
		{
		 c.moveToPosition(i);
		 System.out.print("\n"+c.getString(c.getColumnIndex("InfoUtili.CodiceIU"))+"\n");
		 System.out.print("\n"+c.getString(c.getColumnIndex("Indirizzo"))+"\n");
		 listavisinfoutili.add(  creaMappa( c.getString(c.getColumnIndex("Nome")),
				 							c.getString(c.getColumnIndex("Descrizione")),
				 							c.getString(c.getColumnIndex("NumeroTelefono")),
				 							c.getString(c.getColumnIndex("InfoUtili.CodiceIU")),
				 							c.getString(c.getColumnIndex("Indirizzo")),
				 							i));
		 
		}
        return listavisinfoutili;
    }
	
	 private HashMap<String, String> creaMappa(String titolo, String descrizione,String Chiamata, String ID, String Mappa, int i) {
		 
		    HashMap<String, String> map = new HashMap<String, String>();
		    
		    map.put("Titolo", titolo);
	    	map.put("Descrizione", descrizione);
	    	map.put("Chiamata", Chiamata);
	    	map.put("ID", ID);
	    	map.put("Mappa", Mappa);
	 
	    	return map;
	    }   
	public void EventoChiamata(View v)
	{
		String number = "tel:3463215001";
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number)); 
        startActivity(callIntent);
	}
	
}
