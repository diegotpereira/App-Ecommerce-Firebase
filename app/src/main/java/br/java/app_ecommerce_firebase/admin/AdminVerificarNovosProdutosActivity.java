package br.java.app_ecommerce_firebase.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import br.java.app_ecommerce_firebase.Interface.ItemClickListener;
import br.java.app_ecommerce_firebase.R;
import br.java.app_ecommerce_firebase.ViewHolder.ProdutoViewHolder;
import br.java.app_ecommerce_firebase.modelo.Produtos;

public class AdminVerificarNovosProdutosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private DatabaseReference verificProdutosRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_verificar_novos_produtos);

        verificProdutosRef = FirebaseDatabase.getInstance().getReference().child("Produtos");

        recyclerView = findViewById(R.id.admin_produtos_verificarLista);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Produtos> opcoes = new FirebaseRecyclerOptions.Builder<Produtos>().setQuery(verificProdutosRef.orderByChild("produtoEstado").equalTo("Não aprovado"), Produtos.class).build();

        FirebaseRecyclerAdapter<Produtos, ProdutoViewHolder> adapter = new FirebaseRecyclerAdapter<Produtos, ProdutoViewHolder>(opcoes) {
            @Override
            protected void onBindViewHolder(@NonNull ProdutoViewHolder produtoViewHolder, int i, @NonNull Produtos produtos) {

                produtoViewHolder.txtProdutoNome.setText(produtos.getPnome());
                produtoViewHolder.txtProdutoDescricao.setText(produtos.getDescricao());
                produtoViewHolder.txtProdutoPreco.setText("Preço = " + produtos.getPreco());
                Picasso.get().load(produtos.getImagem()).into(produtoViewHolder.exibirImagem);

                final Produtos itemClick = produtos;

                produtoViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final String produtoID = produtos.getPid();

                        CharSequence opcoes [] = new CharSequence[] {

                                "Sim",
                                "Não"
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminVerificarNovosProdutosActivity.this);
                        builder.setTitle("Deseja Aprovar Este Produto. Tem Certeza?");
                        builder.setItems(opcoes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int position) {

                                if (position == 0) {

                                    MudarEstadoProduto(produtoID);
                                }

                                if (position == 1) {

                                }
                            }
                        });

                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View exibir = LayoutInflater.from(parent.getContext()).inflate(R.layout.produto_itens_layout, parent, false);
                ProdutoViewHolder holder = new ProdutoViewHolder(exibir);
                return holder;

            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void MudarEstadoProduto(String produtoID) {

        verificProdutosRef.child(produtoID).child("produtoEstado").setValue("Aprovado").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(AdminVerificarNovosProdutosActivity.this, "esse item foi aprovado e agora está disponível para venda no vendedor.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}