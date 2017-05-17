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
import kr.ac.hanyang.adapter.CallListAdapter;
import kr.ac.hanyang.adapter.ContactRealmAdapter;
import kr.ac.hanyang.adapter.SMSListAdapter;
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
    private ListView callList;
    private CallListAdapter callAdapter;

    private EditText smsNumberEdit;
    private EditText smsContentEdit;
    private Button smsSendBtn;
    private Button smsReceiveBtn;
    private ListView smsList;
    private SMSListAdapter smsAdapter;

    TabHost tabHost;

    private void handleContact () {
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
    }

    private void handleCall () {
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
        callAdapter = new CallListAdapter(getApplicationContext());
        callList.setAdapter(callAdapter);
    }

    private void handleSMS () {
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
        smsAdapter = new SMSListAdapter(getApplicationContext());
        smsList.setAdapter(smsAdapter);

    }

    private void findViews () {
        contactList = (ListView)findViewById(R.id.contact_list);
        addContactBtn = (Button) findViewById((R.id.addContact));
        searchTextEdit = (EditText)findViewById(R.id.search_text_edit);

        callNumberEdit = (EditText)findViewById(R.id.call_number_edit);
        callSendBtn = (Button)findViewById(R.id.call_send_btn);
        callReceiveBtn = (Button)findViewById(R.id.call_receive_btn);
        callList = (ListView)findViewById(R.id.call_list);

        smsNumberEdit = (EditText)findViewById(R.id.sms_number_edit);
        smsContentEdit = (EditText)findViewById(R.id.sms_content_edit);
        smsSendBtn = (Button)findViewById(R.id.sms_send_btn);
        smsReceiveBtn = (Button)findViewById(R.id.sms_receive_btn);
        smsList = (ListView)findViewById(R.id.sms_list);
    }

    private void setTabs () {
        // Overall
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

        // SMS
        TabHost smsTab = (TabHost)findViewById(R.id.sms_Tab);
        smsTab.setup();

        tabSpec = smsTab.newTabSpec("smsDoTab");
        tabSpec.setContent(R.id.tab3_1);
        tabSpec.setIndicator("SMS DO");
        smsTab.addTab(tabSpec);

        tabSpec = smsTab.newTabSpec("smsListTab");
        tabSpec.setContent(R.id.tab3_2);
        tabSpec.setIndicator("SMS List");
        smsTab.addTab(tabSpec);

        // Call
        TabHost callTab = (TabHost)findViewById(R.id.call_tab);
        callTab.setup();

        tabSpec = callTab.newTabSpec("callDoTab");
        tabSpec.setContent(R.id.tab2_1);
        tabSpec.setIndicator("CALL DO");
        callTab.addTab(tabSpec);

        tabSpec = callTab.newTabSpec("callListTab");
        tabSpec.setContent(R.id.tab2_2);
        tabSpec.setIndicator("CALL List");
        callTab.addTab(tabSpec);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v("mytag", "Start!");

        Realm.init(getApplicationContext());
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("DEVELOP_TEST")
                .build();

        setTabs();
        findViews();
        handleContact();
        handleCall();
        handleSMS();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        contactAdapter.notifyDataSetChanged();
        Log.v("mytag", "contact create update");
    }
}
