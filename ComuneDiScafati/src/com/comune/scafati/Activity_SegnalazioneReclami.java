package com.comune.scafati;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.app.AlertDialog;
import android.widget.TextView;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.comune.scafati.Activity_NewsEdEventi.DummySectionFragment2.task;
import com.comune.scafati.Activity_NewsEdEventi.DummySectionFragment2.taskAll;

/* Questa classe viene visualizzata quando l'utente seleziona
 * il pulsante "Segnalazioni e Reclami" nel MainActivity. */
public class Activity_SegnalazioneReclami extends Activity {


	private static final int REQUEST_CODE = 0;
	TextView posText;
	Button posButton;
	boolean undo = false;
	GPSLocationListener gpsListener;
	boolean OpResult;
	String ogg,descr,loc,imgurl;
	private static Bitmap scaledphoto=null;
    private String filePath=null;
    private Uri u=null;
    private Boolean picTaken=false;
    LocationManager lm;
    ImageView photo_captured=null;
    boolean status = false;
    ProgressDialog pd;
    boolean localization = false;
    
	// Metodo che viene chiamato alla creazione dell'activity.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_segnrec);
		
    	posButton = (Button) findViewById(R.id.buttonPos);
    	photo_captured = (ImageView)findViewById(R.id.imgSegn);
		// Recupero il bottone dal layout e setto il listener da invocare quando viene cliccato.
		Button ButtonInvia = (Button) findViewById(R.id.buttonInvia);
		ButtonInvia.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
         	 EditText oggetto = (EditText) findViewById(R.id.oggetto);
         	 EditText descrizione = (EditText) findViewById(R.id.descrizione);
         	 // Controllo che tutti i campi siano stati compilati.
         	 if(oggetto.getText().length() != 0 && descrizione.getText().length() != 0)
         	 {
             	if(undo)
            	{
            		lm.removeUpdates(gpsListener);
            		undo = false;
            		localization=false;
            	}
         		 ogg = oggetto.getText().toString();
         		 descr = descrizione.getText().toString();
         		if(picTaken)
         		{
         			upLoadPicture();
         		}
         		new task().execute();
         		
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
    	if(picTaken)
    	{
    		photo_captured.setVisibility(View.GONE);
    		picTaken=false;
    	}
    	if(!picTaken)
    	{
    		status=true;
    	}
    	if(undo || posButton.getText()=="Rimuovi Posizione")
    	{
    		lm.removeUpdates(gpsListener);
    		posText.setTextColor(Color.parseColor("#a7d5e1"));
    		posText.setText("Caricamento Coordinate GPS in corso...");
    		posText.setVisibility(View.GONE);
    		posButton.setText("Rileva Posizione");
    		undo = false;
    		localization=false;
    	}
	      }
	
	public void AllFoto(View view) {  
		String pictureName=String.format("%d",new Date().getTime());
		imgurl="http://semplicementech.t15.org/comunescafati/uploads/"+pictureName+".jpg";
		Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory()+"/Pictures",  pictureName+".jpg");//save picture (.jpg) on SD Card
        u=Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,u);      
        filePath = photo.getAbsolutePath();        
        startActivityForResult(intent, REQUEST_CODE);
     }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		 
		 try {

		             getContentResolver().notifyChange(u, null);  
		             ContentResolver cr = getContentResolver();     
		             Bitmap bm = android.provider.MediaStore.Images.Media.getBitmap(cr, u);
		//ImageView to set the picture taken from camera.
		             photo_captured.setImageBitmap(bm); 
		             photo_captured.setVisibility(View.VISIBLE);
		             picTaken=true; //to ensure picture is taken 

		      }catch(Exception e) {
		           
		       e.printStackTrace();   
		     
		     }                            
		}
	
	//Method for uploading picture on FtpServer
	public void upLoadPicture() {
	        
	            pd = ProgressDialog.show(Activity_SegnalazioneReclami.this, "Attendere prego...", "Caricamento immagine...", true);
	            new Thread(){    
	                       
	                  @Override        
	                  public void run() {              
	                 
	                        File file = new File(filePath);  
	                              try {
	                                   
	                                    FTPClient client = new FTPClient();
	                                    client.connect("ftp.semplicementech.t15.org",21);

	                                    client.login("u735935028", "comunescafati"); //this is the login credentials of your ftpserver. Ensure to use valid username and password otherwise it throws exception
	                                    client.changeWorkingDirectory("/public_html/comunescafati/uploads");
	                                    Log.e("serverReply: ",client.getReplyString());
	                                    if (client.getReplyString().contains("250")) {
	                                        client.setFileType(org.apache.commons.net.ftp.FTP.IMAGE_FILE_TYPE);
	                                        BufferedInputStream buffIn = null;
	                                        buffIn = new BufferedInputStream(new FileInputStream(file));
	                                        client.enterLocalPassiveMode();

	                                        status = client.storeFile(file.getName(),buffIn);
		                                    Log.e("Status", String.valueOf(status));  

	                                        buffIn.close();
	                                        client.logout();
	                                        client.disconnect();
	                                    }  
	                                    file.setWritable(true);
	                                    file.delete();
	                              }                                              
	                              catch (Exception e) {
	                              }     
	  	                        pd.dismiss(); 
	  	             			runOnUiThread(new Runnable() 
	  	             	        {                
	  	             	            @Override
	  	             	            public void run() 
	  	             	            {
	                                    Log.e("OpResult", String.valueOf(OpResult));  

	  	             	            	if(OpResult && status)
	  	             	            	{
	  	             	            		finish();
	  	             	            		Context context = getApplicationContext();
	  	             	            		CharSequence text = "Segnalazione Inviata";
	  	             	            		int duration = Toast.LENGTH_SHORT;
	  	             	            		Toast toast = Toast.makeText(context, text, duration);
	  	             	            		toast.show();
	  	             	            	}
	  	             	            	else if(!OpResult || !status)
	  	             	            	{
	  	             	            		Context context = getApplicationContext();
	  	             	            		CharSequence text = "Errore, controlla la tua connessione e riprova.";
	  	             	            		int duration = Toast.LENGTH_SHORT;
	  	             	            		Toast toast = Toast.makeText(context, text, duration);
	  	             	            		toast.show();
	  	             	            	}
	  	             	            }
	  	             	        });
	  	             		}
	                  }.start();       
	}
	
	private class GPSLocationListener implements LocationListener {
		@Override
		public void onLocationChanged(Location location) {
			loc=String.valueOf(location.getLatitude())+" - "+String.valueOf(location.getLongitude());
        	posText.setText("Latitudine: "+String.valueOf(location.getLatitude())+"\nLongitudine: "+String.valueOf(location.getLongitude()));
        	posButton.setText("Rimuovi Posizione");
			localization=true;
			undo=false;
		}

		@Override
		public void onProviderDisabled(String provider) {}

		@Override
		public void onProviderEnabled(String provider) {}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {}

	}
	
	public void AllPos(View view) {  
		
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		posText = (TextView) findViewById(R.id.textPos);
    	posText.setVisibility(View.VISIBLE);
    	
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
			localization=false;
		}
	 }
	
	class task extends AsyncTask<String, String, Void>
	{
	private ProgressDialog progressDialog = new ProgressDialog(Activity_SegnalazioneReclami.this);
	    InputStream is = null ;
	    String result = "";
	    protected void onPreExecute() {
	    	OpResult = true;
	    	   if(!picTaken)
	    	   {
	       progressDialog.setMessage("Caricamento in corso...");
	       progressDialog.show();
	       progressDialog.setOnCancelListener(new OnCancelListener() {
		@Override
			public void onCancel(DialogInterface arg0) {
			task.this.cancel(true);
		   }
		});}
	     }
	       @Override
		protected Void doInBackground(String... params) {
		  String url_select = "http://semplicementech.t15.org/comunescafati/queryscript/setSegnalazioni.php";
		    try {
		  HttpParams httpParameters = new BasicHttpParams();
		  HttpProtocolParams.setContentCharset(httpParameters, "UTF-8");
		  HttpProtocolParams.setHttpElementCharset(httpParameters, "UTF-8");
		  
		  HttpClient httpClient = new DefaultHttpClient(httpParameters);
		  httpClient.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
		  httpClient.getParams().setParameter("http.socket.timeout", new Integer(2000));
		  httpClient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
		  httpParameters.setBooleanParameter("http.protocol.expect-continue", false);
		  HttpPost httpPost = new HttpPost(url_select);

	          ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
				param.add(new BasicNameValuePair("Oggetto",ogg));
				param.add(new BasicNameValuePair("Descrizione",descr));
				if(picTaken)
				{
					param.add(new BasicNameValuePair("Immagine",imgurl));
				}
				else
				{
					param.add(new BasicNameValuePair("Immagine","null"));
				}
				if(localization)
				{
					param.add(new BasicNameValuePair("Posizione",loc));

				}
				else
				{
					param.add(new BasicNameValuePair("Posizione","null"));

				}

			httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
	        is = entity.getContent();
	        Log.e("pass 1", "connection success ");
			} catch (Exception e) {
				OpResult = false;
			Log.e("log_tag", "Error in http connection "+e.toString());
			}
			return null;
	       }
	       
	       protected void onPostExecute(Void v) {
	    	   if(!picTaken){
				this.progressDialog.dismiss();
				if(OpResult)
         		{
         		finish();
         		Context context = getApplicationContext();
      	    	CharSequence text = "Segnalazione Inviata";
      	    	int duration = Toast.LENGTH_SHORT;
      	    	Toast toast = Toast.makeText(context, text, duration);
      	    	toast.show();
         		}
         		else if(!OpResult)
         		{
         			Context context = getApplicationContext();
          	    	CharSequence text = "Errore, controlla la tua connessione e riprova.";
          	    	int duration = Toast.LENGTH_SHORT;
          	    	Toast toast = Toast.makeText(context, text, duration);
          	    	toast.show();
         		}
	    	   }
				return;
			}

	}
	
}