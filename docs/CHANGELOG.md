<!-- Keep a Changelog guide -> https://keepachangelog.com -->
?> All notable changes to this project will be documented in this page.

## [Unreleased]

### Added

- Problem Gathering Service to replace problem gathering and generating dialogs
- Project specific overrides at ```Settings/Preference``` > ```Tools``` > ```AutoCp``` > ```Project```

### Removed

- Gather Problems Action (```Tools``` > ```Gather Problems```)
- Generate Files Dialog

## v0.5.0-eap.2

### Fixed

- Compatibility problems with IntelliJ IDEA Ultimate

## v0.5.0-eap.1

### Added

- File Templates support
- Restricted languages with IDE supported languages

### Changed

- New Testcase viewer UI, Settings UI, Run configuration UI and Generate Solutions Dialog UI
- Moved settings to <kbd>Tool</kbd> > <kbd>AutoCp</kbd>
- ```@in``` and ```@out``` as placeholders for input and output path for constructing build commands

### Fixed

- .autocp file is created unnecessarily in every project
- Build Errors were incorrectly reported as internal errors

## v0.2.2

### Fixed

- Fix some correct output were declared wrong by AutoCp.

## v0.2.1

### Fixed

- Fix output of solution execution being blank.

## v0.2.0

### Added

- Time Limit of Problem is respected while solution execution

## v0.1.1

### Added

- Initial Release 🎉🎉😀
