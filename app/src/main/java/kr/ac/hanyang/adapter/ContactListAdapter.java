package kr.ac.hanyang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import kr.ac.hanyang.realm.Contact;

/**
 * Created by gim-yeongjin on 2017. 5. 17..
 */

public class ContactListAdapter extends BaseAdapter {

    private RealmResults<Contact> contacts;

    private LayoutInflater inflater;
    private Context mContext;
    private RecyclerView.ViewHolder viewHolder = null;

    public ContactListAdapter(Context c) {
        this.mContext = c;
        this.inflater = LayoutInflater.from(c);
        this.contacts = Realm.getDefaultInstance().where(Contact.class).findAll();
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Contact getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.contact_list_row); 
        }
        return null;
    }
}
