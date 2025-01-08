package com.example.appcomidas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Definir el nombre de la base de datos y la versión
    private static final String DATABASE_NAME = "userbd.db";
    private static final int DATABASE_VERSION = 8;
    // Información de los usuarios
    private static final String TABLE_USERS = "Users";
    private static final String KEY_USER_ID = "User_Id";
    private static final String KEY_USERNAME = "UserName";
    private static final String KEY_NAME = "Name";
    private static final String KEY_LAST_NAME = "LastName";
    private static final String KEY_PASSWORD = "Password";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Método onCreate para crear las tablas necesarias al inicializar la base de datos
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla Categories
        db.execSQL("CREATE TABLE IF NOT EXISTS Categories (" +
                "Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ImageCode TEXT, " +
                "Name TEXT)");

        // Crear tabla SubCategories con clave foránea hacia Categories
        db.execSQL("CREATE TABLE IF NOT EXISTS SubCategories (" +
                "Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Name TEXT, " +
                "CategoryId INTEGER, " +
                "ImageCode TEXT, " +
                "VideoURL TEXT, " +
                "FOREIGN KEY (CategoryId) REFERENCES Categories(Id) ON DELETE CASCADE)");
        // Crear tabla Users
        String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + "("
                + KEY_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_USERNAME + " TEXT UNIQUE, "
                + KEY_NAME + " TEXT, "
                + KEY_LAST_NAME + " TEXT, "
                + KEY_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // Insertar datos predeterminados
        insertDefaultCategories(db);
        insertDefaultSubCategories(db);
        insertDefaultUsers(db);
    }
    private void insertDefaultUsers(SQLiteDatabase db) {
        insertUser(db, "juan.perez", "Juan", "Pérez", "123");
        insertUser(db, "maria.gon", "Maria", "González", "456");
        insertUser(db, "carlos_lopez", "Carlos", "López", "789");
    }

    private void insertUser(SQLiteDatabase db, String username, String name, String lastName, String password) {
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, username);
        values.put(KEY_NAME, name);
        values.put(KEY_LAST_NAME, lastName);
        values.put(KEY_PASSWORD, password);
        db.insert(TABLE_USERS, null, values);
    }

    // Método para verificar las credenciales del usuario
    public Cursor getUserDetails(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS, new String[]{KEY_NAME, KEY_LAST_NAME}, KEY_USERNAME + "=?", new String[]{username}, null, null, null);
    }

    public boolean checkUserCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {KEY_USER_ID};
        String selection = KEY_USERNAME + "=? AND " + KEY_PASSWORD + "=?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_USER_ID}, KEY_USERNAME + "=? AND " + KEY_PASSWORD + "=?", new String[]{username, password}, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;  // Retorna true si hay al menos un registro que coincide, false de lo contrario.

    }
    // Método onUpgrade para manejar las actualizaciones de la base de datos
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Si se detecta un cambio en la versión de la base de datos, eliminar las tablas existentes
        db.execSQL("DROP TABLE IF EXISTS TABLE_USERS");
        db.execSQL("DROP TABLE IF EXISTS Categories");
        db.execSQL("DROP TABLE IF EXISTS SubCategories");
        // Volver a crear las tablas con los datos actualizados
        onCreate(db);
    }

    // Habilitar claves foráneas cuando se abre la base de datos
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    // Inserta categorías predeterminadas
    private void insertDefaultCategories(SQLiteDatabase db) {
        insertCategory(db, "img_dulces", "Dulces");
        insertCategory(db, "img_salados", "Salados");
        insertCategory(db, "img_frutas", "Frutas");
    }

    // Inserta subcategorías predeterminadas con nombres de imagen y URLs de video
    private void insertDefaultSubCategories(SQLiteDatabase db) {
        insertSubCategory(db, "Pizza", 2, "img_pizza", "https://www.youtube.com/watch?v=81KSrIJ2NKs");
        insertSubCategory(db, "Hamburguesa", 2, "img_hamburguesa", "https://www.youtube.com/watch?v=Ylg2iS3YROs");
        insertSubCategory(db, "Empanada", 2, "img_empanada", "https://www.youtube.com/watch?v=LmD47r-NH-4");
        insertSubCategory(db, "Galletas de Arroz", 1, "img_galletas", "https://www.youtube.com/watch?v=Op8HPLsgoF4");
        insertSubCategory(db, "Torta de chocolate", 1, "img_torta", "https://www.youtube.com/watch?v=yV7GRlQy-Kw");
        insertSubCategory(db, "Cupcake", 1,  "img_cupcake", "https://www.youtube.com/watch?v=5y5AQvmFrsg");

        insertSubCategory(db, "Trago", 3,  "img_tragos", "https://www.youtube.com/watch?v=8XxD3Wuyvqs");
        insertSubCategory(db, "Ensalada", 3,  "img_ensalada", "https://www.youtube.com/watch?v=5o4FkEMochQ");
        insertSubCategory(db, "Jugo", 3,  "img_jugo", "https://www.youtube.com/watch?v=qPDwGpmJSuE");

    }

    // Método genérico para insertar una subcategoría
    private void insertSubCategory(SQLiteDatabase db, String name, int categoryId,  String imageCode, String videoURL) {
        ContentValues values = new ContentValues();
        values.put("Name", name);
        values.put("CategoryId", categoryId);
        values.put("ImageCode", imageCode);  // ImageCode ya no lleva .jpg o .png
        values.put("VideoURL", videoURL);
        db.insert("SubCategories", null, values);
    }

    // Método genérico para insertar una categoría
    private void insertCategory(SQLiteDatabase db, String imageCode,String name) {
        ContentValues values = new ContentValues();
        values.put("Name", name);
        values.put("ImageCode", imageCode);
        db.insert("Categories", null, values);
    }

    // Método para obtener todas las categorías
    public Cursor getAllCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Categories", null);
    }

    // Método para obtener subcategorías según el ID de categoría
    public Cursor getSubCategoriesByCategoryId(int categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM SubCategories WHERE CategoryId = ?", new String[]{String.valueOf(categoryId)});
    }

    // Método para obtener el nombre de una subcategoría por su ID
    public String getSubCategoryNameById(int subCategoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"Name"};
        String selection = "Id = ?";
        String[] selectionArgs = {String.valueOf(subCategoryId)};
        Cursor cursor = db.query("SubCategories", columns, selection, selectionArgs, null, null, null);
        String name = null;

        // Si se encuentra el resultado, obtener el nombre
        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
        }
        cursor.close();
        return name;
    }

    // Método para obtener el nombre de la categoría por su ID
    public String getCategoryNameById(int categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"Name"};
        String selection = "Id = ?";
        String[] selectionArgs = {String.valueOf(categoryId)};
        Cursor cursor = db.query("Categories", columns, selection, selectionArgs, null, null, null);
        String name = null;

        // Si se encuentra el resultado, obtener el nombre
        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
        }
        cursor.close();
        return name;
    }
    // Método para obtener el código de la imagen de la categoría por su ID
    public String getCategoryImageCodeById(int categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"ImageCode"};
        String selection = "Id = ?";
        String[] selectionArgs = {String.valueOf(categoryId)};
        Cursor cursor = db.query("Categories", columns, selection, selectionArgs, null, null, null);
        String imageCode = null;

        // Si se encuentra el resultado, obtener el código de la imagen
        if (cursor.moveToFirst()) {
            imageCode = cursor.getString(cursor.getColumnIndexOrThrow("ImageCode"));
        }
        cursor.close();
        return imageCode;
    }


}
