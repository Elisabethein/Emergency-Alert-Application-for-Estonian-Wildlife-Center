<template>
  <teleport to="body">
    <div class="edit-ticket-popup">
      <div class="header">
        <h2>#{{ pseudoId }} – {{ editableTicket.species ? editableTicket.species : "Tuvastamata" }}</h2>
        <img
            v-if="editableTicket.status === 'Uus'"
            src="@/assets/bin.png"
            alt="Kustuta"
            class="delete-button"
            @click="deleteTicket"
        />

        <img
            src="@/assets/close.png"
            alt="Sulge"
            class="close-button"
            @click="$emit('close-popup')"
        />
      </div>

      <div class="animal-details">
        <div class="form-group">
          <div class="form-group">
            <label for="animalType"><strong>Liigigrupp: </strong></label>
            <select v-model="editableTicket.upperSpecies" id="animalType" @change="fetchSpecies" >
              <option disabled value="">Vali liigigrupp</option>
              <option v-for="upper in upperSpecies" :key="upper.id" :value="upper">
                {{ upper }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label for="animalName"><strong>Looma liik: </strong></label>
            <select v-model="editableTicket.species" id="animalName" :disabled="!editableTicket.upperSpecies">
              <option disabled value="">Vali loomaliik</option>
              <option v-for="species in filteredSpecies" :key="species.id" :value="species">
                {{ species }}
              </option>
            </select>
          </div>

          <div class="form-group">
            <label for="county"><strong>Maakond:</strong></label>
            <select v-model="editableTicket.region.name" id="county">
              <option disabled value="">Vali maakond</option>
              <option v-for="region in regions" :key="region.id" :value="region">
                {{ region }}
              </option>
            </select>
          </div>

          <div class="form-group">
            <label for="locationDetails"
            ><strong>Asukoht:</strong></label>
            <input type="text" v-model="editableTicket.location"/>
          </div>

          <div class="form-group">
            <label for="locationDetails"
            ><strong>Asukoha juhised:</strong></label>
            <input type="text" v-model="editableTicket.directions"/>
          </div>

          <div class="form-group">
            <label for="date">
              <strong>Sisestamise kuupäev:</strong> {{ formatDate(editableTicket.createdAt) }}
            </label>
          </div>

          <div class="form-group">
            <label for="vigastus"><strong>Vigastus:</strong></label>
            <multiselect
                v-model="editableTicket.tags"
                :options="tags"
                :multiple="true"
                :placeholder="editableTicket.tags.length ? '' : 'Vali vigastused'"
            />
          </div>

          <div class="form-group">
            <label for="resolution"><strong>Lahendus:</strong></label>
            <select v-model="editableTicket.resolution" id="resolution">
              <option disabled value="">Vali lahendus</option> <!-- Default option -->
              <option v-for="resolution in resolutions" :key="resolution.id" :value="resolution.name">
                {{ resolution.name }}
              </option>
            </select>
          </div>
        </div>
      </div>

      <div class="description">
        <h3>Saabunud teate kirjeldus</h3>
        <div class="form-group">
          <label for="describedAnimal"><strong>Kirjeldatud loomaliik:</strong></label>
          <input type="text" v-model="editableTicket.describedAnimal"/>
        </div>
        <div class="form-group">
          <label for="description"><strong>Juhtunu kirjeldus:</strong></label>
          <textarea v-model="editableTicket.description"></textarea>
        </div>
        <div class="form-group">
          <label for="status"><strong>Piltide info: </strong></label>
          <div v-if="editableTicket.images && editableTicket.images.length">
            <div class="image-container" v-for="(image, index) in editableTicket.images" :key="index">
              <p><a :href="'https://drive.google.com/file/d/' + image.driveId + '/view'" target="_blank">Vaata pilti</a></p>
            </div>
          </div>
          <div v-else>
            <span>Ühtegi pilti ei ole lisatud</span>
          </div>
        </div>
      </div>

      <div class="tags">
        <h3>Tagid</h3>
        <div v-if="editableTicket.upperSpecies" class="tag">{{ editableTicket.upperSpecies }}</div>
        <div v-if="editableTicket.region.name" class="tag">{{ editableTicket.region.name }}</div>
        <div v-for="tag in editableTicket.tags" :key="tag.id" class="tag">
          {{ tag }}
        </div>
        <div v-if="editableTicket.resolution" class="tag">{{ editableTicket.resolution }}</div>
      </div>

      <div class="personal-information">
        <div class="reporter-information">
          <h3>Teavitaja kontakt</h3>
          <p><strong>Nimi:</strong> {{ editableTicket.reporterName }}</p>
          <p><strong>Telefon:</strong> {{ editableTicket.reporterPhone }}</p>
          <p><strong>Email:</strong> {{ editableTicket.reporterEmail }}</p>
          <p><strong>On sündmuskohal:</strong> {{ editableTicket.reporterCanWait ? 'jah' : 'ei' }}</p>
          <p><strong>Sotsiaalmeedia:</strong>{{ editableTicket.reporterSocialMedia }}</p>
        </div>

        <div class="volunteer-information">
          <h3>Vabatahtliku kontakt</h3>
          <multiselect
              v-model="editableVolunteers"
              :options="formattedVolunteers"
              :multiple="true"
              :placeholder="editableVolunteers.length ? '' : 'Vali vabatahtlikud'"
              label="fullName"
              track-by="id"
              :close-on-select="false"
          />
        </div>
      </div>

      <div class="history">
        <h3>Hoiukodu info</h3>
        <label>
          <input type="checkbox" v-model="editableTicket.hospital" />
          Loom vajab haiglaravi/on haiglas
        </label>
        <textarea v-model="editableTicket.history"></textarea>
      </div>

      <div class="dates">
        <h3>Alustamise ja lõpetamise kuupäevad</h3>
        <p>Sisestage vajadusel ise alustamise ja/või lõpetamise kuupäevad:</p>
        <div class="form-group">
          <label for="startDate"><strong>Alustamise kuupäev:</strong></label>
          <input
              type="date"
              :value="formatDateForInput(editableTicket.openDate)"
              @input="editableTicket.openDate = $event.target.value"
          />
          <label for="endDate"><strong>Lõpetamise kuupäev:</strong></label>
          <input
              type="date"
              :value="formatDateForInput(editableTicket.closeDate)"
              @input="editableTicket.closeDate = $event.target.value"
          />
        </div>
      </div>

      <div class="status">
        <div class="form-group">
          <label for="status"><strong>Staatus:</strong> </label>
          <select v-model="editableTicket.status" id="status" @change="changeStatus($event.target.value)">
            <option disabled value="">Vali staatus</option>
            <option v-for="status in statuses" :key="status.id" :value="status.name">
              {{ status.name }}
            </option>
          </select>
        </div>
      </div>

      <button @click="saveChanges">Salvesta</button>
    </div>
  </teleport>
</template>

<script>
import Multiselect from 'vue-multiselect';
import 'vue-multiselect/dist/vue-multiselect.min.css';

export default {
  name: 'EditTicket',
  components: {
    Multiselect,
  },
  props: {
    ticket: Object,
    token: String,
    pseudoId: Number,
  },
  emits: ["update-ticket", "close-popup", "delete-ticket"],
  data() {
    return {
      editableTicket: {
        ...this.ticket,
        status: this.ticket.status || '', // For status
        region: this.ticket.region || {name: ''}, // Initialize region
        resolution: this.ticket.resolution || '', // Initialize resolution
        upperSpecies: this.ticket.upperSpecies || '',
        species: this.ticket.species || '',
        volunteers: this.ticket.volunteers || [],
        openDate: this.formatDateForInput(this.ticket.openDate), // Convert the date format
        closeDate: this.formatDateForInput(this.ticket.closeDate), // Convert the date format
      },
      tags: [],
      species: [],
      filteredSpecies: [],
      regions: [],
      volunteers: [],
      upperSpecies: [],
      statuses: [],
      resolutions: [],
      selectedUpperSpecies: '',
    };
  },
  mounted() {
    this.fetchData();
    if (this.editableTicket.upperSpecies) {
      this.fetchSpecies(this.editableTicket.upperSpecies);
    }
  },
  computed: {
    formattedVolunteers() {
      return this.volunteers.map(volunteer => ({
        ...volunteer,
        fullName: `${volunteer.firstName} ${volunteer.lastName}`
      }));
    },
    editableVolunteers: {
      get() {
        return this.editableTicket.volunteers.map(volunteer => ({
          ...volunteer,
          fullName: volunteer.fullName || `${volunteer.firstName} ${volunteer.lastName}`,
        }));
      },
      set(value) {
        this.editableTicket.volunteers = value;
      }
    },
    isReadyForHoolekodus() {
      return this.areRequiredFieldsFilled() && this.editableTicket.volunteers && this.editableTicket.volunteers.length > 0;
    },
    isReadyForAvatud() {
      return this.areRequiredFieldsFilled();
    },
    isReadyForLopetatud(){
      return this.isReadyForHoolekodus && this.editableTicket.resolution
    }
  },
  watch: {
    'editableTicket.upperSpecies': function (newVal) {
      if (newVal) {
        this.fetchSpecies();
      } else {
        this.filteredSpecies = [];
        this.editableTicket.species = null; // Reset species if no upper species is selected
      }
    },
    'editableTicket.tags': {
      handler(newTags) {
        this.editableTicket.tags = newTags;
      },
      immediate: true,
      deep: true,
    },
    'editableTicket.openDate': function (newDate) {
      if (newDate && !this.editableTicket.closeDate) {
        this.changeStatus('Avatud')
      }
    },
    'editableTicket.closeDate': function (newDate) {
      if (newDate) {
        this.changeStatus('Lõpetatud');
      }
    },
    'editableTicket.species': 'updateStatus',
    'editableTicket.region.name': 'updateStatus',
    'editableTicket.resolution': 'updateStatus',
    'editableVolunteers': {
      handler: 'updateStatus',
      deep: true
    },
  },
  methods: {
    async fetchData() {
      try {
        const [tagsResponse, regionsResponse, usersResponse, upperSpeciesResponse, statusesResponse, resolutionsResponse] = await Promise.all([
          //fetch('http://localhost:8080/api/animalTags/injuries', {
          fetch('api/animalTags/injuries', {
            headers: {
              'Authorization': `Bearer ${this.token}`,
            }
          }),
          //fetch('http://localhost:8080/api/regions/all', {
          fetch('api/regions/all', {
            headers: {
              'Authorization': `Bearer ${this.token}`, // Include the token
            }
          }),
          //fetch('http://localhost:8080/api/users/all', {
          fetch('api/users/all', {
            headers: {
              'Authorization': `Bearer ${this.token}`,
            }
          }),
          //fetch('http://localhost:8080/api/upperSpecies/all', {
          fetch('api/upperSpecies/all', {
            headers: {
              'Authorization': `Bearer ${this.token}`,
            }
          }),
          //fetch('http://localhost:8080/api/statuses/all', {
          fetch('api/statuses/all', {
            headers: {
              'Authorization': `Bearer ${this.token}`,
            }
          }),
          //fetch('http://localhost:8080/api/resolutions/all', {
          fetch('api/resolutions/all', {
            headers: {
              'Authorization': `Bearer ${this.token}`,
            }
          })
        ]);

        const tags = await tagsResponse.json();
        const regions = await regionsResponse.json();
        const users = await usersResponse.json();
        const upperSpecies = await upperSpeciesResponse.json();
        const statuses = await statusesResponse.json();
        const resolutions = await resolutionsResponse.json();

        // Assign these to component data properties
        this.tags = tags.map(tag => tag.injury);
        this.regions = regions;
        this.volunteers = users;
        this.upperSpecies = upperSpecies.map(species => species.name)
        this.statuses = statuses;
        this.resolutions = resolutions;
      } catch (error) {
        alert('Andmete laadimine ebaõnnestus!');
        this.$emit('close-popup');
      }
    },

    async fetchSpecies() {
      const upperSpeciesId = this.editableTicket.upperSpecies;
      const token = this.token;
      try {
        //const response = await fetch(`http://localhost:8080/api/species/all/${upperSpeciesId}`, {
        const response = await fetch(`api/species/all/${upperSpeciesId}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          }
        });
        if (response.ok) {
          const filteredSpecies = await response.json();
          this.filteredSpecies = filteredSpecies.map(species => species.name);
        } else if (response.status === 403) {
          this.handleUnauthorized();
        }
      } catch (error) {
        console.error('Failed to fetch species:', error);
      }
    },
    async saveChanges() {
      try {
        //const response = await fetch(`http://localhost:8080/api/tickets/edit/${this.editableTicket.id}`, {
        const response = await fetch(`api/tickets/edit/${this.editableTicket.id}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${this.token}`, // Include the token
          },
          body: JSON.stringify(this.editableTicket),
        });

        if (response.ok) {
          const updatedTicket = await response.json();
          this.$emit('close-popup');
          this.$emit('update-ticket', updatedTicket);
        } else if (response.status === 403) {
          this.handleUnauthorized(response);
        }
      } catch (error) {
        console.error(error);
        alert('Pileti muutmine ebaõnnestus: ' + error.message);
      }
    },
    async deleteTicket() {
      if (confirm('Kas olete kindel, et soovite selle juhtumi kustutada?')) {
        try {
          //const response = await fetch(`http://localhost:8080/api/tickets/delete/${this.editableTicket.id}`, {
          const response = await fetch(`api/tickets/delete/${this.editableTicket.id}`, {
            method: 'DELETE',
            headers: {
              'Authorization': `Bearer ${this.token}`, // Include the token
            }
          });
          if (response.ok) {
            this.$emit('close-popup');
            this.$emit('delete-ticket', this.editableTicket);
          } else if (response.status === 403) {
            this.handleUnauthorized(response);
          }
        } catch (error) {
          console.error(error);
          alert('Teate kustutamine ebaõnnestus: ' + error.message);
        }
      }
    },
    formatDate(dateStr) {
      const date = new Date(dateStr);
      // Format to 'YYYY-MM-DD HH:mm'
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are 0-indexed
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      return `${day}.${month}.${year} ${hours}:${minutes}`;
    },
    formatDateForInput(date) {
      if (!date) return '';
      return new Date(date).toISOString().split('T')[0];
    },
    areRequiredFieldsFilled() {
      return this.editableTicket.species && this.editableTicket.upperSpecies && this.editableTicket.region.name;
    },
    changeStatus(newStatus) {
      // Check if required fields are filled before setting status to 'Avatud' or 'Hoiukodus'
      if (newStatus === 'Hoiukodus') {
        if (this.areRequiredFieldsFilled() || (!this.editableTicket.volunteers || this.editableTicket.volunteers.length === 0)) {
          alert('Palun määrake vähemalt üks vabatahtlik enne staatuse "Hoiukodus" määramist!');
          //this.editableTicket.status = "Avatud";
          return;
        }
        else if(!this.areRequiredFieldsFilled() || (!this.editableTicket.volunteers || this.editableTicket.volunteers.length === 0))  {
          alert('Palun määrake liigigrupp, looma liik, maakond ja vähemalt üks vabatahtlik enne staatuse "Hoiukodus" määramist!');
          //this.editableTicket.status = 'Uus';
          return;
        }
      } else if (newStatus === 'Avatud') {
        if (!this.areRequiredFieldsFilled()) {
          alert('Palun määrake liigigrupp, looma liik ja maakond enne staatuse "Avatud" määramist või alustamise kuupäeva valimist!');
          //this.editableTicket.status = "Uus";
          this.editableTicket.openDate = '';
          return;
        }
      } else if (newStatus === 'Lõpetatud') {
        if (!this.areRequiredFieldsFilled() || (!this.editableTicket.volunteers || this.editableTicket.volunteers.length === 0) || !this.editableTicket.resolution || this.editableTicket.tags.length === 0) {
          alert('Palun määrake liigigrupp, looma liik, maakond, tulemus, vigastused ja vähemalt üks vabatahtlik enne staatuse "Lõpetatud" määramist või lõpetamiskuupäeva valimist!');
          //this.editableTicket.status = "Hoiukodus";
          this.editableTicket.closeDate = '';
          return;
        }
      }
      this.editableTicket.status = newStatus;
    },
    updateStatus() {
      if (this.isReadyForHoolekodus) {
        this.editableTicket.status = "Hoiukodus";
      } else if (this.isReadyForAvatud) {
        this.editableTicket.status = "Avatud";
      } else if (this.isReadyForLopetatud) {
        this.editableTicket.status = "Lõpetatud"
      } else {
        this.editableTicket.status = "Uus"; // Või mõni muu vaikeseade
      }
    },
  }
};
</script>


<style scoped>
.close-button {
  position: absolute;
  top: 20px;
  right: 20px;
  cursor: pointer;
  width: 30px;
  height: 30px;
}

.delete-button {
  cursor: pointer;
  width: 20px;
  height: 20px;
}

.header {
  background-color: rgba(135, 210, 110);
  border-radius: 10px 10px 0 0;
  padding: 5px;
  display: flex;
  justify-content: flex-start;
  align-items: center;
}

.confirmation-popup {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: white;
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 20px;
  z-index: 1000;
  display: flex;
  flex-direction: column;
  align-items: center;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.4);
}

.confirmation-popup h3 {
  margin-bottom: 20px;
}

.confirmation-popup button {
  margin: 5px;
  padding: 10px;
  width: 100px;
}

.edit-ticket-popup {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: white;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  z-index: 1000;
  width: 600px;
  max-width: 80%;
  max-height: 80vh;
  overflow-y: auto;
}

.edit-ticket-popup h2 {
  margin: 20px;
  margin-right: 10px;
}

/* Animal details stiil */
.animal-details {
  background-color: #f9f9f9;
  border-radius: 5px;
  padding: 10px;
  margin: 20px;
  box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.1);
}

.form-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 10px;
}

.form-group label {
  margin-bottom: 5px;
}

select {
  color: #333;
  appearance: none;
  cursor: pointer;
}

input,
select,
textarea {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 5px;
  width: auto;
  align-items: center;
}

/* Kirjeldus ja tagid stiil */
.description,
.tags,
.history,
.status,
.dates {
  background-color: #f9f9f9;
  border-radius: 5px;
  padding: 5px;
  margin: 20px;
  box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.1);
}

textarea {
  height: 100px;
  resize: none;
  margin: 7px;
  width: 94%;
}

.ticket-tags {
  display: flex; /* Kasutame flexboxi, et tagid oleksid reas */
  flex-wrap: wrap; /* Kui tagid ei mahu ühele reale, siis liiguvad järgmisele */
  align-items: center;
}

.tag {
  background-color: #f1f4f1;
  border-radius: 15px;
  padding: 10px 20px;
  margin: 5px;
  display: inline-block; /* Tagid on ühel real */
  box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.1);
}

/* Kontaktinfo stiil */
.personal-information {
  display: flex;
  justify-content: space-between;
  margin: 20px;
}

.reporter-information,
.volunteer-information {
  background-color: #f9f9f9;
  border-radius: 5px;
  padding: 10px;
  width: 46%;
  box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.1);
}

button {
  background-color: rgba(135, 210, 110);
  padding: 15px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  font-weight: bold;
  text-align: center;
  transition: background-color 0.3s ease;
  margin: 20px auto;
  width: 30%;
  display: block;
}

button:hover {
  background-color: #6aa32b;
}
p {
  font-family: 'Inria Serif', serif;
}
</style>
  