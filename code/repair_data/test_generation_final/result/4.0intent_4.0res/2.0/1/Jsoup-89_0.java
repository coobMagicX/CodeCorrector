public String setValue(String val) {
    String oldVal = parent != null ? parent.get(this.key) : this.val;
    if (parent != null) {
        int i = parent.indexOfKey(this.key);
        if (i != Attributes.NotFound)
            parent.vals[i] = val;
    }
    this.val = val;
    return oldVal;
}