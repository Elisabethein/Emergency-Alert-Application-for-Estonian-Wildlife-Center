package com.EmergencyAlertApplication.EAA.Components;

import com.EmergencyAlertApplication.EAA.Entities.*;
import com.EmergencyAlertApplication.EAA.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class DataInitializer implements ApplicationRunner {
    private final TagRepository tagRepository;
    private final UpperSpeciesRepository upperSpeciesRepository;
    private final SpeciesRepository speciesRepository;
    private final UserRepository userRepository;
    private final UserToTagRepository userToTagRepository;
    private final UserToRoleRepository userToRoleRepository;
    private final RoleRepository roleRepository;
    private final UserToSpeciesRepository userToSpeciesRepository;
    private final RegionRepository regionRepository;
    private final UserToRegionRepository userToRegionRepository;
    private final AnimalTagRepository animalTagRepository;
    private final StatusRepository statusRepository;
    private final ResolutionRepository resolutionRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(TagRepository tagRepository, UpperSpeciesRepository upperSpeciesRepository, SpeciesRepository speciesRepository, UserRepository userRepository, UserToTagRepository userToTagRepository, UserToRoleRepository userToRoleRepository, RoleRepository roleRepository, UserToSpeciesRepository userToSpeciesRepository, RegionRepository regionRepository, UserToRegionRepository userToRegionRepository, TicketToAnimalTagRepository ticketToAnimalTagRepository, AnimalTagRepository animalTagRepository, TicketRepository ticketRepository, StatusRepository statusRepository, ResolutionRepository resolutionRepository, TicketToUserRepository ticketToUserRepository, PasswordEncoder passwordEncoder) {
        this.tagRepository = tagRepository;
        this.upperSpeciesRepository = upperSpeciesRepository;
        this.speciesRepository = speciesRepository;
        this.userRepository = userRepository;
        this.userToTagRepository = userToTagRepository;
        this.userToRoleRepository = userToRoleRepository;
        this.roleRepository = roleRepository;
        this.userToSpeciesRepository = userToSpeciesRepository;
        this.regionRepository = regionRepository;
        this.userToRegionRepository = userToRegionRepository;
        this.animalTagRepository = animalTagRepository;
        this.statusRepository = statusRepository;
        this.resolutionRepository = resolutionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // If there are no tags in the database, add some
        List<Tag> tags = tagRepository.findAll();
        if (tags.isEmpty()) {
            insertTags();
            tags = tagRepository.findAll();
        }
        // If there are no upper species in the database, add some
        List<UpperSpecies> upperSpecies = upperSpeciesRepository.findAll();
        if (upperSpecies.isEmpty()) {
            insertUpperSpecies();
            upperSpecies = upperSpeciesRepository.findAll();
        }
        // If there are no species in the database, add some
        if (speciesRepository.findAll().isEmpty()) {
            insertSpecies();
        }
        List<Role> roles = roleRepository.findAll();
        if (roles.isEmpty()) {
            insertRoles();
            roles = roleRepository.findAll();
        }
        List<Region> regions = regionRepository.findAll();
        if (regions.isEmpty()) {
            insertRegions();
            regions = regionRepository.findAll();
        }
        // If there are no users in the database, add some
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            insertUsers();
            users = userRepository.findAll();
        }
        // If there are no animal tags in the database, add some
        List<AnimalTag> animalTags = animalTagRepository.findAll();
        if (animalTags.isEmpty()) {
            insertAnimalTags();
        }
        List<Status> statuses = statusRepository.findAll();
        if (statuses.isEmpty()) {
            insertStatuses();
        }
        List<Resolution> resolutions = resolutionRepository.findAll();
        if (resolutions.isEmpty()) {
            insertResolutions();
        }
        if (userToTagRepository.findAll().isEmpty()) {
            insertUserToTags(tags, users);
        }
        if (userToRoleRepository.findAll().isEmpty()) {
            insertUserToRoles(roles, users);
        }
        if (userToSpeciesRepository.findAll().isEmpty()) {
            insertUserToSpecies(users, upperSpecies);
        }
        if (userToRegionRepository.findAll().isEmpty()) {
            insertUserToRegions(users, regions);
        }

    }

    private void insertUserToRegions(List<User> users, List<Region> regions) {
        User user1 = users.get(0);
        User user2 = users.get(1);
        User user3 = users.get(2);
        User user4 = users.get(3);
        Region region1 = regions.get(0);
        Region region2 = regions.get(1);
        Region region3 = regions.get(2);
        Region region4 = regions.get(3);
        userToRegionRepository.save(new UserToRegion(UUID.randomUUID(), user1, region1));
        userToRegionRepository.save(new UserToRegion(UUID.randomUUID(), user1, region2));
        userToRegionRepository.save(new UserToRegion(UUID.randomUUID(), user2, region2));
        userToRegionRepository.save(new UserToRegion(UUID.randomUUID(), user3, region3));
        userToRegionRepository.save(new UserToRegion(UUID.randomUUID(), user4, region4));
    }

    private void insertRegions() {
        Region region1 = new Region(UUID.randomUUID(), "Harjumaa");
        Region region2 = new Region(UUID.randomUUID(), "Hiiumaa");
        Region region3 = new Region(UUID.randomUUID(), "Ida-Virumaa");
        Region region4 = new Region(UUID.randomUUID(), "Jõgevamaa");
        Region region5 = new Region(UUID.randomUUID(), "Järvamaa");
        Region region6 = new Region(UUID.randomUUID(), "Läänemaa");
        Region region7 = new Region(UUID.randomUUID(), "Lääne-Virumaa");
        Region region8 = new Region(UUID.randomUUID(), "Põlvamaa");
        Region region9 = new Region(UUID.randomUUID(), "Pärnumaa");
        Region region10 = new Region(UUID.randomUUID(), "Raplamaa");
        Region region11 = new Region(UUID.randomUUID(), "Saaremaa");
        Region region12 = new Region(UUID.randomUUID(), "Tartumaa");
        Region region14 = new Region(UUID.randomUUID(), "Valgamaa");
        Region region15 = new Region(UUID.randomUUID(), "Viljandimaa");
        Region region16 = new Region(UUID.randomUUID(), "Võrumaa");
        regionRepository.saveAll(List.of(region1, region2, region3, region4, region5, region6, region7, region8, region9, region10, region11, region12, region14, region15, region16));
    }

    private void insertUserToSpecies(List<User> users, List<UpperSpecies> upperSpecies) {
        User user1 = users.get(0);
        User user2 = users.get(1);
        User user3 = users.get(2);
        User user4 = users.get(3);
        UpperSpecies upperSpecies1 = upperSpecies.get(0);
        UpperSpecies upperSpecies2 = upperSpecies.get(1);
        UpperSpecies upperSpecies3 = upperSpecies.get(2);
        UpperSpecies upperSpecies4 = upperSpecies.get(3);
        userToSpeciesRepository.save(new UserToSpecies(UUID.randomUUID(), false, user1, upperSpecies1));
        userToSpeciesRepository.save(new UserToSpecies(UUID.randomUUID(), false, user2, upperSpecies2));
        userToSpeciesRepository.save(new UserToSpecies(UUID.randomUUID(), true, user3, upperSpecies3));
        userToSpeciesRepository.save(new UserToSpecies(UUID.randomUUID(), false, user4, upperSpecies4));
    }

    private void insertUserToTags(List<Tag> tags, List<User> users) {
        User user1 = users.get(0);
        User user2 = users.get(1);
        User user3 = users.get(2);
        User user4 = users.get(3);
        Tag tag1 = tags.get(0);
        Tag tag2 = tags.get(1);
        Tag tag3 = tags.get(2);
        Tag tag4 = tags.get(3);
        userToTagRepository.save(new UserToTag(UUID.randomUUID(), user1, tag1));
        userToTagRepository.save(new UserToTag(UUID.randomUUID(), user2, tag1));
        userToTagRepository.save(new UserToTag(UUID.randomUUID(), user2, tag2));
        userToTagRepository.save(new UserToTag(UUID.randomUUID(), user3, tag4));
        userToTagRepository.save(new UserToTag(UUID.randomUUID(), user3, tag3));
        userToTagRepository.save(new UserToTag(UUID.randomUUID(), user4, tag4));
    }

    private void insertUserToRoles(List<Role> roles, List<User> users) {
        User user1 = users.get(0);
        User user2 = users.get(1);
        User user3 = users.get(2);
        User user4 = users.get(3);
        Role role1 = roles.get(0);
        Role role2 = roles.get(1);
        Role role3 = roles.get(2);
        Role role4 = roles.get(3);
        userToRoleRepository.save(new UserToRole(UUID.randomUUID(), user1, role1));
        userToRoleRepository.save(new UserToRole(UUID.randomUUID(), user2, role2));
        userToRoleRepository.save(new UserToRole(UUID.randomUUID(), user3, role3));
        userToRoleRepository.save(new UserToRole(UUID.randomUUID(), user4, role4));
    }


    private void insertRoles() {
        Role role1 = new Role(UUID.randomUUID(), "Vabatahtlik");
        Role role2 = new Role(UUID.randomUUID(), "Juhtkond");
        Role role3 = new Role(UUID.randomUUID(), "Piirkonnajuht");
        Role role4 = new Role(UUID.randomUUID(), "Päevajuht");
        roleRepository.saveAll(List.of(role1, role2, role3, role4));
    }

    private void insertUsers() {
        String hashedPassword = passwordEncoder.encode("password123");
        User testUser1 = new User(UUID.randomUUID(), "Test", "Kasutaja",
                Date.valueOf("1990-01-01"), "1234567890", "Test Street", "10",
                "Test City", "Test County", "12345", "test@test.com",
                hashedPassword, Timestamp.valueOf(LocalDateTime.now()));

        User testUser2 = new User(UUID.randomUUID(), "Jane", "Doe",
                Date.valueOf("1985-05-15"), "0987654321", "Main Street", "20",
                "Some City", "Some County", "54321", "jane.doe@example.com",
                hashedPassword, Timestamp.valueOf(LocalDateTime.now()));

        User testUser3 = new User(UUID.randomUUID(), "John", "Smith",
                Date.valueOf("1980-07-25"), "1122334455", "Elm Street", "15",
                "Another City", "Another County", "67890", "john.smith@example.com",
                hashedPassword,  Timestamp.valueOf(LocalDateTime.now()));

        User testUser4 = new User(UUID.randomUUID(), "Alice", "Johnson",
                Date.valueOf("1992-10-12"), "5566778899", "Oak Street", "30",
                "Test City", "Test County", "98765", "alice.johnson@example.com",
                hashedPassword, Timestamp.valueOf(LocalDateTime.now()));

        userRepository.saveAll(List.of(testUser1, testUser2, testUser3, testUser4));
    }
    private void insertUpperSpecies() {
        UpperSpecies[] upperSpecies = {
                new UpperSpecies(UUID.randomUUID(), "Jänes"),
                new UpperSpecies(UUID.randomUUID(), "Kahepaikne"),
                new UpperSpecies(UUID.randomUUID(), "Kährik"),
                new UpperSpecies(UUID.randomUUID(), "Kajakas"),
                new UpperSpecies(UUID.randomUUID(), "Kakuline"),
                new UpperSpecies(UUID.randomUUID(), "Kärplane"),
                new UpperSpecies(UUID.randomUUID(), "Kulliline/Haugaslane"),
                new UpperSpecies(UUID.randomUUID(), "Kurglane"),
                new UpperSpecies(UUID.randomUUID(), "Luik"),
                new UpperSpecies(UUID.randomUUID(), "Merelind-partlane"),
                new UpperSpecies(UUID.randomUUID(), "Nahkhiir"),
                new UpperSpecies(UUID.randomUUID(), "Näriline"),
                new UpperSpecies(UUID.randomUUID(), "Orav"),
                new UpperSpecies(UUID.randomUUID(), "Rebane"),
                new UpperSpecies(UUID.randomUUID(), "Siil"),
                new UpperSpecies(UUID.randomUUID(), "Suuruluk"),
                new UpperSpecies(UUID.randomUUID(), "Tuvi"),
                new UpperSpecies(UUID.randomUUID(), "Väikelind"),
                new UpperSpecies(UUID.randomUUID(), "Vareslane"),
                new UpperSpecies(UUID.randomUUID(), "Muu")
        };
        upperSpeciesRepository.saveAll(Arrays.asList(upperSpecies));
    }

    private void insertTags() {
        Tag[] tags = {
                new Tag(UUID.randomUUID(), "Hoiukodu"),
                new Tag(UUID.randomUUID(), "Transport"),
                new Tag(UUID.randomUUID(), "Tõlkimine"),
                new Tag(UUID.randomUUID(), "Lugude kirjutamine"),
                new Tag(UUID.randomUUID(), "Foto- ja videomontaaž"),
                new Tag(UUID.randomUUID(), "Projektikirjutamine"),
                new Tag(UUID.randomUUID(), "Andmete sisestamine"),
                new Tag(UUID.randomUUID(), "Infotelefonile vastamine"),
                new Tag(UUID.randomUUID(), "Ehitamine"),
                new Tag(UUID.randomUUID(), "Turundus"),
                new Tag(UUID.randomUUID(), "Ürituste korraldamine"),
                new Tag(UUID.randomUUID(), "Digilahenduste arendus (äpp)"),
                new Tag(UUID.randomUUID(), "Veebiarendus (koduleht)"),
                new Tag(UUID.randomUUID(), "Õigusabi")
        };
        tagRepository.saveAll(Arrays.asList(tags));
    }

    private void insertSpecies() {
        UpperSpecies upperSpeciesHülglane = upperSpeciesRepository.findByName("Hülglane");
        UpperSpecies upperSpeciesKahepaikne = upperSpeciesRepository.findByName("Kahepaikne");
        UpperSpecies upperSpeciesKajakas = upperSpeciesRepository.findByName("Kajakas");
        UpperSpecies upperSpeciesKärplane = upperSpeciesRepository.findByName("Kärplane");
        UpperSpecies upperSpeciesKakuline = upperSpeciesRepository.findByName("Kakuline");
        UpperSpecies upperSpeciesKullilineHaugaslane = upperSpeciesRepository.findByName("Kulliline/Haugaslane");
        UpperSpecies upperSpeciesKurglane = upperSpeciesRepository.findByName("Kurglane");
        UpperSpecies upperSpeciesLuik = upperSpeciesRepository.findByName("Luik");
        UpperSpecies upperSpeciesMerelindPartlane = upperSpeciesRepository.findByName("Merelind-partlane");
        UpperSpecies upperSpeciesNahkhiir = upperSpeciesRepository.findByName("Nahkhiir");
        UpperSpecies upperSpeciesNäriline = upperSpeciesRepository.findByName("Näriline");
        UpperSpecies upperSpeciesOrav = upperSpeciesRepository.findByName("Orav");
        UpperSpecies upperSpeciesSiil = upperSpeciesRepository.findByName("Siil");
        UpperSpecies upperSpeciesSuuruluk = upperSpeciesRepository.findByName("Suuruluk");
        UpperSpecies upperSpeciesTuvi = upperSpeciesRepository.findByName("Tuvi");
        UpperSpecies upperSpeciesVäikelind = upperSpeciesRepository.findByName("Väikelind");
        UpperSpecies upperSpeciesVareslane = upperSpeciesRepository.findByName("Vareslane");
        UpperSpecies upperSpeciesRebane = upperSpeciesRepository.findByName("Rebane");
        UpperSpecies upperSpeciesJänes = upperSpeciesRepository.findByName("Jänes");
        UpperSpecies upperSpeciesKährik = upperSpeciesRepository.findByName("Kährik");
        UpperSpecies upperSpeciesMuu = upperSpeciesRepository.findByName("Muu");

        List<Species> speciesList = Arrays.asList(
                new Species(UUID.randomUUID(), "Rebane", upperSpeciesRebane),
                new Species(UUID.randomUUID(), "Jänes", upperSpeciesJänes),
                new Species(UUID.randomUUID(), "Kährik", upperSpeciesKährik),

                new Species(UUID.randomUUID(), "Hallhüljes", upperSpeciesHülglane),
                new Species(UUID.randomUUID(), "Randal", upperSpeciesHülglane),
                new Species(UUID.randomUUID(), "Viigerhüljes", upperSpeciesHülglane),

                new Species(UUID.randomUUID(), "Kärnkonn", upperSpeciesKahepaikne),
                new Species(UUID.randomUUID(), "Rohukonn", upperSpeciesKahepaikne),
                new Species(UUID.randomUUID(), "Mudakonn", upperSpeciesKahepaikne),
                new Species(UUID.randomUUID(), "Rabakonn", upperSpeciesKahepaikne),

                new Species(UUID.randomUUID(), "Hõbekajakas", upperSpeciesKajakas),
                new Species(UUID.randomUUID(), "Kalakajakas", upperSpeciesKajakas),
                new Species(UUID.randomUUID(), "Merikajakas", upperSpeciesKajakas),
                new Species(UUID.randomUUID(), "Koldjalg-hõbekajakas", upperSpeciesKajakas),
                new Species(UUID.randomUUID(), "Naerukajakas", upperSpeciesKajakas),
                new Species(UUID.randomUUID(), "Tõmmukajakas", upperSpeciesKajakas),
                new Species(UUID.randomUUID(), "Väikekajakas", upperSpeciesKajakas),

                new Species(UUID.randomUUID(), "Habekakk", upperSpeciesKakuline),
                new Species(UUID.randomUUID(), "Händkakk", upperSpeciesKakuline),
                new Species(UUID.randomUUID(), "Kodukakk", upperSpeciesKakuline),
                new Species(UUID.randomUUID(), "Karvasjalg-kakk", upperSpeciesKakuline),
                new Species(UUID.randomUUID(), "Kassikakk", upperSpeciesKakuline),
                new Species(UUID.randomUUID(), "Kõrvukräts", upperSpeciesKakuline),
                new Species(UUID.randomUUID(), "Sooräts", upperSpeciesKakuline),
                new Species(UUID.randomUUID(), "Värbkakk", upperSpeciesKakuline),
                new Species(UUID.randomUUID(), "Vöötkakk", upperSpeciesKakuline),

                new Species(UUID.randomUUID(), "Kärp", upperSpeciesKärplane),
                new Species(UUID.randomUUID(), "Nirk", upperSpeciesKärplane),
                new Species(UUID.randomUUID(), "Tuhkur", upperSpeciesKärplane),
                new Species(UUID.randomUUID(), "Nugis", upperSpeciesKärplane),
                new Species(UUID.randomUUID(), "Kivinugis", upperSpeciesKärplane),
                new Species(UUID.randomUUID(), "Naarits", upperSpeciesKärplane),
                new Species(UUID.randomUUID(), "Mink", upperSpeciesKärplane),
                new Species(UUID.randomUUID(), "Mäger", upperSpeciesKärplane),
                new Species(UUID.randomUUID(), "Saarmas", upperSpeciesKärplane),

                new Species(UUID.randomUUID(), "Herilaseviu", upperSpeciesKullilineHaugaslane),
                new Species(UUID.randomUUID(), "Hiireviu", upperSpeciesKullilineHaugaslane),
                new Species(UUID.randomUUID(), "Kalakotkas", upperSpeciesKullilineHaugaslane),
                new Species(UUID.randomUUID(), "Kaljukotkas", upperSpeciesKullilineHaugaslane),
                new Species(UUID.randomUUID(), "Kanakull", upperSpeciesKullilineHaugaslane),
                new Species(UUID.randomUUID(), "Karvasjalg-viu", upperSpeciesKullilineHaugaslane),
                new Species(UUID.randomUUID(), "Merikotkas", upperSpeciesKullilineHaugaslane),
                new Species(UUID.randomUUID(), "Must-harksaba", upperSpeciesKullilineHaugaslane),
                new Species(UUID.randomUUID(), "Puna-harksaba", upperSpeciesKullilineHaugaslane),
                new Species(UUID.randomUUID(), "Punasjalg-pistrik", upperSpeciesKullilineHaugaslane),
                new Species(UUID.randomUUID(), "Rabapistrik", upperSpeciesKullilineHaugaslane),
                new Species(UUID.randomUUID(), "Raudkull", upperSpeciesKullilineHaugaslane),
                new Species(UUID.randomUUID(), "Roo-loorkull", upperSpeciesKullilineHaugaslane),
                new Species(UUID.randomUUID(), "Soo-loorkull", upperSpeciesKullilineHaugaslane),
                new Species(UUID.randomUUID(), "Stepi-loorkull", upperSpeciesKullilineHaugaslane),
                new Species(UUID.randomUUID(), "Suur-konnakotkas", upperSpeciesKullilineHaugaslane),
                new Species(UUID.randomUUID(), "Väike-konnakotkas", upperSpeciesKullilineHaugaslane),
                new Species(UUID.randomUUID(), "Väikepistrik", upperSpeciesKullilineHaugaslane),
                new Species(UUID.randomUUID(), "Välja-loorkull", upperSpeciesKullilineHaugaslane),

                new Species(UUID.randomUUID(), "Must-toonekurg", upperSpeciesKurglane),
                new Species(UUID.randomUUID(), "Valge-toonekurg", upperSpeciesKurglane),
                new Species(UUID.randomUUID(), "Sookurg", upperSpeciesKurglane),

                new Species(UUID.randomUUID(), "Kühmnokk-luik", upperSpeciesLuik),
                new Species(UUID.randomUUID(), "Laululuik", upperSpeciesLuik),
                new Species(UUID.randomUUID(), "Väikeluik", upperSpeciesLuik),

                new Species(UUID.randomUUID(), "Alk", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Aul", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Hahk", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Hallhaigur", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Hallhani", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Hallpõsk-pütt", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Hõbehaigur", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Jääkoskel", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Järvekaur", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Kanada lagle", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Kirjuhahk", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Kormoran", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Krüüsel", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Luitsnokk-part", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Lühinokk-hani", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Merivart", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Mustlagle", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Mustvaeras", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Piilpart", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Punakael-lagle", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Punakurk-kaur", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Punapea-vart", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Rabahani", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Rääkspart", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Rägapart", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Ristpart", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Rohukoskel", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Sarvikpütt", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Sinikael-part", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Soopart", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Sõtkas", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Söödikänn", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Suur-laukahani", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Tait", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Tõmmuvaeras", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Tuttpütt", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Tittvart", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Valgepõsk-lagle", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Väike-laukahani", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Väikekoskel", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Väikepütt", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Veetallaja", upperSpeciesMerelindPartlane),
                new Species(UUID.randomUUID(), "Viupart", upperSpeciesMerelindPartlane),

                new Species(UUID.randomUUID(), "Varustus", upperSpeciesMuu),
                new Species(UUID.randomUUID(), "Ravimid", upperSpeciesMuu),
                new Species(UUID.randomUUID(), "Muu", upperSpeciesMuu),

                new Species(UUID.randomUUID(), "Habedenlane", upperSpeciesNahkhiir),
                new Species(UUID.randomUUID(), "Nahkhiir", upperSpeciesNahkhiir),
                new Species(UUID.randomUUID(), "Kääbus-nahkhiir", upperSpeciesNahkhiir),
                new Species(UUID.randomUUID(), "Nattereri lendlane", upperSpeciesNahkhiir),
                new Species(UUID.randomUUID(), "Hõbe-nahkhiir", upperSpeciesNahkhiir),
                new Species(UUID.randomUUID(), "Pargi-nahkhiir", upperSpeciesNahkhiir),
                new Species(UUID.randomUUID(), "Põhja-nahkhiir", upperSpeciesNahkhiir),
                new Species(UUID.randomUUID(), "Pügmee-nahkhiir", upperSpeciesNahkhiir),
                new Species(UUID.randomUUID(), "Pruun-suurkõrv", upperSpeciesNahkhiir),
                new Species(UUID.randomUUID(), "Suurvidevlane", upperSpeciesNahkhiir),
                new Species(UUID.randomUUID(), "Tõmmulendlane", upperSpeciesNahkhiir),
                new Species(UUID.randomUUID(), "Veelendlane", upperSpeciesNahkhiir),
                new Species(UUID.randomUUID(), "Tiigilendlane", upperSpeciesNahkhiir),

                new Species(UUID.randomUUID(), "Hiir", upperSpeciesNäriline),
                new Species(UUID.randomUUID(), "Rott", upperSpeciesNäriline),
                new Species(UUID.randomUUID(), "Põlluhiir", upperSpeciesNäriline),
                new Species(UUID.randomUUID(), "Mügri", upperSpeciesNäriline),

                new Species(UUID.randomUUID(), "Orav", upperSpeciesOrav),
                new Species(UUID.randomUUID(), "Punaorav", upperSpeciesOrav),
                new Species(UUID.randomUUID(), "Must orav", upperSpeciesOrav),

                new Species(UUID.randomUUID(), "Harilik siil", upperSpeciesSiil),
                new Species(UUID.randomUUID(), "Kaelussiil", upperSpeciesSiil),

                new Species(UUID.randomUUID(), "Hunt", upperSpeciesSuuruluk),
                new Species(UUID.randomUUID(), "Pruunkaru", upperSpeciesSuuruluk),
                new Species(UUID.randomUUID(), "Ilves", upperSpeciesSuuruluk),
                new Species(UUID.randomUUID(), "Metssiga", upperSpeciesSuuruluk),
                new Species(UUID.randomUUID(), "Punahirv", upperSpeciesSuuruluk),
                new Species(UUID.randomUUID(), "Põder", upperSpeciesSuuruluk),
                new Species(UUID.randomUUID(), "Metskits", upperSpeciesSuuruluk),

                new Species(UUID.randomUUID(), "Kodutuvi", upperSpeciesTuvi),
                new Species(UUID.randomUUID(), "Kaelustuvi", upperSpeciesTuvi),
                new Species(UUID.randomUUID(), "Õõnetuvi", upperSpeciesTuvi),
                new Species(UUID.randomUUID(), "Turteltuvi", upperSpeciesTuvi),
                new Species(UUID.randomUUID(), "Kaelus-turteltuvi", upperSpeciesTuvi),

                new Species(UUID.randomUUID(), "Aed-põõsalind", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Aed-roolind", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Hall-kärbsenäpp", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Hallõgija", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Hallpea-rähn", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Hallrästas", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Hangelind", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Hänilane", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Hele-urvalind", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Heletilder", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Hoburästas", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Hüüp", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Jäälind", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Jõgi-ritsiklind", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Jõgitiir", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Jõgivästrik", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Kadakatäks", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Kaelus-kärbsenäpp", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Kaelusrästas", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Kaldapääsuke", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Kanepilind", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Karmiinleevike", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Käblik", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Kägu", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Käosulane", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Kiivitaja", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Kivirullija", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Kivitäks", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Koduvarblane", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Koldvint", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Kõrkja-roolind", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Kõvernokk-rüdi", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Kukkurtihane", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Kuldhänilane", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Kuldnokk", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Kuuse-käbilind", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Laanepüü", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Laanerähn", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Lammitilder", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Lapi tsiitsitaja", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Laulurästas", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Lääne-pöialpoiss", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Leeterüdi", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Leevike", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Lepalind", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Liivatüll", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Linavästrik", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Lõopistrik", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Mägi-kanepilind", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Männi-käbilind", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Männileevike", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Mänsak", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Merirüdi", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Merisk", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Mets-lehelind", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Metsis", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Metskiur", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Metskurvits", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Metstilder", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Metsvint", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Mudanepp", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Mudatilder", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Must-kärbsenäpp", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Must-lepalind", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Mustpea-põõsalind", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Musträhn", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Musträstas", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Mustsaba-vigle", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Musttihane", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Mustviires", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Naaskelnokk", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Nõlva-lehelind", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Nõmmekiur", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Nõmmelõoke", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Nurmkana", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Ohakalind", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Ööbik", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Öösorr", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Pasknäär", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Peoleo", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Piiritaja", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Plütt", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Plüü", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Porr", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Põhjatihane", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Põhjavint", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Põldleoke", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Põldtsiitsitaja", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Põldvarblane", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Põldvutt", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Pöialpoiss", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Pruunselg-põõsalind", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Punajalg-tilder", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Punarind", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Punaselg-õgija", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Puukoristaja", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Rabapüü", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Randkiur", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Randtiir", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Rasvatihane", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Räästapääsuke", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Rästas-roolind", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Räusktiir", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Roherähn", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Rohevint", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Rohunepp", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Roo-ritsiklind", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Roohabekas", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Rooruik", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Roosterind-tüll", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Rootsiitsitaja", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Rukkirääk", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Rüüt", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Sabatihane", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Salu-lehelind", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Salutihane", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Sarviklõoke", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Siidisaba", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Siisike", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Siniraag", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Sinirind", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Sinitihane", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Soo-roolind", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Sookiur", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Soorüdi", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Suitsupääsuke", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Suur-kirjurähn", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Suurkoovitaja", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Suurnokk-vint", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Suurrüdi", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Talvike", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Tamme-kirjurähn", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Täpikhuik", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Teder", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Tiigi-roolind", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Tikutaja", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Tumetilder", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Tutkas", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Tutt-tihane", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Tutt-tiir", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Tuttlõoke", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Tuuletallaja", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Urvalind", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Vaenukägu", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Vainurästas", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Valgeselg-kirjurähn", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Valgetiib-viires", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Väänkael", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Väike-käosulane", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Väike-kärbsenäpp", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Väike-kirjurähn", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Väike-lehelind", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Väike-põõsalind", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Väikehuik", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Väikekoovitaja", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Väikerüdi", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Väiketiir", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Väiketüll", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Värbrüdi", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Vesipapp", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Vihitaja", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Võsa-ritsiklind", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Võsaraat", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Vööt-käbilind", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Vööt-lehelind", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Vööt-põõsalind", upperSpeciesVäikelind),
                new Species(UUID.randomUUID(), "Vöötsaba-vigle", upperSpeciesVäikelind),

                new Species(UUID.randomUUID(), "Hallvares", upperSpeciesVareslane),
                new Species(UUID.randomUUID(), "Hakk", upperSpeciesVareslane),
                new Species(UUID.randomUUID(), "Harakas", upperSpeciesVareslane),
                new Species(UUID.randomUUID(), "Künnivares", upperSpeciesVareslane),
                new Species(UUID.randomUUID(), "Ronk", upperSpeciesVareslane)
        );
        // Salvesta kõik species-ed andmebaasi
        speciesRepository.saveAll(speciesList);
    }
    private void insertAnimalTags() {
        AnimalTag[] animalTags = {
                new AnimalTag(UUID.randomUUID(), "Murdunud jalg"),
                new AnimalTag(UUID.randomUUID(), "Peavigastus"),
                new AnimalTag(UUID.randomUUID(), "Selgroovigastus"),
                new AnimalTag(UUID.randomUUID(), "Verejooks"),
                new AnimalTag(UUID.randomUUID(), "Põletushaav"),
                new AnimalTag(UUID.randomUUID(), "Silmakahjustus"),
                new AnimalTag(UUID.randomUUID(), "Katkine tiib"),
                new AnimalTag(UUID.randomUUID(), "Luumurd"),
                new AnimalTag(UUID.randomUUID(), "Parasiidid"),
                new AnimalTag(UUID.randomUUID(), "Allergiline reaktsioon"),
                new AnimalTag(UUID.randomUUID(), "Nakkushaigus"),
                new AnimalTag(UUID.randomUUID(), "Väliskahjustus"),
                new AnimalTag(UUID.randomUUID(), "Loom kurnatud"),
                new AnimalTag(UUID.randomUUID(), "Loom söömata"),
                new AnimalTag(UUID.randomUUID(), "Muu")
        };
        animalTagRepository.saveAll(Arrays.asList(animalTags));
    }
    private void insertStatuses(){
        Status[] statuses = {
                new Status(UUID.randomUUID(), "Uus"),
                new Status(UUID.randomUUID(), "Avatud"),
                new Status(UUID.randomUUID(), "Hoiukodus"),
                new Status(UUID.randomUUID(), "Lõpetatud")
        };
        statusRepository.saveAll(Arrays.asList(statuses));
    }
    private void insertResolutions() {
        Resolution[] resolutions = {
                new Resolution(UUID.randomUUID(), "Vabastatud"),
                new Resolution(UUID.randomUUID(), "Surnud"),
                new Resolution(UUID.randomUUID(), "Eutaneeritud"),
                new Resolution(UUID.randomUUID(), "Suunatud 1247"),
                new Resolution(UUID.randomUUID(), "Lahenes sekkumiseta"),
                new Resolution(UUID.randomUUID(), "Muu lahendus")
        };
        resolutionRepository.saveAll(Arrays.asList(resolutions));
    }

}
