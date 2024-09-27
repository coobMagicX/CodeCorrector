public int writeValue() {
    // Most likely, object:
    if (_type == TYPE_OBJECT) {
        _gotName = false;
        ++_index;
        return STATUS_OK_AFTER_COLON;
    }

    // Ok, array?
    if (_type == TYPE_ARRAY) {
        int ix = _index;
        ++_index;
        return (ix < 0) ? STATUS_OK_AS_IS : STATUS_OK_AFTER_COMMA;
    }
    
    // Nope, root context
    // No commas within root context, but need space
    if (_currentName != null && !_currentName.isEmpty()) {
        if (!isFieldName(_currentName)) {
            throw new IllegalArgumentException("Invalid field name: " + _currentName);
        }
    }
    
    ++_index;
    return (_index == 0) ? STATUS_OK_AS_IS : STATUS_OK_AFTER_SPACE;
}

private boolean isFieldName(String name) {
    // Check if the name consists of field name characters (alphanumeric, underscores, and dashes)
    return name.matches("[a-zA-Z0-9_-]+");
}