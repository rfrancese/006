package com.comune.scafati;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

/* Questa classe viene caricata all'avvio e si occupa di mostrare all'utente
 *  la schermata principale dell'applicazione. */
public class MainActivity extends Activity {
	
	// Metodo che viene chiamato alla creazione dell'activity.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Questo metodo crea la action bar.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/* I seguenti metodi definiscono cosa fare alla pressione dei vari
	 * pulsanti presenti nell'activity. */
    public void amministrazioneClick(View view) {  
    	Intent amministrazioneIntent = new Intent(MainActivity.this, Activity_Amministrazione.class);
    	MainActivity.this.startActivity(amministrazioneIntent); 
    	 }
    public void cenniStoriciClick(View view) {  
    	Intent cenniStoriciIntent = new Intent(MainActivity.this, Activity_CenniStorici.class);
    	MainActivity.this.startActivity(cenniStoriciIntent); 
    	 }
    public void infoClick(View view) {  
    	Intent infoIntent = new Intent(MainActivity.this, Activity_InformazioniUtili.class);
    	MainActivity.this.startActivity(infoIntent); 
    	 }
    public void newsedEventiClick(View view) {  
    	Intent newsIntent = new Intent(MainActivity.this, Activity_NewsEdEventi.class);
    	MainActivity.this.startActivity(newsIntent); 
    	 }
    public void segnalazioniClick(View view) {  
    	Intent segnIntent = new Intent(MainActivity.this, Activity_SegnalazioneReclami.class);
    	MainActivity.this.startActivity(segnIntent); 
    	 }
}
