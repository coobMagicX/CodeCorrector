public JsonToken nextToken() throws IOException {
    _binaryValue = null;
    if (_nextToken != null) {
        JsonToken t = _nextToken;
        _currToken = t;
        _nextToken = null;
        _updateState(t);
        return t;
    }
    int token = _xmlTokens.next();

    while (token == XmlTokenStream.XML_START_ELEMENT) {
        if (_mayBeLeaf) {
            _nextToken = JsonToken.FIELD_NAME;
            _parsingContext = _parsingContext.createChildObjectContext(-1, -1);
            return (_currToken = JsonToken.START_OBJECT);
        }
        if (_parsingContext.inArray()) {
            token = _xmlTokens.next();
            _mayBeLeaf = true;
            continue;
        }
        String name = _xmlTokens.getLocalName();
        _parsingContext.setCurrentName(name);

        if (_namesToWrap != null && _namesToWrap.contains(name)) {
            _xmlTokens.repeatStartElement();
        }

        _mayBeLeaf = true;
        return (_currToken = JsonToken.FIELD_NAME);
    }

    switch (token) {
    case XmlTokenStream.XML_END_ELEMENT:
        if (_mayBeLeaf) {
            _mayBeLeaf = false;
            return (_currToken = JsonToken.VALUE_NULL); // Expose as empty object not null
        }
        JsonToken endToken = _parsingContext.inArray() ? JsonToken.END_ARRAY : JsonToken.END_OBJECT;
        _updateState(endToken);
        return _currToken;

    case XmlTokenStream.XML_ATTRIBUTE_NAME:
        if (_mayBeLeaf) {
            _mayBeLeaf = false;
            _nextToken = JsonToken.FIELD_NAME;
            _currText = _xmlTokens.getText();
            _parsingContext = _parsingContext.createChildObjectContext(-1, -1);
            return (_currToken = JsonToken.START_OBJECT);
        }
        _parsingContext.setCurrentName(_xmlTokens.getLocalName());
        return (_currToken = JsonToken.FIELD_NAME);

    case XmlTokenStream.XML_ATTRIBUTE_VALUE:
        _currText = _xmlTokens.getText();
        return (_currToken = JsonToken.VALUE_STRING);

    case XmlTokenStream.XML_TEXT:
        _currText = _xmlTokens.getText();
        if (_mayBeLeaf) {
            _mayBeLeaf = false;
            _xmlTokens.skipEndElement();
            if (_parsingContext.inArray()) {
                if (_isEmpty(_currText)) {
                    _currToken = JsonToken.END_ARRAY;
                    _updateState(JsonToken.END_ARRAY);
                    return _currToken;
                }
            }
            return (_currToken = JsonToken.VALUE_STRING);
        } else {
            if (_parsingContext.inObject()
                    && (_currToken != JsonToken.FIELD_NAME) && _isEmpty(_currText)) {
                _currToken = JsonToken.END_OBJECT;
                _updateState(JsonToken.END_OBJECT);
                return _currToken;
            }
        }
        _parsingContext.setCurrentName(_cfgNameForTextElement);
        _nextToken = JsonToken.VALUE_STRING;
        return (_currToken = JsonToken.FIELD_NAME);

    case XmlTokenStream.XML_END:
        return (_currToken = null);
    }

    _throwInternal();
    return null;
}

private void _updateState(JsonToken t) {
    switch (t) {
    case START_OBJECT:
        _parsingContext = _parsingContext.createChildObjectContext(-1, -1);
        break;
    case START_ARRAY:
        _parsingContext = _parsingContext.createChildArrayContext(-1, -1);
        break;
    case END_OBJECT:
    case END_ARRAY:
        _parsingContext = _parsingContext.getParent();
        _namesToWrap = _parsingContext.getNamesToWrap();
        break;
    case FIELD_NAME:
        _parsingContext.setCurrentName(_xmlTokens.getLocalName());
        break;
    default:
    }
}