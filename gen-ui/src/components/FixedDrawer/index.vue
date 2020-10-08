<template>
    <div class="main">
        <button @click="handleButton">侧边栏按钮</button>

        <el-drawer
            title=""
            :visible.sync="open"
            direction="rtl"
            append-to-body
            >
            <slot name="content"></slot>
        </el-drawer>
    </div>
</template>

<script>
export default {
    name: "FixedDrawer",
    props: {},
    dicts:[],
    components: {},
    data() {
        return {
            open:false
        };
    },
    watch: {},
    created() {
    },
    computed: {},
    methods: {
        handleButton(){
            this.open = true
        }
    },
};
</script>

<style scoped lang="scss">

.main{
    position: fixed;
    top: 30%;
    right:0;
}

</style>

