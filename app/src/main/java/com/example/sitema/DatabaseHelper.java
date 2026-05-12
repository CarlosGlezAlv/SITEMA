package com.example.sitema;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SistemaEscolar.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLA_ALUMNOS = "alumnos";
    public static final String COL_ALUMNO_NUM_CONTROL = "numero_control";
    public static final String COL_ALUMNO_NOMBRE = "nombre";
    public static final String COL_ALUMNO_TELEFONO = "numero_telefono";

    // Tabla Docentes
    public static final String TABLA_DOCENTES = "docentes";
    public static final String COL_DOCENTE_NUM_EMPLEADO = "numero_empleado";
    public static final String COL_DOCENTE_NOMBRE = "nombre";
    public static final String COL_DOCENTE_DIRECCION = "direccion";

    // Tabla Materias
    public static final String TABLA_MATERIAS = "materias";
    public static final String COL_MATERIA_CLAVE = "clave_materia";
    public static final String COL_MATERIA_NOMBRE = "nombre_materia";

    // Tabla Alumno_Materia (Relación Muchos a Muchos: Alumno tiene varias materias
    // con calificación)
    public static final String TABLA_ALUMNO_MATERIA = "alumno_materia";
    public static final String COL_AM_NUM_CONTROL = "numero_control";
    public static final String COL_AM_CLAVE_MATERIA = "clave_materia";
    public static final String COL_AM_CALIFICACION = "calificacion";

    // Tabla Docente_Alumno (Relación Muchos a Muchos: Docentes pueden tener varios alumnos)
    public static final String TABLA_DOCENTE_ALUMNO = "docente_alumno";
    public static final String COL_DA_NUM_EMPLEADO = "numero_empleado";
    public static final String COL_DA_NUM_CONTROL = "numero_control";

    // Tabla Docente_Materia (Relación Docentes - Materias)
    public static final String TABLA_DOCENTE_MATERIA = "docente_materia";
    public static final String COL_DM_NUM_EMPLEADO = "numero_empleado";
    public static final String COL_DM_CLAVE_MATERIA = "clave_materia";

    // Sentencias de creación de tablas
    private static final String CREATE_TABLE_ALUMNOS = "CREATE TABLE " + TABLA_ALUMNOS + "("
            + COL_ALUMNO_NUM_CONTROL + " TEXT PRIMARY KEY,"
            + COL_ALUMNO_NOMBRE + " TEXT NOT NULL,"
            + COL_ALUMNO_TELEFONO + " TEXT"
            + ");";

    private static final String CREATE_TABLE_DOCENTES = "CREATE TABLE " + TABLA_DOCENTES + "("
            + COL_DOCENTE_NUM_EMPLEADO + " TEXT PRIMARY KEY,"
            + COL_DOCENTE_NOMBRE + " TEXT NOT NULL,"
            + COL_DOCENTE_DIRECCION + " TEXT"
            + ");";

    private static final String CREATE_TABLE_MATERIAS = "CREATE TABLE " + TABLA_MATERIAS + "("
            + COL_MATERIA_CLAVE + " TEXT PRIMARY KEY,"
            + COL_MATERIA_NOMBRE + " TEXT NOT NULL"
            + ");";

    private static final String CREATE_TABLE_ALUMNO_MATERIA = "CREATE TABLE " + TABLA_ALUMNO_MATERIA + "("
            + COL_AM_NUM_CONTROL + " TEXT,"
            + COL_AM_CLAVE_MATERIA + " TEXT,"
            + COL_AM_CALIFICACION + " REAL," // REAL para guardar decimales
            + "PRIMARY KEY(" + COL_AM_NUM_CONTROL + ", " + COL_AM_CLAVE_MATERIA + "),"
            + "FOREIGN KEY(" + COL_AM_NUM_CONTROL + ") REFERENCES " + TABLA_ALUMNOS + "(" + COL_ALUMNO_NUM_CONTROL
            + "),"
            + "FOREIGN KEY(" + COL_AM_CLAVE_MATERIA + ") REFERENCES " + TABLA_MATERIAS + "(" + COL_MATERIA_CLAVE + ")"
            + ");";

    private static final String CREATE_TABLE_DOCENTE_ALUMNO = "CREATE TABLE " + TABLA_DOCENTE_ALUMNO + "("
            + COL_DA_NUM_EMPLEADO + " TEXT,"
            + COL_DA_NUM_CONTROL + " TEXT,"
            + "PRIMARY KEY(" + COL_DA_NUM_EMPLEADO + ", " + COL_DA_NUM_CONTROL + "),"
            + "FOREIGN KEY(" + COL_DA_NUM_EMPLEADO + ") REFERENCES " + TABLA_DOCENTES + "(" + COL_DOCENTE_NUM_EMPLEADO
            + "),"
            + "FOREIGN KEY(" + COL_DA_NUM_CONTROL + ") REFERENCES " + TABLA_ALUMNOS + "(" + COL_ALUMNO_NUM_CONTROL + ")"
            + ");";

    private static final String CREATE_TABLE_DOCENTE_MATERIA = "CREATE TABLE " + TABLA_DOCENTE_MATERIA + "("
            + COL_DM_NUM_EMPLEADO + " TEXT,"
            + COL_DM_CLAVE_MATERIA + " TEXT,"
            + "PRIMARY KEY(" + COL_DM_NUM_EMPLEADO + ", " + COL_DM_CLAVE_MATERIA + "),"
            + "FOREIGN KEY(" + COL_DM_NUM_EMPLEADO + ") REFERENCES " + TABLA_DOCENTES + "(" + COL_DOCENTE_NUM_EMPLEADO + "),"
            + "FOREIGN KEY(" + COL_DM_CLAVE_MATERIA + ") REFERENCES " + TABLA_MATERIAS + "(" + COL_MATERIA_CLAVE + ")"
            + ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        // Habilitar soporte para Foreign Keys, recomendado en SQLite
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear las tablas
        db.execSQL(CREATE_TABLE_ALUMNOS);
        db.execSQL(CREATE_TABLE_DOCENTES);
        db.execSQL(CREATE_TABLE_MATERIAS);
        db.execSQL(CREATE_TABLE_ALUMNO_MATERIA);
        db.execSQL(CREATE_TABLE_DOCENTE_ALUMNO);
        db.execSQL(CREATE_TABLE_DOCENTE_MATERIA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // En caso de actualizar la versión de la DB, borramos las tablas anteriores y
        // las volvemos a crear
        // El orden de borrado importa por las Foreign Keys
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_DOCENTE_MATERIA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_DOCENTE_ALUMNO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_ALUMNO_MATERIA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_MATERIAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_DOCENTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_ALUMNOS);
        onCreate(db);
    }
}
