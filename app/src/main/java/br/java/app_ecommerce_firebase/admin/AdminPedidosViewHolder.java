package br.java.app_ecommerce_firebase.admin;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import br.java.app_ecommerce_firebase.R;

public class AdminPedidosViewHolder extends RecyclerView.ViewHolder {

    public TextView usuarioNome;
    public TextView usuarioTelefoneNumero;
    public TextView usuarioTotalPreco;
    public TextView usuarioDataHora;
    public TextView usuarioEnderecoEntrega;
    public Button exibirPedidosBtn;

    public AdminPedidosViewHolder(@NonNull View itemView) {
        super(itemView);

        usuarioNome = itemView.findViewById(R.id.pedido_usuario_nome);
        usuarioTelefoneNumero = itemView.findViewById(R.id.pedido_telefone_numero);
        usuarioTotalPreco = itemView.findViewById(R.id.pedido_total_preco);
        usuarioDataHora = itemView.findViewById(R.id.pedido_data_hora);
        usuarioEnderecoEntrega = itemView.findViewById(R.id.pedido_endereco_cidade);

        exibirPedidosBtn = itemView.findViewById(R.id.exibir_todos_produtos_btn);

    }
}
