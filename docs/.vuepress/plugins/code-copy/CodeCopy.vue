<template>
  <div class="code-copy">
    <svg
        @click="copyToClipboard"
        width="24"
        height="24"
        viewBox="0 0 24 24"
        class="hover"
    >
      <path fill="none" d="M0 0h24v24H0z"/>
      <path
          fill="var(--c-brand)"
          d="M16 1H4c-1.1 0-2 .9-2 2v14h2V3h12V1zm-1 4l6 6v10c0 1.1-.9 2-2 2H7.99C6.89 23 6 22.1 6 21l.01-14c0-1.1.89-2 1.99-2h7zm-1 7h5.5L14 6.5V12z"
      />
    </svg>
    <span :class="success ? 'success' : ''" :style="{ color: 'var(--c-brand-light)' }">Copied</span>
  </div>
</template>

<script>
export default {
  name: "CodeCopy",
  props: {
    parent: Object,
    code: String,
    options: {
      backgroundTransition: Boolean,
      backgroundColor: String,
    },
  },
  data() {
    return {
      success: false,
    };
  },
  watch: {
    success: function () {
      if (this.success)
        this.parent.classList.add("success")
      else
        this.parent.classList.remove("success")
    }
  },
  methods: {
    copyToClipboard(el) {
      navigator.clipboard.writeText(this.code).then(() => {
        clearTimeout(this.successTimeout);
        this.success = true;
        this.successTimeout = setTimeout(() => {
          this.success = false;
        }, 1000);
      });
    },
  },
};
</script>
<style lang="scss">
div[class*="language-"]:hover > div > .code-copy svg {
  opacity: 0.75;
}

div[class*="language-"] {
  z-index: 1;
  position: relative;
  overflow: hidden;
  transition: color 0.4s ease-in-out;

  &::before {
    content: '';
    z-index: -1;
    position: absolute;
    top: 100%;
    left: 100%;
    width: 1em;
    height: 1em;
    border-radius: 50%;
    background-color: var(--c-brand-light);
    opacity: 0.1;
    transform-origin: center;
    transform: translate3d(-50%, -50%, 0) scale3d(0, 0, 0);
    transition: transform 0.45s ease-in-out;
  }


  &.success {
    cursor: pointer;
    color: var(--c-brand-light);

    &::before {
      transform: translate3d(-50%, -50%, 0) scale3d(150, 150, 150);
    }
  }
}
</style>
<style scoped>
svg {
  position: absolute;
  bottom: 8px;
  right: 8px;
  opacity: 0.75;
  cursor: pointer;
  z-index: 9999;
}

svg.hover {
  opacity: 0;
}

svg:hover {
  opacity: 1 !important;
}

span {
  position: absolute;
  font-size: 0.85rem;
  line-height: 1.2rem;
  right: 40px;
  bottom: 12px;
  opacity: 0;
  transition: opacity 500ms;
}

.success {
  opacity: 1 !important;
}

</style>
