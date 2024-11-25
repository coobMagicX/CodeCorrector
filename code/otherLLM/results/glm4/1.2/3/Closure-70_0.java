private void declareArguments(Node functionNode) {
  Node astParameters = functionNode.getFirstChild().getNext();
  Node body = astParameters.getNext();
  FunctionType functionType = (FunctionType) functionNode.getJSType();
  if (functionType != null) {
    Node jsDocParameters = functionType.getParametersNode();
    if (jsDocParameters != null) {
      Map<String, JSType> typeMap = new HashMap<>();
      Node jsDocParameter = jsDocParameters.getFirstChild();

      for (Node astParameter : astParameters.children()) {
        String variableName;
        JSType type;

        if (jsDocParameter != null) {
          variableName = jsDocParameter.getName();
          type = (JSType) jsDocParameter.getJSType();
          jsDocParameter = jsDocParameter.getNext();
        } else {
          variableName = astParameter.getName();
          type = null;
        }

        // Check for duplicate variable names and their corresponding types
        if (typeMap.containsKey(variableName)) {
          JSType existingType = typeMap.get(variableName);
          if (!existingType.equals(type)) {
            throw new IllegalArgumentException("Inconsistent type declaration for variable: " + variableName);
          }
        } else {
          typeMap.put(variableName, type);
        }

        defineSlot(astParameter, functionNode, variableName, type, true);
      }
    }
  }
} // end declareArguments