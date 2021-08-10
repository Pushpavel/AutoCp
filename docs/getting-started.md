## Quick start

##### Install plugin

See instructions [here](installation.md)

##### Gather the problems

- In the IDE, open  <kbd>Tools</kbd> > <kbd>Gather Problems</kbd> Dialog.

- Open the problem/ contest page in the browser and press the parse button of competitive companion browser extension.

- AutoCp will gather the problems and generates the boilerplate files.

##### Edit testcases

<kbd>View</kbd> > <kbd>Tool Windows</kbd> > <kbd>AutoCp</kbd> opens a tool window containing the testcases of the
currently selected file.

##### Check the solution

Right-click the solution file and run using AutoCp.

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