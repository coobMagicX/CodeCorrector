private void declareArguments(Node functionNode) {
  Node astParameters = functionNode.getFirstChild().getNext();
  Node body = astParameters.getNext();
  FunctionType functionType = (FunctionType) functionNode.getJSType();
  if (functionType != null) {
    Node jsDocParameters = functionType.getParametersNode();
    if (jsDocParameters != null) {
      Node jsDocParameter = jsDocParameters.getFirstChild();
      for (Node astParameter : astParameters.children()) {
        String variableName = astParameter.getName();
        JSType type = astParameter.getType();

        // Check if the local variable declaration already exists
        if (!scope.isDeclared(variableName, false)) {
          defineSlot(astParameter, functionNode,
              variableName, type, true);
        } else {
          Var oldVar = scope.getVar(variableName);
          validator.expectUndeclaredVariable(
              sourceName, astParameter, null, oldVar, variableName, type);
        }
        
        // Move to the next JSDoc parameter
        jsDocParameter = jsDocParameter.getNext();
      }
    }
  }
} // end declareArguments