# Notes kept during creation of the repository


Accuracies:

| Alg | Features | Parameters | Acc.Dev | 
| ---- | ---- | --- | --- | --- | -- |
| MaxEnt  | string    |  | 0.7690 | 
| MaxEnt  | stringpos |  | 0.7720 |
| MaxEnt  | string1g2g|  | 0.7720 | 
| MaxEnt  | stringtfidf| | 0.7552 | 
| LibSVM  | string    |  | 0.5244 | 
| LibSVM  | stringpos |  | 0.5244 | [??? same?]


!!! Problem: not sure if the results when re-running from within the GUI are correct and if 
the LF always trains/uses a new model. We need to test this (see #99)

Testing with the SVM, idea is to run several train/apply steps one after the other, first
by restarting and removing the model data each time, then in one GUI session:

1) restart/remove between each step

| Alg | Features | Parameters | Acc.Dev | 
| ---- | ---- | --- | --- | --- | -- |
| LibSVM  | string    |       | 0.5244 | 
| LibSVM  | stringpos |       | 0.5375 | 
| LibSVM  | string    | -t 0  | 0.7617 | 
| LibSVM  | stringpos | -t 0  | 0.7598 | 
| LibSVM  | string    | -t 1  | 0.5028 | 
| LibSVM  | stringpos | -t 1  | 0.4991 | 

2) all in one GUI session

| Alg | Features | Parameters | Acc.Dev | Notes
| ---- | ---- | --- | --- | --- | -- | -- |
| LibSVM  | string    |       | 0.5244 | OK |
| LibSVM  | stringpos |       | 0.5375 | OK |
| LibSVM  | string    | -t 0  | 0.5375 |  ERROR 1: apply zeigt richtiges feature an, dennoch falsch? |
| LibSVM  | stringpos | -t 0  | 0. | 
| LibSVM  | string    | -t 1  | 0. | 
| LibSVM  | stringpos | -t 1  | 0. | 

ERROR 1:
* run apply again within the GUI, no change, 0.5375 
* restart GATE GUI, then apply: 0.7617
* Problem lies with apply, not train, it seems
* Verify: load training gap, train the default model (string, no parms), then apply, get 0.7617 instead of 0.5244
* Run apply after re-init of PR: 0.7617
* From the output it looks as if the model would simply not get re-loaded and the same model is used always

Attempted fix: 479f9a31d8b03a6a949211a826daf8c44cc8e60c
looks good!

More detailed testing of the Application PR for classification:
* After loading, running:
* Message: "Loaded LIBSVM model, nrclasses=2" (from libsvm engine, removed)
