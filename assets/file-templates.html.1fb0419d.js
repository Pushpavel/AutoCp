const e={key:"v-51f59145",path:"/guide/file-templates.html",title:"File Templates",lang:"en-US",frontmatter:{},excerpt:"",headers:[{level:3,title:"Edit existing file templates",slug:"edit-existing-file-templates",children:[]},{level:3,title:"Predefined Template Variables",slug:"predefined-template-variables",children:[]}],filePathRelative:"guide/file-templates.md",git:{updatedTime:1634237837e3},content:"# File Templates\n\nAutoCp comes with simple file templates that can be optimized for your liking. It uses the\nbuilt-in [file templates](https://www.jetbrains.com/help/clion/settings-file-and-code-templates.html) system of the IDE.\nIn file templates, you can use text, code, comments, and predefined variables.\n\n### Edit existing file templates\n\nEach template name is of the format ```CP_TEMPLATE_{FILE_EXTENSION_IN_CAPITALS}.{file_extension}```<br>\n(ex - ```CP_TEMPLATE_CPP.cpp```). This template will be used for generating files with the ```{file extension}```\n. Refer IDE docs on [File Templates](https://www.jetbrains.com/help/clion/using-file-and-code-templates.html#syntax) for\nsyntax.\n\n```Settings/Preferences``` > ```Editor``` > ```File and code templates``` > ```Other```\ntab > ```AutoCp Templates```\n\n![Screenshot of File Templates in CLion](../assets/CLionFileTemplatesScreenshot.png)\n\n### Predefined Template Variables\n\nA list of predefined variables provided by autocp is available below. When you use these variables in templates, they\nexpand into corresponding values later in the editor.\n\n| Variable                    | Description                                                                   |\n| --------------------------- | ----------------------------------------------------------------------------- |\n| ```${PROBLEM_GROUP_NAME}``` | Contest or category name of the problem for which this file template is used  |\n| ```${PROBLEM_NAME}```       | Name of the Problem for which this file template is used                      |\n| ```${ONLINE_JUDGE_NAME}```  | Name of your online judge for (for example, CodeChef, Codeforces, and so on). |\n|                             |[...more IDE defined variables](https://www.jetbrains.com/help/clion/settings-file-and-code-templates.html#:~:text=%24%7BCALL_SUPER%7D,Current%20year)|\n\n__C++ file template__\n\n@[code velocity](../../src/main/resources/fileTemplates/j2ee/CP_TEMPLATE_CPP.cpp.ft)",contentRendered:`<h1 id="file-templates" tabindex="-1"><a class="header-anchor" href="#file-templates" aria-hidden="true">#</a> File Templates</h1>
<p>AutoCp comes with simple file templates that can be optimized for your liking. It uses the
built-in <a href="https://www.jetbrains.com/help/clion/settings-file-and-code-templates.html" target="_blank" rel="noopener noreferrer">file templates<OutboundLink/></a> system of the IDE.
In file templates, you can use text, code, comments, and predefined variables.</p>
<h3 id="edit-existing-file-templates" tabindex="-1"><a class="header-anchor" href="#edit-existing-file-templates" aria-hidden="true">#</a> Edit existing file templates</h3>
<p>Each template name is of the format <code>CP_TEMPLATE_{FILE_EXTENSION_IN_CAPITALS}.{file_extension}</code><br>
(ex - <code>CP_TEMPLATE_CPP.cpp</code>). This template will be used for generating files with the <code>{file extension}</code>
. Refer IDE docs on <a href="https://www.jetbrains.com/help/clion/using-file-and-code-templates.html#syntax" target="_blank" rel="noopener noreferrer">File Templates<OutboundLink/></a> for
syntax.</p>
<p><code>Settings/Preferences</code> &gt; <code>Editor</code> &gt; <code>File and code templates</code> &gt; <code>Other</code>
tab &gt; <code>AutoCp Templates</code></p>
<p><img src="@source/assets/CLionFileTemplatesScreenshot.png" alt="Screenshot of File Templates in CLion"></p>
<h3 id="predefined-template-variables" tabindex="-1"><a class="header-anchor" href="#predefined-template-variables" aria-hidden="true">#</a> Predefined Template Variables</h3>
<p>A list of predefined variables provided by autocp is available below. When you use these variables in templates, they
expand into corresponding values later in the editor.</p>
<table>
<thead>
<tr>
<th>Variable</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr>
<td><code>\${PROBLEM_GROUP_NAME}</code></td>
<td>Contest or category name of the problem for which this file template is used</td>
</tr>
<tr>
<td><code>\${PROBLEM_NAME}</code></td>
<td>Name of the Problem for which this file template is used</td>
</tr>
<tr>
<td><code>\${ONLINE_JUDGE_NAME}</code></td>
<td>Name of your online judge for (for example, CodeChef, Codeforces, and so on).</td>
</tr>
<tr>
<td></td>
<td><a href="https://www.jetbrains.com/help/clion/settings-file-and-code-templates.html#:~:text=%24%7BCALL_SUPER%7D,Current%20year" target="_blank" rel="noopener noreferrer">...more IDE defined variables<OutboundLink/></a></td>
</tr>
</tbody>
</table>
<p><strong>C++ file template</strong></p>
<div class="language-velocity ext-velocity line-numbers-mode"><pre v-pre class="language-velocity"><code>File not found</code></pre><div class="line-numbers"></div></div>`,date:"0000-00-00",deps:["/home/runner/work/AutoCp/AutoCp/src/main/resources/fileTemplates/j2ee/CP_TEMPLATE_CPP.cpp.ft"],hoistedTags:[],links:[],pathInferred:"/guide/file-templates.html",pathLocale:"/",permalink:null,slug:"file-templates",filePath:"/home/runner/work/AutoCp/AutoCp/docs/guide/file-templates.md",componentFilePath:"/home/runner/work/AutoCp/AutoCp/docs/.vuepress/.temp/pages/guide/file-templates.html.vue",componentFilePathRelative:"pages/guide/file-templates.html.vue",componentFileChunkName:"v-51f59145",dataFilePath:"/home/runner/work/AutoCp/AutoCp/docs/.vuepress/.temp/pages/guide/file-templates.html.js",dataFilePathRelative:"pages/guide/file-templates.html.js",dataFileChunkName:"v-51f59145",htmlFilePath:"/home/runner/work/AutoCp/AutoCp/docs/.vuepress/dist/guide/file-templates.html",htmlFilePathRelative:"guide/file-templates.html",lastUpdated:"October 14, 2021"};export{e as data};
