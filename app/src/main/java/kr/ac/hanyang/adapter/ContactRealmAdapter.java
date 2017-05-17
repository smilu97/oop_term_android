package kr.ac.hanyang.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;
import kr.ac.hanyang.realm.Contact;
import kr.ac.hanyang.realm.PhoneNumber;
import kr.ac.hanyang.term.DetailContact;
import kr.ac.hanyang.term.MainActivity;
import kr.ac.hanyang.term.R;

/**
 * Created by gim-yeongjin on 2017. 5. 17..
 */

public class ContactRealmAdapter extends BaseAdapter {

    private static class ViewHolder {
        TextView phoneNumberText;
        Button deleteBtn;
    }

    private RealmResults<Contact> realmContact;
    private List<Contact> contacts;

    private LayoutInflater inflater;

    public interface Callback {
        public void onClick(Contact contact);
    }
    private Callback textClickCallback;

    public ContactRealmAdapter(Context c, Callback textClickCallback) {
        this.inflater = LayoutInflater.from(c);
        this.realmContact = Realm.getDefaultInstance().where(Contact.class).findAllSorted("name");
        this.textClickCallback = textClickCallback;
        contacts = new ArrayList<>();

        for(Contact p: realmContact) {
            contacts.add(p);
        }

        for(Contact a: contacts) {
            Log.v("mytag", "<Contact, name: " + a.getName() + ", id: " + a.getId() + ">");
        }

        this.realmContact.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<Contact>>() {
            @Override
            public void onChange(RealmResults<Contact> collection, OrderedCollectionChangeSet changeSet) {
                contacts = new ArrayList<>();
                for(Contact p: realmContact) {
                    contacts.add(p);
                }
            }
        });
    }

    public void setSearchText(String searchText) {
        Iterator<Contact> it = realmContact.iterator();
        ArrayList<Contact> filtered = new ArrayList<Contact>();
        while(it.hasNext()) {
            Contact c = it.next();
            if (c.getName().contains(searchText)) {
                filtered.add(c);
                continue;
            }
            for(PhoneNumber p: c.getPhoneNumbers()) {
                if (p.getPhoneNumber().contains(searchText)) {
                    filtered.add(c);
                    break;
                }
            }
        }
        contacts = filtered;
        notifyDataSetChanged();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Contact contact = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.contact_list_row, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.phoneNumberText = (TextView)convertView.findViewById(R.id.contact_row_phone_number_text);
            viewHolder.deleteBtn = (Button)convertView.findViewById(R.id.contact_row_delete_btn);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.phoneNumberText.setText(contact.getName());
        viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                if(contact.isManaged()) contact.deleteFromRealm();
                realm.commitTransaction();
                notifyDataSetChanged();
            }
        });
        viewHolder.phoneNumberText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textClickCallback.onClick(contact);
            }
        });

        return convertView;
    }
}
