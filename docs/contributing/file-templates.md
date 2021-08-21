# Contributing File Templates

File templates are used to avoid boilerplate code while generating the files. They can be easily bundled with the plugin
by the following.

#### Creating a File Template

Create a file with the following file name format
```{language name in PascalCase}.{language file extension}.ft``` (ex - ```C++.cpp.ft```) and place the file template
file at ```src/main/resources/fileTemplates/j2ee``` folder.

#### Register this file Template as default File Template of a language

Go to ```src/main/resources/languages/{language name in PascalCase}.json``` file and modify fileTemplateName field to
the fileName of the file template file without any extensions (ex - ```C++``` for ```C++.cpp.ft```).