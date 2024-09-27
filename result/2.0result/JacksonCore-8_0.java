public char[] getTextBuffer() {
    // Check if explicitly reset to empty
    if (_inputBuffer == null && _resultArray == null && _resultString == null && !_hasSegments && _currentSegment == null) {
        return new char[0]; // Return an empty array when everything is reset
    }
    // Are we just using shared input buffer?
    if (_inputStart >= 0) return _inputBuffer;
    if (_resultArray != null) return _resultArray;
    if (_resultString != null) {
        return (_resultArray = _resultString.toCharArray());
    }
    // Nope; but does it fit in just one segment?
    if (!_hasSegments && _currentSegment != null) return _currentSegment;
    // Nope, need to have/create a non-segmented array and return it
    return contentsAsArray();
}