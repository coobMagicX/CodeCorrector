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
                if (jsDocParameter != null) {
                    String paramName = astParameter.getString();
                    JSType paramType = jsDocParameter.getJSType();
                    
                    if (declaredVariables.containsKey(paramName)) {
                        JSType existingType = declaredVariables.get(paramName);
                        if (!existingType.equals(paramType)) {
                            throw new IllegalArgumentException("Variable " + paramName + " redeclared with a conflicting type");
                        }
                    } else {
                        declaredVariables.put(paramName, paramType);
                        defineSlot(astParameter, functionNode, paramName, paramType, true);
                    }
                    
                    jsDocParameter = jsDocParameter.getNext();
                } else {
                    defineSlot(astParameter, functionNode, astParameter.getString(), null, true);
                }
            }
        }
    }
} // end declareArguments