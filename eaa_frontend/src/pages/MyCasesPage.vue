<template>
  <div class="ticket-management">
    <h1>Loomade juhtumite ülevaade</h1>

    <div class="filters-section">
      <div class="search">
        <input
            type="text"
            v-model="searchTerm"
            placeholder="Otsi looma järgi..."
            @input="filterTickets"
        />
      </div>

      <div class="status-filters">
        <button
            @click="toggleStatus('Kõik')"
            :class="{ active: selectedStatuses.includes('Kõik') }"
            class="status-all"
        >
          Kõik ({{ countTicketsByStatus("Kõik") }})
        </button>
        <button
            @click="toggleStatus('Hoiukodus')"
            :class="{ active: selectedStatuses.includes('Hoiukodus') }"
            class="status-in-progress"
        >
          Hoiukodus ({{ countTicketsByStatus("Hoiukodus") }})
        </button>
        <button
            @click="toggleStatus('Lõpetatud')"
            :class="{ active: selectedStatuses.includes('Lõpetatud') }"
            class="status-completed"
        >
          Lõpetatud ({{ countTicketsByStatus("Lõpetatud") }})
        </button>
      </div>
    </div>

    <EditTicket
        v-if="isEditing"
        :ticket="selectedTicket"
        :pseudo-id="pseudoIdMap[selectedTicket.id]"
        :users="users"
        @update-ticket="handleTicketUpdate"
        @close-popup="isEditing = false"
        @delete-ticket="deleteTicket"
    />

    <ViewTicket
        v-if="isViewing"
        :ticket="selectedTicket"
        :pseudo-id="pseudoIdMap[selectedTicket.id]"
        :users="users"
        @close-popup="isViewing = false"
    />

    <div class="ticket-list">
      <div v-if="filteredTickets.length === 0">
        <h3>Ühtegi piletit ei ole saadaval.</h3>
      </div>
      <div
          v-for="ticket in filteredTickets"
          :key="ticket.id"
          :class="['ticket-card', statusClass(ticket.status.name)]"
          @click.prevent="openViewTicket(ticket)"
      >
        <div class="edit-link">
          <button
              v-if="ticket.status.name !== 'Lõpetatud'"
              @click.stop.prevent="editTicket(ticket)"
              class="edit-button"
          >
            Muuda
          </button>
          <button
              @click.stop.prevent="openViewTicket(ticket)"
              class="view-button"
          >
            Vaata
          </button>
          <button
              @click.stop.prevent="copyTicketInfo(ticket)"
              class="copy-button"
          >
            Kopeeri
          </button>
        </div>

        <div class="ticket-info">
          <div class="ticket-animal">
            <div class="animal-name">
              #{{ pseudoIdMap[ticket.id] }} - {{ ticket.species.name }}
            </div>
            <div :class="['ticket-status-tag', statusClass(ticket.status.name)]">
              {{ ticket.status.name }}
            </div>
          </div>
          <div class="ticket-details">
            <p>
              <strong>Asukoht:</strong> {{ ticket.location }};
            </p>
            <p>
              <strong>Lisatud:</strong>
              {{ new Date(ticket.createdAt).toLocaleDateString() }}
            </p>
            <p>
              <strong>Teavitaja:</strong> {{ ticket.reporterName }},
              {{ ticket.reporterPhone }}, {{ ticket.reporterEmail }}, {{ ticket.reporterSocialMedia }}
            </p>
            <p>
              <strong>Vabatahtlik(ud):</strong>
            </p>
            <ul>
              <li v-for="user in ticket.users" :key="user.id">
                {{ user.firstName }} {{ user.lastName }}
              </li>
            </ul>
          </div>
          <div class="ticket-tags">
            <span v-if=ticket.upperSpecies class="tag"> {{ ticket.upperSpecies.name }}</span>
            <span v-if=ticket.region class="tag">{{ ticket.region.name }}</span>
            <div v-for="tag in ticket.tags" :key="tag.id" class="tag">
              {{ tag }}
            </div>
            <span v-if=ticket.resolution class="tag"> {{ ticket.resolution.name }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import EditTicket from "@/components/EditMyTicketComponent.vue";
import ViewTicket from "@/components/ViewTicketComponent.vue";
import "vue-multiselect/dist/vue-multiselect.min.css";

export default {
  name: "TicketManagement",
  components: {
    EditTicket,
    ViewTicket,
  },
  data() {
    return {
      tickets: [],
      filteredTickets: [],
      searchTerm: "",
      isEditing: false,
      isViewing: false,
      selectedTicket: null,
      users: [],
      tags: [],
      volunteers: [],
      species: [],
      upperSpecies: [],
      resolutions: [],
      statuses: [],
      selectedStatuses: [],
      alertShown: false,
      pseudoIdMap: {},
    };
  },
  async created() {
    await this.fetchTickets();
    this.filterTickets();
  },
  watch: {
    searchTerm: "filterTickets",
    selectedStatuses: "filterTickets",
  },
  methods: {
    getAuthToken() {
      return localStorage.getItem('token'); // Get the token from local storage
    },
    async fetchUsersForTicket(ticketId) {
      const token = this.getAuthToken();
      //const response = await fetch(`http://localhost:8080/api/ticketToUsers/${ticketId}/users`, {
      const response = await fetch(`api/ticketToUsers/${ticketId}/users`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        }
      });

      if (!response.ok) {
        if (response.status === 403) {
          this.handleUnauthorized();
        } else {
          console.error(`Failed to fetch users for ticket ID ${ticketId}`);
        }
        return [];
      }
      return response.json();
    },
    async fetchTickets() {
      const UserID = this.$store.state.user.id;
      const token = this.getAuthToken();
      //const response = await fetch('http://localhost:8080/api/ticketToUsers/all/' + UserID, {
      const response = await fetch('api/ticketToUsers/all/' + UserID, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        }
      });

      if (!response.ok) {
        if (response.status === 403) {
          this.handleUnauthorized();
        } else {
          console.error(`Failed to fetch data`);
        }
        return null;
      }
      if (response.status === 204) return null;

      const tickets = await response.json();

      // Fetch users and tags for each ticket
      for (const ticket of tickets) {
        ticket.users = await this.fetchUsersForTicket(ticket.id);

        // Fetch tags for each ticket
        //const tagsResponse = await fetch(`http://localhost:8080/api/tickets/${ticket.id}/animalTags`, {
        const tagsResponse = await fetch(`api/tickets/${ticket.id}/animalTags`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          }
        });

        if (tagsResponse.ok) {
          ticket.tags = await tagsResponse.json();
        } else {
          ticket.tags = [];
        }

        // Fetch images for each ticket
        //const imagesResponse = await fetch(`http://localhost:8080/api/picturesToTickets/all/${ticket.id}`, {
        const imagesResponse = await fetch(`api/picturesToTickets/all/${ticket.id}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          }
        });
        if (imagesResponse.ok) {
          ticket.images = await imagesResponse.json();
        } else {
          ticket.images = [];
        }
      }
      this.tickets = tickets.filter(ticket =>
          ticket.status.name !== "Uus" && ticket.status.name !== "Avatud"
      );
      this.pseudoIdMap = this.tickets.reduce((acc, ticket, index) => {
        acc[ticket.id] = index + 1;
        return acc;
      }, {});

      this.filterTickets();
    },
    handleUnauthorized() {
      if (this.alertShown) return;
      alert('Teie sessioon on aegunud, palun logige uuesti sisse!');
      this.$store.dispatch('logout');
      this.$router.push('/login');
      this.alertShown = true;
    },
    filterTickets() {
      let filtered = this.tickets;

      if (this.searchTerm.trim() !== "") {
        filtered = filtered.filter((ticket) =>
            ticket.species.name
                .toLowerCase()
                .includes(this.searchTerm.toLowerCase())
        );
      }

      if (
          this.selectedStatuses.length > 0 &&
          !this.selectedStatuses.includes("Kõik")
      ) {
        filtered = filtered.filter((ticket) =>
            this.selectedStatuses.includes(ticket.status.name)
        );
      }

      // Sort tickets by status first, then by date within each status
      this.filteredTickets = this.sortAndDateSortTickets(filtered);
    },

// Helper function to sort by status, then by date
    sortAndDateSortTickets(tickets) {
      return tickets.sort((a, b) => {
        // First, sort by status
        const statusComparison = this.compareStatuses(a.status.name, b.status.name);

        // If statuses are the same, sort by dateSubmitted (newer dates first)
        if (statusComparison === 0) {
          return new Date(b.createdAt) - new Date(a.createdAt);
        }
        return statusComparison;
      });
    },

    // meetod staatuse arvu saamiseks
    countTicketsByStatus(status) {
      if (status === "Kõik") {
        return this.tickets.length;
      }

      return this.tickets.filter((ticket) => ticket.status.name === status)
          .length;
    },
    toggleStatus(status) {
      const index = this.selectedStatuses.indexOf(status);
      if (index > -1) {
        // Kui staatus on juba valitud, eemalda see
        this.selectedStatuses.splice(index, 1);
      } else {
        // Muul juhul lisa see valitud staatuste hulka
        this.selectedStatuses.push(status);
      }

      // Kui "Kõik" on valitud, eemaldame kõik teised
      if (status === "Kõik") {
        this.selectedStatuses = ["Kõik"];
      } else if (this.selectedStatuses.includes("Kõik")) {
        // Kui "Kõik" on juba valitud, siis eemaldame selle
        this.selectedStatuses = this.selectedStatuses.filter(
            (s) => s !== "Kõik"
        );
      }

      this.filterTickets();
    },
    compareStatuses(statusA, statusB) {
      const statusPriority = {
        "Uus": 1,
        "Avatud": 2,
        "Hoiukodus": 3,
        "Lõpetatud": 4,
      };

      return (statusPriority[statusA] || 99) - (statusPriority[statusB] || 99);
    },
    handleTicketUpdate(updatedTicket) {
      const index = this.tickets.findIndex(
          (ticket) => ticket.id === updatedTicket.id
      );
      if (index !== -1) {
        this.tickets[index] = updatedTicket;
      }
      this.fetchTickets();
      alert('Pilet on edukalt muudetud!');
      this.isEditing = false;
    },
    editTicket(ticket) {
      this.selectedTicket = {
        ...ticket,
        species: ticket.species.name,
        upperSpecies: ticket.upperSpecies.name,
        status: ticket.status.name,
        resolution: ticket.resolution ? ticket.resolution.name : ticket.resolution,
        volunteers: ticket.users,
      };
      this.isEditing = true;
      this.isViewing = false;
    },
    openViewTicket(ticket) {
      this.selectedTicket = {
        ...ticket,
        species: ticket.species.name,
        upperSpecies: ticket.upperSpecies.name,
        status: ticket.status.name,
        resolution: ticket.resolution ? ticket.resolution.name : ticket.resolution,
        volunteers: ticket.users,
      };
      this.isViewing = true;
      this.isEditing = false;
    },
    statusClass(status) {
      if (status === "Uus") return "status-new";
      if (status === "Avatud") return "status-open";
      if (status === "Hoiukodus") return "status-in-progress";
      if (status === "Lõpetatud") return "status-completed";
      return "";
    },
    deleteTicket(ticketId) {
      this.tickets = this.tickets.filter((ticket) => ticket.id !== ticketId);
      this.filterTickets();
    },
    copyTicketInfo(ticket) {
      const ticketInfo = `
          Loom: ${ticket.describedAnimal || 'Määramata'}
          Kirjeldus: ${ticket.description || 'kirjeldus puudub'}
          Looma grupp ja liik: ${ticket.upperSpecies.name || 'Tuvastamata'}, ${ticket.species.name || 'Tuvastamata'}
          Vigastused: ${ticket.tags.join(', ')}
          Asukoht: ${ticket.location} (koordinaadid: ${ticket.latitude}, ${ticket.longitude})
          Asukoha täpsustus: ${ticket.directions || 'Määramata'}
          Lisatud: ${new Date(ticket.createdAt).toLocaleDateString()}
          Teavitaja: ${ticket.reporterName}, telefon: ${ticket.reporterPhone || ''}, e-post: ${ticket.reporterEmail || ''}
          Teavitaja jääb sündmuskohale: ${ticket.reporterCanWait ? 'jah' : 'ei'}`;

      navigator.clipboard.writeText(ticketInfo).then(() => {
        alert('Andmed kopeeritud!');
      }).catch(err => {
        console.error('Failed to copy ticket info: ', err);
      });
    },
  },
};
</script>


<style scoped>
/* Üldised nupu stiilid */
button {
  padding: 10px 15px;
  margin: 5px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s ease;
  font-family: "Inria Serif", serif;
  width: 100%;
  background-color: #f1f4f1;
}

button:hover {
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.ticket-management {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  text-align: center;
}

.filters-section {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  width: 80%;
  margin-bottom: 20px;
}

.filters label {
  flex: 1;
  margin-right: 20px;
}

.search {
  position: relative;
  width: 100%;
  max-width: 400px;
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

.search input:focus {
  outline: none;
}

.ticket-list {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}

.ticket-card {
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  padding: 20px;
  margin: 10px 0;
  border-radius: 10px;
  width: 80%;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  position: relative;
  transition: background-color 0.3s ease; /* Sujuv üleminek värvi vahetamisel */
}

/* Klassid oleku põhjal */
.status-new {
  background-color: #f4cdcd;
}

.status-new:hover {
  background-color: #efa5a5;
}

.status-open {
  background-color: #f4eecd;
}

.status-open:hover {
  background-color: #efe1a5;
}

.status-completed {
  background-color: #d6f4cd;
}

.status-completed:hover {
  background-color: #c2efa5;
}

.status-in-progress {
  background-color: #cdd5f4;
}

.status-in-progress:hover {
  background-color: #a5beef;
}

.ticket-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: flex-start;
  margin-right: 20px;
}

.ticket-animal {
  display: flex;
  margin-bottom: 10px;
  font-weight: bold;
}

.animal-name {
  font-size: 1.5em;
}

.ticket-status-tag {
  font-weight: bold;
  font-size: 1.5em;
  margin-left: 30px;
  text-transform: uppercase;
  background: transparent;
}

.ticket-status-tag.status-new {
  color: #d26e6e;
}

.ticket-status-tag.status-open {
  color: #d2bc6e;
}

.ticket-status-tag.status-completed {
  color: #87d26e;
}

.ticket-status-tag.status-in-progress {
  color: #6e87d2;
}

.ticket-details {
  font-size: 0.9em;
  margin: 5px 0;
  text-align: left;
}

.edit-link {
  display: flex;
  flex-direction: column; /* Paigutab nupud vertikaalselt */
  position: absolute; /* Paigutab konteineri absoluutse positsioneerimisega */
  top: 10px; /* Konteiner asub üleval */
  right: 10px; /* Konteiner asub paremal */
  align-items: flex-end; /* Joondab nupud paremale */
  width: 80px;
}

.edit-link a {
  color: #75a9cf;
  text-decoration: none;
  font-weight: bold;
}

.edit-link a:hover {
  text-decoration: underline;
}

.ticket-tags {
  display: flex;
  align-items: flex-start;
  flex: 1;
  flex-wrap: wrap;
}

.tag {
  background-color: #f1f4f1;
  border-radius: 15px;
  padding: 10px 20px;
  font-size: 1.1em;
  margin: 2px 2px 2px 20px;
  box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.15);
}

.status-filters {
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
  width: 100%;
  max-width: 100%;
}

.status-filters button {
  flex: 1;
  padding: 15px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  margin: 0 5px;
}

.status-filters button.status-all {
  background-color: #bdbdbd;
}

.status-filters .status-new {
  background-color: #d26e6e;
}

.status-filters button.status-open {
  background-color: #d2bc6e;
}

.status-filters button.status-completed {
  background-color: #87d26e;
}

.status-filters button.status-in-progress {
  background-color: #6e87d2;
}

.status-filters button:hover {
  opacity: 0.8;
}

.status-filters button.active {
  font-weight: bolder;
  font-size: 1.2em;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.6);
}

@media (max-width: 820px) {
  .status-filters {
    flex-direction: column;
    align-items: stretch;
  }

  .animal-name {
    font-size: 1em;
  }

  .ticket-status-tag {
    font-size: 1em;
    margin-left: 5px;
  }
}
</style>