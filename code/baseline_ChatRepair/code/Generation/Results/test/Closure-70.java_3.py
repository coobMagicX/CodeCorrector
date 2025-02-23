private void declareArguments(Node functionNode) {
  Node astParameters = functionNode.getFirstChild().getNext();
  Node body = astParameters.getNext();
  FunctionType functionType = (FunctionType) functionNode.getJSType();
  if (functionType != null) {
    Node jsDocParameters = functionType.getParametersNode();
    if (jsDocParameters != null) {
      Node jsDocParameter = jsDocParameters.getFirstChild();
      // Traverse over AST parameters and ensure each parameter declare a slot
      for (Node astParameter : astParameters.children()) {
        // Check if jsDocParameter is not null to use its JSType
        if (jsDocParameter != null && jsDocParameter.getString().equals(astParameter.getString())) {
          defineSlot(astParameter, functionNode, jsDocParameter.getJSType(), true);
          jsDocParameter = jsDocParameter.getNext();
        } else {
          // If jsDocParameter is null, finish up to define slots for remaining parameters
          defineSlot(astParameter, functionNode, null, true);
        }
      }
      // Ensure remaining jsDocParameters without matching AST parameters declare slots
      while (jsDocParameter != null) {
        defineSlotForJSDoc(functionNode, jsDocParameter, true);
        jsDocParameter = jsDocParameter.getNext();
      }
    }
  }
} // end declareArguments

// Helper function to define slots for JSDoc parameters having no corresponding AST node.
private void defineSlotForJSDoc(Node functionNode, Node jsDocParameter, boolean inferred) {
  defineSlot(jsDocParameter, functionNode, null, inferred);
}
