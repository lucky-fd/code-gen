import Vue from 'vue'

//引入图片预览
import Element, {Image} from 'element-ui'
import './assets/styles/element-variables.scss'
import '@/assets/styles/index.scss' // global css
import '@/assets/styles/ruoyi.scss' // ruoyi css
import App from './App'
import store from './store'
import router from './router'
import directive from './directive' // directive
import plugins from './plugins' // plugins
import {download, downloadExcel} from '@/utils/request'

import './assets/icons' // icon
import './permission' // permission control
import {addDateRange, handleTree, parseTime, resetForm, selectDictLabel, selectDictLabels} from "@/utils/ruoyi";
import {isEmpty} from '@/utils/global'


// 分页组件
import Pagination from "@/components/Pagination";
// 自定义表格工具组件
import RightToolbar from "@/components/RightToolbar"
// 富文本组件
import Editor from "@/components/Editor"
// 字典标签组件
import DictTag from '@/components/DictTag'
// 头部标签组件
import VueMeta from 'vue-meta'
// 字典数据组件
import DictData from '@/components/DictData'
// 复制粘贴依赖
import VueClipboard from 'vue-clipboard2'
//引入图片懒加载
import VueLazyload from 'vue-lazyload'
import SelectTag from "@/components/SelectTag";
import SelectTransfer from "@/components/SelectTransfer";
import TableTransfer from "@/components/TableTransfer";
import Bus from "@/utils/bus"
import * as Base64 from 'js-base64'


import {UTable, UTableColumn} from 'umy-ui';


// 全局方法挂载
Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.selectDictLabel = selectDictLabel
Vue.prototype.selectDictLabels = selectDictLabels
Vue.prototype.download = download
Vue.prototype.downloadExcel = downloadExcel
Vue.prototype.handleTree = handleTree
Vue.prototype.isEmpty = isEmpty
Vue.prototype.Base64 = Base64
// 全局组件挂载
Vue.component('DictTag', DictTag)
Vue.component('SelectTag', SelectTag)
Vue.component('Pagination', Pagination)
Vue.component('RightToolbar', RightToolbar)
Vue.component('Editor', Editor)
Vue.component('SelectTransfer', SelectTransfer)
Vue.component('TableTransfer', TableTransfer)
Vue.component('el-image-viewer', Image.components.ImageViewer)
Vue.component(UTable.name, UTable);
Vue.component(UTableColumn.name, UTableColumn);

Vue.use(Bus)
Vue.use(directive)
Vue.use(plugins)
Vue.use(VueMeta)
Vue.use(VueClipboard)
// 注册懒加载
Vue.use(VueLazyload, {
    loading: require("@/assets/images/manDefault.png"), // 加载时
    error: require("@/assets/images/manDefault.png") // 加载失败时
})
DictData.install()

/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online! ! !
 */

Vue.use(Element, {
    size: 'mini'  // set element-ui default size
})
Element.Dialog.props.closeOnClickModal.default = false
Element.Dialog.props.modalAppendToBody.default = false

Vue.config.productionTip = false

new Vue({
    el: '#app',
    router,
    store,
    render: h => h(App)
})
