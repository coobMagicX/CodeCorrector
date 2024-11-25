public void visit(NodeTraversal t, Node n, Node parent) {

  // Record global variable and function declarations
  if (t.inGlobalScope()) {
    if (NodeUtil.isVarDeclaration(n)) {
      NameInformation ns = createNameInformation(t, n, parent);
      Preconditions.checkNotNull(ns);
      recordSet(ns.name, n);
    } else if (NodeUtil.isFunctionDeclaration(n)) {
      Node nameNode = n.getFirstChild();
      NameInformation ns = createNameInformation(t, nameNode, n);
      if (ns != null) {
        JsName nameInfo = getName(nameNode.getString(), true);
        recordSet(nameInfo.name, nameNode);
      }
    } else if (NodeUtil.isObjectLitKey(n, parent)) {
      NameInformation ns = createNameInformation(t, n, parent);
      if (ns != null) {
        recordSet(ns.name, n);
      }
    }
  }

  // Record assignments and call sites
  if (n.isAssign()) {
    Node nameNode = n.getFirstChild();

    NameInformation ns = createNameInformation(t, nameNode, n);
    if (ns != null) {
      if (ns.isPrototype) {
        recordPrototypeSet(ns.prototypeClass, ns.prototypeProperty, n);
      } else {
        // Corrected the call to recordSet by passing 'nameNode' instead of 'n'
        recordSet(ns.name, nameNode);
      }
    }
  } else if (n.isCall()) {
    Node nameNode = n.getFirstChild();
    NameInformation ns = createNameInformation(t, nameNode, n);
    if (ns != null && ns.onlyAffectsClassDef) {
      JsName name = getName(ns.name, false);
      if (name != null) {
        refNodes.add(new ClassDefiningFunctionNode(
            name, nameNode, parent, parent.getParent()));
      }
    }
  }
}

private void recordSet(String name, Node node) {
  JsName jsn = getName(name, true);
  JsNameRefNode nameRefNode = new JsNameRefNode(jsn, node);
  refNodes.add(nameRefNode);

  // Now, look at all parent names and record that their properties have
  // been written to.
  if (node.isGetElem()) {
    recordWriteOnProperties(name);
  } else if (name.indexOf('.') != -1) {
    recordWriteOnProperties(name.substring(0, name.lastIndexOf('.')));
  }
}