<template>
  <body>
  <div class="container">
    <h1 class="form-title">Tule vabatahtlikuks</h1>
    <div class="form-description">
      <p>
        Eesti Metsloomaühing otsib oma ridadesse vabatahtlikke abistajaid, kellele läheb korda lindude ja loomade
        käekäik.
      </p>
      <p>
        Inimesed saavad aidata loomade ja lindude transportimisel. Hoiukodude pakkujad peavad leidma aega hoolealustega
        tegelemiseks.
      </p>
      <p>
        Vabatahtliku suurim töövõit on loodusesse tagasi lastud loom või lind.
      </p>
      <p>
        Kes tunneb, et tahab aidata neid, kes ennast ise aidata ei saa, võivad meiega ühendust võtta selle taotluse
        kaudu.
      </p>
      <p>
        Meil on väga hea meel, et soovite Eesti Metsloomaühingu vabatahtlikega liituda!
      </p>
    </div>
    <h1 class="form-title">Palume täitke järgnev taotlusvorm, et saaksime Teist veidi rohkem teada.</h1>
    <div class="volunteer-form">
      <form @submit.prevent="submitApplication">
        <!-- Name, email, contact fields -->
        <div class="form-group">
          <label for="name">Eesnimi*</label>
          <input type="text" v-model="formData.firstName" id="name" required placeholder="Kirjuta siia..."/>
        </div>

        <div class="form-group">
          <label for="lastName">Perekonnanimi*</label>
          <input type="text" v-model="formData.lastName" id="lastName" required placeholder="Kirjuta siia..."/>
        </div>

        <div class="form-group">
          <label for="phone">Telefon*</label>
          <input type="tel" v-model="formData.phone" id="phone" @blur="validatePhone" required
                 placeholder="Kirjuta siia..."/>
          <span v-if="phoneError" class="error">{{ phoneError }}</span>
        </div>

        <div class="form-group">
          <label for="email">Email*</label>
          <input type="email" v-model="formData.email" id="email" required placeholder="Kirjuta siia..."/>
        </div>

        <div class="form-group">
          <label for="dob">Sünnikuupäev*</label>
          <input type="date" v-model="formData.dob" id="dob" @blur="validateDob" required placeholder="pp.kk.aaaa"/>
          <span v-if="dobError" class="error">{{ dobError }}</span>
        </div>

        <!-- Street field -->
        <div class="form-group">
          <label for="street">Tänav*</label>
          <input type="text" v-model="formData.street" id="street" required placeholder="Kirjuta siia..."/>
        </div>

        <!-- Street number field -->
        <div class="form-group">
          <label for="streetNr">Maja number*</label>
          <input type="text" v-model="formData.streetNr" id="streetNr" required
                 placeholder="Kirjuta siia..."/>
        </div>

        <!-- City field -->
        <div class="form-group">
          <label for="city">Linn*</label>
          <input type="text" v-model="formData.city" id="city" required placeholder="Kirjuta siia..."/>
        </div>

        <!-- Region selector -->
        <div class="form-group">
          <label for="region">Maakond*</label>
          <select v-model="formData.region" id="region" >
            <option disabled value="">Vali maakond</option>
            <option v-for="region in regions" :key="region">{{ region }}</option>
          </select>
        </div>

        <!-- Postal code -->
        <div class="form-group">
          <label for="postalCode">Postiindeks*</label>
          <input type="text" v-model="formData.postalCode" id="postalCode" @blur="validatePostalCode" required
                 placeholder="Kirjuta siia..."/>
          <span v-if="postalCodeError" class="error">{{ postalCodeError }}</span>
        </div>

        <!-- Reason field -->
        <div class="form-group">
          <label for="reason">Miks soovite Eesti Metsloomaühinguga liituda?*</label>
          <textarea v-model="formData.reason" id="reason" required placeholder="Kirjuta siia..."></textarea>
        </div>

        <!-- Help options (Checkboxes) -->
        <div class="form-group">
          <label for="helpOptions">Kuidas meid aidata sooviksid?</label>
          <div class="form-group-checkbox">
            <div v-for="(helpOption, index) in helpOptions" :key="index" class="checkbox-container">
              <label :for="helpOption">{{ helpOption.function }}</label>
              <input type="checkbox" :id="'helpOption-' + index" :value="helpOption" v-model="formData.selectedHelp"/>
            </div>
          </div>
        </div>

        <!-- Experience field -->
        <div class="form-group">
          <label for="experience">Kas kuulud mõnda loomadega tegelevasse organisatsiooni? Kui jah, siis mis
            organisatsiooni?</label>
          <textarea v-model="formData.experience" id="experience" placeholder="Kirjuta siia..."></textarea>
        </div>

        <!-- Password field -->
        <div class="form-group">
          <label for="password">Parool*</label>
          <div class="password-container">
            <input
                :type="showPassword ? 'text' : 'password'"
                v-model="formData.password"
                id="password"
                required
                placeholder="**********"
            />
            <span class="toggle-password" @click="togglePasswordVisibility">
              {{ showPassword ? 'Peida' : 'Näita' }}
            </span>
          </div>
        </div>

        <!-- Confirm Password field -->
        <div class="form-group">
          <label for="confirmPassword">Kinnita parool*</label>
          <input
              :type="showPassword ? 'text' : 'password'"
              v-model="formData.confirmPassword"
              id="confirmPassword"
              @blur="validatePasswordsMatch"
              required
              placeholder="**********"
          />
          <span v-if="passwordError" class="error">{{ passwordError }}</span>
        </div>

        <!-- Confirm sharing personal data -->
        <div class="form-group last-checkbox">
          <label for="shareData">Käesoleva küsitluse täitmisega kinnitan, et luban jagada enda isikuandmeid. Peamiselt
            kasutatakse isikuandmeid loomade abistamise raames kontakti saamiseks.</label>
          <input type="checkbox" v-model="formData.shareData" id="shareData" required/>
        </div>

        <!-- Confirm allowing control visits to home -->
        <div class="form-group last-checkbox">
          <label for="dataPrivacy">Olen teadlik, et Eesti Metsloomaühingu vabatahtlike poolt kogutud andmete ja muude materjalide jagamine kolmandatele osapooltele või avaldamine ilma juhatuse kirjaliku nõusolekuta on keelatud.</label>
          <input type="checkbox" v-model="formData.dataPrivacy" id="dataPrivacy" required/>
        </div>

        <div class="form-group last-checkbox">
          <label for="allowControlVisits">Olen nõus, et Eesti Metsloomaühingu juhatus või nende poolt suunatud inimene teeb seotult hoolealusega kontrollvisiite minu koju.</label>
          <input type="checkbox" v-model="formData.allowControlVisits" id="allowControlVisits" required/>
        </div>

        <!-- Submit button -->
        <button type="submit" class="submit-button" :disabled="formHasErrors">Saada taotlus</button>
      </form>
      <div v-if="showModal" class="modal">
        <div class="modal-content">
          <span class="close" @click="closeModal">&times;</span>
          <h2>Taotlus saadetud!</h2>
          <p>Teie taotlus on edukalt saadetud. Täname, et soovite Eesti Metsloomaühingu vabatahtlikuks tulla!</p>
          <p>Lisaküsimuste korral võtke julgesti ühendust!</p>
          <p>info@metsloom.ee</p>
        </div>
      </div>
    </div>
  </div>
  </body>
</template>

<script>
import { nextTick } from 'vue';
export default {
  name: "RegisterPage",
  data() {
    return {
      formData: {
        firstName: '',
        lastName: '',
        phone: '',
        email: '',
        dob: '',
        street: '',
        streetNr: '',
        city: '',
        region: '',
        postalCode: '',
        reason: '',
        selectedHelp: [],
        experience: '',
        password: '',
        confirmPassword: '',
        shareData: false,
        dataPrivacy: false,
        allowControlVisits: false
      },
      regions: [],
      helpOptions: [],
      showModal: false,
      phoneError: '',
      dobError: '',
      postalCodeError: '',
      formHasErrors: true,
      showPassword: false,
      passwordError: '',
    };
  },
  methods: {
    // validations for some fields
    validatePhone() {
      const phonePattern = /^\+?\d[\d\s]*$/;
      if (!phonePattern.test(this.formData.phone)) {
        this.phoneError = 'Telefoninumber võib sisaldada ainult numbreid!';
      } else {
        this.phoneError = '';
      }
    },
    validateDob() {
      const today = new Date();
      const dob = new Date(this.formData.dob);
      if (dob > today) {
        this.dobError = 'Sünnikuupäev ei saa olla tulevikus!';
      } else {
        this.dobError = '';
      }
      this.checkFormValidity();
    },
    validatePostalCode() {
      const postalCodePattern = /^\d+$/;
      if (!postalCodePattern.test(this.formData.postalCode)) {
        this.postalCodeError = 'Postiindeks peab olema number.';
      } else {
        this.postalCodeError = '';
      }
      this.checkFormValidity();
    },
    checkFormValidity() {
      this.formHasErrors =
          this.phoneError !== '' ||
          this.dobError !== '' ||
          this.postalCodeError !== '' ||
          this.passwordError !== '';
    },
    togglePasswordVisibility() {
      this.showPassword = !this.showPassword;
    },
    validatePasswordsMatch() {
      if (this.formData.password !== this.formData.confirmPassword) {
        this.passwordError = 'Paroolid ei kattu!';
      } else {
        this.passwordError = '';
      }
      this.checkFormValidity();
    },
    async fetchHelpOptions() {
      try {
        //const response = await fetch('http://localhost:8080/api/tags/helpOptions');
        const response = await fetch('api/tags/helpOptions');

        if (response.ok) {
          this.helpOptions = await response.json();
        } else {
          console.error('Error response from server:', response.statusText);
        }
      } catch (error) {
        console.error('Failed to fetch help options:', error);
      }
    },
    async fetchRegions() {
      try {
        //const response = await fetch('http://localhost:8080/api/regions/all');
        const response = await fetch('api/regions/all');

        if (response.ok) {
          this.regions = await response.json();
        } else {
          console.error('Error response from server:', response.statusText);
        }
      } catch (error) {
        console.error('Failed to fetch regions:', error);
      }
    },
    async submitApplication() {
      try {
        const applicationData = {
          firstName: this.formData.firstName,
          lastName: this.formData.lastName,
          phone: this.formData.phone,
          email: this.formData.email,
          dob: this.formData.dob,
          street: this.formData.street,
          streetNr: this.formData.streetNr,
          city: this.formData.city,
          region: this.formData.region,
          postalCode: this.formData.postalCode,
          reason: this.formData.reason,
          experience: this.formData.experience,
          password: this.formData.password
        };
        //const response = await fetch('http://localhost:8080/api/applications/addApplication', {
        const response = await fetch('api/applications/addApplication', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(applicationData)
        });
        if (response.status === 409) {
          alert("Sellise e-posti aadressiga kasutaja on juba olemas!");
          return;
        }
        if (response.ok) {
          const application = await response.json();

          const tagsData = this.formData.selectedHelp.map(helpOption => ({
            application: application,
            tag: helpOption
          }));

          const tagsResponse = await fetch('api/applicationToTags/addApplicationToTag', {
          //const tagsResponse = await fetch('http://localhost:8080/api/applicationToTags/addApplicationToTag', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(tagsData)
          });

          if (tagsResponse.ok) {
            this.showModal = true;
            await nextTick();

            // Scroll to modal once it has been displayed
            const modal = this.$el.querySelector('.modal');
            if (modal) {
              window.scrollTo({
                top: modal.offsetTop,
                behavior: 'smooth'
              });
            }
          }

          this.formData = {
            firstName: '',
            lastName: '',
            phone: '',
            email: '',
            dob: '',
            street: '',
            streetNr: '',
            city: '',
            region: '',
            postalCode: '',
            reason: '',
            selectedHelp: [],
            experience: '',
            password: '',
            confirmPassword: '',
            shareData: false,
            allowControlVisits: false,
            dataPrivacy: false,
          };
        } else {
          alert("Viga! Taotluse saatmisel tekkis viga, palun proovige hiljem uuesti.")
        }
      } catch (error) {
        alert("Viga! Taotluse saatmisel tekkis viga, palun proovige hiljem uuesti.")
      }
    },
    closeModal() {
      this.showModal = false;
    }
  },
  created() {
    this.fetchHelpOptions();
    this.fetchRegions();
  }
};
</script>
<style scoped>

.volunteer-form {
  background-color: #C8ECBD;
  padding: 40px;
  border-radius: 10px;
  width: 50%;
  margin: 0 auto;
  box-sizing: border-box;
  min-width: 400px;
}

.form-title {
  text-align: center;
  font-size: 24px;
  color: #4A4A4A;
}

.form-description {
  font-size: 18px;
  width: 60%;
  margin: 0 auto;
  padding: 20px;
  line-height: 1.6;
  color: #4A4A4A;
  text-align: center;
}

.form-group {
  margin-bottom: 20px;
  width: 100%;
}

.form-group label {
  display: block;
  text-align: left;
  margin-bottom: 10px;
  font-weight: bold;
  font-size: 18px;
}

.form-group input,
.form-group select,
.form-group textarea {
  width: 100%;
  padding: 12px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 16px;
  box-sizing: border-box;
  font-family: 'Inria Serif', serif;
}

.checkbox-group input {
  margin-right: 10px;
}

.password-container {
  display: flex;
  align-items: center;
  border: 1px solid #ccc; /* Border for the container */
  border-radius: 4px; /* Rounded corners for aesthetics */
  padding: 4px 8px; /* Space inside the container */
  background-color: #fff; /* White background */
}

.password-container input {
  flex: 1; /* Allow the input to take up remaining space */
  border: none; /* Remove the default border */
  outline: none; /* Remove the focus outline */
  padding: 8px; /* Add padding to the input field */
}

.password-container .toggle-password {
  margin-left: 8px; /* Space between the input and the toggle text */
  color: #75A9CF; /* Text color for the toggle */
  cursor: pointer; /* Pointer cursor for interactivity */
  font-size: 14px; /* Font size of the toggle text */
  text-decoration: underline; /* Underline for the toggle text */
}

.password-container .toggle-password:hover {
  text-decoration: none; /* Remove underline on hover */
}

.last-checkbox {
  display: flex;
  justify-content: flex-start; /* Adjust to align items at the start */
  align-items: flex-start;
  margin-bottom: 15px;
  flex-wrap: wrap;
}

.last-checkbox label {
  flex: 1;
  text-align: left;
  line-height: 1.5;
  margin-right: 10px;
}

.last-checkbox input {
  flex: 0;
  align-self: flex-start;
}

@media (max-width: 600px) {
  .last-checkbox {
    flex-direction: column; /* Stack items vertically on small screens */
    align-items: flex-start;
  }

  .last-checkbox label {
    margin-bottom: 5px; /* Add some space between label and checkbox */
  }

  .last-checkbox input {
    margin-top: 5px; /* Optional: add space between checkbox and the label */
  }
}

.checkbox-container input[type="checkbox"] {
  appearance: none; /* Reset default styles */
  width: 20px; /* Set size */
  height: 20px;
  border: 2px solid #333;
  border-radius: 4px;
  background-color: white;
  position: relative;
}

.checkbox-container input[type="checkbox"]:checked::before {
  content: "✔"; /* You can customize the checkmark */
  position: absolute;
  left: 3px;
  top: -2px;
  font-size: 18px;
  color: #333;
}
.last-checkbox input[type="checkbox"] {
  appearance: none; /* Reset default styles */
  width: 20px; /* Set size */
  height: 20px;
  border: 2px solid #333;
  border-radius: 4px;
  background-color: white;
  position: relative;
}

.last-checkbox input[type="checkbox"]:checked::before {
  content: "✔"; /* You can customize the checkmark */
  position: absolute;
  left: 3px;
  top: -2px;
  font-size: 18px;
  color: #333;
}

.form-group-checkbox {
  background-color: white;
  padding: 15px;
  border-radius: 10px;
  margin-bottom: 20px;
}

.checkbox-container {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.checkbox-container label {
  flex: 1;
  text-align: left;
}

.checkbox-container input {
  flex: 0;
}

.submit-button {
  background-color: #87D26E;
  padding: 20px 60px;
  border-radius: 15px;
  border: none;
  cursor: pointer;
  font-size: 24px;
  display: block;
  margin: 0 auto;
}

.submit-button:hover {
  background-color: #89d970;
}

.modal {
  background-color: #FBFFFB;
  margin-top: 20px;
  padding: 20px;
  border-radius: 10px;
}

input[type="checkbox"] {
  all: unset;
  display: inline-block !important;
  visibility: visible !important;
  opacity: 1 !important;
}

.error {
  color: red;
}
</style>