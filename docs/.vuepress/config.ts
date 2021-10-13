import { defineUserConfig } from "vuepress";
import type { DefaultThemeOptions } from "vuepress";

export default defineUserConfig<DefaultThemeOptions>({
  lang: "en-US",
  base: "/AutoCp/",
  bundler: "@vuepress/bundler-vite",
  head: [["link", { rel: "icon", herf: "/AutoCp/assets/logo.svg" }]],

  title: "AutoCp",
  description:
    "An Open Source Competitive Programming Plugin for Intellij-Based IDEs",
  themeConfig: {
    logo: "/assets/logo.svg",
  },
});
