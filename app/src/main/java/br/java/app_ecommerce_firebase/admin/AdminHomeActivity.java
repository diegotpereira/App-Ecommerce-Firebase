package br.java.app_ecommerce_firebase.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.java.app_ecommerce_firebase.R;
import br.java.app_ecommerce_firebase.activities.HomeActivity;
import br.java.app_ecommerce_firebase.activities.MainActivity;

public class AdminHomeActivity extends AppCompatActivity {

    private Button VerificarPedidosBtn;
    private Button SairBtn;
    private Button ManterProdutoBtn;
    private Button VerificarAprovadoProdutosBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        VerificarPedidosBtn = findViewById(R.id.verificar_pedidos_btn);
        SairBtn = findViewById(R.id.admin_sair_btn);
        ManterProdutoBtn = findViewById(R.id.manter_btn);
        VerificarAprovadoProdutosBtn = findViewById(R.id.verificar_aprovado_produto_btn);

        ManterProdutoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, HomeActivity.class);
                intent.putExtra("Admin", "Admin");
                startActivity(intent);
            }
        });

        SairBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        VerificarPedidosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminNovoPedidoActivity.class);
                startActivity(intent);
            }
        });

        VerificarAprovadoProdutosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminVerificarNovosProdutosActivity.class);
                startActivity(intent);
            }
        });
    }
}