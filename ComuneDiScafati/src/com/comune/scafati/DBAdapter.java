package com.comune.scafati;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// Classe che si occupa dell'interfacciamento dell'applicazione con il Database.
public class DBAdapter extends ListActivity 
{
    
    private static final String DATABASE_NAME = "comuneScafati";
    private static final int DATABASE_VERSION = 1;
        
    private final Context context; 
    
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) 
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
    
    // Sottoclasse che si occupa di verificare se il database esiste gia', e in caso contrario lo crea.
    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
    	private static String DATABASE_NAME = "comuneScafati";
    	public final static String DATABASE_PATH = "/data/data/com.comune.scafati/databases/";
    	private static final int DATABASE_VERSION = 1;

    	private SQLiteDatabase dataBase;
    	private final Context dbContext;

    	public DatabaseHelper(Context context) {
    	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    	    this.dbContext = context;
    	    if (checkDataBase()) {
    	        openDataBase();
    	    } else
    	    {
    	        try {
    	            this.getReadableDatabase();
    	            copyDataBase();
    	            this.close();
    	            openDataBase();

    	        } catch (IOException e) {
    	            throw new Error("Error copying database");
    	        }
    	    }
    	}

    	private void copyDataBase() throws IOException{
    	    InputStream myInput = dbContext.getAssets().open(DATABASE_NAME);
    	    String outFileName = DATABASE_PATH + DATABASE_NAME;
    	    OutputStream myOutput = new FileOutputStream(outFileName);

    	    byte[] buffer = new byte[1024];
    	    int length;
    	    while ((length = myInput.read(buffer))>0){
    	        myOutput.write(buffer, 0, length);
    	    }

    	    myOutput.flush();
    	    myOutput.close();
    	    myInput.close();
    	}

    	public void openDataBase() throws SQLException {
    	    String dbPath = DATABASE_PATH + DATABASE_NAME;
    	    dataBase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    	}

    	private boolean checkDataBase() {
    	    SQLiteDatabase checkDB = null;
    	    boolean exist = false;
    	    try {
    	        String dbPath = DATABASE_PATH + DATABASE_NAME;
    	        checkDB = SQLiteDatabase.openDatabase(dbPath, null,
    	                SQLiteDatabase.OPEN_READONLY);
    	    } catch (SQLiteException e) {
    	        Log.v("db log", "database does't exist");
    	    }

    	    if (checkDB != null) {
    	        exist = true;
    	        checkDB.close();
    	    }
    	    return exist;
    	}

        // Metodi vuoti che bisogna implementare obbligatoriamente per la classe estesa SQLiteOpenHelper 
        @Override
        public void onCreate(SQLiteDatabase db) 
        {
            
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
        int newVersion) 
        {
            
        }
    }    
    
    // Apre il database.
    public DBAdapter open() throws SQLException 
    {
        db = DBHelper.getReadableDatabase();
        return this;
    }

    // Chiude il database.   
    public void close() 
    {
        DBHelper.close();
    }
    // Restituisce tutte le news.
    public Cursor getAllNews()
    {
    	String query = "select CodiceNE,Titolo,Descrizione,Immagine,Tipo,Data from NewsEdEventi where Tipo='n';";
    	return db.rawQuery(query, null);
    }
    // Restituisce tutti gli eventi.
    public Cursor getAllEvents()
    {
    	String query = "select CodiceNE,Titolo,Descrizione,Immagine,Tipo,Data from NewsEdEventi where Tipo='e';";
    	return db.rawQuery(query, null);
    }
    // Restituisce tutti gli allegati di una data News o Evento.
    public Cursor getAllAttNE(String id)
    {
    	String query = "select Titolo,Indirizzo from Allegati where Allegati.CodiceNE='"+id+"';";
    	return db.rawQuery(query, null);
    }
    // Restituisce tutti gli allegati di un dato elemento di Amministrazione
    public Cursor getAllAttAmm(String id)
    {
    	String query = "select Titolo,Indirizzo from Allegati where Allegati.CodiceA='"+id+"';";
    	return db.rawQuery(query, null);
    }
    // Restituisce tutti gli elementi che si riferscono a un tipo di Informazione utile.
    public Cursor getIU(String tipo)
    {  String query=" select InfoUtili.CodiceIU,Nome,Descrizione,NumeroTelefono,Indirizzo from InfoUtili,NumeriTelefonici,Indirizzi where NumeriTelefonici.CodiceIU=InfoUtili.CodiceIU and InfoUtili.CodiceIU=Indirizzi.CodiceIU and Tipo='"+tipo+"';";
    	//String query = "select Nome,Descrizione from InfoUtili where Tipo='"+tipo+"';";
    	return db.rawQuery(query, null);
    }
    // Restituisce gli elementi di una sottolista di Amministrazione.
    public Cursor getSubItemAmm(String tipo)
    {
    	String query = "select CodiceA,Nome,Descrizione,Immagine from Amministrazione where Tipo='"+tipo+"';";
    	return db.rawQuery(query, null);
    }
    // Restituisce le informazioni di una data Galleria Fotografica.
    public Cursor getGallery(String id)
    {
    	String query = "select Titolo,Descrizione from Galleria where CodiceG='"+id+"';";
    	return db.rawQuery(query, null);
    }
    // Restituisce tutte le foto di una data Galleria Fotografica.
    public Cursor getGalleryPic(String id)
    {
    	String query = "select Indirizzo from FotoGalleria where CodiceG='"+id+"';";
    	return db.rawQuery(query, null);
    }
    
    public Cursor insertPreferito(String id)
    {
    	String query = "INSERT INTO Preferiti (Preferito) VALUES ('"+id+"')";
    	return db.rawQuery(query, null);
    }
    public Cursor deletePreferito(String id)
    {
    	String query = "DELETE FROM Preferiti WHERE Preferito ='"+id+"'";
    	return db.rawQuery(query, null);
    }
}
