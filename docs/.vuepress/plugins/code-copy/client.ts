import CodeCopy from "./CodeCopy.vue";

import {defineClientAppEnhance} from "@vuepress/client";

export default defineClientAppEnhance(({app, router, siteData}) => {
    app.component("CodeCopy", CodeCopy);
});
