package br.java.app_ecommerce_firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button aderirAgoraBtn;
    private Button loginBtn;
    private ProgressBar barraCarregamento;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aderirAgoraBtn = (Button) findViewById(R.id.aderirAgoraBtn);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        barraCarregamento = new ProgressBar(this);

        Paper.init(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            }
        });


    }
}