public JsonToken nextToken() throws IOException {
    // Check for _allowMultipleMatches - false and at least there is one token - which is _currToken
    // check for no buffered context _exposedContext - null
    // If all the conditions matches then check for scalar / non-scalar property
    // if not scalar and ended successfully, then return null
    // else if scalar, and scalar not present in obj/array and !includePath and INCLUDE_ALL matched once
    // then return null
    TokenFilterContext ctxt = _exposedContext;

    if (ctxt != null) {
        while (true) {
            JsonToken t = ctxt.nextTokenToRead();
            if (t != null) {
                _currToken = t;
                return t;
            }
            // all done with buffered stuff?
            if (ctxt == _headContext) {
                _exposedContext = null;
                break;
            }
            // If not, traverse down the context chain
            ctxt = _headContext.findChildOf(ctxt);
            _exposedContext = ctxt;
            if (ctxt == null) { // should never occur
                throw _constructError("Unexpected problem: chain of filtered context broken");
            }
        }
    }

    // Main token processing loop
    JsonToken t = delegate.nextToken();
    if (t == null) {
        return (_currToken = t);
    }

    // otherwise... to include or not?
    TokenFilter f;

    switch (t.id()) {
        // Handle each token type appropriately
        case ID_FIELD_NAME:
            final String name = delegate.getCurrentName();
            f = _headContext.setFieldName(name);
            if (f == TokenFilter.INCLUDE_ALL) {
                _itemFilter = f;
                return (_currToken = t);
            }
            if (f == null) {
                delegate.nextToken();
                delegate.skipChildren();
                break;
            }
            f = f.includeProperty(name);
            if (f == null) {
                delegate.nextToken();
                delegate.skipChildren();
                break;
            }
            _itemFilter = f;
            if (f == TokenFilter.INCLUDE_ALL) {
                if (!_allowMultipleMatches && name.equals(_matchFieldName) && _matchedOnce) {
                    // Skip the token if it's a repeated match and multiple matches are not allowed
                    delegate.nextToken();
                    delegate.skipChildren();
                    break;
                } else if (name.equals(_matchFieldName)) {
                    _matchedOnce = true; // Mark that we have matched the required field name once
                }
                return (_currToken = t);
            }
            break;

        // Implement similar logic for other token types if necessary
        // Other cases (arrays, objects, end structures, etc.)
        default:
            f = _itemFilter;
            if (f == TokenFilter.INCLUDE_ALL) {
                return (_currToken = t);
            }
            if (f != null) {
                f = _headContext.checkValue(f);
                if ((f == TokenFilter.INCLUDE_ALL) || ((f != null) && f.includeValue(delegate))) {
                    return (_currToken = t);
                }
            }
            break;
    }

    // We get here if token was not yet found; offlined handling
    return _nextToken2();
}