package com.comune.scafati;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
/* Questa classe viene visualizzata quando l'utente seleziona
 * il pulsante "News Ed Eventi" nel MainActivity. */
public class Activity_NewsEdEventi extends FragmentActivity implements
		ActionBar.TabListener {

	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	static Intent dettagliIntent;
	static String idAll;
	static boolean conn = true;
	
	// Metodo che viene chiamato alla creazione dell'activity.
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newedeventi);
		
		// Setta l'action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		/* Crea l'adapter che si occupa di restituire il fragment relativo ad ognuna
		 * delle due sezioni della schermata. */
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Setta il ViewPager con l'adapter delle sezioni.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// Listener che seleziona il tab corrispondente quando l'utente scorre tra le diverse sezioni.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// Aggiunge un tab nella action bar per ogni sezione.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		
		if(!(conn=CheckIntConn())){
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Errore di connessione");
			alertDialog.setMessage("Verifica il collegamento ad internet.");
			alertDialog.setButton("OK", new OkOnClickListener());
			
			alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
			alertDialog.show();
		}
	}

	private final class OkOnClickListener implements DialogInterface.OnClickListener {
	  public void onClick(DialogInterface dialog, int which) {
	  Activity_NewsEdEventi.this.finish();
	  }
	} 
	

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// Quando un tab viene selezionato, mostra la pagina corrispondente nel ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	// Restituisce il fragment relativo ad una delle sezioni/tabs/pagine.
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			/* getItem viene chiamata per istanziare il fragment della pagina richiesta.
			 * Restituisce un DummySectionFragment (definito come una static inner class
			 * piu' sotto) con il numero della pagina come suo unico argomento. */
			Fragment fragment = null;
		    Bundle args = new Bundle();

		    switch(position){
		         case 0:
		          fragment = new DummySectionFragment2();
		          args.putInt(DummySectionFragment2.ARG_SECTION_NUMBER, position + 1);
		          fragment.setArguments(args);
		         break;
		         case 1:
		          fragment = new DummySectionFragment3();
		          args.putInt(DummySectionFragment3.ARG_SECTION_NUMBER, position + 1);
		          fragment.setArguments(args);
		         break;
		    }
		    return fragment;
		}

		@Override
		public int getCount() {
			// Mostra 2 pagine in totale.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section3);
			case 1:
				return getString(R.string.title_section4);
			}
			return null;
		}
	}
	
	private boolean CheckIntConn() {
        ConnectivityManager connectivityManager
              = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
	
	/* Un dummy fragment rappresenta una sezione dell'activity, questo in particolare
	 * mostra la sezione "News". */
	public static class DummySectionFragment2 extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		InputStream is = null;
		View rootView;
		
		public DummySectionFragment2() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			rootView = inflater.inflate(
					R.layout.fragment_news, container,
					false);
			
			if(conn)
			new task().execute();
			
			return rootView;
		}
		
		class task extends AsyncTask<String, String, Void>
		{
		private ProgressDialog progressDialog = new ProgressDialog(getActivity());
		    InputStream is = null ;
		    String result = "";
		    protected void onPreExecute() {
		       progressDialog.setMessage("Caricamento in corso...");
		       progressDialog.show();
		       progressDialog.setOnCancelListener(new OnCancelListener() {
			@Override
				public void onCancel(DialogInterface arg0) {
				task.this.cancel(true);
			   }
			});
		     }
		       @Override
			protected Void doInBackground(String... params) {
			  String url_select = "http://semplicementech.t15.org/comunescafati/queryscript/getAllNewsOrEventi.php";

			  HttpClient httpClient = new DefaultHttpClient();
			  HttpPost httpPost = new HttpPost(url_select);

		          ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
					param.add(new BasicNameValuePair("type","n"));

			    try {
				httpPost.setEntity(new UrlEncodedFormEntity(param));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();

				//read content
				is =  httpEntity.getContent();					

				} catch (Exception e) {

				Log.e("log_tag", "Error in http connection "+e.toString());
				}
			try {
			    BufferedReader br = new BufferedReader(new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();
				String line = "";
				while((line=br.readLine())!=null)
				{
				   sb.append(line+"\n");
				}
					is.close();
					result=sb.toString();				

						} catch (Exception e) {
							// TODO: handle exception
							Log.e("log_tag", "Error converting result "+e.toString());
						}

					return null;

				}
			protected void onPostExecute(Void v) {

				try {
					final JSONArray JArray = new JSONArray(result);
					
					ArrayList<HashMap<String, Object>> data=new ArrayList<HashMap<String,Object>>();
			        
					for(int i=0;i<JArray.length();i++)
					{
					JSONObject json_data = null;
					json_data = JArray.getJSONObject(i);

					HashMap<String,Object> newsMap=new HashMap<String, Object>();// Creiamo una mappa di valori.
	                
	                newsMap.put("titolo", json_data.getString("Titolo"));
	                String datauk = json_data.getString("Data");
	                String datait = datauk.substring(8, 10)+"/"+datauk.substring(5, 7)+"/"+datauk.substring(0, 4);
	                newsMap.put("data", datait );
	                data.add(newsMap);  // Aggiungiamo la mappa di valori alla sorgente dati.
					}
					
					String[] from={"titolo","data"}; // Dai valori contenuti in queste chiavi...
			        int[] to={R.id.titoloNE,R.id.dataNE};// Agli id delle view.
			        
			        // Costruzione dell adapter.
			        SimpleAdapter adapter=new SimpleAdapter(
			                        getActivity(),
			                        data,// Sorgente dati.
			                        R.layout.riga_lista_newsedeventi, // Layout contenente gli id di "to".
			                        from,
			                        to);
			       
			        // Utilizzo dell'adapter.
			        ((ListView)rootView.findViewById(R.id.listViewNews)).setAdapter(adapter);
			        ((ListView)rootView.findViewById(R.id.listViewNews)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
			        	@Override  
			            public void onItemClick(AdapterView<?> adattatore, final View componente, int pos, long id){  
			            // Recupero l'id della riga cliccata tramite l'ArrayAdapter.
						dettagliIntent = new Intent(getActivity().getApplicationContext(), Activity_VisualizzatoreDocumento.class);
						try {
						JSONObject json_data = null;
						json_data = JArray.getJSONObject(pos);
						dettagliIntent.putExtra("Titolo", json_data.getString("Titolo"));
						dettagliIntent.putExtra("Descrizione", json_data.getString("Descrizione"));
						String img = json_data.getString("Immagine");
						if(img == "null")
							img = null;
						dettagliIntent.putExtra("Immagine", img);
						dettagliIntent.putExtra("Tipo", "News");
						idAll = json_data.getString("CodiceNE");
						new taskAll().execute();
						} catch (Exception e) {
							// TODO: handle exception
							Log.e("log_tag", "Error parsing data "+e.toString());
						}
			          }         
			   });
					this.progressDialog.dismiss();

				} catch (Exception e) {
					// TODO: handle exception
					Log.e("log_tag", "Error parsing data "+e.toString());
				}
			}
		    }
		class taskAll extends AsyncTask<String, String, Void>
		{
		private ProgressDialog progressDialog = new ProgressDialog(getActivity());
		    InputStream is = null ;
		    String result = "";
		    protected void onPreExecute() {
		       progressDialog.setMessage("Caricamento in corso...");
		       progressDialog.show();
		       progressDialog.setOnCancelListener(new OnCancelListener() {
			@Override
				public void onCancel(DialogInterface arg0) {
				taskAll.this.cancel(true);
			   }
			});
		     }
		       @Override
			protected Void doInBackground(String... params) {
			  String url_select = "http://semplicementech.t15.org/comunescafati/queryscript/getAll.php";

			  HttpClient httpClient = new DefaultHttpClient();
			  HttpPost httpPost = new HttpPost(url_select);

		          ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
					param.add(new BasicNameValuePair("id",idAll));

			    try {
				httpPost.setEntity(new UrlEncodedFormEntity(param));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();

				//read content
				is =  httpEntity.getContent();					

				} catch (Exception e) {

				Log.e("log_tag", "Error in http connection "+e.toString());
				}
			try {
			    BufferedReader br = new BufferedReader(new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();
				String line = "";
				while((line=br.readLine())!=null)
				{
				   sb.append(line+"\n");
				}
					is.close();
					result=sb.toString();				

						} catch (Exception e) {
							// TODO: handle exception
							Log.e("log_tag", "Error converting result "+e.toString());
						}

					return null;

				}
			protected void onPostExecute(Void v) {

				try {
					final JSONArray JArray = new JSONArray(result);
			        int numAll = JArray.length();
					dettagliIntent.putExtra("NumAllegati", numAll);
			        for(int i=0;i<numAll;i++){
			                JSONObject json_data2 = JArray.getJSONObject(i);
							dettagliIntent.putExtra("TitoloAll"+i, json_data2.getString("Titolo"));
							dettagliIntent.putExtra("IndirizzoAll"+i, json_data2.getString("Indirizzo"));
			        }
						
						} catch (Exception e) {
							// TODO: handle exception
							Log.e("log_tag", "Error parsing data "+e.toString());
						}
	        	startActivity(dettagliIntent);
					this.progressDialog.dismiss();
		    }
		}
	}

	// Questo fragment mostra la sezione "Approfondimenti".
	public static class DummySectionFragment3 extends Fragment{

		public static final String ARG_SECTION_NUMBER = "section_number";
		View rootView;
		InputStream is = null;

		
		public DummySectionFragment3() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			rootView = inflater.inflate(
					R.layout.fragment_eventi, container,
					false);
			
			if(conn)
			new task2().execute();

			return rootView;
		}
		
		class task2 extends AsyncTask<String, String, Void>
		{
		private ProgressDialog progressDialog = new ProgressDialog(getActivity());
		    InputStream is = null ;
		    String result = "";
		    protected void onPreExecute() {
		       progressDialog.setMessage("Caricamento in corso...");
		       progressDialog.show();
		       progressDialog.setOnCancelListener(new OnCancelListener() {
			@Override
				public void onCancel(DialogInterface arg0) {
				task2.this.cancel(true);
			   }
			});
		     }
		       @Override
			protected Void doInBackground(String... params) {
			  String url_select = "http://semplicementech.t15.org/comunescafati/queryscript/getAllNewsOrEventi.php";

			  HttpClient httpClient = new DefaultHttpClient();
			  HttpPost httpPost = new HttpPost(url_select);

		          ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
					param.add(new BasicNameValuePair("type","e"));

			    try {
				httpPost.setEntity(new UrlEncodedFormEntity(param));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();

				//read content
				is =  httpEntity.getContent();					

				} catch (Exception e) {

				Log.e("log_tag", "Error in http connection "+e.toString());
				}
			try {
			    BufferedReader br = new BufferedReader(new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();
				String line = "";
				while((line=br.readLine())!=null)
				{
				   sb.append(line+"\n");
				}
					is.close();
					result=sb.toString();				

						} catch (Exception e) {
							// TODO: handle exception
							Log.e("log_tag", "Error converting result "+e.toString());
						}

					return null;

				}
			protected void onPostExecute(Void v) {

				try {
					final JSONArray JArray2 = new JSONArray(result);
					
					ArrayList<HashMap<String, Object>> data=new ArrayList<HashMap<String,Object>>();
			        
					for(int i=0;i<JArray2.length();i++)
					{
					JSONObject json_data = null;
					json_data = JArray2.getJSONObject(i);

					HashMap<String,Object> newsMap=new HashMap<String, Object>();// Creiamo una mappa di valori.
	                newsMap.put("titolo", json_data.getString("Titolo"));
	                String datauk = json_data.getString("Data");
	                String datait = datauk.substring(8, 10)+"/"+datauk.substring(5, 7)+"/"+datauk.substring(0, 4);
	                newsMap.put("data", datait );
	                data.add(newsMap);  // Aggiungiamo la mappa di valori alla sorgente dati.
	                
					}
					
					String[] from={"titolo","data"}; // Dai valori contenuti in queste chiavi...
			        int[] to={R.id.titoloNE,R.id.dataNE};// Agli id delle view.
			        
			        // Costruzione dell adapter.
			        SimpleAdapter adapter=new SimpleAdapter(
			                        getActivity(),
			                        data,// Sorgente dati.
			                        R.layout.riga_lista_newsedeventi, // Layout contenente gli id di "to".
			                        from,
			                        to);
			        // Utilizzo dell'adapter.
			        ((ListView)rootView.findViewById(R.id.listViewEvents)).setAdapter(adapter);
			        ((ListView)rootView.findViewById(R.id.listViewEvents)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
			        	@Override  
			            public void onItemClick(AdapterView<?> adattatore, final View componente, int pos, long id){  
			            // Recupero l'id della riga cliccata tramite l'ArrayAdapter.
						dettagliIntent = new Intent(getActivity().getApplicationContext(), Activity_VisualizzatoreDocumento.class);
						try {
						JSONObject json_data = null;
						json_data = JArray2.getJSONObject(pos);
						dettagliIntent.putExtra("Titolo", json_data.getString("Titolo"));
						dettagliIntent.putExtra("Descrizione", json_data.getString("Descrizione"));
						String img = json_data.getString("Immagine");
						if(img == "null")
							img = null;
						dettagliIntent.putExtra("Immagine", img);
						dettagliIntent.putExtra("Tipo", "Eventi");
						idAll = json_data.getString("CodiceNE");
						new taskAll2().execute();
						} catch (Exception e) {
							// TODO: handle exception
							Log.e("log_tag", "Error parsing data "+e.toString());
						}
			          }         
			   });
					this.progressDialog.dismiss();

				} catch (Exception e) {
					// TODO: handle exception
					Log.e("log_tag", "Error parsing data "+e.toString());
				}
			}
		    }
		class taskAll2 extends AsyncTask<String, String, Void>
		{
		private ProgressDialog progressDialog = new ProgressDialog(getActivity());
		    InputStream is = null ;
		    String result = "";
		    protected void onPreExecute() {
		       progressDialog.setMessage("Caricamento in corso...");
		       progressDialog.show();
		       progressDialog.setOnCancelListener(new OnCancelListener() {
			@Override
				public void onCancel(DialogInterface arg0) {
				taskAll2.this.cancel(true);
			   }
			});
		     }
		       @Override
			protected Void doInBackground(String... params) {
			  String url_select = "http://semplicementech.t15.org/comunescafati/queryscript/getAll.php";

			  HttpClient httpClient = new DefaultHttpClient();
			  HttpPost httpPost = new HttpPost(url_select);

		          ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
					param.add(new BasicNameValuePair("id",idAll));

			    try {
				httpPost.setEntity(new UrlEncodedFormEntity(param));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();

				//read content
				is =  httpEntity.getContent();					

				} catch (Exception e) {

				Log.e("log_tag", "Error in http connection "+e.toString());
				}
			try {
			    BufferedReader br = new BufferedReader(new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();
				String line = "";
				while((line=br.readLine())!=null)
				{
				   sb.append(line+"\n");
				}
					is.close();
					result=sb.toString();				

						} catch (Exception e) {
							// TODO: handle exception
							Log.e("log_tag", "Error converting result "+e.toString());
						}

					return null;

				}
			protected void onPostExecute(Void v) {

				try {
					final JSONArray JArray2 = new JSONArray(result);
			        int numAll = JArray2.length();
					dettagliIntent.putExtra("NumAllegati", numAll);
			        for(int i=0;i<numAll;i++){
			                JSONObject json_data2 = JArray2.getJSONObject(i);
							dettagliIntent.putExtra("TitoloAll"+i, json_data2.getString("Titolo"));
							dettagliIntent.putExtra("IndirizzoAll"+i, json_data2.getString("Indirizzo"));
			        }
						
						} catch (Exception e) {
							// TODO: handle exception
							Log.e("log_tag", "Error parsing data "+e.toString());
						}
	        	startActivity(dettagliIntent);
					this.progressDialog.dismiss();
		    }
		}
	}
	
}
