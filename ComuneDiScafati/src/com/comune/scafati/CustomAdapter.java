package com.comune.scafati;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class CustomAdapter extends BaseAdapter   implements OnClickListener {
	/*********** Dichiarazione Di Variabili *********/
    private Activity activity;
    private ArrayList<ListVisInfoUtili> dataVIS;
    private static LayoutInflater inflater=null;
    public Resources res;
    ListVisInfoUtili tempValues=null;
    ViewHolder holder;
    View vi;

    /*************  Metodo Costruttore *****************/
    public CustomAdapter(Activity a, ArrayList<ListVisInfoUtili> d,Resources resLocal) {

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
         
        if(convertView==null){
             
            /****** inserire il layout per le righe *******/
        	convertView = inflater.inflate(R.layout.riga_lista_visinfoutili, null);
             
            /****** vista "porta-oggetti" per contenere gli elementi del file riga_lista_visinfoutili.xml ******/

            holder = new ViewHolder();
            holder.titolo = (TextView) convertView.findViewById(R.id.titolodescr);
            holder.descrizione=(TextView)convertView.findViewById(R.id.descrInfo);
            holder.buttonchiamata=(ImageButton)convertView.findViewById(R.id.ButtonChiamata);
            holder.buttonpreferito=(ImageButton)convertView.findViewById(R.id.ButtonPreferito);
            holder.buttonmappa=(ImageButton)convertView.findViewById(R.id.ButtonMappa);
             
           /************  imposta holder per layoutinflater ************/
            convertView.setTag( holder );
        }
        else 
            holder=(ViewHolder)convertView.getTag();
         
        if(dataVIS.size()<=0)
        {
             
        }
        else
        {
            /***** Ricevi la posizione di ogni elemento dalla classe ListVisInfoUtili e lo mette nel Arraylist ********/
            tempValues=null;
            tempValues = ( ListVisInfoUtili ) dataVIS.get( position );
        
            if(tempValues.getNumCell()==null)
        	{
        		holder.buttonchiamata.setVisibility(View.INVISIBLE);
        	}
            else
            {
            	holder.buttonchiamata.setVisibility(View.VISIBLE);
            }
           
           holder.buttonpreferito.setTag(position);
           
           if(tempValues.getPreferito()==0)
           {
           	   holder.buttonpreferito.setImageResource(convertView.getResources().getIdentifier("icn_preferiti_off", "drawable", activity.getPackageName()));
	
           }	
           else
           {
           	holder.buttonpreferito.setImageResource(convertView.getResources().getIdentifier("icn_preferiti", "drawable", activity.getPackageName()));
	
           }
           
           
           if(tempValues.getIndirizzo()==null)
       	{
       		holder.buttonmappa.setVisibility(View.INVISIBLE);
       	}
           else
           {
           	holder.buttonmappa.setVisibility(View.VISIBLE);
           }
           
             holder.titolo.setText( tempValues.getTitolo() );
             holder.descrizione.setText( tempValues.getDescrizione() );
              
             /******** Setto i onClickListener per ogni bottone *******/

             holder.buttonchiamata.setOnClickListener(new OnChiamataClickListener(position));
             holder.buttonpreferito.setOnClickListener(new OnPreferitoClickListener(position));
             holder.buttonmappa.setOnClickListener(new OnIndirizzoClickListener(position));
        }
        
        return convertView;
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
        private int tagPosition;
         
        OnPreferitoClickListener(int position){
        	mPosition = position;
        }
         
        @Override
        public void onClick(View v) {

        	tagPosition=(Integer) v.getTag();
        	Log.i("ConfirmAdapter ","Order       Edit @ position : " + mPosition);
        	Log.i("ConfirmAdapter ","Order       Edit @ position : " + tagPosition);

        	Activity_VisInfoUtili sct = (Activity_VisInfoUtili)activity;

         /****  quest metodo si trova nella classe Activity_VisInfoUtili e serve a
          ****  sapere la posizione in cui sta il bottone****/

            sct.onPreferitoClick(tagPosition);
            
           
        }               
    } 
    

    private class OnIndirizzoClickListener  implements OnClickListener{           
        private int mPosition;
         
        OnIndirizzoClickListener(int position){
             mPosition = position;
        }
         
        @Override
        public void onClick(View arg0) {

          
        	Activity_VisInfoUtili sct = (Activity_VisInfoUtili)activity;

         /****  questo metodo si trova nella classe Activity_VisInfoUtili e serve a
          ****  sapere la posizione in cui sta il bottone****/

            sct.onMappaClick(mPosition);
            
        	
        	
        }               
    } 
}

