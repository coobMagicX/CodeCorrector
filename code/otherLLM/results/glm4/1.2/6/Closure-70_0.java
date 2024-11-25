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
          String jsDocParamType = jsDocParameter.getJSType();
          if (jsDocParamType != null && !astParameter.getType().equals(jsDocParamType)) {
            // Log a warning or throw an error based on the requirements
            System.err.println("Type mismatch between JSDoc and AST parameter for: " + astParameter.getName());
            // Option 1: Throw an error to stop execution
            // throw new IllegalArgumentException("Type mismatch between JSDoc and AST parameter types");

            // Option 2: Continue with a warning (if the requirement is to continue on warnings)
          } else {
            defineSlot(astParameter, functionNode,
                jsDocParamType != null ? jsDocParamType : astParameter.getType(), true);
          }
        } else {
          defineSlot(astParameter, functionNode, null, true);
        }
        // Move to the next AST parameter
      }
    }
  }
} // end declareArguments