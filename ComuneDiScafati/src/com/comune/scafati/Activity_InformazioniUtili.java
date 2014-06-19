package com.comune.scafati;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
 
/* Questa classe viene visualizzata quando l'utente seleziona
 * il pulsante "Informazioni Utili" nel MainActivity. */
public class Activity_InformazioniUtili extends ListActivity {
	
	String[] infoutili;
	ListView list;
	  Integer[] imageId = {
	      R.drawable.icn_preferiti,
	      R.drawable.icn_numeriutili,
	      R.drawable.icn_istitutiscolastici,
	      R.drawable.icn_farmaciediturno,
	      R.drawable.icn_benzinai,
	      R.drawable.icn_banche,
	      R.drawable.icn_pizzerieeristoranti,
	      R.drawable.icn_luoghidiculto
	  };
	String immagine;
    
	// Metodo che viene chiamato alla creazione dell'activity.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        infoutili = getResources().getStringArray(R.array.infoutili);
        CustomList adapter = new
        CustomList(Activity_InformazioniUtili.this, infoutili, imageId);
        list = getListView();
        list.setAdapter(adapter);
 
        list.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	/* Quando l'utente seleziona un elemento viene aperto Activity_VisInfoUtili
            	 * che mostra le informazioni richieste. */
            	String tipo = infoutili[position];
				Intent infoIntent = new Intent(getApplicationContext(), Activity_VisInfoUtili.class);
				infoIntent.putExtra("Tipo", tipo);
				startActivity(infoIntent);
                }
          });
 
    }
}
