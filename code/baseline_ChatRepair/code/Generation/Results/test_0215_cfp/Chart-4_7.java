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
            for (Object obj : this.annotations) {
                if (obj instanceof XYAnnotation) {
                    XYAnnotation annotation = (XYAnnotation) obj;
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
        mappedDatasets.addAll(getDatasetsMappedToRangeAxis(new Integer(rangeIndex)));
        if (rangeIndex == 0 && this.annotations != null) {
            for (Object obj : this.annotations) {
                if (obj instanceof XYAnnotation) {
                    XYAnnotation annotation = (XYAnnotation) obj;
                    if (annotation instanceof XYAnnotationBoundsInfo) {
                        includedAnnotations.add(annotation);
                    }
                }
            }
        }
    }

    
    for (Object obj : mappedDatasets) {
        if (obj instanceof XYDataset) {
            XYDataset d = (XYDataset) obj;
            XYItemRenderer r = getRendererForDataset(d);
            if (r != null) {
                Range bounds;
                if (isDomainAxis) {
                    bounds = r.findDomainBounds(d);
                } else {
                    bounds = r.findRangeBounds(d);
                }
                result = Range.combine(result, bounds);
                
                Collection c = r.getAnnotations();
                if (c != null) {
                    for (Object annotationObj : c) {
                        if (annotationObj instanceof XYAnnotationBoundsInfo) {
                            XYAnnotationBoundsInfo annotation = (XYAnnotationBoundsInfo) annotationObj;
                            includedAnnotations.add(annotation);
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

    for (Object obj : includedAnnotations) {
        if (obj instanceof XYAnnotationBoundsInfo) {
            XYAnnotationBoundsInfo xyabi = (XYAnnotationBoundsInfo) obj;
            if (xyabi.getIncludeInDataBounds()) {
                Range annotationRange = isDomainAxis ? xyabi.getXRange() : xyabi.getYRange();
                result = Range.combine(result, annotationRange);
            }
        }
    }

    return result;
}
