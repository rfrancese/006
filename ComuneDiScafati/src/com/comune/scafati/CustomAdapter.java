package com.comune.scafati;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter   implements OnClickListener {
	/*********** Dichiarazione Di Variabili *********/
    private Activity activity;
    private ArrayList dataVIS;
    private static LayoutInflater inflater=null;
    public Resources res;
    ListVisInfoUtili tempValues=null;
    int i=0;
     
    /*************  Metodo Costruttore *****************/
    public CustomAdapter(Activity a, ArrayList d,Resources resLocal) {

            activity = a;
            dataVIS=d;
            res = resLocal;

            inflater = ( LayoutInflater )activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         
    }
 
    /******** Dimensione dell'arraylist ************/
    public int getCount() {
         
        if(dataVIS.size()<=0)
            return 1;
        return dataVIS.size();
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }
     
    /********* Creo un classe di supporto per contenere gli elementi del file xml *********/
    public static class ViewHolder{
         
        public TextView titolo;
        public TextView descrizione;
        public ImageButton buttonchiamata;
        public ImageButton buttonpreferito;
        public ImageButton buttonmappa;
 
    }
 
    /****** Dipende dalle dimensioni dei dati chiamati per ogni riga. Creare ogni riga nella ListView *****/
    public View getView(int position, View convertView, ViewGroup parent) {
         
        View vi = convertView;
        ViewHolder holder;
         
        if(convertView==null){
             
            /****** inserire il layout per le righe *******/
            vi = inflater.inflate(R.layout.riga_lista_visinfoutili, null);
             
            /****** vista "porta-oggetti" per contenere gli elementi del file riga_lista_visinfoutili.xml ******/

            holder = new ViewHolder();
            holder.titolo = (TextView) vi.findViewById(R.id.titolodescr);
            holder.descrizione=(TextView)vi.findViewById(R.id.descrInfo);
            holder.buttonchiamata=(ImageButton)vi.findViewById(R.id.ButtonChiamata);
            holder.buttonpreferito=(ImageButton)vi.findViewById(R.id.ButtonPreferito);
            holder.buttonmappa=(ImageButton)vi.findViewById(R.id.ButtonMappa);
             
           /************  imposta holder per layoutinflater ************/
            vi.setTag( holder );
        }
        else 
            holder=(ViewHolder)vi.getTag();
         
        if(dataVIS.size()<=0)
        {
             
        }
        else
        {
            /***** Ricevi ogni elemento dalla classe ListVisInfoUtili e lo mette nel Arraylist ********/
            tempValues=null;
            tempValues = ( ListVisInfoUtili ) dataVIS.get( position );

             holder.titolo.setText( tempValues.getTitolo() );
             holder.descrizione.setText( tempValues.getDescrizione() );
              /*holder.image.setImageResource(
                          res.getIdentifier(
                          "com.androidexample.customlistview:drawable/"+tempValues.getImage()
                          ,null,null));*/
              
             /******** Setto i onClickListener per ogni bottone *******/

             holder.buttonchiamata.setOnClickListener(new OnChiamataClickListener( position ));
             holder.buttonpreferito.setOnClickListener(new OnPreferitoClickListener( position ));
        }
        return vi;
    }
     
    @Override
    public void onClick(View v) {
            Log.v("CustomAdapter", "=====Row button clicked=====");
    }
     
    /********* Classi che vengono chiamati quando i bottoni vengono cliccati nella ListView ************/
    
    
    private class OnChiamataClickListener  implements OnClickListener{           
        private int mPosition;
         
        OnChiamataClickListener(int position){
             mPosition = position;
        }
         
        @Override
        public void onClick(View arg0) {

   
        	Activity_VisInfoUtili sct = (Activity_VisInfoUtili)activity;

         /****  questo metodo si trova nella classe Activity_VisInfoUtili serve a
          ****  sapere la posizione in cui sta il bottone****/

            sct.onChiamataClick(mPosition);
        }               
    }   
    
    private class OnPreferitoClickListener  implements OnClickListener{           
        private int mPosition;
         
        OnPreferitoClickListener(int position){
             mPosition = position;
        }
         
        @Override
        public void onClick(View arg0) {

   
        	Activity_VisInfoUtili sct = (Activity_VisInfoUtili)activity;

         /****  questo metodo si trova nella classe Activity_VisInfoUtili serve a
          ****  sapere la posizione in cui sta il bottone****/

            sct.onPreferitoClick(mPosition);
        }               
    } 
}

