<template>
    <span v-if="num&&num.length!==0">
       <span :style="prefixStyle">{{ num.substring(0, valueLen - serialLen - dateLen) }}</span>
       <span :style="yearStyle">{{
               num.substring(valueLen - serialLen - dayLen - monthLen - yearLen, valueLen - serialLen - dayLen - monthLen)
           }}</span>
       <span :style="monthStyle">{{
               num.substring(valueLen - serialLen - dayLen - monthLen, valueLen - serialLen - dayLen)
           }}</span>
       <span :style="dayStyle">{{
               num.substring(valueLen - serialLen - dayLen, valueLen - serialLen)
           }}</span>
       <span :style="serialNumStyle">{{ num.substring(valueLen - serialLen) }}</span>
    </span>
</template>

<script>
export default {
    name: "SerialNumberFormat",
    props: {
        //尾号流水长度
        serialLen: {
            type: Number,
            default() {
                return 4
            }
        },
        date: {
            type: String,
            regex: "",
            default() {
                return "yyMMdd"
            }
        },
        num: {
            type: String,
            default() {
                return ""
            }
        },
        prefixStyle: {
            type: Object,
            default() {
                return {
                    "color": "#112438"
                }
            }
        },
        yearStyle: {
            type: Object,
            default() {
                return {
                    "color": "#0f59a6"
                }
            }
        },
        monthStyle: {
            type: Object,
            default() {
                return {
                    "color": "#07295b",
                    "marginLeft":"2px"
                }
            }
        },
        dayStyle: {
            type: Object,
            default() {
                return {
                    "color": "#0b5bad",
                    "marginLeft":"2px"
                }
            }
        },
        serialNumStyle: {
            type: Object,
            default() {
                return {
                    "color": "#000810",
                    "marginLeft":"2px"
                }
            }
        }
    },
    dict: [],
    components: {},
    data() {
        return {
            yearLen: 0,
            monthLen: 0,
            dayLen: 0
        };
    },
    watch: {
        date: {
            immediate: true,
            handler(val) {
                for (let i in val) {
                    if (val[i] === 'y') {
                        this.yearLen++
                    } else if (val[i] === 'M') {
                        this.monthLen++
                    } else {
                        this.dayLen++
                    }
                }
            }
        }
    },
    created() {
    },
    computed: {
        dateLen() {
            return this.date.length
        },
        valueLen() {
            return this.num.length
        }
    },
    methods: {},
};
</script>

<style scoped lang="scss">
.dd {
    color: red;
}
</style>

