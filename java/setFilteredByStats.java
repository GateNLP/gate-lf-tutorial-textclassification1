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

import gate.Annotation;
import gate.FeatureMap;


String annotationType;
String scoreFeature;
Double scoreValue;
double mindf;
double maxrdf;
Double nDocs;
String featureName4Value;
int nrTotal;
int nrKept;

@Override
public void controllerStarted() {
  annotationType = (String)parms.get("annotationType");
  if(annotationType == null) {
     annotationType = "Token";
  }
  featureName4Value = (String)parms.get("featureName4Value");
  if(featureName4Value == null) {
    throw new RuntimeException("Need a feature name for the featureName4Value scriptParams parameter");
  }
  scoreFeature = (String)parms.get("scoreFeature");
  String scoreValueString = (String)parms.get("scoreValue");
  if(scoreFeature != null) {
    scoreValue = null;
  } else {
    if(scoreValueString == null) {
      scoreValue = 1.0;
    } else {
      scoreValue = Double.parseDouble(scoreValueString);
    }
  }
  String mindfString = (String)parms.get("mindf");
  if(mindfString == null) {
    mindf = 5;
  } else {
    mindf = Double.parseDouble(mindfString);
  }
  String maxrdfString = (String)parms.get("maxrdf");
  if(maxrdfString == null) {
    maxrdf = 0.5;
  } else {
    maxrdf = Double.parseDouble(maxrdfString);
  }
  String nDocsString = (String)parms.get("nDocs");
  if(nDocsString != null) {
    nDocs = Double.parseDouble(nDocsString);
  } else {
    nDocs = null;
  }
  nrTotal = 0;
  nrKept = 0;
}

@Override
public void controllerFinished() {
  System.out.println("setFilteredByStats: total annotations: "+nrTotal);
  System.out.println("setFilteredByStats: kept annotations:  "+nrKept);
}

@Override
public void execute() {
for(Annotation ann : inputAS.get(annotationType)) {
  nrTotal += 1;
  FeatureMap fm = ann.getFeatures();
  Double df = (Double)fm.get("cs_df");
  if(df == null) {
    df = 0.0;
  }
  if(df < mindf) {
    continue;
  }
  if(nDocs == null) {
    nDocs = (Double)fm.get("cs_nDocs");
  }
  if((df/nDocs) > maxrdf) {
    continue;
  }
  Double score = 0.0;
  if(scoreValue != null) {
    score = scoreValue;
  } else {
    score = (Double)fm.get(scoreFeature);
  }
  fm.put(featureName4Value, score);
  nrKept +=1 ;
} 
} //execute
