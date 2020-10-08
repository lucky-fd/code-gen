<template>
    <div>
        <div class="run-tag" v-if="running">
            <svg-icon icon-class="扫码枪"/>
        </div>
        <div class="inputBox">
            <el-input @change="changeValue" type="password" clearable
                      @blur="blurInput" v-model="scanValue" id="scan-input"></el-input>
        </div>
    </div>
</template>

<script>

export default {
    name: "ScanGunBack",
    props: {
        value: {
            type: [String, Array, Object,Number]
        },
        multiple: {
            type: Boolean,
            default: false
        },
    },
    dicts: [],
    components: {},
    data() {
        return {
            scanValue: '',
            values: [],
            //默认不展示扫码枪图标
            running:false
        }
    },
    watch: {},
    created() {

    },
    mounted() {
    },
    computed: {},
    methods: {
        handleStartScan() {
            document.getElementById("scan-input").focus()
            this.running = true
        },
        handleClearScan() {
            this.scanValue = ""
            this.values = []
            if (this.multiple) {
                this.$emit('input', this.values)
            } else {
                this.$emit('input', "")
            }
            document.getElementById("scan-input").focus()
            this.running = true
        },
        changeValue(value) {
            console.log("扫码枪扫码内容",value)
            try {
                //判断是否包含 { 有则可能是json，没有则按照字符串处理
                if(value.indexOf('{') !== -1){
                    //先把直板字符串转为对象
                    value = JSON.parse(value)
                }else{
                    value = String(value)

                }
            } catch (e) {
                console.log(e)
                this.$modal.msgWarning("扫码拒绝，请联系管理员")
                this.scanValue = ""
                return
            }
            if (this.multiple) {
                this.values.push(value)
                //双向绑定数据回调
                this.$emit('input', this.values)
                //当内容改变时回调
                this.$emit('change', this.values)
                //单独返回变化的新内容
                this.$emit('newValue', value)
            } else {
                //双向绑定数据回调
                this.$emit('input', value)
                //当内容改变时回调
                this.$emit('change', value)
                //单独返回变化的新内容
                this.$emit('newValue', value)
            }
            //初始化内容，等待重新扫描
            this.scanValue = ""
        },
        //失去焦点时触发
        blurInput(){
            this.running = false
        }
    },
};
</script>

<style scoped lang="scss">

.inputBox {
    position: absolute;
    left: -1000px;
}

.run-tag{
    position: absolute;
    top: 5px;
    right: 20px;
    font-size: 80px;
    z-index: 1000;
}
</style>

