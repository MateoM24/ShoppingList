package mm.shoppinglist;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class ProductListActivity extends ListActivity implements AdapterView.OnItemClickListener {
    private SQLiteDatabase db;
    private Cursor cursor;
    LinearLayout linearLayout;
    TextView currentTV;
    DBHelper dbHelper;
    CheckBox lastCB;
    CheckBox currentCB;
    Button currentButton;
    Intent intent;
    boolean isSelected;
    ListView list;
    ListAdapter listAdapter;
    Button didBuy;
    Button toDoneList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        dbHelper=new DBHelper(this);
        didBuy=(Button)findViewById(R.id.bought);
        toDoneList=(Button)findViewById(R.id.toDoneList);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        cursor.close();
        db.close();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        isSelected=true;
        linearLayout= (LinearLayout)view;
        if(currentCB==null){
        currentCB=(CheckBox) linearLayout.getChildAt(0);
        currentCB.toggle();
            currentButton=(Button)linearLayout.getChildAt(1);
        }else{
            lastCB=currentCB;
            lastCB.toggle();
            currentCB=(CheckBox) linearLayout.getChildAt(0);
            currentCB.toggle();
            currentButton=(Button)linearLayout.getChildAt(1);
            }
        }


        @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu__shopping_list, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_option:
                intent=new Intent(this,Popup.class);
                startActivity(intent);
                return true;
            case R.id.edit_option:
                if(isSelected){
                intent=new Intent(this,PopupEdit.class);
                intent.putExtra("name",currentButton.getText().toString());
                startActivity(intent);}else{
                    Toast.makeText(getBaseContext(),R.string.select_item,Toast.LENGTH_SHORT);
                }
                return true;
            case R.id.delete_option:
                if(isSelected) {
                    intent = new Intent(this, PopupRemove.class);
                    intent.putExtra("name", currentButton.getText().toString());
                    startActivity(intent);
                isSelected=false;}else{
                    Toast.makeText(getBaseContext(),R.string.select_item,Toast.LENGTH_SHORT);
                }
                return true;
            case R.id.delete_all_option:
                intent=new Intent(this,PopupRemoveAll.class);
                startActivity(intent);
                isSelected=false;
                return true;
            case R.id.return_button:
                intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        list=getListView();
        dbHelper=new DBHelper(this);
        db=dbHelper.getWritableDatabase();
        cursor=DBHelper.getListToBuy(db);

        listAdapter=new MySimpleCursorAdapter2(this,
                R.layout.single_row,cursor, new String[]{"NAME"},
                new int[]{R.id.productNameTV},0);
        list.setAdapter(listAdapter);
        list.setOnItemClickListener(this);
    }
    //================================================================================
    public class MySimpleCursorAdapter2 extends android.support.v4.widget.SimpleCursorAdapter {
        SharedPreferences sharedPreferences;

        public MySimpleCursorAdapter2(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);

        }

        @Override
        public void setViewText(TextView v, String text) {
            super.setViewText(v, text);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return super.newView(context, cursor, parent);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null)
            {
                LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.single_row, null);
            }
            TextView tv = (TextView)convertView.findViewById(R.id.productNameTV);
            sharedPreferences=getSharedPreferences("prefs",MODE_PRIVATE);
            tv.setTextSize(sharedPreferences.getInt("size",20));
            tv.setTextColor(sharedPreferences.getInt("color",Color.GRAY));
//            String currViewText=tv.getText().toString();
//            Cursor cursorI=DBHelper.getDoneValue(dbHelper.getReadableDatabase(),currViewText);
//            if(cursorI!=null && cursorI.moveToFirst()) {
//                int i = cursorI.getInt(0);
//                if (i == 1) {
//                    tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                }else{
//                    tv.setPaintFlags(tv.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
//                }
//            }
           return super.getView(position, convertView, parent);

        }
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            super.bindView(view, context, cursor);
        }
    }
    //=====================================================
    private void refreshTheList(){
        this.onResume();
    }
    public void markDone(View v) {
        if (currentButton != null) {
        DBHelper.updateRowDoneValue(dbHelper.getWritableDatabase(), currentButton.getText().toString(), true);}
        refreshTheList();
    }
    public void goToDoneList(View v){
        intent=new Intent(this,DoneListActivity.class);
        startActivity(intent);
    }
}
