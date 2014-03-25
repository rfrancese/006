package com.comune.scafati;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
 
/* Questa classe viene visualizzata quando l'utente seleziona
 * il pulsante "Amministrazione" nel MainActivity, e si occupa
 * di mostrare la lista dei componenti dell'Amministrazione Comunale. */
public class Activity_Amministrazione extends ListActivity {
	
	DBAdapter db;
	Cursor c;
	int n;
	String[] amministrazione;
	String tipo;
	Intent dettagliIntent;
    
	// Metodo che viene chiamato alla creazione dell'activity.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        ListView listaV = getListView();
    	// Istanzio il database.
        db = new DBAdapter(getApplicationContext());
    	c = null;
        // Controllo se mostrare la lista principale oppure una sua sottolista
    	if(intent.getExtras()==null)
        {
    		// Leggo l'array che contiene gli elementi da visualizzare nella lista.
        	amministrazione = getResources().getStringArray(R.array.amministrazione);
        	setListAdapter(new ArrayAdapter<String>(this, R.layout.riga_lista_amministrazione, amministrazione));
            /* Imposto il listener della lista per definire il suo comportamento quando
        	 * un elemento viene premuto. */
        	listaV.setOnItemClickListener(new OnItemClickListener() {
            	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                	/* Controllo se viene premuto un elemento che richiama una sottolista
                	 * oppure uno che richiama l'Activity_VisualizzatoreDocumento.
                	 */
            		if(position==1 || position==5)
                    {
                    	dettagliIntent = new Intent(getApplicationContext(), Activity_Amministrazione.class);
    					dettagliIntent.putExtra("Position", position);
    		        	startActivity(dettagliIntent);
                    }
                	else
                	{
                		dettagliIntent = new Intent(getApplicationContext(), Activity_VisualizzatoreDocumento.class);
                		db.open();
                		tipo=amministrazione[position];
                		// Eseguo la query sul database per recuperare i dati relativi all'elemento cliccato.
                		c = db.getSubItemAmm(tipo);
                		c.moveToFirst();
                		prepareAndStartVisDoc();
                	}
                }
              });
        }
        else
        {
        	if(intent.getIntExtra("Position", 0)==1) tipo="La Giunta";
        	else tipo="Organizzazione";
        	setTitle(tipo);
        	// Apro il database ed eseguo la query per determinare gli elementi della sottolista, poi li conto.
			db.open();
			c = db.getSubItemAmm(tipo);
			n = c.getCount();
			// Riempio un array che contiene i dati prelevati dal database.
	        amministrazione = new String[n];
	        for(int i=0;i<n;i++){
	                c.moveToPosition(i);
	                amministrazione[i] = c.getString(1);
	        }
	        // Imposto l'adapter della lista passandogli l'array riempito prima e setto il suo listener.
        	setListAdapter(new ArrayAdapter<String>(this, R.layout.riga_lista_amministrazione, amministrazione));
            listaV.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                	dettagliIntent = new Intent(getApplicationContext(), Activity_VisualizzatoreDocumento.class);
                	c.moveToPosition(position);
                	tipo=amministrazione[position];
                	prepareAndStartVisDoc();
                }
              });
        }

 
    }
    
    /* Questo metodo si occupa del passaggio di parametri verso
     * l'Activity_VisualizzatoreDocumenti e della sua successiva apertura. */
    private void prepareAndStartVisDoc()
    {
    	dettagliIntent.putExtra("Titolo", c.getString(1));
		dettagliIntent.putExtra("Descrizione", c.getString(2));
		dettagliIntent.putExtra("Immagine", c.getString(3));
		dettagliIntent.putExtra("Tipo", tipo);
		/* Eseguo la query per prelevare dal database gli allegati dell'elemento selezionato,
		 * che poi vengono passati all'Activity_Visualizzatore Documento. */
		Cursor a = db.getAllAttAmm(c.getString(0));
		int numAll = a.getCount();
		dettagliIntent.putExtra("NumAllegati", numAll);
		for(int i=0;i<numAll;i++)
		{
			a.moveToPosition(i);
			dettagliIntent.putExtra("TitoloAll"+i, a.getString(0));
			dettagliIntent.putExtra("IndirizzoAll"+i, a.getString(1));
		}
		startActivity(dettagliIntent);
    }
}