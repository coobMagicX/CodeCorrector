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
  
  // Check if --process_closure_primitives is set to false
  if (!flags.process_closure_primitives) {
    options.closurePass = false;
  } else {
    // If true, do not override the closurePass option with a default value
    // and let initOptionsFromFlags handle it properly.
  }

  initOptionsFromFlags(options);
  return options;
}