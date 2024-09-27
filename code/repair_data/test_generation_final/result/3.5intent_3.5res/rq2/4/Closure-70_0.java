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
          // Check if the parameter has already been defined with a different type
          JSType previousType = getSlotType(astParameter.getString());
          JSType newType = jsDocParameter.getJSType();
          if (previousType != null && !previousType.equals(newType)) {
            throw new RuntimeException("Parameter " + astParameter.getString() + " has already been defined with a different type.");
          }
          defineSlot(astParameter, functionNode, newType, true);
          jsDocParameter = jsDocParameter.getNext();
        } else {
          defineSlot(astParameter, functionNode, null, true);
        }
      }
    }
  }
} // end declareArguments