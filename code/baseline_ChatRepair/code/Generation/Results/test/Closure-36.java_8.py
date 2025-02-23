if (value.isFunction()) {
  Node callNode = reference.getParent();
  if (callNode.isCall()) {
    CodingConvention convention = compiler.getCodingConvention();

    // Don't inline subclass definitions into calls defining classes
    SubclassRelationship relationship = convention.getClassesDefinedByCall(callNode);
    if (relationship != null) {
      return false;
    }

    // Don't inline singleton getter methods.
    if (convention.isSingletonGetter(callNode)) {
      return false;
    }
  }
}
