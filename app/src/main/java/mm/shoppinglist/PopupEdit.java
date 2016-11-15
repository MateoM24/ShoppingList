package mm.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PopupEdit extends Activity{
    DBHelper dbHelper;
    EditText editText;
    Intent intent;
    String oldName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_edit);
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width= displayMetrics.widthPixels;
        int hight=displayMetrics.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(hight*0.4));
        editText=(EditText)findViewById(R.id.edit_tv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        intent=getIntent();
        oldName=intent.getStringExtra("name");
        editText.setText(oldName);
    }

    public void edit(View v){
        dbHelper=new DBHelper(this);
        String newName=editText.getText().toString();
        DBHelper.updateRowNameValue(dbHelper.getWritableDatabase(),oldName,newName);
        Toast.makeText(getBaseContext(),"Product edited",Toast.LENGTH_SHORT).show();
        finish();
    }
}
