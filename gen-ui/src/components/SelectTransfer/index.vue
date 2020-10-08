<template>
    <div @click.capture.stop="clickFun">
        <el-dialog :visible.sync="open" width="36%" append-to-body>
            <el-transfer
                style="margin-left: 5%"
                filterable
                filter-placeholder="请输入搜索值"
                v-model="selectValue"
                :data="options"
                :titles="titles"

            >
            </el-transfer>
            <span slot="footer" class="dialog-footer">
                <el-button @click="open = false">取 消</el-button>
                <el-button type="primary" @click="submit">确 定</el-button>
            </span>
        </el-dialog>




        <el-select v-model="selectValue" multiple placeholder="请选择">
            <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
        </el-select>

    </div>
</template>

<script>
export default {
    name: "SelectTransfer",
    props: {
        options: {
            type: Array
        },
        value: {
            type: Array,
            default:[]
        },
        titles:{
            type:Array,
            default:()=>['选择', '确定']
        },
        props:{
            type:Object,
            default:{
                key: 'key',
                label: 'label',
                disabled:'disabled'
            }
        }
    },
    components: {},
    data() {
        return {
            open: false,
            selectValue: this.value,
        };
    },
    watch: {},
    created() {
        let temp = JSON.parse(JSON.stringify(this.value));
        if (temp !== null && typeof temp !== 'undefined') {
            this.selectValue = Array.isArray(temp) ? temp : [String(temp)];
        } else {
            this.selectValue = [];
        }
    },
    computed: {},
    methods: {
        clickFun() {
            this.open = true
        },
        submit() {
            this.$emit("input", this.selectValue)
            this.open = false
        }
    },
};
</script>

<style scoped lang="scss">

::v-deep .el-icon-close{
    display: none;

}
::v-deep .el-select__caret{
    display: none;
}
</style>

