package com.comune.scafati;

import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.app.AlertDialog;
import android.widget.TextView;

/* Questa classe viene visualizzata quando l'utente seleziona
 * il pulsante "Segnalazioni e Reclami" nel MainActivity. */
public class Activity_SegnalazioneReclami extends Activity {

	private static final int SELECT_PICTURE = 1;
	TextView posText;
	Button posButton;
	boolean undo;
	GPSLocationListener gpsListener;
	
	// Metodo che viene chiamato alla creazione dell'activity.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_segnrec);

		// Recupero il bottone dal layout e setto il listener da invocare quando viene cliccato.
		Button ButtonInvia = (Button) findViewById(R.id.buttonInvia);
		ButtonInvia.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
         	 EditText oggetto = (EditText) findViewById(R.id.oggetto);
         	 EditText descrizione = (EditText) findViewById(R.id.descrizione);
         	 // Controllo che tutti i campi siano stati compilati.
         	 if(oggetto.getText().length() != 0 && descrizione.getText().length() != 0)
         	 {
         		finish();
         		Context context = getApplicationContext();
      	    	CharSequence text = "Segnalazione Inviata";
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
	
	public void AllFoto(View view) {  
		Intent pickIntent = new Intent();
		pickIntent.setType("image/*");
		pickIntent.setAction(Intent.ACTION_GET_CONTENT);

		Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		String pickTitle = "Seleziona o scatta una foto"; // Or get from strings.xml
		Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
		chooserIntent.putExtra
		(
		  Intent.EXTRA_INITIAL_INTENTS, 
		  new Intent[] { takePhotoIntent }
		);

		startActivityForResult(chooserIntent, SELECT_PICTURE);
     }
	
	private class GPSLocationListener implements LocationListener {
		@Override
		public void onLocationChanged(Location location) {

        	posText.setText("Latitudine: "+String.valueOf(location.getLatitude())+"nLongitudine: "+String.valueOf(location.getLongitude()));
        	posButton.setText("Rimuovi Posizione");
		}

		@Override
		public void onProviderDisabled(String provider) {}

		@Override
		public void onProviderEnabled(String provider) {}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {}

	}
	
	public void AllPos(View view) {  
		
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		posText = (TextView) findViewById(R.id.textPos);
    	posText.setVisibility(View.VISIBLE);

    	posButton = (Button) findViewById(R.id.buttonPos);
    	
		boolean gps = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if(!undo)
		{
		if (!gps) {

			
			posText.setTextColor(Color.RED);
    		posText.setText("Errore: GPS Disattivato.");

        } else {
 
        	undo = true;
        	
        	posButton.setText("Annulla");
			posText.setTextColor(Color.parseColor("#a7d5e1"));
			posText.setText("Caricamento Coordinate GPS in corso...");

        	gpsListener = new GPSLocationListener();

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    60000, 
                    100, 
                    gpsListener);
        }
		}
		else
		{
			lm.removeUpdates(gpsListener);
			posText.setTextColor(Color.parseColor("#a7d5e1"));
			posText.setText("Caricamento Coordinate GPS in corso...");
			posText.setVisibility(View.GONE);
			posButton.setText("Rileva Posizione");
			undo = false;
		}
	 }
}