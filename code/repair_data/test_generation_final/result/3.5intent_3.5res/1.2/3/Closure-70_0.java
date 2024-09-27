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
          JSType jsType = jsDocParameter.getJSType();
          if (jsType != null || jsType == JSType.UNKNOWN || jsType.isNullable()) {
            defineSlot(astParameter, functionNode, jsType, true);
          } else {
            // Handle non-inferrable types
            defineSlot(astParameter, functionNode, null, true);
          }
          jsDocParameter = jsDocParameter.getNext();
        } else {
          defineSlot(astParameter, functionNode, null, true);
        }
      }
    }
  }
} // end declareArguments