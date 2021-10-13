package br.java.app_ecommerce_firebase.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import br.java.app_ecommerce_firebase.MainActivity;
import br.java.app_ecommerce_firebase.R;

public class AdminCategoriaActivity extends AppCompatActivity {

    private ImageView tCamisas;
    private ImageView camisasEsportivas;
    private ImageView vestidosFemininos;
    private ImageView sueteres;

    private ImageView oculos;
    private ImageView chapeusBones;
    private ImageView muchilasBolsasCarteiras;
    private ImageView sapatos;

    private ImageView fonesOuvidosSemFio;
    private ImageView Laptops;
    private ImageView relogios;
    private ImageView celulares;

    private Button VerificarPedidosBtn;
    private Button SairBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_categoria);

        tCamisas = (ImageView) findViewById(R.id.t_camisas);
        camisasEsportivas = (ImageView) findViewById(R.id.esportes_t_camisas);
        vestidosFemininos = (ImageView) findViewById(R.id.vestidos_femininos);
        sueteres = (ImageView) findViewById(R.id.sueteres);

        oculos = (ImageView) findViewById(R.id.oculos);
        chapeusBones = (ImageView) findViewById(R.id.chapeus_bones);
        muchilasBolsasCarteiras = (ImageView) findViewById(R.id.muchilas_bolsas_carteiras);
        sapatos = (ImageView) findViewById(R.id.sapatos);

        fonesOuvidosSemFio = (ImageView) findViewById(R.id.fones_ouvidos_sem_fio);
        Laptops = (ImageView) findViewById(R.id.laptop_pc);
        relogios = (ImageView) findViewById(R.id.relogios);
        celulares = (ImageView) findViewById(R.id.celulares);

        VerificarPedidosBtn = findViewById(R.id.verificar_pedidos_btn);
        SairBtn = findViewById(R.id.admin_sair_btn);

        SairBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoriaActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        VerificarPedidosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoriaActivity.this, AdminNovoPedidoActivity.class);
                startActivity(intent);
            }
        });

        tCamisas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoriaActivity.this, AdminAddNovoProdutoActivity.class);
                intent.putExtra("categoria", "tCamisas");
                startActivity(intent);
            }
        });

        camisasEsportivas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoriaActivity.this, AdminAddNovoProdutoActivity.class);
                intent.putExtra("categoria", "Camisetas esportes");
                startActivity(intent);
            }
        });

        vestidosFemininos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoriaActivity.this, AdminAddNovoProdutoActivity.class);
                intent.putExtra("categoria", "Vestidos femininos");
                startActivity(intent);
            }
        });

        sueteres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoriaActivity.this, AdminAddNovoProdutoActivity.class);
                intent.putExtra("categoria", "Sueteres");
                startActivity(intent);
            }
        });

        oculos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoriaActivity.this, AdminAddNovoProdutoActivity.class);
                intent.putExtra("categoria", "Oculos");
                startActivity(intent);
            }
        });

        chapeusBones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoriaActivity.this, AdminAddNovoProdutoActivity.class);
                intent.putExtra("categoria", "Chapeus Bones");
                startActivity(intent);
            }
        });

        muchilasBolsasCarteiras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoriaActivity.this, AdminAddNovoProdutoActivity.class);
                intent.putExtra("categoria", "Muchilas Bolsas Carteiras");
                startActivity(intent);
            }
        });

        sapatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoriaActivity.this, AdminAddNovoProdutoActivity.class);
                intent.putExtra("categoria", "Sapatos");
                startActivity(intent);
            }
        });

        fonesOuvidosSemFio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoriaActivity.this, AdminAddNovoProdutoActivity.class);
                intent.putExtra("categoria", "Fones sem Fio");
                startActivity(intent);
            }
        });

        Laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoriaActivity.this, AdminAddNovoProdutoActivity.class);
                intent.putExtra("categoria", "Laptos");
                startActivity(intent);
            }
        });

        relogios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoriaActivity.this, AdminAddNovoProdutoActivity.class);
                intent.putExtra("categoria", "Relogios");
                startActivity(intent);
            }
        });

        celulares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoriaActivity.this, AdminAddNovoProdutoActivity.class);
                intent.putExtra("categoria", "Celuares");
                startActivity(intent);
            }
        });
    }
}