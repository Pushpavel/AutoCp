import{_ as a,r as s,c as i,b as e,a as n,w as d,F as c,d as t,o as h}from"./app.7bce988d.js";var u="/AutoCp/assets/WebstormSearchAndInstallScreenshot.e69cc1eb.png",_="/AutoCp/assets/WebstormInstallFromDiskScreenshot.73446802.png",p="/AutoCp/assets/WebstormPluginRepoAddScreenshot.7440353e.png";const g={},m=e("h1",{id:"getting-started",tabindex:"-1"},[e("a",{class:"header-anchor",href:"#getting-started","aria-hidden":"true"},"#"),t(" Getting Started")],-1),f=t("If you come across bugs or have feature requests, please raise an "),b={href:"https://github.com/Pushpavel/AutoCp/issues/new/choose",target:"_blank",rel:"noopener noreferrer"},k=t("issue"),v=t(" on our GitHub repository."),w=e("h2",{id:"prerequisites",tabindex:"-1"},[e("a",{class:"header-anchor",href:"#prerequisites","aria-hidden":"true"},"#"),t(" Prerequisites")],-1),y=e("h4",{id:"competitive-companion",tabindex:"-1"},[e("a",{class:"header-anchor",href:"#competitive-companion","aria-hidden":"true"},"#"),t(" Competitive Companion")],-1),P={href:"https://chrome.google.com/webstore/detail/competitive-companion/cjnmckjndlpiamhfimnnjmnckgghkjbl",target:"_blank",rel:"noopener noreferrer"},C=e("strong",null,"Chrome extension",-1),x={href:"https://addons.mozilla.org/en-US/firefox/addon/competitive-companion/",target:"_blank",rel:"noopener noreferrer"},A=e("strong",null,"Firefox add-on",-1),I=e("h2",{id:"installing-the-plugin",tabindex:"-1"},[e("a",{class:"header-anchor",href:"#installing-the-plugin","aria-hidden":"true"},"#"),t(" Installing the plugin")],-1),S=e("h3",{id:"direct-install",tabindex:"-1"},[e("a",{class:"header-anchor",href:"#direct-install","aria-hidden":"true"},"#"),t(" Direct Install")],-1),D=e("p",null,"Open your IDE and install AutoCp by clicking the button below.",-1),j=e("h3",{id:"from-ide-settings",tabindex:"-1"},[e("a",{class:"header-anchor",href:"#from-ide-settings","aria-hidden":"true"},"#"),t(" From IDE Settings")],-1),F=e("p",null,[e("code",null,"Settings/Preferences"),t(" > "),e("code",null,"Plugins"),t(" > "),e("code",null,"Marketplace"),t(" tab > "),e("code",null,'Search for " AutoCp"'),t(" > "),e("code",null,"Install Plugin")],-1),B=e("p",null,[e("img",{src:u,alt:"screenshot of searching and installing from Webstorm"})],-1),T=e("h3",{id:"from-github-releases",tabindex:"-1"},[e("a",{class:"header-anchor",href:"#from-github-releases","aria-hidden":"true"},"#"),t(" From GitHub Releases")],-1),E=t("Download the "),L={href:"https://github.com/Pushpavel/AutoCp/releases",target:"_blank",rel:"noopener noreferrer"},R=t("latest release"),W=t(" and install it manually using"),q=e("p",null,[e("code",null,"Settings/Preferences"),t(" > "),e("code",null,"Plugins"),t(" > "),e("code",null,"\u2699\uFE0F"),t(" > "),e("code",null,"Install plugin from disk...")],-1),N=e("p",null,[e("img",{src:_,alt:"Screenshot of installing from disk on Webstorm"})],-1),G={class:"custom-container details"},H=e("summary",null,"Install EAP version to try out the latest features",-1),J={class:"custom-container danger"},O=e("p",{class:"custom-container-title"},"Caution",-1),V=t("EAP releases might contain bugs. Please file an "),z={href:"https://github.com/Pushpavel/AutoCp/issues/new/choose",target:"_blank",rel:"noopener noreferrer"},M=t("issue"),K=t(" in that case."),U=e("h4",{id:"add-pre-release-channel",tabindex:"-1"},[e("a",{class:"header-anchor",href:"#add-pre-release-channel","aria-hidden":"true"},"#"),t(" Add pre-release channel")],-1),Y=e("p",null,[t("Navigate to "),e("code",null,"Settings/Preferences"),t(" > "),e("code",null,"Plugins"),t(" > "),e("code",null,"\u2699\uFE0F"),t(" > "),e("code",null,"Manage Plugin Repositories...")],-1),Q=e("p",null,"And add the below url",-1),X=e("div",{class:"language-url ext-url line-numbers-mode"},[e("pre",{class:"language-url"},[e("code",null,[e("span",{class:"token scheme"},[t("https"),e("span",{class:"token scheme-delimiter"},":")]),e("span",{class:"token authority"},[e("span",{class:"token authority-delimiter"},"//"),e("span",{class:"token host"},"plugins.jetbrains.com")]),e("span",{class:"token path"},[e("span",{class:"token path-separator"},"/"),t("plugins"),e("span",{class:"token path-separator"},"/"),t("eap"),e("span",{class:"token path-separator"},"/"),t("17061")]),t(`
`)])]),e("div",{class:"line-numbers"},[e("span",{class:"line-number"},"1"),e("br")])],-1),Z=e("p",null,[e("img",{src:p,alt:"Screenshot of adding plugin repositories in Webstorm"})],-1),$=e("h4",{id:"install-plugin",tabindex:"-1"},[e("a",{class:"header-anchor",href:"#install-plugin","aria-hidden":"true"},"#"),t(" Install plugin")],-1),ee=e("p",null,[t("you can either follow "),e("a",{href:"#direct-install"},"Direct Install"),t(" or "),e("a",{href:"#from-ide-settings"},"From IDE Settings")],-1),te=e("h2",{id:"programming-language",tabindex:"-1"},[e("a",{class:"header-anchor",href:"#programming-language","aria-hidden":"true"},"#"),t(" Programming Language")],-1),ne=e("p",null,[t("AutoCp will look for popular compilers or interpreters of your programming language from the system "),e("code",null,"PATH"),t(" variable and sets up the commands and file templates automatically.If it could not find anything, you might be required to set this up yourself.")],-1),oe=e("div",{class:"custom-container tip"},[e("p",{class:"custom-container-title"},"TIP"),e("p",null,"AutoCp plugin is not bundled with any build tool.")],-1),se=e("h4",{id:"default-build-tools",tabindex:"-1"},[e("a",{class:"header-anchor",href:"#default-build-tools","aria-hidden":"true"},"#"),t(" Default Build Tools")],-1),le=t("This list only includes the already setup programming languages and build tools. You could easily set this up manually for your programming language or build tool by adding "),re=t("Commands"),ae=t(" of your build tool."),ie=e("thead",null,[e("tr",null,[e("th",null,"Programming Language"),e("th",null,"Build Tools")])],-1),de=e("td",null,"C",-1),ce={href:"https://gcc.gnu.org/install/binaries.html",target:"_blank",rel:"noopener noreferrer"},he=t("gcc"),ue=t(", "),_e={href:"https://clang.llvm.org",target:"_blank",rel:"noopener noreferrer"},pe=t("clang"),ge=e("td",null,"C++",-1),me={href:"https://www.cs.odu.edu/~zeil/cs250PreTest/latest/Public/installingACompiler/",target:"_blank",rel:"noopener noreferrer"},fe=t("g++"),be=t(", "),ke={href:"https://www.ics.uci.edu/~pattis/common/handouts/macclion/clang.html",target:"_blank",rel:"noopener noreferrer"},ve=t("clang++"),we=e("td",null,"Java",-1),ye={href:"https://en.wikipedia.org/wiki/Javac",target:"_blank",rel:"noopener noreferrer"},Pe=t("javac"),Ce=e("td",null,"Python",-1),xe={href:"https://www.python.org/downloads/",target:"_blank",rel:"noopener noreferrer"},Ae=t("python"),Ie=e("td",null,"Kotlin",-1),Se={href:"https://kotlinlang.org/docs/command-line.html",target:"_blank",rel:"noopener noreferrer"},De=t("kotlinc"),je=e("td",null,"Rust",-1),Fe={href:"https://www.rust-lang.org/tools/install",target:"_blank",rel:"noopener noreferrer"},Be=t("rustc"),Te=e("td",null,"C#",-1),Ee={href:"https://dotnet.microsoft.com/download",target:"_blank",rel:"noopener noreferrer"},Le=t("csc"),Re=e("td",null,"Javascript",-1),We={href:"https://v8.dev/",target:"_blank",rel:"noopener noreferrer"},qe=t("d8"),Ne=t(", "),Ge={href:"https://nodejs.org/en/download/",target:"_blank",rel:"noopener noreferrer"},He=t("node"),Je=t("Didn't find your programming language or build tool ? File an "),Oe={href:"https://github.com/Pushpavel/AutoCp/issues/new/choose",target:"_blank",rel:"noopener noreferrer"},Ve=t("issue"),ze=t(" to add it.");function Me(Ke,Ue){const o=s("OutboundLink"),l=s("InstallButton"),r=s("RouterLink");return h(),i(c,null,[m,e("p",null,[f,e("a",b,[k,n(o)]),v]),w,y,e("ul",null,[e("li",null,[e("a",P,[C,n(o)])]),e("li",null,[e("a",x,[A,n(o)])])]),I,S,D,n(l),j,F,B,T,e("p",null,[E,e("a",L,[R,n(o)]),W]),q,N,e("details",G,[H,e("div",J,[O,e("p",null,[V,e("a",z,[M,n(o)]),K])]),U,Y,Q,X,Z,$,ee]),te,ne,oe,se,e("p",null,[le,n(r,{to:"/guide/commands.html"},{default:d(()=>[re]),_:1}),ae]),e("table",null,[ie,e("tbody",null,[e("tr",null,[de,e("td",null,[e("a",ce,[he,n(o)]),ue,e("a",_e,[pe,n(o)])])]),e("tr",null,[ge,e("td",null,[e("a",me,[fe,n(o)]),be,e("a",ke,[ve,n(o)])])]),e("tr",null,[we,e("td",null,[e("a",ye,[Pe,n(o)])])]),e("tr",null,[Ce,e("td",null,[e("a",xe,[Ae,n(o)])])]),e("tr",null,[Ie,e("td",null,[e("a",Se,[De,n(o)])])]),e("tr",null,[je,e("td",null,[e("a",Fe,[Be,n(o)])])]),e("tr",null,[Te,e("td",null,[e("a",Ee,[Le,n(o)])])]),e("tr",null,[Re,e("td",null,[e("a",We,[qe,n(o)]),Ne,e("a",Ge,[He,n(o)])])])])]),e("p",null,[Je,e("a",Oe,[Ve,n(o)]),ze])],64)}var Qe=a(g,[["render",Me]]);export{Qe as default};
