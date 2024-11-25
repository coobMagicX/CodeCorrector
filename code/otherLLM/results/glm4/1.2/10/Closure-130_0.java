private void inlineAliases(GlobalNamespace namespace) {
  Deque<Name> workList = new ArrayDeque<>(namespace.getNameForest());
  while (!workList.isEmpty()) {
    Name name = workList.pop();

    if (name.type == Name.Type.GET || name.type == Name.Type.SET) {
      continue;
    }

    if (name.globalSets == 1 && name.localSets == 0 &&
        name.aliasingGets > 0) {
      List<Ref> refs = Lists.newArrayList(name.getRefs());
      for (Ref ref : refs) {
        if (ref.type == Type.ALIASING_GET && ref.scope.isLocal()) {
          if (inlineAliasIfPossible(ref, namespace)) {
            name.removeRef(ref);
          }
        }
      }

      // Check the aliasing gets after inlining to potentially add children
      List<Name> newChildren = Lists.newArrayList();
      for (Name child : name.props) {
        if ((child.type == Name.Type.OBJECTLIT ||
             child.type == Name.Type.FUNCTION) &&
            child.aliasingGets == 0 && child.props != null) {
          newChildren.addAll(child.props);
        } else if (child.aliasingGets == 0 && child.props == null) {
          newChildren.add(child);
        }
      }

      name.props = Lists.newArrayList();
      name.props.addAll(newChildren);
    }

    if ((name.type == Name.Type.OBJECTLIT ||
         name.type == Name.Type.FUNCTION) &&
        name.aliasingGets == 0 && name.props != null) {
      workList.addAll(name.props);
    }
  }
}

private boolean inlineAliasIfPossible(Ref alias, GlobalNamespace namespace) {
  Node aliasParent = alias.node.getParent();
  if (aliasParent.isName()) {
    Scope scope = alias.scope;
    Var aliasVar = scope.getVar(aliasParent.getString());
    ReferenceCollectingCallback collector =
        new ReferenceCollectingCallback(compiler,
            ReferenceCollectingCallback.DO_NOTHING_BEHAVIOR,
            Predicates.<Var>equalTo(aliasVar));
    (new NodeTraversal(compiler, collector)).traverseAtScope(scope);

    ReferenceCollection aliasRefs = collector.getReferences(aliasVar);
    if (aliasRefs.isWellDefined()
        && aliasRefs.firstReferenceIsAssigningDeclaration()
        && aliasRefs.isAssignedOnceInLifetime()) {
      int size = aliasRefs.references.size();
      Set<Node> newNodes = Sets.newHashSetWithExpectedSize(size - 1);
      for (int i = 1; i < size; i++) {
        ReferenceCollectingCallback.Reference aliasRef =
            aliasRefs.references.get(i);

        Node newNode = alias.node.cloneTree();
        aliasRef.getParent().replaceChild(aliasRef.getNode(), newNode);
        newNodes.add(newNode);
      }

      aliasParent.replaceChild(alias.node, IR.nullNode());
      compiler.reportCodeChange();

      namespace.scanNewNodes(alias.scope, newNodes);
      return true;
    }
  }

  return false;
}