package com.brayansystem.analizadorsemantico.ast;

public abstract class Nodo {

    private final int linea;

    public Nodo(int linea) {
        this.linea = linea;
    }

    public int getLinea() {
        return linea;
    }

    public abstract String getDescripcion();
}