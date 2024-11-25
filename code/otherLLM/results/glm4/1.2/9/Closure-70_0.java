private void declareArguments(Node functionNode) {
  Node astParameters = functionNode.getFirstChild().getNext();
  Node body = astParameters.getNext();
  FunctionType functionType = (FunctionType) functionNode.getJSType();
  if (functionType != null) {
    Node jsDocParameters = functionType.getParametersNode();
    if (jsDocParameters != null) {
      Node jsDocParameter = jsDocParameters.getFirstChild();
      for (Node astParameter : astParameters.children()) {
        // Ensure that the type is retrieved consistently from the jsDocParameter
        String parameterType = jsDocParameter == null ? null : jsDocParameter.getJSType();

        defineSlot(astParameter, functionNode, parameterType, true);
        
        // Update jsDocParameter to the next one in the list for consistency
        jsDocParameter = jsDocParameter.getNext();
      }
    }
  }
} // end declareArguments