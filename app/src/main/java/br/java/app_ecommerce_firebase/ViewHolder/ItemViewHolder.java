package br.java.app_ecommerce_firebase.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.java.app_ecommerce_firebase.Interface.ItemClickListener;
import br.java.app_ecommerce_firebase.R;


public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProdutoNome;
    public TextView txtProdutoDescricao;
    public TextView txtProdutoPreco;
    public TextView txtProdutoEstado;

    public ImageView exibirImagem;

    public ItemClickListener listner;

    public ItemViewHolder(@NonNull View itemView) {

        super(itemView);

        exibirImagem = (ImageView) itemView.findViewById(R.id.produto_vendedor_imagem);
        txtProdutoNome = (TextView) itemView.findViewById(R.id.produto_vendedor_nome);
        txtProdutoDescricao = (TextView) itemView.findViewById(R.id.produto_vendedor_descricao);
        txtProdutoPreco = (TextView) itemView.findViewById(R.id.produto_vendedor_preco);
        txtProdutoEstado = (TextView) itemView.findViewById(R.id.produto_vendedor_estado);
    }

    public void setItemClickListner(ItemClickListener listner) {
        this.listner = listner;
    }

    @Override
    public void onClick(View view) {

        listner.onClick(view, getAbsoluteAdapterPosition(), false);
    }
}