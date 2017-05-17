package kr.ac.hanyang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import io.realm.Realm;
import kr.ac.hanyang.realm.PhoneNumber;
import kr.ac.hanyang.term.R;

/**
 * Created by gim-yeongjin on 2017. 5. 17..
 */

public class PhoneNumberArrayAdapter extends BaseAdapter {

    private static class ViewHolder {
        TextView phoneNumberText;
        Button deleteBtn;
    }

    public List<PhoneNumber> numbers;

    public PhoneNumberArrayAdapter(List<PhoneNumber> numbers) {
        this.numbers = numbers;
    }

    @Override
    public int getCount() {
        return numbers.size();
    }

    @Override
    public PhoneNumber getItem(int position) {
        return numbers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final PhoneNumber phoneNumber = getItem(position);
        ViewHolder viewHolder;
        final Context context = parent.getContext();
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.phone_number_row, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.phoneNumberText = (TextView)convertView.findViewById(R.id.phone_number_row_text);
            viewHolder.deleteBtn = (Button)convertView.findViewById(R.id.phone_number_row_delete_btn);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.phoneNumberText.setText(phoneNumber.getPhoneNumber());

        viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                if(phoneNumber.isManaged()) phoneNumber.deleteFromRealm();
                numbers.remove(position);
                realm.commitTransaction();
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
