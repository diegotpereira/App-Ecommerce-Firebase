package br.java.app_ecommerce_firebase.sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import br.java.app_ecommerce_firebase.R;
import br.java.app_ecommerce_firebase.admin.VendedorProdutoCategoriaActivity;


public class VendedorAddNovoProdutoActivity extends AppCompatActivity {

    private String CategoriaNome;
    private String Descricao;
    private String Preco;
    private String Pnome;
    private String salvarAtualData;
    private String salvarAtualHora;

    private String vNome;
    private String vEndereco;
    private String vTelefone;
    private String vEmail;
    private String vID;

    private Button AddNovoProdutoBtn;

    private ImageView EntradaProdutoImagem;

    private EditText EntradaProdutoNome;
    private EditText EntradaProdutoDescricao;
    private EditText EntradaProdutoPreco;

    private static final int GaleriaEscolha = 1;

    private Uri ImagemUri;

    private String chaveAleatoriaProduto;
    private String downloadImagemUrl;

    private StorageReference ProdutoImagensRef;
    private DatabaseReference ProdutosRef;
    private DatabaseReference VendedorRef;

    private ProgressDialog carregarBarra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendedor_add_novo_produto);

        CategoriaNome = getIntent().getExtras().get("categoria").toString();

        ProdutoImagensRef = FirebaseStorage.getInstance().getReference().child("Produto Imagens");
        ProdutosRef = FirebaseDatabase.getInstance().getReference().child("Produtos");
        VendedorRef = FirebaseDatabase.getInstance().getReference().child("Vendedores");

        AddNovoProdutoBtn = (Button) findViewById(R.id.add_novo_produto);

        EntradaProdutoImagem = (ImageView) findViewById(R.id.selecione_produto_imagem);

        EntradaProdutoNome = (EditText) findViewById(R.id.produto_nome);
        EntradaProdutoDescricao = (EditText) findViewById(R.id.produto_descricao);
        EntradaProdutoPreco = (EditText) findViewById(R.id.produto_preco);

        carregarBarra = new ProgressDialog(this);

        EntradaProdutoImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AbrirGaleria();
            }
        });

        AddNovoProdutoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ValidarDadosProduto();
            }
        });

        VendedorRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    vNome = snapshot.child("nome").getValue().toString();
                    vEndereco = snapshot.child("endereco").getValue().toString();
                    vTelefone = snapshot.child("telefone").getValue().toString();
                    vID = snapshot.child("sid").getValue().toString();
                    vEmail = snapshot.child("email").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void AbrirGaleria() {
        Intent galeriaIntent = new Intent();
        galeriaIntent.setAction(Intent.ACTION_GET_CONTENT);
        galeriaIntent.setType("image/*");
        startActivityForResult(galeriaIntent, GaleriaEscolha);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GaleriaEscolha  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            ImagemUri = data.getData();
            EntradaProdutoImagem.setImageURI(ImagemUri);
        }
    }

    private void ValidarDadosProduto() {
        Descricao = EntradaProdutoDescricao.getText().toString();
        Preco = EntradaProdutoPreco.getText().toString();
        Pnome = EntradaProdutoNome.getText().toString();
        
        if (ImagemUri == null) {
            Toast.makeText(this, "A imagem do produto é obrigatória ...", Toast.LENGTH_SHORT).show();
            
        } else if (TextUtils.isEmpty(Descricao)) {
            Toast.makeText(this, "Escreva a descrição do produto ...", Toast.LENGTH_SHORT).show();
            
        } else if (TextUtils.isEmpty(Preco)) {
            Toast.makeText(this, "Escreva o preço do produto ...", Toast.LENGTH_SHORT).show();
            
        } else if (TextUtils.isEmpty(Pnome)) {
            Toast.makeText(this, "Escreva o nome do produto ...", Toast.LENGTH_SHORT).show();
        } else {
            InformacoesSobreProdutoLoja();
        }
    }

    private void InformacoesSobreProdutoLoja() {
        carregarBarra.setTitle("Adicionar Novo Produto");
        carregarBarra.setMessage("Caro administrador, aguarde enquanto adicionamos o novo produto.");
        carregarBarra.setCanceledOnTouchOutside(false);
        carregarBarra.show();

        Calendar calendario = Calendar.getInstance();

        SimpleDateFormat dataAtual = new SimpleDateFormat("MM dd, yyyy");
        salvarAtualData = dataAtual.format(calendario.getTime());

        SimpleDateFormat horaAtual = new SimpleDateFormat("HH:mm:SS a");
        salvarAtualHora = horaAtual.format(calendario.getTime());

        chaveAleatoriaProduto = salvarAtualData + salvarAtualHora;

        final  StorageReference caminhoArquivo = ProdutoImagensRef.child(ImagemUri.getLastPathSegment() + chaveAleatoriaProduto + ".jpg");
        final UploadTask carregarTarefa = caminhoArquivo.putFile(ImagemUri);

        carregarTarefa.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String mensagem = e.toString();
                Toast.makeText(VendedorAddNovoProdutoActivity.this, "Error: " + mensagem, Toast.LENGTH_SHORT).show();

                carregarBarra.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(VendedorAddNovoProdutoActivity.this, "Imagem do produto carregada com sucesso ...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = carregarTarefa.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {

                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        downloadImagemUrl = caminhoArquivo.getDownloadUrl().toString();
                        return caminhoArquivo.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {

                            downloadImagemUrl = task.getResult().toString();

                            Toast.makeText(VendedorAddNovoProdutoActivity.this, "obteve a URL da imagem do produto com sucesso ...\n", Toast.LENGTH_SHORT).show();

                            SalvarInformacoesProdutoNoBancoDados();
                        }
                    }
                });
            }
        });
    }

    private void SalvarInformacoesProdutoNoBancoDados() {
        HashMap<String, Object> produtoMap = new HashMap<>();
        produtoMap.put("pid", chaveAleatoriaProduto);
        produtoMap.put("data", salvarAtualData);
        produtoMap.put("hora", salvarAtualHora);
        produtoMap.put("descricao", Descricao);
        produtoMap.put("imagem", downloadImagemUrl);
        produtoMap.put("categoria", CategoriaNome);
        produtoMap.put("preco", Preco);
        produtoMap.put("pnome", Pnome);

        produtoMap.put("vendedorNome", vNome);
        produtoMap.put("vendedorEndereco", vEndereco);
        produtoMap.put("vendedorTelefone", vTelefone);
        produtoMap.put("vendedorEmail", vEmail);
        produtoMap.put("vID", vID);
        produtoMap.put("produtoEstado", "Não aprovado");

        ProdutosRef.child(chaveAleatoriaProduto).updateChildren(produtoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(VendedorAddNovoProdutoActivity.this, VendedorHomeActivity.class);
                    startActivity(intent);
                    
                    carregarBarra.dismiss();
                    Toast.makeText(VendedorAddNovoProdutoActivity.this, "Produto adicionado com sucesso...", Toast.LENGTH_SHORT).show();
                } else  {
                    carregarBarra.dismiss();

                    String menssagem = task.getException().toString();
                    Toast.makeText(VendedorAddNovoProdutoActivity.this, "Error: " + menssagem, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}