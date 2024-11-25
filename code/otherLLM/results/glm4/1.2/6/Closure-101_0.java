protected CompilerOptions createOptions() {
    CompilerOptions options = new CompilerOptions();
    options.setCodingConvention(new ClosureCodingConvention());
    CompilationLevel level = flags.compilation_level;
    level.setOptionsForCompilationLevel(options);
    if (flags.debug) {
        level.setDebugOptionsForCompilationLevel(options);
    }

    WarningLevel wLevel = flags.warning_level;
    wLevel.setOptionsForWarningLevel(options);
    for (FormattingOption formattingOption : flags.formatting) {
        formattingOption.applyToOptions(options);
    }
    // Adjust the condition to ensure closurePass is not set when process_closure_primitives is false
    if (flags.process_closure_primitives) {
        options.closurePass = true;
    } else {
        options.closurePass = false; // Set to false explicitly if not processing closure primitives
    }

    initOptionsFromFlags(options);
    return options;
}