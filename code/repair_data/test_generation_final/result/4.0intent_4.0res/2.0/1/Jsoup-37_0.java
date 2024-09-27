public String html() {
    StringBuilder accum = new StringBuilder();
    html(accum);
    // Check if prettyPrint is set to false, and if so, do not trim the output.
    if (!outputSettings().prettyPrint()) {
        return accum.toString(); // Return without trimming
    }
    return accum.toString().trim(); // Previously existing code where trimming is applied
}