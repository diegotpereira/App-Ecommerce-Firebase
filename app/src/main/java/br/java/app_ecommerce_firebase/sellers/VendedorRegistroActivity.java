package br.java.app_ecommerce_firebase.sellers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.java.app_ecommerce_firebase.R;

public class VendedorRegistroActivity extends AppCompatActivity {
    private Button VendedorLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendedor_registro);

        VendedorLoginBtn = findViewById(R.id.vendedor_tem_conta_btn);

        VendedorLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VendedorRegistroActivity.this, VendedorLoginActivity.class);
                startActivity(intent);
            }
        });
    }
}