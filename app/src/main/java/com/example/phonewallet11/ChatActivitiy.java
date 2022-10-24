package com.example.phonewallet11;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChatActivitiy extends Activity {
    private TextView tvName,tvChatInfo;
    private Button btnSend;
    private EditText etInput;
    private String name;

    @Override
    protected void onCreate(/*@Nullable*/ Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        initView();

    }

    private void initView() {
        tvName = (TextView) findViewById(R.id.tvName);
        tvChatInfo = (TextView) findViewById(R.id.tvChatInfo);
        btnSend = (Button)findViewById(R.id.btnSend);
        etInput = (EditText) findViewById(R.id.etInput);
        tvName.setText(name);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etInput.getText().equals("")){
                    tvChatInfo.setText(etInput.getText());
                    tvChatInfo.setVisibility(View.VISIBLE);
                    etInput.setText("");
                }
            }
        });
    }
}
