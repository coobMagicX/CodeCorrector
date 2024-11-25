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
  
  // Adjust the logic based on the value of `flags.process_closure_primitives`
  if (!flags.process_closure_primitives) {
    options.closurePass = false; // Ensure closurePass is set to false when the flag is false
  } else {
    options.closurePass = true;
  }

  initOptionsFromFlags(options);
  return options;
}