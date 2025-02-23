if (value.isFunction()) {
  Node callNode = reference.getParent();
  if (callNode.isCall()) {
    CodingConvention convention = compiler.getCodingConvention();

    // Don't inline subclass definitions into class defining calls
    SubclassRelationship relationship = convention.getClassesDefinedByCall(callNode);
    if (relationship != null) {
      return false;
    }

    // Issue 668: Don't inline singleton getter method calls.
    if (convention.isSingletonGetter(callNode)) {
      return false;
    }
  }
}
