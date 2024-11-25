void handleGet(JSModule module, Scope scope,
        Node n, Node parent, String name) {
    // Create or get an existing Name instance for the given name
    Name nameInstance = getOrCreateName(name);
    
    // Update the global namespace with the reference to the name
    updateGlobalNamespace(module, scope, nameInstance);
}