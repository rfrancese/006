package com.comune.scafati;

public class ListVisInfoUtili {
	private  String titolo="";
    private  String descrizione="";
    private  String numcell="";
    private  String id="";
    private  String indirizzo="";
     
    //Metodi Modificatori
     
    public void setTitolo(String titolo)
    {
        this.titolo = titolo;
    }
     
    public void setDescrizione(String descrizione)
    {
        this.descrizione = descrizione;
    }
     
    public void setNumCell(String numcell)
    {
        this.numcell = numcell;
    }
     
    public void setID(String id)
    {
        this.id = id;
    }
     
    public void setIndirizzo(String indirizzo)
    {
        this.indirizzo = indirizzo;
    }
     
  //Metodi Accesso
     
    public String getTitolo()
    {
        return this.titolo;
    }
     
    public String getDescrizione()
    {
        return this.descrizione;
    }
 
    public String getNumCell()
    {
        return this.numcell;
    }    

    public String getID()
    {
        return this.id;
    }  
    
    public String getIndirizzo()
    {
        return this.indirizzo;
    }  
}
