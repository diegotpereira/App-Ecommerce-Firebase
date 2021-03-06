package br.java.app_ecommerce_firebase.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import com.rey.material.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.java.app_ecommerce_firebase.R;
import br.java.app_ecommerce_firebase.admin.AdminHomeActivity;
import br.java.app_ecommerce_firebase.sellers.VendedorProdutoCategoriaActivity;
import br.java.app_ecommerce_firebase.predominante.Predominante;
import br.java.app_ecommerce_firebase.modelo.Usuarios;
import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText EntradaTelefoneNumero;
    private EditText EntradaSenha;

    private Button LoginBtn;

    private ProgressDialog progressoDialogo;

    private TextView AdminLink;
    private TextView NotAdminLink;
    private TextView EsquecerSenhaLink;

    private String nomeBancoPai = "Usuarios";
    private CheckBox chkBoxLembreMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginBtn = (Button) findViewById(R.id.login_btn);
        EntradaSenha = (EditText) findViewById(R.id.login_senha_entrada);
        EntradaTelefoneNumero = (EditText) findViewById(R.id.login_telefone_numero_entrada);

        AdminLink = (TextView) findViewById(R.id.admin_panel_link);
        NotAdminLink = (TextView) findViewById(R.id.not_admin_panel_link);
        EsquecerSenhaLink = (TextView) findViewById(R.id.esuqecer_senha_link);

        progressoDialogo = new ProgressDialog(this);

        chkBoxLembreMe = (CheckBox) findViewById(R.id.lembre_me_chkb);
        Paper.init(this);

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUsuario();
            }
        });

        EsquecerSenhaLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, ResetarSenhaActivity.class);
                intent.putExtra("verificar", "login");
                startActivity(intent);
            }
        });

        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginBtn.setText("Entrar como Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);

                nomeBancoPai = "Admins";
            }
        });

        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LoginBtn.setText("Entrar");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                nomeBancoPai = "Usuarios";
            }
        });
    }

    private void LoginUsuario() {
        String telefone = EntradaTelefoneNumero.getText().toString();
        String senha = EntradaSenha.getText().toString();
        
        if (TextUtils.isEmpty(telefone)) {
            Toast.makeText(this, "Por favor digite seu n??mero de telefone...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(senha)) {
            Toast.makeText(this, "Por favor digite sua senha", Toast.LENGTH_SHORT).show();
        } else {
            progressoDialogo.setTitle("Conta de login");
            progressoDialogo.setMessage("Aguarde, enquanto verificamos as credenciais.");
            progressoDialogo.setCanceledOnTouchOutside(false);
            progressoDialogo.show();

            PermitirAcessoParaConta(telefone, senha);
        }
    }

    private void PermitirAcessoParaConta(final String telefone, final String senha) {
        if (chkBoxLembreMe.isChecked()) {
            Paper.book().write(Predominante.UsuarioTelefoneChave, telefone);
            Paper.book().write(Predominante.UsuarioSenhaChave, senha);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(nomeBancoPai).child(telefone).exists()) {
                    Usuarios usuariosDado = snapshot.child(nomeBancoPai).child(telefone).getValue(Usuarios.class);

                    if (usuariosDado.getTelefone().equals(telefone)) {
                        if (usuariosDado.getSenha().equals(senha)) {
                            if (nomeBancoPai.equals("Admins")) {
                                Toast.makeText(LoginActivity.this, "Bem-Vindo admin, voc?? est?? logado com sucesso ...", Toast.LENGTH_SHORT).show();
                                progressoDialogo.dismiss();

                                Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                                startActivity(intent);
                            } else if (nomeBancoPai.equals("Usuarios")) {
                                Toast.makeText(LoginActivity.this, "Conectado com sucesso...", Toast.LENGTH_SHORT).show();
                                progressoDialogo.dismiss();

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                Predominante.atualUsuarioOnline = usuariosDado;
                                startActivity(intent);
                            }
                        } else {
                            progressoDialogo.dismiss();
                            Toast.makeText(LoginActivity.this, "Senha Incorreta.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Conta com este " + telefone + " n??mero n??o existe.", Toast.LENGTH_SHORT).show();
                    progressoDialogo.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}