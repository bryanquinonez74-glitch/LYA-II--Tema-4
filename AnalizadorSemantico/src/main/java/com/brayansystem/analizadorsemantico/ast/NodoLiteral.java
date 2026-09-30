package com.brayansystem.analizadorsemantico.ast;

import com.brayansystem.analizadorsemantico.modelo.TipoDato;

public class NodoLiteral extends Nodo {

    private final Object valor;
    private final TipoDato tipo;

    public NodoLiteral(
            Object valor,
            TipoDato tipo,
            int linea) {

        super(linea);
        this.valor = valor;
        this.tipo = tipo;
    }

    public Object getValor() {
        return valor;
    }

    public TipoDato getTipo() {
        return tipo;
    }

    @Override
    public String getDescripcion() {
        return String.valueOf(valor);
    }
}