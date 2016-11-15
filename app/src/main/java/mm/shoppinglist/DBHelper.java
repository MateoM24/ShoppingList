package mm.shoppinglist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import static android.R.attr.name;
import static java.lang.Boolean.FALSE;

/**
 * Created by Mateusz on 2016-10-29.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="DataBase";
    private static final int DB_VERSION=1;
    private static final String TableShoppings="ShoppingList";

    DBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override  //gdy BD po raz pierwszy jest tworzona
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+TableShoppings+
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "NAME TEXT UNIQUE, "+
                "DONE INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }


    public static boolean insertRowShoppingList (SQLiteDatabase db, String name)throws SQLiteConstraintException {
       ContentValues values=new ContentValues();
        values.put("NAME",name);
        values.put("DONE",false);
           long success=db.insert(TableShoppings,null,values);
            if(success==-1) throw new SQLiteConstraintException("Product already exists");
           return success!=-1;


    }
    public static boolean updateRowNameValue(SQLiteDatabase db, String OldName, String NewName){
        ContentValues values=new ContentValues();
        values.put("Name",NewName);
        int success = db.update(TableShoppings,values,"NAME = ?",new String[]{OldName});
        return success!=0;
    }
    public static boolean updateRowDoneValue(SQLiteDatabase db, String Name, boolean done){
        ContentValues values=new ContentValues();
        values.put("DONE",done);
        int success = db.update(TableShoppings,values,"NAME = ?",new String[]{Name});
        return success!=0;
    }
    public static int deleteRowShoppingList(SQLiteDatabase db, String name){
        int success=db.delete(TableShoppings,"NAME = ?",new String[]{name});
        return success;
    }
    public static int deleteWholeList(SQLiteDatabase db){
        int success=db.delete(TableShoppings,null,null);
        return success;
    }
    public static Cursor getAllShoppings(SQLiteDatabase db){
        Cursor cursor=db.query(TableShoppings,new String[]{"_ID","NAME","DONE"},null,null,null
        ,null,null);
        return cursor;
    }
    public static Cursor getListToBuy(SQLiteDatabase db){
        Cursor cursor=db.query(TableShoppings,new String[]{"_ID","NAME","DONE"},"DONE = ?",new String[]{"0"},null
                ,null,null);
        return cursor;
    }
    public static Cursor getListDone(SQLiteDatabase db){
        Cursor cursor=db.query(TableShoppings,new String[]{"_ID","NAME","DONE"},"DONE = ?",new String[]{"1"},null
                ,null,null);
        return cursor;
    }
    public static Cursor getDoneValue(SQLiteDatabase db, String name){
        Cursor cursor=db.query(TableShoppings,new String[]{"DONE"},"NAME = ?",new String[]{name},null,null,null);
        return cursor;
    }
}
