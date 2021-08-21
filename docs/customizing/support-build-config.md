## Build Configuration

A Build tool creates an executable from the source code. AutoCp simply executes the build command of the build tool to
get the executable and tests the testcases on it.

To add or modify different build configurations, go to

<kbd>Settings/Preferences</kbd> > <kbd>Tools</kbd> > <kbd>AutoCp</kbd> > <kbd>Languages</kbd>

### Properties

#### Name

Name of the build configuration

#### Command Template

Template to construct a command that builds an executable. this string must contain ```@in``` and optionally ```@out```.
```@in``` will be replaced with input path of solution file with quotes.
```@out``` will be replaced with output path of the executable with quotes.
```@out``` can be ignored if executing this command does not produce any executable.

Example:

```text
g++ @in -o @out
```

will be replaced as

```bash
g++ "C:/path/to/file.cpp" -o "C:/temp/executable.exe"
```
