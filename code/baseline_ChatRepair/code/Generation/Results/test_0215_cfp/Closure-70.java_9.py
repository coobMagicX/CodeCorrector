private void declareArguments(Node functionNode) {
  Node astParameters = functionNode.getFirstChild().getNext();
  Node body = astParameters.getNext();
  FunctionType functionType = (FunctionType) functionNode.getJSType();
  
  if (functionType != null) {
    Node jsDocParameters = functionType.getParametersNode();

    // Iterate through AST parameters
    for (Node astParameter : astParameters.children()) {
      JSType parameterType = null;
      
      // Check if there are corresponding JSDoc parameters
      if (jsDocParameters != null && jsDocParameters.hasChildren()) {
        Node jsDocParameter = jsDocParameters.getFirstChild();
        if (jsDocParameter != null) {
          parameterType = jsDocParameter.getJSType();
          jsDocParameters.removeChild(jsDocParameter); // Move to the next jsDocParameter
        }
      }
      
      // Define the slot for each AST parameter, with the derived or default type
      defineSlot(astParameter, functionNode, parameterType, true);
    }
  }
} // end declareArguments
