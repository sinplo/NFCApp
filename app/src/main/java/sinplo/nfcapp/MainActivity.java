package sinplo.nfcapp;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import sinplo.nfcapp.util.ReadNFCTagUtil;

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
        if (savedInstanceState != null) {
            //  Log.e("dkjfkdfjdkf", savedInstanceState.getString(DATA));
            text.setText(savedInstanceState.getString(DATA));
        }

        //forcing mainActivity to be on top
        //  getIntent().setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP), 0);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {

        }
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mFilters = new IntentFilter[]{ndef};
        mtechList = new String[][]{
                new String[]{NfcA.class.getName()},
                new String[]{Ndef.class.getName()},
                new String[]{MifareUltralight.class.getName()},
                new String[]{NdefFormatable.class.getName()},
                new String[]{NfcB.class.getName()}

        };


        Intent i = getIntent();
        if (i != null) {
            if (i.getAction() == NfcAdapter.ACTION_TECH_DISCOVERED) {
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

    private void readTheIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        String[] techList = tag.getTechList();

        Boolean containsMifareUltralightTech = Arrays.toString(techList).contains(MifareUltralight.class.getName());
        Boolean containsNfcA = Arrays.toString(techList).contains(NfcA.class.getName());
        if (containsMifareUltralightTech) {
            MifareUltralight mifare = MifareUltralight.get(tag);
            if (mifare != null) {
                ArrayList<Byte> result = ReadNFCTagUtil.readMifareUltralightTag(tag);
                ReadNFCTagUtil.setTextView(result, text);
            }
        } else if (containsNfcA) {
            ArrayList<Byte> result = ReadNFCTagUtil.readNfcATag(tag);
            ReadNFCTagUtil.setTextView(result, text);
        }

    }


    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
        readTheIntent(intent);

    }

    //TODO transceive raw data edited data to read tag
    //possible if it wasn't succesfull return something boolean or something else

    public void transceiveRawHexToTag() {

    }


    //menu stuff
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_upload) {
            transceiveRawHexToTag();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
