package br.java.app_ecommerce_firebase.modelo;

public class Produtos {

    private String pnome;
    private String descricao;
    private String preco;
    private String imagem;
    private String categoria;
    private String produtoEstado;
    private String pid;
    private String data;
    private String hora;

    public Produtos() {
    }

    public Produtos(String pnome, String descricao, String preco, String imagem, String categoria, String pid, String data, String hora, String produtoEstado) {
        this.pnome = pnome;
        this.descricao = descricao;
        this.preco = preco;
        this.imagem = imagem;
        this.categoria = categoria;
        this.pid = pid;
        this.data = data;
        this.hora = hora;
        this.produtoEstado = produtoEstado;
    }

    public String getPnome() {
        return pnome;
    }

    public void setPnome(String pnome) {
        this.pnome = pnome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getProdutoEstado() {
        return produtoEstado;
    }

    public void setProdutoEstado(String produtoEstado) {
        this.produtoEstado = produtoEstado;
    }
}
