import {parseTime} from "@/utils/ruoyi";

const notice = [
    {
        "title": "流程的变动与更改 重要!!!!!!!!!!!",
        "content": "通知：从(23.1.30)起对流程的查看进行了优化与改进，新增了流程中心的模块，现在自己发起的流程在我的流程处进行查看，如果有相关待审的流程在我的待办处进行查看，非必要流程将不会使用消息进行通知!!。"+
            "如之前的流程存在问题，请联系管理员",
        "footer": "2023.1.30"
    },
    {
        "title": "新增财务费用报销流程:",
        "content": "通知：下周一（22.12.19）开始启用线上费用报销流程。",
        "footer": "2022.12.14"
    }
]
const mealNotice = [
    {
        "mealTitle": "每日报餐申报时间:",
        "mealContent": "1.晚餐申报时间：15:00前"+'\n'+"2.早餐申报时间：16:40前",
        "mealFooter": parseTime(new Date(),'{y}-{m}-{d}')
    },
]
export {
    notice,mealNotice
}
