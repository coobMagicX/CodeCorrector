protected void verifyNonDup(AnnotatedWithParams newOne, int typeIndex, boolean explicit) {
    final int mask = (1 << typeIndex);
    _hasNonDefaultCreator = true;
    AnnotatedWithParams oldOne = _creators[typeIndex];
    // already had an explicitly marked one?
    if (oldOne != null) {
        boolean oldExplicit = (_explicitCreators & mask) != 0;
        if (explicit && oldExplicit) {
            // both explicit: verify
            throw new IllegalArgumentException("Conflicting " + TYPE_DESCS[typeIndex] + 
                " creators: already had explicitly marked " + oldOne + ", encountered " + newOne);
        } else if (!explicit && oldExplicit) {
            // old one is explicit and new one is not, skip
            return;
        } else if (explicit && !oldExplicit) {
            // new one is explicit and old one is not, replace
        } else {
            // neither are explicit, use subclass if applicable
            if (oldOne.getClass().isAssignableFrom(newOne.getClass())) {
                // new type more specific, use it
            } else {
                // new type not more specific or the same, skip
                return;
            }
        }
    }
    if (explicit) {
        _explicitCreators |= mask;
    }
    _creators[typeIndex] = _fixAccess(newOne);
}