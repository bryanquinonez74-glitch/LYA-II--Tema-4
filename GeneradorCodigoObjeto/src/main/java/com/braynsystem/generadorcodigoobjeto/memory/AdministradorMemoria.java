package com.braynsystem.generadorcodigoobjeto.memory;

import com.braynsystem.generadorcodigoobjeto.model.Variable;

import java.util.ArrayList;
import java.util.List;

public class AdministradorMemoria {

    private int siguienteDireccion = 1000;

    private final List<Variable> variables = new ArrayList<>();

    public Variable reservar(String nombre, String tipo) {

        Variable variable = new Variable(
                nombre,
                tipo,
                siguienteDireccion
        );

        variables.add(variable);

        siguienteDireccion += 4;

        return variable;
    }

    public List<Variable> obtenerVariables() {
        return variables;
    }

    public void limpiar() {
        variables.clear();
        siguienteDireccion = 1000;
    }
}