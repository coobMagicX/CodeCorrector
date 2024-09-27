private void declareArguments(Node functionNode) {
    Node astParameters = functionNode.getFirstChild().getNext();
    Node body = astParameters.getNext();
    FunctionType functionType = (FunctionType) functionNode.getJSType();
    Map<String, JSType> declaredVariables = new HashMap<>();

    if (functionType != null) {
        Node jsDocParameters = functionType.getParametersNode();
        if (jsDocParameters != null) {
            Node jsDocParameter = jsDocParameters.getFirstChild();
            for (Node astParameter : astParameters.children()) {
                String variableName = astParameter.getString(); // Assuming getString() gets the variable name
                JSType jsDocParamType = jsDocParameter != null ? jsDocParameter.getJSType() : null;

                if (declaredVariables.containsKey(variableName)) {
                    JSType existingType = declaredVariables.get(variableName);
                    if (!existingType.equals(jsDocParamType)) {
                        // Log error or throw exception due to type mismatch on redeclaration
                        throw new IllegalArgumentException("Variable '" + variableName + "' redeclared with a different type");
                    }
                } else {
                    declaredVariables.put(variableName, jsDocParamType);
                    defineSlot(astParameter, functionNode, variableName, jsDocParamType, true);
                }

                if (jsDocParameter != null) {
                    jsDocParameter = jsDocParameter.getNext();
                }
            }
        }
    }
} // end declareArguments