const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  /*devServer: {
    proxy: {
      '/api': {
        target: 'http://193.40.11.165:8080',  // Replace with your backend addr>
        changeOrigin: true,               // This is needed for cross-origin re>
        secure: false,                    // Set to false if using HTTP instead>
      }
    },
    // Optionally specify a port for Vue dev server (if you need to run it on a>
    port: 8081
  }*/
})
