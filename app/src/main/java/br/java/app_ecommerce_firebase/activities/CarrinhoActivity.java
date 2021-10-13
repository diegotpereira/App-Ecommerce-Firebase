package br.java.app_ecommerce_firebase.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.java.app_ecommerce_firebase.R;
import br.java.app_ecommerce_firebase.ViewHolder.CarrinhoViewHolder;
import br.java.app_ecommerce_firebase.modelo.Carrinho;
import br.java.app_ecommerce_firebase.predominante.Predominante;

public class CarrinhoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private Button ProximoProcessoBtn;

    private TextView txtvalorTotal;
    private TextView txtMsg1;

    private int valorFinal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        recyclerView = findViewById(R.id.carrinho_lista);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ProximoProcessoBtn = findViewById(R.id.proximo_btn);
        txtvalorTotal = findViewById(R.id.preco_total);
        txtMsg1 = findViewById(R.id.msg1);

        ProximoProcessoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                txtvalorTotal.setText("Preco Total = R$." + valorFinal);

                VerificarEstadoPedido();

                Intent intent = new Intent(CarrinhoActivity.this, FinalizarPedidoActivity.class);
                intent.putExtra("Preco Total", String.valueOf(valorFinal));
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        VerificarEstadoPedido();

        final DatabaseReference carrinhoListaRef = FirebaseDatabase.getInstance().getReference().child("carrinho lista");

        FirebaseRecyclerOptions<Carrinho> opcoes = new FirebaseRecyclerOptions.Builder<Carrinho>().setQuery(carrinhoListaRef.child("Visualizacao de administrador").child(Predominante.atualUsuarioOnline.getTelefone()).child("Produtos"), Carrinho.class).build();

        FirebaseRecyclerAdapter<Carrinho, CarrinhoViewHolder> adapter = new FirebaseRecyclerAdapter<Carrinho, CarrinhoViewHolder>(opcoes) {
            @Override
            protected void onBindViewHolder(@NonNull CarrinhoViewHolder carrinhoViewHolder, int i, @NonNull Carrinho carrinho) {

                carrinhoViewHolder.txtProdutoNome.setText(carrinho.getPnome());
                carrinhoViewHolder.txtProdutoPreco.setText("Preço = " + "Rp." + carrinho.getPreco());
                carrinhoViewHolder.txtProdutoQuantidade.setText("Quantidade = " + carrinho.getQuantidade());

                int umTipoProdutoTPreco = ((Integer.valueOf(carrinho.getPreco()))) * Integer.valueOf(carrinho.getQuantidade());
                valorFinal = valorFinal + umTipoProdutoTPreco;

                carrinhoViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        CharSequence[] opcoes = new CharSequence[]
                                {
                                        "Editar",
                                        "Remover"
                                };

                        AlertDialog.Builder builder = new AlertDialog.Builder(CarrinhoActivity.this);
                        builder.setTitle("Opções de Carrinho");

                        builder.setItems(opcoes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if (i == 0) {

                                    Intent intent = new Intent(CarrinhoActivity.this, ProdutoDetalhesActivity.class);
                                    intent.putExtra("pid", carrinho.getPid());
                                    startActivity(intent);
                                }

                                if (i == 1) {

                                    carrinhoListaRef.child("Visualizacao de Usuario").child(Predominante.atualUsuarioOnline.getTelefone()).child("Produtos").child("Produtos").child(carrinho.getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {

                                                Toast.makeText(CarrinhoActivity.this, "Item removido com sucesso", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(CarrinhoActivity.this, HomeActivity.class);
                                                startActivity(intent);

                                            }
                                        }
                                    });
                                }
                            }
                        });

                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CarrinhoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carrinho_itens, parent, false);
                CarrinhoViewHolder holder = new CarrinhoViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void VerificarEstadoPedido() {

        DatabaseReference pedidosRef;
        pedidosRef = FirebaseDatabase.getInstance().getReference().child("Pedidos").child(Predominante.atualUsuarioOnline.getTelefone());

        pedidosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    String estadoEnvio = snapshot.child("estado").getValue().toString();
                    String usuarioNome = snapshot.child("nome").getValue().toString();

                    if (estadoEnvio.equals("enviado")) {

                        txtvalorTotal.setText("Querido " + usuarioNome + "\n seu pedido foi enviado com sucesso.");
                        recyclerView.setVisibility(View.GONE);

                        txtMsg1.setVisibility(View.VISIBLE);
                        txtMsg1.setText("parabéns, seu pedido foi enviado com sucesso. Em breve você receberá seu pedido em sua porta.");
                        ProximoProcessoBtn.setVisibility(View.GONE);

                        Toast.makeText(CarrinhoActivity.this, "você pode comprar mais produtos, assim que finalizar seu pedido.", Toast.LENGTH_SHORT).show();
                    } else {

                        txtvalorTotal.setText("Estado de envio = Nao enviado");
                        recyclerView.setVisibility(View.GONE);

                        txtMsg1.setVisibility(View.VISIBLE);
                        ProximoProcessoBtn.setVisibility(View.GONE);

                        Toast.makeText(CarrinhoActivity.this, "você pode comprar mais produtos, assim que receber seu primeiro pedido finalizado.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}