private void declareArguments(Node functionNode) {
  Node astParameters = functionNode.getFirstChild().getNext();
  Node body = astParameters.getNext();
  FunctionType functionType = (FunctionType) functionNode.getJSType();
  if (functionType != null) {
    Node jsDocParameters = functionType.getParametersNode();
    if (jsDocParameters != null && astParameters != null) {
      Node jsDocParameter = jsDocParameters.getFirstChild();
      for (Node astParameter : astParameters.children()) {
        if (jsDocParameter != null) {
          defineSlot(astParameter, functionNode,
              jsDocParameter.getJSType(), true);
          jsDocParameter = jsDocParameter.getNext();
        } else {
          // Ensure that we don't try to redefine a slot with a different type
          if (!astParameter.isTypeDeclared()) {
            defineSlot(astParameter, functionNode, null, true);
          }
        }
      }
    }
  }
} // end declareArguments

// Helper method to check if the parameter has been declared before
boolean isTypeDeclared(Node astParameter) {
  String variableName = astParameter.getString();
  Scope currentScope = scope;
  while (currentScope != null && !currentScope.isDeclared(variableName, false)) {
    currentScope = currentScope.getEnclosingScope();
  }
  return currentScope != null;
}