package com.example.tutorialfirebase.Clases;

public class Moda extends Producto{
    private Producto p;
    private String talla;
    private String color;
    private String material;
    private String sexo;
    private String categoria_moda;

    public Moda(Producto p, String talla, String color, String material, String sexo, String categoria_moda) {
        super(p.getCod_producto(), p.getCod_QR(), p.getMarca(), p.getModelo(), p.getDescripción());
        this.p = p;
        this.talla = talla;
        this.color = color;
        this.material = material;
        this.sexo = sexo;
        this.categoria_moda = categoria_moda;
    }

    public Producto getP() {
        return p;
    }

    public void setP(Producto p) {
        this.p = p;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getCategoria_moda() {
        return categoria_moda;
    }

    public void setCategoria_moda(String categoria_moda) {
        this.categoria_moda = categoria_moda;
    }

    @Override
    public String toString() {
        return "Moda{" +
                "p=" + p +
                ", talla='" + talla + '\'' +
                ", color='" + color + '\'' +
                ", material='" + material + '\'' +
                ", sexo='" + sexo + '\'' +
                ", categoria_moda='" + categoria_moda + '\'' +
                '}';
    }
}
