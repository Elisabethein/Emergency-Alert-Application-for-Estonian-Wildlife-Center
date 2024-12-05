<template>
  <div class="login-page">
    <div class="login-container">
      <h2>Logi sisse</h2>

      <!-- Form for user login -->
      <form @submit.prevent="handleLogin">
        <div class="form-field">
          <label for="email">Email</label>
          <input type="text" id="email" v-model="email" placeholder="Sisesta email" required />
        </div>

        <div class="form-field">
          <label for="password">Salasõna</label>
          <input type="password" id="password" v-model="password" placeholder="Sisesta salasõna" required />
        </div>

        <div class="button-container">
          <button type="submit" class="login-button">Logi sisse</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex';
export default {
  name: "LoginPage",
  data() {
    return {
      email: '',
      password: '',
    };
  },
  methods: {
    ...mapActions(['login']),

    async handleLogin() {
      try {
        //const response = await fetch('http://localhost:8080/api/login', {
        const response = await fetch('api/login', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            email: this.email,
            password: this.password,
          }),
        });
        const text = await response.text();

        if (response.ok) {
          const data = JSON.parse(text);

          const user = data.user;
          const roles = data.roles;
          const regions = data.regions;
          const token = data.token;

          await this.login({ user, roles, regions, token });

          this.$router.push('/my-cases');
        } else {
          alert('Kasutajanimi või salasõna on vale!');
          console.error('Login failed');
        }
      } catch (error) {
        alert('Kasutajanimi või salasõna on vale!');
        console.error('Login failed', error);
      }
    },
  },
};
</script>

<style scoped>
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-container {
  background-color: #C8ECBD;
  border-radius: 10px;
  padding: 20px 40px;
  width: 300px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

h2 {
  text-align: center;
  color: #2d2d2d;
}

.form-field {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 5px;
}

input {
  width: 100%;
  padding: 10px;
  border: none;
  border-radius: 5px;
  margin-top: 5px;
  font-size: 1em;
}

.button-container {
  text-align: right; /* Align button to the right */
}

button.login-button {
  background-color: #87D26E;
  border: none;
  border-radius: 7px;
  padding: 10px 20px;
  cursor: pointer;
  font-size: 1.1em;
  margin-top: 10px;
}

button.login-button:hover {
  background-color: #89d970;
}

.forgot-password {
  text-align: left;
  margin-bottom: 10px;
}

.forgot-password a {
  text-decoration: none;
  color: #5E89A8;
  font-size: 0.9em;
}

.forgot-password a:hover {
  text-decoration: underline;
}

</style>
