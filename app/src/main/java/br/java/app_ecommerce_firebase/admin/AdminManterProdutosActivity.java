package br.java.app_ecommerce_firebase.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import br.java.app_ecommerce_firebase.R;

public class AdminManterProdutosActivity extends AppCompatActivity {

    private Button AplicarMudancasBtn;
    private Button DeletarBtn;

    private EditText nome;
    private EditText preco;
    private EditText descricao;

    private ImageView exibirImagem;

    private String produtoID = "";

    private DatabaseReference produtosRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manter_produtos);

        produtoID = getIntent().getStringExtra("pid");
        produtosRef = FirebaseDatabase.getInstance().getReference().child("Produtos").child(produtoID);

        nome =  findViewById(R.id.produto_nome_manter);
        preco = findViewById(R.id.produto_preco_manter);
        descricao = findViewById(R.id.produto_descricao_manter);

        exibirImagem = findViewById(R.id.produto_imagem_manter);

        AplicarMudancasBtn = findViewById(R.id.aplicar_mudancas_btn);
        DeletarBtn = findViewById(R.id.deletar_produto_btn);

        exibirInformacoesEspecíficasProduto();

        AplicarMudancasBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                aplicarMudancas();
            }
        });

        DeletarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deletarEssesProdutos();
            }
        });
    }

    private void aplicarMudancas() {

        String pNome = nome.getText().toString();
        String pPreco = preco.getText().toString();
        String pDescricao = descricao.getText().toString();

        if (pNome.equals("")) {

            Toast.makeText(this, "escreva o nome do produto", Toast.LENGTH_SHORT).show();

        } else if (pPreco.equals("")) {

            Toast.makeText(this, "escreva o preço do produto", Toast.LENGTH_SHORT).show();

        } else if (pDescricao.equals("")) {

            Toast.makeText(this, "escreva a descrição do produto", Toast.LENGTH_SHORT).show();

        } else {

            HashMap<String, Object> produtoMapa = new HashMap<>();
            produtoMapa.put("pid", produtoID);
            produtoMapa.put("nome", pNome);
            produtoMapa.put("preco", pPreco);
            produtoMapa.put("descricao", pDescricao);

            produtosRef.updateChildren(produtoMapa).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {

                        Toast.makeText(AdminManterProdutosActivity.this, "Alterações aplicadas com sucesso.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AdminManterProdutosActivity.this, VendedorProdutoCategoriaActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    private void deletarEssesProdutos() {
        
        produtosRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                
                Intent intent = new Intent(AdminManterProdutosActivity.this, VendedorProdutoCategoriaActivity.class);
                startActivity(intent);
                finish();

                Toast.makeText(AdminManterProdutosActivity.this, "esse produto fpo deletado com sucesso.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private  void exibirInformacoesEspecíficasProduto() {

        produtosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    String pNome = snapshot.child("pnome").getValue().toString();
                    String pPreco = snapshot.child("preco").getValue().toString();
                    String pDescricao = snapshot.child("descricao").getValue().toString();
                    String pImagem = snapshot.child("imagem").getValue().toString();

                    nome.setText(pNome);
                    preco.setText(pPreco);
                    descricao.setText(pDescricao);
                    Picasso.get().load(pImagem).into(exibirImagem);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}