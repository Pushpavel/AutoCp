import { defineUserConfig } from "vuepress";
import type { DefaultThemeOptions } from "vuepress";

export default defineUserConfig<DefaultThemeOptions>({
  lang: "en-US",
  base: "/AutoCp/",
  bundler: "@vuepress/bundler-vite",
  head: [
    ["link", { rel: "icon", herf: "/AutoCp/assets/logo.svg" }],
    ["script",{ src: "https://plugins.jetbrains.com/assets/scripts/mp-widget.js" }],
    ["script",{},`MarketplaceWidget.setupMarketplaceWidget('install', 17061, "#installBtn");`]
  ],

  title: "AutoCp",
  description:
    "An Open Source Competitive Programming Plugin for Intellij-Based IDEs",
  themeConfig: {
    logo: "/assets/logo.svg",
  },
});
