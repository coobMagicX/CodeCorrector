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
  
  // Corrected this section to check `flags.process_closure_primitives` before setting `options.closurePass`
  if (!flags.process_closure_primitives) { // Ensure the flag is not set to process closure primitives
    options.closurePass = false; // Set closurePass to false if the flag is false
  } else {
    options.closurePass = true; // Otherwise, keep it as true
  }

  initOptionsFromFlags(options);
  return options;
}