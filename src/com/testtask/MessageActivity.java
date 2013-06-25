package com.testtask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Класс второго активити, отображает содержимое, переданное из первого активити
 * @author Вылгин Виталий
 *
 */
public class MessageActivity extends Activity {
	/**
	 * Создание второго активити
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);
        TextView textView = (TextView) findViewById(R.id.textView1);
        Bundle bundle = getIntent().getExtras();
        String[] msg = bundle.getStringArray("msg");
        String[] tm = bundle.getStringArray("tm");
        int[] a = bundle.getIntArray("a");
        int[] b = bundle.getIntArray("b");
        int[] c = bundle.getIntArray("c");
        int[] d = bundle.getIntArray("d");
        String[] e = bundle.getStringArray("e");
        String[] dd = bundle.getStringArray("dd");
        String[] f = bundle.getStringArray("f");
        int[] g = bundle.getIntArray("g");
        String str = "";
        
        for (int i = 0; i < msg.length; i++) {
        	 str += "msg = " + msg[i] + "\n" 
        			+ "tm = " + tm[i] + "\n"
        			+ "a = " + a[i] + "\n"
        			+ "b = " + b[i] + "\n"
        			+ "c = " + c[i] + "\n"
        	 		+ "d = " + d[i] + "\n"
        	 		+ "e = " + e[i] + "\n"
        	 		+ "dd = " + dd[i] + "\n"
        	 		+ "f = " + f[i] + "\n"
        	 		+ "g = " + g[i] + "\n\n";
		}
        
        textView.setText(str);
    }
    
    /**
     * Обработка нажатия кнопки "Назад"
     * @param v - View
     */
    public void clickBack(View v) {
        Intent intent = new Intent();
        intent.setClass(MessageActivity.this, TestTaskActivity.class);
        startActivity(intent);
    }

}
