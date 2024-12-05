<template>
  <div class="ticket-management">
    <h1>Loomade juhtumite ülevaade</h1>

    <div class="filters-section">
      <div class="filters">
        <label for="regions">Vali regioon</label>
        <multiselect
            v-model="selectedRegions"
            :options="regions"
            :multiple="true"
            placeholder="Vali regioon"
            @input="filterTickets"
        >
        </multiselect>
      </div>

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
            v-if="hasAdminPermissions"
            @click="toggleStatus('Kõik')"
            :class="{ active: selectedStatuses.includes('Kõik') }"
            class="status-all"
        >
          Kõik ({{ countTicketsByStatus("Kõik") }})
        </button>
        <button
            v-if="hasAdminPermissions"
            @click="toggleStatus('Uus')"
            :class="{ active: selectedStatuses.includes('Uus') }"
            class="status-new"
        >
          Uus ({{ countTicketsByStatus("Uus") }})
        </button>
        <button
            @click="toggleStatus('Avatud')"
            :class="{ active: selectedStatuses.includes('Avatud') }"
            class="status-open"
        >
          Avatud ({{ countTicketsByStatus("Avatud") }})
        </button>
        <button
            v-if="hasAdminPermissions"
            @click="toggleStatus('Hoiukodus')"
            :class="{ active: selectedStatuses.includes('Hoiukodus') }"
            class="status-in-progress"
        >
          Hoiukodus ({{ countTicketsByStatus("Hoiukodus") }})
        </button>
        <button
            v-if="hasManagementPermissions"
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
        :token="getAuthToken()"
        @update-ticket="handleTicketUpdate"
        @close-popup="isEditing = false"
        @delete-ticket="handleTicketDelete"
    />

    <ViewTicket
        v-if="isViewing"
        :ticket="selectedTicket"
        :pseudo-id="pseudoIdMap[selectedTicket.id]"
        @close-popup="isViewing = false"
    />

    <div class="ticket-list">
      <div
          v-if="filteredTickets.length === 0"
      >
        <h3>Ühtegi piletit ei ole saadaval.</h3>
      </div>
      <div
          v-for="ticket in filteredTickets"
          :key="ticket.id"
          :class="['ticket-card', statusClass(ticket.status)]"
          @click.prevent="openViewTicket(ticket)"
      >
        <div class="edit-link">
          <button
              v-if="hasAdminPermissions"
              @click.stop.prevent="editTicket(ticket)"
              class="edit-button"
          >
            Muuda
          </button>
          <button
              @click="openViewTicket(ticket)"
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
          <button
              v-if="ticket.status !== 'Lõpetatud' && ticket.status !== 'Uus' && ticket.status !== 'Hoiukodus'"
              class="assign-button"
              @click.stop.prevent="assignTicket(ticket)"
          >
            Määra mulle
          </button>
        </div>

        <div class="ticket-info">
          <div class="ticket-animal">
            <div class="animal-name">
              #{{ pseudoIdMap[ticket.id] }} - {{ ticket.species ? ticket.species : 'Tuvastamata' }}
            </div>
            <div :class="['ticket-status-tag', statusClass(ticket.status)]">
              {{ ticket.status }}
            </div>
          </div>
          <div class="ticket-details">
            <p>
              <strong>Asukoht:</strong> {{ ticket.location }};
            </p>
            <p>
              <strong>Lisatud:</strong>
              {{ formatDate(ticket.createdAt) }}
            </p>
            <p>
              <strong>Teavitaja:</strong> {{ ticket.reporterName !== " "? ticket.reporterName : 'Nimi määramata'}},
              {{ ticket.reporterPhone? ticket.reporterPhone : 'Number määramata' }}, {{ ticket.reporterEmail? ticket.reporterEmail : 'Email määrmata' }}, {{ticket.reporterSocialMedia? ticket.reporterSocialMedia : 'Sotsiaalmeedia määramata'}}
            </p>
            <p><strong>Vabatahtlik(ud): </strong></p>
            <ul>
              <li v-if="ticket.volunteers.length === 0">Määramata</li>
              <li v-else v-for="user in ticket.volunteers" :key="user.id">
                {{ user.firstName + " " + user.lastName }}<br>
              </li>
            </ul>

          </div>

          <div class="ticket-tags">
            <span v-if=ticket.upperSpecies class="tag"> {{ ticket.upperSpecies }}</span>
            <span v-if=ticket.region class="tag">{{ ticket.region.name }}</span>
            <div v-for="tag in ticket.tags" :key="tag.id" class="tag">
              {{ tag }}
            </div>
            <span v-if=ticket.resolution class="tag"> {{ ticket.resolution }}</span>
          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
import Multiselect from "vue-multiselect";
import EditTicket from "@/components/EditTicketComponent.vue";
import ViewTicket from "@/components/ViewTicketComponent.vue";
import "vue-multiselect/dist/vue-multiselect.min.css";

export default {
  name: "TicketManagement",
  components: {
    Multiselect,
    EditTicket,
    ViewTicket,
  },
  data() {
    return {
      regions: [],
      selectedRegions: [],
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
      selectedStatuses: ["Kõik"],
      alertShown: false,
      pseudoIdMap: {},
    };
  },
  computed: {
    hasJuhtkondRole() {
      const roles = this.$store.getters.getRoles;
      return roles.includes('Juhtkond');
    },
    hasPiirkonnajuhtRole() {
      const roles = this.$store.getters.getRoles;
      return roles.includes('Piirkonnajuht');
    },
    hasPäevajuhtRole() {
      const roles = this.$store.getters.getRoles;
      return roles.includes('Päevajuht');
    },
    canViewCompletedTickets() {
      return this.hasJuhtkondRole || this.hasPiirkonnajuhtRole;
    },
    hasAdminPermissions() {
      return this.hasJuhtkondRole || this.hasPiirkonnajuhtRole || this.hasPäevajuhtRole;
    },
    hasManagementPermissions() {
      return this.hasJuhtkondRole || this.hasPiirkonnajuhtRole;
    },
  },
  async created() {
    await Promise.all([
      this.fetchRegions(),
      this.fetchTickets(),
      this.fetchTags(),
      this.fetchUsers(),
      this.fetchUpperSpecies(),
      this.fetchResolutions(),
      this.fetchStatuses(),
    ]);
  },
  watch: {
    selectedRegions: "filterTickets",
    searchTerm: "filterTickets",
    selectedStatuses: "filterTickets",
  },

  methods: {
    getAuthToken() {
      return localStorage.getItem('token'); // Get the token from local storage
    },
    handleUnauthorized() {
      if (this.alertShown) return;
      alert('Teie sessioon on aegunud, palun logige uuesti sisse!');
      this.$store.dispatch('logout');
      this.$router.push('/login');
      this.alertShown = true;
    },
    async fetchData(endpoint) {
      const token = this.getAuthToken();
      //const response = await fetch(`http://localhost:8080/api/${endpoint}`, {
      const response = await fetch(`api/${endpoint}`, {
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
      return response.json();
    },
    async fetchTags() {
      const tags = await this.fetchData('animalTags/injuries');
      this.tags = tags ? tags.map(tag => tag.injury) : [];
    },
    async fetchUsers() {
      const users = await this.fetchData('users/all');
      this.users = users || [];
    },
    async fetchRegions() {
      const regions = await this.fetchData('regions/all');
      this.regions = regions || [];
    },
    async fetchUpperSpecies() {
      const upperSpecies = await this.fetchData('upperSpecies/all');
      this.upperSpecies = upperSpecies ? upperSpecies.map(species => species.name) : [];
    },
    async fetchResolutions() {
      const resolutions = await this.fetchData('resolutions/all');
      this.resolutions = resolutions ? resolutions.map(resolution => resolution.name) : [];
    },
    async fetchStatuses() {
      const statuses = await this.fetchData('statuses/all');
      this.statuses = statuses || [];
    },

    async fetchTickets() {
      const tickets = await this.fetchData('tickets/all');
      if (!tickets) return;

      this.tickets = await Promise.all(tickets.map(async (ticket, index) => {
        const tags = await this.fetchData(`tickets/${ticket.id}/animalTags`);
        const volunteers = await this.fetchData(`tickets/${ticket.id}/users`);

        // Fetch resolution and status
        const resolutionData = await this.fetchData(`tickets/${ticket.id}/resolution`);
        const resolution = resolutionData ? resolutionData.name : '';
        const statusData = await this.fetchData(`tickets/${ticket.id}/status`);
        const status = statusData ? statusData.name : '';

        const speciesName = ticket.species?.name || ''; // Get species name
        const upperSpeciesName = ticket.species?.upperSpecies?.name || '';

        const enrichedVolunteers = await Promise.all(volunteers.map(async (volunteer) => {
          const volunteerTags = await this.fetchData(`users/${volunteer.id}/tags`);
          const volunteerRegions = await this.fetchData(`users/${volunteer.id}/regions`);
          const volunteerSpecies = await this.fetchData(`users/${volunteer.id}/species`);

          return {
            ...volunteer,
            tags: volunteerTags || [],
            regions: volunteerRegions || [],
            species: volunteerSpecies || []
          };
        }));

        const images = await this.fetchData(`picturesToTickets/all/${ticket.id}`);
        // Assign a pseudo ID based on the index
        this.pseudoIdMap[ticket.id] = index + 1;

        // Return the ticket with all necessary data
        return {
          ...ticket,
          tags: tags || [],
          volunteers: enrichedVolunteers || [],
          species: speciesName,
          upperSpecies: upperSpeciesName,
          resolution: resolution || "",
          status: status || '',
          images: images || [],
        };
      }));

      this.filterTickets();
    },


    filterTickets() {
      this.filteredTickets = this.tickets.filter(ticket => {
        // Basic filters for search term, region, and selected statuses
        const matchesSearchTerm = this.searchTerm === '' || ticket.animalName.toLowerCase().includes(this.searchTerm.toLowerCase());
        const matchesRegion = this.selectedRegions.length === 0 || this.selectedRegions.includes(ticket.region);
        const matchesStatus = this.selectedStatuses.includes('Kõik') || this.selectedStatuses.includes(ticket.status);

        // Check role-based access for "Completed" tickets
        const canViewTicket = ticket.status !== 'Lõpetatud' || this.canViewCompletedTickets;
        const canViewAllTickets = this.hasAdminPermissions || ticket.status === 'Avatud'

        return matchesSearchTerm && matchesRegion && matchesStatus && canViewTicket && canViewAllTickets;
      });

      // First, sort tickets by status
      const statusSortedTickets = this.sortTicketsByStatus(this.filteredTickets);

      // Then, sort each group by date (newer dates first within each status)
      this.filteredTickets = statusSortedTickets.sort((a, b) => {
        // Sort by status first (assuming `sortTicketsByStatus` applies this)
        const statusComparison = this.compareStatuses(a.status, b.status);

        // If statuses are the same, sort by dateSubmitted in descending order
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
      return this.tickets.filter((ticket) => ticket.status === status).length;
    },
    toggleStatus(status) {
      const index = this.selectedStatuses.indexOf(status);
      if (index > -1) {
        // If the status is already selected, remove it
        this.selectedStatuses.splice(index, 1);
      } else {
        // Otherwise, add it to the selected statuses
        this.selectedStatuses.push(status);
      }

      // If "Kõik" is selected, remove all others
      if (status === "Kõik") {
        this.selectedStatuses = ["Kõik"];
      } else if (this.selectedStatuses.includes("Kõik")) {
        // If "Kõik" is already selected, remove it
        this.selectedStatuses = this.selectedStatuses.filter(
            (s) => s !== "Kõik"
        );
      }

      this.filterTickets(); // Call the filter method to update the displayed tickets
    },

    sortTicketsByStatus(tickets) {
      const statusOrder = {
        Uus: 0,
        Avatud: 1,
        Hoiukodus: 2,
        Lõpetatud: 3,
      };

      return tickets.sort((a, b) => statusOrder[a.status] - statusOrder[b.status]);
    },
    compareStatuses(statusA, statusB) {
      // Define the priority order for statuses, e.g., "Open" > "In Progress" > "Completed"
      const statusPriority = {
        "Uus": 1,
        "Avatud": 2,
        "Hoiukodus": 3,
        "Lõpetatud": 4,
      };

      return (statusPriority[statusA] || 99) - (statusPriority[statusB] || 99);
    },
    async handleTicketUpdate() {
      this.isEditing = false;
      await this.fetchTickets();
      alert('Pilet on edukalt muudetud!');
    },
    editTicket(ticket) {
      this.selectedTicket = {...ticket};
      this.isEditing = true; // Avame redigeerimise seisundi
      this.isViewing = false; // Sulge vaatamise seisund
    },

    openViewTicket(ticket) {
      this.selectedTicket = ticket;
      this.isViewing = true; // Avame vaatamise seisundi
      this.isEditing = false;
    },

    statusClass(status) {
      if (status === "Uus") return "status-new";
      if (status === "Avatud") return "status-open";
      if (status === "Hoiukodus") return "status-in-progress";
      if (status === "Lõpetatud") return "status-completed";
      return "";
    },
    handleTicketDelete() {
      this.isEditing = false;
      location.reload();
      alert('Teade on edukalt kustutatud!');
    },
    async assignTicket(ticket) {
      const isConfirmed = window.confirm('Kas soovite selle pileti endale määrata?');

      if (!isConfirmed) {
        return;
      }

      const currentUser = this.$store.state.user;
      const userId = currentUser.id;
      const token = this.getAuthToken();
      //const response = await fetch(`http://localhost:8080/api/ticketToUsers/add/${ticket.id}/${userId}`, {
      const response = await fetch(`api/ticketToUsers/add/${ticket.id}/${userId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        }
      });
      if (!response.ok) {
        if (response.status === 403) {
          this.handleUnauthorized();
        } else {
          console.error(`Failed to assign ticket`);
        }
      } else {
        alert('Pilet on edukalt määratud!');
        await this.fetchTickets();
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

      return `${day}.${month}.${year} ${hours}:${minutes}`; // Change the format as needed
    },
    copyTicketInfo(ticket) {
      // Create a string with the ticket information
      const ticketInfo = `
Loom: ${ticket.describedAnimal || 'Määramata'}
Kirjeldus: ${ticket.description || 'kirjeldus puudub'}
Looma grupp ja liik: ${ticket.upperSpecies || 'Tuvastamata'}, ${ticket.species || 'Tuvastamata'}
Vigastused: ${ticket.tags.join(', ')}
Asukoht: ${ticket.location} (koordinaadid: ${ticket.latitude}, ${ticket.longitude})
Asukoha täpsustus: ${ticket.directions || 'Määramata'}
Lisatud: ${this.formatDate(ticket.createdAt)}
Teavitaja: ${ticket.reporterName !== " "? ticket.reporterName: '-'}, telefon: ${ticket.reporterPhone || '-'}, e-post: ${ticket.reporterEmail || '-'}, sotsiaalmeedia: ${ticket.reporterSocialMedia || '-'}
Teavitaja jääb sündmuskohale: ${ticket.reporterCanWait ? 'jah' : 'ei'}`;

      // Copy to clipboard
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

.filters {
  align-items: center;
  flex: 1;
  margin-bottom: 20px;
  min-width: 30%;
  padding-right: 20px;
}

.filters multiselect {
  flex: 2;
  width: 100%;
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
  background-color: #fabdbd;
}

.status-new:hover {
  background-color: #FF9999;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
}

.status-open {
  background-color: #fadec1;
}

.status-open:hover {
  background-color: #FFCC99;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
}

.status-in-progress {
  background-color: #FFFACD;
}

.status-in-progress:hover {
  background-color: #fdf4a4;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
}

.status-completed {
  background-color: #D6F4CD;
}

.status-completed:hover {
  background-color: #b5e1aa;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
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
  pointer-events: none;
  color: #d26e6e;
}

.ticket-status-tag.status-open {
  pointer-events: none;
  color: #d29b6e;
}

.ticket-status-tag.status-in-progress {
  pointer-events: none;
  color: #d2bc6e;
}

.ticket-status-tag.status-completed {
  pointer-events: none;
  color: #87d26e;
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
  flex-wrap: wrap;
}

.tag {
  background-color: #F1F4F1;
  border-radius: 15px;
  padding: 10px 20px;
  margin: 5px;
  font-size: 1.1em;
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
  background-color: #d29b6e;;
}

.status-filters button.status-in-progress {
  background-color: #d2bc6e;
}

.status-filters button.status-completed {
  background-color: #87d26e;
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