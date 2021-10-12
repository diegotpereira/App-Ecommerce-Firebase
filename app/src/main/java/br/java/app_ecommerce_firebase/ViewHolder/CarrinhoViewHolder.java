package br.java.app_ecommerce_firebase.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.java.app_ecommerce_firebase.Interface.ItemClickListener;


import br.java.app_ecommerce_firebase.R;

public class CarrinhoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProdutoNome;
    public TextView txtProdutoPreco;
    public TextView txtProdutoQuantidade;

    private ItemClickListener itemClickListener;

    public CarrinhoViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProdutoNome = itemView.findViewById(R.id.carrinho_produto_nome);
        txtProdutoPreco = itemView.findViewById(R.id.carrinho_produto_preco);
        txtProdutoQuantidade = itemView.findViewById(R.id.carrinho_produto_quantidade);

    }

    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }
}
