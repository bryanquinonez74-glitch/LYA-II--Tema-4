package com.brayansystem.analizadorsemantico.modelo;

public class Simbolo {

    private String nombre;
    private TipoDato tipo;
    private Object valor;
    private int linea;

    public Simbolo(String nombre, TipoDato tipo, Object valor, int linea) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = valor;
        this.linea = linea;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoDato getTipo() {
        return tipo;
    }

    public void setTipo(TipoDato tipo) {
        this.tipo = tipo;
    }

    public Object getValor() {
        return valor;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    @Override
    public String toString() {
        return "Simbolo{" +
                "nombre='" + nombre + '\'' +
                ", tipo=" + tipo +
                ", valor=" + valor +
                ", linea=" + linea +
                '}';
    }
}