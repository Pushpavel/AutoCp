<!-- Keep a Changelog guide -> https://keepachangelog.com -->
> All notable changes to this project will be documented in this page.

## [Unreleased]

## v0.7.5
- Supports 2022.2 IDE versions.
- [#87](https://github.com/Pushpavel/AutoCp/pull/87) - filename support non-ascii letters

## v0.1.1
- Initial Release 🎉🎉😀

## v0.5.0-eap.2
- Fixes - Compatibility problems with IntelliJ IDEA Ultimate

## v0.5.0-eap.1
- Adds File Templates support
- Restricts languages to IDE supported languages
- Adds updated Testcase viewer UI
- Adds Settings UI
- Adds Run configuration UI
- Adds Generate Solutions Dialog UI
- Moves settings under ```Tool``` > ```AutoCp```
- Changes ```@in``` and ```@out``` as placeholders for input and output path for constructing build commands
- Fixes ```.autocp``` file is created unnecessarily in every project.
- Fixes - Build Errors incorrectly reported as internal errors

## v0.5.0-eap.4
- Adds time constraints UI.
- Adds settings tab in the Testcase tool window.
- Adds link to diff viewer to compare expected output with actual output for each testcase result in run window.
- Fixes - commands producing ```.exe``` suffixed executables in mac or linux where it is not an executable.
- Fixes Testing Process not exiting

## v0.5.0-eap.3
- Adds Background Problem Gathering Service.
- Adds Project specific settings overrides at ```Settings/Preference``` > ```Tools``` > ```AutoCp``` > ```Project```
- Removes Problem Gathering Dialog
- Removes Gather Problems Action (```Tools``` > ```Gather Problems```)
- Removes Generate Files Dialog