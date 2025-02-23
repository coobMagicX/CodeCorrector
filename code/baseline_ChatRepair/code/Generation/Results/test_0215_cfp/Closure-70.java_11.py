private void declareArguments(Node functionNode) {
    Node astParameters = functionNode.getFirstChild().getNext(); // Parameters node
    FunctionType functionType = (FunctionType) functionNode.getJSType();
    if (functionType != null) {
        Node jsDocParameters = functionType.getParametersNode();
        HashSet<String> paramNames = new HashSet<String>(); // Specified String in generic
        
        for (Node astParameter : astParameters.children()) {
            String paramName = astParameter.getString(); // Assume the Node method getString() gives the parameter name
            JSType jsDocType = null;
            
            if (jsDocParameters != null) {
                for (Node jsDocParameter = jsDocParameters.getFirstChild();
                     jsDocParameter != null;
                     jsDocParameter = jsDocParameter.getNext()) {
                    if (jsDocParameter.getString().equals(paramName)) {
                        jsDocType = jsDocParameter.getJSType();
                        break;
                    }
                }
            }
            
            // Check for duplicate parameter name
            if (!paramNames.add(paramName)) {
                // Handle duplicate parameter name
                throw new IllegalStateException("Duplicate parameter name detected: " + paramName);
            }
            
            // Define the slot for each parameter
            defineSlot(astParameter, functionNode, jsDocType, true);
        }
    }
} // end declareArguments
