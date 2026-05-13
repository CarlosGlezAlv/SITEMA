package com.example.sitema;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class AlumnoManager {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public AlumnoManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // =======================================================
    // CRUD DE ALUMNOS
    // =======================================================

    public boolean insertarAlumno(Alumno alumno) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_ALUMNO_NUM_CONTROL, alumno.getNumControl());
        values.put(DatabaseHelper.COL_ALUMNO_NOMBRE, alumno.getNombre());
        values.put(DatabaseHelper.COL_ALUMNO_TELEFONO, alumno.getTelefono());

        long result = db.insert(DatabaseHelper.TABLA_ALUMNOS, null, values);
        return result != -1;
    }

    public ArrayList<Alumno> obtenerTodosLosAlumnos() {
        ArrayList<Alumno> listaAlumnos = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLA_ALUMNOS, null, null, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Alumno alumno = new Alumno();
                    alumno.setNumControl(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ALUMNO_NUM_CONTROL)));
                    alumno.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ALUMNO_NOMBRE)));
                    alumno.setTelefono(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ALUMNO_TELEFONO)));
                    listaAlumnos.add(alumno);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return listaAlumnos;
    }

    public Alumno obtenerAlumnoPorControl(String numControl) {
        Alumno alumno = null;
        Cursor cursor = db.query(DatabaseHelper.TABLA_ALUMNOS, null,
                DatabaseHelper.COL_ALUMNO_NUM_CONTROL + "=?",
                new String[]{numControl}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                alumno = new Alumno();
                alumno.setNumControl(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ALUMNO_NUM_CONTROL)));
                alumno.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ALUMNO_NOMBRE)));
                alumno.setTelefono(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ALUMNO_TELEFONO)));
            }
            cursor.close();
        }
        return alumno;
    }

    public boolean actualizarAlumno(Alumno alumno) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_ALUMNO_NOMBRE, alumno.getNombre());
        values.put(DatabaseHelper.COL_ALUMNO_TELEFONO, alumno.getTelefono());

        int rows = db.update(DatabaseHelper.TABLA_ALUMNOS, values,
                DatabaseHelper.COL_ALUMNO_NUM_CONTROL + "=?",
                new String[]{alumno.getNumControl()});
        return rows > 0;
    }

    public boolean eliminarAlumno(String numControl) {
        int rows = db.delete(DatabaseHelper.TABLA_ALUMNOS,
                DatabaseHelper.COL_ALUMNO_NUM_CONTROL + "=?",
                new String[]{numControl});
        return rows > 0;
    }

    // =======================================================
    // RELACIONES DE ALUMNOS CON MATERIAS
    // =======================================================

    public boolean asignarMateria(String numControl, String claveMateria, double calificacion) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_AM_NUM_CONTROL, numControl);
        values.put(DatabaseHelper.COL_AM_CLAVE_MATERIA, claveMateria);
        values.put(DatabaseHelper.COL_AM_CALIFICACION, calificacion);

        long result = db.insert(DatabaseHelper.TABLA_ALUMNO_MATERIA, null, values);
        return result != -1;
    }

    public boolean actualizarCalificacion(String numControl, String claveMateria, double nuevaCalificacion) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_AM_CALIFICACION, nuevaCalificacion);

        int rows = db.update(DatabaseHelper.TABLA_ALUMNO_MATERIA, values,
                DatabaseHelper.COL_AM_NUM_CONTROL + "=? AND " + DatabaseHelper.COL_AM_CLAVE_MATERIA + "=?",
                new String[]{numControl, claveMateria});
        return rows > 0;
    }

    public ArrayList<MateriaCursada> obtenerMateriasYCalificaciones(String numControl) {
        ArrayList<MateriaCursada> listaMaterias = new ArrayList<>();
        String query = "SELECT m." + DatabaseHelper.COL_MATERIA_CLAVE + ", m." + DatabaseHelper.COL_MATERIA_NOMBRE +
                ", am." + DatabaseHelper.COL_AM_CALIFICACION +
                " FROM " + DatabaseHelper.TABLA_MATERIAS + " m" +
                " INNER JOIN " + DatabaseHelper.TABLA_ALUMNO_MATERIA + " am" +
                " ON m." + DatabaseHelper.COL_MATERIA_CLAVE + " = am." + DatabaseHelper.COL_AM_CLAVE_MATERIA +
                " WHERE am." + DatabaseHelper.COL_AM_NUM_CONTROL + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{numControl});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    MateriaCursada mc = new MateriaCursada();
                    mc.setClaveMateria(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MATERIA_CLAVE)));
                    mc.setNombreMateria(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MATERIA_NOMBRE)));
                    mc.setCalificacion(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_AM_CALIFICACION)));
                    listaMaterias.add(mc);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return listaMaterias;
    }
}
