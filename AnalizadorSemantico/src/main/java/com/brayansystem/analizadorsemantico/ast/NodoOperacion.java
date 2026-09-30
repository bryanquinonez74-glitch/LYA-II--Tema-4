package com.brayansystem.analizadorsemantico.ast;

public class NodoOperacion extends Nodo {

    private final Nodo izquierdo;
    private final String operador;
    private final Nodo derecho;

    public NodoOperacion(
            Nodo izquierdo,
            String operador,
            Nodo derecho,
            int linea) {

        super(linea);

        this.izquierdo = izquierdo;
        this.operador = operador;
        this.derecho = derecho;
    }

    public Nodo getIzquierdo() {
        return izquierdo;
    }

    public String getOperador() {
        return operador;
    }

    public Nodo getDerecho() {
        return derecho;
    }

    @Override
    public String getDescripcion() {

        return izquierdo.getDescripcion()
                + " "
                + operador
                + " "
                + derecho.getDescripcion();
    }
}