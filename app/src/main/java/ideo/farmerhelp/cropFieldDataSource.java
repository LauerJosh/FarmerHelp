package ideo.farmerhelp;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class cropFieldDataSource
{
    private SQLiteDatabase database;
    private DB dbHelper;
    private String[] allColumns = { DB.COLUMN_ID, DB.COLUMN_COMMENT};

    public cropFieldDataSource(Context context)
    {
        dbHelper = new DB(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close()
    {
        dbHelper.close();
    }

    public CropField createCropField(String CropField)
    {
        ContentValues values = new ContentValues();
        values.put(dbHelper.COLUMN_COMMENT, CropField);
        long insertID = database.insert(dbHelper.TABLE_COMMENTS, null, values);
        Cursor cursor = database.query(dbHelper.TABLE_COMMENTS, allColumns, dbHelper.COLUMN_ID + " = " + insertID, null, null, null, null);
        cursor.moveToFirst();
        CropField newCropField = cursorToCropField(cursor);
        cursor.close();
        return newCropField;
    }

    public void deleteCropField(CropField cropfield)
    {
        long id = cropfield.getID();
        System.out.println("Cropfield deleted with id: " + id);
        database.delete(DB.TABLE_COMMENTS, dbHelper.COLUMN_ID + " = " + id, null);
    }

    public List<CropField> getAllCropfields()
    {
        List<CropField> cropfields = new ArrayList<CropField>();

        Cursor cursor = database.query(dbHelper.TABLE_COMMENTS, allColumns, null, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            CropField cropfield = cursorToCropField(cursor);
            cropfields.add(cropfield);
            cursor.moveToNext();
        }
        //Make sure to close the cursor
        cursor.close();
        return cropfields;
    }

    private CropField cursorToCropField(Cursor cursor)
    {
        CropField cropfield = new CropField();
        cropfield.setID(cursor.getLong(0));
        cropfield.setCropfield(cursor.getString(1));
        return cropfield;
    }
}

