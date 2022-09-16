<template>
  <!--  <div ref="installBtn"></div>-->
  <iframe height="64" width="288" style="border: none" srcdoc='
    <html>
    <head>
    </head>
    <body>
        <script src="//plugins.jetbrains.com/assets/scripts/mp-widget.js"></script>
        <script>
          setTimeout(()=> MarketplaceWidget.setupMarketplaceWidget("install", 17061, "#installBtn"),500);
        </script>
        <div id="installBtn" style="display:flex;justify-content:center;"></div>
    </body>
    </html>
  '></iframe>
</template>

<script>
export default {
  name: "InstallButton"
}
</script>

<style>
iframe {
  color-scheme: initial;
}
</style>