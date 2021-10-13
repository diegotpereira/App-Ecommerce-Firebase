package br.java.app_ecommerce_firebase.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import br.java.app_ecommerce_firebase.R;
import br.java.app_ecommerce_firebase.modelo.Produtos;
import br.java.app_ecommerce_firebase.predominante.Predominante;

public class ProdutoDetalhesActivity extends AppCompatActivity {

    private Button addNoCarrinhoBtn;

    private ImageView produtoImagem;
    private ElegantNumberButton numeroBtn;

    private TextView produtoPreco;
    private TextView produtoDescricao;
    private TextView produtoNome;

    private String produtoID = " ",  estado = "Normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_detalhes);

        produtoID = getIntent().getStringExtra("pid");

        addNoCarrinhoBtn = findViewById(R.id.pd_to_carrinho_btn);
        numeroBtn = findViewById(R.id.numero_btn);
        produtoImagem =    findViewById(R.id.produto_imagem_detalhes);
        produtoNome =      findViewById(R.id.produto_nome_detalhes);
        produtoDescricao = findViewById(R.id.produto_descricao_detalhes);
        produtoPreco =     findViewById(R.id.produto_preco_detalhes);
        produtoPreco =     findViewById(R.id.produto_preco_detalhes);

        getProdutosDetalhes(produtoID);


        addNoCarrinhoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (estado.equals("Pedido feito") || estado.equals("Encomenda Enviada")) {

                    Toast.makeText(ProdutoDetalhesActivity.this, "você pode adicionar uma compra, uma vez que seu pedido seja enviado ou confrmado.", Toast.LENGTH_SHORT).show();
                } else {

                    addCarrinhoLista();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        VerifiqueEstadoPedido();
    }

    private void addCarrinhoLista() {

        String salvarHoraAtual;
        String salvarDataAtual;

        Calendar calParaData =  Calendar.getInstance();
        SimpleDateFormat atualData = new SimpleDateFormat("MMM dd, yyyy");
        salvarDataAtual = atualData.format(calParaData.getTime());

        SimpleDateFormat atualHora = new SimpleDateFormat("HH:mm:ss");
        salvarHoraAtual = atualData.format(calParaData.getTime());

        final DatabaseReference carrinhoListaRef = FirebaseDatabase.getInstance().getReference().child("carrinho lista");

        final HashMap<String, Object> carrinhoMap = new HashMap<>();
        carrinhoMap.put("pid", produtoID);
        carrinhoMap.put("pnome", produtoNome.getText().toString());
        carrinhoMap.put("preco", produtoPreco.getText().toString());
        carrinhoMap.put("data", salvarDataAtual);
        carrinhoMap.put("hora", salvarHoraAtual);
        carrinhoMap.put("quantidade", numeroBtn.getNumber());
        carrinhoMap.put("desconto", " ");

        carrinhoListaRef.child("Visualizacao do usuario").child(Predominante.atualUsuarioOnline.getTelefone()).child("Produtos").child(produtoID).updateChildren(carrinhoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    carrinhoListaRef.child("Visualizacao de administrador").child(Predominante.atualUsuarioOnline.getTelefone()).child("Produtos").child(produtoID).updateChildren(carrinhoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            
                            if (task.isSuccessful()) {

                                Toast.makeText(ProdutoDetalhesActivity.this, "Adicionado à lista do carrinho.", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(ProdutoDetalhesActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });
    }

    private void getProdutosDetalhes(String produtoID) {

        DatabaseReference produtoRef = FirebaseDatabase.getInstance().getReference().child("Produtos");

        produtoRef.child(produtoID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    Produtos produtos = snapshot.getValue(Produtos.class);

                    produtoNome.setText(produtos.getPnome());
                    produtoPreco.setText(produtos.getPreco());
                    produtoDescricao.setText(produtos.getDescricao());

                    Picasso.get().load(produtos.getImagem()).into(produtoImagem);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void VerifiqueEstadoPedido() {

        DatabaseReference pedidoRef;
        pedidoRef = FirebaseDatabase.getInstance().getReference().child("Pedidos").child(Predominante.atualUsuarioOnline.getTelefone());

        pedidoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String estadoDeEnvio = snapshot.child("estado").getValue().toString();

                    if (estadoDeEnvio.equals("enviado")) {

                        estado = "Encomenda enviada";
                    } else if (estadoDeEnvio.equals("nao enviado")) {

                        estado = "Pedido feito";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}