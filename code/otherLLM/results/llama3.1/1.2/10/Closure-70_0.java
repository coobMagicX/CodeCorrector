private void declareArguments(Node functionNode) {
  Node astParameters = functionNode.getFirstChild().getNext();
  Node body = astParameters.getNext();
  FunctionType functionType = (FunctionType) functionNode.getJSType();
  if (functionType != null) {
    Node jsDocParameters = functionType.getParametersNode();
    if (jsDocParameters != null) {
      Node jsDocParameter = jsDocParameters.getFirstChild();
      for (Node astParameter : astParameters.children()) {
        JSType astType = astParameter.getJSType();
        JSType jsDocType = jsDocParameter.getJSType();
        
        // Check if a type annotation has been provided in the source code
        // and reconcile it with the JSDoc type.
        if (astType != null) {
          defineSlot(astParameter, functionNode,
              astParameter.getName(), astType, true);
        } else if (jsDocType != null) {
          defineSlot(astParameter, functionNode,
              jsDocParameter.getName(), jsDocType, false);
        }
        
        // If there is no type annotation and no JSDoc type provided,
        // use the original type of the variable.
        if (astParameter.getType() == Token.NAME && astParameter.getOriginalType() != null) {
          defineSlot(astParameter, functionNode,
              astParameter.getName(), astParameter.getOriginalType(), true);
        }
        
        jsDocParameter = jsDocParameter.getNext();
      }
    }
  }
} // end declareArguments