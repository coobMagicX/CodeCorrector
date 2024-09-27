public JsonToken nextToken() throws IOException {
    if (!_allowMultipleMatches && (_currToken != null) && (_exposedContext == null)) {
        if (_currToken.isStructEnd()) {
            if (_headContext.isStartHandled() && _headContext.inScope()) {
                return (_currToken = null);
            }
        } else if (_currToken.isScalarValue()) {
            if (!_headContext.isStartHandled() && (_itemFilter == TokenFilter.INCLUDE_ALL)) {
                return (_currToken = null);
            }
        }
    }

    TokenFilterContext ctxt = _exposedContext;

    if (ctxt != null) {
        while (true) {
            JsonToken t = ctxt.nextTokenToRead();
            if (t != null) {
                _currToken = t;
                return t;
            }
            if (ctxt == _headContext) {
                _exposedContext = null;
                break;
            }
            ctxt = _headContext.findChildOf(ctxt);
            _exposedContext = ctxt;
            if (ctxt == null) { 
                throw _constructError("Unexpected problem: chain of filtered context broken");
            }
        }
    }

    JsonToken t = delegate.nextToken();
    if (t == null) {
        _currToken = t;
        return t;
    }

    TokenFilter f;

    switch (t.id()) {
    case ID_START_ARRAY:
    case ID_START_OBJECT:
        f = _itemFilter;
        if (f == TokenFilter.INCLUDE_ALL) {
            _headContext = _headContext.createChildContext(t, f, true);
            return (_currToken = t);
        }
        if (f == null) {
            delegate.skipChildren();
            break;
        }
        f = _headContext.checkValue(f);
        if (f == null) {
            delegate.skipChildren();
            break;
        }
        if (f != TokenFilter.INCLUDE_ALL) {
            f = t.isStartObject() ? f.filterStartObject() : f.filterStartArray();
        }
        _itemFilter = f;
        if (f == TokenFilter.INCLUDE_ALL) {
            _headContext = _headContext.createChildContext(t, f, true);
            return (_currToken = t);
        }
        _headContext = _headContext.createChildContext(t, f, false);
        if (_includePath) {
            t = _nextTokenWithBuffering(_headContext);
            if (t != null) {
                _currToken = t;
                return t;
            }
        }
        break;

    case ID_END_ARRAY:
    case ID_END_OBJECT:
        if (_headContext.isStartHandled()) {
            f = _headContext.getFilter();
            if (f != null && f != TokenFilter.INCLUDE_ALL) {
                f.filterFinishArray();
            }
            _headContext = _headContext.getParent();
            _itemFilter = _headContext.getFilter();
            return (_currToken = t);
        }
        _headContext = _headContext.getParent();
        _itemFilter = _headContext.getFilter();
        break;

    case ID_FIELD_NAME:
        String name = delegate.getCurrentName();
        f = _headContext.setFieldName(name);
        if (f == TokenFilter.INCLUDE_ALL) {
            _itemFilter = f;
            if (!_includePath) {
                if (_includeImmediateParent && !_headContext.isStartHandled()) {
                    t = _headContext.nextTokenToRead(); 
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

    default: // scalar value
        f = _itemFilter;
        if (f == TokenFilter.INCLUDE_ALL) {
            return (_currToken = t);
        }
        if (f != null) {
            f = _headContext.checkValue(f);
            if (f == TokenFilter.INCLUDE_ALL || (f != null && f.includeValue(delegate))) {
                return (_currToken = t);
            }
        }
        break;
    }

    return _nextToken2();
}