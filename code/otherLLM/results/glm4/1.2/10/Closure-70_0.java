private void declareArguments(Node functionNode) {
  Node astParameters = functionNode.getFirstChild().getNext();
  Node body = astParameters.getNext();
  FunctionType functionType = (FunctionType) functionNode.getJSType();
  if (functionType != null) {
    Node jsDocParameters = functionType.getParametersNode();
    if (jsDocParameters != null) {
      Node jsDocParameter = jsDocParameters.getFirstChild();
      for (Node astParameter : astParameters.children()) {
        // Check if there is a corresponding JSDoc parameter
        if (jsDocParameter != null) {
          String jsDocType = jsDocParameter.getJSType(); // Extract the type from JSDoc
          defineSlot(astParameter, functionNode, jsDocType, true);
          jsDocParameter = jsDocParameter.getNext();
        } else {
          // If there is no corresponding JSDoc parameter, use a default or null type
          String jsDocType = null; // Default to null if not specified in the JSDoc
          defineSlot(astParameter, functionNode, jsDocType, true);
        }
      }
    }
  } else {
    // If there is no FunctionType associated with the node, skip defining slots or handle it appropriately
    return;
  }
} // end declareArguments