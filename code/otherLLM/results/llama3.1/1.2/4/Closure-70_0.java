private void declareArguments(Node functionNode) {
  Node astParameters = functionNode.getFirstChild().getNext();
  Node body = astParameters.getNext();
  FunctionType functionType = (FunctionType) functionNode.getJSType();
  if (functionType != null) {
    Node jsDocParameters = functionType.getParametersNode();
    if (jsDocParameters != null) {
      Set<Node> declaredParameters = new HashSet<>();
      Node jsDocParameter = jsDocParameters.getFirstChild();
      for (Node astParameter : astParameters.children()) {
        if (jsDocParameter != null) {
          String name = astParameter.getName();
          if (!declaredParameters.contains(astParameter)) {
            defineSlot(astParameter, functionNode,
                jsDocParameter.getJSType(), true);
            declaredParameters.add(astParameter);
            jsDocParameter = jsDocParameter.getNext();
          }
        } else {
          defineSlot(astParameter, functionNode, null, true);
        }
      }
    }
  }
} // end declareArguments