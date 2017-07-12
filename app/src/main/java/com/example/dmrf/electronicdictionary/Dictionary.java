package com.example.dmrf.electronicdictionary;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Dictionary extends Activity implements View.OnClickListener, TextWatcher {

    //d定义数据库的存储路径
    private final String DATABASE_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/dictionary";
    //用户输入文本框
    private AutoCompleteTextView word;
    //定义数据库的名字
    private final String DAYABASEFILE_NAME = "dictionary.db";
    private SQLiteDatabase database;
    //搜索按钮
    private Button searchword;
    //用户查询结果显示
    private TextView showResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        searchword = findViewById(R.id.searchWord);
        showResult = findViewById(R.id.result);
        //打开数据库
        try {
            database = openDatabase();
            String databaseFilename = DATABASE_PATH + "/" + DAYABASEFILE_NAME;
            Log.i("information", String.valueOf(new File(databaseFilename).exists()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        word = findViewById(R.id.word);
        //绑定监听器
        searchword.setOnClickListener(this);
        word.addTextChangedListener(this);


    }

    private SQLiteDatabase openDatabase() throws IOException {
        //获得dictionary.db的绝对路径
        String databaseFilename = DATABASE_PATH + "/" + DAYABASEFILE_NAME;
        File dir = new File(DATABASE_PATH);
        //如果/sdcard/dictionary这个路径不存在
        if (!dir.exists()) {
            //就创建该目录

            dir.mkdir();
        }
        /**
         * 如果在/sdcard/dictionary目录中不存在dictionar.db的文件，则从res/raw下复制dictionary.db到该路径下：
         */
        if (!(new File(databaseFilename)).exists()) {
            //获得封装dictionary.db文件的InputStream文件
            InputStream is = getResources().openRawResource(R.raw.dictionary);
            FileOutputStream fos = new FileOutputStream(databaseFilename);
            byte[] buffer = new byte[8192];
            int count = 0;

            while ((count = is.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
            //关闭文件流
            fos.close();
            is.close();

        }
        //打开/sdcard.dictionary下的dictionary.db文件
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);
        return database;
    }


    @Override
    public void onClick(View view) {
        //查询指定单词
        String sql = "select chinese from t_words where english=?";
        Cursor cursor = database.rawQuery(sql, new String[]{word.getText().toString()});
        String result = "未找到该单词.";
        //如果找到该单词，就显示其中文意思
        if (cursor.getCount() > 0) {
            //使用moveToFirst方法把指针移动到第一条匹配结果
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex("chinese")).replace("&amp", "&");
        }
        //将结果显示到TextView
        showResult.setText(word.getText() + "\n" + result.toString());
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        //必须将english的别名设置为_id
        // Cursor cursor = database.rawQuery("select english as _id from t_words where english like ?", new String[]{editable.toString() + "%"});
        Cursor cursor = database.rawQuery(
                "select english as _id from t_words where english like ?",
                new String[]{s.toString() + "%"});
        //新建Adapter
        DicnionaryAdapter adapter = new DicnionaryAdapter(this, cursor, true);
        //绑定适配器
        word.setAdapter(adapter);
    }
}
