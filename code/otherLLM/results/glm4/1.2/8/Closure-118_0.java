private void handleObjectLit(NodeTraversal t, Node n) {
  for (Node child = n.getFirstChild();
      child != null;
      child = child.getNext()) {
    // Maybe STRING, GET, SET

    // We should never see a mix of numbers and strings.
    String name = child.getString();
    T type = typeSystem.getType(getScope(), n, name);

    Property prop = getProperty(name);
    if (prop == null) {
      // If the property does not exist, skip processing this child node
      continue;
    }

    if (!prop.scheduleRenaming(child, processProperty(t, prop, type, null))) {
      // TODO(user): It doesn't look like the user can do much in this
      // case right now.
      if (propertiesToErrorFor.containsKey(name)) {
        compiler.report(JSError.make(
            t.getSourceName(), child, propertiesToErrorFor.get(name),
            Warnings.INVALIDATION, name,
            (type == null ? "null" : type.toString()), n.toString(), ""));
      }
    }
  }
}

private T processProperty(NodeTraversal t, Property prop, T type, T relatedType) {
  type = typeSystem.restrictByNotNullOrUndefined(type);
  if (prop.skipRenaming || typeSystem.isInvalidatingType(type)) {
    return null;
  }

  Iterable<T> alternatives = typeSystem.getTypeAlternatives(type);
  if (alternatives != null) {
    T firstType = relatedType;
    for (T subType : alternatives) {
      T lastType = processProperty(t, prop, subType, firstType);
      if (lastType != null) {
        firstType = firstType == null ? lastType : firstType;
      }
    }
    return firstType;
  } else {
    T topType = typeSystem.getTypeWithProperty(prop.name, type);
    if (typeSystem.isInvalidatingType(topType)) {
      return null;
    }
    prop.addType(type, topType, relatedType);
    return topType;
  }
}