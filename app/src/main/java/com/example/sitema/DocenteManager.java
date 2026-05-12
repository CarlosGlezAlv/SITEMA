package com.example.sitema;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DocenteManager {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public DocenteManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // =======================================================
    // CRUD DE DOCENTES
    // =======================================================

    public boolean insertarDocente(Docente docente) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_DOCENTE_NUM_EMPLEADO, docente.getNumEmpleado());
        values.put(DatabaseHelper.COL_DOCENTE_NOMBRE, docente.getNombre());
        values.put(DatabaseHelper.COL_DOCENTE_DIRECCION, docente.getDireccion());

        long result = db.insert(DatabaseHelper.TABLA_DOCENTES, null, values);
        return result != -1;
    }

    public ArrayList<Docente> obtenerTodosLosDocentes() {
        ArrayList<Docente> listaDocentes = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLA_DOCENTES, null, null, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Docente docente = new Docente();
                    docente.setNumEmpleado(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DOCENTE_NUM_EMPLEADO)));
                    docente.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DOCENTE_NOMBRE)));
                    docente.setDireccion(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DOCENTE_DIRECCION)));
                    listaDocentes.add(docente);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return listaDocentes;
    }

    public Docente obtenerDocentePorEmpleado(String numEmpleado) {
        Docente docente = null;
        Cursor cursor = db.query(DatabaseHelper.TABLA_DOCENTES, null,
                DatabaseHelper.COL_DOCENTE_NUM_EMPLEADO + "=?",
                new String[]{numEmpleado}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                docente = new Docente();
                docente.setNumEmpleado(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DOCENTE_NUM_EMPLEADO)));
                docente.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DOCENTE_NOMBRE)));
                docente.setDireccion(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DOCENTE_DIRECCION)));
            }
            cursor.close();
        }
        return docente;
    }

    public boolean actualizarDocente(Docente docente) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_DOCENTE_NOMBRE, docente.getNombre());
        values.put(DatabaseHelper.COL_DOCENTE_DIRECCION, docente.getDireccion());

        int rows = db.update(DatabaseHelper.TABLA_DOCENTES, values,
                DatabaseHelper.COL_DOCENTE_NUM_EMPLEADO + "=?",
                new String[]{docente.getNumEmpleado()});
        return rows > 0;
    }

    public boolean eliminarDocente(String numEmpleado) {
        int rows = db.delete(DatabaseHelper.TABLA_DOCENTES,
                DatabaseHelper.COL_DOCENTE_NUM_EMPLEADO + "=?",
                new String[]{numEmpleado});
        return rows > 0;
    }

    // =======================================================
    // RELACIONES DE DOCENTES CON ALUMNOS
    // =======================================================

    public boolean asignarAlumno(String numEmpleado, String numControl) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_DA_NUM_EMPLEADO, numEmpleado);
        values.put(DatabaseHelper.COL_DA_NUM_CONTROL, numControl);

        long result = db.insert(DatabaseHelper.TABLA_DOCENTE_ALUMNO, null, values);
        return result != -1;
    }

    public boolean removerAlumno(String numEmpleado, String numControl) {
        int rows = db.delete(DatabaseHelper.TABLA_DOCENTE_ALUMNO,
                DatabaseHelper.COL_DA_NUM_EMPLEADO + "=? AND " + DatabaseHelper.COL_DA_NUM_CONTROL + "=?",
                new String[]{numEmpleado, numControl});
        return rows > 0;
    }

    public boolean asignarMateria(String numEmpleado, String claveMateria) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_DM_NUM_EMPLEADO, numEmpleado);
        values.put(DatabaseHelper.COL_DM_CLAVE_MATERIA, claveMateria);

        long result = db.insert(DatabaseHelper.TABLA_DOCENTE_MATERIA, null, values);
        return result != -1;
    }

    public ArrayList<Alumno> obtenerAlumnosPorDocente(String numEmpleado) {
        ArrayList<Alumno> listaAlumnos = new ArrayList<>();
        String query = "SELECT a.* FROM " + DatabaseHelper.TABLA_ALUMNOS + " a" +
                " INNER JOIN " + DatabaseHelper.TABLA_DOCENTE_ALUMNO + " da" +
                " ON a." + DatabaseHelper.COL_ALUMNO_NUM_CONTROL + " = da." + DatabaseHelper.COL_DA_NUM_CONTROL +
                " WHERE da." + DatabaseHelper.COL_DA_NUM_EMPLEADO + " = ?";
        
        Cursor cursor = db.rawQuery(query, new String[]{numEmpleado});

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
}
