private void declareArguments(Node functionNode) {
  Node astParameters = functionNode.getFirstChild().getNext();
  Node body = astParameters.getNext();
  FunctionType functionType = (FunctionType) functionNode.getJSType();
  if (functionType != null) {
    Node jsDocParameters = functionType.getParametersNode();
    if (jsDocParameters != null) {
      List<Node> jsDocParameterList = new ArrayList<>();
      while (jsDocParameterList.isEmpty()) {
        Node jsDocParameter = jsDocParameters.getFirstChild();
        if (jsDocParameter != null) {
          for (Node astParameter : astParameters.children()) {
            if (!astParameter.equals(jsDocParameter)) { // check for parameter name conflict
              defineSlot(astParameter, functionNode,
                  getJsdocTypeOrNull(jsDocParameter), true);
              jsDocParameterList.add(jsDocParameter); // store valid JS doc parameters
              jsDocParameter = jsDocParameter.getNext();
            } else {
              defineSlot(astParameter, functionNode, null, true);
            }
          }
        } else {
          break;
        }
      }

      for (Node astParameter : astParameters.children()) {
        if (!jsDocParameterList.contains(astParameter)) { // check for unmatched AST parameters
          defineSlot(astParameter, functionNode, null, true);
        }
      }
    }
  }
}

private String getJsdocTypeOrNull(Node jsDocParameter) {
  Type type = jsDocParameter.getJSType();
  if (type != null && !type.getType().equals("void")) { // check for void types
    return type.getType();
  } else {
    return null;
  }
}