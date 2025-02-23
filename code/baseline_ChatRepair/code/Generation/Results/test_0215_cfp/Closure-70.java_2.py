private void declareArguments(Node functionNode) {
    Node astParameters = functionNode.getFirstChild().getNext();
    Node body = astParameters.getNext();
    FunctionType functionType = (FunctionType) functionNode.getJSType();
  
    if (functionType != null) {
        Node jsDocParameters = functionType.getParametersNode();
        Node jsDocParameter = jsDocParameters != null ? jsDocParameters.getFirstChild() : null;

        for (Node astParameter : astParameters.children()) {
            JSType paramType = null;
            // Ensure JSType is used from jsDoc if available
            if (jsDocParameter != null) {
                paramType = jsDocParameter.getJSType();
                jsDocParameter = jsDocParameter.getNext();
            } 
            else {
                if (jsDocParameters != null) {
                    // reset to the first documented parameter if jsDoc is shorter than astParameters
                    jsDocParameter = jsDocParameters.getFirstChild();
                    paramType = jsDocParameter.getJSType();
                    jsDocParameter = jsDocParameter.getNext();
                }
            }
            defineSlot(astParameter, functionNode, paramType, true);
        }
    }
} // end declareArguments
