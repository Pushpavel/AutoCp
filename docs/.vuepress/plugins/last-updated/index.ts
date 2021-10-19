import {PluginFunction, PluginObject} from "vuepress";

const path = require('path')
const spawn = require('cross-spawn')

export default (options: any, app) => ({
    extendsPageData(page, app) {
        const {transformer, dateOptions} = options
        const timestamp = getGitLastUpdatedTimeStamp(page.filePath)
        const $lang = page.lang
        if (timestamp) {
            return {
                ...page,
                lastUpdated: (typeof transformer === 'function'
                    ? transformer(timestamp, $lang)
                    : defaultTransformer(timestamp, $lang, dateOptions))
            }
        }
        return page as any
    }
} as PluginObject) as PluginFunction<any, any>

function defaultTransformer(timestamp, lang, dateOptions) {
    return new Date(timestamp).toLocaleString(lang ?? "en", dateOptions ?? {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    })
}

function getGitLastUpdatedTimeStamp(filePath) {
    let lastUpdated
    try {
        lastUpdated = parseInt(spawn.sync(
            'git',
            ['log', '-1', '--format=%at', path.basename(filePath)],
            {cwd: path.dirname(filePath)}
        ).stdout.toString('utf-8')) * 1000
    } catch (e) { /* do not handle for now */
    }
    return lastUpdated
}