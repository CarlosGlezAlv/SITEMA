package com.example.sitema;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class MateriaManager {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public MateriaManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // =======================================================
    // CRUD DE MATERIAS
    // =======================================================

    public boolean insertarMateria(Materia materia) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_MATERIA_CLAVE, materia.getClaveMateria());
        values.put(DatabaseHelper.COL_MATERIA_NOMBRE, materia.getNombreMateria());

        long result = db.insert(DatabaseHelper.TABLA_MATERIAS, null, values);
        return result != -1;
    }

    public ArrayList<Materia> obtenerTodasLasMaterias() {
        ArrayList<Materia> listaMaterias = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLA_MATERIAS, null, null, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Materia materia = new Materia();
                    materia.setClaveMateria(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MATERIA_CLAVE)));
                    materia.setNombreMateria(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MATERIA_NOMBRE)));
                    listaMaterias.add(materia);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return listaMaterias;
    }

    public Materia obtenerMateriaPorClave(String clave) {
        Materia materia = null;
        Cursor cursor = db.query(DatabaseHelper.TABLA_MATERIAS, null,
                DatabaseHelper.COL_MATERIA_CLAVE + "=?",
                new String[]{clave}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                materia = new Materia();
                materia.setClaveMateria(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MATERIA_CLAVE)));
                materia.setNombreMateria(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MATERIA_NOMBRE)));
            }
            cursor.close();
        }
        return materia;
    }

    public boolean actualizarMateria(Materia materia) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_MATERIA_NOMBRE, materia.getNombreMateria());

        int rows = db.update(DatabaseHelper.TABLA_MATERIAS, values,
                DatabaseHelper.COL_MATERIA_CLAVE + "=?",
                new String[]{materia.getClaveMateria()});
        return rows > 0;
    }

    public boolean eliminarMateria(String clave) {
        // Eliminar dependencias por Foreign Keys
        db.delete(DatabaseHelper.TABLA_ALUMNO_MATERIA,
                DatabaseHelper.COL_AM_CLAVE_MATERIA + "=?", new String[]{clave});
        db.delete(DatabaseHelper.TABLA_DOCENTE_MATERIA,
                DatabaseHelper.COL_DM_CLAVE_MATERIA + "=?", new String[]{clave});

        int rows = db.delete(DatabaseHelper.TABLA_MATERIAS,
                DatabaseHelper.COL_MATERIA_CLAVE + "=?",
                new String[]{clave});
        return rows > 0;
    }
}
