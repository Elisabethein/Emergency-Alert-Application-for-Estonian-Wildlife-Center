<template>
  <nav class="navbar">
    <ul v-if="isLoggedIn">
      <li :class="{ active: $route.name === 'TicketManagement' }">
        <router-link to="/ticket-management">Juhtumid</router-link>
      </li>
      <li :class="{ active: $route.name === 'MyCases' }">
        <router-link to="/my-cases">Minu Juhtumid</router-link>
      </li>
      <li :class="{ active: $route.name === 'KnowledgeBase' }">
        <router-link to="/knowledge-base">Materjalid</router-link>
      </li>
      <li :class="{ active: $route.name === 'Statistics' }">
        <router-link to="/statistics">Statistika</router-link>
      </li>
      <li :class="{ active: $route.name === 'UserManagement' }">
        <router-link to="/user-management">Olemasolevad kasutajad</router-link>
      </li>
      <li :class="{ active: $route.name === 'ApplicationManagement' }" v-if="hasJuhtkondRole || hasPiirkonnajuhtRole || hasPäevajuhtRole">
        <router-link to="/application-management">Uued kasutajad</router-link>
      </li>
      <li :class="{ active: $route.name === 'Settings' }" v-if="hasJuhtkondRole">
        <router-link to="/settings">Seaded</router-link>
      </li>
    </ul>
    <ul v-else>
      <li :class="{ active: $route.name === 'WelcomePage' }">
        <router-link to="/">Esileht</router-link>
      </li>
      <li :class="{ active: $route.name === 'Alert' }">
        <router-link to="/alert">Teavita juhtunust</router-link>
      </li>
      <li :class="{ active: $route.name === 'Login' }">
        <router-link to="/login">Logi sisse</router-link>
      </li>
      <li :class="{ active: $route.name === 'Register' }">
        <router-link to="/register">Registreeru</router-link>
      </li>
    </ul>
  </nav>
</template>

<script>
export default {
  name: "NavigationBar",
  computed: {
    // Get the loggedIn state from Vuex
    isLoggedIn() {
      return this.$store.getters.isLoggedIn;
    },
    hasJuhtkondRole() {
      const roles = this.$store.getters.getRoles;
      return roles.includes('Juhtkond');
    },
    hasPäevajuhtRole() {
      const roles = this.$store.getters.getRoles;
      return roles.includes('Päevajuht');
    },
    hasPiirkonnajuhtRole() {
      const roles = this.$store.getters.getRoles;
      return roles.includes('Piirkonnajuht');
    },
  },
};
</script>

<style scoped>
.navbar {
  width: 87%;
  margin: 10px auto 30px;
  background-color: rgba(135, 210, 110, 0.44);
  border-radius: 15px;
  padding: 10px 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
@media (max-width: 820px) {
  .navbar ul {
    flex-direction: column;
    gap: 10px;
  }
}
@media (max-width: 1024px) {
  .navbar ul {
    flex-direction: column;
    gap: 5px;
  }
}
.navbar ul {
  list-style-type: none;
  display: flex;
  justify-content: center;
  gap: 20px;
  padding: 0;
  margin: 0;
  height: 100%;
  align-items: center;
}

.navbar li {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 10px 20px;
  font-family: 'Inria Serif', serif;
  font-size: 1.1em;
  border-radius: 15px;
  height: 100%;
  z-index: 1;
  text-align: center;
}

.navbar li.active {
  font-weight: bold;
  background-color: #87D26E;
  border-radius: 15px;
  transition: background-color 0.5s;
}

a {
  text-decoration: none;
  color: inherit;
}

</style>
