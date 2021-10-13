package br.java.app_ecommerce_firebase.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.java.app_ecommerce_firebase.R;
import br.java.app_ecommerce_firebase.ViewHolder.CarrinhoViewHolder;
import br.java.app_ecommerce_firebase.modelo.Carrinho;

public class AdminUsuarioProdutosActivity extends AppCompatActivity {

    private RecyclerView produtosLista;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference carrinhoListaRef;

    private String usuarioID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_usuario_produtos);

        usuarioID = getIntent().getStringExtra("uid");

        produtosLista = findViewById(R.id.produtos_lista);
        produtosLista.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        produtosLista.setLayoutManager(layoutManager);

        carrinhoListaRef = FirebaseDatabase.getInstance().getReference().child("carrinho lista").child("Visualizacao de administrador").child(usuarioID).child("Produtos");
    }

    @Override
    protected void onStart() {

        super.onStart();

        FirebaseRecyclerOptions<Carrinho> opcoes = new FirebaseRecyclerOptions.Builder<Carrinho>().setQuery(carrinhoListaRef, Carrinho.class).build();

        FirebaseRecyclerAdapter<Carrinho, CarrinhoViewHolder> adapter = new FirebaseRecyclerAdapter<Carrinho, CarrinhoViewHolder>(opcoes) {
            @Override
            protected void onBindViewHolder(@NonNull CarrinhoViewHolder carrinhoViewHolder, int i, @NonNull Carrinho carrinho) {

                carrinhoViewHolder.txtProdutoQuantidade.setText("Quantidade = " + carrinho.getQuantidade());
                carrinhoViewHolder.txtProdutoPreco.setText("Preço = " + "R$ " + carrinho.getPreco());
                carrinhoViewHolder.txtProdutoNome.setText(carrinho.getPnome());
            }

            @NonNull
            @Override
            public CarrinhoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View exibir = LayoutInflater.from(parent.getContext()).inflate(R.layout.carrinho_itens, parent, false);
                CarrinhoViewHolder carrinhoViewHolder = new CarrinhoViewHolder(exibir);
                return carrinhoViewHolder;
            }
        };

        produtosLista.setAdapter(adapter);
        adapter.startListening();
    }
}