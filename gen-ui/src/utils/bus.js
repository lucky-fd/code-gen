const install = Vue => {
    Vue.prototype.$bus = new Vue({
        methods: {
            on(event, ...args) {
                this.$on(event, ...args);
            },
            emit(event, callback) {
                this.$emit(event, callback);
            },
            off(event, callback) {
                this.$off(event, callback);
            }
        }
    });
};
export default install;
