<template>
    <span>{{ label }}</span>
</template>

<script>
export default {
    name: "SelectTag",
    props: {
        options: {
            type: Array,
            default: null,
        },
        value: [Number, String, Array],
        splitReg: {
            type: String,
            default: ','
        }
    },
    computed: {
        values() {

            if (this.value !== null && typeof this.value !== 'undefined') {
                return Array.isArray(this.value) ? this.value : [String(this.value)];
            } else {
                return [];
            }
        },
        label() {
            let arr = []
            if (this.options === null) {
                return ""
            }
            this.options.forEach(item => {
                if (this.values.includes(item.value) || this.values.includes(String(item.value))) {
                    arr.push(item.label)
                }
            })
            if (arr.length !== 0) {
                return arr.join(this.splitReg)
            }
            return ""
        }
    },
};
</script>
<style scoped>
.el-tag + .el-tag {
    margin-left: 10px;
}
</style>
