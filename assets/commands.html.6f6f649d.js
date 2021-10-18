import{_ as o,r as a,c as s,b as e,a as l,w as d,F as u,d as t,o as c}from"./app.0cc2048b.js";var i="/AutoCp/assets/CLionLangSettingsScreenshot.c96402a4.png";const r={},h=e("h1",{id:"commands",tabindex:"-1"},[e("a",{class:"header-anchor",href:"#commands","aria-hidden":"true"},"#"),t(" Commands")],-1),m=t("You can customize the default build and execute commands for your programming language if you are using a different build tool or to edit its arguments.These defaults are set according to the compilers or interpreters found in your system. "),_=t("more about defaults"),g=e("p",null,"Navigate to the language settings to edit the commands.",-1),p=e("p",null,[e("code",null,"Settings/Preferences"),t(" > "),e("code",null,"Tools"),t(" > "),e("code",null,"AutoCp"),t(" > "),e("code",null,"Languages")],-1),f=e("p",null,[e("img",{src:i,alt:"Screenshot of Language settings"})],-1),b=e("p",null,"These commands are used while running a solution file with AutoCp Run Configuration.",-1),x=e("h3",{id:"build-command",tabindex:"-1"},[e("a",{class:"header-anchor",href:"#build-command","aria-hidden":"true"},"#"),t(" Build Command")],-1),C=e("p",null,"Run once before testing begins, usually should compile or generate an executable.",-1),y=e("h3",{id:"execute-command",tabindex:"-1"},[e("a",{class:"header-anchor",href:"#execute-command","aria-hidden":"true"},"#"),t(" Execute Command")],-1),w=e("p",null,"Run for each testcase. usually should run the executable generated by Build Command. execution time of this command is considered as the execution time of a testcase.",-1),L=e("h3",{id:"macros-that-can-be-used-in-these-commands",tabindex:"-1"},[e("a",{class:"header-anchor",href:"#macros-that-can-be-used-in-these-commands","aria-hidden":"true"},"#"),t(" Macros that can be used in these commands")],-1),B=e("table",null,[e("thead",null,[e("tr",null,[e("th",null,"Macro"),e("th",null,"Expands to")])]),e("tbody",null,[e("tr",null,[e("td",null,[e("code",null,"@in")]),e("td",null,"absolute path to a solution file with quotes.")]),e("tr",null,[e("td",null,[e("code",null,"$dir")]),e("td",null,"absolute path to the isolated temp directory without quotes.")]),e("tr",null,[e("td",null,[e("code",null,"@dir")]),e("td",null,[t('"'),e("code",null,"$dir"),t('"')])])])],-1);function R(k,v){const n=a("RouterLink");return c(),s(u,null,[h,e("p",null,[m,l(n,{to:"/guide/getting-started.html#programming-language"},{default:d(()=>[_]),_:1})]),g,p,f,b,x,C,y,w,L,B],64)}var S=o(r,[["render",R]]);export{S as default};
