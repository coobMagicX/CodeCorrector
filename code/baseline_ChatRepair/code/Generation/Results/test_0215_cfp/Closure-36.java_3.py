if (value.isFunction()) {
  Node callNode = reference.getParent();
  if (callNode.isCall()) {
    CodingConvention convention = compiler.getCodingConvention();

    // Check if the function call defines a subclass, which should not be inlined.
    SubclassRelationship relationship = convention.getClassesDefinedByCall(callNode);
    if (relationship != null) {
      return false;
    }

    // Added check to determine if the function is a singleton getter method.
    if (convention.isSingletonGetterMethod(callNode)) {
      return false;
    }
  }
}
