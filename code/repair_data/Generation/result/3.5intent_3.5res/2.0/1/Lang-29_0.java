static int toJavaVersionInt(String version) {            
    return toVersionInt(toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE));       
    // Rest of the implementation...    
}