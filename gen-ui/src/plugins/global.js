//minio地址
const minioUrl = process.env.VUE_APP_MINIO_DOWN_URL;
//websocket信息地址
const wsUrl = process.env.VUE_APP_WS_URL;
//文件预览地址
const fileViewUrl = process.env.VUE_APP_FILE_VIEW_URL;

//公共篮
const bucketPub = '/cierp-pub'
//私有篮
const bucketPri = '/cierp-pri'


//头像前缀
const userHeadPrefix = minioUrl + bucketPub;
//附件前缀
const attachmentPrefix = minioUrl + bucketPri;
//minio公共url前缀
const minioPubPrefix = minioUrl + bucketPub;

const descLabelStyle = {
    color: '#091d0b',
    fontWeight: 'bold',
    fontFamily: "Helvetica Neue",
    minWidth:'110px',
    width:'10%'
}

const descContentStyle = {}

export default {
    userHeadPrefix,
    minioUrl,
    wsUrl,
    attachmentPrefix,
    descLabelStyle,
    descContentStyle,
    fileViewUrl,
    minioPubPrefix
};
