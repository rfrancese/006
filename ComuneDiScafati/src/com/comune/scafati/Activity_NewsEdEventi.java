package com.comune.scafati;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.database.Cursor;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
/* Questa classe viene visualizzata quando l'utente seleziona
 * il pulsante "News Ed Eventi" nel MainActivity. */
public class Activity_NewsEdEventi extends FragmentActivity implements
		ActionBar.TabListener {

	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	
	// Metodo che viene chiamato alla creazione dell'activity.
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
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Questo metodo crea la action bar.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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

	/* Un dummy fragment rappresenta una sezione dell'activity, questo in particolare
	 * mostra la sezione "News". */
	public static class DummySectionFragment2 extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		DBAdapter db;
		Cursor c;
		int n;
		public DummySectionFragment2() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_news, container,
					false);
			// Apro il database per prelevare la lista delle news
			db = new DBAdapter(getActivity());
			db.open();
			c = db.getAllNews();
			n = c.getCount();

			ArrayList<HashMap<String, Object>> data=new ArrayList<HashMap<String,Object>>();
	        
	        
	        for(int i=0;i<n;i++){
	                c.moveToPosition(i);
	                
	                HashMap<String,Object> newsMap=new HashMap<String, Object>();// Creiamo una mappa di valori.
	                
	                newsMap.put("titolo", c.getString(1));
	                String datauk = c.getString(5);
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
				Intent dettagliIntent = new Intent(getActivity().getApplicationContext(), Activity_VisualizzatoreDocumento.class);
				
				c.moveToPosition(pos);
				dettagliIntent.putExtra("Titolo", c.getString(1));
				dettagliIntent.putExtra("Descrizione", c.getString(2));
				dettagliIntent.putExtra("Immagine", c.getString(3));
				dettagliIntent.putExtra("Tipo", "News");
				db = new DBAdapter(getActivity());
				db.open();
				// Recupero gli allegati.
				Cursor a = db.getAllAttNE(c.getString(0));
				int numAll = a.getCount();
				dettagliIntent.putExtra("NumAllegati", numAll);
				for(int i=0;i<numAll;i++)
				{
					a.moveToPosition(i);
					dettagliIntent.putExtra("TitoloAll"+i, a.getString(0));
					dettagliIntent.putExtra("IndirizzoAll"+i, a.getString(1));
				}
				db.close();
	        	startActivity(dettagliIntent);
	          }         
	   });
			db.close();
			return rootView;
		}
	}
	
	// Questo fragment mostra la sezione "Approfondimenti".
	public static class DummySectionFragment3 extends Fragment{

		public static final String ARG_SECTION_NUMBER = "section_number";
		DBAdapter db;
		Cursor c;
		int n;
		public DummySectionFragment3() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_eventi, container,
					false);
			
			// Apro il database per prelevare la lista degli eventi.
						db = new DBAdapter(getActivity());
						db.open();
						c = db.getAllEvents();
						n = c.getCount();

						ArrayList<HashMap<String, Object>> data=new ArrayList<HashMap<String,Object>>();
				        
				        
				        for(int i=0;i<n;i++){
				                c.moveToPosition(i);
				                
				                HashMap<String,Object> eventsMap=new HashMap<String, Object>();// Creiamo una mappa di valori.
				                
				                eventsMap.put("titolo", c.getString(1));
				                String datauk = c.getString(5);
				                String datait = datauk.substring(8, 10)+"/"+datauk.substring(5, 7)+"/"+datauk.substring(0, 4);
				                eventsMap.put("data", datait );
				                data.add(eventsMap);  // Aggiungiamo la mappa di valori alla sorgente dati.
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
							Intent dettagliIntent = new Intent(getActivity().getApplicationContext(), Activity_VisualizzatoreDocumento.class);
							
							c.moveToPosition(pos);
							dettagliIntent.putExtra("Titolo", c.getString(1));
							dettagliIntent.putExtra("Descrizione", c.getString(2));
							dettagliIntent.putExtra("Immagine", c.getString(3));
							dettagliIntent.putExtra("Tipo", "Eventi");
							db = new DBAdapter(getActivity());
							db.open();
							// Recupero gli allegati.
							Cursor a = db.getAllAttNE(c.getString(0));
							int numAll = a.getCount();
							dettagliIntent.putExtra("NumAllegati", numAll);
							for(int i=0;i<numAll;i++)
							{
								a.moveToPosition(i);
								dettagliIntent.putExtra("TitoloAll"+i, a.getString(0));
								dettagliIntent.putExtra("IndirizzoAll"+i, a.getString(1));
							}
							db.close();
				        	startActivity(dettagliIntent);
				          }         
				   });
						db.close();
			return rootView;
			

		}
	}
	
	
}
