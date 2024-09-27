private void declareArguments(Node functionNode) {
    Node astParameters = functionNode.getFirstChild().getNext();
    Node body = astParameters.getNext();  // This line seems unused and can be omitted if 'body' is not used later.
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
        }
    }
} // end declareArguments