private void readTypeVariables() {
    Type[] bounds = typeVariable.getBounds();
    if (bounds != null && bounds.length > 0) {
        registerTypeVariablesOn(bounds[0]);
    }
    registerTypeVariablesOn(getActualTypeArgumentFor(typeVariable));
}