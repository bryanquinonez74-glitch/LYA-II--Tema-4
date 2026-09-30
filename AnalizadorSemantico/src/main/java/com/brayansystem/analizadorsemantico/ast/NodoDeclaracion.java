package com.brayansystem.analizadorsemantico.ast;

import com.brayansystem.analizadorsemantico.modelo.TipoDato;

public class NodoDeclaracion extends Nodo {

    private final String nombre;
    private final TipoDato tipo;
    private final Nodo valor;

    public NodoDeclaracion(
            String nombre,
            TipoDato tipo,
            Nodo valor,
            int linea) {

        super(linea);
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = valor;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoDato getTipo() {
        return tipo;
    }

    public Nodo getValor() {
        return valor;
    }

    @Override
    public String getDescripcion() {

        if (valor == null) {
            return tipo + " " + nombre;
        }

        return tipo + " "
                + nombre
                + " = "
                + valor.getDescripcion();
    }
}