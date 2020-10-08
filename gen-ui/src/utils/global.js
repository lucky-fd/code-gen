import store from "@/store";

/*
    判断目标值，是否存在于数组对象中的某个属性
 */
export function existInArrObjProperty(tar,arr,prop) {
    for (let item of arr) {
        if(item[prop] === tar){
            return true
        }
    }
    return false
}


/*
    判断目标是否为空
 */
export function isEmpty(obj) {
    return obj === null || obj === undefined || obj === '';
}

/*
    将true，false改为 ‘1’，‘0’
 */
export function matchBool(obj) {
    if( typeof obj == 'boolean'){
        if(obj){
            return '1'
        }
        return '0'
    }else if(typeof obj == 'string'){
        return obj === '1';
    }
    return undefined;
}


/*
    数组是否包含某个对象的属性
 */
export function arrContains(arr, val) {
    for (let i = 0; i < arr.length; i++) {
        if ( arr[i] === val) {
            return true;
        }
    }
    return false;
}

/*
    数组是否包含某个对象的属性
 */
export function arrContainsByProperty(arr, val,property) {
    for (let i = 0; i < arr.length; i++) {
        if (arr[i][property] === val) {
            return true;
        }
    }
    return false;
}


/*
    删除数组中的某个对象
 */
export function delArrObjByProperty(arr, val,property) {
    for (let i = 0; i < arr.length; i++) {
        if (arr[i][property] === val) {
            arr.splice(i,1)
            return
        }
    }
}


/*
    判断数组中是否包含自己
 */
export function checkOwner(arr) {
    let currentUser = store.getters.currentUser
    if(arr instanceof Array){
        for (let i = 0; i < arr.length; i++) {
            if (arr[i] === currentUser.userId+'') {
                return true
            }
        }
    }else{
        console.log('checkOwner数据异常:',arr)
        return false
    }


}

/*
    人员翻译
 */
export function findUserById(userId) {
    let userList = store.state.cache.system.userSelect
    let result = userList.find(user=>{
        return user.userId === userId
    })
    return result
}

/*
    在数组中寻找目标值
 */
export function findInArr(arr,target,property) {
    let result = arr.find(item=>{
        return item[property] === target
    })
    return result
}


