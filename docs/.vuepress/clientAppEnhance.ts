import {defineClientAppEnhance} from '@vuepress/client'

export default defineClientAppEnhance(() => {
    // @ts-ignore
    if (!__VUEPRESS_SSR__)
        globalThis.MarketplaceWidget.setupMarketplaceWidget('install', 17061, "#installBtnOriginal");
})