package com.testtask;

import java.io.IOException;
import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;

/**
 * В приложении SQLite  Database c двумя таблицами -
user(uid integer, nick varchar(20)), message(uid integer, msg
varchar(255), tm DateTime, a integer, b integer,c  integer,d  integer,
e varchar(25), dd varchar(25), f varchar(25), g integer).
БД предзаполнена несколькими пользователями, для каждого из них - по
нескольку сообщений.
В приложении 2 Activity.
Первая показывает списком пользователей (отображаются ники, алфавитная
сортировка). При нажатии на пользователя открывается вторая Activity,
которая показывает список сообщений выбранного пользователя. Рядом с
каждым сообщением показываем время сообщения и значения полей a,b...g.
Сортировка сообщений - по их времени.
Работать с БД должна только первое активити. Вторая должна получить
данные от первой, а не из БД напрямую.
 * @author Вылгин Виталий
 *
 */
public class TestTaskActivity extends Activity {
	DataBaseHelper myDbHelper = new DataBaseHelper(this);
	    	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
                
        try {
        	myDbHelper.createDataBase();
        } catch (IOException ioe) {
        	throw new Error("Unable to create database");
        }
        
        try {
        	myDbHelper.openDataBase();
        } catch (SQLException sqle){
        	throw sqle;
        }
        
        ListView listView = (ListView) findViewById(R.id.listNick);        	
    	listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, myDbHelper.getUser()));
    	    	
        //Обрабатываем клик по нику
    	listView.setOnItemClickListener(new OnItemClickListener() {
    		public void onItemClick(AdapterView<?> a, View v, int position, long id) {
	            int pos = new Integer(position);
	            int uid;
	            String[] users = myDbHelper.getUser();
				String nick = null;
				
	            for (int i = 0; i < users.length; i++) {
	            	if (i == pos) nick = users[i];
	            }
	            
	            uid = myDbHelper.getUid(nick);
	            Intent intent = new Intent();
	            intent.setClass(TestTaskActivity.this, MessageActivity.class);
	            Bundle b = myDbHelper.getMessage(uid);
	            intent.putExtras(b);
	            startActivity(intent);
            }
        });
      }
      
}