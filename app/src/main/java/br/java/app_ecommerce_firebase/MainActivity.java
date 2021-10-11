package br.java.app_ecommerce_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.java.app_ecommerce_firebase.predominante.Predominante;
import br.java.app_ecommerce_firebase.modelo.Usuarios;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button aderirAgoraBtn;
    private Button loginBtn;
    private ProgressBar barraCarregamento;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aderirAgoraBtn = (Button) findViewById(R.id.principal_junte_agora_btn);
        loginBtn = (Button) findViewById(R.id.principal_login_btn);
        barraCarregamento = new ProgressBar(this);

        Paper.init(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        aderirAgoraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegistrarActivity.class);
                startActivity(intent);
            }
        });

        String UsuarioTelefoneChave = Paper.book().read(Predominante.UsuarioTelefoneChave);
        String UsuarioSenhaChave = Paper.book().read(Predominante.UsuarioSenhaChave);

        if (UsuarioTelefoneChave != "" && UsuarioSenhaChave != "") {
           if (!TextUtils.isEmpty(UsuarioTelefoneChave) && !TextUtils.isEmpty(UsuarioSenhaChave)) {
               PermitirAcesso(UsuarioTelefoneChave, UsuarioSenhaChave);

               //barraCarregamento.setTitulo("Já logado");
               //barraCarregamento.setMensagem("Por favor aguarde...");
               //barraCarregamento.setCanceladoToqueExterno(false);
               //barraCarregamento.show();
           }
        }
    }

    private void PermitirAcesso(final String telefone, final String senha) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Usuarios").child(telefone).exists()) {
                    Usuarios usuarioDados = snapshot.child("Usuarios").child(telefone).getValue(Usuarios.class);

                    if (usuarioDados.getTelefone().equals(telefone)) {
                        if (usuarioDados.getSenha().equals(senha)) {
                            Toast.makeText(MainActivity.this, "Aguarde, você já está logado ...", Toast.LENGTH_SHORT).show();
                            //.liberar();

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            Predominante.atualUsuarioOnline = usuarioDados;
                            startActivity(intent);

                        } else {
                            //barraCarregamento.liberar();

                            Toast.makeText(MainActivity.this, "Senha incorreta.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Conta com este " + telefone + " número não existe.", Toast.LENGTH_SHORT).show();
                    //barraCarregamento.liberar();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}