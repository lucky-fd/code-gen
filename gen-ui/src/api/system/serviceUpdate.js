import request from '@/utils/request'

// 查询列表
export function listServiceUpdate(query) {
    return request({
        url: '/system/serviceUpdate/list',
        method: 'get',
        params: query
    })
}

// 查询
export function getServiceUpdate(id) {
    return request({
        url: '/system/serviceUpdate/getById/' + id,
        method: 'get'
    })
}

// 新增
export function addServiceUpdate(data) {
    return request({
        url: '/system/serviceUpdate/add',
        method: 'post',
        data: data
    })
}

// 修改
export function updateServiceUpdate(data) {
    return request({
        url: '/system/serviceUpdate/update',
        method: 'post',
        data: data
    })
}

// 删除
export function delServiceUpdate(ids) {
    return request({
        url: '/system/serviceUpdate/delete',
        method: 'post',
        data: ids
    })
}

// 归档
export function archive(ids) {
    return request({
        url: '/system/serviceUpdate/archive',
        method: 'post',
        data: ids
    })
}

// 取消归档
export function cancelArchive(ids) {
    return request({
        url: '/system/serviceUpdate/cancelArchive',
        method: 'post',
        data: ids
    })
}

//导出Excel
export function exportExcel(data) {
    return request({
        url: '/system/serviceUpdate/export',
        method: 'post',
        timeout: 1000 * 60 * 10,
        data: data,
        responseType: 'blob'
    })
}

// 发起更新
export function doUpdate(data) {
    return request({
        url: '/system/serviceUpdate/doUpdate',
        method: 'post',
        data: data
    })
}

// 完成更新
export function stopUpdate(data) {
    return request({
        url: '/system/serviceUpdate/stopUpdate',
        method: 'post',
        data: data
    })
}

// 取消更新
export function cancelUpdate(data) {
    return request({
        url: '/system/serviceUpdate/cancelUpdate',
        method: 'post',
        data: data
    })
}



//******RestForm********
export function resetForm(that, targetForm) {
    that[targetForm] = {
        id: null,
        appName: null,
        routers: null,
        currentVersion: null,
        updatingVersion: null,
        updating: null,
        status: null,
        updatedRefresh: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        active: null,
        delFlag: null,
        remark: null,
        reMark2: null,
        reMark3: null,
        reMark4: null,
        reMark5: null,
        reMark6: null,
        reMark7: null,
        reMark8: null,
        reMark9: null
    };
    that.resetForm(targetForm)
}

//******QueryForm*******

const queryParams = {
    pageNum: 1,
    pageSize: 10,
    active: true,
    appName: null,
    routers: null,
    currentVersion: null,
    updatingVersion: null,
    updating: null,
    status: null,
    updatedRefresh: null,
    reMark2: null,
    reMark3: null,
    reMark4: null,
    reMark5: null,
    reMark6: null,
    reMark7: null,
    reMark8: null,
    reMark9: null
}

export {
    queryParams
}

//******校验规则**********

const serviceUpdateRules = {
    appName: [
        {required: true, message: "请输入内容", trigger: "blur"}
    ],
    routers: [
        {required: true, message: "请输入内容", trigger: "blur"}
    ],
    currentVersion: [
        {required: true, message: "请输入内容", trigger: "blur"}
    ],
    updatingVersion: [
        {required: true, message: "请输入内容", trigger: "blur"}
    ],
    updating: [
        {required: true, message: "请输入内容", trigger: "blur"}
    ],
    status: [
        {required: true, message: "请输入内容", trigger: "blur"}
    ],
    updatedRefresh: [
        {required: true, message: "请输入内容", trigger: "blur"}
    ],
    active: [
        {required: true, message: "请输入内容", trigger: "blur"}
    ],
    showName: [
        {required: true, message: "请输入内容", trigger: "blur"}
    ],
    jenkinsJobName: [
        {required: true, message: "请输入内容", trigger: "blur"}
    ],
    notice: [
        {required: true, message: "请选择内容", trigger: "blur"}
    ],
    reMark5: [
        {required: true, message: "请输入内容", trigger: "blur"}
    ],
    reMark6: [
        {required: true, message: "请输入内容", trigger: "blur"}
    ],
    reMark7: [
        {required: true, message: "请输入内容", trigger: "blur"}
    ],
    reMark8: [
        {required: true, message: "请输入内容", trigger: "blur"}
    ],
    reMark9: [
        {required: true, message: "请输入内容", trigger: "blur"}
    ],
}

export {
    serviceUpdateRules
}
