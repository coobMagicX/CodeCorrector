public void removeIgnoreCase(String key) {
    Validate.notEmpty(key);
    if (attributes == null)
        return;
    List<String> toRemove = new ArrayList<>();
    for (String attrKey : attributes.keySet()) {
        if (attrKey.equalsIgnoreCase(key)) {
            toRemove.add(attrKey);
        }
    }
    for (String attrKey : toRemove) {
        attributes.remove(attrKey);
    }
}