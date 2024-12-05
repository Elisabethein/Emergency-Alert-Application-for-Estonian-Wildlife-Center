<template>
  <div class="application-management">
    <h1>Vabatahtlikeks registreerinud kasutajad</h1>

    <div class="user-list-container">

      <h3 v-if="users.length === 0">Hetkel pole uusi kasutajaid.</h3>

      <ul v-else>
        <li v-for="user in users" :key="user.id" class="user-list">
          <div class="user-name">
            <div class="user-info">
              {{ user.firstName }} {{ user.lastName }}
            </div>
            <div class="button-container">
              <button class="delete-button" @click="confirmDelete(user)">Kustuta</button>
              <button class="add-button" @click="confirmAdd(user)">Lisa</button>
            </div>
          </div>
          <div class="user-details">
            <p><strong>Aadress:</strong> {{ user.streetName }} {{ user.streetNr }}, {{user.postalCode }}, {{ user.city }}, {{ user.county }}</p>
            <p><strong>Telefon:</strong> {{ user.phoneNr }}</p>
            <p><strong>Email:</strong> {{ user.email }}</p>
            <p><strong>Sünnipäev:</strong> {{ formatDate(user.birthDate) }}</p>
            <p><strong>Miks soovite Eesti Metsloomaühinguga liituda?</strong> {{ user.question1 }}</p>
            <p><strong>Kas kuulud mõnda loomadega tegelevasse organisatsiooni?</strong> {{ user.question2 }}</p>
            <p><strong>Kuidas meid aidata sooviksid:</strong></p>

            <div class="user-tags-and-regions">
              <!-- Tags -->
              <div v-for="tag in user.tags" :key="tag.id" class="tag-or-region tag">
                {{ tag }}
              </div>
            </div>

          </div>
        </li>
      </ul>
    </div>

  </div>
</template>

<script>
export default {
  name: "ApplicationManagement",

  data() {
    return {
      users: [],
      tags: [],
    };
  },
  async created() {
    await Promise.all([
        this.fetchUsers(),
        this.fetchTags(),
    ]);
  },
  methods: {

    getAuthToken() {
      return localStorage.getItem('token');
    },
    handleUnauthorized() {
      if (this.alertShown) return;
      alert('Teie sessioon on aegunud, palun logige uuesti sisse!');
      this.$store.dispatch('logout');
      this.$router.push('/login');
      this.alertShown = true;
    },
    async fetchData(endpoint) {
      const token = this.getAuthToken();
      //const response = await fetch(`http://localhost:8080/api/${endpoint}`, {
      const response = await fetch(`api/${endpoint}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        }
      });
      if (!response.ok) {
        if (response.status === 403) {
          this.handleUnauthorized();
        } else {
          console.error(`Failed to fetch data`);
        }
        return null;
      }
      return response.json();
    },

    async fetchUsers() {
      const users = await this.fetchData('applications/all');
      if (!users) return;

      this.users = await Promise.all(users
          .filter(user => user.isAccepted === false)
          .map(async (user) => {
            const tags = await this.fetchData(`applications/${user.id}/tags`);
            return {
              ...user,
              tags: tags || [],
            };
          })
      );

    },
    formatDate(isoDate) {
      const date = new Date(isoDate);
      const day = date.getDate().toString().padStart(2, '0');
      const month = (date.getMonth() + 1).toString().padStart(2, '0');
      const year = date.getFullYear();
      return `${day}.${month}.${year}`;
    },
    async fetchTags() {
      const tags = await this.fetchData('tags/helpOptions');
      this.tags = tags ? tags.map(tag => tag.function) : [];
    },
    confirmDelete(user) {
      if (confirm(`Kas olete kindel, et soovite kasutaja ${user.firstName} ${user.lastName} kustutada?`)) {
        this.deleteUser(user);
      }
    },
    async deleteUser(user) {
      const token = this.getAuthToken();
      try {
        //const response = await fetch(`http://localhost:8080/api/applications/delete/${user.id}`, {
        const response = await fetch(`api/applications/delete/${user.id}`, {
          method: 'DELETE',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          }
        });

        if (response.ok) {
          this.users = this.users.filter(u => u.id !== user.id);
          alert(`${user.firstName} ${user.lastName} on kustutatud`);
        } else if (response.status === 403) {
          this.handleUnauthorized();
        } else {
          console.error("Kasutaja kustutamine ebaõnnestus.");
        }
      } catch (error) {
        console.error("Viga kasutaja kustutamisel:", error);
      }
    },

    confirmAdd(user) {
      if (confirm(`Kas olete kindel, et soovite kasutaja ${user.firstName} ${user.lastName} lisada?`)) {
        this.addUser(user);
      }
    },
    async addUser(user) {
      const token = this.getAuthToken();
      try {
        //const response = await fetch(`http://localhost:8080/api/users/add`, {
        const response = await fetch(`api/users/add`, {
          method: 'POST',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(user) // Pass the user data as the body
        });

        if (response.ok) {
          const newUser = await response.json();
          alert(`${newUser.firstName} ${newUser.lastName} on lisatud`);
          await this.fetchUsers();

        } else if (response.status === 403) {
          this.handleUnauthorized();
        } else if (response.status === 400) {
          alert('Sellise emailiga kasutaja on juba olemas!');
        } else {
          console.error("Kasutaja lisamine ebaõnnestus.");
        }
      } catch (error) {
        console.error("Viga kasutaja lisamisel:", error);
      }
    },
  },


};
</script>

<style scoped>
.application-management {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  padding: 20px;
  border-radius: 10px;

}

h1 {
  color: #070400;
  margin-bottom: 20px;
}

.user-details {
  font-size: 0.9em;
  margin: 5px 0;
  text-align: left;
}

.user-list-container {
  margin-bottom: 20px;
  width: 80%;
}

.user-list {
  background-color: #D6F4CD;
  padding: 20px;
  margin: 10px 0 20px;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  text-align: left;
}

.user-list-container ul {
  list-style-type: none;
}

.user-name {
  font-size: 1.5em;
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.user-tags-and-regions {
  display: flex;
  flex-wrap: wrap;
}

.tag-or-region {
  background-color: #F1F4F1;
  border-radius: 15px;
  padding: 10px 20px;
  margin: 5px;
  font-size: 1.1em;
  box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.15);
}

@media (max-width: 820px) {
  .user-name {
    font-size: 1em;
    font-weight: bolder;
  }
}

.button-container {
  display: flex;
  gap: 10px;
}

.add-button,
.delete-button {
  flex: 1;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 10px;
  font-size: 0.9em;
  cursor: pointer;
  transition: background-color 0.3s;
}

.add-button:hover {
  background-color: #29602c;
}

.delete-button {
  background-color: #f80a0a;
}

.delete-button:hover {
  background-color: rgb(157, 4, 4);
}
</style>
