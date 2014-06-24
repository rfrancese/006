package com.comune.scafati;

import java.util.ArrayList;
import java.util.HashMap;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
/* Questa classe viene visualizzata quando l'utente seleziona un elemento
 * della lista presente nell'Activity_InformazioniUtili, e serve per
 * visualizzare le informazioni di quel dato elemento. */
public class Activity_VisInfoUtili extends Activity {
	
	ListView listViewVIS;
    CustomAdapter adapter;
    public  Activity_VisInfoUtili CustomListView = null;
    public  ArrayList<ListVisInfoUtili> CustomListViewValuesArr = new ArrayList<ListVisInfoUtili>();
    
	// Metodo che viene chiamato alla creazione dell'activity.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity__visualizzatore_info);
		Intent intent=getIntent();
		
		String tipo = intent.getStringExtra("Tipo");
		
		setTitle(tipo);
		
		// Recupero l'imageView dal Layout e il nome dell'immagine che verra' poi settata.
		ImageView imgView=(ImageView)findViewById(R.id.imgInfo);
		String immagine = "icn_"+tipo.replace(" ", "").toLowerCase();
		imgView.setImageResource(getResources().getIdentifier(immagine, "drawable", getPackageName()));
		
		TextView titolo =(TextView)findViewById(R.id.titoloInfo);
		titolo.setText(tipo);
		
	    TextView preferitonull =(TextView)findViewById(R.id.titolopreferitonull);
	    
		listViewVIS=(ListView)findViewById(R.id.listVisInfoUtili);
		
		CustomListView = this;
        
		//funziona che avvalora la lista
        setListData(tipo, preferitonull);
         
        Resources res =getResources();
        // List defined in XML ( See Below )
         
    
        adapter=new CustomAdapter( CustomListView, CustomListViewValuesArr,res );
        listViewVIS.setAdapter( adapter );
     
	}
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Questo metodo crea la action bar.
		getMenuInflater().inflate(R.menu.activity__visualizzatore_documento,
				menu);
		return true;
	}	
	
	 /****** Avvaloro la lista *************/
    public void setListData(String tipo, TextView preferitonull)
    {
    	// Apro il database
		DBAdapter db = new DBAdapter(this);
		db.open();
		// Recupero dal database le informazioni utili del tipo richiesto.
		Cursor c = db.getIU(tipo);
		Cursor b = db.getPreferito();
		int numInfo = c.getCount(); 
		int numPref = b.getCount();
		
		 if(tipo.equalsIgnoreCase("Preferiti"))
			{
			 if(numPref==0)
				{
					listViewVIS.setVisibility(View.GONE);
					preferitonull.setVisibility(View.VISIBLE);
				}
			 else
				 for (int i = 0; i < numPref; i++) 
					{
						    b.moveToPosition(i);
									final ListVisInfoUtili sched = new ListVisInfoUtili();
									sched.setTitolo(b.getString(b.getColumnIndex("Nome")));
									sched.setDescrizione(b.getString(b.getColumnIndex("Descrizione")));
									sched.setNumCell(b.getString(b.getColumnIndex("NumeroTelefono"))); 
									sched.setID(b.getString(b.getColumnIndex("CodiceIU")));
									sched.setPreferito(b.getInt(b.getColumnIndex("Preferito")));
									sched.setIndirizzo(b.getString(b.getColumnIndex("Indirizzo")));

							CustomListViewValuesArr.add( sched );
						}
			}
		 else
		 {
           for (int i = 0; i < numInfo; i++) 
        	{
        		c.moveToPosition(i);
                final ListVisInfoUtili sched = new ListVisInfoUtili();
                     
                   sched.setTitolo(c.getString(c.getColumnIndex("Nome")));
                   sched.setDescrizione(c.getString(c.getColumnIndex("Descrizione")));
                   sched.setNumCell(c.getString(c.getColumnIndex("NumeroTelefono"))); 
                   sched.setID(c.getString(c.getColumnIndex("CodiceIU")));
                   sched.setPreferito(c.getInt(c.getColumnIndex("Preferito")));
                   sched.setIndirizzo(c.getString(c.getColumnIndex("Indirizzo")));

                CustomListViewValuesArr.add( sched );	
        	}
		 } 
      db.close();   
    }
    
    /*****************  Queste funzioni sono usate dal CustomAdapter ****************/
    
    public void onChiamataClick(int mPosition)
    {
    	ListVisInfoUtili tempValues = ( ListVisInfoUtili ) CustomListViewValuesArr.get(mPosition);
    	
    	
    	String number = "tel:" + tempValues.getNumCell();
    	
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number)); 
        startActivity(callIntent);
    	
        
    }
	
    public void onPreferitoClick(int mPosition)
    {
    	ListVisInfoUtili tempValues = ( ListVisInfoUtili ) CustomListViewValuesArr.get(mPosition);
    	
    	        Intent intent=getIntent();
    	        String tipo = intent.getStringExtra("Tipo");
    			DBAdapter db = new DBAdapter(this);
    			db.open();
  
    			ImageButton buttonpreferito=(ImageButton)findViewById(R.id.ButtonPreferito);

    	if(tipo.equalsIgnoreCase("Preferiti"))
    	{
        		db.alterPreferito(0, tempValues.getID());
        		tempValues.setPreferito(0);	
        		//buttonpreferito.setImageResource(getResources().getIdentifier("icn_preferiti_off", "drawable", getPackageName()));
        		Toast.makeText(CustomListView,"Rimosso dai preferiti ",Toast.LENGTH_LONG).show();	
    	}		
    	else
    	{	
    	if(tempValues.getPreferito()==0)	
    	{
    		db.alterPreferito(1, tempValues.getID());
    		tempValues.setPreferito(1);	
    		//buttonpreferito.setImageResource(getResources().getIdentifier("icn_preferiti", "drawable", getPackageName()));
    		Toast.makeText(CustomListView,"Aggiunto ai preferiti ",Toast.LENGTH_LONG).show();
    	}
    	else
  
    	{   
    		db.alterPreferito(0, tempValues.getID());
    		tempValues.setPreferito(0);
    		buttonpreferito.setImageResource(getResources().getIdentifier("icn_preferiti_off", "drawable", getPackageName()));
    		Toast.makeText(CustomListView,"Rimosso dai preferiti ",Toast.LENGTH_LONG).show();
    	}
    	}
      		
		 db.close();
    }
    
    public void onMappaClick(int mPosition)
    {
    	ListVisInfoUtili tempValues = ( ListVisInfoUtili ) CustomListViewValuesArr.get(mPosition);
    	
    	String map = "http://maps.google.co.in/maps?q=" + tempValues.getIndirizzo(); 
    	Intent intent = new Intent(android.content.Intent.ACTION_VIEW,Uri.parse(map));
    	startActivity(intent);
	
    }
    
}
