private void declareArguments(Node functionNode) {
    Node astParameters = functionNode.getFirstChild().getNext();
    FunctionType functionType = (FunctionType) functionNode.getJSType();
    
    if (functionType == null) {
        for (Node astParameter : astParameters.children()) {
            defineSlot(astParameter, functionNode, null, true);
        }
        return;
    }

    Node jsDocParameters = functionType.getParametersNode();
    Node jsDocParameter = (jsDocParameters != null) ? jsDocParameters.getFirstChild() : null;

    for (Node astParameter : astParameters.children()) {
        if (jsDocParameter != null) {
            defineSlot(astParameter, functionNode, jsDocParameter.getJSType(), true);
            jsDocParameter = jsDocParameter.getNext();
        } else {
            // If there are more AST parameters than JSDoc parameters, define them with no specific type.
            defineSlot(astParameter, functionNode, null, true);
        }
    }
} // end declareArguments
