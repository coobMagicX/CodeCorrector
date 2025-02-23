  if (value.isFunction()) {
    Node callNode = reference.getParent();
    if (callNode.isCall()) {
      CodingConvention convention = compiler.getCodingConvention();
      
      // Prevent inlining of subclass definitions into class defining calls
      SubclassRelationship relationship = convention.getClassesDefinedByCall(callNode);
      if (relationship != null) {
        return false;
      }

      // Prevent inlining of singleton getter methods to avoid context issues
      if (convention.isSingletonGetter(callNode)) {
         return false;
      }
      
      // Add any additional checks for other forms/functions that shouldn't be inlined
    }
  }
