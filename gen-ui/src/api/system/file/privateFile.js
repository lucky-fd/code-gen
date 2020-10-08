import request from '@/utils/request'



// 新增文件夹
export function addNewPath(data) {
    return request({
        url: '/system/files/privateAddNewPath',
        method: 'post',
        data: data
    })
}


// 新增文件夹
export function getList(data) {
    return request({
        url: '/system/files/privateGetList',
        method: 'post',
        data: data
    })
}

// 上传文件
export function uploadFile(data,currentPath) {
    let formData = new FormData()
    let file = data.raw
    formData.append('file',file)
    return request({
        url: '/system/files/privateUploadFile/'+currentPath,
        method: 'post',
        data: formData,
        timeout:1000 * 60 * 20,
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}

export function delFilePath(data){
    return request({
        url: '/system/files/privateDelFilePath',
        method: 'post',
        data: data
    })
}

export function delFile(data){
    return request({
        url: '/system/files/privateDelFile',
        method: 'post',
        data: data
    })
}


export function getDownloadKey(data){
    return request({
        url: '/system/files/privateDownFile',
        method: 'post',
        data: data
    })
}

