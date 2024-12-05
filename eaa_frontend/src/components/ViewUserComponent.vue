<template>
  <teleport to="body">
    <div class="view-user-popup">
      <div class="header">
        <h2>{{ localUser.firstName }} {{ localUser.lastName }}</h2>

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
          <label for="firstName"><strong>Eesnimi: </strong></label>
          <span>{{ localUser.firstName }}</span>
        </div>

        <div class="form-group">
          <label for="lastName"><strong>Perekonnanimi: </strong></label>
          <span>{{ localUser.lastName }}</span>
        </div>

        <div class="form-group">
          <label for="birthDate"><strong>Sünnikuupäev: </strong></label>
          <span>{{ localUser.birthDate }}</span>
        </div>

        <div class="form-group">
          <label for="createdDate"><strong>Liitumiskuupäev: </strong></label>
          <span>{{ new Date(localUser.createdAt).toLocaleDateString() }}</span>
        </div>
      </div>

      <div class="user-roles">
        <h3>Rollid</h3>
          <div v-for="role in localUser.roles" :key="role.id" class="tag-or-region role">
            {{ role }}
          </div>
      </div>

      <div class="user-contact">
        <h3>Kontakt</h3>
        <div class="form-group">
          <label for="phoneNr"><strong>Telefon: </strong></label>
          <span>{{ localUser.phoneNr }}</span>
        </div>

        <div class="form-group">
          <label for="email"><strong>Email: </strong></label>
          <span>{{ localUser.email }}</span>
        </div>
      </div>

      <div class="user-address">
        <h3>Asukoha info</h3>
        <div class="form-group">
          <label for="county"><strong>Maakond: </strong></label>
          <span>{{ localUser.county }}</span>
        </div>

        <div class="form-group">
          <label for="city"><strong>Linn: </strong></label>
          <span>{{ localUser.city }}</span>
        </div>

        <div class="form-group">
          <label for="address"><strong>Aadress: </strong></label>
          <span>{{ localUser.streetName }} {{ localUser.streetNr }}, {{ localUser.postalCode }}</span>
        </div>
        </div>

      <div class="user-tags-regions-species">
        <h3>Funktsioonid</h3>
        <div v-for="tag in localUser.tags" :key="tag.id" class="tag-or-region tag">
          {{ tag }}
        </div>
        <h3>Regioonid</h3>
        <div v-for="region in localUser.regions" :key="region.id" class="tag-or-region region">
          {{ region }}
        </div>
        <h3>Liigigrupid</h3>
        <div v-for="species in localUser.species" :key="species.id" class="tag-or-region species" :class="{'expert-tag': species.expert}">
          <span v-if="species.expert" class="expert-icon">⭐ Ekspert: </span>
          <span>{{ species.name }}</span>
        </div>
      </div>

      <button @click="$emit('close-popup')">Sulge</button>
    </div>
  </teleport>
</template>

<script>
export default {
  name: "ViewUser",
  emits: ["close-popup"],
  props: {
    user: Object,
  },
  data() {
    return {
      localUser: { ...this.user }, // Create a copy from the prop
    };
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
  border-radius: 10px 10px 0 0; /* Rounded corners on the top */
  padding: 0px; /* Padding to avoid content touching edges */
  display: flex; /* Flex to align content */
  justify-content: space-between; /* Space between title and close button */
  align-items: center;
}
.view-user-popup {
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

.view-user-popup h2 {
  margin: 20px;
}

/* User details */
.user-details,
.user-address,
.user-contact,
.user-tags-regions-species,
.user-roles{
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 5px;
  margin: 10px;
  box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.1);
}

.tag-or-region {
  background-color: #D6F4CD;
  border-radius: 15px;
  padding: 10px 20px;
  margin: 5px;
  font-size: 1.1em;
  box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.15);
  display: inline-block;
}

.expert-tag {
  background-color: #efe291;
  font-weight: bold;
}

.expert-icon {
  margin-right: 10px;
}

.form-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 15px;
}

.form-group label {
  margin-bottom: 5px;
}

span {
  color: #333;
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
