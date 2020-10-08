<template>
    <div
        class="notice-bar"
        :style="{
      width: `${width}px`,
      '--wrapper-width': `${width}px`,
      '--font-color': fontColor,
      '--icon-color': iconColor,
    }"
    >
        <el-icon class="notice-bar__icon">
        </el-icon>
        <div class="notice-bar__content" ref="wrapperRef">
      <span
          class="text"
          ref="contentRef"
          :style="{ animationDuration: animationDuration }"
      >{{ message }}</span
      >
        </div>
    </div>
</template>

<script>

export default {
    name: "NoticeBar",
    props: {
        message: {
            type: String,
            default: "横向滚动消息框示例文本",
        },
        width: {
            type: Number,
            default: 400,
        },
        scrollSpeed: {
            type: Number,
            default: 1,
        },
        fontColor: {
            type: String,
            default: "black",
        },
        iconColor: {
            type: String,
            default: "black",
        },
    },
    data() {
        return {
            animationDuration: "4s",
        };
    },
    methods: {
        // 根据内容长度动态计算单次动画所需时间
        calAnimationDuration() {
            const contentWidth = Math.max(400, this.$refs.contentRef.clientWidth);
            const wrapperWidth = this.$refs.wrapperRef.clientWidth;
            console.log(contentWidth, wrapperWidth);
            return `${Math.ceil(
                ((contentWidth / wrapperWidth) * 4) / this.scrollSpeed
            )}s`;
        },
    },
    mounted() {
        this.animationDuration = this.calAnimationDuration();
    },
};
</script>

<style scoped>
.notice-bar {
    margin: 0 auto;
    display: flex;
    align-items: center;
    overflow: hidden;
}

@keyframes textAnim {
    0% {
        transform: translateX(var(--wrapper-width));
    }
    100% {
        transform: translateX(-100%);
    }
}

.notice-bar__icon {
    color: var(--icon-color);
}

.notice-bar__content {
    overflow: hidden;
    flex: 1;
}

.text {
    display: inline-block;
    white-space: nowrap;
    animation: textAnim;
    animation-duration: 4s;
    animation-iteration-count: infinite;
    animation-timing-function: linear;
    color: var(--font-color);
}
</style>

