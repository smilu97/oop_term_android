package kr.ac.hanyang.term;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import kr.ac.hanyang.adapter.PhoneNumberArrayAdapter;
import kr.ac.hanyang.realm.Contact;
import kr.ac.hanyang.realm.PhoneNumber;

public class DetailContact extends AppCompatActivity {

    private TextView nameText;
    private ListView phoneNumberList;
    private EditText phoneNumberEdit;
    private Button addPhoneNumberBtn;

    private PhoneNumberArrayAdapter phoneNumberAdapter;
    private List<PhoneNumber> phoneNumbers;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);

        Intent intent = getIntent();
        int contactId = intent.getIntExtra("id", -1);

        contact = Realm.getDefaultInstance().where(Contact.class).equalTo("id", contactId).findFirst();

        phoneNumbers = new ArrayList<PhoneNumber>();
        for(PhoneNumber number: contact.getPhoneNumbers()) {
            phoneNumbers.add(number);
        }

        nameText = (TextView)findViewById(R.id.name_text);
        phoneNumberList = (ListView)findViewById(R.id.phone_number_list);
        phoneNumberEdit = (EditText)findViewById(R.id.phone_number_edit);
        addPhoneNumberBtn = (Button)findViewById(R.id.phone_number_add_btn);

        nameText.setText(contact.getName());

        addPhoneNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                PhoneNumber number = realm.createObject(PhoneNumber.class);
                number.setPhoneNumber(phoneNumberEdit.getText().toString());
                phoneNumberEdit.setText("");
                contact.getPhoneNumbers().add(number);
                realm.commitTransaction();
                phoneNumbers.add(number);
                phoneNumberAdapter.notifyDataSetChanged();
            }
        });

        phoneNumbers = new ArrayList<PhoneNumber>();
        for(PhoneNumber p: contact.getPhoneNumbers()) {
            phoneNumbers.add(p);
        }
        phoneNumberAdapter = new PhoneNumberArrayAdapter(phoneNumbers);
        phoneNumberList.setAdapter(phoneNumberAdapter);
    }
}
