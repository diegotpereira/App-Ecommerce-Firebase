package br.java.app_ecommerce_firebase.sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import br.java.app_ecommerce_firebase.R;

public class VendedorLoginActivity extends AppCompatActivity {

    private EditText emailEntrada;
    private EditText senhaEntrada;

    private Button VendedorLoginBtn;

    private ProgressDialog carregarBarra;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendedor_login);

        mAuth = FirebaseAuth.getInstance();
        carregarBarra = new ProgressDialog(this);

        emailEntrada = findViewById(R.id.vendedor_login_email);
        senhaEntrada = findViewById(R.id.vendedor_login_senha);
        VendedorLoginBtn = findViewById(R.id.vendedor_login_btn);

        VendedorLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginVendedor();
            }
        });
    }

    private void loginVendedor() {

        final String email = emailEntrada.getText().toString();
        final String password = senhaEntrada.getText().toString();

        if (!email.equals("") && !password.equals("")) {

            carregarBarra.setTitle("Login da conta do vendedor");
            carregarBarra.setMessage("Aguarde enquanto suas informações estão sendo verificadas.");
            carregarBarra.setCanceledOnTouchOutside(false);
            carregarBarra.show();

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        Intent intent = new Intent(VendedorLoginActivity.this, VendedorHomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        } else {

            Toast.makeText(this, "Por favor, preencha o formulário de inscrição completamente\n", Toast.LENGTH_SHORT).show();

        }
    }
}