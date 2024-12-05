<template>
  <div id="app">
    <HeaderComponent />
    <div class="pt-32"></div>

    <NavigationBar/>

    <main class="content">
      <router-view></router-view>
    </main>

    <FooterComponent />
  </div>
</template>

<script>
import HeaderComponent from './components/HeaderComponent.vue';
import NavigationBar from './components/NavigationBar.vue';
import FooterComponent from './components/FooterComponent.vue';

export default {
  name: 'App',
  components: {
    HeaderComponent,
    NavigationBar,
    FooterComponent
  },
  computed: {
    isLoggedIn() {
      return this.$store.getters.isLoggedIn;
    }
  },
  created() {
    this.$store.dispatch('loadUser');
  },
}
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Inria+Serif&display=swap');

#app {
  background-color: #FBFFFB;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  margin: 0;
  font-family: 'Inria Serif', serif;
}
.content {
  flex: 1;
}
</style>
