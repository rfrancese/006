package com.comune.scafati;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

/* Questa classe viene caricata all'avvio e si occupa di mostrare all'utente
 *  la schermata principale dell'applicazione. */
public class MainActivity extends Activity {
	
	ImageView button;
	Bundle savedInstanceStateLocal;
	// Metodo che viene chiamato alla creazione dell'activity.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		savedInstanceStateLocal = savedInstanceState;
		setContentView(R.layout.activity_main);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Questo metodo crea la action bar.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.about:
	            openAbout();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void openAbout()
	{
		Intent aboutIntent = new Intent(MainActivity.this, Activity_About.class);
    	MainActivity.this.startActivity(aboutIntent); 
		return;
	}
	
	@Override
	protected void onResume() {
		onCreate(savedInstanceStateLocal);
	}
	
	/* I seguenti metodi definiscono cosa fare alla pressione dei vari
	 * pulsanti presenti nell'activity. */
    public void amministrazioneClick(View view) {  
    	button = (ImageView) findViewById(R.id.buttonAmministrazione);
    	button.setColorFilter(0xFF555555, PorterDuff.Mode.MULTIPLY);
    	Intent amministrazioneIntent = new Intent(MainActivity.this, Activity_Amministrazione.class);
    	MainActivity.this.startActivity(amministrazioneIntent); 
    	 }
    public void cenniStoriciClick(View view) {  
    	button = (ImageView) findViewById(R.id.buttonCenniStorici);
    	button.setColorFilter(0xFF555555, PorterDuff.Mode.MULTIPLY);
    	Intent cenniStoriciIntent = new Intent(MainActivity.this, Activity_CenniStorici.class);
    	MainActivity.this.startActivity(cenniStoriciIntent); 
    	 }
    public void infoClick(View view) {  
    	button = (ImageView) findViewById(R.id.buttonInfo);
    	button.setColorFilter(0xFF555555, PorterDuff.Mode.MULTIPLY);
    	Intent infoIntent = new Intent(MainActivity.this, Activity_InformazioniUtili.class);
    	MainActivity.this.startActivity(infoIntent); 
    	 }
    public void newsedEventiClick(View view) {  
    	button = (ImageView) findViewById(R.id.buttonNewsedEventi);
    	button.setColorFilter(0xFF555555, PorterDuff.Mode.MULTIPLY);
    	Intent newsIntent = new Intent(MainActivity.this, Activity_NewsEdEventi.class);
    	MainActivity.this.startActivity(newsIntent); 
    	 }
    public void segnalazioniClick(View view) {  
    	button = (ImageView) findViewById(R.id.buttonSegnalazioni);
    	button.setColorFilter(0xFF555555, PorterDuff.Mode.MULTIPLY);
    	Intent segnIntent = new Intent(MainActivity.this, Activity_SegnalazioneReclami.class);
    	MainActivity.this.startActivity(segnIntent); 
    	 }
}
