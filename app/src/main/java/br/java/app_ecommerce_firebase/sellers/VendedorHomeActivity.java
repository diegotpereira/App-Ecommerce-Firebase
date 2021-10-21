package br.java.app_ecommerce_firebase.sellers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import br.java.app_ecommerce_firebase.R;
import br.java.app_ecommerce_firebase.ViewHolder.ItemViewHolder;
import br.java.app_ecommerce_firebase.activities.MainActivity;
import br.java.app_ecommerce_firebase.modelo.Produtos;


public class VendedorHomeActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference naoverificProdutosRef;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendedor_home);

        BottomNavigationView navView = findViewById(R.id.nav_btn);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationıtemSelectedListener);

        naoverificProdutosRef = FirebaseDatabase.getInstance().getReference().child("Produtos");

//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_add, R.id.navigation_logout)
//                .build();


        recyclerView = findViewById(R.id.vendedor_home_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationıtemSelectedListener = menuItem -> {
        switch(menuItem.getItemId())
        {
            case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
//                    return true;
                Intent intentHome = new Intent(VendedorHomeActivity.this, VendedorHomeActivity.class);
                startActivity(intentHome);
                return true;

            case R.id.navigation_add:
                Intent intentCate = new Intent(VendedorHomeActivity.this, VendedorProdutoCategoriaActivity.class);
                startActivity(intentCate);
                return true;

            case R.id.navigation_logout:
                final FirebaseAuth mAuth;
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();

                Intent intentMain = new Intent(VendedorHomeActivity.this, MainActivity.class);
                intentMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentMain);
                finish();
                return true;
        }
        return false;
    };
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Produtos> opcoes = new FirebaseRecyclerOptions.Builder<Produtos>().setQuery(naoverificProdutosRef.orderByChild("vID").equalTo(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()), Produtos.class).build();

        FirebaseRecyclerAdapter<Produtos, ItemViewHolder> adapter = new FirebaseRecyclerAdapter<Produtos, ItemViewHolder>(opcoes) {
            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder produtoViewHolder, int position, @NonNull final Produtos produtos) {

                produtoViewHolder.txtProdutoNome.setText(produtos.getPnome());
                produtoViewHolder.txtProdutoDescricao.setText("Estado : " + produtos.getDescricao());
                produtoViewHolder.txtProdutoPreco.setText("Preço = " + produtos.getPreco());
                produtoViewHolder.txtProdutoEstado.setText(produtos.getProdutoEstado());
                Picasso.get().load(produtos.getImagem()).into(produtoViewHolder.exibirImagem);

//                final Produtos itemClick = produtos;

               produtoViewHolder.itemView.setOnClickListener(view -> {

                   final String produtoID = produtos.getPid();

                   CharSequence[] opcoes1 = new CharSequence[] {

                           "Sim",
                           "Não"
                   };

                   AlertDialog.Builder builder = new AlertDialog.Builder(VendedorHomeActivity.this);
                   builder.setTitle("Deseja Deletar Este Produto. Tem Certeza?");
                   builder.setItems(opcoes1, (dialogInterface, position1) -> {

                       if (position1 == 0) {

                           deletarProduto(produtoID);
                       }

                       if (position1 == 1) {

                       }
                   });

                   builder.show();

               });
            }

            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View exibir = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendedor_item_exibir, parent, false);
                return new ItemViewHolder(exibir);

            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void deletarProduto(String produtoID) {

//        naoverificProdutosRef.child(produtoID).child("produtoEstado").setValue("Aprovado").addOnCompleteListener(task -> Toast.makeText(VendedorHomeActivity.this, "Esse Item foi Excluído com Sucesso.", Toast.LENGTH_SHORT).show());
        naoverificProdutosRef.child(produtoID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(VendedorHomeActivity.this, "O item foi excluído com sucesso.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}