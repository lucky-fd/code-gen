export default {
    showOnPc() {
        return document.documentElement.clientWidth >= 500
    },
    showOnPe() {
        return document.documentElement.clientWidth < 500
    }

}

