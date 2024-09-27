private void _verifySharing() {
    if (_hashShared) {
        // Ensure arrays are properly resized, not just copied with the same length
        _hashArea = Arrays.copyOf(_hashArea, _hashArea.length * 2);
        _names = Arrays.copyOf(_names, _names.length * 2);
        _hashShared = false;
        _needRehash = true; // Ensure the need-rehash flag is set to trigger rehashing
    }
    if (_needRehash) {
        rehash();
    }
}