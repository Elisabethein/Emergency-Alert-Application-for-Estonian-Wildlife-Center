<template>
  <div class="user-management">
    <h1>Olemasolevate kasutajate ülevaade</h1>

    <div class="filters-section">
      <div class="filters">
        <div class="filter-item">
        <label for="regions">Vali regioon</label>
        <multiselect
            v-model="selectedRegions"
            :options="regions"
            :multiple="true"
            placeholder="Vali regioon"
            @input="filterUsers"
        >
        </multiselect>
      </div>
        <div class="filter-item">
        <label for="regions">Vali funkstisoon</label>
        <multiselect
            v-model="selectedTags"
            :options="tags"
            :multiple="true"
            placeholder="Vali funktsioon"
            @input="filterUsers"
        >
        </multiselect>
        </div>
          <div class="filter-item">

        <label for="regions">Vali liigigrupp</label>
        <multiselect
            v-model="selectedSpecies"
            :options="species"
            :multiple="true"
            placeholder="Vali liigigrupp"
            @input="filterUsers"
        >
        </multiselect>
          </div>
      </div>

      <div class="search">
        <input
            type="text"
            v-model="searchTerm"
            placeholder="Otsi nime järgi..."
            @input="filterUsers"
        />
      </div>
    </div>

    <EditUser
        v-if="isEditing && (hasJuhtkondRole || hasPiirkonnajuhtRole)"
        :user="selectedUser"
        :token="getAuthToken()"
        @update-user="handleUserUpdate"
        @delete-user="handleUserDelete"
        @close-popup="isEditing = false"
    />

    <ViewUser
        v-if="isViewing && (hasJuhtkondRole || hasPiirkonnajuhtRole || hasPäevajuhtRole)"
        :user="selectedUser"
        @close-popup="isViewing = false"
    />

    <div class="user-list">

      <div v-if="filteredUsers.length === 0" class="no-users-message">
        <h2>Kasutajaid ei leitud</h2>
      </div>
      <div
          v-else
          v-for="user in filteredUsers"
          :key="user.id"
          :class="['user-card', { 'hover-enabled': hasJuhtkondRole || hasPiirkonnajuhtRole || hasPäevajuhtRole }]"
          @click.prevent="openViewUser(user)"
      >
        <div class="edit-link" v-if="hasJuhtkondRole || hasPiirkonnajuhtRole">
          <a href="#" @click.stop.prevent="editUser(user)">Muuda</a>
        </div>

        <div class="user-info">
          <div class="user-name-role">
            <div class="user-name">
              {{ user.firstName }} {{ user.lastName }}
            </div>
            <div class="user-role">
              {{ user.roles.join(', ') }}
            </div>
          </div>
          <div class="user-details">
            <p><strong>Asukoht:</strong> {{ user.county }}, {{ user.city }}</p>
            <p><strong>Telefon:</strong> {{ user.phoneNr }}</p>
            <p><strong>Email:</strong> {{ user.email }}</p>
            <p><strong>Liitunud:</strong> {{ new Date(user.createdAt).toLocaleDateString() }}</p>
          </div>

          <div class="user-tags-and-regions">
            <!-- Tags -->
            <div v-for="tag in user.tags" :key="tag.id" class="tag-or-region tag">
              {{ tag }}
            </div>
            <!-- Regions -->
            <div v-for="region in user.regions" :key="region.id" class="tag-or-region region">
              {{ region }}
            </div>
            <!-- Species -->
            <div v-for="species in user.species" :key="species.id" class="tag-or-region species" :class="{'expert-tag': species.expert}">
              <span v-if="species.expert" class="expert-icon">⭐ Ekspert: </span>
              <span>{{ species.name }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>



<script>
import EditUser from "@/components/EditUserComponent.vue";
import ViewUser from "@/components/ViewUserComponent.vue";
import Multiselect from 'vue-multiselect';
import 'vue-multiselect/dist/vue-multiselect.min.css';

export default {
  name: "UserManagement",
  components: {
    Multiselect,
    EditUser,
    ViewUser
  },
  data() {
    return {
      regions: [],
      selectedRegions: [],
      users: [],
      filteredUsers: [],
      searchTerm: '',
      isEditing: false,
      isViewing: false,
      selectedUser: null,
      selectedTags: [],
      selectedSpecies: [],
      tags: [],
      species: [],
      alertShown: false,
    };
  },
  computed: {
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
  async created() {
    await Promise.all([
      this.fetchRegions(),
      this.fetchUsers(),
      this.fetchTags(),
      this.fetchSpecies(),
    ]);
  },
  watch: {
    selectedRegions: 'filterUsers',
    searchTerm: 'filterUsers',
    selectedTags: 'filterUsers',
    selectedSpecies: 'filterUsers',
  },
  methods: {
    getAuthToken() {
      return localStorage.getItem('token'); // Get the token from local storage
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
    async fetchRegions() {
      const allRegions = await this.fetchData('regions/all');
      this.regions = this.hasJuhtkondRole ? allRegions : allRegions.filter(region => this.$store.getters.getRegions.includes(region));
    },
    async fetchTags() {
      const tags = await this.fetchData('tags/helpOptions');
      this.tags = tags ? tags.map(tag => tag.function) : [];
    },
    async fetchSpecies() {
      const species = await this.fetchData('upperSpecies/all');
      this.species = species ? species.map(species => species.name) : [];
    },
    async fetchUsers() {
      const users = await this.fetchData('users/all');
      if (!users) return;

      this.users = await Promise.all(users.map(async (user) => {
        const roles = await this.fetchData(`users/${user.id}/roles`);
        const tags = await this.fetchData(`users/${user.id}/tags`);
        const regions = await this.fetchData(`users/${user.id}/regions`);
        const species = await this.fetchData(`users/${user.id}/species`);

        return {
          ...user,
          roles: roles || [],
          tags: tags || [],
          regions: regions || [],
          species: species || []
        };
      }));

      this.filterUsers();
    },
    filterUsers() {
      this.filteredUsers = this.users.filter(user => {
        const userRegions = this.hasJuhtkondRole ? [] : this.$store.getters.getRegions; // Retrieve user regions if not "Juhtkond"

        const regionMatch = this.hasJuhtkondRole
            || (this.selectedRegions.length === 0
                ? userRegions.some(region => user.regions.includes(region)) // Check if user belongs to any of the user's regions
                : user.regions.some(region => this.selectedRegions.includes(region))); // Match selected regions if not empty

        const tagMatch = this.selectedTags.length === 0 || user.tags.some(tag => this.selectedTags.includes(tag));
        const speciesMatch = this.selectedSpecies.length === 0 || user.species.some(species => this.selectedSpecies.includes(species.name));
        const nameMatch = this.searchTerm.trim() === '' ||
            user.firstName.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
            user.lastName.toLowerCase().includes(this.searchTerm.toLowerCase());

        return regionMatch && nameMatch && tagMatch && speciesMatch;
      });
    },
    editUser(user) {
      this.selectedUser = { ...user };
      this.isEditing = true;
      this.isViewing = false;
    },

    openViewUser(user) {
      this.selectedUser = user;
      this.isViewing = true;
      this.isEditing = false;
    },

    handleUserUpdate(updatedUser) {
      this.isEditing = false;
      location.reload();
      alert('Kasutaja ' + updatedUser.firstName + ' ' + updatedUser.lastName + ' on edukalt muudetud!');
    },
    handleUserDelete(deletedUser) {
      this.isEditing = false;
      location.reload();
      alert('Kasutaja ' + deletedUser.firstName + ' ' + deletedUser.lastName + ' on edukalt kustutatud!');
    },
  },
};
</script>


<style scoped>
.user-management {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  text-align: center;
}

.filters-section {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  width: 80%;
  margin-bottom: 20px;
}

.filters {
  display: flex;
  flex-direction: row;
  align-items: center;
  flex: 1;
  margin-bottom: 20px;
  width: 100%; /* Increase the width of the filters container */
}

.filter-item {
  flex: 1;
  padding-right: 20px;
  min-width: 30%
}

.filters-item multiselect {
  flex: 2;
  width: 100%;
}

.search {
  position: relative;
  width: 100%;
  max-width: 400px; /* Suurema laiuse piiramine */
  margin-bottom: 20px;
  border-radius: 15px;
  background-color: #D9D9D9;
  padding: 15px 15px;
  display: flex;
  align-items: center;
}

.search input {
  flex: 1;
  border: none;
  background-color: #D9D9D9;
  border-radius: 15px;
  font-size: 1em;
}
.search input:focus {
  outline: none; /* Eemaldab kõik fokuseerituse äärised */
}

.user-list {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}

.user-card {
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  background-color: #D6F4CD;
  padding: 20px;
  margin: 10px 0;
  border-radius: 10px;
  width: 80%;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  position: relative;
}

.hover-enabled:hover {
  background-color: #b5e1aa;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
}

.user-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: flex-start;
  margin-right: 20px;
}

.user-name-role {
  display: flex;
  margin-bottom: 10px;
}

.user-name {
  font-size: 1.5em;
}

.user-role {
  font-size: 1.5em;
  margin-left: 20px;
  color: green;
  font-weight: bold;
}

.user-details {
  font-size: 0.9em;
  margin: 5px 0;
  text-align: left;
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

.expert-tag {
  background-color: #efe291;
  font-weight: bold;
}

.expert-icon {
  margin-right: 10px;
}


.edit-link {
  position: absolute;
  top: 10px;
  right: 20px;
}

.edit-link a {
  color: #75A9CF;
  text-decoration: none;
  font-weight: bold;
}

.edit-link a:hover {
  text-decoration: underline;
}

@media (max-width: 820px) {
  .filters {
    flex-direction: column;
    align-items: stretch;
  }
  .user-name {
    font-size: 1em;
    font-weight: bolder;
  }
  .user-role {
    font-size: 1em;
    font-weight: bolder;
    margin-left: 5px;
  }
}

</style>
