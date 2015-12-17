package ideo.farmerhelp;

import java.util.List;
import java.util.Random;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;


public class DatabaseActivity extends ListActivity {
    private cropFieldDataSource datasource;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_activity);
        datasource = new cropFieldDataSource(this);
        datasource.open();

        List<CropField> values = datasource.getAllCropfields();

        //Use the SimpleCursorAdapter to show elements in a ListView
        ArrayAdapter<CropField> adapter = new ArrayAdapter<CropField>(this, android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

    // Will be called via the onClick attribute of the buttons in the main XML
    public void AddItems(View view)
    {
        @SuppressWarnings("unchecked")
                ArrayAdapter<CropField> adapter = (ArrayAdapter<CropField>) getListAdapter();
        CropField cropfield = null;
        datasource.open();
        /*String[] cropfields = new String[]{"One", "Two", "Three"};
        int nextInt = new Random().nextInt(3);
        //Save new cropfield to database
        cropfield = datasource.createCropField(cropfields[nextInt]);
        adapter.add(cropfield);*/
        switch(view.getId()) {
            case R.id.add:
                String[] cropfields = new String[]{"One", "Two", "Three"};
                int nextInt = new Random().nextInt(3);
                //Save new cropfield to database
                //cropfield = datasource.createCropField("one");
                Log.w("Test", String.valueOf(datasource.createCropField("one")));

                //adapter.add(cropfield);
                break;
            /*case R.id.delete:
                if (getListAdapter().getCount() > 0) {
                    cropfield = (CropField) getListAdapter().getItem(0);
                    datasource.deleteCropField(cropfield);
                    adapter.remove(cropfield);
                }
                break;*/
        }
        //adapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume()
    {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        datasource.close();
        super.onPause();
    }
}
