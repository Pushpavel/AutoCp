# Getting Started

## Prerequisites

#### Competitive Companion

- [__Chrome
  extension__](https://chrome.google.com/webstore/detail/competitive-companion/cjnmckjndlpiamhfimnnjmnckgghkjbl)
- [__Firefox add-on__](https://addons.mozilla.org/en-US/firefox/addon/competitive-companion/)

## Installing the plugin

### Direct Install

Open your IDE and install AutoCp by clicking the button below.

<div id="installBtn" style="padding-bottom: 32px;"></div>

### From IDE Settings

```Settings/Preferences``` > ```Plugins``` > ```Marketplace``` tab > ```Search for "
AutoCp"``` > ```Install Plugin```

![screenshot of searching and installing from Webstorm](../assets/WebstormSearchAndInstallScreenshot.png)

### From GitHub Releases

Download the [latest release](https://github.com/Pushpavel/AutoCp/releases) and install it manually using

```Settings/Preferences``` > ```Plugins``` > ```⚙️``` > ```Install plugin from disk...```

![Screenshot of installing from disk on Webstorm](../assets/WebstormInstallFromDiskScreenshot.png)

:::: details Install EAP version to try out the latest features

::: danger Caution

EAP releases might contain bugs. Please file an [issue](https://github.com/Pushpavel/AutoCp/issues/new/choose) in that
case.
:::

#### Add pre-release channel

Navigate to
```Settings/Preferences``` > ```Plugins``` > ```⚙️``` > ```Manage Plugin Repositories...```

And add the below url

```url
https://plugins.jetbrains.com/plugins/eap/17061
```

![Screenshot of adding plugin repositories in Webstorm](../assets/WebstormPluginRepoAddScreenshot.png)

#### Install plugin

you can either follow [Direct Install](#direct-install) or [From IDE Settings](#from-ide-settings)
::::

## Programming Language

AutoCp will look for popular compilers or interpreters of your programming language from the system ```PATH``` variable
and sets up the commands and file templates automatically.If it could not find anything, you might be required to set
this up yourself.

<!-- TODO: add programming languages build tool table -->

::: tip

AutoCp plugin is not bundled with any build tool.
:::