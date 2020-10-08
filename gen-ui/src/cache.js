import store from './store'
import {Message} from 'element-ui'

const SupplyChain = 'supply-chain'
const Storehouse = 'storehouse'

export function chechCache(to,from,next){
    if(to.path.indexOf(SupplyChain) !== -1 || to.path.indexOf(Storehouse) !== -1){
        refreshSypplyChainCache()
    }
}

/**
 * 刷新供应链的缓存
 */
function refreshSypplyChainCache(){
    if(!store.state.cache.supplyChain.hasValue){
        //先修改hashValue有值
        store.commit('SET_SYPPLY_CHAIN_HAS_VALUE')

        //获取缓存
        store.dispatch("SET_SUPPLY_CHAIN_CACHE")
    }
}
