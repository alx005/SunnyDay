package com.google.sunnyday.view.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.cursoradapter.widget.CursorAdapter;

import com.google.sunnyday.R;

public class SearchCursorAdapter extends CursorAdapter {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private SearchView searchView;

    public SearchCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public SearchCursorAdapter(Context context, Cursor cursor, SearchView sv) {
        super(context, cursor, false);
        mContext = context;
        searchView = sv;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = mLayoutInflater.inflate(R.layout.cell_layout, parent, false);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String cityname = cursor.getString(cursor.getColumnIndexOrThrow("cityname"));

        TextView tv = (TextView) view.findViewById(R.id.cityname);
        tv.setText(cityname);

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //take next action based user selected item
                TextView cityname = (TextView) view.findViewById(R.id.cityname);
                searchView.setIconified(true);
                Toast.makeText(context, "Selected suggestion "+cityname.getText(),
                        Toast.LENGTH_LONG).show();

            }
        });

    }
}
