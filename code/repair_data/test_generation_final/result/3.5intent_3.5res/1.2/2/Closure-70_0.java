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
          if (jsType == null) {
            defineSlot(astParameter, functionNode, null, true);
          } else if (jsType.isUnknownType() || jsType.isNullable() || jsType.isVoidType()) {
            defineSlot(astParameter, functionNode, jsType, true);
          } else {
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