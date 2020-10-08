<template>
    <div id="app">
        <router-view/>
    </div>
</template>

<script>
export default {
    name: 'App',
    components: {},
    metaInfo() {
        return {
            title: "gen",
            titleTemplate: title => {
                return title ? `${title} - ${process.env.VUE_APP_TITLE}` : process.env.VUE_APP_TITLE
            }
        }
    },

}
</script>
<style>

html, body {
    font-size: 15px;
    font-weight: 600;
    font-family: Helvetica,Arial,sans-serif,"Microsoft YaHei","微软雅黑";
}


input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
    -webkit-appearance: none;
}

input[type='number'] {
    -moz-appearance: textfield;
}


</style>

