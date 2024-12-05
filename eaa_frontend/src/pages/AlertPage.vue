<template>
  <div class="alert">
    <h1 class="form-title">Teavitusvorm</h1>

    <p class="form-description">Kui soovid meile teada anda õnnetusjuhtumist või mõnest muust probleemist,
      siis võta meiega julgelt läbi allpool oleva vormi ühendust!</p>

    <div class="volunteer-form">
      <form @submit.prevent="submitAlert">

        <!-- What happened field -->
        <div class="form-group">
          <label for="reason">Mis on juhtunud?*</label>
          <textarea id="reason" v-model="formData.reason" required placeholder="Kirjuta siia võimalikult täpselt, mis on juhtunud või millest soovid teada anda..."></textarea>
        </div>

        <!-- What animal? -->
        <div class="form-group">
          <label for="animals">Kellega juhtus?*</label>
          <input type="text" id="animals" v-model="formData.animals" required placeholder="Kirjuta siia, millise looma või linnuga tegu võib olla..."/>
        </div>

        <!-- Pictures-->
        <div class="form-group">
          <label for="pictures">Pildid</label>
          <input type="file" id="pictures" multiple accept=".jpeg,.jpg" @change="handleFileUpload"/>
        </div>

        <!-- Where (map)?-->
        <div class="form-group">
          <label for="where">Kus juhtus?*</label>
          <div class="toggle-buttons">
            <button type="button" @click="autoDetectLocation">Tuvasta asukoht automaatselt</button>
            <button type="button" @click="manualInputLocation">Sisesta asukoht käsitsi</button>
          </div>
          <input type="text" v-if="showManualInput" id="location" v-model="formData.location"
                 placeholder="Kirjuta siia asukoht..."/>
          <div id="address" v-if="address">{{ address }}</div>
        </div>

        <!-- Map iframe, shown only when showManualInput is false -->
        <div v-if="showMap" class="map-container">
          <iframe
              :src="'https://maps.google.com/maps?q=' + formData.latitude + ',' + formData.longitude + '&t=&z=15&ie=UTF8&iwloc=&output=embed'"
              width="500" height="300">
          </iframe>
        </div>

        <!-- Tühi rida, kuvatakse ainult kui map ka on. Nii on ilusam -->
        <div v-if="showMap" style="height: 20px;"></div>

        <!-- Directions specified?-->
        <div class="form-group">
          <label for="directions">Asukoha juhised?</label>
          <textarea id="directions" v-model="formData.directions" placeholder="Kirjuta siia täpsustavad juhised, kuidas sündmuskohale jõuda..."></textarea>
        </div>

        <!-- Name, email, contact fields -->
        <div class="form-group">
          <label for="name">Eesnimi</label>
          <input type="text" id="name" v-model="formData.name" placeholder="Kirjuta siia..."/>
        </div>

        <div class="form-group">
          <label for="lastName">Perekonnanimi</label>
          <input type="text" id="lastName" v-model="formData.lastName" placeholder="Kirjuta siia..."/>
        </div>

        <div class="form-group">
          <label for="phone">Telefon</label>
          <input type="tel" id="phone" v-model="formData.phone" @blur="validatePhone"
                 placeholder="Kirjuta siia..."/>
          <span v-if="phoneError" class="error">{{ phoneError }}</span>
        </div>

        <div class="form-group">
          <label for="email">Email</label>
          <input type="email" id="email" v-model="formData.email" @blur="validateEmail"
                 placeholder="Kirjuta siia..."/>
          <span v-if="emailError" class="error">{{ emailError }}</span>
        </div>

        <div class="form-group">
          <label for="socialMedia">Sotsiaalmeedia kontakt</label>
          <input type="text" id="socialMedia" v-model="formData.socialMedia"
                 placeholder="Kirjuta siia..."/>
        </div>

        <div v-if="contactError" class="error">
          Palun täitke vähemalt üks järgmistest väljadest: Telefon, Email või Sotsiaalmeedia kontakt.
        </div>

        <p></p>

        <!-- Can wait at location? -->
        <div class="form-group">
          <label for="waiting">Kas asute sündmuskoha läheduses tänase päeva jooksul?</label>
          <div class="toggle-buttons">
            <button
                type="button"
                :class="{ active: canWait }"
                @click="setCanWait(true)"
            >Jah
            </button>
            <button
                type="button"
                :class="{ active: !canWait }"
                @click="setCanWait(false)"
            >Ei
            </button>
          </div>
        </div>

        <!-- Submit button -->
        <button type="submit" class="submit-button">Saada teavitus</button>

      </form>

      <div v-if="showModal" class="modal">
        <div class="modal-content">
          <span class="close" @click="closeModal">&times;</span>
          <h2>Teavitus saadetud!</h2>
          <p>Teie teavitus on edukalt saadetud.</p>
          <p>Kui soovite lisainfot jagada, võtke julgesti ühendust!</p>
          <p>+372 5632 2200</p>
          <p>info@metsloom.ee</p>
        </div>
      </div>


    </div>
  </div>
</template>

<script>
import {nextTick} from "vue";

export default {
  name: "AlertPage",

  data() {
    return {
      formData: {
        reason: '',
        animals: '',
        location: '',
        directions: '',
        name: '',
        lastName: '',
        phone: '',
        email: '',
        latitude: null,
        longitude: null,
        socialMedia: '',
      },
      // Vaikimisi on väljad enamasti tühjad või false
      canWait: false,
      showModal: false,
      address: '',
      uploadedImages: [],
      phoneError: '',
      emailError: '',
      formHasErrors: false,
      showManualInput: false,
      showMap: false,
      contactError: false,
      isResetting: false,
    }
  }, //data
  watch: {
    'formData.phone'() {
      if (!this.isResetting) this.validateContactFields();
    },
    'formData.email'() {
      if (!this.isResetting) this.validateContactFields();
    },
    'formData.socialMedia'() {
      if (!this.isResetting) this.validateContactFields();
    },
  },
  methods: {
    validatePhone() {
      const phonePattern = /^\+?\d[\d\s]*$/;
      if (this.formData.phone && !phonePattern.test(this.formData.phone)) {
        this.phoneError = 'Telefoninumber võib sisaldada ainult numbreid!';
      } else {
        this.phoneError = '';
      }
      this.checkFormValidity();
    },

    validateEmail() {
      if (this.formData.email && !this.formData.email.includes('@')) {
        this.emailError = 'E-post peab sisaldama "@" sümbolit.';
      } else {
        this.emailError = '';
      }
      this.checkFormValidity();
    },
    validateContactFields() {
      if (this.isResetting) return;
      this.contactError = !this.formData.phone && !this.formData.email && !this.formData.socialMedia;
    },

    checkFormValidity() {
      // Kontrollib, kas kõik nõutavad väljad on täidetud
      this.validateContactFields();
      this.formHasErrors =
          this.phoneError ||
          this.emailError ||
          this.formData.reason === '' ||
          this.formData.animals === '' ||
          this.contactError;
    },

    manualInputLocation() {
      this.showMap = false;
      this.showManualInput = true;
      this.address = '';
    },

    autoDetectLocation() {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition((position) => {
          const lat = position.coords.latitude;
          const lon = position.coords.longitude;
          this.formData.latitude = lat;
          this.formData.longitude = lon;
          this.getAddress(lat, lon);
          this.showManualInput = false; // Peida tekstikast, kui asukoht on automaatselt tuvastatud
          this.showMap = true;
        }, () => {
          alert('Ei suuda teie asukohta tuvastada.');
        });
      } else {
        alert('Geolokatsioon ei ole selle brauseri poolt toetatud.');
      }
    },

    async getAddress(lat, lon) {
      let apiKey = '';
      try {
        //const response = await fetch('http://localhost:8080/api/get-key');
        const response = await fetch('api/get-key');
        if (response.ok) {
          apiKey = await response.text();
        }
      } catch (error) {
        console.error(' Failed to fetch apikey', error);
      }
      const url = `https://eu1.locationiq.com/v1/reverse?lat=${lat}&lon=${lon}&key=${apiKey}&format=json`;
      fetch(url)
          .then(response => response.json())
          .then(data => {
            if (data.display_name) {
              this.address = 'Automaatselt tuvastatud asukoht: ' + data.display_name;
            } else {
              this.address = 'Aadress ei leitud.';
            }
          })
          .catch(error => {
            console.error('Viga aadressi toomisel:', error);
            this.address = 'Viga aadressi saamisel.';
          });
    },
    closeModal() {
      this.showModal = false; // Close the modal
    },

    setCanWait(value) {
      this.canWait = value;
    },


    async submitAlert() {
      this.checkFormValidity();
      if (this.formHasErrors) {
        alert('Palun kontrollige vormi ja täitke vajalikud väljad.');
        return;
      }
      try {
        const alertData = {
          reason: this.formData.reason,
          animals: this.formData.animals,
          location: this.address || this.formData.location, // Võtab automaatselt tuvastatud aadressi või käsitsi sisestatud asukoha
          directions: this.formData.directions,
          firstName: this.formData.name,
          lastName: this.formData.lastName,
          phone: this.formData.phone,
          email: this.formData.email,
          canWait: this.canWait,
          pictures: [],
          latitude: this.formData.latitude,
          longitude: this.formData.longitude,
          socialMedia: this.formData.socialMedia,
        };


        //const response = await fetch('http://localhost:8080/api/tickets/add', {
        const response = await fetch('api/tickets/add', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(alertData)
        });

        if (response.ok) {
          const result = await response.json();
          this.showModal = true; // Kuvab modaalakna eduka saatmise korral

          await nextTick();

          const modal = this.$el.querySelector('.modal');
          if (modal) {
            window.scrollTo({
              top: modal.offsetTop,
              behavior: 'smooth'
            });
          }
          // Piltide üleslaadimise ja ID-de saamine
          alertData.pictures = await this.uploadImages(result.id); // Lisa piltide ID-d alertData objektile
          // Vorm tühjendatakse
          this.isResetting = true;
          this.formData = {
            reason: '',
            animals: '',
            location: '',
            directions: '',
            name: '',
            lastName: '',
            phone: '',
            email: '',
            canWait: false,
            socialMedia: '',
          };
          this.phoneError = "";
          this.emailError = "";
          this.contactError = false;
          this.formHasErrors = false;
          this.uploadedImages = [];
          //Timeout, et vorm tühjendataks enne kui erroreid hakatakse kontrollima
          setTimeout(() => {
            this.isResetting = false;
          }, 500);
        } else if (response.status === 409) {
          alert('Viga! Sarnane teavitus on juba olemas.');
        } else {
          const errorResponse = await response.text();
          alert('Viga! Teavituse saatmisel tekkis probleem: ' + errorResponse);
        }
      } catch (error) {
        console.error('Error submitting alert:', error);
        alert('Viga! Teavituse saatmisel tekkis viga, palun proovige hiljem uuesti.');
      }
    },


    async handleFileUpload(event) {
      const files = event.target.files;
      const allowedTypes = ["image/jpeg", "image/heif", "image/heic", "image/png", "video/mp4", "video/x-matroska", "video/webm"];
      const validFiles = [];

      for (const element of files) {
        const file = element;
        if (allowedTypes.includes(file.type)) {
          validFiles.push(file);
        } else {
          alert(`${file.name} ei ole lubatud failiformaadis! Palun laadige üles ainult JPEG, HEIF, PNG, MP4, MKV või WebM faile.`);
        }
      }
      // Salvesta valideeritud failid
      this.uploadedImages = validFiles;

    },

    async uploadImages(ticketId) {
      const uploadedImageIds = [];

      try {
        // Hangi folderId serverist
        //const folderIdResponse = await fetch('http://localhost:8080/api/get-folderId');
        const folderIdResponse = await fetch('api/get-folderId');
        if (!folderIdResponse.ok) {
          throw new Error('Failed to fetch folder ID');
        }
        const folderId = await folderIdResponse.text();

        // Failide üleslaadimine
        for (let i = 0; i < this.uploadedImages.length; i++) {
          const file = this.uploadedImages[i];
          const formData = new FormData();
          const date = new Date();

          // Genereeri uus failinimi
          const newFileName = `alert_${date.getFullYear()}-${(date.getMonth() + 1)
              .toString()
              .padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}_${date
              .getHours()
              .toString()
              .padStart(2, '0')}-${date.getMinutes().toString().padStart(2, '0')}-${date
              .getSeconds()
              .toString()
              .padStart(2, '0')}_${i + 1}`;

          formData.append('file', file, newFileName);

          // Lisa folderId POST-päringusse
          formData.append('folderId', folderId);

          // Lisa ticketId POST-päringusse
          formData.append('ticketId', ticketId);

          // Faili üleslaadimine
          try {
            const response = await fetch('/api/uploadToDrive', {
            //const response = await fetch('http://localhost:8080/api/uploadToDrive', {
              method: 'POST',
              body: formData,
            });
            if (response.status === 413) {
              // Kui server vastab veakoodiga 413
              alert("Fail " + file.name + " on liiga suur! Teie teade on saadetud edasi, kuid palun saatke pildid e-posti teel: metsloompildid@gmail.com");
            } else if (!response.ok) {
              alert("Faili " + file.name + " üleslaadimisel tekkis viga!");
            } else {
              const data = await response.json();
              uploadedImageIds.push(data.fileId);
            }
          } catch (error) {
            console.error("Error uploading file:", error);
          }
        }
      } catch (error) {
        console.error('Error:', error);
      }

      return uploadedImageIds; // Tagasta üleslaaditud failide ID-d
    }

  },
};

</script>

<style scoped>
.alert {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
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

.volunteer-form {
  background-color: #C8ECBD;
  padding: 40px;
  border-radius: 10px;
  width: 50%;
  margin: 0 auto;
  box-sizing: border-box;
  min-width: 400px;
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

.submit-button {
  background-color: #87D26E;
  padding: 20px 60px;
  border-radius: 15px;
  border: none;
  cursor: pointer;
  font-size: 20px;
  display: block;
  margin: 0 auto;
}

.submit-button:hover {
  background-color: #89d970;
}

.toggle-buttons {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.toggle-buttons button {
  padding: 10px 20px;
  border-radius: 25px;
  border: none;
  cursor: pointer;
  background-color: #ffffff;
  color: #4A4A4A;
  font-size: 16px;
  transition: background-color 0.3s, color 0.3s;
  margin-top: 10px;
  margin-bottom: 40px;
}

.toggle-buttons button.active {
  background-color: #89d970;
  color: #fff;
}

.toggle-buttons button:not(.active) {
  background-color: white;
  color: black;
}

.toggle-buttons button:hover {
  background-color: #e6f7e3;
}

.error {
  color: red;
  font-weight: bold;
}

.form-group input[type="file"] {
  background-color: white;
  border: 2px solid #4A4A4A;
  padding: 10px;
  font-size: 16px;
  width: 100%;
  border-radius: 5px;
  box-sizing: border-box;
}

.modal {
  background-color: #FBFFFB;
  margin-top: 20px;
  padding: 20px;
  border-radius: 10px;
}

</style>