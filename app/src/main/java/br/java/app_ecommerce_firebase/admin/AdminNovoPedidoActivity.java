package br.java.app_ecommerce_firebase.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.java.app_ecommerce_firebase.R;
import br.java.app_ecommerce_firebase.modelo.AdminPedidos;

public class AdminNovoPedidoActivity extends AppCompatActivity {

    private RecyclerView pedidosLista;
    private DatabaseReference pedidosRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_novo_pedido);

        pedidosRef = FirebaseDatabase.getInstance().getReference().child("Pedidos");

        pedidosLista = findViewById(R.id.pedidos_lista);
        pedidosLista.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminPedidos> opcoes = new FirebaseRecyclerOptions.Builder<AdminPedidos>().setQuery(pedidosRef, AdminPedidos.class).build();

        FirebaseRecyclerAdapter<AdminPedidos, AdminPedidosViewHolder> adapter = new FirebaseRecyclerAdapter<AdminPedidos, AdminPedidosViewHolder>(opcoes) {
            @Override
            protected void onBindViewHolder(@NonNull AdminPedidosViewHolder adminPedidosViewHolder, @SuppressLint("RecyclerView") int position, @NonNull AdminPedidos adminPedidos) {

                adminPedidosViewHolder.usuarioNome.setText("Nome: " + adminPedidos.getNome());
                adminPedidosViewHolder.usuarioTelefoneNumero.setText("Telefone: " + adminPedidos.getTelefone());
                adminPedidosViewHolder.usuarioTotalPreco.setText("Valor Total: " + adminPedidos.getValorTotal());
                adminPedidosViewHolder.usuarioDataHora.setText("Pedido em: " + adminPedidos.getHora());
                adminPedidosViewHolder.usuarioEnderecoEntrega.setText("Endereço de entrega: " + adminPedidos.getCidade());

                adminPedidosViewHolder.exibirPedidosBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String uID = getRef(position).getKey();

                        Intent intent = new Intent(AdminNovoPedidoActivity.this, AdminUsuarioProdutosActivity.class);
                        intent.putExtra("uid", uID);
                        startActivity(intent);
                    }
                });

                adminPedidosViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        CharSequence[] opcoes = new CharSequence[]
                                {
                                        "Sim",
                                        "Não"
                                };

                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminNovoPedidoActivity.this);
                        builder.setTitle("Você enviou os produtos deste pedido?");

                        builder.setItems(opcoes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if (i == 0) {

                                    String uID = getRef(position).getKey();

                                    RemoverPedido(uID);
                                } else {

                                    finish();
                                }
                            }
                        });

                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public AdminPedidosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

                View exibir = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pedidos_layout, viewGroup, false);

                return new AdminPedidosViewHolder(exibir);
            }
        };

        pedidosLista.setAdapter(adapter);
        adapter.startListening();
    }

    private void RemoverPedido(String uID) {

        pedidosRef.child(uID).removeValue();

    }
}