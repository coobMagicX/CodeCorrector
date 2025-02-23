public Range getDataRange(ValueAxis axis) {
    Range result = null;
    List mappedDatasets = new ArrayList();
    List includedAnnotations = new ArrayList();
    boolean isDomainAxis = true;

    
    int domainIndex = getDomainAxisIndex(axis);
    if (domainIndex >= 0) {
        isDomainAxis = true;
        mappedDatasets.addAll(getDatasetsMappedToDomainAxis(new Integer(domainIndex)));
        if (domainIndex == 0 && this.annotations != null) {  
            Iterator iterator = this.annotations.iterator();
            while (iterator.hasNext()) {
                XYAnnotation annotation = (XYAnnotation) iterator.next();
                if (annotation instanceof XYAnnotationBoundsInfo) {
                    includedAnnotations.add(annotation);
                }
            }
        }
    }

    
    int rangeIndex = getRangeAxisIndex(axis);
    if (rangeIndex >= 0) {
        isDomainAxis = false;
        mappedDatasets.addAll(getDatasetsMappedToRangeAxis(new Integer(rangeIndex)));
        if (rangeIndex == 0 && this.annotations != null) {  
            Iterator iterator = this.annotations.iterator();
            while (iterator.hasNext()) {
                XYAnnotation annotation = (XYAnnotation) iterator.next();
                if (annotation instanceof XYAnnotationBoundsInfo) {
                    includedAnnotations.add(annotation);
                }
            }
        }
    }

    
    Iterator iterator = mappedDatasets.iterator();
    while (iterator.hasNext()) {
        XYDataset d = (XYDataset) iterator.next();
        if (d != null) {
            XYItemRenderer r = getRendererForDataset(d);
            if (r != null) {
                
                result = isDomainAxis ? Range.combine(result, r.findDomainBounds(d)) 
                                      : Range.combine(result, r.findRangeBounds(d));
                
                if (r.getAnnotations() != null) {
                    for (XYAnnotation a : (Collection<XYAnnotation>) r.getAnnotations()) {
                        if (a instanceof XYAnnotationBoundsInfo) {
                            includedAnnotations.add(a);
                        }
                    }
                }
            } else {
                
                result = isDomainAxis ? Range.combine(result, DatasetUtilities.findDomainBounds(d))
                                      : Range.combine(result, DatasetUtilities.findRangeBounds(d));
            }
        }
    }

    
    for (XYAnnotationBoundsInfo xyabi : (List<XYAnnotationBoundsInfo>) includedAnnotations) {
        if (xyabi.getIncludeInDataBounds()) {
            result = isDomainAxis ? Range.combine(result, xyabi.getXRange())
                                  : Range.combine(result, xyabi.getYRange());
        }
    }

    return result;
}
