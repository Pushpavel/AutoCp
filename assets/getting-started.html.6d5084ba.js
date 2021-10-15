const e={key:"v-fb0f0066",path:"/guide/getting-started.html",title:"Getting Started",lang:"en-US",frontmatter:{},excerpt:"",headers:[{level:2,title:"Prerequisites",slug:"prerequisites",children:[]},{level:2,title:"Installing the plugin",slug:"installing-the-plugin",children:[{level:3,title:"Direct Install",slug:"direct-install",children:[]},{level:3,title:"From IDE Settings",slug:"from-ide-settings",children:[]},{level:3,title:"From GitHub Releases",slug:"from-github-releases",children:[]}]},{level:2,title:"Programming Language",slug:"programming-language",children:[]}],filePathRelative:"guide/getting-started.md",git:{updatedTime:1634330217e3},content:`# Getting Started

If you come across bugs or have feature requests, please raise
an [issue](https://github.com/Pushpavel/AutoCp/issues/new/choose) on our GitHub repository.

## Prerequisites

#### Competitive Companion

- [__Chrome
  extension__](https://chrome.google.com/webstore/detail/competitive-companion/cjnmckjndlpiamhfimnnjmnckgghkjbl)
- [__Firefox add-on__](https://addons.mozilla.org/en-US/firefox/addon/competitive-companion/)

## Installing the plugin

### Direct Install

Open your IDE and install AutoCp by clicking the button below.

<InstallButton></InstallButton>

### From IDE Settings

\`\`\`Settings/Preferences\`\`\` > \`\`\`Plugins\`\`\` > \`\`\`Marketplace\`\`\` tab > \`\`\`Search for "
AutoCp"\`\`\` > \`\`\`Install Plugin\`\`\`

![screenshot of searching and installing from Webstorm](../assets/WebstormSearchAndInstallScreenshot.png)

### From GitHub Releases

Download the [latest release](https://github.com/Pushpavel/AutoCp/releases) and install it manually using

\`\`\`Settings/Preferences\`\`\` > \`\`\`Plugins\`\`\` > \`\`\`\u2699\uFE0F\`\`\` > \`\`\`Install plugin from disk...\`\`\`

![Screenshot of installing from disk on Webstorm](../assets/WebstormInstallFromDiskScreenshot.png)

:::: details Install EAP version to try out the latest features

::: danger Caution

EAP releases might contain bugs. Please file an [issue](https://github.com/Pushpavel/AutoCp/issues/new/choose) in that
case.
:::

#### Add pre-release channel

Navigate to
\`\`\`Settings/Preferences\`\`\` > \`\`\`Plugins\`\`\` > \`\`\`\u2699\uFE0F\`\`\` > \`\`\`Manage Plugin Repositories...\`\`\`

And add the below url

\`\`\`url
https://plugins.jetbrains.com/plugins/eap/17061
\`\`\`

![Screenshot of adding plugin repositories in Webstorm](../assets/WebstormPluginRepoAddScreenshot.png)

#### Install plugin

you can either follow [Direct Install](#direct-install) or [From IDE Settings](#from-ide-settings)
::::

## Programming Language

AutoCp will look for popular compilers or interpreters of your programming language from the system \`\`\`PATH\`\`\` variable
and sets up the commands and file templates automatically.If it could not find anything, you might be required to set
this up yourself.

::: tip

AutoCp plugin is not bundled with any build tool.
:::

#### Default Build Tools

This list only includes the already setup programming languages and build tools. You could easily set this up manually
for your programming language or build tool by adding [Commands](commands.md) of your build tool.

| Programming Language | Build Tools |
| --------- | ------- |
| C | [gcc][gcc], [clang][clang] |
| C++ | [g++][g++], [clang++][clang++] |
| Java | [javac][javac] |
| Python | [python][python] |
| Kotlin | [kotlinc][kotlinc] |
| Rust | [rustc][rustc] | 
| C# | [csc][csc] |
| Javascript | [d8][d8], [node][node] |

Didn't find your programming language or build tool ? File
an [issue](https://github.com/Pushpavel/AutoCp/issues/new/choose) to add it.


[gcc]: https://gcc.gnu.org/install/binaries.html

[clang]: https://clang.llvm.org

[g++]: https://www.cs.odu.edu/~zeil/cs250PreTest/latest/Public/installingACompiler/

[clang++]: https://www.ics.uci.edu/~pattis/common/handouts/macclion/clang.html

[javac]: https://en.wikipedia.org/wiki/Javac

[python]: https://www.python.org/downloads/

[kotlinc]: https://kotlinlang.org/docs/command-line.html

[rustc]: https://www.rust-lang.org/tools/install

[csc]: https://dotnet.microsoft.com/download

[d8]: https://v8.dev/

[node]: https://nodejs.org/en/download/`,contentRendered:`<h1 id="getting-started" tabindex="-1"><a class="header-anchor" href="#getting-started" aria-hidden="true">#</a> Getting Started</h1>
<p>If you come across bugs or have feature requests, please raise
an <a href="https://github.com/Pushpavel/AutoCp/issues/new/choose" target="_blank" rel="noopener noreferrer">issue<OutboundLink/></a> on our GitHub repository.</p>
<h2 id="prerequisites" tabindex="-1"><a class="header-anchor" href="#prerequisites" aria-hidden="true">#</a> Prerequisites</h2>
<h4 id="competitive-companion" tabindex="-1"><a class="header-anchor" href="#competitive-companion" aria-hidden="true">#</a> Competitive Companion</h4>
<ul>
<li><a href="https://chrome.google.com/webstore/detail/competitive-companion/cjnmckjndlpiamhfimnnjmnckgghkjbl" target="_blank" rel="noopener noreferrer"><strong>Chrome
extension</strong><OutboundLink/></a></li>
<li><a href="https://addons.mozilla.org/en-US/firefox/addon/competitive-companion/" target="_blank" rel="noopener noreferrer"><strong>Firefox add-on</strong><OutboundLink/></a></li>
</ul>
<h2 id="installing-the-plugin" tabindex="-1"><a class="header-anchor" href="#installing-the-plugin" aria-hidden="true">#</a> Installing the plugin</h2>
<h3 id="direct-install" tabindex="-1"><a class="header-anchor" href="#direct-install" aria-hidden="true">#</a> Direct Install</h3>
<p>Open your IDE and install AutoCp by clicking the button below.</p>
<InstallButton></InstallButton>
<h3 id="from-ide-settings" tabindex="-1"><a class="header-anchor" href="#from-ide-settings" aria-hidden="true">#</a> From IDE Settings</h3>
<p><code>Settings/Preferences</code> &gt; <code>Plugins</code> &gt; <code>Marketplace</code> tab &gt; <code>Search for &quot; AutoCp&quot;</code> &gt; <code>Install Plugin</code></p>
<p><img src="@source/assets/WebstormSearchAndInstallScreenshot.png" alt="screenshot of searching and installing from Webstorm"></p>
<h3 id="from-github-releases" tabindex="-1"><a class="header-anchor" href="#from-github-releases" aria-hidden="true">#</a> From GitHub Releases</h3>
<p>Download the <a href="https://github.com/Pushpavel/AutoCp/releases" target="_blank" rel="noopener noreferrer">latest release<OutboundLink/></a> and install it manually using</p>
<p><code>Settings/Preferences</code> &gt; <code>Plugins</code> &gt; <code>\u2699\uFE0F</code> &gt; <code>Install plugin from disk...</code></p>
<p><img src="@source/assets/WebstormInstallFromDiskScreenshot.png" alt="Screenshot of installing from disk on Webstorm"></p>
<details class="custom-container details"><summary>Install EAP version to try out the latest features</summary>
<div class="custom-container danger"><p class="custom-container-title">Caution</p>
<p>EAP releases might contain bugs. Please file an <a href="https://github.com/Pushpavel/AutoCp/issues/new/choose" target="_blank" rel="noopener noreferrer">issue<OutboundLink/></a> in that
case.</p>
</div>
<h4 id="add-pre-release-channel" tabindex="-1"><a class="header-anchor" href="#add-pre-release-channel" aria-hidden="true">#</a> Add pre-release channel</h4>
<p>Navigate to
<code>Settings/Preferences</code> &gt; <code>Plugins</code> &gt; <code>\u2699\uFE0F</code> &gt; <code>Manage Plugin Repositories...</code></p>
<p>And add the below url</p>
<div class="language-url ext-url line-numbers-mode"><pre v-pre class="language-url"><code><span class="token scheme">https<span class="token scheme-delimiter">:</span></span><span class="token authority"><span class="token authority-delimiter">//</span><span class="token host">plugins.jetbrains.com</span></span><span class="token path"><span class="token path-separator">/</span>plugins<span class="token path-separator">/</span>eap<span class="token path-separator">/</span>17061</span>
</code></pre><div class="line-numbers"><span class="line-number">1</span><br></div></div><p><img src="@source/assets/WebstormPluginRepoAddScreenshot.png" alt="Screenshot of adding plugin repositories in Webstorm"></p>
<h4 id="install-plugin" tabindex="-1"><a class="header-anchor" href="#install-plugin" aria-hidden="true">#</a> Install plugin</h4>
<p>you can either follow <a href="#direct-install">Direct Install</a> or <a href="#from-ide-settings">From IDE Settings</a></p>
</details>
<h2 id="programming-language" tabindex="-1"><a class="header-anchor" href="#programming-language" aria-hidden="true">#</a> Programming Language</h2>
<p>AutoCp will look for popular compilers or interpreters of your programming language from the system <code>PATH</code> variable
and sets up the commands and file templates automatically.If it could not find anything, you might be required to set
this up yourself.</p>
<div class="custom-container tip"><p class="custom-container-title">TIP</p>
<p>AutoCp plugin is not bundled with any build tool.</p>
</div>
<h4 id="default-build-tools" tabindex="-1"><a class="header-anchor" href="#default-build-tools" aria-hidden="true">#</a> Default Build Tools</h4>
<p>This list only includes the already setup programming languages and build tools. You could easily set this up manually
for your programming language or build tool by adding <RouterLink to="/guide/commands.html">Commands</RouterLink> of your build tool.</p>
<table>
<thead>
<tr>
<th>Programming Language</th>
<th>Build Tools</th>
</tr>
</thead>
<tbody>
<tr>
<td>C</td>
<td><a href="https://gcc.gnu.org/install/binaries.html" target="_blank" rel="noopener noreferrer">gcc<OutboundLink/></a>, <a href="https://clang.llvm.org" target="_blank" rel="noopener noreferrer">clang<OutboundLink/></a></td>
</tr>
<tr>
<td>C++</td>
<td><a href="https://www.cs.odu.edu/~zeil/cs250PreTest/latest/Public/installingACompiler/" target="_blank" rel="noopener noreferrer">g++<OutboundLink/></a>, <a href="https://www.ics.uci.edu/~pattis/common/handouts/macclion/clang.html" target="_blank" rel="noopener noreferrer">clang++<OutboundLink/></a></td>
</tr>
<tr>
<td>Java</td>
<td><a href="https://en.wikipedia.org/wiki/Javac" target="_blank" rel="noopener noreferrer">javac<OutboundLink/></a></td>
</tr>
<tr>
<td>Python</td>
<td><a href="https://www.python.org/downloads/" target="_blank" rel="noopener noreferrer">python<OutboundLink/></a></td>
</tr>
<tr>
<td>Kotlin</td>
<td><a href="https://kotlinlang.org/docs/command-line.html" target="_blank" rel="noopener noreferrer">kotlinc<OutboundLink/></a></td>
</tr>
<tr>
<td>Rust</td>
<td><a href="https://www.rust-lang.org/tools/install" target="_blank" rel="noopener noreferrer">rustc<OutboundLink/></a></td>
</tr>
<tr>
<td>C#</td>
<td><a href="https://dotnet.microsoft.com/download" target="_blank" rel="noopener noreferrer">csc<OutboundLink/></a></td>
</tr>
<tr>
<td>Javascript</td>
<td><a href="https://v8.dev/" target="_blank" rel="noopener noreferrer">d8<OutboundLink/></a>, <a href="https://nodejs.org/en/download/" target="_blank" rel="noopener noreferrer">node<OutboundLink/></a></td>
</tr>
</tbody>
</table>
<p>Didn't find your programming language or build tool ? File
an <a href="https://github.com/Pushpavel/AutoCp/issues/new/choose" target="_blank" rel="noopener noreferrer">issue<OutboundLink/></a> to add it.</p>
`,date:"0000-00-00",deps:[],hoistedTags:[],links:[{raw:"commands.md",relative:"guide/commands.md",absolute:"/AutoCp/guide/commands.md"}],pathInferred:"/guide/getting-started.html",pathLocale:"/",permalink:null,slug:"getting-started",filePath:"/home/runner/work/AutoCp/AutoCp/docs/guide/getting-started.md",componentFilePath:"/home/runner/work/AutoCp/AutoCp/docs/.vuepress/.temp/pages/guide/getting-started.html.vue",componentFilePathRelative:"pages/guide/getting-started.html.vue",componentFileChunkName:"v-fb0f0066",dataFilePath:"/home/runner/work/AutoCp/AutoCp/docs/.vuepress/.temp/pages/guide/getting-started.html.js",dataFilePathRelative:"pages/guide/getting-started.html.js",dataFileChunkName:"v-fb0f0066",htmlFilePath:"/home/runner/work/AutoCp/AutoCp/docs/.vuepress/dist/guide/getting-started.html",htmlFilePathRelative:"guide/getting-started.html",lastUpdated:"October 15, 2021"};export{e as data};
