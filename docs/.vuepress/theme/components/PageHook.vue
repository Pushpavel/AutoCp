<template></template>
<script>
import R from "../../configs/R";

export default {
  name: "PageHook",
  mounted() {
    document.dispatchEvent(new Event(R.events.pageMounted))
  }
}
</script>