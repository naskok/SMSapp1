package com.example.konas.smsapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.jar.JarEntry;

public class MainActivity extends AppCompatActivity {

    Button sendBtn,BtnCall,BtnInt,BtnEnv,BtnCtc,BtnCh;
    EditText txtphoneNo;
    EditText txtMessage;
    private static final int RESULT_PICK_CONTACT = 8550;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendBtn=(Button) findViewById(R.id.btnSendSms);
        BtnCall=(Button)findViewById(R.id.Call);
        BtnInt=(Button)findViewById(R.id.Internet);
        BtnCtc= findViewById(R.id.Contacts);
    BtnEnv=(Button)findViewById(R.id.button);
    BtnCh=findViewById(R.id.choise);

        txtphoneNo=(EditText)findViewById(R.id.editTextPhoneNo);
        txtMessage=(EditText)findViewById(R.id.editTextSms);
        BtnCh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
            }
        });

        BtnCtc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String myData = "content://contacts/people/";
                Intent myActivity2 = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(myData));
                startActivity(myActivity2);
            }
        });

        BtnEnv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myActivity3 = new Intent(
                        Intent.ACTION_SENDTO,
                        Uri.parse("smsto:"+txtphoneNo.getText().toString()));
                myActivity3.putExtra("sms_body",
                        txtMessage.getText().toString());
                startActivity(myActivity3);

            }
        });
        BtnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String myData = "tel:"+txtphoneNo.getText().toString();
                    Intent myActivity2 = new Intent(Intent.ACTION_DIAL,
                            Uri.parse(myData));
                    startActivity(myActivity2);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });




        BtnInt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String myUriString = "http://"+txtMessage.getText().toString();;
                Intent myActivity2 = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(myUriString));
                startActivity(myActivity2);
            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSMSmessage();
            }
        });
    }

    protected void sendSMSmessage() {
        String PhoneNo=txtphoneNo.getText().toString();
        String Message=txtMessage.getText().toString();
        try
        {
            SmsManager smsManager= SmsManager.getDefault();
            smsManager.sendTextMessage(PhoneNo,null,Message,null,null);
            Toast.makeText(getApplicationContext(),"sms sent",Toast.LENGTH_SHORT).show();

        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(),"sms NOT sent",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }
    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String phoneNo = null ;
            String name = null;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            // column index of the phone number
            int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            // column index of the contact name
            int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);
            // Set the value to the textviews
            txtMessage.setText(name);
            txtphoneNo.setText(phoneNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    }

