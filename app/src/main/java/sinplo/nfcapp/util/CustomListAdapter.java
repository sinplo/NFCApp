package sinplo.nfcapp.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sinplo.nfcapp.R;

/**
 * Created by Sintija on 2/2/2018.
 */

public class CustomListAdapter extends ArrayAdapter<String> {
    private ArrayList<String> mpageContentList;
    private Context mContext;


    public CustomListAdapter(@NonNull Context context, @NonNull List<String> objects) {
        super(context, 0, objects);
        mContext = context;
        mpageContentList = (ArrayList<String>) objects;
    }




    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(mContext).inflate(R.layout.list_item_page, parent, false);
        }

        TextView pageNumber = (TextView) listItemView.findViewById(R.id.txt_page);
        pageNumber.setText("Page " + position +":");

        EditText hexCodeText = (EditText) listItemView.findViewById(R.id.edit_hex_code);
        hexCodeText.setText(mpageContentList.get(position));

        return listItemView;
    }

}
