public String html() {
    StringBuilder accum = new StringBuilder();
    html(accum);
    return accum.toString().trim();
}

public Element html(String html) {
    StringBuilder accum = new StringBuilder();
    accum.append(html);
    return this;
}