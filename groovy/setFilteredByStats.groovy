// Set the value for the feature name given as parameter featureName4Value
// based on the features created from the stats but only if we do not want to filter.
// NOTE: for now this assumes that the default prefix "cs_" for the corpus statistics features is used!
// Parameters:
// * annotationType: the annotation type to process, default is "Token"
// * featureName4Value: the name of the feature to set (or not set in case we want to filter)
// * scoreFeature: the feature we want to set the featureName4Value to if not filtered 
// * scoreValue: the actual value to set the featureName4Value to if not filtered, if 
//   specified this is used instead of scoreFeature. If neither scoreFeature nor scoreValue
//   is specified, scoreValue=1.0 is used.
// * mindf: the minimum document frequency (default: 5)
// * maxrdf: maximum relative document frequency (default: 0.5)
// * nDocs: the number of documents used for gathering the statistics, if not given will 
//   try to get it from the "cs_nDocs" feature on the annotation

void beforeCorpus(c) {
  annotationType = scriptParams.get("annotationType")
  if(annotationType == null) {
     annotationType = "Token"
  }
  featureName4Value = scriptParams.get("featureName4Value")
  if(featureName4Value == null) {
    throw new RuntimeException("Need a feature name for the featureName4Value scriptParams parameter")
  }
  scoreFeature = scriptParams.get("scoreFeature")
  scoreValue = scriptParams.get("scoreValue")
  if(scoreFeature != null) {
    scoreValue = null
  } else {
    if(scoreValue == null) {
      scoreValue = 1.0
    } else {
      scoreValue = Double.parseDouble(scoreValue)
    }
  }
  mindf = scriptParams.get("mindf")
  if(mindf == null) {
    mindf = 5
  } else {
    mindf = Double.parseDouble(mindf)
  }
  maxrdf = scriptParams.get("maxrdf")
  if(maxrdf == null) {
    maxrdf = 0.5
  } else {
    maxrdf = Double.parseDouble(maxrdf)
  }
  nDocs = scriptParams.get("nDocs")
  if(nDocs != null) {
    nDocs = Double.parseDouble(nDocs)
  }
  nrTotal = 0
  nrKept = 0
}

void afterCorpus(c) {
  System.out.println("setFilteredByStats: total annotations: "+nrTotal)
  System.out.println("setFilteredByStats: kept annotations:  "+nrKept)
}

for(Annotation ann : inputAS.get(annotationType)) {
  nrTotal += 1
  fm = ann.getFeatures()
  df = fm.get("cs_df")
  if(df == null) {
    df = 0
  }
  if(df < mindf) {
    continue
  }
  if(nDocs == null) {
    nDocs = fm.get("cs_nDocs")
  }
  if((df/nDocs) > maxrdf) {
    continue
  }
  if(scoreValue != null) {
    score = scoreValue
  } else {
    score = fm.get(scoreFeature)
  }
  fm.put(featureName4Value, score)
  nrKept +=1 
} 
