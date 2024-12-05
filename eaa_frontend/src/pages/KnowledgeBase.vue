<template>
  <div class="knowledge-base">
    <h1>Materjalid</h1>

    <div class="filters-section">
      <div class="filters">
        <label for="regions">Vali liigigrupp: </label>
        <multiselect
            v-model="selectedSpecies"
            :options="upperSpeciesList"
            :multiple="true"
            placeholder="Vali liigigrupp"
            @input="filterMaterials"
        >
        </multiselect>
      </div>
    </div>

    <div class="filters-section">
      <div class="filters">
        <label for="material-type">Vali materjal:</label>
        <select v-model="selectedMaterialType" id="material-type" class="custom-dropdown">
          <option value="all">Kõik materjalid</option>
          <option value="general">Üldine materjal</option>
        </select>
      </div>
    </div>

    <div class="search">
      <input
          type="text"
          v-model="searchTerm"
          placeholder="Otsi pealkirja või selgitava teksti järgi..."
          @input="filterMaterials"
      />
    </div>
  </div>

  <div class="add-materials">
    <div v-if="hasJuhtkondOrPiirkonnajuhtRole" class="section">
      <h3>Lisa materjale</h3>
      <div class="form-field">
        <label for="material-title">Materjali pealkiri:</label>
        <input
            type="text"
            id="material-title"
            v-model="materialTitle"
            placeholder="Sisesta materjali pealkiri"
        />
      </div>
      <div class="form-field">
        <label for="material-link">Materjali link:</label>
        <input
            type="text"
            id="material-link"
            v-model="materialLink"
            placeholder="Sisesta link algusega http:// või https://"
        />
      </div>
      <div class="form-field">
        <label for="description-input">Selgitav tekst:</label>
        <input
            type="text"
            id="description-input"
            v-model="newDescriptionInput"
            placeholder="Sisesta tekst"
            maxlength="250"
        />
        <span>{{ 250 - newDescriptionInput.length }} sümbolit jäänud</span>
      </div>
      <div class="form-field" v-if="upperSpeciesList.length > 0">
        <label for="species-group">Vali liigigrupp:</label>
        <select v-model="selectedSpeciesGroup" id="species-group" class="species-select">
          <option v-for="(group, index) in upperSpeciesList" :key="index" :value="group">
            {{ group }}
          </option>
        </select>
      </div>
      <div class="form-field">
        <label>
          Üldine materjal:
          <input type="checkbox" v-model="isGeneralMaterial"/>
        </label>
      </div>
      <div class="button-container">
        <button @click="handleAddMaterial">Lisa Materjal</button>
      </div>
    </div>
  </div>
  <div class="card-container">
    <div v-if="filteredMaterials.length === 0">
      <h3>Materjale ei ole hetkel saadaval.</h3>
    </div>
    <div class="card" v-for="(material, index) in filteredMaterials" :key="index">
      <div class="button-group" v-if="hasJuhtkondOrPiirkonnajuhtRole">
        <button class="action-button" @click="editMaterial(material)">Muuda</button>
        <button class="action-button" @click="deleteMaterial(material)">Kustuta</button>
      </div>
      <a :href="material.link" target="_blank" class="card-link">
        <h2>{{ material.title }}</h2>
        <p>{{ material.description }}</p>
      </a>
      <div class="tag-container">
        <p v-if="material.generalMaterial" class="general-label">Üldine materjal</p>
        <p v-if="material.selectedSpeciesGroup" class="general-label">{{ material.selectedSpeciesGroup }}</p>
      </div>
    </div>
  </div>

  <!-- Muuda materjali koht -->
  <div v-if="showEditPopup" class="popup-overlay">
    <div class="popup-content">
      <h3>Muuda Materjali</h3>
      <div class="form-field">
        <label for="edit-title">Materjali pealkiri:</label>
        <input type="text" v-model="editMaterialTitle" id="edit-title"/>
      </div>
      <div class="form-field">
        <label for="edit-link">Materjali link:</label>
        <input type="text" v-model="editMaterialLink" id="edit-link"/>
      </div>
      <div class="form-field">
        <label for="edit-description">Selgitav tekst:</label>
        <input type="text" v-model="editMaterialDescription" id="edit-description"/>
      </div>
      <div class="form-field">
        <label for="edit-general">Üldine materjal:</label>
        <input type="checkbox" v-model="editGeneralMaterial" id="edit-general"/>
      </div>
      <div class="form-field" v-if="upperSpeciesList.length > 0">
        <label for="species-group">Vali liigigrupp:</label>
        <select v-model="editselectedSpeciesGroup" id="species-group" class="species-select">
          <option v-for="(group, index) in upperSpeciesList" :key="index" :value="group">
            {{ group }}
          </option>
        </select>
      </div>
      <div class="button-container2">
        <button @click="saveEdit">Salvesta</button>
        <button @click="closeEditPopup">Tühista</button>
      </div>
    </div>
  </div>


</template>

<script>
import Multiselect from "vue-multiselect";

export default {
  name: "KnowledgeBase",
  components: {Multiselect},
  data() {
    return {
      searchTerm: "",
      materials: [],
      materialTitle: '',
      materialLink: '',
      newDescriptionInput: '',
      isGeneralMaterial: false,
      selectedSpeciesGroup: '',
      upperSpeciesList: [],
      filteredMaterials: [],
      selectedSpecies: [],
      selectedMaterials: [],
      selectedMaterialType: 'all',
      showEditPopup: false,
      editMaterialId: null,
      editMaterialTitle: '',
      editMaterialLink: '',
      editMaterialDescription: '',
      editGeneralMaterial: false,
      editselectedSpeciesGroup: '',
      alertShown: false,
      user: {
        id: null,
        species: []
      },
    };
  },
  created() {
    this.fetchUser().then(() => {
      this.fetchMaterials();
      this.fetchSpeciesGroups();
    });
  },
  computed: {
    hasJuhtkondOrPiirkonnajuhtRole() {
      const roles = this.$store.getters.getRoles;
      return roles.includes('Juhtkond') || roles.includes('Piirkonnajuht');
    },
  },
  watch: {
    selectedSpecies: "filterMaterials",
    selectedMaterialType: "filterMaterials",
  },
  methods: {
    getAuthToken() {
      return localStorage.getItem('token'); // Example using local storage
    },
    handleUnauthorized() {
      if (this.alertShown) return;
      alert('Teie sessioon on aegunud, palun logige uuesti sisse!');
      this.$store.dispatch('logout');
      this.$router.push('/login');
      this.alertShown = true;
    },
    async handleAddMaterial() {
      // Regulaaravaldis, et kontrollida, kas sisestatud tekst on link
      const linkRegex = /^(ftp|http|https):\/\/[^ "]+$/;

      // Kontrollige, kas pealkiri ja link on sisestatud
      if (this.materialTitle && this.materialLink && this.newDescriptionInput) {
        // Kontrollige, kas link vastab regulaaravaldistele
        if (!linkRegex.test(this.materialLink)) {
          alert('Palun sisestage kehtiv link.');
          return;
        }

        const token = this.getAuthToken();
        try {
          //const response = await fetch('http://localhost:8080/api/materials/add', {
          const response = await fetch('api/materials/add', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({
              title: this.materialTitle,
              link: this.materialLink,
              description: this.newDescriptionInput,
              generalMaterial: this.isGeneralMaterial,
              selectedSpeciesGroup: this.selectedSpeciesGroup,
            })
          });
          if (response.ok) {
            this.materialTitle = '';
            this.materialLink = '';
            this.newDescriptionInput = '';
            this.isGeneralMaterial = false;
            this.selectedSpeciesGroup = '';
            alert('Materjal edukalt lisatud!');
            await this.fetchMaterials();
          } else if (response.status === 403) {
            this.handleUnauthorized();
          }
        } catch (error) {
          console.error('Failed to add material:', error);
        }
      } else {
        alert('Palun sisestage nii materjali pealkiri kui ka link.');
      }
    },

    async fetchMaterials() {
      const token = this.getAuthToken();
      try {
        //const response = await fetch('http://localhost:8080/api/materials/all', {
        const response = await fetch('api/materials/all', {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          }
        });
        if (response.ok) {
          const data = await response.json();
          //console.log("Materials fetched:", data);
          this.materials = data;
          this.filteredMaterials = data;
        } else if (response.status === 403) {
          this.handleUnauthorized();
        } else {
          console.error('Failed to fetch materials:', response.status);
        }
      } catch (error) {
        console.error('Error fetching materials:', error);
      }
      this.filterMaterials();
    },

    async fetchSpeciesGroups() {
      try {
        //const response = await fetch('http://localhost:8080/api/upperSpecies/all');
        const response = await fetch('api/upperSpecies/all');
        if (response.ok) {
          const data = await response.json();
          this.upperSpeciesList = data.map(group => group.name);
        } else if (response.status === 403) {
          this.handleUnauthorized();
        } else {
          console.error('Failed to fetch species groups:', response.status);
        }
      } catch (error) {
        console.error('Error fetching species groups:', error);
      }
    },
    async deleteMaterial(material) {
      // Küsi kasutajalt kinnitust
      const confirmation = confirm(`Kas soovite tõepoolest kustutada materjali: "${material.title}"?`);

      if (confirmation) {
        const token = this.getAuthToken();
        try {
          //const response = await fetch(`http://localhost:8080/api/materials/delete/${material.id}`, {
          const response = await fetch(`api/materials/delete/${material.id}`, {
            method: 'DELETE',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${token}`
            }
          });
          if (response.ok) {
            alert('Materjal edukalt kustutatud!');
            await this.fetchMaterials();
          } else {
            console.error('Kustutamine ebaõnnestus:', response.status);
          }
        } catch (error) {
          console.error('Viga materjali kustutamisel:', error);
        }
      }
    },
    // Avab hüpikakna ja täidab vormi andmetega
    editMaterial(material) {
      this.showEditPopup = true;
      this.editMaterialId = material.id;
      this.editMaterialTitle = material.title;
      this.editMaterialLink = material.link;
      this.editMaterialDescription = material.description;
      this.editGeneralMaterial = material.generalMaterial;
      this.editselectedSpeciesGroup = material.selectedSpeciesGroup;
    },
    // Sulgeb hüpikakna
    closeEditPopup() {
      this.showEditPopup = false;
    },
    // Salvestab muudatused ja saadab serverisse
    async saveEdit() {
      const token = this.getAuthToken();
      try {
        //const response = await fetch(`http://localhost:8080/api/materials/update/${this.editMaterialId}`, {
        const response = await fetch(`api/materials/update/${this.editMaterialId}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          },
          body: JSON.stringify({
            title: this.editMaterialTitle,
            link: this.editMaterialLink,
            description: this.editMaterialDescription,
            generalMaterial: this.editGeneralMaterial,
            selectedSpeciesGroup: this.editselectedSpeciesGroup,
          })
        });
        if (response.ok) {
          alert('Materjal edukalt uuendatud!');
          await this.fetchMaterials();
          this.closeEditPopup();
        } else {
          console.error('Uuendamine ebaõnnestus:', response.status);
        }
      } catch (error) {
        console.error('Viga materjali uuendamisel:', error);
      }
    },


    filterMaterialsForSearch() {
      // Filtreerime ainult nende materjalide põhjal, mis on juba kasutaja grupi järgi filtreeritud
      this.filteredMaterials = this.filteredMaterials.filter(material => {
        const matchesSpeciesGroup = this.selectedSpecies.length === 0 || this.selectedSpecies.some(group => material.selectedSpeciesGroup === group);
        const matchesSearchTerm = material.title.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
            material.description.toLowerCase().includes(this.searchTerm.toLowerCase());

        const matchesMaterialType =
            this.selectedMaterialType === 'all' ||
            (this.selectedMaterialType === 'general' && material.generalMaterial === true);

        return matchesSpeciesGroup && matchesSearchTerm && matchesMaterialType;
      });
    },

    filterMaterialsForUser() { // Leiab kasutajale ainult üldised või tema liigigrupi materjalid
      this.filteredMaterials = this.materials.filter(material => {

        const matchesUserSpeciesGroup = material.generalMaterial === true ||
            this.user.species.some(userGroup => {
              return String(material.selectedSpeciesGroup) === String(userGroup.name);
            });

        return matchesUserSpeciesGroup;
      });
    },

    filterMaterials() {
      // Kui kasutaja ei ole juhtkonnas, siis rakendame kasutaja liigigruppide filtri
      if (!this.hasJuhtkondOrPiirkonnajuhtRole) {
        this.filterMaterialsForUser();
      }

      // Seejärel rakendame otsingutermini ja materjalitüübi filtri
      this.filterMaterialsForSearch();
    },


    async fetchUser() {
      const currentUser = this.$store.state.user; // Assuming user data is stored in the store

      // Fetch species for the current user
      const [species] = await Promise.all([
        this.fetchData(`users/${currentUser.id}/species`)
      ]);

      // Merge fetched data into user object
      this.user = {
        ...currentUser,
        species: species || []
      };
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

  },
};
</script>

<style scoped>
.knowledge-base {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
}

input[type="text"] {
  padding: 10px;
  width: 80%;
  max-width: 600px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 18px;
}


.form-field,
.button-container {
  margin-bottom: 5px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.search {
  position: relative;
  width: 100%;
  max-width: 600px;
  margin-bottom: 20px;
  border-radius: 15px;
  background-color: #d9d9d9;
  padding: 15px 15px;
  display: flex;
  align-items: center;
}

.search input {
  flex: 1;
  border: none;
  background-color: #d9d9d9;
  border-radius: 15px;
  font-size: 1em;
}

.search button {
  background-color: #4CAF50;
  border: none;
  color: white;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
}

.search input:focus {
  outline: none;
}

.section {
  background-color: #D6F4CD;
  border-radius: 10px;
  padding: 15px;
  margin: 10px;
  width: 25%;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
}

.add-materials {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
}

.card-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 30px;
}

.card {
  background-color: #d3d3d3;
  border-radius: 10px;
  width: 900px;
  padding: 20px;
  margin: 10px 0;
  text-align: left;
  word-wrap: break-word; /* Allow long words to break and wrap to the next line */
  overflow-wrap: break-word; /* Ensure browser compatibility */
  transition: background-color 0.3s;
  position: relative;
}

.card:hover {
  background-color: #b0b0b0;
}

.card-link {
  text-decoration: none;
  color: inherit;
  display: block;
  height: 100%;
}

.button-container button {
  background-color: #4CAF50;
  border: none;
  color: white;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1em;
}

.button-container2 button:hover,
.search button:hover,
.button-container button:hover {
  background-color: #45a049;
}

.button-container2 {
  display: flex;
  justify-content: center;
  margin-top: 10px;
}

.button-container2 button {
  background-color: #4CAF50;
  border: none;
  color: white;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1em;
  margin: 0 10px;
}

.species-select {
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 5px;
  width: 100%;
  max-width: 400px;
  margin-top: 10px;
}

.form-field {
  margin-bottom: 15px;
}

.button-group {
  display: flex;
  gap: 10px;
  position: absolute;
  top: 10px;
  right: 10px;
}

.action-button {
  background-color: white;
  color: black;
  border: none;
  padding: 5px 10px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 0.9em;
}

.action-button:hover {
  background-color: #e6f7e3;
}

.tag-container {
  display: flex;
  flex-wrap: wrap;
}

.general-label {
  background-color: #F1F4F1;
  border-radius: 15px;
  padding: 10px 20px;
  margin: 5px;
  font-size: 1.1em;
  box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.15);
}

.popup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.popup-content {
  background: #D6F4CD;
  padding: 20px;
  border-radius: 5px;
  width: 400px;
  max-width: 90%;
}

.custom-dropdown,
.filters-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 80%;
  margin-bottom: 20px;
}

.custom-dropdown,
.filters {
  display: flex;
  flex-direction: row;
  align-items: center;
  width: 40%;
  margin-bottom: 20px;
}

.custom-dropdown {
  width: 100%;
  padding: 8px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #fff;
  margin-top: 25px;
  color: #888;
}

h3 {
  margin: 10px 0;
  font-size: 18px;
  text-align: center;
}

p {
  font-size: 14px;
  color: #666666;
}
</style>
