protected JavaType _fromVariable(TypeVariable<?> type, TypeBindings context) {
    final String name = type.getName();
    if (context == null) {
        return _unknownType();
    } else {
        // Check if the type name is a placeholder
        if (context.isPlaceholder(name)) {
            // If it is a placeholder, return the placeholder type
            return context.resolvePlaceholder(name);
        }

        // Ok: here's where context might come in handy!
        JavaType actualType = context.findType(name);
        if (actualType != null) {
            return actualType;
        }
    }

    Type[] bounds = type.getBounds();

    // With type variables we must use bound information.
    // Theoretically this gets tricky, as there may be multiple
    // bounds ("... extends A & B"); and optimally we might
    // want to choose the best match. Also, bounds are optional;
    // but here we are lucky in that implicit "Object" is
    // added as bounds if so.
    // Either way let's just use the first bound, for now, and
    // worry about better match later on if there is need.

    context._addPlaceholder(name);

    // Return the first bound as the JavaType
    return _constructType(bounds[0], context);
}