import Vue from 'vue'

//引入图片预览
import Element, {Image, Statistic} from 'element-ui'
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
import {getDicts} from "@/api/system/dict/data";
import {getConfigKey} from "@/api/system/config";
import {addDateRange, handleTree, parseTime, resetForm, selectDictLabel, selectDictLabels} from "@/utils/ruoyi";
import {
    arrContains,
    arrContainsByArrProperty,
    arrContainsByProperty, checkOwner,
    existInArrObjProperty,
    isEmpty,
    matchBool,
} from '@/utils/global'
import {checkPermi, checkRole} from '@/utils/permission'


// 分页组件
import Pagination from "@/components/Pagination";
// 自定义表格工具组件
import RightToolbar from "@/components/RightToolbar"
// 富文本组件
import Editor from "@/components/Editor"

// 图片预览组件
import ImagePreview from "@/components/ImagePreview"
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
//echarts
import echarts from 'echarts'


import {UTable, UTableColumn} from 'umy-ui';

Vue.use(MyPD);

// 全局方法挂载
Vue.prototype.getDicts = getDicts
Vue.prototype.getConfigKey = getConfigKey
Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.selectDictLabel = selectDictLabel
Vue.prototype.selectDictLabels = selectDictLabels
Vue.prototype.download = download
Vue.prototype.downloadExcel = downloadExcel
Vue.prototype.handleTree = handleTree
Vue.prototype.existInArrObjProperty = existInArrObjProperty
Vue.prototype.isEmpty = isEmpty
Vue.prototype.matchBool = matchBool
Vue.prototype.arrContainsByProperty = arrContainsByProperty
Vue.prototype.arrContainsByArrProperty = arrContainsByArrProperty
Vue.prototype.arrContains = arrContains
Vue.prototype.checkOwner = checkOwner


Vue.prototype.checkPermi = checkPermi
Vue.prototype.checkRole = checkRole


Vue.prototype.viewDetailWithPro = viewDetailWithPro
Vue.prototype.downloadFromMinio = downloadFromMinio
Vue.prototype.deepClone = deepClone
Vue.prototype.Base64 = Base64
Vue.prototype.$echarts = echarts

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
