## Quick start

#### Install plugin

See instructions [here](installation.md)

#### Generate solution files

With the IDE project already open, Open the problem/ contest page in the browser and press the competitive companion
button in the browser. This will generate files with default file template. You can change the programming language
in ```Settings/Preferences``` > ```Tools``` > ```AutoCp``` before generating the files.

#### Edit testcases

<kbd>View</kbd> > <kbd>Tool Windows</kbd> > <kbd>AutoCp</kbd> opens a tool window containing the testcases of the
currently selected file.

#### Check the solution

Right-click the solution file and run using AutoCp.

## File Structure

files are generated inside a folder named like ```<judge> - <category/contest>``` that the problem belongs to.

!> Moving or renaming files will unlink the solution file to its problem. This restriction will be removed

## ```.autocp``` file

This file stores the problems and testcases. It is very crucial to this plugin and should not be touched.

## Custom File Template

AutoCp comes with few file templates optimized for competitive programming.

If you wish to customize you can,

- __Edit existing file templates__

  <kbd>Settings/Preferences</kbd> > <kbd>Editor</kbd> > <kbd>File and code templates</kbd> > <kbd>Other</kbd>
  tab > <kbd>AutoCp Templates</kbd>

- __Create new file templates__

  <kbd>Settings/Preferences</kbd> > <kbd>Editor</kbd> > <kbd>File and code templates</kbd> > <kbd>Files</kbd>
  tab > <kbd>+</kbd>

  Also, change file template for your programming language in

  <kbd>Settings/Preferences</kbd> > <kbd>Tools</kbd> > <kbd>AutoCp</kbd> > <kbd>Languages</kbd>

Refer IDE docs on [File Templates](https://www.jetbrains.com/help/clion/settings-file-and-code-templates.html) for
syntax. If you do not use its special capabilities, you can simply wrap your custom template with ```#[[```
and ```]]#```

__example__

```
#[[
int main() {
    
}
]]#
```

!> ```#[[``` causes a newline.

## Limitations

Hopefully, these limitations will be removed soon.

- Memory Limit is ignored.
- Strict File Structure
- No Debugging through AutoCp
- Only files created by AutoCp can be run by AutoCp

## Help ? 😀

- If you've noticed a bug or have a feature request,
  consider [opening a new issue](https://github.com/Pushpavel/AutoCp/issues/new/choose).
- Use [discussions](https://github.com/Pushpavel/AutoCp/discussions) for questions.

