package jluvlox;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    final Environment enclosing;
    private final Map<String, Object> values = new HashMap<>();
    // constructors, with and without an enclosing scope
    Environment() {
        enclosing = null;
    }
    Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    void define(String name, Object value) {
        values.put(name, value);
    }

    Object get(Token name) {
        // look in current environment
        if (values.containsKey(name.lexeme)) {
            return values.get(name.lexeme);
        }
        // if not here, look in enclosing env (se p130)
        if (enclosing != null) return enclosing.get(name);
        //wind up here if nothing found
        throw new RuntimeError( name,
                "Undefined variable '" + name.lexeme + "'.");
    }

    void assign(Token name, Object value) {
        // same thing as get when looking for spot to assign to
        if (values.containsKey(name.lexeme)) {
            values.put(name.lexeme, value);
            return;
        }
        if (enclosing != null) {
            enclosing.assign(name, value);
            return;
        }

        throw new RuntimeError(name,
                "Undefined variable '" + name.lexeme + "'.");
    }
}
