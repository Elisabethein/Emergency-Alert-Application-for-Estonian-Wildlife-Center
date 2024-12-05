<template>
  <div class="settings-page">
    <h1>Seaded</h1>
    <h3>Siin lehel on võimalus vaadata ja lisada regioone, liigigruppe, alamliike, kasutajate funktsioone, vigastuste ja lahenduste nimetusi.</h3>

    <div class="settings-container">
      <div class="section" v-for="(section, index) in sections" :key="index">
        <h3>{{ section.title }}</h3>
        <div class="view-container">
          <span v-for="(item, i) in section.items" :key="i">{{ item }}</span>
        </div>
        <div class="form-field">
          <label for="input-{{ index }}">{{ section.inputLabel }}</label>
          <input
              type="text"
              :id="'input-' + index"
              v-model="section.inputValue"
              :placeholder="section.inputPlaceholder"
          />
        </div>

        <div class="button-container">
          <button @click="handleAdd(section)">Lisa</button>
        </div>
      </div>

      <!-- New Section for Species -->
      <div class="section">
        <h3>Liigid</h3>
        <div class="form-field">
          <label for="upperSpecies">Vali Ülemliik:</label>
          <select v-model="selectedUpperSpecies" @change="fetchSpecies">
            <option v-for="(species, index) in upperSpeciesList" :key="index" :value="species">
              {{ species }}
            </option>
          </select>
        </div>
        <div class="view-container">
          <span v-for="(species, i) in speciesItems" :key="i">{{ species }}</span>
        </div>
        <div class="form-field">
          <label for="species-input">Uus liik:</label>
          <input
              type="text"
              id="species-input"
              v-model="newSpeciesInput"
              placeholder="Sisesta uus liik"
          />
        </div>
        <div class="button-container">
          <button @click="handleAddSpecies">Lisa Liik</button>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
export default {
  name: "SettingsPage",
  data() {
    return {
      sections: [
        {
          title: "Regioonid",
          inputLabel: "Uus regioon:",
          inputValue: "",
          inputPlaceholder: "Sisesta uus regioon",
          items: [],
          apiEndpoint: '/api/regions/all',
          apiAddEndpoint: '/api/regions/add',
        },
        {
          title: "Funktsioonid",
          inputLabel: "Uus funktsioon:",
          inputValue: "",
          inputPlaceholder: "Sisesta uus funktsioon",
          items: [],
          apiEndpoint: '/api/tags/helpOptions',
          apiAddEndpoint: '/api/tags/add',
        },
        {
          title: "Liigigrupid",
          inputLabel: "Uus liigigrupp:",
          inputValue: "",
          inputPlaceholder: "Sisesta uus liigigrupp",
          items: [],
          apiEndpoint: '/api/upperSpecies/all',
          apiAddEndpoint: '/api/upperSpecies/add',
        },
        {
          title: "Vigastused",
          inputLabel: "Uus vigastus:",
          inputValue: "",
          inputPlaceholder: "Sisesta uus vigastus",
          items: [],
          apiEndpoint: '/api/animalTags/injuries',
          apiAddEndpoint: '/api/animalTags/add',
        },
        {
          title: "Lahendused",
          inputLabel: "Uus lahendus:",
          inputValue: "",
          inputPlaceholder: "Sisesta uus lahendus",
          items: [],
          apiEndpoint: '/api/resolutions/all',
          apiAddEndpoint: '/api/resolutions/add',
        }
      ],
      upperSpeciesList: [],
      selectedUpperSpecies: null,
      speciesItems: [],
      newSpeciesInput: '',
      alertShown: false,
    };
  },
  created() {
    this.sections.forEach((section, index) => {
      this.fetchData(section.apiEndpoint, index);
    });
    this.fetchUpperSpecies();
  },
  methods: {
    getAuthToken() {
      return localStorage.getItem('token');
    },
    async fetchData(endpoint, sectionIndex) {
      const token = this.getAuthToken();
      try {
        //const response = await fetch(`http://localhost:8080${endpoint}`, {
        const response = await fetch(`${endpoint}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          }
        });
        if (response.ok) {
          const data = await response.json();
          this.sections[sectionIndex].items = data.map(item => item.name || item.function || item.injury ||item);
        } else if (response.status === 403) {
          this.handleUnauthorized();
        }
      } catch (error) {
        console.error(`Failed to fetch data from ${endpoint}:`, error);
      }
    },
    async fetchUpperSpecies() {
      await this.fetchData('/api/upperSpecies/all', 2);
      this.upperSpeciesList = this.sections[2].items;
    },
    async handleAdd(section) {
      if (section.inputValue) {
        const token = this.getAuthToken();
        try {
          //const response = await fetch(`http://localhost:8080${section.apiAddEndpoint}`, {
          const response = await fetch(`${section.apiAddEndpoint}`, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ name: section.inputValue })
          });
          if (response.ok) {
            section.items.push(section.inputValue);
            section.inputValue = '';
            location.reload();
          } else if (response.status === 403) {
            this.handleUnauthorized();
          } else if (response.status === 400) {
            alert('Selline nimi on juba olemas!');
            section.inputValue = '';
          }
        } catch (error) {
          console.error(`Failed to add item to ${section.title}:`, error);
        }
      }
    },
    handleUnauthorized() {
      if (this.alertShown) return;
      console.error('Unauthorized: session expired');
      alert('Teie sessioon on aegunud, palun logige uuesti sisse!');
      this.$store.dispatch('logout');
      this.$router.push('/login');
      this.alertShown = true;
    },
    async fetchSpecies() {
      if (!this.selectedUpperSpecies) return;
      const token = this.getAuthToken();
      try {
        //const response = await fetch(`http://localhost:8080/api/species/all/${this.selectedUpperSpecies}`, {
        const response = await fetch(`api/species/all/${this.selectedUpperSpecies}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          }
        });
        if (response.ok) {
          this.speciesItems = (await response.json()).map(species => species.name);
        } else if (response.status === 403) {
          this.handleUnauthorized();
        }
      } catch (error) {
        console.error('Failed to fetch species:', error);
      }
    },
    async handleAddSpecies() {
      if (this.newSpeciesInput && this.selectedUpperSpecies) {
        const token = this.getAuthToken();
        try {
          //const response = await fetch(`http://localhost:8080/api/species/add`, {
          const response = await fetch('api/species/add', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({
              name: this.newSpeciesInput,
              upperSpeciesName: this.selectedUpperSpecies
            })
          });
          if (response.ok) {
            this.speciesItems.push(this.newSpeciesInput);
            this.newSpeciesInput = '';
          } else if (response.status === 403) {
            this.handleUnauthorized();
          } else if (response.status === 400) {
            alert('Selline nimi on juba olemas!');
            this.newSpeciesInput = '';
          }
        } catch (error) {
          console.error('Failed to add species:', error);
        }
      }
    },


  }
};
</script>

<style scoped>
.settings-page {
  padding: 10px;
  display: flex;
  flex-direction: column; /* Stack the header and settings container vertically */
  align-items: center;
}

h2 {
  text-align: center;
  color: #2d2d2d;
}

.settings-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  width: 90%;
}

h3 {
  text-align: center;
}

.section {
  background-color: #D6F4CD;
  border-radius: 10px;
  padding: 15px; /* Reduced padding */
  margin: 10px; /* Reduced margin */
  width: 25%;
  display: flex;
  flex-direction: column; /* Stack children vertically */
  justify-content: flex-start; /* Align items to the start */
}

.form-field,
.button-container {
  margin-bottom: 5px; /* Reduced bottom margin */
  display: flex;
  flex-direction: column; /* Stack label and input vertically */
  align-items: center; /* Center align label and input */
}

label {
  display: block;
  margin-bottom: 5px;
}

.form-field input,
.form-field select {
  width: 90%; /* Fill the width of the section */
  padding: 8px; /* Adjust padding for comfort */
  border: none;
  border-radius: 5px;
  font-size: 0.9em; /* Slightly smaller font size */
}


button {
  background-color: #87D26E;
  border: none;
  border-radius: 7px;
  padding: 8px 12px; /* Reduced padding */
  cursor: pointer;
  font-size: 0.9em; /* Slightly smaller font size */
  margin-top: 5px; /* Reduced margin for spacing */
}

button:hover {
  background-color: #89d970;
}

.view-container {
  margin-top: 5px;
  display: flex;
  flex-direction: column;
  flex-grow: 1;
}

.view-container span {
  margin: 5px;
  background-color: #F1F4F1;
  border-radius: 5px;
  display: inline-flex;
  padding: 10px 20px;
  font-size: 1.1em;
  box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.15);
}

/* Media queries for responsiveness */
@media (max-width: 768px) {
  .settings-container {
    width: 100%;
  }

  .section {
    width: 65%;
    margin: 5px;
  }

  .form-field input,
  .form-field select {
    width: 100%;
    font-size: 0.85em;
  }

  button {
    padding: 6px 10px;
    font-size: 0.85em;
  }
}
</style>
