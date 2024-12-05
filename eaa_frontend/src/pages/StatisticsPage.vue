<template>
  <div class="statistics">
    <h1>Statistika</h1>
    <div class="filters-section">
      <div class="filters">

        <div class="filter-item">
          <label for="liigigrupp">Vali Liigigrupp</label>
          <input type="checkbox" @change="toggleSelectAll('liigigrupp')"/>
          <span class="choose-all-text">Vali Kõik</span>
          <multiselect
              v-model="selectedUpperSpecies"
              :options="liigigrupid"
              :multiple="true"
              :track-by="'name'"
              :label="'name'"
              placeholder="Vali Liigigrupp"
          ></multiselect>
        </div>

        <div class="filter-item">
          <label for="loom">Vali Looma Liik</label>
          <input type="checkbox" @change="toggleSelectAll('loomaliik')"/>
          <span class="choose-all-text">Vali Kõik</span>
          <multiselect
              v-model="selectedSpecies"
              :options="loomaLiigid"
              :multiple="true"
              :track-by="'name'"
              :label="'name'"
              placeholder="Vali Looma Liik"
          ></multiselect>
        </div>

        <div class="filter-item">
          <label for="piirkond">Vali Piirkond</label>
          <input type="checkbox" @change="toggleSelectAll('piirkond')"/>
          <span class="choose-all-text">Vali Kõik</span>
          <multiselect
              v-model="selectedRegions"
              :options="piirkonnad"
              :multiple="true"
              :track-by="'name'"
              :label="'name'"
              placeholder="Vali Piirkond"
          ></multiselect>
        </div>
        <div class="filter-item">
          <label for="vigastus">Vali Looma Vigastus</label>
          <input type="checkbox" @change="toggleSelectAll('vigastus')"/>
          <span class="choose-all-text">Vali Kõik</span>
          <multiselect
              v-model="selectedInjuries"
              :options="vigastused"
              :multiple="true"
              placeholder="Vali Looma Vigastus"
          ></multiselect>
        </div>
        <div class="filter-item">
          <label for="resolution">Vali Lahendus</label>
          <input type="checkbox" @change="toggleSelectAll('lahendus')"/>
          <span class="choose-all-text">Vali Kõik</span>
          <multiselect
              v-model="selectedResolutions"
              :options="lahendused"
              :multiple="true"
              :track-by="'name'"
              :label="'name'"
              placeholder="Vali Lahendus"
          ></multiselect>
        </div>
        <div class="filter-item">
          <label for="count-frequency">Vali Kuvatava Perioodi Sagedus</label>
          <select v-model="selectedCountFrequency" id="count-frequency" class="custom-select">
            <option value="" disabled selected>Vali Sagedus</option>
            <option value="daily">Päeva kaupa</option>
            <option value="monthly">Kuu kaupa</option>
            <option value="yearly">Aasta kaupa</option>
          </select>
        </div>
        <div class="filter-item">
          <label for="ajaperioodi-algus">Ajaperioodi Algus</label>
          <input type="date" v-model="selectedStartDate" id="ajaperioodi-algus" class="date-input"/>
        </div>

        <div class="filter-item">
          <label for="ajaperioodi-lopp">Ajaperioodi Lõpp</label>
          <input type="date" v-model="selectedEndDate" id="ajaperioodi-lopp" class="date-input"/>
        </div>

      </div>

      <div class="filters">
        <div class="filter-item filter-button">
          <button @click="validateSelection">Rakenda Filtrid</button>
        </div>
      </div>
    </div>

    <!-- Statistics Table-->
    <div class="fetched-tickets">
      <span v-if="Object.keys(groupedStatistics).length > 0" id="count-of-tickets">Leiti {{ statisticsDTOs.length }} filtritele vastavat piletit!</span>
      <table>
        <thead v-if="Object.keys(groupedStatistics).length > 0">
        <tr>
          <th>Kuupäev</th>
          <th>Liigigrupp</th>
          <th v-if="selectedSpecies.length > 0">Loomaliik</th>
          <th v-if="selectedRegions.length > 0">Piirkond</th>
          <th v-if="selectedResolutions.length > 0">Lahendus</th>
          <th v-if="selectedInjuries.length > 0">Vigastus</th>
          <th>Summa</th>
        </tr>
        </thead>
        <tbody>
        <template v-for="(groups, date) in groupedStatistics" :key="date">
          <tr v-for="(ticket, index) in groups" :key="index">
            <td>{{ date }}</td>
            <td>{{ ticket.upperSpecies }}</td>
            <td v-if="selectedSpecies.length > 0">{{ ticket.species }}</td>
            <td v-if="selectedRegions.length > 0">{{ ticket.region }}</td>
            <td v-if="selectedResolutions.length > 0">{{ ticket.resolution }}</td>
            <td v-if="selectedInjuries.length > 0">
              <span v-if="ticket.injuries && ticket.injuries.length > 0">
                <span v-for="(injury, index) in ticket.injuries" :key="index">
                  {{ injury }}<span v-if="index < ticket.injuries.length - 1">, </span>
                </span>
              </span>
              <span v-else>Puudub</span>
            </td>
            <td>{{ ticket.count }}</td>
          </tr>
        </template>
        </tbody>
      </table>
      <div v-if="Object.keys(groupedStatistics).length === 0 && buttonClicked">
        <p>Valitud filtrite alusel ei leitud ühtegi piletit.</p>
      </div>
    </div>

  </div>
</template>

<script>
import Multiselect from 'vue-multiselect';
import 'vue-multiselect/dist/vue-multiselect.min.css';
import index from "vuex";

/**
 * @typedef {Object} Entry
 * @property {string} date - Date of the record
 * @property {string} liigigrupp - Group of species
 * @property {string} loomaLiik - Specific species
 * @property {string} piirkond - Region
 * @property {string} vigastus - Injury
 * @property {string} lahendus - Resolution
 * @property {number} count - Count of occurrences
 */
export default {
  components: {Multiselect},
  name: "StatisticsPage",
  data() {
    return {
      selectedUpperSpecies: [],
      selectedSpecies: [],
      selectedRegions: [],
      selectedInjuries: [],
      selectedResolutions: [],
      selectedStartDate: '',
      selectedEndDate: '',
      selectedCountFrequency: '',
      loomaLiigid: [],
      liigigrupid: [],
      piirkonnad: [],
      vigastused: [],
      lahendused: [],
      alertShown: false,
      statisticsDTOs: [],
      /** @type {Entry[]} */
      filteredStatistics: [],
      buttonClicked: false,
      groupedStatistics: {},
    };
  },
  computed: {
    index() {
      return index
    }
  },
  methods: {
    toggleTickets() {
      this.buttonClicked = !this.buttonClicked; // Toggle the buttonClicked property
    },
    getAuthToken() {
      return localStorage.getItem('token');
    },
    handleUnauthorized() {
      if (this.alertShown) return;
      console.error('Unauthorized: session expired');
      alert('Teie sessioon on aegunud, palun logige uuesti sisse!');
      this.$store.dispatch('logout');
      this.$router.push('/login');
      this.alertShown = true;
    },
    async fetchData(endpoint) {
      const token = this.getAuthToken();
      try {
        //const response = await fetch(`http://localhost:8080/api/${endpoint}`,
        const response = await fetch(`api/${endpoint}`,
            {
              method: 'GET',
              headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
              },
            }
        );
        if (!response.ok) {
          if (response.status === 403) {
            this.handleUnauthorized();
          }
        }
        return await response.json();
      } catch (error) {
        console.error('Fetch error:', error);
        return [];
      }
    },
    async fetchUpperSpecies() {
      const UpperSpecies = await this.fetchData('upperSpecies/all');
      this.liigigrupid = UpperSpecies.map(UpperSpecies => ({name: UpperSpecies.name}));
    },
    async fetchSpecies() {
      const species = await this.fetchData('species/all');
      this.loomaLiigid = species.map(species => ({name: species.name}));
    },
    async fetchRegions() {
      const regions = await this.fetchData('regions/all');
      this.piirkonnad = regions.map(region => ({name: region}));
    },
    async fetchTags() {
      const tags = await this.fetchData('animalTags/injuries');
      this.vigastused = tags ? tags.map(tag => tag.injury) : [];
    },
    async fetchResolutions() {
      const resolutions = await this.fetchData('resolutions/all');
      this.lahendused = resolutions.map(resolution => ({name: resolution.name}));
    },
    toggleSelectAll(field) {
      switch (field) {
        case 'liigigrupp':
          if (this.selectedUpperSpecies.length === this.liigigrupid.length) {
            this.selectedUpperSpecies = [];
          } else {
            this.selectedUpperSpecies = [...this.liigigrupid];
          }
          break;
        case 'loomaliik':
          if (this.selectedSpecies.length === this.loomaLiigid.length) {
            this.selectedSpecies = [];
          } else {
            this.selectedSpecies = [...this.loomaLiigid];
          }
          break;
        case 'piirkond':
          if (this.selectedRegions.length === this.piirkonnad.length) {
            this.selectedRegions = [];
          } else {
            this.selectedRegions = [...this.piirkonnad];
          }
          break;
        case 'vigastus':
          if (this.selectedInjuries.length === this.vigastused.length) {
            this.selectedInjuries = [];
          } else {
            this.selectedInjuries = this.vigastused.map(option => option);
          }
          break;
        case 'lahendus':
          if (this.selectedResolutions.length === this.lahendused.length) {
            this.selectedResolutions = [];
          } else {
            this.selectedResolutions = [...this.lahendused];
          }
          break;
      }
    },
    validateSelection() {
      if (!this.selectedUpperSpecies.length && !this.selectedSpecies.length) {
        alert('Palun valige vähemalt üks liikigrupp või loomaliik!');
        return;
      }
      if (!this.selectedStartDate || !this.selectedEndDate) {
        alert('Palun valige algus- ja lõppkuupäev!');
        return;
      }
      const startDate = new Date(this.selectedStartDate);
      const endDate = new Date(this.selectedEndDate);
      const today = new Date();

      if (startDate > today || endDate > today) {
        alert('Valitud kuupäevad ei tohi olla tulevikus!');
        return;
      }

      if (startDate > endDate) {
        alert('Alguskuupäev peab olema enne lõppkuupäeva või sama nagu lõppkuupäev!');
        return;
      }

      if (!this.selectedCountFrequency) {
        alert('Palun valige statistika perioodiline sagedus!');
        return;
      }
      this.applyFilters();
    },

    async applyFilters() {

      const filters = {
        ajaperioodiAlgus: this.selectedStartDate,
        ajaperioodiLopp: this.selectedEndDate,
        liigigrupp: this.selectedUpperSpecies.length > 0 ? this.selectedUpperSpecies.map(item => item.name) : null,
        loomaLiik: this.selectedSpecies.length > 0 ? this.selectedSpecies.map(item => item.name) : null,
        piirkond: this.selectedRegions.length > 0 ? this.selectedRegions.map(item => item.name) : null,
        vigastus: this.selectedInjuries.length > 0 ? this.selectedInjuries : null,
        lahendus: this.selectedResolutions.length > 0 ? this.selectedResolutions.map(item => item.name) : null,
      };

      try {
        //const response = await fetch('http://localhost:8080/api/statistics/filtered', {
        const response = await fetch("api/statistics/filtered", {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${this.getAuthToken()}`,
          },
          body: JSON.stringify(filters),
        });

        if (!response.ok) {
          if (response.status === 401) {
            console.error('Unauthorized access - perhaps your token is invalid or expired.');
            this.handleUnauthorized();
          } else if (response.status === 403) {
            console.error('Forbidden - you do not have permission to access this resource.');
            this.handleUnauthorized();
          } else if (response.status === 404) {
            console.error('Not Found - the requested resource could not be found.');
          } else if (response.status === 500) {
            console.error('Server Error - something went wrong on the server.');
          } else {
            console.error(`Failed to fetch data: ${response.status} ${response.statusText}`);
          }
          return null;
        }

        if (response.status === 204) return null; // No content

        this.statisticsDTOs = await response.json();
        this.groupedStatistics = this.groupTicketsByFrequency(this.statisticsDTOs, this.selectedCountFrequency, this.selectedStartDate, this.selectedEndDate);
        for (const dateKey in this.groupedStatistics) {
          this.groupedStatistics[dateKey].sort((a, b) => this.sortGroupedData(a, b));
        }
        this.toggleTickets()
      } catch (error) {
        console.error('Fetch error:', error);
      }
    },
    formatDate(date) {
      const d = new Date(date);
      return d.toLocaleDateString();
    },
    groupTicketsByFrequency(tickets, frequency, startDate, endDate) {
      const groupedData = {};

      // Extracts start and end dates
      const start = new Date(startDate);
      const end = new Date(endDate);
      end.setHours(23, 59, 59, 999);

      // Daily grouping
      if (frequency === 'daily') {
        let currentDate = new Date(start);
        currentDate.setHours(0, 0, 0, 0);

        while (currentDate <= end) {
          const dateKey = this.formatDate(currentDate);
          const tempGroupedData = {};

          tickets.forEach(ticket => {
            const ticketDate = new Date(ticket.date);
            if (ticketDate.toDateString() === currentDate.toDateString()) {
              // Constructs uniqueKey based on selected fields
              const uniqueKeyParts = [];
              uniqueKeyParts.push(ticket.upperSpecies);

              if (this.selectedSpecies.length > 0) {
                uniqueKeyParts.push(ticket.species);
              }
              if (this.selectedRegions.length > 0) {
                uniqueKeyParts.push(ticket.region);
              }
              if (this.selectedInjuries.length > 0) {
                uniqueKeyParts.push(ticket.injuries.join(','));
              }
              if (this.selectedResolutions.length > 0) {
                uniqueKeyParts.push(ticket.resolution);
              }

              const uniqueKey = uniqueKeyParts.join('|');

              if (!tempGroupedData[uniqueKey]) {
                tempGroupedData[uniqueKey] = {
                  date: dateKey,
                  upperSpecies: ticket.upperSpecies,
                  species: this.selectedSpecies.length > 0 ? ticket.species : null,
                  region: this.selectedRegions.length > 0 ? ticket.region : null,
                  injuries: this.selectedInjuries.length > 0 ? ticket.injuries : [],
                  resolution: this.selectedResolutions.length > 0 ? ticket.resolution : null,
                  count: 0
                };
              }
              tempGroupedData[uniqueKey].count += 1;
            }
          });

          for (const key in tempGroupedData) {
            if (tempGroupedData[key].count > 0) {
              if (!groupedData[dateKey]) {
                groupedData[dateKey] = [];
              }
              groupedData[dateKey].push(tempGroupedData[key]);
            }
          }

          currentDate.setDate(currentDate.getDate() + 1); // Moves to the next day
          currentDate.setHours(0, 0, 0, 0);
        }
      }

      // Monthly grouping
      else if (frequency === 'monthly') {
        // Check if start and end are in the same month
        if (start.getFullYear() === end.getFullYear() && start.getMonth() === end.getMonth()) {
          const sameMonthKey = `${this.formatDate(start)} - ${this.formatDate(end)}`;
          const tempGroupedDataSameMonth = {};

          tickets.forEach(ticket => {
            const ticketDate = new Date(ticket.date);
            if (ticketDate >= start && ticketDate <= end) {
              const uniqueKeyParts = [ticket.upperSpecies];

              if (this.selectedSpecies.length > 0) {
                uniqueKeyParts.push(ticket.species);
              }
              if (this.selectedRegions.length > 0) {
                uniqueKeyParts.push(ticket.region);
              }
              if (this.selectedInjuries.length > 0) {
                uniqueKeyParts.push(ticket.injuries.join(','));
              }
              if (this.selectedResolutions.length > 0) {
                uniqueKeyParts.push(ticket.resolution);
              }

              const uniqueKey = uniqueKeyParts.join('|');

              if (!tempGroupedDataSameMonth[uniqueKey]) {
                tempGroupedDataSameMonth[uniqueKey] = {
                  date: sameMonthKey,
                  upperSpecies: ticket.upperSpecies,
                  species: this.selectedSpecies.length > 0 ? ticket.species : null,
                  region: this.selectedRegions.length > 0 ? ticket.region : null,
                  injuries: this.selectedInjuries.length > 0 ? ticket.injuries : [],
                  resolution: this.selectedResolutions.length > 0 ? ticket.resolution : null,
                  count: 0
                };
              }
              tempGroupedDataSameMonth[uniqueKey].count += 1;
            }
          });

          for (const key in tempGroupedDataSameMonth) {
            if (tempGroupedDataSameMonth[key].count > 0) {
              if (!groupedData[sameMonthKey]) {
                groupedData[sameMonthKey] = [];
              }
              groupedData[sameMonthKey].push(tempGroupedDataSameMonth[key]);
            }
          }
        } else {
          // Process the first month
          const firstMonthStart = new Date(start.getFullYear(), start.getMonth(), start.getDate());
          const firstMonthEnd = new Date(start.getFullYear(), start.getMonth() + 1, 0); // Last day of the month
          const firstMonthKey = `${this.formatDate(firstMonthStart)} - ${this.formatDate(firstMonthEnd)}`;
          const tempGroupedDataFirstMonth = {};

          tickets.forEach(ticket => {
            const ticketDate = new Date(ticket.date);
            if (ticketDate >= firstMonthStart && ticketDate <= firstMonthEnd) {
              const uniqueKeyParts = [ticket.upperSpecies];

              if (this.selectedSpecies.length > 0) {
                uniqueKeyParts.push(ticket.species);
              }
              if (this.selectedRegions.length > 0) {
                uniqueKeyParts.push(ticket.region);
              }
              if (this.selectedInjuries.length > 0) {
                uniqueKeyParts.push(ticket.injuries.join(','));
              }
              if (this.selectedResolutions.length > 0) {
                uniqueKeyParts.push(ticket.resolution);
              }

              const uniqueKey = uniqueKeyParts.join('|');

              if (!tempGroupedDataFirstMonth[uniqueKey]) {
                tempGroupedDataFirstMonth[uniqueKey] = {
                  date: firstMonthKey,
                  upperSpecies: ticket.upperSpecies,
                  species: this.selectedSpecies.length > 0 ? ticket.species : null,
                  region: this.selectedRegions.length > 0 ? ticket.region : null,
                  injuries: this.selectedInjuries.length > 0 ? ticket.injuries : [],
                  resolution: this.selectedResolutions.length > 0 ? ticket.resolution : null,
                  count: 0
                };
              }
              tempGroupedDataFirstMonth[uniqueKey].count += 1;
            }
          });

          for (const key in tempGroupedDataFirstMonth) {
            if (tempGroupedDataFirstMonth[key].count > 0) {
              if (!groupedData[firstMonthKey]) {
                groupedData[firstMonthKey] = [];
              }
              groupedData[firstMonthKey].push(tempGroupedDataFirstMonth[key]);
            }
          }

          // Move to the next month
          let currentDate = new Date(firstMonthEnd);
          currentDate.setMonth(currentDate.getMonth() + 1);

          // Process full months in between
          while (currentDate < end) {
            const monthStart = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1);
            const monthEnd = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 0); // Last day of the month
            const monthKey = `${this.formatDate(monthStart)} - ${this.formatDate(monthEnd)}`;
            const tempGroupedDataFullMonth = {};

            tickets.forEach(ticket => {
              const ticketDate = new Date(ticket.date);
              if (ticketDate >= monthStart && ticketDate <= monthEnd) {
                const uniqueKeyParts = [ticket.upperSpecies];

                if (this.selectedSpecies.length > 0) {
                  uniqueKeyParts.push(ticket.species);
                }
                if (this.selectedRegions.length > 0) {
                  uniqueKeyParts.push(ticket.region);
                }
                if (this.selectedInjuries.length > 0) {
                  uniqueKeyParts.push(ticket.injuries.join(','));
                }
                if (this.selectedResolutions.length > 0) {
                  uniqueKeyParts.push(ticket.resolution);
                }

                const uniqueKey = uniqueKeyParts.join('|');

                if (!tempGroupedDataFullMonth[uniqueKey]) {
                  tempGroupedDataFullMonth[uniqueKey] = {
                    date: monthKey,
                    upperSpecies: ticket.upperSpecies,
                    species: this.selectedSpecies.length > 0 ? ticket.species : null,
                    region: this.selectedRegions.length > 0 ? ticket.region : null,
                    injuries: this.selectedInjuries.length > 0 ? ticket.injuries : [],
                    resolution: this.selectedResolutions.length > 0 ? ticket.resolution : null,
                    count: 0
                  };
                }
                tempGroupedDataFullMonth[uniqueKey].count += 1;
              }
            });

            for (const key in tempGroupedDataFullMonth) {
              if (tempGroupedDataFullMonth[key].count > 0) {
                if (!groupedData[monthKey]) {
                  groupedData[monthKey] = [];
                }
                groupedData[monthKey].push(tempGroupedDataFullMonth[key]);
              }
            }

            // Move to the next month
            currentDate.setMonth(currentDate.getMonth() + 1);
          }

          // Process the last month
          const lastMonthStart = new Date(end.getFullYear(), end.getMonth(), 1);
          const lastMonthEnd = new Date(end.getFullYear(), end.getMonth(), end.getDate(), 23, 59, 59);
          const lastMonthKey = `${this.formatDate(lastMonthStart)} - ${this.formatDate(lastMonthEnd)}`;
          const tempGroupedDataLastMonth = {};

          tickets.forEach(ticket => {
            const ticketDate = new Date(ticket.date);
            if (ticketDate >= lastMonthStart && ticketDate <= lastMonthEnd) {
              const uniqueKeyParts = [ticket.upperSpecies];

              if (this.selectedSpecies.length > 0) {
                uniqueKeyParts.push(ticket.species);
              }
              if (this.selectedRegions.length > 0) {
                uniqueKeyParts.push(ticket.region);
              }
              if (this.selectedInjuries.length > 0) {
                uniqueKeyParts.push(ticket.injuries.join(','));
              }
              if (this.selectedResolutions.length > 0) {
                uniqueKeyParts.push(ticket.resolution);
              }

              const uniqueKey = uniqueKeyParts.join('|');

              if (!tempGroupedDataLastMonth[uniqueKey]) {
                tempGroupedDataLastMonth[uniqueKey] = {
                  date: lastMonthKey,
                  upperSpecies: ticket.upperSpecies,
                  species: this.selectedSpecies.length > 0 ? ticket.species : null,
                  region: this.selectedRegions.length > 0 ? ticket.region : null,
                  injuries: this.selectedInjuries.length > 0 ? ticket.injuries : [],
                  resolution: this.selectedResolutions.length > 0 ? ticket.resolution : null,
                  count: 0
                };
              }
              tempGroupedDataLastMonth[uniqueKey].count += 1;
            }
          });

          for (const key in tempGroupedDataLastMonth) {
            if (tempGroupedDataLastMonth[key].count > 0) if (!groupedData[lastMonthKey]) {
              groupedData[lastMonthKey] = [];
            }
            groupedData[lastMonthKey].push(tempGroupedDataLastMonth[key]);
          }
        }
      }

      // Yearly grouping
      else if (frequency === 'yearly') {
        // Check if start and end are in the same year
        if (start.getFullYear() === end.getFullYear()) {
          const sameYearKey = `${this.formatDate(start, 'year')} - ${this.formatDate(end)}`;
          const tempGroupedDataSameYear = {};

          tickets.forEach(ticket => {
            const ticketDate = new Date(ticket.date);
            if (ticketDate >= start && ticketDate <= end) {
              // Constructs uniqueKey based on selected fields
              const uniqueKeyParts = [];
              uniqueKeyParts.push(ticket.upperSpecies);

              if (this.selectedSpecies.length > 0) {
                uniqueKeyParts.push(ticket.species);
              }
              if (this.selectedRegions.length > 0) {
                uniqueKeyParts.push(ticket.region);
              }
              if (this.selectedInjuries.length > 0) {
                uniqueKeyParts.push(ticket.injuries.join(',')); // Joins injuries as a string
              }
              if (this.selectedResolutions.length > 0) {
                uniqueKeyParts.push(ticket.resolution);
              }

              const uniqueKey = uniqueKeyParts.join('|'); // Creates unique key

              if (!tempGroupedDataSameYear[uniqueKey]) {
                tempGroupedDataSameYear[uniqueKey] = {
                  date: sameYearKey,
                  upperSpecies: ticket.upperSpecies,
                  species: this.selectedSpecies.length > 0 ? ticket.species : null,
                  region: this.selectedRegions.length > 0 ? ticket.region : null,
                  injuries: this.selectedInjuries.length > 0 ? ticket.injuries : [],
                  resolution: this.selectedResolutions.length > 0 ? ticket.resolution : null,
                  count: 0
                };
              }
              tempGroupedDataSameYear[uniqueKey].count += 1;
            }
          });

          for (const key in tempGroupedDataSameYear) {
            if (tempGroupedDataSameYear[key].count > 0) {
              if (!groupedData[sameYearKey]) {
                groupedData[sameYearKey] = [];
              }
              groupedData[sameYearKey].push(tempGroupedDataSameYear[key]);
            }
          }
        } else {
          // Process each year in the range
          let currentDate = new Date(start);
          currentDate.setFullYear(currentDate.getFullYear());

          while (currentDate <= end) {
            const yearStart = new Date(currentDate.getFullYear(), 0, 1);
            const yearEnd = new Date(currentDate.getFullYear(), 11, 31, 23, 59, 59, 999);
            const dateRangeKey = `${this.formatDate(yearStart)} - ${this.formatDate(yearEnd)}`; // e.g., "1.1.2023 - 31.12.2023"
            const tempGroupedData = {};

            tickets.forEach(ticket => {
              const ticketDate = new Date(ticket.date);
              if (ticketDate >= yearStart && ticketDate <= yearEnd) {
                // Constructs uniqueKey based on selected fields
                const uniqueKeyParts = [];
                uniqueKeyParts.push(ticket.upperSpecies);

                if (this.selectedSpecies.length > 0) {
                  uniqueKeyParts.push(ticket.species);
                }
                if (this.selectedRegions.length > 0) {
                  uniqueKeyParts.push(ticket.region);
                }
                if (this.selectedInjuries.length > 0) {
                  uniqueKeyParts.push(ticket.injuries.join(',')); // Joins injuries as a string
                }
                if (this.selectedResolutions.length > 0) {
                  uniqueKeyParts.push(ticket.resolution);
                }

                const uniqueKey = uniqueKeyParts.join('|'); // Creates unique key

                if (!tempGroupedData[uniqueKey]) {
                  tempGroupedData[uniqueKey] = {
                    date: this.formatDate(yearStart, 'year'),
                    upperSpecies: ticket.upperSpecies,
                    species: this.selectedSpecies.length > 0 ? ticket.species : null,
                    region: this.selectedRegions.length > 0 ? ticket.region : null,
                    injuries: this.selectedInjuries.length > 0 ? ticket.injuries : [],
                    resolution: this.selectedResolutions.length > 0 ? ticket.resolution : null,
                    count: 0
                  };
                }
                tempGroupedData[uniqueKey].count += 1;
              }
            });

            for (const key in tempGroupedData) {
              if (tempGroupedData[key].count > 0) {
                if (!groupedData[dateRangeKey]) {
                  groupedData[dateRangeKey] = [];
                }
                groupedData[dateRangeKey].push(tempGroupedData[key]);
              }
            }

            currentDate.setFullYear(currentDate.getFullYear() + 1);
          }
          const lastYearKey = `${this.formatDate(new Date(end.getFullYear(), 0, 1))} - ${this.formatDate(end)}`;
          if (!groupedData[lastYearKey]) {
            groupedData[lastYearKey] = [];
          }
        }
      }
      return groupedData;
    },
    sortGroupedData(a, b) {
      // Compares upperSpecies
      const upperSpeciesComparison = a.upperSpecies.localeCompare(b.upperSpecies);
      if (upperSpeciesComparison !== 0) return upperSpeciesComparison;

      // Compares species if selected
      if (this.selectedSpecies.length > 0) {
        const speciesComparison = a.species?.localeCompare(b.species);
        if (speciesComparison !== 0) return speciesComparison;
      }

      // Compares region if selected
      if (this.selectedRegions.length > 0) {
        const regionComparison = a.region?.localeCompare(b.region);
        if (regionComparison !== 0) return regionComparison;
      }

      // Compares injuries if selected
      if (this.selectedInjuries.length > 0) {
        const injuriesA = a.injuries.join(','); // Convert to string for comparison
        const injuriesB = b.injuries.join(',');
        const injuriesComparison = injuriesA.localeCompare(injuriesB);
        if (injuriesComparison !== 0) return injuriesComparison;
      }

      // Compares resolution if selected
      if (this.selectedResolutions.length > 0) {
        const resolutionComparison = a.resolution?.localeCompare(b.resolution);
        if (resolutionComparison !== 0) return resolutionComparison;
      }

      // If all comparisons are equal
      return 0;
    },
  },
  async mounted() {
    await this.fetchUpperSpecies();
    await this.fetchSpecies();
    await this.fetchRegions();
    await this.fetchTags();
    await this.fetchResolutions();
  },
};
</script>

<style scoped>
.statistics {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  text-align: center;
}

.filters-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}

.filters {
  display: flex;
  flex-wrap: wrap;
  width: 80%;
}

.filter-item {
  flex: 1 1 30%;
  margin: 10px;
  min-width: 200px;
}

.filters-item multiselect {
  flex: 2;
  width: 100%;
}

.filter-item:last-child {
  padding-right: 0;
}

.date-input {
  width: 100%;
  padding: 10px;
  border: 1px solid #f0f0f0;
  border-radius: 4px;
  box-sizing: border-box;
  height: 40px;
}

.custom-select {
  width: 100%;
  padding: 10px;
  border: 1px solid #f0f0f0;
  border-radius: 4px;
  box-sizing: border-box;
  height: 40px;
  font-size: 0.85em;
  background-color: white;
  font-family: 'Inria Serif', serif;
}

.custom-select option {
  font-size: 0.9em;
  cursor: pointer;
}

.custom-select option:hover {
  background-color: #87D26E;
  color: white;
}

label {
  margin-bottom: 5px;
  margin-right: 15px;
}

.choose-all-text {
  font-size: 0.85em;
  color: darkslategray;
}

select, input[type="date"] {
  padding: 5px;
  border: 1px solid #f0f0f0;
  border-radius: 5px;
}

.filter-button {
  display: flex;
  align-items: flex-end;
}

button {
  background-color: #87D26E;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1.1em;
}

button:hover {
  background-color: #89d970;
}

.fetched-tickets table {
  width: 100%;
  border-collapse: collapse;
}

.fetched-tickets th, .fetched-tickets td {
  padding: 12px;
}

.fetched-tickets th {
  background-color: rgba(135, 210, 110, 0.44);
  width: 200px;
  text-align: center;
  border: 1px solid #45a049;
}

.fetched-tickets td {
  max-width: 200px;
  text-align: left;
  border: 1px solid #45a049;
}

.fetched-tickets #count-of-tickets {
  margin-bottom: 30px;
}

.ticket p {
  margin: 5px 0;
}

.ticket p strong {
  font-weight: bold;
}
</style>