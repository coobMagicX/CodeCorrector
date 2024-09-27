public BaseSettings withDateFormat(DateFormat df) {
    if (_dateFormat == df) {
        return this;
    }
    // Check if df is null and only then use _timeZone, otherwise get timezone from df
    TimeZone tz = (df == null) ? _timeZone : df.getTimeZone();
    return new BaseSettings(_classIntrospector, _annotationIntrospector, _visibilityChecker, _propertyNamingStrategy, _typeFactory,
            _typeResolverBuilder, df, _handlerInstantiator, _locale,
            tz, _defaultBase64);
}