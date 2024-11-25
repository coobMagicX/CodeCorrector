private void declareArguments(Node functionNode) {
  Node astParameters = functionNode.getFirstChild().getNext();
  Node body = astParameters.getNext();
  FunctionType functionType = (FunctionType) functionNode.getJSType();
  if (functionType != null) {
    Node jsDocParameters = functionType.getParametersNode();
    if (jsDocParameters != null) {
      Node jsDocParameter = jsDocParameters.getFirstChild();
      for (Node astParameter : astParameters.children()) {
        String typeAnnotation = (jsDocParameter != null) ? jsDocParameter.getJSType() : null;
        defineSlot(astParameter, functionNode, typeAnnotation, true);
        if (jsDocParameter != null) {
          jsDocParameter = jsDocParameter.getNext();
        }
      }
    } else {
      for (Node astParameter : astParameters.children()) {
        defineSlot(astParameter, functionNode, null, true);
      }
    }
  } else {
    for (Node astParameter : astParameters.children()) {
      defineSlot(astParameter, functionNode, null, true);
    }
  }
} // end declareArguments