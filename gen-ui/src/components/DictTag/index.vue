<template>
  <span>
    <template v-for="(item, index) in options">
      <template v-if="values.includes(item.value)">
        <span v-if="item.raw.listClass == 'default' || item.raw.listClass == ''"
              :key="item.value"
              :index="index"
              :class="item.raw.cssClass"
        >{{ item.label }}</span>
        <el-tag v-else-if="defaultType.includes(item.raw.listClass)"
                :disable-transitions="true"
                :key="item.value"
                :index="index"
                :type="item.raw.listClass"
                :class="item.raw.cssClass">
          {{ item.label }}
        </el-tag>
        <el-tag v-else
                :disable-transitions="true"
                :key="item.value"
                :index="index"
                :class="item.raw.listClass">
          {{ item.label }}
        </el-tag>
      </template>
    </template>
  </span>
</template>

<script>
export default {
    name: "DictTag",
    props: {
        options: {
            type: Array,
            default: null,
        },
        value: [Number, String, Array],
    },
    data() {
        return {
            defaultType: ['primary', 'success', 'info', 'warning', 'danger']
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
    },
};
</script>
<style scoped>
.el-tag + .el-tag {
    margin-left: 10px;
}
.orange {
    background: #FFFF00;
    color: #FF8C00;
}

.pink {
    background: #FFF0F5;
    color: #FFC0CB;
}
.beige {
    background: #E0FFFF;
    color: #20B2AA;
}
.violet{
    background: #D8BFD8;
    color: #DA70D6;
}
.blue {
    background: #E0FFFF;
    color: #00BFFF;
}
</style>
