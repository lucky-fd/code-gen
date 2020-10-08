import request from '@/utils/request'

// 查询模板列表
export function listExportModule(query) {
    return request({
        url: '/system/exportModule/list',
        method: 'get',
        params: query
    })
}

// 查询模板详细
export function getExportModule(moduleId) {
    return request({
        url: '/system/exportModule/getDetail/' + moduleId,
        method: 'get'
    })
}

// 新增模板
export function addExportModule(data) {
    return request({
        url: '/system/exportModule/add',
        method: 'post',
        data: data
    })
}

// 修改模板
export function updateExportModule(data) {
    return request({
        url: '/system/exportModule/update',
        method: 'post',
        data: data
    })
}

// 删除模板
export function delExportModule(moduleIds) {
    return request({
        url: '/system/exportModule/delete',
        method: 'post',
        data: moduleIds
    })
}


export function exportModuleSelect() {
    return request({
        url: '/system/exportModule/moduleSelect',
        method: 'get'
    })
}
