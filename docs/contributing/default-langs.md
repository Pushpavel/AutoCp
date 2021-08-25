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
  langId: "ObjectiveC",
  // Name of the file template file in "src/main/resources/fileTemplates/j2ee" folder, ex - Python.py.ft
  // can also be null if this language does not have a file template yet.
  fileTemplateName: "C++",
  // id of a buildConfig that is selected by default when creating a run configuration
  "defaultBuildConfigId": "ObjectiveC.C++ 17",
  // contains all the buildConfigurations you can use with this language
  buildConfigs: {
    "ObjectiveC.C++ 17": {
      // Name of the Build Configuration
      "name": "C++ 17",
      // unique string across buildConfigs in all default languages
      // it must be of the form {langId of this Language}.{name of this Build Config}
      "id": "ObjectiveC.C++ 17",
      // run once before testing begins
      "buildCommand": "g++ @in -o \"./a.exe\" -std=c++17",
      // run for each testcase
      "executeCommand": "\"$dir/a.exe\""
    }
  }
}
```

### Note on buildCommand & executeCommand

> [Macros](/docs/customizing/support-build-config.md#Macros) for these commands. ```@in``` must be present in at least one of these commands.

For the commands to be cross-platform, the below steps are taken.

- File separators ```\``` or ```/``` are replaced according to Platform.
- ```.exe``` extension is removed on UNIX Platforms. (So, any executable should always end with ```.exe```)
