package br.java.app_ecommerce_firebase.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.java.app_ecommerce_firebase.Interface.ItemClickListner;
import br.java.app_ecommerce_firebase.R;

public class ProdutoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProdutoNome;
    public TextView txtProdutoDescricao;
    public TextView txtProdutoPreco;

    public ImageView exibirImagem;

    public ItemClickListner listner;

    public ProdutoViewHolder(@NonNull View itemView) {

        super(itemView);

        exibirImagem = (ImageView) itemView.findViewById(R.id.produto_imagem);

        txtProdutoNome = (TextView) itemView.findViewById(R.id.produto_nome);
        txtProdutoDescricao = (TextView) itemView.findViewById(R.id.produto_descricao);
        txtProdutoPreco = (TextView) itemView.findViewById(R.id.produto_preco);
    }

    public void setItemClickListner(ItemClickListner listner) {
        this.listner = listner;
    }
    @Override
    public void onClick(View view) {

    }
}
