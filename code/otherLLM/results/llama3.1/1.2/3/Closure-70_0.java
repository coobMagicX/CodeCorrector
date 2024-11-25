private void declareArguments(Node functionNode) {
  Node astParameters = functionNode.getFirstChild().getNext();
  Node body = astParameters.getNext();
  FunctionType functionType = (FunctionType) functionNode.getJSType();
  
  if (functionType != null) {
    Node jsDocParameters = functionType.getParametersNode();
    if (jsDocParameters != null) {
      Node jsDocParameter = jsDocParameters.getFirstChild();
      
      // Check for duplicate parameter names
      Set<String> paramNames = new HashSet<>();
      for (Node astParameter : astParameters.children()) {
        String paramName = astParameter.getName();
        
        // If the parameter name is already declared, validate its type consistency
        if (paramNames.contains(paramName)) {
          Var oldVar = scope.getVar(paramName);
          validator.expectUndeclaredVariable(sourceName, astParameter, functionNode, oldVar, paramName, astParameter.getType());
        } else {
          paramNames.add(paramName);
          
          // Define slot for the function argument
          defineSlot(astParameter, functionNode,
              jsDocParameter.getJSType(), true);
          jsDocParameter = jsDocParameter.getNext();
        }
      }
    }
  }
} // end declareArguments