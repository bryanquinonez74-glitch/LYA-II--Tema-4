package com.brayansystem.analizadorsemantico.semantico;

import com.brayansystem.analizadorsemantico.modelo.Simbolo;

import java.util.ArrayList;
import java.util.List;

public class TablaSimbolos {

    private final List<Simbolo> simbolos;

    public TablaSimbolos() {
        simbolos = new ArrayList<>();
    }

    public void agregar(Simbolo simbolo) {

        if (simbolo != null) {
            simbolos.add(simbolo);
        }
    }

    public boolean existe(String nombre) {

        if (nombre == null) {
            return false;
        }

        return simbolos.stream()
                .anyMatch(simbolo ->
                        simbolo.getNombre().equals(nombre));
    }

    public Simbolo buscar(String nombre) {

        if (nombre == null) {
            return null;
        }

        for (Simbolo simbolo : simbolos) {

            if (simbolo.getNombre().equals(nombre)) {
                return simbolo;
            }
        }

        return null;
    }

    public List<Simbolo> getSimbolos() {
        return new ArrayList<>(simbolos);
    }

    public void limpiar() {
        simbolos.clear();
    }

    public int tamaño() {
        return simbolos.size();
    }

    public boolean estaVacia() {
        return simbolos.isEmpty();
    }

    @Override
    public String toString() {
        return simbolos.toString();
    }
}