package com.brayansystem.analizadorsemantico.semantico;

import com.brayansystem.analizadorsemantico.ast.Nodo;
import com.brayansystem.analizadorsemantico.ast.NodoAsignacion;
import com.brayansystem.analizadorsemantico.ast.NodoDeclaracion;
import com.brayansystem.analizadorsemantico.ast.NodoIdentificador;
import com.brayansystem.analizadorsemantico.ast.NodoIf;
import com.brayansystem.analizadorsemantico.ast.NodoLiteral;
import com.brayansystem.analizadorsemantico.ast.NodoOperacion;
import com.brayansystem.analizadorsemantico.modelo.ErrorSemantico;
import com.brayansystem.analizadorsemantico.modelo.Simbolo;
import com.brayansystem.analizadorsemantico.modelo.Token;
import com.brayansystem.analizadorsemantico.modelo.TipoDato;
import com.brayansystem.analizadorsemantico.parser.AnalizadorSintactico;

import java.util.ArrayList;
import java.util.List;

public class AnalizadorSemantico {

    private final TablaSimbolos tablaSimbolos;
    private final List<ErrorSemantico> errores;

    public AnalizadorSemantico() {

        tablaSimbolos = new TablaSimbolos();
        errores = new ArrayList<>();
    }

    public void analizar(List<Token> tokens) {

        errores.clear();
        tablaSimbolos.limpiar();

        if (tokens == null || tokens.isEmpty()) {
            return;
        }

        AnalizadorSintactico parser =
                new AnalizadorSintactico();

        List<Nodo> nodos =
                parser.analizar(tokens);

        if (!parser.getErrores().isEmpty()) {

            for (String error : parser.getErrores()) {

                errores.add(
                        new ErrorSemantico(
                                error,
                                0
                        )
                );
            }

            return;
        }

        for (Nodo nodo : nodos) {
            analizarNodo(nodo);
        }
    }

    private void analizarNodo(Nodo nodo) {

        if (nodo instanceof NodoDeclaracion) {

            analizarDeclaracion(
                    (NodoDeclaracion) nodo
            );

            return;
        }

        if (nodo instanceof NodoAsignacion) {

            analizarAsignacion(
                    (NodoAsignacion) nodo
            );

            return;
        }

        if (nodo instanceof NodoIf) {

            analizarIf(
                    (NodoIf) nodo
            );
        }
    }

    private void analizarDeclaracion(
            NodoDeclaracion nodo) {

        String nombre = nodo.getNombre();
        TipoDato tipo = nodo.getTipo();

        if (tablaSimbolos.existe(nombre)) {

            errores.add(
                    new ErrorSemantico(
                            "La variable '"
                                    + nombre
                                    + "' ya fue declarada.",
                            nodo.getLinea()
                    )
            );

            return;
        }

        Object valor = null;

        if (nodo.getValor() != null) {

            Resultado resultado =
                    evaluar(nodo.getValor());

            if (resultado.tipo == TipoDato.DESCONOCIDO) {
                return;
            }

            if (!tiposCompatibles(
                    tipo,
                    resultado.tipo)) {

                errores.add(
                        new ErrorSemantico(
                                "Tipo incompatible en la variable '"
                                        + nombre
                                        + "'. Se esperaba "
                                        + tipo
                                        + " pero se recibió "
                                        + resultado.tipo
                                        + ".",
                                nodo.getLinea()
                        )
                );

                return;
            }

            valor = resultado.valor;
        }

        tablaSimbolos.agregar(
                new Simbolo(
                        nombre,
                        tipo,
                        valor,
                        nodo.getLinea()
                )
        );
    }

    private void analizarAsignacion(
            NodoAsignacion nodo) {

        String nombre = nodo.getNombre();

        Simbolo simbolo =
                tablaSimbolos.buscar(nombre);

        if (simbolo == null) {

            errores.add(
                    new ErrorSemantico(
                            "La variable '"
                                    + nombre
                                    + "' no ha sido declarada.",
                            nodo.getLinea()
                    )
            );

            return;
        }

        Resultado resultado =
                evaluar(nodo.getValor());

        if (resultado.tipo == TipoDato.DESCONOCIDO) {
            return;
        }

        if (!tiposCompatibles(
                simbolo.getTipo(),
                resultado.tipo)) {

            errores.add(
                    new ErrorSemantico(
                            "No se puede asignar un valor de tipo "
                                    + resultado.tipo
                                    + " a la variable '"
                                    + nombre
                                    + "' de tipo "
                                    + simbolo.getTipo()
                                    + ".",
                            nodo.getLinea()
                    )
            );

            return;
        }

        simbolo.setValor(resultado.valor);
    }
    private void analizarIf(NodoIf nodo) {

        Resultado condicion =
                evaluar(nodo.getCondicion());

        if (condicion.tipo != TipoDato.BOOLEAN
                && condicion.tipo != TipoDato.DESCONOCIDO) {

            errores.add(
                    new ErrorSemantico(
                            "La condición del if debe ser de tipo BOOLEAN.",
                            nodo.getLinea()
                    )
            );

            return;
        }

        for (Nodo instruccion :
                nodo.getInstrucciones()) {

            analizarNodo(instruccion);
        }
    }
    private Resultado evaluar(Nodo nodo) {

        if (nodo instanceof NodoLiteral) {

            NodoLiteral literal =
                    (NodoLiteral) nodo;

            return new Resultado(
                    literal.getTipo(),
                    literal.getValor()
            );
        }

        if (nodo instanceof NodoIdentificador) {

            NodoIdentificador identificador =
                    (NodoIdentificador) nodo;

            Simbolo simbolo =
                    tablaSimbolos.buscar(
                            identificador.getNombre()
                    );

            if (simbolo == null) {

                errores.add(
                        new ErrorSemantico(
                                "La variable '"
                                        + identificador.getNombre()
                                        + "' no ha sido declarada.",
                                identificador.getLinea()
                        )
                );

                return new Resultado(
                        TipoDato.DESCONOCIDO,
                        null
                );
            }

            return new Resultado(
                    simbolo.getTipo(),
                    simbolo.getValor()
            );
        }

        if (nodo instanceof NodoOperacion) {

            return evaluarOperacion(
                    (NodoOperacion) nodo
            );
        }

        return new Resultado(
                TipoDato.DESCONOCIDO,
                null
        );
    }

    private Resultado evaluarOperacion(
            NodoOperacion nodo) {

        Resultado izquierdo =
                evaluar(nodo.getIzquierdo());

        Resultado derecho =
                evaluar(nodo.getDerecho());

        if (izquierdo.tipo == TipoDato.DESCONOCIDO
                || derecho.tipo == TipoDato.DESCONOCIDO) {

            return new Resultado(
                    TipoDato.DESCONOCIDO,
                    null
            );
        }

        String operador =
                nodo.getOperador();

        if (operador.equals("+")
                || operador.equals("-")
                || operador.equals("*")
                || operador.equals("/")
                || operador.equals("%")) {

            if (!esNumerico(izquierdo.tipo)
                    || !esNumerico(derecho.tipo)) {

                errores.add(
                        new ErrorSemantico(
                                "La operación '"
                                        + operador
                                        + "' requiere valores numéricos.",
                                nodo.getLinea()
                        )
                );

                return new Resultado(
                        TipoDato.DESCONOCIDO,
                        null
                );
            }

            if (izquierdo.tipo == TipoDato.DOUBLE
                    || derecho.tipo == TipoDato.DOUBLE) {

                double a =
                        ((Number) izquierdo.valor)
                                .doubleValue();

                double b =
                        ((Number) derecho.valor)
                                .doubleValue();

                double resultado;

                switch (operador) {

                    case "+":
                        resultado = a + b;
                        break;

                    case "-":
                        resultado = a - b;
                        break;

                    case "*":
                        resultado = a * b;
                        break;

                    case "/":
                        resultado = a / b;
                        break;

                    default:
                        resultado = a % b;
                        break;
                }

                return new Resultado(
                        TipoDato.DOUBLE,
                        resultado
                );
            }

            int a =
                    ((Number) izquierdo.valor)
                            .intValue();

            int b =
                    ((Number) derecho.valor)
                            .intValue();

            int resultado;

            switch (operador) {

                case "+":
                    resultado = a + b;
                    break;

                case "-":
                    resultado = a - b;
                    break;

                case "*":
                    resultado = a * b;
                    break;

                case "/":
                    resultado = b == 0 ? 0 : a / b;
                    break;

                default:
                    resultado = b == 0 ? 0 : a % b;
                    break;
            }

            return new Resultado(
                    TipoDato.INT,
                    resultado
            );
        }

        if (operador.equals(">")
                || operador.equals("<")
                || operador.equals(">=")
                || operador.equals("<=")) {

            if (!esNumerico(izquierdo.tipo)
                    || !esNumerico(derecho.tipo)) {

                errores.add(
                        new ErrorSemantico(
                                "Los operadores "
                                        + operador
                                        + " requieren valores numéricos.",
                                nodo.getLinea()
                        )
                );

                return new Resultado(
                        TipoDato.DESCONOCIDO,
                        null
                );
            }

            double a =
                    ((Number) izquierdo.valor)
                            .doubleValue();

            double b =
                    ((Number) derecho.valor)
                            .doubleValue();

            boolean resultado;

            switch (operador) {

                case ">":
                    resultado = a > b;
                    break;

                case "<":
                    resultado = a < b;
                    break;

                case ">=":
                    resultado = a >= b;
                    break;

                default:
                    resultado = a <= b;
                    break;
            }

            return new Resultado(
                    TipoDato.BOOLEAN,
                    resultado
            );
        }

        if (operador.equals("==")
                || operador.equals("!=")) {

            if (!tiposCompatibles(
                    izquierdo.tipo,
                    derecho.tipo)
                    && !tiposCompatibles(
                    derecho.tipo,
                    izquierdo.tipo)) {

                errores.add(
                        new ErrorSemantico(
                                "No se pueden comparar valores de tipo "
                                        + izquierdo.tipo
                                        + " y "
                                        + derecho.tipo
                                        + ".",
                                nodo.getLinea()
                        )
                );

                return new Resultado(
                        TipoDato.DESCONOCIDO,
                        null
                );
            }

            boolean iguales =
                    izquierdo.valor != null
                            && izquierdo.valor.equals(
                            derecho.valor
                    );

            if (operador.equals("!=")) {
                iguales = !iguales;
            }

            return new Resultado(
                    TipoDato.BOOLEAN,
                    iguales
            );
        }

        return new Resultado(
                TipoDato.DESCONOCIDO,
                null
        );
    }
    private boolean esNumerico(TipoDato tipo) {

        return tipo == TipoDato.INT
                || tipo == TipoDato.DOUBLE;
    }

    private boolean tiposCompatibles(
            TipoDato esperado,
            TipoDato recibido) {

        if (esperado == recibido) {
            return true;
        }

        return esperado == TipoDato.DOUBLE
                && recibido == TipoDato.INT;
    }

    public TablaSimbolos getTablaSimbolos() {
        return tablaSimbolos;
    }

    public List<ErrorSemantico> getErrores() {
        return errores;
    }

    public boolean tieneErrores() {
        return !errores.isEmpty();
    }

    public void limpiar() {
        errores.clear();
        tablaSimbolos.limpiar();
    }

    private static class Resultado {

        private final TipoDato tipo;
        private final Object valor;

        public Resultado(
                TipoDato tipo,
                Object valor) {

            this.tipo = tipo;
            this.valor = valor;
        }
    }
}