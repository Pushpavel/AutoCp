<template>
  <div ref="installBtn"></div>
</template>

<script>
export default {
  name: "InstallButton",
  mounted() {
    const original = document.querySelector('#installBtnOriginal > div')
    const clone = original.cloneNode(true);
    this.$refs.installBtn.appendChild(clone)
  }
}
</script>

<style>
iframe {
  color-scheme: initial;
}
</style>