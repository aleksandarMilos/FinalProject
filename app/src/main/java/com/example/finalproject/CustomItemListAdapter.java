package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomItemListAdapter extends BaseAdapter {
    private Context context;
    private List<Item> itemList;
    private LayoutInflater inflater;//Similar to recyclerview, basically a custom recyclerview?
    public CustomItemListAdapter(Context context, List<Item> itemList){
        this.context = context;
        this.itemList = itemList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) { return itemList.get(position); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.itemlist_data, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Item item = itemList.get(i);
        viewHolder.bindData(item);
        return view;
    }

    public void removeList(int index){ //For removing and instantly updating
        itemList.remove(index);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        private TextView label;

        public ViewHolder(View view) { //this ViewHolder is the custom one??
            label = view.findViewById(R.id.itemList);
        }

        void bindData(Item item) {
            label.setText(item.getItem());
        }
    }

}

