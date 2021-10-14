import {defineUserConfig} from "vuepress";
import type {DefaultThemeOptions} from "vuepress";

const path = require("path");

export default defineUserConfig<DefaultThemeOptions>({
    lang: "en-US",
    base: "/AutoCp/",
    bundler: "@vuepress/bundler-vite",
    title: "AutoCp",
    description: "An Open Source Competitive Programming Plugin for Intellij-Based IDEs",
    templateDev: path.resolve(__dirname, "template.dev.html"),
    templateSSR: path.resolve(__dirname, "template.html"),
    plugins: ['@vuepress/plugin-search'],
    themeConfig: {logo: "/assets/logo.svg"},
});
