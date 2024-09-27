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
          String variableName = astParameter.getString();
          JSType jsDocParameterType = jsDocParameter.getJSType();
          JSType astParameterType = astParameter.getJSType();
          if (astParameterType != null && !astParameterType.equals(jsDocParameterType)) {
            // Variable redeclaration with different type
            throw new IllegalArgumentException("Variable '" + variableName + "' is redeclared with a different type");
          }
          defineSlot(astParameter, functionNode, variableName, jsDocParameterType, true);
          jsDocParameter = jsDocParameter.getNext();
        } else {
          defineSlot(astParameter, functionNode, null, null, true);
        }
      }
    }
  }
} // end declareArguments