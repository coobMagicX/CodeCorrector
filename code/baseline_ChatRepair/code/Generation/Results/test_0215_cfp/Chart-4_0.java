public Range getDataRange(ValueAxis axis) {
    Range result = null;
    List mappedDatasets = new ArrayList();
    List includedAnnotations = new ArrayList();
    boolean isDomainAxis = true;

    
    if (axis == null) {
        return null;
    }
    
    
    int domainIndex = getDomainAxisIndex(axis);
    if (domainIndex >= 0) {
        isDomainAxis = true;
        List datasetsForDomainAxis = getDatasetsMappedToDomainAxis(new Integer(domainIndex));
        if (datasetsForDomainAxis != null) {
            mappedDatasets.addAll(datasetsForDomainAxis);
        }

        if (domainIndex == 0 && this.annotations != null) {
            Iterator iterator = this.annotations.iterator();
            while (iterator.hasNext()) {
                Object nextObject = iterator.next();
                if (nextObject instanceof XYAnnotation) {
                    XYAnnotation annotation = (XYAnnotation) nextObject;
                    if (annotation instanceof XYAnnotationBoundsInfo) {
                        includedAnnotations.add(annotation);
                    }
                }
            }
        }
    }

    
    int rangeIndex = getRangeAxisIndex(axis);
    if (rangeIndex >= 0) {
        isDomainAxis = false;
        List datasetsForRangeAxis = getDatasetsMappedToRangeAxis(new Integer(rangeIndex));
        if (datasetsForRangeAxis != null) {
            mappedDatasets.addAll(datasetsForRangeAxis);
        }

        if (rangeIndex == 0 && this.annotations != null) {
            Iterator iterator = this.annotations.iterator();
            while (iterator.hasNext()) {
                Object nextObject = iterator.next();
                if (nextObject instanceof XYAnnotation) {
                    XYAnnotation annotation = (XYAnnotation) nextObject;
                    if (annotation instanceof XYAnnotationBoundsInfo) {
                        includedAnnotations.add(annotation);
                    }
                }
            }
        }
    }

    
    Iterator iterator = mappedDatasets.iterator();
    while (iterator.hasNext()) {
        Object dataset = iterator.next();
        if (dataset instanceof XYDataset) {
            XYDataset d = (XYDataset) dataset;
            XYItemRenderer r = getRendererForDataset(d);
            if (r != null) {
                if (isDomainAxis) {
                    result = Range.combine(result, r.findDomainBounds(d));
                } else {
                    result = Range.combine(result, r.findRangeBounds(d));
                }
                if (r.getAnnotations() != null) {
                    Iterator i = r.getAnnotations().iterator();
                    while (i.hasNext()) {
                        Object annotationObject = i.next();
                        if (annotationObject instanceof XYAnnotationBoundsInfo) {
                            XYAnnotationBoundsInfo a = (XYAnnotationBoundsInfo) annotationObject;
                            includedAnnotations.add(a);
                        }
                    }
                }
            } else {
                if (isDomainAxis) {
                    result = Range.combine(result, DatasetUtilities.findDomainBounds(d));
                } else {
                    result = Range.combine(result, DatasetUtilities.findRangeBounds(d));
                }
            }
        }
    }

    
    Iterator it = includedAnnotations.iterator();
    while (it.hasNext()) {
        Object annotationObject = it.next();
        if (annotationObject instanceof XYAnnotationBoundsInfo) {
            XYAnnotationBoundsInfo xyabi = (XYAnnotationBoundsInfo) annotationObject;
            if (xyabi.getIncludeInDataBounds()) {
                if (isDomainAxis) {
                    result = Range.combine(result, xyabi.getXRange());
                } else {
                    result = Range.combine(result, xyabi.getYRange());
                }
            }
        }
    }

    return result;
}
