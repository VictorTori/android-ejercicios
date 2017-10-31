package com.sfaci.noticias.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.constraint.solver.ArrayLinkedVariables;

import com.sfaci.noticias.base.Noticia;
import com.sfaci.noticias.util.Util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import static com.sfaci.noticias.util.Constantes.AUTOR;
import static com.sfaci.noticias.util.Constantes.FECHA;
import static com.sfaci.noticias.util.Constantes.TABLA_NOTICIAS;
import static com.sfaci.noticias.util.Constantes.TEXTO;
import static com.sfaci.noticias.util.Constantes.TITULO;


public class BaseDatos extends SQLiteOpenHelper {

    private static final String NOMBRE = "noticias.db";
    private static final int VERSION = 1;

    public BaseDatos(Context context) {
        super(context, NOMBRE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLA_NOTICIAS + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                TITULO + " TEXT, " + TEXTO + " TEXT, " +
                AUTOR + " TEXT, " + FECHA + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE " + TABLA_NOTICIAS);
        onCreate(db);
    }

    public void insertarNoticia(Noticia noticia) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(TITULO, noticia.getTitulo());
        valores.put(TEXTO, noticia.getTexto());
        valores.put(AUTOR, noticia.getAutor());
        valores.put(FECHA, Util.formatFecha(noticia.getFecha()));

        db.insertOrThrow(TABLA_NOTICIAS, null, valores);
    }

    public ArrayList<Noticia> obtenerNoticias() {

        ArrayList<Noticia> noticias = new ArrayList<>();
        final String[] SELECT = {TITULO, TEXTO, AUTOR, FECHA};
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor =
                db.query(TABLA_NOTICIAS, SELECT, null, null, null, null, null);
        while(cursor.moveToNext()) {
            Noticia noticia = new Noticia();
            noticia.setTitulo(cursor.getString(0));
            noticia.setTexto(cursor.getString(1));
            noticia.setAutor(cursor.getString(2));
            try {
                noticia.setFecha(Util.parseFecha(cursor.getString(3)));
            } catch (ParseException pe) {
                noticia.setFecha(new Date(0));
            }
            noticias.add(noticia);
        }
        cursor.close();
        db.close();

        return noticias;
    }
}
