package br.java.app_ecommerce_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import br.java.app_ecommerce_firebase.predominante.Predominante;
import de.hdodenhof.circleimageview.CircleImageView;

public class ConfiguracoesActivity extends AppCompatActivity {

    private CircleImageView perfilImagemView;

    private EditText nomeCompletoEditText;
    private EditText usuarioTelefoneEditText;
    private EditText enderecoEditText;

    private TextView perfilAlterarTextBtn;
    private TextView fecharTextBtn;
    private TextView salvarTextBtn;

    private Uri imagemUri;
    private String minhaUrl ="";
    private StorageTask carregarTarefa;
    private StorageReference imagemPerfilArmazenamentoRef;
    private String verificador = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        imagemPerfilArmazenamentoRef = FirebaseStorage.getInstance().getReference().child("Fotos de perfil");

        perfilImagemView = (CircleImageView) findViewById(R.id.imagem_perfil_configuracoes);
        nomeCompletoEditText = (EditText) findViewById(R.id.nome_completo_configuracoes);
        usuarioTelefoneEditText = (EditText) findViewById(R.id.configuracoes_numero_telefone);
        enderecoEditText = (EditText) findViewById(R.id.endereco_configuracoes);

        perfilAlterarTextBtn = (TextView) findViewById(R.id.mudanca_imagem_perfil_btn);
        fecharTextBtn = (TextView) findViewById(R.id.fechar_configuracoes_btn);
        salvarTextBtn = (TextView) findViewById(R.id.atualizar_conta_configuracoes_btn);

        exibirInfoUsuario(perfilImagemView, nomeCompletoEditText, usuarioTelefoneEditText, enderecoEditText );

        fecharTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        salvarTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (verificador.equals("clicado")) {

                    infoUsuarioSalvas();

                } else  {

                    atualizarApenasInfoUsuario();
                }
            }
        });

        perfilAlterarTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verificador = "clicado";

                CropImage.activity(imagemUri).setAspectRatio(1, 1).start(ConfiguracoesActivity.this);
            }
        });
    }

    private void infoUsuarioSalvas() {
        
        if (TextUtils.isEmpty(nomeCompletoEditText.getText().toString())) {
            Toast.makeText(this, "O nome é obrigatório.", Toast.LENGTH_SHORT).show();
            
        } else if (TextUtils.isEmpty(enderecoEditText.getText().toString())) {
            Toast.makeText(this, "O nome é o endereço.", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(usuarioTelefoneEditText.getText().toString())) {
            Toast.makeText(this, "O nome é obrigatório", Toast.LENGTH_SHORT).show();

        } else if (verificador.equals("clicado")) {
            enviarImagem();
        }
    }

    private void enviarImagem() {
        final ProgressDialog progressoDialogo = new ProgressDialog(this);
        progressoDialogo.setTitle("Atualizar Perfil");
        progressoDialogo.setMessage("Aguarde, enquanto atualizamos as informações da sua conta.");
        progressoDialogo.setCanceledOnTouchOutside(false);
        progressoDialogo.show();

        if (imagemUri != null) {
            final StorageReference arquivoRef = imagemPerfilArmazenamentoRef.child(Predominante.atualUsuarioOnline.getTelefone() + ".jpg");

            carregarTarefa = arquivoRef.putFile(imagemUri);

            carregarTarefa.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if (!task.isSuccessful()) {
                        throw  task.getException();
                    }

                    return arquivoRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        minhaUrl = downloadUri.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Usuarios");

                        HashMap<String, Object> usuarioMap = new HashMap<>();
                        usuarioMap.put("nome", nomeCompletoEditText.getText().toString());
                        usuarioMap.put("endereco", enderecoEditText.getText().toString());
                        usuarioMap.put("telefonePedido", usuarioTelefoneEditText.getText().toString());
                        usuarioMap.put("imagem", minhaUrl);

                        ref.child(Predominante.atualUsuarioOnline.getTelefone()).updateChildren(usuarioMap);

                        progressoDialogo.dismiss();
                        
                        startActivity(new Intent(ConfiguracoesActivity.this, MainActivity.class));

                        Toast.makeText(ConfiguracoesActivity.this, "Atualização das informações do perfil com sucesso.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else  {
                        progressoDialogo.dismiss();

                        Toast.makeText(ConfiguracoesActivity.this, "Error.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "A imagem não está selecionada.", Toast.LENGTH_SHORT).show();
        }
    }

    private void atualizarApenasInfoUsuario() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Usuarios");

        HashMap<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("nome", nomeCompletoEditText.getText().toString());
        usuarioMap.put("endereco", enderecoEditText.getText().toString());
        usuarioMap.put("telefonePedido", usuarioTelefoneEditText.getText().toString());

        ref.child(Predominante.atualUsuarioOnline.getTelefone()).updateChildren(usuarioMap);
        
        startActivity(new Intent(ConfiguracoesActivity.this, MainActivity.class));

        Toast.makeText(ConfiguracoesActivity.this, "Atualização das informações do perfil atualizada com sucesso.", Toast.LENGTH_SHORT).show();

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imagemUri = result.getUri();

            perfilImagemView.setImageURI(imagemUri);
        }
        else
        {
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(ConfiguracoesActivity.this, ConfiguracoesActivity.class));
            finish();
        }
    }

    private void exibirInfoUsuario(final CircleImageView perfilImagemView, final EditText nomeCompletoEditText, final EditText usuarioTelefoneEditText, final EditText enderecoEditText) {

        DatabaseReference UsuarioRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(Predominante.atualUsuarioOnline.getTelefone());

        UsuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    if (snapshot.child("imagem").exists()) {

                        String imagem = snapshot.child("imagem").getValue().toString();
                        String nome = snapshot.child("nome").getValue().toString();
                        String telefone = snapshot.child("telefone").getValue().toString();
                        String endereco = snapshot.child("endereco").getValue().toString();

                        Picasso.get().load(imagem).into(perfilImagemView);
                        nomeCompletoEditText.setText(nome);
                        usuarioTelefoneEditText.setText(telefone);
                        enderecoEditText.setText(endereco);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}