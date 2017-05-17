package kr.ac.hanyang.term;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import kr.ac.hanyang.adapter.ContactRealmAdapter;
import kr.ac.hanyang.realm.Call;
import kr.ac.hanyang.realm.Contact;
import kr.ac.hanyang.realm.SMS;

public class MainActivity extends AppCompatActivity {
    public static String myNumber = "01020666927";

    private ListView contactList;
    private ContactRealmAdapter contactAdapter;
    private Button addContactBtn;
    private EditText searchTextEdit;

    private EditText callNumberEdit;
    private Button callSendBtn;
    private Button callReceiveBtn;

    private EditText smsNumberEdit;
    private EditText smsContentEdit;
    private Button smsSendBtn;
    private Button smsReceiveBtn;

    TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabHost = (TabHost)findViewById(R.id.tab_host);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("contactTab");
        tabSpec.setContent(R.id.tab1);
        tabSpec.setIndicator("CONTACT");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("callTab");
        tabSpec.setContent(R.id.tab2);
        tabSpec.setIndicator("CALL");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("smsTab");
        tabSpec.setContent(R.id.tab3);
        tabSpec.setIndicator("SMS");
        tabHost.addTab(tabSpec);

        Log.v("mytag", "Start!");

        Realm.init(getApplicationContext());
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("DEVELOP_TEST")
                .build();

        contactList = (ListView)findViewById(R.id.contact_list);
        addContactBtn = (Button) findViewById((R.id.addContact));
        searchTextEdit = (EditText)findViewById(R.id.search_text_edit);

        callNumberEdit = (EditText)findViewById(R.id.call_number_edit);
        callSendBtn = (Button)findViewById(R.id.call_send_btn);
        callReceiveBtn = (Button)findViewById(R.id.call_receive_btn);

        smsNumberEdit = (EditText)findViewById(R.id.sms_number_edit);
        smsContentEdit = (EditText)findViewById(R.id.sms_content_edit);
        smsSendBtn = (Button)findViewById(R.id.sms_send_btn);
        smsReceiveBtn = (Button)findViewById(R.id.sms_receive_btn);

        ////////////////////////////////////////////////////////////
        // Contact
        ////////////////////////////////////////////////////////////

        addContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, AddContact.class), 0);
            }
        });

        contactAdapter = new ContactRealmAdapter(getApplicationContext(), new ContactRealmAdapter.Callback() {
            @Override
            public void onClick(Contact contact) {
                Intent intent = new Intent(MainActivity.this, DetailContact.class);
                intent.putExtra("id", contact.getId());
                startActivityForResult(intent, contact.getId());
            }
        });
        contactList.setAdapter(contactAdapter);

        searchTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText = searchTextEdit.getText().toString();
                searchText = searchText.replace(" ", "");
                contactAdapter.setSearchText(searchText);
                Log.v("mytag", "set searchtext");
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ////////////////////////////////////////////////////////////
        // Call
        ////////////////////////////////////////////////////////////

        callSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = callNumberEdit.getText().toString();
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                Call call = realm.createObject(Call.class);
                call.setFrom(myNumber);
                call.setTo(number);
                call.setDate(new Date());
                realm.commitTransaction();
            }
        });
        callReceiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = callNumberEdit.getText().toString();
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                Call call = realm.createObject(Call.class);
                call.setFrom(number);
                call.setTo(myNumber);
                call.setDate(new Date());
                realm.commitTransaction();
            }
        });

        ////////////////////////////////////////////////////////////
        // SMS
        ////////////////////////////////////////////////////////////

        smsSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = smsNumberEdit.getText().toString();
                String content = smsContentEdit.getText().toString();
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                SMS sms = realm.createObject(SMS.class);
                sms.setFrom(myNumber);
                sms.setTo(number);
                sms.setContent(content);
                sms.setDate(new Date());
                realm.commitTransaction();
            }
        });
        smsReceiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = smsNumberEdit.getText().toString();
                String content = smsContentEdit.getText().toString();
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                SMS sms = realm.createObject(SMS.class);
                sms.setFrom(number);
                sms.setTo(myNumber);
                sms.setContent(content);
                sms.setDate(new Date());
                realm.commitTransaction();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        contactAdapter.notifyDataSetChanged();
        Log.v("mytag", "contact create update");
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.activity_main_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch(item.getItemId()) {
//            case R.id.main_menu_search:
//                return true;
//            case R.id.main_menu_setting:
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}
