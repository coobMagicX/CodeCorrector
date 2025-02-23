private void declareArguments(Node functionNode) {
  Node astParameters = functionNode.getFirstChild().getNext();
  Node body = astParameters.getNext();
  FunctionType functionType = (FunctionType) functionNode.getJSType();
  if (functionType != null) {
    Node jsDocParameters = functionType.getParametersNode();
    Node jsDocParameter = jsDocParameters != null ? jsDocParameters.getFirstChild() : null;
    
    for (Node astParameter : astParameters.children()) {
      JSType type = jsDocParameter != null ? jsDocParameter.getJSType() : null;
      defineSlot(astParameter, functionNode, type, true);
      if (jsDocParameter != null) {
        jsDocParameter = jsDocParameter.getNext();
      }
    }
  }
} // end declareArguments
