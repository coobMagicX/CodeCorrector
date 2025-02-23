private void declareArguments(Node functionNode) {
  Node astParameters = functionNode.getFirstChild().getNext();
  Node body = astParameters.getNext();
  FunctionType functionType = (FunctionType) functionNode.getJSType();
  if (functionType != null) {
    Node jsDocParameters = functionType.getParametersNode();
    Node jsDocParameter = jsDocParameters != null ? jsDocParameters.getFirstChild() : null;

    for (Node astParameter : astParameters.children()) {
      JSType paramType = jsDocParameter != null ? jsDocParameter.getJSType() : null;
      defineSlot(astParameter, functionNode, paramType, true);
      jsDocParameter = jsDocParameter != null ? jsDocParameter.getNext() : null;
    }
  }
} // end declareArguments
