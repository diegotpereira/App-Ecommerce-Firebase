package br.java.app_ecommerce_firebase.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import br.java.app_ecommerce_firebase.R;
import br.java.app_ecommerce_firebase.predominante.Predominante;

public class FinalizarPedidoActivity extends AppCompatActivity {

    private EditText nomeEditTxt;
    private EditText telefoneEditTxt;
    private EditText enderecoEditTxt;
    private EditText cidadeEditTxt;

    private Button confirmePedidoBtn;

    private TextView txtValorTotal;

    private String valorTotal = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar_pedido);

        valorTotal = getIntent().getStringExtra("Preco Total");
        Toast.makeText(this, "Preço Total = R$" + valorTotal, Toast.LENGTH_SHORT).show();

        confirmePedidoBtn = findViewById(R.id.confirme_final_pedido_btn);
        nomeEditTxt = findViewById(R.id.nome_envio);
        telefoneEditTxt = findViewById(R.id.telefone_numero_envio);
        enderecoEditTxt = findViewById(R.id.endereco_envio);
        cidadeEditTxt = findViewById(R.id.cidade_envio);
        
        confirmePedidoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                Verificar();
            }
        });
    }
    
    private void Verificar() {
        
        if (TextUtils.isEmpty(nomeEditTxt.getText().toString())) {

            Toast.makeText(this, "Por favor, forneça o seu nome completo.", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(telefoneEditTxt.getText().toString())) {

            Toast.makeText(this, "Por favor, forneça seu telefone", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(enderecoEditTxt.getText().toString())) {

            Toast.makeText(this, "Por favor, forneça seu endereço", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(cidadeEditTxt.getText().toString())) {

            Toast.makeText(this, "Por favor, forneça nome da sua cidade", Toast.LENGTH_SHORT).show();

        } else {

            ConfirmarPedido();
        }
    }

    private void ConfirmarPedido() {

        final String salvarAtualData;
        final String salvarAtualHora;

        Calendar calParaData = Calendar.getInstance();
        SimpleDateFormat atualData = new SimpleDateFormat("MMM, dd, yyyy");
        salvarAtualData = atualData.format(calParaData.getTime());

        SimpleDateFormat atualHora = new SimpleDateFormat("HH:mm:ss");
        salvarAtualHora = atualData.format(calParaData.getTime());

        final DatabaseReference pedidoRef = FirebaseDatabase.getInstance().getReference().child("Pedidos").child(Predominante.atualUsuarioOnline.getTelefone());

        HashMap<String, Object> pedidoMap = new HashMap<>();
        pedidoMap.put("valorTotal", valorTotal);
        pedidoMap.put("nome", nomeEditTxt.getText().toString());
        pedidoMap.put("telefone", telefoneEditTxt.getText().toString());
        pedidoMap.put("endereco", enderecoEditTxt.getText().toString());
        pedidoMap.put("cidade", cidadeEditTxt.getText().toString());
        pedidoMap.put("data", salvarAtualData);
        pedidoMap.put("hora", salvarAtualHora);
        pedidoMap.put("estado", "nao enviado");

        pedidoRef.updateChildren(pedidoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    FirebaseDatabase.getInstance().getReference().child("carrinho lista").child("Visualizacao do usuario").child(Predominante.atualUsuarioOnline.getTelefone()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            
                            if (task.isSuccessful()) {

                                Toast.makeText(FinalizarPedidoActivity.this, "seu pedido final foi finalizado com sucesso.", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(FinalizarPedidoActivity.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }
}