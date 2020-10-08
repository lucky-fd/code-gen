import request from '@/utils/request'

// 查询列表
export function listPreview(query) {
  return request({
    url: '/system/preview/list',
    method: 'get',
    params: query
  })
}
// 查询
export function getPreview(id) {
  return request({
    url: '/system/preview/getById/' + id,
    method: 'get'
  })
}
// 新增
export function addPreview(data) {
  return request({
    url: '/system/preview/add',
    method: 'post',
    data: data
  })
}
// 修改
export function updatePreview(data) {
  return request({
    url: '/system/preview/update',
    method: 'post',
    data: data
  })
}
// 删除
export function delPreview(ids) {
  return request({
    url: '/system/preview/delete',
    method: 'post',
    data:ids
  })
}

// 通过key查询
export function getInfoByKey(key) {
    return request({
        url: '/system/preview/getByKey/' + key,
        method: 'get'
    })
}

// 上传附件
export function uploadFile(formData) {
    return request({
        url: '/system/preview/uploadFile',
        method: 'post',
        data: formData,
        timeout: 1000*60*60*5,
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}
