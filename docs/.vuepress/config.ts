import {defineUserConfig} from "vuepress";
import type {DefaultThemeOptions} from "vuepress";
import {path} from "@vuepress/utils"
import {sidebarConfig} from "./configs/sidebar";

export default defineUserConfig<DefaultThemeOptions>({
    lang: "en-US",
    base: "/AutoCp/",
    bundler: "@vuepress/bundler-vite",
    title: "AutoCp",
    description: "An Open Source Competitive Programming Plugin for Intellij-Based IDEs",
    templateDev: path.resolve(__dirname, "template.dev.html"),
    templateSSR: path.resolve(__dirname, "template.html"),
    plugins: ['@vuepress/plugin-search', path.resolve(__dirname, './plugins/code-copy'), path.resolve(__dirname, './plugins/last-updated')],
    theme: path.resolve(__dirname, "./theme"),
    themeConfig: {
        logo: "/assets/logo.svg",
        navbar: [{text: "Guide", link: "/guide/"}, {
            text: "Changelog",
            link: "https://github.com/Pushpavel/AutoCp/blob/dev/CHANGELOG.md"
        }],
        repoLink: "https://github.com/Pushpavel/AutoCp",
        marketplaceLink: "https://plugins.jetbrains.com/plugin/17061-autocp",
        sidebar: sidebarConfig,
        editLink: true,
        contributors: false,
        docsRepo: "https://github.com/Pushpavel/AutoCp",
        docsBranch: 'docs',
        docsDir: 'docs',
        lastUpdated: true,
    },
});
