package br.java.app_ecommerce_firebase.sellers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import br.java.app_ecommerce_firebase.R;

public class VendedorProdutoCategoriaActivity extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendedor_produto_categoria);

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


        tCamisas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VendedorProdutoCategoriaActivity.this, VendedorAddNovoProdutoActivity.class);
                intent.putExtra("categoria", "tCamisas");
                startActivity(intent);
            }
        });

        camisasEsportivas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VendedorProdutoCategoriaActivity.this, VendedorAddNovoProdutoActivity.class);
                intent.putExtra("categoria", "Camisetas esportes");
                startActivity(intent);
            }
        });

        vestidosFemininos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VendedorProdutoCategoriaActivity.this, VendedorAddNovoProdutoActivity.class);
                intent.putExtra("categoria", "Vestidos femininos");
                startActivity(intent);
            }
        });

        sueteres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VendedorProdutoCategoriaActivity.this, VendedorAddNovoProdutoActivity.class);
                intent.putExtra("categoria", "Sueteres");
                startActivity(intent);
            }
        });

        oculos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VendedorProdutoCategoriaActivity.this, VendedorAddNovoProdutoActivity.class);
                intent.putExtra("categoria", "Oculos");
                startActivity(intent);
            }
        });

        chapeusBones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VendedorProdutoCategoriaActivity.this, VendedorAddNovoProdutoActivity.class);
                intent.putExtra("categoria", "Chapeus Bones");
                startActivity(intent);
            }
        });

        muchilasBolsasCarteiras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VendedorProdutoCategoriaActivity.this, VendedorAddNovoProdutoActivity.class);
                intent.putExtra("categoria", "Muchilas Bolsas Carteiras");
                startActivity(intent);
            }
        });

        sapatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VendedorProdutoCategoriaActivity.this, VendedorAddNovoProdutoActivity.class);
                intent.putExtra("categoria", "Sapatos");
                startActivity(intent);
            }
        });

        fonesOuvidosSemFio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VendedorProdutoCategoriaActivity.this, VendedorAddNovoProdutoActivity.class);
                intent.putExtra("categoria", "Fones sem Fio");
                startActivity(intent);
            }
        });

        Laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VendedorProdutoCategoriaActivity.this, VendedorAddNovoProdutoActivity.class);
                intent.putExtra("categoria", "Laptos");
                startActivity(intent);
            }
        });

        relogios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VendedorProdutoCategoriaActivity.this, VendedorAddNovoProdutoActivity.class);
                intent.putExtra("categoria", "Relogios");
                startActivity(intent);
            }
        });

        celulares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VendedorProdutoCategoriaActivity.this, VendedorAddNovoProdutoActivity.class);
                intent.putExtra("categoria", "Celuares");
                startActivity(intent);
            }
        });
    }
}