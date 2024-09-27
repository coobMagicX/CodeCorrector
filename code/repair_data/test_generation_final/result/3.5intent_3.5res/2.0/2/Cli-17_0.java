protected void burstToken(String token, boolean stopAtNonOption) {
    if (options.hasOption(token)) {
        tokens.add("-" + token);
        currentOption = options.getOption(token);
    } else {
        if (stopAtNonOption) {
            process(token);
        } else {
            tokens.add(token);
        }
    }
}