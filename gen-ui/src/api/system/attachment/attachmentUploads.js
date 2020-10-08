import request from '@/utils/request'
//查询附件
export function listAttachment(attachmentNo) {
    return request({
        url: '/system/attachment/listBy/'+attachmentNo,
        method: 'get',
    })
}

// 附件上传
export function uploads(data,url,attachmentNo) {
    let formData = new FormData();
    data.forEach(item=>{
        formData.append('files',item.raw);
    })
    formData.append('url',url);
    if (attachmentNo === null) {
        formData.append('attachmentNo','');
    }else {
        formData.append('attachmentNo',attachmentNo);
    }
    return request({
        url: '/system/attachment/uploads',
        method: 'post',
        data: formData,
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}

// 附件上传
export function multipleUploads(data) {
    return request({
        url: '/system/attachment/multipleUploads',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}

//附件删除
export function delAttachment(attachmentId) {
    return request({
        url: '/system/attachment/del/'+attachmentId,
        method: 'get',
    })
}

//附件删除
export function delAttachmentByNo(attachmentByNo) {
    return request({
        url: '/system/attachment/delAttachmentByNo/'+attachmentByNo,
        method: 'get',
    })
}

//获得单个附件
export function getInfo(attachmentId) {
    return request({
        url: '/system/attachment/'+attachmentId,
        method: 'get',
    })
}

//获取附件key
export function getDownloadKey(attachmentId) {
    return request({
        url: '/system/attachment/getDownloadKey/'+attachmentId,
        method: 'post',
    })
}

