package br.java.app_ecommerce_firebase.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import br.java.app_ecommerce_firebase.R;
import br.java.app_ecommerce_firebase.predominante.Predominante;

public class ResetarSenhaActivity extends AppCompatActivity {

    private TextView títuloPagina;
    private TextView questoesTitulo;

    private EditText telefoneNumero;
    private EditText questao1;
    private EditText questao2;

    private Button verificarBtn;

    private String verificar = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetar_senha);

        verificar = getIntent().getStringExtra("verificar");

        títuloPagina = findViewById(R.id.título_pagina);
        questoesTitulo = findViewById(R.id.questoes_titulo);
        telefoneNumero = findViewById(R.id.buscar_numero_telefone);
        questao1 = findViewById(R.id.questoes_1);
        questao2 = findViewById(R.id.questoes_2);
        verificarBtn = findViewById(R.id.verificar_btn);
    }

    @Override
    protected void onStart() {

        super.onStart();

        telefoneNumero.setVisibility(View.GONE);

        if (verificar.equals("definicoes")) {

            títuloPagina.setText("Definir perguntas");
            questoesTitulo.setText("Defina a resposta para as seguintes perguntas de segurança.");
            verificarBtn.setText("Definir");

            exibirRespostasAnteriores();

            verificarBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    definirRespostas();
                }
            });
        } else if (verificar.equals("login")) {

            telefoneNumero.setVisibility(View.VISIBLE);

            verificarBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    verificarUsuario();
                }
            });
        }
    }

    private void verificarUsuario() {

        String telefone = telefoneNumero.getText().toString();

        final String resposta1 = questao1.getText().toString().toLowerCase();
        final String resposta2 = questao2.getText().toString().toLowerCase();

        if (!telefone.equals("") && !resposta1.equals("") && !resposta2.equals("")) {

            final DatabaseReference verificaUsuarioRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(telefone);
            verificaUsuarioRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {

                        String mTelefone = snapshot.child("telefone").getValue().toString();

                        if (snapshot.hasChild("Questoes de seguranca")) {

                            String resp1 = snapshot.child("Questoes de seguranca").child("resposta1").getValue().toString();
                            String resp2 = snapshot.child("Questoes de seguranca").child("resposta2").getValue().toString();
                            
                            if (!resp1.equals(resposta1)) {

                                Toast.makeText(ResetarSenhaActivity.this, "sua primeira resposta está errada.", Toast.LENGTH_SHORT).show();

                            } else  if (!resp2.equals(resposta2)) {

                                Toast.makeText(ResetarSenhaActivity.this, "sua segunda resposta está errada.", Toast.LENGTH_SHORT).show();

                            } else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(ResetarSenhaActivity.this);
                                builder.setTitle("Nova Senha");

                                final EditText novaSenha = new EditText(ResetarSenhaActivity.this);
                                novaSenha.setHint("Digite sua senha aqui...");
                                builder.setView(novaSenha);

                                builder.setPositiveButton("alterar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        if (!novaSenha.getText().toString().equals("")) {

                                            verificaUsuarioRef.child("senha").setValue(novaSenha.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    Toast.makeText(ResetarSenhaActivity.this, "Senha Alterada com sucesso.", Toast.LENGTH_SHORT).show();

                                                    Intent intent = new Intent(ResetarSenhaActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                }
                                            });

                                        }
                                    }
                                });

                                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        dialogInterface.cancel();

                                    }
                                });
                                
                                builder.show();
                            }
                        } else {

                            Toast.makeText(ResetarSenhaActivity.this, "você não configurou a segurança.", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toast.makeText(ResetarSenhaActivity.this, "Este número de telefone não existe.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {

            Toast.makeText(this, "por favor preencha o formulário.", Toast.LENGTH_SHORT).show();
        }
    }

    private void definirRespostas() {

        String resposta1 = questao1.getText().toString().toLowerCase();
        String resposta2 = questao2.getText().toString().toLowerCase();
        
        if (questao1.equals("") && questao2.equals("")) {

            Toast.makeText(ResetarSenhaActivity.this, "Por favor, responda a ambas as perguntas.", Toast.LENGTH_SHORT).show();

        } else {

            DatabaseReference resetarRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(Predominante.atualUsuarioOnline.getTelefone());

            HashMap<String, Object> usuarioDadoMapa = new HashMap<>();
            usuarioDadoMapa.put("resposta1", resposta1);
            usuarioDadoMapa.put("resposta2", resposta2);

            resetarRef.child("Questoes de seguranca").updateChildren(usuarioDadoMapa).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    
                    if (task.isSuccessful()) {

                        Toast.makeText(ResetarSenhaActivity.this, "você definiu suas questões com sucesso", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ResetarSenhaActivity.this, HomeActivity.class);
                        startActivity(intent);

                    }
                }
            });
        }

    }

    private void exibirRespostasAnteriores() {

        DatabaseReference respostaRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(Predominante.atualUsuarioOnline.getTelefone());

        respostaRef.child("Questoes de seguranca").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    String resp1 = snapshot.child("resposta1").getValue().toString();
                    String resp2 = snapshot.child("resposta2").getValue().toString();

                    questao1.setText(resp1);
                    questao2.setText(resp2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}