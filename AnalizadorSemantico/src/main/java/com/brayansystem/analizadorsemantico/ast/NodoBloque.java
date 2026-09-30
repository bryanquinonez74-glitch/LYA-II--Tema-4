package com.brayansystem.analizadorsemantico.ast;

import java.util.ArrayList;
import java.util.List;

public class NodoBloque extends Nodo {

    private final List<Nodo> sentencias =
            new ArrayList<>();

    public NodoBloque(int linea) {
        super(linea);
    }

    public void agregar(Nodo nodo) {
        sentencias.add(nodo);
    }

    public List<Nodo> getSentencias() {
        return sentencias;
    }

    @Override
    public String getDescripcion() {

        StringBuilder resultado =
                new StringBuilder();

        resultado.append("{\n");

        for (Nodo nodo : sentencias) {
            resultado.append("  ")
                    .append(nodo.getDescripcion())
                    .append("\n");
        }

        resultado.append("}");

        return resultado.toString();
    }
}