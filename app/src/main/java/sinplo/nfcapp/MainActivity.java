package sinplo.nfcapp;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private TextView text;
    private NfcAdapter mNfcAdapter;
    private IntentFilter[] mFilters;
    private String[][] mtechList;
    PendingIntent pendingIntent;
    static final String DATA = "Data";
    private static String tagData = "";

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String textm = savedInstanceState.getString(DATA);
        this.text.setText(textm);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(DATA, tagData);

        super.onSaveInstanceState(outState);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.textViee);
        if(savedInstanceState !=null){
          //  Log.e("dkjfkdfjdkf", savedInstanceState.getString(DATA));
            text.setText(savedInstanceState.getString(DATA));
        }

        //forcing mainActivity to be on top
        getIntent().setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        try{
            ndef.addDataType("*/*");
        }catch (IntentFilter.MalformedMimeTypeException e){

        }
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mFilters = new IntentFilter[]{ndef};
        mtechList = new String[][]{
                new String[] {NfcA.class.getName()},
                new String[] {Ndef.class.getName()},
                new String[] {MifareUltralight.class.getName()},
                new String[] {NdefFormatable.class.getName()},
                new String[] {NfcB.class.getName()}

        };


        Intent i = getIntent();
        if(i != null){
            if(i.getAction() == NfcAdapter.ACTION_TECH_DISCOVERED){
                Log.e("dddd", "afadfadfafadf");
                readTheIntent(i);
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNfcAdapter.enableForegroundDispatch(this, pendingIntent, mFilters, mtechList);
    }

    private void readTheIntent(Intent intent){
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//        text.setText(Arrays.toString(tag.getTechList()));
//        String s = tag.toString();
//        text.setText(s);
        MifareUltralight mifare = MifareUltralight.get(tag);
        if(mifare !=  null) {
            try {
                mifare.connect();
                text.setText("");
                tagData = "";
//            for(int index = 0; index < 16; index ++){
                int x = 0;
                byte[] info = mifare.getTag().getId();

                StringBuilder sb = new StringBuilder();
                for (byte b : info) {
                    sb.append(String.format("%02X", b));
                }
                ;

                String nfcTagSerialNum = sb.toString();
                Log.e("nfc ID", nfcTagSerialNum);

//                String as = new String(info, Charset.forName("UTF-8"));
                Log.e("Mdafadfaf", Arrays.toString(info));

                while (mifare.readPages(x) != null) {
                    byte[] payload = mifare.readPages(x);
//                Log.e("buffrSize" , ""+ payload.length);
                    String z = Arrays.toString(payload);
//                String mu = new String(payload, Charset.forName("x-IBM921"));
                    Log.e("mumumumumum", z);

//                Log.e("MainActivity", z);
//                Log.e("dfdfd", as);
//                text.setText(z);
                    int index = 0;
                    for (byte b : payload) {
                        String m = String.format("x%02X", b);
                        text.append(m);
                        tagData += m;
                        index++;
                        if (index == 4) {
                            text.append("\n");
                            tagData+= "\n";
                            index = 0;
                        }
                    }
                    x += 4;
                }
//             // hex code
//                String mm = "";
//                for(byte b: payload) {
//
//                    String formatedString = String.format("0x%02X", b);
//                    mm = formatedString;
//                    text.append(mm + "\n");
//////                    text.append(formatedString);
////                        mm += formatedString;
//////                    text.append(d);
//                }

//                Log.e("ddddddd", mm);
//            }

            } catch (IOException e) {

            } finally {
                if (mifare != null) {
                    try {
                        mifare.close();
                    } catch (IOException e) {

                    }
                }
            }

        }

        Log.e("aaaaaaaaaaaaaaaaaaaaaaa", tagData);

        Parcelable[] rMessage = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_TAG);
        if (rMessage != null) {
            Log.e("memeeme", "muuuuuuuuu");
            NdefMessage[] messages = new NdefMessage[rMessage.length];
            for (int index = 0; index < rMessage.length; index++) {
                messages[index] = (NdefMessage) rMessage[index];
            }

            String messageString = "";
            for (NdefMessage m : messages) {
                messageString += m.toString() + " ";
            }
            text.setText("muhah");
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        readTheIntent(intent);

//        Log.e("MainActity", "AAAAAAA");
//        if(intent != null) {
//            Log.e("SSSSS", "aaaaaaaaakkkkkkkkkkkkkkkkkk");
//            if ( NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
//                Log.e("MainACtivity", "IN");
//                Parcelable[] rMessage = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
//                if (rMessage != null) {
//                    NdefMessage[] messages = new NdefMessage[rMessage.length];
//                    for (int index = 0; index < rMessage.length; index++) {
//                        messages[index] = (NdefMessage) rMessage[index];
//                    }
//
//                    String messageString = "";
//                    for (NdefMessage m : messages) {
//                        messageString += m.toString() + " ";
//                    }
//                    text.setText(messageString);
//                }
//            }
//        }
    }
}
