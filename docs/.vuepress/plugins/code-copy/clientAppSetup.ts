import {createApp} from "vue";
import CodeCopy from "./CodeCopy.vue";
import {defineClientAppSetup} from "@vuepress/client";
import R from "../../configs/R";

export default defineClientAppSetup(() => {
    document.addEventListener(R.events.pageMounted, () => {
        document.querySelectorAll('div[class*="language-"]').forEach((block) => {
            let options = {
                backgroundColor: "#0075b8",
                backgroundTransition: true,
            };
            let instance = createApp(CodeCopy, {
                parent: block,
                code: block.querySelector("pre").innerText,
                options: options,
            });
            let childEl = document.createElement("div");
            block.appendChild(childEl);
            instance.mount(childEl);
        });
    })
});
