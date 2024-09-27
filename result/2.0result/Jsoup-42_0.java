public List<Connection.KeyVal> formData() {
    ArrayList<Connection.KeyVal> data = new ArrayList<Connection.KeyVal>();

    // iterate the form control elements and accumulate their values
    for (Element el: elements) {
        if (!el.tag().isFormSubmittable() || el.hasAttr("disabled")) continue; // skip non-submittable and disabled elements
        String name = el.attr("name");
        if (name.length() == 0) continue;
        String type = el.attr("type");

        if ("select".equals(el.tagName())) {
            Elements options = el.select("option[selected]");
            for (Element option: options) {
                data.add(HttpConnection.KeyVal.create(name, option.val()));
            }
            if (options.isEmpty()) {
                Element option = el.select("option").first();
                if (option != null) {
                    data.add(HttpConnection.KeyVal.create(name, option.val()));
                }
            }
        } else if ("checkbox".equalsIgnoreCase(type) || "radio".equalsIgnoreCase(type)) {
            // only add checkbox or radio if they have the 'checked' attribute, handle default 'on' value
            if (el.hasAttr("checked")) {
                String val = el.val().length() > 0 ? el.val() : "on"; // use 'on' if value is empty
                data.add(HttpConnection.KeyVal.create(name, val));
            }
        } else if ("textarea".equals(el.tagName())) {
            data.add(HttpConnection.KeyVal.create(name, el.text())); // ensure textareas are processed correctly
        } else {
            data.add(HttpConnection.KeyVal.create(name, el.val()));
        }
    }
    return data;
}