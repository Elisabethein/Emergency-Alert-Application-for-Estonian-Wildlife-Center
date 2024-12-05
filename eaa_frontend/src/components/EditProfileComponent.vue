<template>
  <teleport to="body">
    <div class="edit-user-popup">
      <div class="header">
        <h2>Muuda kasutajat: {{ editableUser.firstName }} {{ editableUser.lastName }}</h2>

        <img
            src="@/assets/close.png"
            alt="Sulge"
            class="close-button"
            @click="$emit('close-popup')"
        />
      </div>

      <div class="user-details">
        <h3>Kasutaja info</h3>
        <div class="form-group">
          <label for="firstName"><strong>Eesnimi*: </strong></label>
          <input v-model="editableUser.firstName" required type="text" id="firstName" @blur="validateWord('firstName')" placeholder="Eesnimi"/>
          <span v-if="firstNameError" class="error">{{ firstNameError }}</span>
        </div>

        <div class="form-group">
          <label for="lastName"><strong>Perekonnanimi*: </strong></label>
          <input v-model="editableUser.lastName" required type="text" id="lastName" @blur="validateWord('lastName')" placeholder="Perekonnanimi"/>
          <span v-if="lastNameError" class="error">{{ lastNameError }}</span>
        </div>

        <div class="form-group">
          <label for="birthDate"><strong>Sünnikuupäev*: </strong></label>
          <input v-model="editableUser.birthDate" required type="date" id="birthDate" />
          <span v-if="birthDateError" class="error">{{ birthDateError }}</span>
        </div>

        <div class="form-group">
          <label for="createdDate"><strong>Liitumiskuupäev: </strong></label>
          <span>{{ new Date(editableUser.createdAt).toLocaleDateString() }}</span>
        </div>
      </div>

      <div class="user-roles">
        <h3>Rollid</h3>
        <span>{{ editableUser .roles.join(', ') }}</span>
      </div>

      <div class="user-contact">
        <h3>Kontakt</h3>
        <div class="form-group">
          <label for="phoneNr"><strong>Telefon*: </strong></label>
          <input v-model="editableUser.phoneNr" required type="text" id="phoneNr" @blur="validatePhoneNr" placeholder="Telefoni number"/>
          <span v-if="phoneNrError" class="error">{{ phoneNrError }}</span>
        </div>

        <div class="form-group">
          <label for="email"><strong>Email*: </strong></label>
          <input v-model="editableUser.email" required type="email" id="email" @blur="validateEmail" placeholder="Email"/>
          <span v-if="emailError" class="error">{{ emailError }}</span>
        </div>
      </div>

      <div class="user-address">
        <h3>Asukoha info</h3>
        <div class="form-group">
          <label for="county"><strong>Maakond*: </strong></label>
          <input v-model="editableUser.county" required type="text" id="county" @blur="validateWord('county')" placeholder="Maakond"/>
          <span v-if="countyError" class="error">{{ countyError }}</span>
        </div>

        <div class="form-group">
          <label for="city"><strong>Linn*: </strong></label>
          <input v-model="editableUser.city" required type="text" id="city" @blur="validateWord('city')" placeholder="Linn"/>
          <span v-if="cityError" class="error">{{ cityError }}</span>
        </div>

        <div class="form-group">
          <label for="address"><strong>Aadress*: </strong></label>
          <input v-model="editableUser.streetName" required type="text" id="streetName" @blur="validateWord('streetName')" placeholder="Tänav">
          <span v-if="streetNameError" class="error">{{ streetNameError }}</span>
          <input v-model="editableUser.streetNr" required type="text" id="streetNr" placeholder="Maja ja/või korteri number">
          <span v-if="streetNrError" class="error">{{ streetNrError }}</span>
          <input v-model="editableUser.postalCode" required type="text" id="postalCode" @blur="validateNumber('postalCode')" placeholder="Postiindeks">
          <span v-if="postalcodeError" class="error">{{ postalcodeError }}</span>
        </div>
      </div>

      <div class="user-tags-regions-species">
        <h3>Funktsioonid</h3>
        <multiselect
            v-model="editableUser.tags"
            :options="tags"
            :multiple="true"
            :placeholder="editableUser.tags.length ? '' : 'Vali funktsioonid'"
        />

        <h3>Regioonid*</h3>
        <multiselect
            v-model="editableUser.regions"
            :options="regions"
            :multiple="true"
            :placeholder="editableUser.regions.length ? '' : 'Vali regioonid'"
            :class="{'is-invalid': !isValidRegions}"
        />
        <div v-if="!isValidRegions" class="error">Regioonid on kohustuslikud!</div>

        <h3>Liigigrupid</h3>
        <div>
            <span v-for="speciesItem in editableUser .species" :key="speciesItem.id">
              <span>{{ speciesItem.name }}</span>
              <span v-if="speciesItem.expert"> (Ekspert)</span>
            </span>
        </div>
      </div>

      <div class="buttons">
        <button @click="saveChanges">Salvesta kasutaja</button>
        <button @click="deleteUser" class="delete-button">Kustuta kasutaja</button>
        <button @click="$emit('close-popup')">Sulge</button>
      </div>

    </div>
  </teleport>
</template>

<script>
import Multiselect from 'vue-multiselect';
import 'vue-multiselect/dist/vue-multiselect.min.css';

export default {
  name: 'EditUser',
  components: {
    Multiselect,
  },
  props: {
    user: Object,
    token: String, // Add a prop for the token
  },
  emits: ["update-user", "close-popup", "delete-user"],
  data() {
    return {
      editableUser: {...this.user},
      roles: [],
      tags: [],
      regions: [],
      species: [],
      phoneNrError: '',
      emailError: '',
      firstNameError: '',
      lastNameError: '',
      birthDateError: '',
      countyError: '',
      cityError: '',
      streetNameError: '',
      streetNrError: '',
      postalcodeError: '',
      formHasErrors: true,
      isValidRegions: true,
    };
  },
  mounted() {
    this.fetchData();
  },
  methods: {
    validatePhoneNr() {
      const phoneNrPattern = /^\+?\d+$/;
      if (this.editableUser.phoneNr && !phoneNrPattern.test(this.editableUser.phoneNr)) {
        this.phoneNrError = 'Telefoninumber võib sisaldada ainult numbreid!';
      } else {
        this.phoneNrError = '';
      }
      this.checkFormValidity();
    },
    validateEmail() {
      if (this.editableUser.email && !this.editableUser.email.includes('@')) {
        this.emailError = 'E-post peab sisaldama "@" sümbolit.';
      } else {
        this.emailError = '';
      }
      this.checkFormValidity();
    },
    validateNumber(field) {
      const numberPattern = /^\d+$/;
      if (field === "postalCode") {
        if (this.editableUser.postalCode && !numberPattern.test(this.editableUser.postalCode)) {
          this.postalcodeError = 'Postiindeks võib sisaldada ainult numbreid!';
        }
      }
      this.checkFormValidity();
    },
    validateWord(field) {
      const wordPattern = /^[A-Za-z ]+$/;
      if (field === "firstName") {
        if (this.editableUser.firstName && !wordPattern.test(this.editableUser.firstName)) {
          this.firstNameError = 'Eesnimi võib sisaldada ainult tähti!';
        }
      } else if (field === "lastName") {
        if (this.editableUser.lastName && !wordPattern.test(this.editableUser.lastName)) {
          this.lastNameError = 'Perekonnanimi võib sisaldada ainult tähti!';
        }
      } else if (field === "county") {
        if (this.editableUser.county && !wordPattern.test(this.editableUser.county)) {
          this.countyError = 'Maakond võib sisaldada ainult tähti!';
        }
      } else if (field === "city") {
        if (this.editableUser.city && !wordPattern.test(this.editableUser.city)) {
          this.cityError = 'Linn võib sisaldada ainult tähti!';
        }
      } else if (field === "streetName") {
        if (this.editableUser.streetName && !wordPattern.test(this.editableUser.streetName)) {
          this.streetNameError = 'Tänav võib sisaldada ainult tähti!';
        }
      }
      this.checkFormValidity();
    },
    checkFormValidity() {
      // Kontrollib, kas kõik nõutavad väljad on täidetud
      this.formHasErrors =
          this.phoneNrError !== '' ||
          this.emailError !== '' ||
          this.firstNameError !== '' ||
          this.lastNameError !== '' ||
          this.countyError !== '' ||
          this.cityError !== '' ||
          this.streetNameError !== '' ||
          this.streetNrError !== '' ||
          this.postalcodeError !== '';
    },
    async fetchData() {
      try {
        const [tagsResponse, regionsResponse] = await Promise.all([
          fetch('api/tags/helpOptions', {
          //fetch('http://localhost:8080/api/tags/helpOptions', {
            headers: {
              'Authorization': `Bearer ${this.token}`, // Include the token
            }
          }),
          fetch('api/regions/all', {
          //fetch('http://localhost:8080/api/regions/all', {
            headers: {
              'Authorization': `Bearer ${this.token}`, // Include the token
            }
          })
        ]);
        const tags = await tagsResponse.json();
        this.tags = tags.map(tag => tag.function);
        this.regions = await regionsResponse.json();
      } catch (error) {
        alert('Andmete laadimine ebaõnnestus!');
        this.$emit('close-popup');
      }
    },
    validateFields() {
      // Reset errors
      this.firstNameError = '';
      this.lastNameError = '';
      this.countyError = '';
      this.cityError = '';
      this.streetNameError = '';
      this.phoneNrError = '';
      this.emailError = '';
      this.streetNrError = '';
      this.postalcodeError = '';
      this.birthDateError = '';

      // Check required fields
      if (!this.editableUser.firstName) {
        this.firstNameError = 'Eesnimi on kohustuslik!';
      }
      if (!this.editableUser.lastName) {
        this.lastNameError = 'Perekonnanimi on kohustuslik!';
      }
      if (!this.editableUser.birthDate) {
        this.birthDateError = 'Sünnikuupäev on kohustuslik!';
      }
      if (!this.editableUser.phoneNr) {
        this.phoneNrError = 'Telefoni number on kohustuslik!';
      }
      if (!this.editableUser.email) {
        this.emailError = 'Email on kohustuslik!';
      }
      if (!this.editableUser.county) {
        this.countyError = 'Maakond on kohustuslik!';
      }
      if (!this.editableUser.city) {
        this.cityError = 'Linn on kohustuslik!';
      }
      if (!this.editableUser.streetName) {
        this.streetNameError = 'Tänav on kohustuslik!';
      }
      if (!this.editableUser.streetNr) {
        this.streetNrError = 'Maja ja/või korteri number on kohustuslik!';
      }
      if (!this.editableUser.postalCode) {
        this.postalcodeError = 'Postiindeks on kohustuslik!';
      }
      this.isValidRegions = this.editableUser.regions.length > 0;
      return !this.firstNameError && !this.lastNameError && !this.birthDateError && !this.countyError && !this.cityError && !this.streetNameError && !this.phoneNrError && !this.emailError && !this.streetNrError && !this.postalcodeError && this.isValidRegions;
    },
    async saveChanges() {
      if (!this.validateFields()) {
        alert('Palun täitke kõik kohustuslikud väljad!');
        return;
      }
      try {
        const response = await fetch('api/users/edit/' + this.editableUser.id, {
        //const response = await fetch(`http://localhost:8080/api/users/edit/${this.editableUser.id}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${this.token}`, // Include the token
          },
          body: JSON.stringify(this.editableUser),
        });
        if (response.ok) {
          const updatedUser = await response.json();
          this.$emit('close-popup');
          this.$emit('update-user', updatedUser);
        } else if (response.status === 403) {
          this.handleUnauthorized(response);
        }
      } catch (error) {
        console.error(error);
        alert('Kasutaja muutmine ebaõnnestus: ' + error.message);
      }
    },
    async deleteUser() {
      const isConfirmed = confirm('Kas olete kindel, et soovite selle kasutaja kustutada?');

      if (isConfirmed) {
        try {
          const response = await fetch('api/users/delete/' + this.editableUser.id, {
          //const response = await fetch(`http://localhost:8080/api/users/delete/${this.editableUser.id}`, {
            method: 'DELETE',
            headers: {
              'Authorization': `Bearer ${this.token}`, // Include the token
            },
          });
          if (response.ok) {
            this.$emit('close-popup');
            this.$emit('delete-user', this.editableUser);
          } else if (response.status === 403) {
            this.handleUnauthorized(response);
          }
        } catch (error) {
          console.error(error);
          alert('Kasutaja kustutamine ebaõnnestus: ' + error.message);
        }
      }
    },
    handleUnauthorized(response) {
      console.error('Unauthorized:', response.statusText);
      alert('Teie sessioon on aegunud, palun logige uuesti sisse!');
      this.$store.dispatch('logout');
      this.$router.push('/login');
    },
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

.header {
  background-color: rgba(135, 210, 110);
  border-radius: 10px 10px 0 0; /* Ümarad nurgad pealt */
  padding: 0px; /* Padding, et sisu ei oleks servades */
  display: flex; /* Flex, et joondada sisu */
  justify-content: space-between; /* Ruum pealkirja ja sulgemise nupu vahel */
  align-items: center;
}

.edit-user-popup {
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

.edit-user-popup h2 {
  margin: 20px;
}

.user-details,
.user-address,
.user-contact,
.user-tags-regions-species,
.user-roles {
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 5px;
  margin: 10px;
  box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.1);
}

.form-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 15px;
}

.form-group label {
  margin-bottom: 5px;
}

multiselect {
  width: 100%;
  margin-bottom: 15px;
}


input {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 5px;
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

.buttons {
  display: flex;
  justify-content: space-between;
}

.species-item-element span {
  margin-right: 10px;
  font-weight: bold;
  font-size: large;
}

.error {
  color: red;
}
</style>