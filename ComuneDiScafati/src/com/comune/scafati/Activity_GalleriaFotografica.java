package com.comune.scafati;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/* Questa classe viene visualizzata quando l'utente seleziona una galleria
 * nel fragment "Approfondimenti" all'interno dell'Activity_CenniStorici,
 * e serve a visualizzare la schermata con la descrizione della
 * galleria fotografica e le foto che la compongono. */
public class Activity_GalleriaFotografica extends Activity {

	DBAdapter db;
	Cursor c,a;
	int n,cur;
	GridView griglia;
	GridviewAdapter mAdapter;
	ArrayList<Drawable> listImg;
	final Context context = this;
	
	// Metodo che viene chiamato alla creazione dell'activity.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_galleria_fotografica);
		Intent intent=getIntent();
		
		// Apro il database per prelevare le informazioni della galleria selezionata e le relative foto.
		db = new DBAdapter(this);
		db.open();
		String galleryNum = intent.getStringExtra("GalleryNumber");
		c = db.getGallery(galleryNum);
		a = db.getGalleryPic(galleryNum);
		n = a.getCount();
		cur = 0;
		
		prepareList();
		
		c.moveToFirst();
		setTitle(c.getString(0));
		TextView descrizione = (TextView)findViewById(R.id.descrImage);
		descrizione.append(c.getString(1));
		
	}
	
	// Questo metodo carica listImg con le immagini da visualizzare.
	public void prepareList()
    {
          listImg = new ArrayList<Drawable>();
          for(int i=0;i<n;i++)
          {
        	a.moveToPosition(i);
        	String immagine = a.getString(0);
        	CaricaImmagine caricaImmagineAsync = new CaricaImmagine();
      		caricaImmagineAsync.execute(new String[]{immagine});
          }
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Questo metodo crea la action bar.
		getMenuInflater().inflate(R.menu.activity__galleria_fotografica, menu);
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
			listImg.add(result);
			cur++;
			/* Ad ogni immagine caricata e aggiunta a listImg incremento cur,
			 * quando ho scaricato tutte le immagini la condizione dell'if sarà
			 * vera e verranno eseguite le istruzioni seguenti. */
			if(cur==n)
			{
				// passo listImg all'Adapter della gridview
		        mAdapter = new GridviewAdapter(Activity_GalleriaFotografica.this, listImg);
		 
		        // Setto l'adapter della gridview
		        griglia = (GridView) findViewById(R.id.grigliaImmagini);
		        griglia.setAdapter(mAdapter);
		        TextView loading =(TextView)findViewById(R.id.loading);
		        //Controllo se c'è la connessione
		        if(isOnline())
		        {
		        	// Nascondo la scritta del caricamento e rendo visibile la gridview con le immagini.
		        	loading.setVisibility(View.GONE);
		        	griglia.setVisibility(View.VISIBLE);
		        	griglia.setOnItemClickListener(new OnItemClickListener()
		        	{
		            	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
							// Creo la finestra di prompt che appare quando un'immagine viene cliccata.
		            		LayoutInflater li = LayoutInflater.from(context);
							View promptsView = li.inflate(R.layout.image_prompt, null);
		 
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		 
							alertDialogBuilder.setView(promptsView);
		 
							final ImageView imageZoom = (ImageView) promptsView.findViewById(R.id.imageZoom);
							imageZoom.setImageDrawable(listImg.get(position));
						
							alertDialogBuilder
								.setCancelable(false)
								.setNegativeButton("Chiudi",
									new DialogInterface.OnClickListener() {
							    	public void onClick(DialogInterface dialog,int id) {
							    	dialog.cancel();
							    	}
								});
		 
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();
		            	}
		        	});
		        }
		        else
		        {
		        	loading.setText("Errore! Controlla la tua connessione\nad internet.");
		        	loading.setTextColor(Color.RED);
		        }
			}      
		}    	
    }
	
	// Funzione che restituisce true se il telefono è connesso ad internet, false altrimenti.
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	
	// Sottoclasse per definire il comportamento della gridview.
	public class GridviewAdapter extends BaseAdapter
	{
	    private ArrayList<Drawable> listImg;
	    private Activity activity;
	 
	    public GridviewAdapter(Activity activity, ArrayList<Drawable> listImg) {
	        super();
	        this.listImg = listImg;
	        this.activity = activity;
	    }
	 
	    @Override
	    public int getCount() {
	        // TODO Auto-generated method stub
	        return listImg.size();
	    }
	 
	    @Override
	    public Drawable getItem(int position) {
	        // TODO Auto-generated method stub
	        return listImg.get(position);
	    }
	 
	    @Override
	    public long getItemId(int position) {
	        // TODO Auto-generated method stub
	        return 0;
	    }
	 
	    public class ViewHolder
	    {
	        public ImageView imgView;
	    }
	 
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        // TODO Auto-generated method stub
	        ViewHolder view;
	        LayoutInflater inflator = activity.getLayoutInflater();
	 
	        if(convertView==null)
	        {
	            view = new ViewHolder();
	            convertView = inflator.inflate(R.layout.gridview_row, null);
	 
	            view.imgView = (ImageView) convertView.findViewById(R.id.imageView1);
	 
	            convertView.setTag(view);
	        }
	        else
	        {
	            view = (ViewHolder) convertView.getTag();
	        }
	        view.imgView.setImageDrawable(listImg.get(position));
	 
	        return convertView;
	    }
	}
}