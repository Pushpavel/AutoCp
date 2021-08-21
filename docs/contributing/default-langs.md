# Language Support

A new language can be supported by adding it in settings, [learn more](/docs/customizing/support-lang.md). To make this
setting available by default, a [json](https://www.w3schools.com/js/js_json_intro.asp) file of that settings should be
placed in plugin's source code at ```src/main/resources/languages``` folder.

### JSON filename

```{Name of the language in pascal case}.json```

### JSON format

```json5
{
  // Name of the language in PascalCase
  langId: "Python",
  // Name of the file template file in "src/main/resources/fileTemplates/j2ee" folder, ex - Python.py.ft
  // can also be null if this language does not have a file template yet.
  fileTemplateName: "Python",
  // id of a buildConfig that is selected by default when creating a run configuration
  defaultBuildConfigId: "Python.PyExecute",
  // contains all the buildConfigurations you can use with this language
  buildConfigs: {
    "Python.PyExecute": {
      // Name of the Build Configuration
      name: "PyExecute",
      // unique string across buildConfigs in all default languages
      // it must be of the form {langId of this Language}.{name of this Build Config}
      id: "Python.PyExecute",
      // template to construct a command that builds an executable 
      // this string must contain @in
      // @in will be replaced with input path of solution file with quotes
      // @out will be replaced with output path of the executable with quotes
      // @out can be ignored if executing this command does not produce any executable.
      commandTempalte: "python @in",
    }
  }
}
```