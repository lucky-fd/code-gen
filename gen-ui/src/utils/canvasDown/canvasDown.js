
import html2canvas from "html2canvas";
import jsPDF from "jspdf";

/**
 * 画布转为为pdf，放入zip中
 * @param {*} el 页面元素名
 * @param {*} zip JSZIp
 * @param {*} title 文件名称,不用加后缀
 * @returns
 */

export  function addPdfToZip(el,zip,name){
    return new Promise((resolve) => {
        html2canvas(document.querySelector(el), {
            allowTaint: true,
            scale: 2
        }).then((canvas) => {
            // 得到canvas画布的单位是px 像素单位
            var contentWidth = 800
            var contentHeight = 800

            // 将canvas转为base64图片
            var pageData = canvas.toDataURL('image/jpeg', 1.0)

            // 设置pdf的尺寸，pdf要使用pt单位 已知 1pt/1px = 0.75   pt = (px/scale)* 0.75
            // 2为上面的scale 缩放了2倍
            var pdfX = (contentWidth + 30) / 2 * 0.75
            var pdfY = (contentHeight + 30) / 2 * 0.75 // 500为底部留白

            // 设置内容图片的尺寸，img是pt单位
            var imgX = pdfX
            var imgY = (contentHeight / 2 * 0.75) // 内容图片这里不需要留白的距离

            // 初始化jspdf 第一个参数方向：默认''时为纵向，第二个参数设置pdf内容图片使用的长度单位为pt，第三个参数为PDF的大小，单位是pt
            let pdf = new jsPDF('', 'pt', [pdfX, pdfY])

            // 将内容图片添加到pdf中，因为内容宽高和pdf宽高一样，就只需要一页，位置就是 0,0
            pdf.addImage(pageData, 'jpeg', 0, 0, imgX, imgY)
            //将结果放到zip中
            let blobPDF =  pdf.output('blob')
            zip.file(name+'.pdf',blobPDF);
            resolve();
        })
    })
};

/*
    下载zip导包
    zip：jsZIp
    name:输出名字，不用加后缀，默认为zip
 */
export function createZip(zip,name) {
    zip.generateAsync({ type: 'blob' }).then(function (content) {
        saveAs(content, name+'.zip');
    });
};

/**
 * idl:元素id名字
 *  下载为单个图片
 *  fileName:导出的问题名
 */
export function  exportCanvasAsPNG(fileName,id) {
    let canvasElement = document.getElementById(id);
    let MIME_TYPE = "image/jpeg";
    let imgURL = canvasElement.toDataURL(MIME_TYPE);
    let dlLink = document.createElement('a');
    dlLink.download = fileName;
    dlLink.href = imgURL;
    dlLink.dataset.downloadurl = [MIME_TYPE, dlLink.download, dlLink.href].join(':');
    document.body.appendChild(dlLink);
    dlLink.click();
    document.body.removeChild(dlLink);
};


/*
    转为图片放入zip中
    canvas：画布元素：获取(document.getElementById('画布id'))
    zip：jsZIp
    name：文件名，不能有一样的文件名重复，需要指定后缀，支持图片类型
 */
export  function addToZip(canvas, zip, name) {
    return new Promise((resolve, reject) => {
        canvas.toBlob(function (blob) {
            zip.file(name, blob);
            resolve();
        });
    })
};

