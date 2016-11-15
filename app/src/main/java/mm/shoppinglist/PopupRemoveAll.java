package mm.shoppinglist;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

public class PopupRemoveAll extends Activity{
    DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_remove_all);
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width= displayMetrics.widthPixels;
        int hight=displayMetrics.heightPixels;
        getWindow().setLayout((int)(width*.6),(int)(hight*0.4));

    }
    public void removeAll(View v){
        dbHelper=new DBHelper(this);
        DBHelper.deleteWholeList(dbHelper.getWritableDatabase());
        dismiss(v);
    }
    public void dismiss(View v){
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
