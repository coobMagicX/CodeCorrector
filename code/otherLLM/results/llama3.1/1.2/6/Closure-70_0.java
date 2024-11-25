private void declareArguments(Node functionNode) {
  Node astParameters = functionNode.getFirstChild().getNext();
  Node body = astParameters.getNext();
  FunctionType functionType = (FunctionType) functionNode.getJSType();
  if (functionType != null) {
    Node jsDocParameters = functionType.getParametersNode();
    if (jsDocParameters != null) {
      Node jsDocParameter = jsDocParameters.getFirstChild();
      Set<String> localVars = new HashSet<>();
      for (Node astParameter : astParameters.children()) {
        if (jsDocParameter != null) {
          String varName = astParameter.getName();
          if (localVars.contains(varName)) {
            validator.expectDuplicateLocalVariable(sourceName, astParameter, jsDocParameter, varName);
          } else {
            localVars.add(varName);
            defineSlot(astParameter, functionNode,
                jsDocParameter.getJSType(), true);
          }
          jsDocParameter = jsDocParameter.getNext();
        } else {
          String varName = astParameter.getName();
          if (localVars.contains(varName)) {
            validator.expectDuplicateLocalVariable(sourceName, astParameter, null, varName);
          } else {
            localVars.add(varName);
            defineSlot(astParameter, functionNode, null, true);
          }
        }
      }
    }
  }
} // end declareArguments