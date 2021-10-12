package br.java.app_ecommerce_firebase.modelo;

public class Carrinho {

    private String pid;
    private String pnome;
    private String preco;
    private String quantidade;
    private String desconto;

    public Carrinho() {
    }

    public Carrinho(String pid, String pnome, String preco, String quantidade, String desconto) {
        this.pid = pid;
        this.pnome = pnome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.desconto = desconto;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPnome() {
        return pnome;
    }

    public void setPnome(String pnome) {
        this.pnome = pnome;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    public String getDesconto() {
        return desconto;
    }

    public void setDesconto(String desconto) {
        this.desconto = desconto;
    }
}
