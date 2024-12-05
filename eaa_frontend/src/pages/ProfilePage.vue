<template>
  <div class="profile-page">
    <h1>Kasutaja Profiil</h1>

    <div v-if="user" class="profile-content">
      <!-- Left column: Contact Info -->
      <div class="left-column">
        <h3>Kontaktandmed:</h3>
        <div class="user-info" v-for="(label, key) in contactInfo" :key="key">
          <strong>{{ label }}: </strong>
          <span v-if="key === 'createdAt'">{{ formatDate(user.createdAt) }}</span>
          <span v-else-if="key === 'streetName'">
            {{ formatAddress(user.streetName, user.streetNr, user.postalCode) }}
          </span>
          <span v-else>{{ user[key] }}</span>
        </div>
      </div>

      <!-- Right column: Roles, Regions, etc. -->
      <div class="right-column">
        <!-- Display Roles -->
        <div class="user-tags-and-regions">
          <h3>Rollid:</h3>
          <div v-if="user.roles && user.roles.length">
            <div v-for="role in user.roles" :key="role.id" class="role">{{ role }}</div>
          </div>
          <div v-else>No roles available</div>
        </div>

        <!-- Display Regions -->
        <div class="user-tags-and-regions">
          <h3>Regioonid:</h3>
          <div v-if="user.regions && user.regions.length">
            <div v-for="region in user.regions" :key="region.id" class="region">{{ region }}</div>
          </div>
          <div v-else>No regions available</div>
        </div>

        <!-- Display Functions -->
        <div class="user-tags-and-regions">
          <h3>Funktsioonid:</h3>
          <div v-if="user.tags && user.tags.length">
            <div v-for="tag in user.tags" :key="tag.id" class="tag">{{ tag }}</div>
          </div>
          <div v-else>No functions available</div>
        </div>

        <!-- Display Species -->
        <div class="user-tags-and-regions">
          <h3>Liigigrupid:</h3>
          <div v-if="user.species && user.species.length">
            <div v-for="species in user.species" :key="species.id" class="species">
              <span v-if="species.expert" class="expert-icon">⭐ Ekspert: </span>
              <span>{{ species.name }}</span>
            </div>
          </div>
          <div v-else>No species available</div>
        </div>
      </div>
    </div>



    <div class="button-container">
      <button @click.stop.prevent="editUser()">Muuda Profiili</button>
    </div>
  </div>

  <!-- EditUserComponent Popup -->
  <EditUser
      v-if="isEditing"
      :user="user"
      :token="getAuthToken()"
      @update-user="handleUserUpdate"
      @delete-user="handleUserDelete"
      @close-popup="isEditing = false"
  />
</template>

<script>
import EditUser from "@/components/EditProfileComponent.vue";

export default {
  name : 'ProfilePage',
  components: {
    EditUser
  },
  data() {
    return {
      regions: [],
      roles: [],
      searchTerm: '',
      isEditing: false,
      tags: [],
      species: [],
      user: null,
      token: null,
      isEditPopupOpen: false,
    };
  },
  async created() {
    this.user = this.$store.state.user;
    this.token = this.$store.state.token;

    await this.fetchUser();
  },
  computed: {
    contactInfo() {
      return {
        firstName: "Eesnimi",
        lastName: "Perekonnanimi",
        birthDate: "Sünnikuupäev",
        createdAt: "Liitumiskuupäev",
        email: "Email",
        phoneNr: "Telefon",
        county: "Maakond",
        city: "Linn",
        streetName: "Aadress",
      };
    }
  },
  methods: {
    getAuthToken() {
      return localStorage.getItem('token');
    },
    async fetchData(endpoint) {
      const token = this.getAuthToken();
      //const response = await fetch(`http://localhost:8080/api/${endpoint}`, {
      const response = await fetch(`api/${endpoint}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
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
    editUser() {
      this.isEditPopupOpen = true;
      this.isEditing = true;
      this.isViewing = false;
    },
    handleUserUpdate(updatedUser) {
      this.isEditing = false;
      location.reload();
      alert('Kasutaja ' + updatedUser.firstName + ' ' + updatedUser.lastName + ' on edukalt muudetud!');
      this.$store.commit('setUser', updatedUser);
    },
    handleUserDelete(deletedUser) {
      this.isEditing = false;
      location.reload();
      alert('Kasutaja ' + deletedUser.firstName + ' ' + deletedUser.lastName + ' on edukalt kustutatud!');
    },
    async fetchUser() {
      const currentUser = this.$store.state.user; // Assuming user data is stored in the store

      // Fetch roles, tags, regions, and species for the current user
      const [roles, tags, regions, species] = await Promise.all([
        this.fetchData(`users/${currentUser.id}/roles`),
        this.fetchData(`users/${currentUser.id}/tags`),
        this.fetchData(`users/${currentUser.id}/regions`),
        this.fetchData(`users/${currentUser.id}/species`)
      ]);

      // Merge fetched data into user object
      this.user = {
        ...currentUser,
        roles: roles || [],
        tags: tags || [],
        regions: regions || [],
        species: species || []
      };
    },
    formatAddress(streetName, streetNr, postalCode) {
      return `${streetName} ${streetNr}, ${postalCode}`;
    },

    formatDate(dateString) {
      const date = new Date(dateString);
      const day = String(date.getDate()).padStart(2, '0');
      const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are zero-based
      const year = date.getFullYear();
      return `${day}.${month}.${year}`;
    }
  },
};
</script>

<style scoped>
  .profile-page {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20px;
  }

  .profile-content {
    display: flex;
    justify-content: space-between;
    width: 80%; /* Adjust width to your needs */
  }

  .left-column {
    width: 45%; /* Adjust the width of the left column */
    background-color: #C8ECBD;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  }

  .right-column {
    width: 45%; /* Adjust the width of the right column */
    background-color: #C8ECBD;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  }

  .user-info {
    margin-bottom: 20px;
  }

  .user-tags-and-regions {
    margin-top: 20px;
  }

  .role, .region, .tag, .species {
    background-color: #fff;
    padding: 10px;
    margin: 5px;
    border-radius: 5px;
    display: inline-block;
  }


  .expert-icon {
    margin-right: 5px;
  }

  .button-container {
    margin-top: 20px;
  }

  button {
    background-color: #87D26E;
    border: none;
    padding: 10px 20px;
    border-radius: 5px;
    cursor: pointer;
    font-size: 1.1em;
  }

  button:hover {
    background-color: #89d970;
  }
  @media (max-width: 820px) {
    .profile-content {
      flex-direction: column;
      align-items: center;
    }

    .left-column,
    .right-column {
      width: 100%;
      margin-bottom: 20px;
    }
  }
</style>