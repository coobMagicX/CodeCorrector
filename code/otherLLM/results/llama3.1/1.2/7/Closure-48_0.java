void maybeDeclareQualifiedName(NodeTraversal t, JSDocInfo info,
    Node n, Node parent, Node rhsValue) {
  // ...
  if (rhsValue != null && rhsValue.isFunction()) {
    FunctionType funcType = FunctionType.cast(rhsValue.getJSType());
    if (funcType != null && !scope.isDeclared(qName, false)) {
      inferred = false;
    }
  }
  // ...
}

void defineSlot(Node n, Node parent, JSType type, boolean inferred) {
  // ...
}

void stubDeclarations.add(new StubDeclaration(
    n,
    t.getInput() != null && t.getInput().isExtern(),
    ownerName));
// ...
}