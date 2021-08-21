## Pull Request

If you have never created a pull request before, welcome
😺. [Here is a quick tutorial](https://www.youtube.com/watch?v=8lGpZkjnkt4) for you.

- __Please ask first__ before embarking on any significant pull request (e.g. implementing features, refactoring code).
  You can use [issues](https://github.com/Pushpavel/AutoCp/issues/new/choose)
  or [discussions](https://github.com/Pushpavel/AutoCp/discussions/8).

- __Keep your change as focused as possible__. If there are multiple changes you would like to make that are not
  dependent upon each other, consider submitting them as separate pull requests.
- __Prefer ```dev``` branch__ to develop features and non-critical fixes.

## Branching

- ```dev``` - most of the development takes place here, mostly unstable. distinct branches are created for features and
  fixes and are merged into ```dev```.
- ```eap``` - currently released eap version of the plugin. merging ```dev``` into ```eap``` marks an eap release.

- ```main``` - currently released stable version of the plugin. merging ```eap``` or ```dev``` into ```main``` marks a
  stable release.

- ```gh-pages``` - currently deployed [docs](https://pushpavel.github.io/AutoCp/#/) site.