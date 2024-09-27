public void remove(SettableBeanProperty propToRm) {
    ArrayList<SettableBeanProperty> props = new ArrayList<SettableBeanProperty>(_size);
    String key = getPropertyName(propToRm).toLowerCase(); // Ensure case insensitivity by converting to lower case
    boolean found = false;

    for (int i = 0, end = _hashArea.length; i < end; i += 2) { // Start loop from 0 to check the correct key slot
        SettableBeanProperty prop = (SettableBeanProperty) _hashArea[i + 1];
        if (prop == null) {
            continue;
        }
        if (!found) {
            // Check against the lower-case version of the key in the hash area
            found = key.equals(getPropertyName(prop).toLowerCase());
            if (found) {
                // need to leave a hole here
                _propsInOrder[_findFromOrdered(prop)] = null;
                continue;
            }
        }
        props.add(prop);
    }
    if (!found) {
        throw new NoSuchElementException("No entry '" + propToRm.getName() + "' found, can't remove");
    }
    init(props);
}