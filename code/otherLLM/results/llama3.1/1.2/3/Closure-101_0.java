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
  
  // Check if --process_closure_primitives flag affects the compilation level setting
  if (flags.process_closure_primitives) {
    options.closurePass = true;
    
    // Verify that closurePass option is correctly being enabled or disabled based on process_closure_primitives flag
    CompilationLevel newLevel = flags.compilation_level;
    newLevel.setOptionsForCompilationLevel(options);
    newLevel.setDebugOptionsForCompilationLevel(options);
  }
  
  initOptionsFromFlags(options);
  return options;
}