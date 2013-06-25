package com.testtask;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

/**
 * Данный класс создает базу данных в системной папке из базы данных, 
 * расположенной в папке assets данного проекта.
 * В этом классе описываются методы, необходимые приложению, для взаимодействия с БД
 * @author Вылгин Виталий
 *
 */
public class DataBaseHelper extends SQLiteOpenHelper {
	 
    //Путь к базе данных
    private static String DB_PATH = "/data/data/com.testtask/databases/";
    private static final int DB_VERSION = 1;
    private static String DB_NAME = "database";
    private SQLiteDatabase myDataBase; 
    private final Context myContext;
 
    /**
     * Конструктор класса
     * @param context
     */
    public DataBaseHelper(Context context) {
    	super(context, DB_NAME, null, DB_VERSION);
        this.myContext = context;
    }	
 
    /**
     * Создает пустую базу данных в системе и копирует содержимое из базы в каталоге assets
     * 
     * */
    public void createDataBase() throws IOException {
    	boolean dbExist = checkDataBase();
 
    	if (dbExist) {
    		//База создана, ничего делать не надо
    	} else {
    		//Создается пустая база и в нее копируется содержимое базы из assets
        	this.getReadableDatabase();
 
        	try {
    			copyDataBase();
    		} catch (IOException e) {
        		throw new Error("Error copying database");
        	}
    	}
    }
 
    /**
     * Проверяет существование базы данных в системе, чтобы избежать повторного копирования
     * @return true если база создана, false если база не создана
     */
    private boolean checkDataBase() {
    	SQLiteDatabase checkDB = null;
 
    	try {
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    	} catch (SQLiteException e) {
    		// Базы данных не существует
    	}
 
    	if (checkDB != null) {
    		checkDB.close();
    	}
 
    	return checkDB != null ? true : false;
    }
 
    /**
     * Копирование базы данных из каталога assets в только что созданную базу в
     * системном каталоге, откуда она может быть доступна и обработана.
     *  Это делается за счет байтового потока.
     * */
    private void copyDataBase() throws IOException {
 
    	// Открытие локальной базы данных в качестве входного потока
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
 
    	// Путь к только что созданной БД
    	String outFileName = DB_PATH + DB_NAME;
 
    	// Открыть пустую БД в качестве выходного потока
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	// Передача байтов из входного потока в выходной
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer)) > 0) {
    		myOutput.write(buffer, 0, length);
    	}
 
    	// Закрываем потоки
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
    }
 
    /**
     * Открытие базы данных
     * @throws SQLException
     */
    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }
 
    /**
     * Закрытие базы данных
     */
    @Override
	public synchronized void close() {
    	    if (myDataBase != null)
    		    myDataBase.close();
    	    super.close();
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { 
	}
	
	/**
	 * Вспомогательный метод.
	 * @param query строка sql запроса
	 * @return целое число. Например, колличество сообщений или колличество пользователей.
	 */
	private int getCount(String query) {
        Cursor cursor = myDataBase.rawQuery(query, null);
        cursor.moveToFirst();
        return cursor.getInt(0);
	}
	
	/**
	 * Вспомогательный метод для создания строкового массива из БД.
	 * @param array строковый массив
	 * @param query строка sql-запроса
	 * @return массив типа String
	 */
	private String[] getArrayString(String[] array, String query) {
        Cursor cursor = myDataBase.rawQuery(query, null);
        cursor.moveToFirst();
        for (int i = 0; i < array.length; i++) {
        	array[i] = cursor.getString(0);
        	cursor.moveToNext();
        }
        return array;
	}
	
	/**
	 * Вспомогательный метод для создания целочисленного массива из БД  
	 * @param array массив целых чисел
	 * @param query строка sql-запроса
	 * @return массив целых чисел
	 */
	private int[] getArrayInt(int[] array, String query) {
        Cursor cursor = myDataBase.rawQuery(query, null);
        cursor.moveToFirst();
        for (int i = 0; i < array.length; i++) {
        	array[i] = cursor.getInt(0);
        	cursor.moveToNext();
        }
        return array;
	}
	/**
	 * Метод возвращает колличество ников в таблице user
	 * @return колличество ников
	 */
	public int getCountUser() {
        return getCount("SELECT count(*) FROM user");
	}
	
	/**
	 * Метод для подсчета колличества сообщений по выбранному пользователю
	 * @param uid - идетрификатор пользователя из БД
	 * @return число сообщений для выбранного пользователя
	 */
	public int getCountMessage(int uid) {
        return getCount("SELECT count(*) FROM message WHERE uid=" + uid);
	}
	
	/**
	 * Метод возвращает строковый массив ников
	 * @return строковый массив ников
	 */
	public String[] getUser() {
		//Строковый массив длинна которого равна колличеству пользователей в таблице user
		String[] user = new String[this.getCountUser()];
        return getArrayString(user, "SELECT nick FROM user ORDER BY nick");
	}
	
	/**
	 * Метод выясняет uid пользователя по нику
	 * @param nick ник пользователя
	 * @return uid пользователя
	 */
	public int getUid (String nick) {
		return getCount("SELECT uid FROM user WHERE nick=\'" + nick + "\'");
	}
	
	/**
	 * Вспомогательный метод для создания sql-запроса
	 * @param ch - название атрибута из таблицы БД
	 * @param uid - uid пользователя 
	 * @return сформированный sql запрос
	 */
	private String genQueryMessage(String ch, int uid) {
		return "SELECT " + ch + " FROM message WHERE uid=" + uid + " ORDER BY tm";
	}
	
	/**
	 * Метод получает сообщения и другие параметры из таблицы message БД и помещает в структуру Bundle.
	 * @param uid - uid пользователя
	 * @return возвращяет структуру типа Bundle
	 */
	public Bundle getMessage(int uid) {
		int countmessage = this.getCountMessage(uid);
		String[] msg = new String[countmessage];
		String[] tm = new String[countmessage];
		int[] a = new int[countmessage];
		int[] b = new int[countmessage]; 
		int[] c = new int[countmessage]; 
		int[] d = new int[countmessage];
		String[] e = new String[countmessage]; 
		String[] dd = new String[countmessage]; 
		String[] f = new String[countmessage];
		int [] g = new int[countmessage];
			
        msg = getArrayString(msg, genQueryMessage("msg", uid));
        tm = getArrayString(tm, genQueryMessage("tm", uid));
        a = getArrayInt(a, genQueryMessage("a", uid));
        b = getArrayInt(b, genQueryMessage("b", uid));
        c = getArrayInt(c, genQueryMessage("c", uid));
        d = getArrayInt(d, genQueryMessage("d", uid));
        e = getArrayString(e, genQueryMessage("e", uid));
        dd = getArrayString(dd, genQueryMessage("dd", uid));
        f = getArrayString(f, genQueryMessage("f", uid));
        g = getArrayInt(g, genQueryMessage("g", uid));
        
        
		Bundle bundle = new Bundle();
		bundle.putStringArray("msg", msg);
		bundle.putStringArray("tm", tm);
		bundle.putIntArray("a", a);
		bundle.putIntArray("b", b);
		bundle.putIntArray("c", c);
		bundle.putIntArray("d", d);
		bundle.putStringArray("e", e);
		bundle.putStringArray("dd", dd);
		bundle.putStringArray("f", f);
		bundle.putIntArray("g", g);
                
        return bundle;
	}
}
