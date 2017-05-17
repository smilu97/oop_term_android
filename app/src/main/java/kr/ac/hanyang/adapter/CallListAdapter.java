package kr.ac.hanyang.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;
import kr.ac.hanyang.realm.Call;
import kr.ac.hanyang.term.R;

/**
 * Created by gim-yeongjin on 2017. 5. 17..
 */

public class CallListAdapter extends BaseAdapter {

    List<Call> data;
    RealmResults<Call> results;
    LayoutInflater inflater;

    private static class ViewHolder {
        TextView text;
    }

    public CallListAdapter (Context context) {
        inflater = LayoutInflater.from(context);
        Realm realm = Realm.getDefaultInstance();
        data = new ArrayList<>();
        results = realm.where(Call.class).findAllSorted("date");
        for(Call c: results) {
            data.add(c);
        }
        results.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<Call>>() {
            @Override
            public void onChange(RealmResults<Call> collection, OrderedCollectionChangeSet changeSet) {
                data.clear();
                for(Call c: results) {
                    data.add(c);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Call getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Call call = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.simple_text_row, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView)convertView.findViewById(R.id.simple_row_text);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.text.setText(call.description());

        return convertView;
    }
}
