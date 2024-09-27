private void readTypeVariables() {
    registerTypeVariablesOn(typeVariable);
    registerTypeVariablesOn(getActualTypeArgumentFor(typeVariable));
}