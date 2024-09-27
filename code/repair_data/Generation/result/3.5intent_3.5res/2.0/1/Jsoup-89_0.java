public String setValue(String val) {
    if (parent == null) {
        // Handle the case when the attribute is orphaned (parent is null)
        // For example, you can throw an exception or return a default value
        throw new IllegalStateException("Cannot set value on orphan attribute");
    }
    
    String oldVal = parent.get(this.key);
    int i = parent.indexOfKey(this.key);
    if (i != Attributes.NotFound) {
        parent.vals[i] = val;
    }
    
    this.val = val;
    return Attributes.checkNotNull(oldVal);
}