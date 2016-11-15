package mm.shoppinglist;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by Mateusz on 2016-11-05.
 */

public class ShoppingListContentProvider extends ContentProvider {
    DBHelper dbHelper;
    SQLiteDatabase db;
    private static final UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
    static{
        uriMatcher.addURI("content://ShoppingListCP","TableShoppings",1);
        uriMatcher.addURI("content://ShoppingListCP","TableShoppingsToBuy",2);
        uriMatcher.addURI("content://ShoppingListCP","TableShoppingsDone",3);
        uriMatcher.addURI("content://ShoppingListCP","TableShoppings/Insert",4);
        uriMatcher.addURI("content://ShoppingListCP","TableShoppings/#",5);

    }
    @Override
    public boolean onCreate() {
        dbHelper=new DBHelper(getContext());
        db=dbHelper.getWritableDatabase();
        return (db==null)?false:true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        DBHelper dbHelper=new DBHelper(getContext());
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        switch (uriMatcher.match(uri)){
            case 1:
                Cursor cursor1=DBHelper.getAllShoppings(db);
                cursor1.setNotificationUri(getContext().getContentResolver(),uri);
                return cursor1;
            case 2:
                Cursor cursor2=DBHelper.getListToBuy(db);
                cursor2.setNotificationUri(getContext().getContentResolver(),uri);
                return cursor2;
            case 3:
                Cursor cursor3=DBHelper.getListDone(db);
                cursor3.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor3;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case 1:
                return "vnd.android.cursor.dir/vnd.mm.shoppinglist.ShoppingList";
            case 2:
                return "vnd.android.cursor.dir/vnd.mm.shoppinglist.ShoppingList.ToBuy";
            case 3:
                return "vnd.android.cursor.dir/vnd.mm.shoppinglist.ShoppingList.MarkPurchased";
            case 4:
                return "vnd.android.cursor.dir/vnd.mm.shoppinglist.ShoppingList.Insert";
            case 5:
                return "vnd.android.cursor.dir/vnd.mm.shoppinglist.ShoppingList.Product";

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        if (uriMatcher.match(uri)==4){
            DBHelper dbHelper=new DBHelper(getContext());
            SQLiteDatabase db=dbHelper.getWritableDatabase();
            try {
                String name = (String) contentValues.get("NAME");
                DBHelper.insertRowShoppingList(db,name);
                SQLiteDatabase rdb=dbHelper.getReadableDatabase();
                Cursor cursor=DBHelper.getAllShoppings(rdb);
                long id=cursor.getLong(cursor.getColumnIndex("_ID"));
                Uri uriuri=ContentUris.withAppendedId(uri,id);
                getContext().getContentResolver().notifyChange(uriuri, null);
                return uriuri;
            }catch (Exception e){return null;}
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        db=dbHelper.getWritableDatabase();
        switch(uriMatcher.match(uri)){
            case 1:
                int success=DBHelper.deleteWholeList(db);
                getContext().getContentResolver().notifyChange(uri, null);
                return success;
            case 5:
                String id = uri.getPathSegments().get(1);
                int count = db.delete("ShoppingList", "_ID" +  " = " + id +
                    (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                    getContext().getContentResolver().notifyChange(uri, null);
                    return count;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)){
            case 1:
                int count = db.update("ShoppingList", contentValues, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return count;

            case 5:
                count = db.update("ShoppingList", contentValues, "_ID" + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +selection + ')' : ""), selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return count;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
    }
}