private void declareArguments(Node functionNode) {
    Node astParameters = functionNode.getFirstChild().getNext();
    Node body = astParameters.getNext();
    FunctionType functionType = (FunctionType) functionNode.getJSType();
    HashSet<String> declaredParameters = new HashSet<>();
    if (functionType != null) {
        Node jsDocParameters = functionType.getParametersNode();
        if (jsDocParameters != null) {
            Node jsDocParameter = jsDocParameters.getFirstChild();
            for (Node astParameter : astParameters.children()) {
                String paramName = astParameter.getString();
                if (declaredParameters.contains(paramName)) {
                    throw new IllegalStateException("Parameter " + paramName + " is already defined.");
                }
                declaredParameters.add(paramName);
                if (jsDocParameter != null) {
                    defineSlot(astParameter, functionNode, jsDocParameter.getJSType(), true);
                    jsDocParameter = jsDocParameter.getNext();
                } else {
                    defineSlot(astParameter, functionNode, null, true);
                }
            }
        }
    }
} // end declareArguments