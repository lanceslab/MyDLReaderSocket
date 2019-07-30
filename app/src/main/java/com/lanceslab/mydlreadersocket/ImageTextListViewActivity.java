package com.lanceslab.mydlreadersocket;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
//import com.theopentutorials.android.R;
import com.lanceslab.mydlreadersocket.CustomListViewAdapter;
import com.lanceslab.mydlreadersocket.ImageAdapter;
import com.lanceslab.mydlreadersocket.RowItem;
/**
 * Created by LanceTaylor on 4/10/2017.
 */

public class ImageTextListViewActivity extends Activity  implements
        OnItemClickListener {


    public static final String[] titles = new String[] { "Strawberry",
            "Banana", "Orange", "Mixed" };

    public static final String[] descriptions = new String[] {
            "It is an aggregate accessory fruit",
            "It is the largest herbaceous flowering plant", "Citrus Fruit",
            "Mixed Fruits" };

    public static final Integer[] images = { R.drawable.lanceheadshot,
            R.drawable.lanceheadshot, R.drawable.lanceheadshot, R.drawable.lanceheadshot };

    ListView listView;
    List<RowItem> rowItems;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        rowItems = new ArrayList<RowItem>();
        for (int i = 0; i < titles.length; i++) {
            RowItem item = new RowItem(images[i], titles[i], descriptions[i]);
            rowItems.add(item);
        }

        listView = (ListView) findViewById(R.id.list);

        ImageAdapter adapter = new ImageAdapter(this);


//        CustomListViewAdapter adapter = new CustomListViewAdapter(this,
//                R.layout.list_item, rowItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Item " + (position + 1) + ": " + rowItems.get(position),
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }


}
