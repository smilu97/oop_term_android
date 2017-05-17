package kr.ac.hanyang.adapter;

import android.content.Context;
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
import kr.ac.hanyang.realm.SMS;
import kr.ac.hanyang.term.R;

/**
 * Created by gim-yeongjin on 2017. 5. 17..
 */

public class SMSListAdapter extends BaseAdapter {
    List<SMS> data;
    RealmResults<SMS> results;
    LayoutInflater inflater;

    private static class ViewHolder {
        TextView text;
    }

    public SMSListAdapter (Context context) {
        inflater = LayoutInflater.from(context);
        Realm realm = Realm.getDefaultInstance();
        data = new ArrayList<>();
        results = realm.where(SMS.class).findAllSorted("date");
        for(SMS c: results) {
            data.add(c);
        }
        results.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<SMS>>() {
            @Override
            public void onChange(RealmResults<SMS> collection, OrderedCollectionChangeSet changeSet) {
                data.clear();
                for(SMS c: results) {
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
    public SMS getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SMS sms = getItem(position);
        SMSListAdapter.ViewHolder viewHolder;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.simple_text_row, parent, false);
            viewHolder = new SMSListAdapter.ViewHolder();
            viewHolder.text = (TextView)convertView.findViewById(R.id.simple_row_text);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (SMSListAdapter.ViewHolder)convertView.getTag();
        }

        viewHolder.text.setText(sms.description());

        return convertView;
    }
}
