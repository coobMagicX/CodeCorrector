public Range getDataRange(ValueAxis axis) {
    Range result = null;
    List<XYDataset> mappedDatasets = new ArrayList<>();
    List<XYAnnotationBoundsInfo> includedAnnotations = new ArrayList<>();
    boolean isDomainAxis = true;

    
    int domainIndex = getDomainAxisIndex(axis);
    if (domainIndex >= 0) {
        isDomainAxis = true;
        mappedDatasets.addAll(getDatasetsMappedToDomainAxis(new Integer(domainIndex)));
        addAnnotationsFromDomain(includedAnnotations, domainIndex);
    }

    
    int rangeIndex = getRangeAxisIndex(axis);
    if (rangeIndex >= 0) {
        isDomainAxis = false;
        mappedDatasets.addAll(getDatasetsMappedToRangeAxis(new Integer(rangeIndex)));
        addAnnotationsFromRange(includedAnnotations, rangeIndex);
    }

    
    for (XYDataset d : mappedDatasets) {
        if (d != null) {
            XYItemRenderer r = getRendererForDataset(d);
            if (r != null) {
                
                result = calculateRange(isDomainAxis, result, d, r);
                
                includeRendererAnnotations(includedAnnotations, r);
            } else {
                result = isDomainAxis ? Range.combine(result, DatasetUtilities.findDomainBounds(d)) 
                                      : Range.combine(result, DatasetUtilities.findRangeBounds(d));
            }
        }
    }

    
    for (XYAnnotationBoundsInfo xyabi : includedAnnotations) {
        if (xyabi.getIncludeInDataBounds()) {
            result = isDomainAxis ? Range.combine(result, xyabi.getXRange()) 
                                  : Range.combine(result, xyabi.getYRange());
        }
    }

    return result;
}


private void addAnnotationsFromDomain(List<XYAnnotationBoundsInfo> list, int index) {
    if (index == 0 && this.annotations != null) {
        Iterator<XYAnnotation> iterator = this.annotations.iterator();
        addAnnotations(iterator, list);
    }
}

private void addAnnotationsFromRange(List<XYAnnotationBoundsInfo> list, int index) {
    if (index == 0 && this.annotations != null) {
        Iterator<XYAnnotation> iterator = this.annotations.iterator();
        addAnnotations(iterator, list);
    }
}

private void addAnnotations(Iterator<XYAnnotation> iterator, List<XYAnnotationBoundsInfo> list) {
    while (iterator.hasNext()) {
        XYAnnotation annotation = iterator.next();
        if (annotation instanceof XYAnnotationBoundsInfo) {
            list.add((XYAnnotationBoundsInfo) annotation);
        }
    }
}

private Range calculateRange(boolean isDomainAxis, Range current, XYDataset dataset, XYItemRenderer renderer) {
    return isDomainAxis ? Range.combine(current, renderer.findDomainBounds(dataset))
                        : Range.combine(current, renderer.findRangeBounds(dataset));
}

private void includeRendererAnnotations(List<XYAnnotationBoundsInfo> list, XYItemRenderer renderer) {
    if (renderer.getAnnotations() != null) {
        for (Object annotation : renderer.getAnnotations()) {
            if (annotation instanceof XYAnnotationBoundsInfo) {
                list.add((XYAnnotationBoundsInfo) annotation);
            }
        }
    }
}
