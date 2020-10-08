import {getToken,} from '@/utils/auth'
import {Message} from "element-ui";

let socket = null
let isConnect = false
let globalCallback = (e) => {
    console.log(e);
}
let reConnectNum = 0
let url = null
// 心跳 60s一次，防止长时间不交互后台断开
let heartCheck = {
    heartbeatData: {
        msgType: 0,
        msgTag: '发送心跳信息~'
    },
    timeout: 60 * 1000,
    heartbeat: null,
    start: function () {
        this.heartbeat = setInterval(() => {
            if (isConnect) {
                webSocketSend(this.heartbeatData)
            } else {
                this.clear()
            }
        }, this.timeout);
    },
    reset: function () {
        clearInterval(this.heartbeat)
        this.start()
    },
    clear: function () {
        clearInterval(this.heartbeat)
    }
}

// 初始化连接
export function initWebSocket(wsURL, callback) {
    if (wsURL != null) {
        url = wsURL
    }
    if (callback) {
        globalCallback = callback
    }
    if ('WebSocket' in window) {
        socket = new WebSocket(wsURL, [getToken()])
    } else {
        Message({
            message: '浏览器不支持webSocket!',
            type: 'warning'
        })
        return
    }
    socket.onopen = () => {
        webSocketOnOpen()
    }
    socket.onmessage = (e) => {
        webSocketOnMessage(e)
    }
    socket.onclose = (e) => {
        webSocketOnClose(e)
    }
    socket.onerror = (e) => {
        webSocketOnError(e)
    }
}

function webSocketOnOpen() {
    heartCheck.start()
    isConnect = true
    setTimeout(()=>{
        if (isConnect){
            reConnectNum = 0
            console.log('[socket] 连接成功')
        }
    },1000)

}

function webSocketOnMessage(e) {
    console.log('[socket] 收到推送消息');
    const data = JSON.parse(e.data)
    globalCallback(data)
}

function webSocketOnClose(e) {
    heartCheck.clear()
    isConnect = false
    console.log('[socket] 关闭连接',+ e.code + ' ' + e.reason + ' ' + e.wasClean);
    if (e.code !== 1000 || e.code !== 1001) {
        if (reConnectNum < 2) {
            initWebSocket(url, globalCallback)
            ++reConnectNum
        }
        // else {
        //     Message({
        //         message: '消息系统无法连接,清刷新页面重试',
        //         type: 'warning'
        //     })
        // }
    }
}

function webSocketOnError(e) {
    heartCheck.clear()
    isConnect = false
    console.log('[socket] 连接发生错误', e);
}

// 发送数据
function webSocketSend(data) {
    socket.send(JSON.stringify(data))
}

// 主动关闭
export function closeWebSocket(e) {
    socket.close()
    heartCheck.clear()
    isConnect = false
    reConnectNum = 0
}

// 在组件中接受数据
export function getSocket(callback) {
    globalCallback = callback
}

// 判断socket连接状态
export function sendSocket(agenData) {
    switch (socket.readyState) {
        case socket.CONNECTING:
            setTimeout(() => {
                sendSocket(agenData, callback)
            }, 1000)
            break
        case socket.OPEN:
            webSocketSend(agenData)
            break
        case socket.CLOSING:
            setTimeout(() => {
                sendSocket(agenData, callback)
            }, 1000)
            break
        case socket.CLOSED:
            break
        default:
            break
    }
}



