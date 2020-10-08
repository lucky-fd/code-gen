<template>
    <div class="block">
        <template>
            <el-select v-model="boolValue" placeholder="请选择" @change="change"  :disabled="disabled">
                <el-option
                    v-for="item in dict.type.whether_or_not"
                    :key="item.value"
                    :label="item.label"
                    :value="matchBool(item.value)"
                >
                </el-option>
            </el-select>
        </template>
    </div>
</template>

<script>


export default {
    name: "WhetherOrNotSelect",
    dicts: ['whether_or_not'],
    props: {
        value: {
            type: Boolean,
            default() {
                return undefined
            }
        },
        disabled: {
            type: Boolean,
            default() {
                return false
            }
        },
        reverse: {
            type: Boolean,
            default() {
                return false
            }
        }
    },
    dict: [],
    components: {},
    data() {
        return {
            boolValue: this.value
        };
    },
    watch: {
        value(val) {
            if (this.reverse) {
                this.boolValue = !this.value
            } else {
                this.boolValue = this.value
            }
        }
    },
    created() {
        if (this.reverse) {
            this.boolValue = !this.value
        } else {
            this.boolValue = this.value
        }
    },
    computed: {},
    methods: {
        change(value) {
            if (this.reverse) {
                value = !value
            }
            this.$emit('input', value)
        }
    },
};
</script>

<style scoped lang="scss">

</style>

