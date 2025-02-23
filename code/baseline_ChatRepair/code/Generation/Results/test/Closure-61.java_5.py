static boolean functionCallHasSideEffects(Node callNode, @Nullable AbstractCompiler compiler) {
    if (callNode.getType() != Token.CALL) {
        throw new IllegalStateException("Expected CALL node, got " + Token.name(callNode.getType()));
    }

    if (callNode.isNoSideEffectsCall()) {
        return false;
    }
    
    Node nameNode = callNode.getFirstChild();

    // Built-in functions that are known to have no side effects, excluding Math.random
    if (nameNode.getType() == Token.NAME) {
        String name = nameNode.getString();
        if (BUILTIN_FUNCTIONS_WITHOUT_SIDEEFFECTS.contains(name) && !name.equals("random")) {
            return false;
        }
    } else if (nameNode.getType() == Token.GETPROP) {
        if (callNode.hasOneChild() && OBJECT_METHODS_WITHOUT_SIDEEFFECTS.contains(nameNode.getLastChild().getString())) {
            return false;
        }

        if (callNode.isOnlyModifiesThisCall() && evaluatesToLocalValue(nameNode.getFirstChild())) {
            return false;
        }

        Node objectNode = nameNode.getFirstChild();
        String propertyName = nameNode.getLastChild().getString();
        if (objectNode.getType() == Token.NAME && "Math".equals(objectNode.getString())) {
            // Enumerate safe Math functions that do not have side effects. Exclude Math.random()
            if (Arrays.asList("abs", "sin", "cos", "tan", "exp", "log", "sqrt").contains(propertyName)) {
                return false;
            }
        }
    }

    return true;
}
