<template>
  <div class="welcome">
    <div class="info-details">
      <div class="photo">
        <img src="@/assets/welcome-image.jpg" alt="About Page Picture" />
      </div>
      <div class="text">
        <h1>Tere tulemast!</h1>
        <p>Eesti Metsloomaühing on suurim ja tuntuim metsloomi abistav organisatsioon Eestis.</p>
        <p>Meie missiooniks on peamiselt linnakeskkonnas või ka mujal inimtegevuse tõttu hätta sattunud metsloomade abistamine ning vabastamine loomulikku elukeskkonda.</p>
        <p>Meie lehel saad teavitada meid metsloomadest, kes vajavad abi.</p>
        <p>Teavita meid abivajavast loomast:</p>
        <button @click="start">Alusta</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "WelcomePage",
  methods: {
    start() {
      this.$router.push("/alert");
    }
  }
};
</script>

<style scoped>
.welcome {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  padding: 30px 30px 100px;
  box-sizing: border-box;
}

h1 {
  font-size: 2.5em;
  margin-bottom: 20px;
}

.info-details {
  display: flex;
  align-items: center;
  justify-content: center;
  max-width: 1000px;
  width: 87%;
}

.photo img {
  margin-right: 20px;
  border-radius: 10px;
  max-height: 500px;
}

.text {
  text-align: center;
  max-height: 500px;
  border-radius: 10px;
  box-shadow: #4A4A4A;
  padding: 20px;
}

p {
  font-size: 1.2em;
  margin-bottom: 30px;
}

button {
  background-color: rgba(135, 210, 110, 0.8);
  border: none;
  border-radius: 10px;
  padding: 10px 20px;
  font-family: 'Inria Serif', serif;
  font-size: 1.2em;
  cursor: pointer;
  transition: background-color 0.3s;
}

@media (max-width: 768px) {
  .info-details, .welcome {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }

  .photo img {
    max-width: 80%;
    max-height: 300px;
    margin-bottom: 20px;
  }
}
</style>