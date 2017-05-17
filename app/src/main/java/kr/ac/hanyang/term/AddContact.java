package kr.ac.hanyang.term;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import kr.ac.hanyang.adapter.PhoneNumberArrayAdapter;
import kr.ac.hanyang.realm.Contact;
import kr.ac.hanyang.realm.PhoneNumber;

public class AddContact extends AppCompatActivity {

    private EditText nameEdit;
    private ListView phoneNumberList;
    private EditText phoneNumberEdit;
    private Button addPhoneNumberBtn;
    private Button commitButton;

    private PhoneNumberArrayAdapter phoneNumberAdapter;
    private List<PhoneNumber> phoneNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        nameEdit = (EditText)findViewById(R.id.name_edit);
        phoneNumberEdit = (EditText)findViewById(R.id.phone_number_edit);
        phoneNumberList = (ListView)findViewById(R.id.phone_number_list);
        addPhoneNumberBtn = (Button)findViewById(R.id.add_phone_number_btn);
        commitButton = (Button)findViewById(R.id.commit_contact_btn);

        addPhoneNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneNumber newNumber = new PhoneNumber();
                newNumber.setPhoneNumber(phoneNumberEdit.getText().toString());
                phoneNumberEdit.setText("");
                phoneNumbers.add(newNumber);
                phoneNumberAdapter.notifyDataSetChanged();
            }
        });

        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean trans = true;
                String name = nameEdit.getText().toString();
                if(name.length() == 0) {
                    Toast.makeText(AddContact.this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                    trans = false;
                }
                if(phoneNumbers.size() == 0) {
                    Toast.makeText(AddContact.this, "최소 한 개 이상의 전화번호가 있어야 합니다", Toast.LENGTH_SHORT).show();
                    trans = false;
                }
                if (trans) {
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction(); // begin transaction
                    Number id = realm.where(Contact.class).max("id");
                    if(id == null) id = 0;
                    Contact newContact = realm.createObject(Contact.class, id.intValue()+1);
                    newContact.setName(nameEdit.getText().toString());
                    for(PhoneNumber phoneNumber: phoneNumbers) {
                        realm.copyToRealm(phoneNumber);
                        newContact.getPhoneNumbers().add(phoneNumber);
                    }
                    realm.commitTransaction(); // end transaction
                    setResult(1, new Intent());
                    finish();
                }
            }
        });

        phoneNumbers = new ArrayList<>();
        phoneNumberAdapter = new PhoneNumberArrayAdapter(phoneNumbers);
        phoneNumberList.setAdapter(phoneNumberAdapter);
    }
}
