package br.java.app_ecommerce_firebase.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import br.java.app_ecommerce_firebase.R;
import br.java.app_ecommerce_firebase.ViewHolder.ProdutoViewHolder;
import br.java.app_ecommerce_firebase.admin.AdminManterProdutosActivity;
import br.java.app_ecommerce_firebase.modelo.Produtos;
import br.java.app_ecommerce_firebase.predominante.Predominante;
import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference ProdutosRef;
    private RecyclerView recyclerView;

    RecyclerView.LayoutManager layoutManager;

    private String tipo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            tipo = getIntent().getExtras().get("Admin").toString();
        }

        ProdutosRef = FirebaseDatabase.getInstance().getReference().child("Produtos");

        Paper.init(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Substitua por sua própria ação", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show(); */
                if (!tipo.equals("Admin")) {
                    Intent intent = new Intent(HomeActivity.this, CarrinhoActivity.class);
                    startActivity(intent);
                }
            }
        });

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View vistaCabecalho = navigationView.getHeaderView(0);
        TextView visualizacaoTextoNomeUsuario = vistaCabecalho.findViewById(R.id.nome_perfil_usuario);
        CircleImageView perfilVisualizacaoImagem = vistaCabecalho.findViewById(R.id.imagem_perfil_usuário);

        if (!tipo.equals("Admin")) {

            visualizacaoTextoNomeUsuario.setText(Predominante.atualUsuarioOnline.getNome());
            Picasso.get().load(Predominante.atualUsuarioOnline.getImagem()).placeholder(R.drawable.profile).into(perfilVisualizacaoImagem);
        }

        recyclerView = findViewById(R.id.reciclar_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Produtos> options = new FirebaseRecyclerOptions.Builder<Produtos>().setQuery(ProdutosRef.orderByChild("produtoEstado").equalTo("Aprovado"), Produtos.class).build();

        FirebaseRecyclerAdapter<Produtos, ProdutoViewHolder> adapter = new FirebaseRecyclerAdapter<Produtos, ProdutoViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProdutoViewHolder holder, int position, @NonNull Produtos model) {

                holder.txtProdutoNome.setText(model.getPnome());
                holder.txtProdutoDescricao.setText(model.getDescricao());
                holder.txtProdutoPreco.setText("Preço =" + model.getPreco());
                Picasso.get().load(model.getImagem()).into(holder.exibirImagem);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (tipo.equals("Admin")) {

                            Intent intent = new Intent(HomeActivity.this, AdminManterProdutosActivity.class);
                            intent.putExtra("pid", model.getPid());
                            startActivity(intent);

                        } else {

                            Intent intent = new Intent(HomeActivity.this, ProdutoDetalhesActivity.class);
                            intent.putExtra("pid", model.getPid());
                            startActivity(intent);
                        }
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

//        if (id == R.id.action_settings) {
//
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_carrinho) {

            Intent intent = new Intent(HomeActivity.this, CarrinhoActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_buscar) {

            if (!tipo.equals("Admin")) {

                Intent intent = new Intent(HomeActivity.this, BuscarProdutosActivity.class);
                startActivity(intent);

            }

        } else if (id == R.id.nav_categorias) {

        } else if (id == R.id.nav_configuracoes) {

            if (!tipo.equals("Admin")) {

                Intent intent = new Intent(HomeActivity.this, ConfiguracoesActivity.class);
                startActivity(intent);

            }
        } else if (id == R.id.nav_sair) {

          if (!tipo.equals("Admin")) {

              Paper.book().destroy();

              Intent intent = new Intent(HomeActivity.this, MainActivity.class);
              intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
              startActivity(intent);
              finish();
          }

        }

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}