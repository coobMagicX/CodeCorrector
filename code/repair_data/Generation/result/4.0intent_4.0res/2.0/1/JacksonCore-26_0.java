public void feedInput(byte[] buf, int start, int end) throws IOException {
    // Must not have remaining input
    if (_inputPtr < _inputEnd) {
        _reportError("Still have %d undecoded bytes, should not call 'feedInput'", _inputEnd - _inputPtr);
    }
    if (end < start) {
        _reportError("Input end (%d) may not be before start (%d)", end, start);
    }
    // and shouldn't have been marked as end-of-input
    if (_endOfInput) {
        _reportError("Already closed, can not feed more input");
    }
    
    // Calculate the actual number of bytes processed since last input
    int bytesProcessed = _inputEnd - _inputPtr;

    // Update total bytes processed
    _currInputProcessed += bytesProcessed;

    // Reset the row start to account for new input
    _currInputRowStart = -start;

    // Update buffer settings
    _inputBuffer = buf;
    _inputPtr = start;
    _inputEnd = end;
    // Reset original buffer length to reflect new input length
    _origBufferLen = end - start;
}