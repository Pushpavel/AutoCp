import {PluginObject} from "vuepress";

const {path} = require("@vuepress/utils");

export default {
    name: 'code-copy',
    multiple: false,
    clientAppEnhanceFiles: path.resolve(__dirname, "./client.ts"),
    clientAppSetupFiles: path.resolve(__dirname, "./clientAppSetup.ts"),
} as PluginObject