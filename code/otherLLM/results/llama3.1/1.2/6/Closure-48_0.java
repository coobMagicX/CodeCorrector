void maybeDeclareQualifiedName(NodeTraversal t, JSDocInfo info,
    Node n, Node parent, Node rhsValue) {
  Node ownerNode = n.getFirstChild();
  String ownerName = ownerNode.getQualifiedName();
  String qName = n.getQualifiedName();
  String propName = n.getLastChild().getString();
  Preconditions.checkArgument(qName != null && ownerName != null);

  // Precedence of type information on GETPROPs:
  // 1) @type annnotation / @enum annotation
  // 2) ASSIGN to FUNCTION literal
  // 3) @param/@return annotation (with no function literal)
  // 4) ASSIGN to something marked @const
  // 5) ASSIGN to anything else
  //
  // 1, 3, and 4 are declarations, 5 is inferred, and 2 is a declaration iff
  // the function has jsdoc or has not been declared before.
  //
  // FUNCTION literals are special because TypedScopeCreator is very smart
  // about getting as much type information as possible for them.

  // Determining type for #1 + #3 + #4
  boolean inferred = true;
  if (info != null) {
    inferred = !(info.hasType()
        || info.hasEnumParameterType()
        || (info.isConstant() && rhsValue.getJSType() != null));
  }

  // Determining declaration for #2
  if (!inferred) {
    if (rhsValue != null && rhsValue.isFunction()) {
      declareArguments(rhsValue);
      inferred = !(info != null || !rhsValue.getJSType().isDeclared());
    }
  }

  // Rest of the function remains the same...
}