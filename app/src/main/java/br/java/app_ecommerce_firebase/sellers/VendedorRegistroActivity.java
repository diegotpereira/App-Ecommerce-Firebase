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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import br.java.app_ecommerce_firebase.R;


public class VendedorRegistroActivity extends AppCompatActivity {


    private EditText nomeEntrada;
    private EditText telefoneEntrada;
    private EditText emailEntrada;
    private EditText senhaEntrada;
    private EditText enderecoEntrada;

    private Button VendedorLoginBtn;


    private FirebaseAuth mAuth;

    private ProgressDialog carregarBarra;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendedor_registro);

        mAuth = FirebaseAuth.getInstance();

        carregarBarra = new ProgressDialog(this);

        nomeEntrada = findViewById(R.id.vendedor_nome);
        telefoneEntrada = findViewById(R.id.vendedor_telefone);
        emailEntrada = findViewById(R.id.vendedor_email);
        senhaEntrada = findViewById(R.id.vendedor_senha);
        enderecoEntrada = findViewById(R.id.vendedor_endereco);


        VendedorLoginBtn = findViewById(R.id.vendedor_login_btn);
        VendedorLoginBtn = findViewById(R.id.vendedor_registro_btn);

        VendedorLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VendedorRegistroActivity.this, VendedorLoginActivity.class);
                startActivity(intent);
            }
        });

        VendedorLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registrarVendedor();
            }
        });
    }


    private void registrarVendedor() {

        final String nome = nomeEntrada.getText().toString();
        final String telefone = telefoneEntrada.getText().toString();
        final String email = emailEntrada.getText().toString();
        String password = senhaEntrada.getText().toString();
        final String endereco = enderecoEntrada.getText().toString();

        if (!nome.equals("") && !telefone.equals("") && !email.equals("") && !password.equals("") && !endereco.equals(""))  {

            carregarBarra.setTitle("Criar Conta de Vendedor");
            carregarBarra.setMessage("Por favor, aguarde enquanto estamos checando suas credenciais.");
            carregarBarra.setCanceledOnTouchOutside(false);
            carregarBarra.show();
            
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        final DatabaseReference rootRef;
                        rootRef = FirebaseDatabase.getInstance().getReference();

                        String sid = mAuth.getCurrentUser().getUid();

                        HashMap<String, Object> vendedorMap = new HashMap<>();
                        vendedorMap.put("sid", sid);
                        vendedorMap.put("telefone", telefone);
                        vendedorMap.put("email", email);
                        vendedorMap.put("endereco", endereco);
                        vendedorMap.put("nome", nome);

                        rootRef.child("Vendedores").child(sid).updateChildren(vendedorMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                carregarBarra.dismiss();
                                Toast.makeText(VendedorRegistroActivity.this, "Você foi registrado com sucesso.", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(VendedorRegistroActivity.this, VendedorHomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                }
            });
        } else {

            Toast.makeText(this, "Por favor complete o formulário de registro.", Toast.LENGTH_SHORT).show();
        }

    }
}