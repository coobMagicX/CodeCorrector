public JsonToken nextToken() throws IOException {
    //Check for _allowMultipleMatches - false and atleast there is one token - which is _currToken
    // check for no buffered context _exposedContext - null
    //If all the conditions matches then check for scalar / non-scalar property
        //if not scalar and ended successfully, then return null
        //else if scalar, and scalar not present in obj/array and !includePath and INCLUDE_ALL matched once
        // then return null 
    // Anything buffered?
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
                if (ctxt.inArray()) {
                    t = delegate.getCurrentToken();
                    if (t != JsonToken.START_ARRAY) {
                        _currToken = t;
                        return t;
                    }
                }
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

    // If not, need to read more. If we got any:
    JsonToken t = delegate.nextToken();
    if (t == null) {
        // no strict need to close, since we have no state here
        return (_currToken = t);
    }

    // otherwise... to include or not?
    TokenFilter f;

    switch (t.id()) {
        case ID_START_ARRAY:
            f = _itemFilter;
            if (f == TokenFilter.INCLUDE_ALL) {
                _headContext = _headContext.createChildArrayContext(f, true);
                return (_currToken = t);
            }
            if (f == null) { // does this occur?
                delegate.skipChildren();
                break;
            }
            // Otherwise still iffy, need to check
            f = _headContext.checkValue(f);
            if (f == null) {
                delegate.skipChildren();
                break;
            }
            if (f != TokenFilter.INCLUDE_ALL) {
                f = f.filterStartArray();
            }
            _itemFilter = f;
            if (f == TokenFilter.INCLUDE_ALL) {
                _headContext = _headContext.createChildArrayContext(f, true);
                return (_currToken = t);
            }
            _headContext = _headContext.createChildArrayContext(f, false);

            // Also: only need buffering if parent path to be included
            if (_includePath) {
                t = _nextTokenWithBuffering(_headContext);
                if (t != null) {
                    _currToken = t;
                    return t;
                }
            }
            break;

        case ID_START_OBJECT:
            f = _itemFilter;
            if (f == TokenFilter.INCLUDE_ALL) {
                _headContext = _headContext.createChildObjectContext(f, true);
                return (_currToken = t);
            }
            if (f == null) { // does this occur?
                delegate.skipChildren();
                break;
            }
            // Otherwise still iffy, need to check
            f = _headContext.checkValue(f);
            if (f == null) {
                delegate.skipChildren();
                break;
            }
            if (f != TokenFilter.INCLUDE_ALL) {
                f = f.filterStartObject();
            }
            _itemFilter = f;
            if (f == TokenFilter.INCLUDE_ALL) {
                _headContext = _headContext.createChildObjectContext(f, true);
                return (_currToken = t);
            }
            _headContext = _headContext.createChildObjectContext(f, false);
            // Also: only need buffering if parent path to be included
            if (_includePath) {
                t = _nextTokenWithBuffering(_headContext);
                if (t != null) {
                    _currToken = t;
                    return t;
                }
            }
            // note: inclusion of surrounding Object handled separately via
            // FIELD_NAME
            break;

        case ID_END_ARRAY:
        case ID_END_OBJECT:
            {
                boolean returnEnd = _headContext.isStartHandled();
                f = _headContext.getFilter();
                if ((f != null) && (f != TokenFilter.INCLUDE_ALL)) {
                    f.filterFinishArray();
                }
                _headContext = _headContext.getParent();
                _itemFilter = _headContext.getFilter();
                if (returnEnd) {
                    return (_currToken = t);
                }
            }
            break;

        case ID_FIELD_NAME:
            {
                final String name = delegate.getCurrentName();
                // note: this will also set 'needToHandleName'
                f = _headContext.setFieldName(name);
                if (f == TokenFilter.INCLUDE_ALL) {
                    _itemFilter = f;
                    if (!_includePath) {
                        // Minor twist here: if parent NOT included, may need to induce output of
                        // surrounding START_OBJECT/END_OBJECT
                        if (_includeImmediateParent && !_headContext.isStartHandled()) {
                            t = _headContext.nextTokenToRead(); // returns START_OBJECT but also marks it handled
                            _exposedContext = _headContext;
                        }
                    }
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
                    if (_includePath) {
                        return (_currToken = t);
                    }
                }
                if (_includePath) {
                    t = _nextTokenWithBuffering(_headContext);
                    if (t != null) {
                        _currToken = t;
                        return t;
                    }
                }
                break;
            }

        default: // scalar value
            f = _itemFilter;
            if (f == TokenFilter.INCLUDE_ALL) {
                return (_currToken = t);
            }
            if (f != null) {
                f = _headContext.checkValue(f);
                if ((f == TokenFilter.INCLUDE_ALL)
                        || ((f != null) && f.includeValue(delegate))) {
                    return (_currToken = t);
                }
            }
            // Otherwise not included (leaves must be explicitly included)
            break;
    }

    // We get here if token was not yet found; offlined handling
    return _nextToken2();
}

protected JsonToken _nextTokenWithBuffering(final TokenFilterContext buffRoot)
        throws IOException {
    main_loop:
    while (true) {
        JsonToken t = delegate.nextToken();
        if (t == null) { // is this even legal?
            return t;
        }
        TokenFilter f;

        // One simplification here: we know for a fact that the item filter is
        // neither null nor 'include all', for most cases; the only exception
        // being FIELD_NAME handling

        switch (t.id()) {
            case ID_START_ARRAY:
                f = _headContext.checkValue(_itemFilter);
                if (f == null) {
                    delegate.skipChildren();
                    continue main_loop;
                }
                if (f != TokenFilter.INCLUDE_ALL) {
                    f = f.filterStartArray();
                }
                _itemFilter = f;
                if (f == TokenFilter.INCLUDE_ALL) {
                    _headContext = _headContext.createChildArrayContext(f, true);
                    return _nextBuffered(buffRoot);
                }
                _headContext = _headContext.createChildArrayContext(f, false);
                // but if we didn't figure it out yet, need to buffer possible events
                if (_includePath) {
                    t = _nextTokenWithBuffering(_headContext);
                    if (t != null) {
                        _currToken = t;
                        return t;
                    }
                }
                continue main_loop;

            case ID_START_OBJECT:
                f = _itemFilter;
                if (f == TokenFilter.INCLUDE_ALL) {
                    _headContext = _headContext.createChildObjectContext(f, true);
                    return t;
                }
                if (f == null) { // does this occur?
                    delegate.skipChildren();
                    continue main_loop;
                }
                // Otherwise still iffy, need to check
                f = _headContext.checkValue(f);
                if (f == null) {
                    delegate.skipChildren();
                    continue main_loop;
                }
                if (f != TokenFilter.INCLUDE_ALL) {
                    f = f.filterStartObject();
                }
                _itemFilter = f;
                if (f == TokenFilter.INCLUDE_ALL) {
                    _headContext = _headContext.createChildObjectContext(f, true);
                    return _nextBuffered(buffRoot);
                }
                _headContext = _headContext.createChildObjectContext(f, false);
                if (_includePath) {
                    t = _nextTokenWithBuffering(_headContext);
                    if (t != null) {
                        _currToken = t;
                        return t;
                    }
                }
                continue main_loop;

            case ID_END_ARRAY:
            case ID_END_OBJECT:
                {
                    boolean returnEnd = _headContext.isStartHandled();
                    f = _headContext.getFilter();
                    if ((f != null) && (f != TokenFilter.INCLUDE_ALL)) {
                        f.filterFinishArray();
                    }
                    _headContext = _headContext.getParent();
                    _itemFilter = _headContext.getFilter();
                    if (returnEnd) {
                        return t;
                    }
                }
                continue main_loop;

            case ID_FIELD_NAME:
                {
                    final String name = delegate.getCurrentName();
                    f = _headContext.setFieldName(name);
                    if (f == TokenFilter.INCLUDE_ALL) {
                        _itemFilter = f;
                        return _nextBuffered(buffRoot);
                    }
                    if (f == null) { // filter out the value
                        delegate.nextToken();
                        delegate.skipChildren();
                        continue main_loop;
                    }
                    f = f.includeProperty(name);
                    if (f == null) { // filter out the value
                        delegate.nextToken();
                        delegate.skipChildren();
                        continue main_loop;
                    }
                    _itemFilter = f;
                    if (f == TokenFilter.INCLUDE_ALL) {
                        if (_includePath) {
                            return _nextBuffered(buffRoot);
                        }
//                        if (_includeImmediateParent) { ...
                        continue main_loop;
                    }
                    if (_includePath) {
                        t = _nextTokenWithBuffering(_headContext);
                        if (t != null) {
                            _currToken = t;
                            return t;
                        }
                    }
                }
                continue main_loop;

            default: // scalar value
                f = _itemFilter;
                if (f == TokenFilter.INCLUDE_ALL) {
                    return _nextBuffered(buffRoot);
                }
                if (f != null) {
                    f = _headContext.checkValue(f);
                    if ((f == TokenFilter.INCLUDE_ALL)
                            || ((f != null) && f.includeValue(delegate))) {
                        return _nextBuffered(buffRoot);
                    }
                }
                // Otherwise not included (leaves must be explicitly included)
                continue main_loop;
        }
    }
}

private JsonToken _nextBuffered(TokenFilterContext buffRoot) throws IOException {
    _exposedContext = buffRoot;
    TokenFilterContext ctxt = buffRoot;
    JsonToken t = ctxt.nextTokenToRead();
    if (t != null) {
        return t;
    }
    while (true) {
        // all done with buffered stuff?
        if (ctxt == _headContext) {
            throw _constructError("Internal error: failed to locate expected buffered tokens");
        }
        // If not, traverse down the context chain
        ctxt = _exposedContext.findChildOf(ctxt);
        _exposedContext = ctxt;
        if (ctxt == null) { // should never occur
            throw _constructError("Unexpected problem: chain of filtered context broken");
        }
        t = _exposedContext.nextTokenToRead();
        if (t != null) {
            return t;
        }
    }
}