private void declareArguments(Node functionNode) {
  Node astParameters = functionNode.getFirstChild().getNext();
  Node body = astParameters.getNext();
  FunctionType functionType = (FunctionType) functionNode.getJSType();
  
  // Ensure function's JSType is not null and has parameters node.
  Node jsDocParameters = functionType != null ? functionType.getParametersNode() : null;
  Node jsDocParameter = jsDocParameters != null ? jsDocParameters.getFirstChild() : null;
  
  for (Node astParameter : astParameters.children()) {
    JSType type = null;
    if (jsDocParameter != null) {
      type = jsDocParameter.getJSType();
      jsDocParameter = jsDocParameter.getNext();
    }
    defineSlot(astParameter, functionNode, type, true);
  }
} // end declareArguments
