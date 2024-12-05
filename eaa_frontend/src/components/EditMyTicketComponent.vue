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
          <label for="animalType"><strong>Liigigrupp: </strong></label>
          <select v-model="editableTicket.upperSpecies" @change="fetchSpecies">
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
          <span>{{ editableTicket.region ? editableTicket.region.name : 'Tuvastamata' }}</span>
        </div>
        <div class="form-group">
          <label for="location"><strong>Asukoht:</strong></label>
          <span>{{ editableTicket.location }}</span>
        </div>
        <div class="form-group">
          <label for="location"><strong>Latituud:</strong></label>
          <span>{{ editableTicket.latitude }}</span>
        </div>

        <div class="form-group">
          <label for="location"><strong>Longituud:</strong></label>
          <span>{{ editableTicket.longitude }}</span>
        </div>
        <div class="form-group">
          <label for="date"><strong>Asukoha kirjeldus:</strong></label>
          <span>{{ editableTicket.directions }}</span>
        </div>

        <div class="form-group">
          <label for="date"><strong>Kuupäev:</strong></label>
          <span>{{ formatDate(editableTicket.createdAt) }}</span>
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
            <option disabled value="">Vali lahendus</option>
            <option v-for="resolution in resolutions" :key="resolution.id" :value="resolution.name">
              {{ resolution.name }}
            </option>
          </select>
        </div>
      </div>

      <div class="description">
        <h3>Saabunud teate kirjeldus</h3>
        <div class="form-group">
          <label for="status"><strong>Kirjeldatud loomaliik: </strong></label>
          <span>{{ editableTicket.describedAnimal }}</span>
        </div>
        <div class="form-group">
          <label for="status"><strong>Juhtunu kirjeldus: </strong></label>
          <span>{{ editableTicket.description }}</span>
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

      <div v-if="editableTicket.upperSpecies || editableTicket.region || editableTicket.tags || editableTicket.resolution" class="tags">
        <h3>Tagid</h3>
        <div>
          <span v-if="editableTicket.upperSpecies" class="tag">{{ editableTicket.upperSpecies }}</span>
          <span v-else><strong>Liigigrupp</strong> tuvastamata</span>
        </div>
        <div>
          <span v-if="editableTicket.region" class="tag">{{ editableTicket.region.name }}</span>
          <span v-else><strong>Piirkond</strong> tuvastamata</span>
        </div>
        <div>
          <div v-if="editableTicket.tags && editableTicket.tags.length">
            <span v-for="(tag, index) in editableTicket.tags" :key="tag.id" class="tag">
              {{ tag }}<span v-if="index < editableTicket.tags.length - 1"> </span>
            </span>
          </div>
          <span v-else><strong>Vigastused</strong> tuvastamata</span>
        </div>
        <div>
          <span v-if="editableTicket.resolution" class="tag">{{ editableTicket.resolution }}</span>
          <span v-else><strong>Tulemus</strong> tuvastamata</span>
        </div>
      </div>

      <div class="personal-information">
        <div class="reporter-information">
          <h3>Teavitaja kontakt</h3>
          <p><strong>Nimi:</strong> {{ editableTicket.reporterName }}</p>
          <p><strong>Telefon:</strong> {{ editableTicket.reporterPhone }}</p>
          <p><strong>Email:</strong> {{ editableTicket.reporterEmail }}</p>
          <p><strong>Sotsiaalmeedia:</strong>{{ editableTicket.reporterSocialMedia }}</p>
          <p><strong>On sündmuskohal:</strong> {{ editableTicket.reporterCanWait ? 'jah' : 'ei' }}</p>
        </div>
        <div v-if=editableTicket.volunteers class="volunteer-information">
          <h3>Vabatahtliku kontakt</h3>
          <div v-for="user in editableTicket.volunteers" :key="user.id" class="volunteer-details">
            <p><strong>Nimi: </strong>{{ user.firstName + " " + user.lastName }}</p>
          </div>
        </div>
        <div v-else>
          <h3>Vabatahtlikke pole</h3>
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
          <label for="status"><strong>Staatus:</strong></label>
          <select v-model="editableTicket.status" id="status" @change="changeStatus($event.target.value)">
            <option disabled value="">Vali staatus</option>
            <option
                v-for="status in filteredStatuses"
                :key="status.id"
                :value="status.name"
            >
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
        status: this.ticket.status || '',
        tags: this.ticket.tags || [],
        resolution: this.ticket.resolution || '',
        history: this.ticket.history || '',
        openDate: this.formatDateForInput(this.ticket.openDate), // Convert the date format
        closeDate: this.formatDateForInput(this.ticket.closeDate),
      },
      tags: [],
      filteredSpecies: [],
      statuses: [],
      resolutions: [],
      alertShown: false,
    };
  },
  mounted() {
    this.fetchData();
    if (this.editableTicket.upperSpecies) {
      this.fetchSpecies(this.editableTicket.upperSpecies);
    }
  },
  watch: {
    'editableTicket.resolution': 'updateStatus',
    'editableTicket.closeDate': function (newDate) {
      if (newDate) {
        this.changeStatus('Lõpetatud');
      }
    },
  },
  computed: {
    filteredStatuses() {
      const filtered = this.statuses.filter(status =>
          status.name === 'Hoiukodus' || status.name === 'Lõpetatud'
      );
      return filtered;
    }
  },
  methods: {
    getAuthToken() {
      return localStorage.getItem('token');
    },
    async fetchData() {
      try {
        const [tagsResponse, regionsResponse, upperSpeciesResponse, statusesResponse, resolutionsResponse] = await Promise.all([
          fetch('api/animalTags/injuries', {
          //fetch('http://localhost:8080/api/animalTags/injuries', {
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
        const upperSpecies = await upperSpeciesResponse.json();
        const statuses = await statusesResponse.json();
        const resolutions = await resolutionsResponse.json();

        // Assign these to component data properties
        this.tags = tags.map(tag => tag.injury);
        this.regions = regions;
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
      const token = this.token; // Use the passed token directly
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
      const userID = this.$store.state.user.id;
      const token = this.getAuthToken();
      try {
        //const response = await fetch(`http://localhost:8080/api/tickets/edit/${this.editableTicket.id}/${userID}`, {
        const response = await fetch(`api/tickets/edit/${this.editableTicket.id}/${userID}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
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
      const token = this.getAuthToken();
      if (confirm('Kas olete kindel, et soovite selle juhtumi kustutada?')) {
        try {
          //const response = await fetch(`http://localhost:8080/api/tickets/delete/${this.editableTicket.id}`, {
          const response = await fetch(`api/tickets/delete/${this.editableTicket.id}`, {
            method: 'DELETE',
            headers: {
              'Authorization': `Bearer ${token}`,
            }
          });
          if (response.ok) {
            this.$emit('close-popup');
            this.$emit('delete-ticket', this.editableTicket);
          } else if (response.status === 403) {
            this.handleUnauthorized();
          }
        } catch (error) {
          console.error('Delete failed:', error);
        }
      }
    },

    handleUnauthorized() {
      if (this.alertShown) return;
      alert('Teie sessioon on aegunud, palun logige uuesti sisse!');
      this.$store.dispatch('logout');
      this.$router.push('/login');
      this.alertShown = true;
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
    changeStatus(newStatus) {
      if (newStatus === 'Lõpetatud') {
        if (!this.editableTicket.resolution && this.editableTicket.tags.length === 0) {
          alert('Palun määrake lahendus ja vigastused enne staatuse "Lõpetatud" määramist või lõpetamiskuupäeva valimist!');
          this.editableTicket.status = "Hoiukodus";
          this.editableTicket.closeDate = '';
        } else {
          this.editableTicket.status = newStatus;
        }
      }
    },
    updateStatus() {
      if (this.editableTicket.resolution) {
        this.editableTicket.status = "Lõpetatud"
      } else {
        this.editableTicket.status = "Hoiukodus";
      }
    }
  },
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

p {
  font-family: 'Inria Serif', serif;
}

.header {
  background-color: rgba(135, 210, 110);
  border-radius: 10px 10px 0 0;
  padding: 5px;
  display: flex;
  justify-content: flex-start;
  align-items: center;
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
</style>
