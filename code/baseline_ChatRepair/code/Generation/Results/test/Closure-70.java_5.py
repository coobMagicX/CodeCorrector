private void declareArguments(Node functionNode) {
  Node astParameters = functionNode.getFirstChild().getNext();
  Node body = astParameters.getNext();
  FunctionType functionType = (FunctionType) functionNode.getJSType();
  
  if (functionType != null) {
    Node jsDocParameters = functionType.getParametersNode();
    if (jsDocParameters != null) {
      Node jsDocParameter = jsDocParameters.getFirstChild();

      // Map JSDoc parameters to their types for quick lookup
      Map<String, JSType> jsDocParamTypeMap = new HashMap<String, JSType>();
      while (jsDocParameter != null) {
        jsDocParamTypeMap.put(jsDocParameter.getString(), jsDocParameter.getJSType());
        jsDocParameter = jsDocParameter.getNext();
      }

      // Process each AST parameter
      for (Node astParameter : astParameters.children()) {
        // Retrieve the corresponding JSType from JSDoc if exists
        JSType jsType = jsDocParamTypeMap.get(astParameter.getString());
        // If JSType is not found, send null
        defineSlot(astParameter, functionNode, jsType, true);
      }
    } else {
      // If there are no JSDoc parameters, define all AST parameters with null type
      for (Node astParameter : astParameters.children()) {
        defineSlot(astParameter, functionNode, null, true);
      }
    }
  } else {
    // If there is no function Type, define all AST parameters with null type
    for (Node astParameter : astParameters.children()) {
      defineSlot(astParameter, functionNode, null, true);
    }
  }
} // end declareArguments
