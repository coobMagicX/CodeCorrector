protected void checkRequiredOptions() throws MissingOptionException {
    // if there are required options that have not been processed
    if (getRequiredOptions().size() > 0) {
        Iterator iter = getRequiredOptions().iterator();
        StringBuilder buff = new StringBuilder("Missing required option");
        buff.append(getRequiredOptions().size() == 1 ? "" : "s");
        buff.append(": ");

        // loop through the required options
        while (iter.hasNext()) {
            buff.append(iter.next());
            if (iter.hasNext()) {
                buff.append(", "); // add a comma separator if there are more missing options
            }
        }

        throw new MissingOptionException(buff.toString());
    }
}