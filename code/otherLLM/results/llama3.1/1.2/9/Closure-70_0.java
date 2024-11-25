private void declareArguments(Node functionNode) {
  Node astParameters = functionNode.getFirstChild().getNext();
  Node body = astParameters.getNext();
  FunctionType functionType = (FunctionType) functionNode.getJSType();
  if (functionType != null) {
    Node jsDocParameters = functionType.getParametersNode();
    if (jsDocParameters != null) {
      Node jsDocParameter = jsDocParameters.getFirstChild();
      for (Node astParameter : astParameters.children()) {
        if (jsDocParameter != null && !astParameter.getName().equals(jsDocParameter.getName())) {
          defineSlot(astParameter, functionNode,
              jsDocParameter.getJSType(), true);
          jsDocParameter = jsDocParameter.getNext();
        } else if (jsDocParameter != null) {
          // handle duplicate local variable declarations
          if (!variableExists(functionNode, astParameter.getName())) {
            defineSlot(astParameter, functionNode,
                jsDocParameter.getJSType(), true);
            jsDocParameter = jsDocParameter.getNext();
          }
        } else {
          defineSlot(astParameter, functionNode, null, true);
        }
      }
    }
  }
} // end declareArguments

private boolean variableExists(Node functionNode, String varName) {
  Node astParameters = functionNode.getFirstChild().getNext();
  for (Node astParameter : astParameters.children()) {
    if (astParameter.getName().equals(varName)) {
      return true;
    }
  }
  return false;
} // end variableExists