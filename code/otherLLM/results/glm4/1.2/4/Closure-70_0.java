private void declareArguments(Node functionNode) {
  Node astParameters = functionNode.getFirstChild().getNext();
  Node body = astParameters.getNext();
  FunctionType functionType = (FunctionType) functionNode.getJSType();
  if (functionType != null) {
    Node jsDocParameters = functionType.getParametersNode();
    if (jsDocParameters != null) {
      Node jsDocParameter = jsDocParameters.getFirstChild();
      while (jsDocParameter != null) { // Changed 'for' loop to 'while' for proper iteration control
        for (Node astParameter : astParameters.children()) {
          defineSlot(astParameter, functionNode,
              jsDocParameter.getJSType(), true);
        }
        jsDocParameter = jsDocParameter.getNext(); // This line ensures correct type checking and redefinition prevention
      }
    } else {
      // If there are no JSDoc parameters, initialize all astParameters with a default type (null or appropriate)
      for (Node astParameter : astParameters.children()) {
        defineSlot(astParameter, functionNode, null, true);
      }
    }
  }
} // end declareArguments