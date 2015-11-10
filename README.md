# VariantSync
=== automating the synchronization of software variants ===

VariantSync is an eclipse-plugin to support conventional development of a small number of software variants by introducing feature-orientation. The plugin not only considers single changes on a low code level, but also extends the granularity of changes by adapting features and feature-expressions which are known from the research area of software product-lines. VariantSync implements a strategy to synchronize features of software variants by combining low-level changes and assign them to features or feature-expressions. The goal is to synchronize features and feature-expressions between variants.

## System Requirements
* glpk (http://sourceforge.net/projects/winglpk/)
* Eclipse IDE with the following plug-ins:
  * Plug-in Development Environment (PDE - version luna recommended)
  * Feature IDE (version 2.7.4 recommended)

## Installation
* download and extract winglpk in a folder of your choice
* import the VariantSync as existing project in an eclipse workspace/ clone VariantSync as git project in an eclipse workspace
* run MANIFEST.MF in folder META-INF as eclipse application with the following run configuration:
  * Program arguments:
    * -os ${target.os} -ws ${target.ws} -arch ${target.arch} -nl ${target.nl} -consoleLog
  * VM arguments: 
    * -Dosgi.requiredJavaVersion=1.6 -Xms40m -Xmx512m
    * -Djava.library.path=ABSOLUTE_PATH_TO_FOLDER\winglpk-4.5x\glpk-4.5x\w64 (or \w32 for a 32-bit operating system)

## First Steps
1. create a FeatureIDE-Project named variantsyncFeatureInfo
2. create a feature model for variantsyncFeatureInfo (this feature model describes the domain for the variants)
3. create a feature configuration in variantsyncFeatureInfo for each variant - the configuration file needs to have the same name as the variant
4. import the variants (if they are not already in the workspace)

## Workflow
* See VariantSync/Workflow.pdf for an instruction how to use this tool.

## Hints
* do not use eclipse code formatting function (CTRL + SHIFT + F)

## Known Misbehavior
* color for code-highlighting can only be changed if the active context is stopped
* At program start: If a class is opened in the editor after starting VariantSync, then existing annotations and code-highlighting for this class will sometimes disappear. To avoid misbehavior, open any other file in the code editor and navigate back to the first file. Then, annotations and code-highlighting are displayed correctly.
* After performing a synchronization, code in synchronized file is sometimes not correctly tagged.
* Change history is saved after closing VariantSync, but changes are actually not loaded after program start.
