package com.loneyang.someviewgroup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.loneyang.someviewgroup.veiw.ArcMenu;
import com.loneyang.someviewgroup.veiw.ArcMenu.OnMenuItemClickListener;


public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ArcMenu mArcMenuLeftTop = (ArcMenu) findViewById(R.id.id_arcmenu1);
        //动态添加一个MenuItem
        ImageView people = new ImageView(this);
        people.setImageResource(R.drawable.ic_4);
        people.setTag("People");
        mArcMenuLeftTop.addView(people);

        mArcMenuLeftTop.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public void onClick(View view, int pos) {
                Toast.makeText(Main2Activity.this,
                        pos + ":" + view.getTag(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
