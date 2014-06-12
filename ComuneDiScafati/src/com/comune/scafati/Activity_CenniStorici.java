package com.comune.scafati;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
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
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
/* Questa classe viene visualizzata quando l'utente seleziona
 * il pulsante "Cenni Storici" nel MainActivity, e mostra le due
 * sezioni "Storia" e "Approfondimenti" che contengono la storia
 * della citta' con alcuni approfondimenti e gallerie fotografiche. */
public class Activity_CenniStorici extends FragmentActivity implements
		ActionBar.TabListener {

	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	static ListView listview;
	
	// Metodo che viene chiamato alla creazione dell'activity.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cenni_storici);

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
		          fragment = new DummySectionFragment();
		          args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
		          fragment.setArguments(args);
		         break;
		         case 1:
		          fragment = new DummySectionFragment1();
		          args.putInt(DummySectionFragment1.ARG_SECTION_NUMBER, position + 1);
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
			// Restituisce il titolo della pagina
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1);
			case 1:
				return getString(R.string.title_section2);
			}
			return null;
		}
	}

	/* Un dummy fragment rappresenta una sezione dell'activity, questo in particolare
	 * mostra la sezione "Storia". */
	public static class DummySectionFragment extends Fragment {

		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_storia, container,
					false);
			WebView view = new WebView(getActivity());
		    view.setVerticalScrollBarEnabled(false);

		    ((LinearLayout)rootView.findViewById(R.id.insert_webview)).addView(view);

		    view.loadUrl("file:///android_asset/html/storia.html");
			return rootView;
		}
	}
	
	// Questo fragment mostra la sezione "Approfondimenti".
	public static class DummySectionFragment1 extends Fragment{
		
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment1() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_approfondimenti, container,
					false);
			 listview =(ListView)rootView.findViewById(R.id.listView1);
			 
	           // Dichiaro due Array di tipo String che contengono rispettivamente i titoli e le descrizioni delle gallerie.
	           String[] ListItems = new String[] {"Scorci,borghi e vedute panoramiche della citta' di Scafati", "Ambiente e beni archittettonici della citta'", "Arte e scultura Scafati"};
	           String[] SubItems = new String[] {"Visualizza le immagini fotografiche della citta' di Scafati. La storia, le abitudini e le tradizioni", "Scopri il Palazzo Santonicola, il Polverificio e la villa comunale", "Monumenti, arte e scultura si fondono in una citta' ricca di storia"};
	            
	           List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	            for (int i=0; i<ListItems.length; i++) {
	                Map<String, String> datum = new HashMap<String, String>(2);
	                datum.put("title", ListItems[i]);
	                datum.put("subtitle", String.valueOf(SubItems[i]));
	                data.add(datum);
	            }
	            
	            // Creo e imposto l'adapter e il clicklistener per la lista.
	            SimpleAdapter adapter = new SimpleAdapter(getActivity(), data,
	                                                  R.layout.riga_lista_cennistorici,
	                                                  new String[] {"title", "subtitle"},
	                                                  new int[] {R.id.text1,
	                                                             R.id.text2});
	            
	            listview.setAdapter(adapter);
	            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		        	@Override  
		            public void onItemClick(AdapterView<?> adattatore, final View componente, int pos, long id){
		        		// Passo il valore della galleria cliccata all'Activity_GalleriaFotografica e la avvio.
		        		Intent galleriaIntent = new Intent(getActivity().getApplicationContext(), Activity_GalleriaFotografica.class);
		        		pos++;
		        		galleriaIntent.putExtra("GalleryNumber", ""+pos);
			        	startActivity(galleriaIntent);
		        	}
		        });
	            
				WebView view = new WebView(getActivity());
			    view.setVerticalScrollBarEnabled(false);

			    ((LinearLayout)rootView.findViewById(R.id.insert_webview2)).addView(view);

			    view.loadUrl("file:///android_asset/html/approfondimenti.html");
	            
			return rootView;
		}
	}  
	public void onWindowFocusChanged(boolean hasFocus) {
		 // get content height
		 int firstPosition = listview.getFirstVisiblePosition() - listview.getHeaderViewsCount();
		 int contentHeight = 0;
		 
		 for(int i=0; i<3; i++)
			 contentHeight += listview.getChildAt(i+firstPosition).getHeight();

		 // set listview height
		 LayoutParams lp = listview.getLayoutParams();
		 lp.height = contentHeight;
		 listview.setLayoutParams(lp);
		}
}

