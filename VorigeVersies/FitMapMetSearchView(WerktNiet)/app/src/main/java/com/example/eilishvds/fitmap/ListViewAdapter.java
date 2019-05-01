package com.example.eilishvds.fitmap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<resultaten> resultatenList = null;
    private ArrayList<resultaten> arrayList;

    public ListViewAdapter(Context context, List<resultaten> resultatenList){
        mContext = context;
        this.resultatenList = resultatenList;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<resultaten>();
        this.arrayList.addAll(resultatenList);
    }

    public class ViewHolder{
        TextView name;
    }

    @Override
    public int getCount() {
        return resultatenList.size();
    }

    @Override
    public resultaten getItem(int position) {
        return resultatenList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_view_items, null);
            holder.name = (TextView)convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(resultatenList.get(position).getResultaatName());

        return convertView;
    }

    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        resultatenList.clear();

        if (charText.length() == 0){
            resultatenList.addAll(arrayList);
        } else {
            for (resultaten wp : arrayList){
                if (wp.getResultaatName().toLowerCase(Locale.getDefault()).contains(charText)){
                    resultatenList.add(wp);
                }
            }
        }

        notifyDataSetChanged();
    }
}
