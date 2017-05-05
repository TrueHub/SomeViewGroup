package com.loneyang.someviewgroup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loneyang.someviewgroup.veiw.FlowLayout;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private FlowLayout flowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        flowLayout.setOnChildSelectedListener(new FlowLayout.OnChildSelectedListener() {
            @Override
            public void onChildSelected(String content, View child) {
                editText.setText(content);
            }
        });
    }

    private void initView() {
        editText = (EditText) findViewById(R.id.editText);
        flowLayout = (FlowLayout) findViewById(R.id.flowLayout);
    }

    private void submit() {
        // validate
        String editTextString = editText.getText().toString().trim();
        if (TextUtils.isEmpty(editTextString)) {
            Toast.makeText(this, "吾与徐公熟美", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


    }
}
