package sinplo.nfcapp.util;

import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.NfcA;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Sintija on 1/7/2018.
 */

public class ReadNFCTagUtil {

    public static ArrayList<Byte> readNfcATag(Tag tag) {
        ArrayList<Byte> value = null;
        NfcA nfcATag = NfcA.get(tag);

        try {
            nfcATag.connect();

            if (nfcATag != null) {

                int x = 0;
                value = new ArrayList<>();
                byte[] command = new byte[]{0x30 , (byte) x};
                while (nfcATag.transceive(command) != null) {
                    byte[] payload = nfcATag.transceive(command);
                    int index = 0;
                    for (byte b : payload) {
                        value.add(b);
                        index++;
                    }
                    x += 4;
                    //TODO change this to check whether the first 8 elements in list are the same as equal to byte sequence
                    if(x == 16) break;
                }

            }

        } catch (IOException e) {
            Log.e(ReadNFCTagUtil.class.getSimpleName(), "09 " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (nfcATag != null) {
                try {
                    nfcATag.close();
                } catch (IOException ec) {

                }
            }
        }
        return value;
    }

    public static ArrayList<Byte> readMifareUltralightTag(Tag tag) {
        ArrayList<Byte> value = null;
        MifareUltralight mifare = MifareUltralight.get(tag);
        if (mifare != null) {
            try {
                mifare.connect();
                int x = 0;
                value = new ArrayList<>();
                while (mifare.readPages(x) != null) {
                    byte[] payload = mifare.readPages(x);
                    int index = 0;
                    for (byte b : payload) {
                        String m = String.format("x%02X", b);
                        value.add(b);
                        index++;
                    }
                    x += 4;
                }
            } catch (IOException e) {
                Log.e(ReadNFCTagUtil.class.getSimpleName(), "0 " + e.getMessage());
            } finally {
                if (mifare != null) {
                    try {
                        mifare.close();
                    } catch (IOException e) {
                        Log.e(ReadNFCTagUtil.class.getSimpleName(), "1 " + e.getMessage());

                    }
                }
            }

        }

        return value;
    }

    public static void setTextView(ArrayList<Byte> bytelist, TextView view) {

        int tempIndex = 0;
        if (bytelist == null) {
            view.setText("Tag is empty or something wrong happened");
        } else {
            view.setText("");
            for (int index = 0; index < bytelist.size(); index++) {
                byte currentByte = bytelist.get(index);
                String formatedString = String.format("x%02X", currentByte);

                view.append(formatedString);
                tempIndex++;
                if (tempIndex == 4) {
                    view.append("\n");
                    tempIndex = 0;
                }

            }
        }

    }
}
