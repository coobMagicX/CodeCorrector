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

  if (flags.process_closure_primitives) {
    // Directly accessing and setting the public field 'closurePass' as shown in the problematic original code snippet.
    options.closurePass = true;
  }

  initOptionsFromFlags(options);
  return options;
}
