package com.brayansystem.analizadorsemantico.parser;

import com.brayansystem.analizadorsemantico.ast.Nodo;
import com.brayansystem.analizadorsemantico.ast.NodoAsignacion;
import com.brayansystem.analizadorsemantico.ast.NodoDeclaracion;
import com.brayansystem.analizadorsemantico.ast.NodoIdentificador;
import com.brayansystem.analizadorsemantico.ast.NodoIf;
import com.brayansystem.analizadorsemantico.ast.NodoLiteral;
import com.brayansystem.analizadorsemantico.ast.NodoOperacion;
import com.brayansystem.analizadorsemantico.modelo.Token;
import com.brayansystem.analizadorsemantico.modelo.TipoDato;

import java.util.ArrayList;
import java.util.List;

public class AnalizadorSintactico {

    private List<Token> tokens;
    private int posicion;

    private final List<String> errores =
            new ArrayList<>();

    public List<Nodo> analizar(List<Token> tokens) {

        this.tokens = tokens;
        this.posicion = 0;

        errores.clear();

        List<Nodo> nodos = new ArrayList<>();

        if (tokens == null || tokens.isEmpty()) {
            return nodos;
        }

        while (!fin()) {

            if (tipoActual("EOF")) {
                break;
            }

            Nodo nodo = sentencia();

            if (nodo != null) {
                nodos.add(nodo);
            } else {
                recuperar();
            }
        }

        return nodos;
    }

    public List<String> getErrores() {
        return errores;
    }

    private Nodo sentencia() {

        if (tipoActual("TIPO_DATO")) {
            avanzar();
            return declaracion();
        }

        if (tipoActual("IDENTIFICADOR")) {
            avanzar();
            return asignacion();
        }

        if (tipoActual("IF")) {
            avanzar();
            return bloqueIf();
        }

        error(
                "Sentencia no reconocida.",
                actual()
        );

        return null;
    }

    private Nodo declaracion() {

        Token tipoToken = anterior();

        TipoDato tipo =
                convertirTipo(tipoToken.getLexema());

        if (!coincide("IDENTIFICADOR")) {

            error(
                    "Se esperaba un identificador.",
                    actual()
            );

            return null;
        }

        Token nombre = anterior();

        Nodo valor = null;

        if (coincide("ASIGNACION")) {

            valor = expresion();

            if (valor == null) {
                return null;
            }
        }

        if (!coincide("PUNTO_COMA")) {

            error(
                    "Se esperaba ';' al final de la declaración.",
                    actual()
            );

            return null;
        }

        return new NodoDeclaracion(
                nombre.getLexema(),
                tipo,
                valor,
                nombre.getLinea()
        );
    }

    private Nodo asignacion() {

        Token nombre = anterior();

        if (!coincide("ASIGNACION")) {

            error(
                    "Se esperaba '=' después del identificador.",
                    actual()
            );

            return null;
        }

        Nodo valor = expresion();

        if (valor == null) {
            return null;
        }

        if (!coincide("PUNTO_COMA")) {

            error(
                    "Se esperaba ';' al final de la asignación.",
                    actual()
            );

            return null;
        }

        return new NodoAsignacion(
                nombre.getLexema(),
                valor,
                nombre.getLinea()
        );
    }

    private Nodo bloqueIf() {

        Token tokenIf = anterior();

        if (!coincide("PARENTESIS_ABRE")) {

            error(
                    "Se esperaba '(' después de if.",
                    actual()
            );

            return null;
        }

        Nodo condicion = expresion();

        if (condicion == null) {
            return null;
        }

        if (!coincide("PARENTESIS_CIERRA")) {

            error(
                    "Se esperaba ')' después de la condición.",
                    actual()
            );

            return null;
        }

        if (!coincide("LLAVE_ABRE")) {

            error(
                    "Se esperaba '{' después de la condición.",
                    actual()
            );

            return null;
        }

        List<Nodo> instrucciones =
                new ArrayList<>();

        while (!fin()
                && !tipoActual("LLAVE_CIERRA")) {

            Nodo nodo = sentencia();

            if (nodo != null) {
                instrucciones.add(nodo);
            } else {
                recuperar();
            }
        }

        if (!coincide("LLAVE_CIERRA")) {

            error(
                    "Se esperaba '}'.",
                    actual()
            );

            return null;
        }

        return new NodoIf(
                condicion,
                instrucciones,
                tokenIf.getLinea()
        );
    }

    private Nodo expresion() {
        return comparacion();
    }

    private Nodo comparacion() {

        Nodo izquierdo = suma();

        if (izquierdo == null) {
            return null;
        }

        while (
                tipoActual("IGUAL_QUE")
                        || tipoActual("DIFERENTE")
                        || tipoActual("MENOR")
                        || tipoActual("MENOR_IGUAL")
                        || tipoActual("MAYOR")
                        || tipoActual("MAYOR_IGUAL")
        ) {

            Token operador = actual();
            avanzar();

            Nodo derecho = suma();

            if (derecho == null) {
                return null;
            }

            izquierdo = new NodoOperacion(
                    izquierdo,
                    operador.getLexema(),
                    derecho,
                    operador.getLinea()
            );
        }

        return izquierdo;
    }

    private Nodo suma() {

        Nodo izquierdo = multiplicacion();

        while (
                tipoActual("SUMA")
                        || tipoActual("RESTA")
        ) {

            Token operador = actual();
            avanzar();

            Nodo derecho = multiplicacion();

            if (derecho == null) {
                return null;
            }

            izquierdo = new NodoOperacion(
                    izquierdo,
                    operador.getLexema(),
                    derecho,
                    operador.getLinea()
            );
        }

        return izquierdo;
    }

    private Nodo multiplicacion() {

        Nodo izquierdo = primario();

        while (
                tipoActual("MULTIPLICACION")
                        || tipoActual("DIVISION")
                        || tipoActual("MODULO")
        ) {

            Token operador = actual();
            avanzar();

            Nodo derecho = primario();

            if (derecho == null) {
                return null;
            }

            izquierdo = new NodoOperacion(
                    izquierdo,
                    operador.getLexema(),
                    derecho,
                    operador.getLinea()
            );
        }

        return izquierdo;
    }

    private Nodo primario() {

        if (coincide("ENTERO")) {

            Token token = anterior();

            return new NodoLiteral(
                    Integer.parseInt(
                            token.getLexema()
                    ),
                    TipoDato.INT,
                    token.getLinea()
            );
        }

        if (coincide("DECIMAL")) {

            Token token = anterior();

            return new NodoLiteral(
                    Double.parseDouble(
                            token.getLexema()
                    ),
                    TipoDato.DOUBLE,
                    token.getLinea()
            );
        }

        if (coincide("CADENA")) {

            Token token = anterior();

            return new NodoLiteral(
                    token.getLexema(),
                    TipoDato.STRING,
                    token.getLinea()
            );
        }

        if (coincide("BOOLEANO")) {

            Token token = anterior();

            return new NodoLiteral(
                    Boolean.parseBoolean(
                            token.getLexema()
                    ),
                    TipoDato.BOOLEAN,
                    token.getLinea()
            );
        }

        if (coincide("IDENTIFICADOR")) {

            Token token = anterior();

            return new NodoIdentificador(
                    token.getLexema(),
                    token.getLinea()
            );
        }

        if (coincide("PARENTESIS_ABRE")) {

            Nodo nodo = expresion();

            if (!coincide("PARENTESIS_CIERRA")) {

                error(
                        "Se esperaba ')'.",
                        actual()
                );
            }

            return nodo;
        }

        error(
                "Expresión no válida.",
                actual()
        );

        return null;
    }

    private TipoDato convertirTipo(String tipo) {

        return switch (tipo) {

            case "int" ->
                    TipoDato.INT;

            case "double" ->
                    TipoDato.DOUBLE;

            case "String" ->
                    TipoDato.STRING;

            case "boolean" ->
                    TipoDato.BOOLEAN;

            default ->
                    TipoDato.DESCONOCIDO;
        };
    }

    private boolean coincide(String tipo) {

        if (tipoActual(tipo)) {

            avanzar();

            return true;
        }

        return false;
    }

    private boolean tipoActual(String tipo) {

        return !fin()
                && actual().getTipo().equals(tipo);
    }

    private void avanzar() {

        if (!fin()) {
            posicion++;
        }
    }

    private Token actual() {

        if (posicion >= tokens.size()) {
            return tokens.get(tokens.size() - 1);
        }

        return tokens.get(posicion);
    }

    private Token anterior() {

        return tokens.get(posicion - 1);
    }

    private boolean fin() {

        return posicion >= tokens.size()
                || actual().getTipo().equals("EOF");
    }

    private void error(
            String mensaje,
            Token token) {

        errores.add(
                "Línea "
                        + token.getLinea()
                        + ", columna "
                        + token.getColumna()
                        + ": "
                        + mensaje
        );
    }

    private void recuperar() {

        while (!fin()) {

            if (coincide("PUNTO_COMA")) {
                return;
            }

            if (coincide("LLAVE_CIERRA")) {
                return;
            }

            avanzar();
        }
    }
}