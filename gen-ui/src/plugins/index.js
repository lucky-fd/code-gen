import tab from './tab'
import auth from './auth'
import cache from './cache'
import modal from './modal'
import download from './download'
import customize from './customize'
import global from "./global";

export default {
    install(Vue) {
        // 页签操作
        Vue.prototype.$tab = tab
        // 认证对象
        Vue.prototype.$auth = auth
        // 缓存对象
        Vue.prototype.$cache = cache
        // 模态框对象
        Vue.prototype.$modal = modal
        // 系统导出文件功能
        Vue.prototype.$download = download
        // 自定义
        Vue.prototype.$cust = customize
        // 全局变量
        Vue.prototype.$global = global

    }
}
