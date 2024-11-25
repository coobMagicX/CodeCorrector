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
  
  // Ensure that the value of `flags.process_closure_primitives` is used
  if (flags.process_closure_primitives) {
    options.closurePass = true;
  }

  // Use the existing method to initialize options from flags.
  initConfigFromFlags(flags.args, System.err);

  return options;
}

// Make sure this method correctly sets the CompilerOptions based on flags.
private void initConfigFromFlags(String[] args, PrintStream err)
throws CmdLineException {
  Pattern argPattern = Pattern.compile("(--[a-zA-Z_]+)=(.*)");
  Pattern quotesPattern = Pattern.compile("^['\"](.*)['\"]$");
  List<String> processedArgs = Lists.newArrayList();
  for (String arg : args) {
    Matcher matcher = argPattern.matcher(arg);
    if (matcher.matches()) {
      processedArgs.add(matcher.group(1));

      String value = matcher.group(2);
      Matcher quotesMatcher = quotesPattern.matcher(value);
      if (quotesMatcher.matches()) {
        processedArgs.add(quotesMatcher.group(1));
      } else {
        processedArgs.add(value);
      }
    } else {
      processedArgs.add(arg);
    }
  }

  CmdLineParser parser = new CmdLineParser(flags);
  try {
    parser.parseArgument(processedArgs.toArray(new String[0]));
  } catch (CmdLineException e) {
    err.println(e.getMessage());
    parser.printUsage(err);
    throw e;
  }

  // Use the getCommandLineConfig() to set all necessary options
  getCommandLineConfig()
      .setPrintTree(flags.print_tree)
      .setComputePhaseOrdering(flags.compute_phase_ordering)
      .setPrintAst(flags.print_ast)
      .setPrintPassGraph(flags.print_pass_graph)
      .setJscompDevMode(flags.jscomp_dev_mode)
      .setLoggingLevel(flags.logging_level)
      .setExterns(flags.externs)
      .setJs(flags.js)
      .setJsOutputFile(flags.js_output_file)
      .setModule(flags.module)
      .setVariableMapInputFile(flags.variable_map_input_file)
      .setPropertyMapInputFile(flags.property_map_input_file)
      .setVariableMapOutputFile(flags.variable_map_output_file)
      .setCreateNameMapFiles(flags.create_name_map_files)
      .setPropertyMapOutputFile(flags.property_map_output_file)
      .setThirdParty(flags.third_party)
      .setSummaryDetailLevel(flags.summary_detail_level)
      .setOutputWrapper(flags.output_wrapper)
      .setOutputWrapperMarker(flags.output_wrapper_marker)
      .setModuleWrapper(flags.module_wrapper)
      .setModuleOutputPathPrefix(flags.module_output_path_prefix)
      .setCreateSourceMap(flags.create_source_map)
      .setJscompError(flags.jscomp_error)
      .setJscompWarning(flags.jscomp_warning)
      .setJscompOff(flags.jscomp_off)
      .setDefine(flags.define)
      .setCharset(flags.charset);
}