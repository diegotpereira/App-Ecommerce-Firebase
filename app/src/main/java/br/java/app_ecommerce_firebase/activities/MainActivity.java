package br.java.app_ecommerce_firebase.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;


import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.java.app_ecommerce_firebase.R;
import br.java.app_ecommerce_firebase.predominante.Predominante;
import br.java.app_ecommerce_firebase.modelo.Usuarios;
import br.java.app_ecommerce_firebase.sellers.VendedorHomeActivity;
import br.java.app_ecommerce_firebase.sellers.VendedorRegistroActivity;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button aderirAgoraBtn;
    private Button loginBtn;
    private ProgressDialog barraCarregamento;
    private TextView VendedorTxtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aderirAgoraBtn = (Button) findViewById(R.id.principal_junte_agora_btn);
        loginBtn = (Button) findViewById(R.id.principal_login_btn);
        barraCarregamento = new ProgressDialog(this);
        VendedorTxtView = (TextView) findViewById(R.id.vendedor_login_btn);

        Paper.init(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        VendedorTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, VendedorRegistroActivity.class);
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

               barraCarregamento.setTitle("J?? logado");
               barraCarregamento.setMessage("Por favor aguarde...");
               barraCarregamento.setCanceledOnTouchOutside(false);
               barraCarregamento.show();
           }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null ){

            Intent intent = new Intent(MainActivity.this, VendedorHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

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
                            Toast.makeText(MainActivity.this, "Aguarde, voc?? j?? est?? logado ...", Toast.LENGTH_SHORT).show();
                            barraCarregamento.dismiss();

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            Predominante.atualUsuarioOnline = usuarioDados;
                            startActivity(intent);

                        } else {
                            barraCarregamento.dismiss();

                            Toast.makeText(MainActivity.this, "Senha incorreta.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Conta com este " + telefone + " n??mero n??o existe.", Toast.LENGTH_SHORT).show();
                    barraCarregamento.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}