private void declareArguments(Node functionNode) {
    Node astParameters = functionNode.getFirstChild().getNext();
    Node body = astParameters.getNext();
    FunctionType functionType = (FunctionType) functionNode.getJSType();
    if (functionType != null) {
        Node jsDocParameters = functionType.getParametersNode();
        if (jsDocParameters != null) {
            Node jsDocParameter = jsDocParameters.getFirstChild();
            for (Node astParameter : astParameters.children()) {
                if (jsDocParameter != null) {
                    defineSlot(astParameter, functionNode,
                        jsDocParameter.getJSType(), true);
                    jsDocParameter = jsDocParameter.getNext();
                } else {
                    defineSlot(astParameter, functionNode, null, true);
                }
            }
        } else {
            // Ensure slots are defined even when jsDocParameters is null
            for (Node astParameter : astParameters.children()) {
                defineSlot(astParameter, functionNode, null, true);
            }
        }
    }
} // end declareArguments