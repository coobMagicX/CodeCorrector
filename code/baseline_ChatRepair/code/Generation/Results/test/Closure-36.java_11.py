if (value.isFunction()) {
    Node callNode = reference.getParent();
    if (callNode.isCall()) {
        CodingConvention convention = compiler.getCodingConvention();
        
        // Checking if the callNode represents subclass relationships or singleton getters
        SubclassRelationship relationship = convention.getClassesDefinedByCall(callNode);
        if (relationship != null || convention.isSingletonGetterCall(callNode)) { 
            // Prevent inlining if it's defining a subclass or is a singleton getter
            return false;
        }
    }
}
