import {defineClientAppEnhance} from '@vuepress/client'
import InstallButton from './components/InstallButton.vue'

export default defineClientAppEnhance(({app}) => {
    app.component("InstallButton", InstallButton)
})