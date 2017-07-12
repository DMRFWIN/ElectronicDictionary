package com.example.dmrf.electronicdictionary;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by DMRF on 2017/7/10.
 */

//自定义Adapter类
public class DicnionaryAdapter extends CursorAdapter {

    public DicnionaryAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private LayoutInflater layoutInflater;

    public CharSequence convertToString(Cursor cursor) {
        return cursor == null ? "" : cursor.getString(cursor.getColumnIndex("_id"));
    }

    //生成新的选项
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = layoutInflater.inflate(R.layout.word_list_item, null);
        setView(view, cursor);
        return view;
    }

    //将单词显示到列表中
    public void setView(View view, Cursor cursor) {
        TextView tvWordItem = (TextView) view;
        tvWordItem.setText(cursor.getString(cursor.getColumnIndex("_id")));
    }

    //绑定选项到列表中
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        setView(view, cursor);
    }

}

