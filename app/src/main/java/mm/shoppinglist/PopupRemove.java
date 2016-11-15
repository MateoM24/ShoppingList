package mm.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class PopupRemove extends Activity{
    DBHelper dbHelper;
    TextView tv;
    String product;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_remove);
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width= displayMetrics.widthPixels;
        int hight=displayMetrics.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(hight*0.4));

    }
    public void remove(View v){
        dbHelper=new DBHelper(this);
        DBHelper.deleteRowShoppingList(dbHelper.getWritableDatabase(),product);
        Toast.makeText(getBaseContext(),R.string.removed,Toast.LENGTH_SHORT).show();
        dismiss(v);
    }
    public void dismiss(View v){
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent=getIntent();
        product=intent.getStringExtra("name");
        tv=(TextView)findViewById(R.id.TVProduct);
        tv.setText(product);
    }
}
