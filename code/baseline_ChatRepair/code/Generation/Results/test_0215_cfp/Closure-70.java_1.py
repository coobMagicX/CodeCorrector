private void declareArguments(Node functionNode) {
    Node astParameters = functionNode.getFirstChild().getNext();
    Node body = astParameters.getNext();
    FunctionType functionType = (FunctionType) functionNode.getJSType();

    if (functionType != null) {
        Node jsDocParameters = functionType.getParametersNode();
        Node jsDocParameter = jsDocParameters != null ? jsDocParameters.getFirstChild() : null;

        for (Node astParameter : astParameters.children()) {
            if (jsDocParameter != null) {
                defineSlot(astParameter, functionNode, jsDocParameter.getJSType(), true);
                jsDocParameter = jsDocParameter.getNext();
            } else {
                defineSlot(astParameter, functionNode, null, true);
            }
        }
        
        // Ensure slots for excess arguments are defined, if AST has more parameters than JSDoc.
        while (jsDocParameter != null) {
            defineSlot(jsDocParameter, functionNode, null, true);
            jsDocParameter = jsDocParameter.getNext();
        }
    }
} // end declareArguments
