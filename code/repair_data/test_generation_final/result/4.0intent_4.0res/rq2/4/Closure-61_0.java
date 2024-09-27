import java.util.Set;

public class SideEffectChecker {
    static final Set<String> BUILTIN_FUNCTIONS_WITHOUT_SIDEEFFECTS = Set.of("Math.sin", "Math.cos", "Math.log");
    static final Set<String> OBJECT_METHODS_WITHOUT_SIDEEFFECTS = Set.of("toString", "valueOf");
    static final Set<String> REGEXP_METHODS = Set.of("test", "exec");
    static final Set<String> STRING_REGEXP_METHODS = Set.of("match", "replace");

    static boolean functionCallHasSideEffects(
        Node callNode, @Nullable AbstractCompiler compiler) {
        if (callNode.getType() != Token.CALL) {
            throw new IllegalStateException(
                "Expected CALL node, got " + Token.name(callNode.getType()));
        }

        if (callNode.isNoSideEffectsCall()) {
            return false;
        }

        Node nameNode = callNode.getFirstChild();

        // Built-in functions with no side effects.
        if (nameNode.getType() == Token.NAME) {
            String name = nameNode.getString();
            if (BUILTIN_FUNCTIONS_WITHOUT_SIDEEFFECTS.contains(name)) {
                return false;
            }
        } else if (nameNode.getType() == Token.GETPROP) {
            if (callNode.hasOneChild()
                && OBJECT_METHODS_WITHOUT_SIDEEFFECTS.contains(
                      nameNode.getLastChild().getString())) {
                return false;
            }

            if (callNode.isOnlyModifiesThisCall()
                && evaluatesToLocalValue(nameNode.getFirstChild())) {
                return false;
            }

            // Functions in the "Math" namespace have no side effects.
            Node firstChild = nameNode.getFirstChild();
            if (firstChild.getType() == Token.NAME && "Math".equals(firstChild.getString())) {
                if (BUILTIN_FUNCTIONS_WITHOUT_SIDEEFFECTS.contains(
                    nameNode.getLastChild().getString())) {
                    return false;
                }
            }

            if (compiler != null && !compiler.hasRegExpGlobalReferences()) {
                if (nameNode.getFirstChild().getType() == Token.REGEXP
                    && REGEXP_METHODS.contains(nameNode.getLastChild().getString())) {
                    return false;
                } else if (nameNode.getFirstChild().getType() == Token.STRING
                    && STRING_REGEXP_METHODS.contains(
                        nameNode.getLastChild().getString())) {
                    Node param = nameNode.getNext();
                    if (param != null &&
                        (param.getType() == Token.STRING
                            || param.getType() == Token.REGEXP))
                    return false;
                }
            }
        }

        return true;
    }

    public static void main(String[] args) {
        // Example usage
        Node callNode = parse("Math.sin(0)");
        System.out.println(functionCallHasSideEffects(callNode, null));
    }

    // Mock implementations for additional dependencies like Node, Token, AbstractCompiler
    static Node parse(String code) {
        // Simple parse implementation to create a node
        return new Node();
    }

    static class Node {
        int type;
        String string;
        Node firstChild;

        int getType() { return type; }
        String getString() { return string; }
        Node getFirstChild() { return firstChild; }
        boolean isNoSideEffectsCall() { return false; }
        boolean hasOneChild() { return false; }
        boolean isOnlyModifiesThisCall() { return false; }
        Node getLastChild() { return new Node(); }
        Node getNext() { return new Node(); }
    }

    enum Token {
        CALL, NAME, GETPROP, STRING, REGEXP;

        static String name(int type) { return "CALL"; }
    }

    interface AbstractCompiler {
        boolean hasRegExpGlobalReferences();
    }
}