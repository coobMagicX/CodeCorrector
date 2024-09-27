public void matchConstraint(JSType constraint) {
    // We only want to match constraints on anonymous types.
    if (hasReferenceName()) {
        return;
    }

    // Check if the constraint is a union type and handle each possibility
    if (constraint.isUnionType()) {
        UnionType unionType = constraint.toMaybeUnionType();
        for (JSType alternate : unionType.getAlternates()) {
            if (alternate.isRecordType()) {
                matchRecordTypeConstraint(alternate.toObjectType());
            }
        }
    } else if (constraint.isRecordType()) {
        // Handle the case where the constraint object is a record type.
        matchRecordTypeConstraint(constraint.toObjectType());
    }
}