package com.brayansystem.analizadorsemantico.modelo;

public class Token {

    private String lexema;
    private String tipo;
    private int linea;
    private int columna;

    public Token(String lexema, String tipo, int linea, int columna) {
        this.lexema = lexema;
        this.tipo = tipo;
        this.linea = linea;
        this.columna = columna;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    @Override
    public String toString() {
        return "Token{" +
                "lexema='" + lexema + '\'' +
                ", tipo='" + tipo + '\'' +
                ", linea=" + linea +
                ", columna=" + columna +
                '}';
    }
}