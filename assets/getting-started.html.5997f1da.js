const e={key:"v-fb0f0066",path:"/guide/getting-started.html",title:"Getting Started",lang:"en-US",frontmatter:{},excerpt:"",headers:[{level:2,title:"Prerequisites",slug:"prerequisites",children:[]},{level:2,title:"Installing the plugin",slug:"installing-the-plugin",children:[{level:3,title:"Direct Install",slug:"direct-install",children:[]},{level:3,title:"From IDE Settings",slug:"from-ide-settings",children:[]},{level:3,title:"From GitHub Releases",slug:"from-github-releases",children:[]}]},{level:2,title:"Programming Language",slug:"programming-language",children:[]}],filePathRelative:"guide/getting-started.md",git:{updatedTime:1634320976e3},content:'# Getting Started\n\nIf you come across bugs or have feature requests, please raise\nan [issue](https://github.com/Pushpavel/AutoCp/issues/new/choose) on our GitHub repository.\n\n## Prerequisites\n\n#### Competitive Companion\n\n- [__Chrome\n  extension__](https://chrome.google.com/webstore/detail/competitive-companion/cjnmckjndlpiamhfimnnjmnckgghkjbl)\n- [__Firefox add-on__](https://addons.mozilla.org/en-US/firefox/addon/competitive-companion/)\n\n## Installing the plugin\n\n### Direct Install\n\nOpen your IDE and install AutoCp by clicking the button below.\n\n<InstallButton></InstallButton>\n\n### From IDE Settings\n\n```Settings/Preferences``` > ```Plugins``` > ```Marketplace``` tab > ```Search for "\nAutoCp"``` > ```Install Plugin```\n\n![screenshot of searching and installing from Webstorm](../assets/WebstormSearchAndInstallScreenshot.png)\n\n### From GitHub Releases\n\nDownload the [latest release](https://github.com/Pushpavel/AutoCp/releases) and install it manually using\n\n```Settings/Preferences``` > ```Plugins``` > ```\u2699\uFE0F``` > ```Install plugin from disk...```\n\n![Screenshot of installing from disk on Webstorm](../assets/WebstormInstallFromDiskScreenshot.png)\n\n:::: details Install EAP version to try out the latest features\n\n::: danger Caution\n\nEAP releases might contain bugs. Please file an [issue](https://github.com/Pushpavel/AutoCp/issues/new/choose) in that\ncase.\n:::\n\n#### Add pre-release channel\n\nNavigate to\n```Settings/Preferences``` > ```Plugins``` > ```\u2699\uFE0F``` > ```Manage Plugin Repositories...```\n\nAnd add the below url\n\n```url\nhttps://plugins.jetbrains.com/plugins/eap/17061\n```\n\n![Screenshot of adding plugin repositories in Webstorm](../assets/WebstormPluginRepoAddScreenshot.png)\n\n#### Install plugin\n\nyou can either follow [Direct Install](#direct-install) or [From IDE Settings](#from-ide-settings)\n::::\n\n## Programming Language\n\nAutoCp will look for popular compilers or interpreters of your programming language from the system ```PATH``` variable\nand sets up the commands and file templates automatically.If it could not find anything, you might be required to set\nthis up yourself.\n\n<!-- TODO: add programming languages build tool table -->\n\n::: tip\n\nAutoCp plugin is not bundled with any build tool.\n:::',contentRendered:`<h1 id="getting-started" tabindex="-1"><a class="header-anchor" href="#getting-started" aria-hidden="true">#</a> Getting Started</h1>
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
<!-- TODO: add programming languages build tool table -->
<div class="custom-container tip"><p class="custom-container-title">TIP</p>
<p>AutoCp plugin is not bundled with any build tool.</p>
</div>
`,date:"0000-00-00",deps:[],hoistedTags:[],links:[],pathInferred:"/guide/getting-started.html",pathLocale:"/",permalink:null,slug:"getting-started",filePath:"/home/runner/work/AutoCp/AutoCp/docs/guide/getting-started.md",componentFilePath:"/home/runner/work/AutoCp/AutoCp/docs/.vuepress/.temp/pages/guide/getting-started.html.vue",componentFilePathRelative:"pages/guide/getting-started.html.vue",componentFileChunkName:"v-fb0f0066",dataFilePath:"/home/runner/work/AutoCp/AutoCp/docs/.vuepress/.temp/pages/guide/getting-started.html.js",dataFilePathRelative:"pages/guide/getting-started.html.js",dataFileChunkName:"v-fb0f0066",htmlFilePath:"/home/runner/work/AutoCp/AutoCp/docs/.vuepress/dist/guide/getting-started.html",htmlFilePathRelative:"guide/getting-started.html",lastUpdated:"October 15, 2021"};export{e as data};
