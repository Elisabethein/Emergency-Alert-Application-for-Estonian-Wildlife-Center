<template>
  <body>
  <header class="header">
    <div class="logo-container">
      <img src="../assets/logo.png" alt="Logo" class="logo" />
    </div>

    <div v-if="isLoggedIn" class="user-menu">
      <div class="user-profile-menu">
        <router-link to="/profile" class="profile-button">Profiil</router-link>
        <button class="logout-button" @click="logOut">Logi välja</button>
      </div>
    </div>
  </header>
  </body>
</template>

<script>
export default {
  name: "HeaderComponent",
  data() {
    return {
      menuOpen: false,
    };
  },
  computed: {
    isLoggedIn() {
      return this.$store.getters.isLoggedIn;
    },
  },
  methods: {
    logOut() {
      this.$store.dispatch('logout');
      this.$router.push('/');
    },
  },
};
</script>

<style scoped>
body {
  margin: 0;
  display: flex;
  justify-content: center;
  align-items: center;
}

.header {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
  position: relative;
  width: 87%;
  flex-direction: row;
}

.logo-container {
  display: flex;
  justify-content: center;
  align-items: center;
}

.logo {
  width: 200px;
  height: auto;
}

.user-menu {
  display: flex;
  align-items: center;
  position: absolute;
  right: 0;
  top: 50%;
}

.user-profile-menu {
  display: flex;
  justify-content: flex-end;
}

.profile-button,
.logout-button {
  background-color: rgba(135, 210, 110, 0.8);
  border: none;
  border-radius: 10px;
  padding: 10px 20px;
  font-family: 'Inria Serif', serif;
  font-size: 1em;
  cursor: pointer;
  margin-left: 10px;
  transition: background-color 0.3s;
  text-decoration: none;
  color: black;
}

.profile-button:hover,
.logout-button:hover {
  background-color: #87D26E;
}

.logout-button {
  background-color: rgba(135, 210, 110, 0.8);
  border: none;
  border-radius: 10px;
  padding: 10px 20px;
  font-family: 'Inria Serif', serif;
  font-size: 1em;
  cursor: pointer;
  margin-left: 10px;
  transition: background-color 0.3s;
}

.logout-button:hover {
  background-color: #87D26E;
}

@media (max-width: 768px) {
  .header {
    flex-direction: column;
    align-items: center;
    gap: 10px;
    margin: 10px;
    height: auto;
  }
  .user-menu{
    position: relative;
  }
}
</style>
