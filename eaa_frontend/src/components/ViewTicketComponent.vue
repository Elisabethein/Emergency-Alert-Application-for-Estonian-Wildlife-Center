<template>
  <teleport to="body">
    <div class="view-ticket-popup">
      <div class="header">
        <h2>#{{ pseudoId }} – {{ localTicket.species ? localTicket.species : 'Tuvastamata' }}</h2>

        <img
            src="@/assets/close.png"
            alt="Sulge"
            class="close-button"
            @click="$emit('close-popup')"
        />
      </div>

      <div class="animal-details">
        <div class="form-group">
          <label for="animalName"><strong>Looma liik: </strong></label>
          <span>{{ localTicket.species ? localTicket.species : 'Tuvastamata' }}</span>
        </div>

        <div class="form-group">
          <label for="animalType"><strong>Liigigrupp: </strong></label>
          <span>{{ localTicket.upperSpecies ? localTicket.upperSpecies : 'Tuvastamata'}}</span>
        </div>

        <div class="form-group">
          <label for="county"><strong>Maakond:</strong></label>
          <span>{{ localTicket.region ? localTicket.region.name : 'Tuvastamata' }}</span>
        </div>

        <div class="form-group">
          <label for="location"><strong>Asukoht:</strong></label>
          <span>{{ localTicket.location }}</span>
        </div>

        <div class="form-group">
          <label for="location"><strong>Latituud:</strong></label>
          <span>{{ localTicket.latitude }}</span>
        </div>

        <div class="form-group">
          <label for="location"><strong>Longituud:</strong></label>
          <span>{{ localTicket.longitude }}</span>
        </div>

        <div class="form-group">
          <label for="date"><strong>Asukoha kirjeldus:</strong></label>
          <span>{{ localTicket.directions }}</span>
        </div>

        <div class="form-group">
          <label for="status"><strong>Staatus:</strong></label>
          <span>{{ localTicket.status }}</span>
        </div>

        <div class="form-group">
          <label for="status"><strong>Vigastus:</strong></label>
          <div v-if="localTicket.tags && localTicket.tags.length">
            <span v-for="(tag, index) in localTicket.tags" :key="tag.id">
              {{ tag }}<span v-if="index < localTicket.tags.length - 1">, </span>
            </span>
          </div>
          <div v-else>
            <span>Tuvastamata</span>
          </div>
        </div>


        <div v-if="localTicket.status === 'Lõpetatud'" class="form-group">
          <label for="status"><strong>Lahendus:</strong></label>
          <div v-if="localTicket.resolution">
            <span>{{ localTicket.resolution }}</span>
          </div>
          <div v-else>
            <span>Tuvastamata</span>
          </div>
        </div>
      </div>

      <div class="description">
        <h3>Saabunud teate kirjeldus</h3>
        <div class="form-group">
          <label for="status"><strong>Kirjeldatud loomaliik: </strong></label>
          <span>{{ localTicket.describedAnimal }}</span>
        </div>
        <div class="form-group">
          <label for="status"><strong>Juhtunu kirjeldus: </strong></label>
          <span>{{ localTicket.description }}</span>
        </div>
        <div class="form-group">
          <label for="status"><strong>Piltide info: </strong></label>
          <div v-if="localTicket.images && localTicket.images.length">
            <div class="image-container" v-for="(image, index) in localTicket.images" :key="index">
              <p><a :href="'https://drive.google.com/file/d/' + image.driveId + '/view'" target="_blank">Vaata pilti</a></p>
            </div>
          </div>
          <div v-else>
            <span>Ühtegi pilti ei ole lisatud</span>
          </div>
        </div>
      </div>

      <div v-if="localTicket.upperSpecies || localTicket.region || localTicket.tags || localTicket.resolution" class="tags">
        <h3>Tagid</h3>
        <div>
          <span v-if="localTicket.upperSpecies" class="tag">{{ localTicket.upperSpecies }}</span>
          <span v-else><strong>Liigigrupp</strong> tuvastamata</span>
        </div>
        <div>
          <span v-if="localTicket.region" class="tag">{{ localTicket.region.name }}</span>
          <span v-else><strong>Piirkond</strong> tuvastamata</span>
        </div>
        <div>
          <div v-if="localTicket.tags && localTicket.tags.length">
            <span v-for="(tag, index) in localTicket.tags" :key="tag.id" class="tag">
              {{ tag }}<span v-if="index < localTicket.tags.length - 1">, </span>
            </span>
          </div>
          <span v-else><strong>Vigastused</strong> tuvastamata</span>
        </div>
        <div>
          <span v-if="localTicket.resolution" class="tag">{{ localTicket.resolution }}</span>
          <span v-else><strong>Tulemus</strong> tuvastamata</span>
        </div>
      </div>


      <div class="personal-information">
        <div class="reporter-information">
          <h3>Teavitaja kontakt</h3>
          <p><strong>Nimi:</strong> {{ localTicket.reporterName }}</p>
          <p><strong>Telefon:</strong> {{ localTicket.reporterPhone }}</p>
          <p><strong>Email:</strong> {{ localTicket.reporterEmail }}</p>
          <p><strong>Sotsiaalmeedia:</strong>{{ localTicket.reporterSocialMedia }}</p>
          <p><strong>Jääb sündmuskohale:</strong> {{ localTicket.reporterCanWait ? 'jah' : 'ei' }}</p>
        </div>

        <div v-if="localTicket.volunteers && localTicket.volunteers.length" class="volunteer-information">
          <h3>Vabatahtliku(te) kontakt</h3>
          <div v-for="user in localTicket.volunteers" :key="user.id" class="volunteer-details">
            <p><strong>Nimi: </strong>{{ user.firstName + " " + user.lastName }}</p>

            <p v-if="user.regions && user.regions.length">
              <strong>Piirkonnad: </strong>
              <span>{{ user.regions.join(', ') }}</span>
            </p>

            <p v-if="user.tags && user.tags.length">
              <strong>Funktsioonid: </strong>
              <span v-for="(tag, index) in user.tags" :key="tag">
                {{ tag }}<span v-if="index < user.tags.length - 1">, </span>
              </span>
            </p>

            <p v-if="user.species && user.species.length">
              <strong>Liigid: </strong>
              <span v-for="(tag, index) in user.species" :key="tag">
                {{ tag.name }}
                <span v-if="tag.expert" class="expert-icon">⭐</span>
                <span v-if="index < user.species.length - 1">, </span>
              </span>
            </p>
          </div>
        </div>
        <span v-else class="volunteer-information"><h3>Vabatahtlik(ud) tuvastamata</h3></span>
      </div>

      <div class="history">
        <h3>Hoiukodu info</h3>
        <p><strong>Kas loom vajab veterinaari visiiti?</strong> {{localTicket.hospital ? 'Jah' : 'Ei'}}</p>
        <p>{{ localTicket.history ? localTicket.history : 'Info puudub' }}</p>
      </div>

      <div class="dates">
        <h3>Kuupäevad</h3>
        <p><strong>Juhtumi sisestamise kuupäev:</strong> {{ formatDate(localTicket.createdAt) }}</p>
        <p><strong>Juhtumi alustamise kuupäev:</strong> {{ localTicket.openDate? formatOpenAndCloseDate(localTicket.openDate) : "Alustamata" }}</p>
        <p><strong>Juhtumi lõpetamise kuupäev:</strong> {{ localTicket.closeDate? formatOpenAndCloseDate(localTicket.closeDate) : "Lõpetamata"}}</p>
      </div>

      <button @click="$emit('close-popup')">Sulge</button>
    </div>
  </teleport>
</template>

<script>
export default {
  name: "ViewTicketComponent",
  props: {
    ticket: {
      type: Object,
      required: true
    },
    pseudoId: Number,
  },
  data() {
    return {
      localTicket: {...this.ticket},
      images: [],
    };
  },
  watch: {
    ticket: {
      deep: true,
      immediate: true
    }
  },
  methods: {
    formatDate(dateStr){
      const date = new Date(dateStr);
      // Format to 'YYYY-MM-DD HH:mm'
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      return `${day}.${month}.${year} ${hours}:${minutes}`;
    },
    formatOpenAndCloseDate(dateStr){
      const date = new Date(dateStr);
      // Format to 'YYYY-MM-DD'
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      return `${day}.${month}.${year}`;
    },
    handleUnauthorized() {
      if (this.alertShown) return;
      alert('Teie sessioon on aegunud, palun logige uuesti sisse!');
      this.$store.dispatch('logout');
      this.$router.push('/login');
      this.alertShown = true;
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

.header {
  background-color: rgba(135, 210, 110);
  border-radius: 10px 10px 0 0; /* Rounded corners on the top */
  padding: 0px; /* Padding to avoid content touching edges */
  display: flex; /* Flex to align content */
  justify-content: space-between; /* Space between title and close button */
  align-items: center;
}

.view-ticket-popup {
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

.view-ticket-popup h2 {
  margin: 20px;
}

/* Animal details style */
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

span {
  color: #333;
}

/* Description and tags style */
.description,
.tags,
.history,
.dates {
  background-color: #f9f9f9;
  border-radius: 5px;
  padding: 5px;
  margin: 20px;
  box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.1);
}

.ticket-tags {
  display: flex; /* Use flexbox to arrange tags in a row */
  flex-wrap: wrap; /* Wrap to the next line if they overflow */
  align-items: center;
}

.tag {
  background-color: #f1f4f1;
  border-radius: 15px;
  padding: 10px 20px;
  margin: 5px;
  display: inline-block; /* Tags in a row */
  box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.1);
}

/* Contact info style */
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
  