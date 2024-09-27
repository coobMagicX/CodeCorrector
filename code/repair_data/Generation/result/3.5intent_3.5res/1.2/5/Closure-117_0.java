while (objectType != null && !objectType.hasOwnProperty(propName)) {
  if (objectType.getImplicitPrototype() == null) {
    // Property not found in the prototype chain
    break;
  }
  objectType = objectType.getImplicitPrototype();
}