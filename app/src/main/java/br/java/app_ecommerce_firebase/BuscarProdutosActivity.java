package br.java.app_ecommerce_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import br.java.app_ecommerce_firebase.ViewHolder.ProdutoViewHolder;
import br.java.app_ecommerce_firebase.modelo.Produtos;

public class BuscarProdutosActivity extends AppCompatActivity {

    private EditText entradaTxt;
    private Button BuscarBtn;
    private RecyclerView buscarLista;
    private String BuscarEntrada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_produtos);

        entradaTxt = findViewById(R.id.buscar_produto_nome);
        BuscarBtn = findViewById(R.id.buscar_btn);
        buscarLista = findViewById(R.id.buscar_lista);

        buscarLista.setLayoutManager(new LinearLayoutManager(BuscarProdutosActivity.this));

        BuscarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuscarEntrada = entradaTxt.getText().toString();

                onStart();
            }
        });
    }

    @Override
    protected void onStart() {

        super.onStart();

        DatabaseReference buscarRef = FirebaseDatabase.getInstance().getReference().child("Produtos");

        FirebaseRecyclerOptions<Produtos> opcoes = new FirebaseRecyclerOptions.Builder<Produtos>().setQuery(buscarRef.orderByChild("pnome").startAt(BuscarEntrada), Produtos.class).build();

        FirebaseRecyclerAdapter<Produtos, ProdutoViewHolder> adapter = new FirebaseRecyclerAdapter<Produtos, ProdutoViewHolder>(opcoes) {
            @Override
            protected void onBindViewHolder(@NonNull ProdutoViewHolder produtoViewHolder, int i, @NonNull Produtos produtos) {

                produtoViewHolder.txtProdutoNome.setText(produtos.getPnome());
                produtoViewHolder.txtProdutoDescricao.setText(produtos.getDescricao());
                produtoViewHolder.txtProdutoPreco.setText(produtos.getPreco());

                Picasso.get().load(produtos.getImagem()).into(produtoViewHolder.exibirImagem);

                produtoViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(BuscarProdutosActivity.this, ProdutoDetalhesActivity.class);
                        intent.putExtra("pid", produtos.getPid());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View exibir = LayoutInflater.from(parent.getContext()).inflate(R.layout.produto_itens_layout, parent, false);
                ProdutoViewHolder produtoViewHolder = new ProdutoViewHolder(exibir);
                return produtoViewHolder;
            }
        };

        buscarLista.setAdapter(adapter);
        adapter.startListening();
    }
}