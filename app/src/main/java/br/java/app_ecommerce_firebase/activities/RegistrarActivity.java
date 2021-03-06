package br.java.app_ecommerce_firebase.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import br.java.app_ecommerce_firebase.R;

public class RegistrarActivity extends AppCompatActivity {

    private Button CriarContaBtn;
    private EditText EntradaNome;
    private EditText EntradaTelefoneNumero;
    private EditText EntradaSenha;

    private ProgressDialog progressoDialogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        CriarContaBtn = (Button) findViewById(R.id.registro_btn);
        EntradaNome = (EditText) findViewById(R.id.registro_usuario_entrada);
        EntradaSenha = (EditText) findViewById(R.id.registro_senha_entrada);
        EntradaTelefoneNumero = (EditText) findViewById(R.id.registro_telefone_numero_entrada);
        progressoDialogo = new ProgressDialog(this);

        CriarContaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CriarConta();
            }
        });
    }

    private void CriarConta() {
        String nome = EntradaNome.getText().toString();
        String telefone = EntradaTelefoneNumero.getText().toString();
        String senha = EntradaSenha.getText().toString();

        if (TextUtils.isEmpty(nome)) {
            Toast.makeText(this, "Pro favor digite seu nome.", Toast.LENGTH_SHORT).show();

        } else  if (TextUtils.isEmpty(telefone)) {
            Toast.makeText(this, "Por favor digite seu n??mero de telefone.", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(senha)) {
            Toast.makeText(this, "Por favor digite sua senha.", Toast.LENGTH_SHORT).show();

        } else {

            progressoDialogo.setTitle("Criar Conta");
            progressoDialogo.setMessage("Por favor aguarde, enquanto checamos suas credenciais.");
            progressoDialogo.setCanceledOnTouchOutside(false);
            progressoDialogo.show();

            ValidarNumeroTelefone(nome, telefone, senha);
        }
    }

    private void ValidarNumeroTelefone(final String nome, final String telefone, final String senha) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Usuarios").child(telefone).exists())) {
                    HashMap<String, Object> usuarioDadoMap = new HashMap<>();
                    usuarioDadoMap.put("nome", nome);
                    usuarioDadoMap.put("telefone", telefone);
                    usuarioDadoMap.put("senha", senha);

                    RootRef.child("Usuarios").child(telefone).updateChildren(usuarioDadoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegistrarActivity.this, "Parab??ns, sua conta foi criado com sucesso.", Toast.LENGTH_SHORT).show();

                                progressoDialogo.dismiss();
                                Intent intent = new Intent(RegistrarActivity.this, LoginActivity.class);
                                startActivity(intent);

                            } else {
                                progressoDialogo.dismiss();
                                Toast.makeText(RegistrarActivity.this, "Erro de rede: tente novamente depois de algum tempo ...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegistrarActivity.this, "Esse " + telefone + " j?? existe.", Toast.LENGTH_SHORT).show();

                    progressoDialogo.dismiss();
                    Toast.makeText(RegistrarActivity.this, "Tente novamente usando outro n??mero de telefone.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegistrarActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}