if (value.isFunction()) {
  Node callNode = reference.getParent();
  if (callNode.isCall()) {
    CodingConvention convention = compiler.getCodingConvention();
    SubclassRelationship relationship = convention.getClassesDefinedByCall(callNode);
    if (relationship != null) {
      return false;
    }

    // Correcting this section for handling singleton getter methods
    if (convention.isSingletonGetter(callNode)) {
      // Do not inline if this is a singleton getter
      return false;
    }

    // Bug 2388531 prevention noted previously
    String className = convention.getSingletonGetterClassName(callNode);
    if (className != null) {
      return false;
    }
  }
}
