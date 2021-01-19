import com.game.GameTopScore;
import com.game.dto.GameDto;
import com.game.entity.Game;
import com.game.repository.GameRepository;
import com.game.service.GameServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import utils.TestUtil;

import javax.persistence.EntityManager;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.game.GameConverter.convertToGame;
import static org.hamcrest.Matchers.hasItem;
import  static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

///**
// * Integration tests for the REST controller.
// */
@SpringBootTest(classes = GameTopScore.class)
//@ActiveProfiles("test")
@AutoConfigureMockMvc
@WithMockUser
 class GameControllerTestIT {

    private static final Integer DEFAULT_SCORE = 1;
    private static final Integer UPDATED_SCORE = 2;
//    private static final Integer SMALLER_SCORE = 1 - 1;

    private static final DateTimeFormatter FORMATE_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter FORMATE_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final LocalDateTime DEFAULT_TIME = LocalDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final LocalDateTime UPDATED_TIME = LocalDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final LocalDateTime SMALLER_TIME = LocalDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_PLAYER = "AAAAAAAAAA";
    private static final String UPDATED_PLAYER = "BBBBBBBBBB";

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameServiceImpl gameService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGameMockMvc;

    private Game game;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameDto createEntity(EntityManager em) {
        GameDto game = new GameDto(DEFAULT_PLAYER, DEFAULT_SCORE, DEFAULT_TIME);
        return game;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Game createUpdatedEntity(EntityManager em) {
        Game game = new Game(UPDATED_PLAYER, UPDATED_SCORE, UPDATED_TIME);
        return game;
    }

    @BeforeEach
    public void initTest() {
        game =convertToGame( createEntity(em));
    }

    @Test
    @Transactional
    public void createGame() throws Exception {
        int databaseSizeBeforeCreate = gameRepository.findAll().size();

        // Create the Game
        restGameMockMvc.perform(post("/api/v1/player")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(game)))
                .andExpect(status().isCreated());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeCreate + 1);
        Game testGame = gameList.get(gameList.size() - 1);
        assertThat(testGame.getScore()).isEqualTo(DEFAULT_SCORE);
        assertThat(testGame.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testGame.getPlayer()).isEqualTo(DEFAULT_PLAYER);
    }

    @Test
    @Transactional
    public void createGameWithExistingId() throws Exception {

        int databaseSizeBeforeCreate = gameRepository.findAll().size();

        // Create the Game with an existing ID
        game.setId(1);
//        GameDTO gameDTO = gameMapper.toDto(game);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGameMockMvc.perform(post("/api/v1/player")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(game)))
                .andExpect(status().isBadRequest());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
     void getGame() throws Exception {
        // Initialize the databas
        gameRepository.saveAndFlush(game);

        ;
        // Get the game
        restGameMockMvc.perform(get("/api/v1/player/{id}", game.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(game.getId()))
                .andExpect(jsonPath("$.score").value(DEFAULT_SCORE))
                .andExpect(jsonPath("$.time").value((DEFAULT_TIME.format(FORMATE_DATE_TIME))))
                .andExpect(jsonPath("$.player").value(DEFAULT_PLAYER));
    }


    @Test
    @Transactional
     void getAllPlayersByTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database -2
        gameRepository.saveAndFlush(game);

        // Get all the gameList where time is greater than or equal to DEFAULT_TIME
        defaultGameShouldBeFound("after=" + DEFAULT_TIME.format(FORMATE_DATE));

        // Get all the gameList where time is greater than or equal to UPDATED_TIME
        defaultGameShouldNotBeFound("after=" + UPDATED_TIME.format(FORMATE_DATE));
    }

    @Test
    @Transactional
     void getAllPlayersByTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database -1
        gameRepository.saveAndFlush(game);

        // Get all the gameList where time is less than or equal to DEFAULT_TIME
        defaultGameShouldBeFound("before=" + DEFAULT_TIME.format(FORMATE_DATE));

        // Get all the gameList where time is less than or equal to SMALLER_TIME
        defaultGameShouldNotBeFound("before=" + SMALLER_TIME.format(FORMATE_DATE));
    }


    @Test
    @Transactional
     void getAllPlayersByPlayersAndTimeIsGreaterAndTimeLessThanSomething() throws Exception {
        // Initialize the database -9
        gameRepository.saveAndFlush(game);

        // Get all the gameList where time is greater than DEFAULT_PLAYER
        defaultGameShouldBeFound("name=" + DEFAULT_PLAYER+","+UPDATED_PLAYER+"&after=" +
                DEFAULT_TIME.format(FORMATE_DATE)+"&before="+UPDATED_TIME.format(FORMATE_DATE));

        // Get all the gameList where time is greater than UPDATED_PLAYER
        defaultGameShouldNotBeFound("name=" + UPDATED_PLAYER);


    }


    @Test
    @Transactional
     void getAllGamesByPlayerIsEqualToSomething() throws Exception {
        // Initialize the database -3
        gameRepository.saveAndFlush(game);

        // Get all the gameList where player equals to DEFAULT_PLAYER
        defaultGameShouldBeFound("name=" + DEFAULT_PLAYER);

        // Get all the gameList where player equals to UPDATED_PLAYER
        defaultGameShouldNotBeFound("name=" + UPDATED_PLAYER);
    }

    @Test
    @Transactional
     void getAllPlayersByGreaterAndLessThanSomething() throws Exception {
        // Initialize the database -8
        gameRepository.saveAndFlush(game);

        // Get all the gameList where player not equals to DEFAULT_PLAYER
        defaultGameShouldBeFound("after=" + DEFAULT_TIME.format(FORMATE_DATE)+"&before="+UPDATED_TIME.format(FORMATE_DATE));

        // Get all the gameList where player not equals to SMALLER_TIME
        defaultGameShouldNotBeFound("before=" + SMALLER_TIME.format(FORMATE_DATE) );
    }

    @Test
    @Transactional
     void getAllPlayerByPlayersIsIn() throws Exception {
        // Initialize the database -4
        gameRepository.saveAndFlush(game);

        // Get all the gameList where player in DEFAULT_PLAYER or UPDATED_PLAYER
        defaultGameShouldBeFound("name=" + DEFAULT_PLAYER + "," + UPDATED_PLAYER);

        // Get all the gameList where player equals to UPDATED_PLAYER
        defaultGameShouldNotBeFound("name=" + UPDATED_PLAYER);
    }

    @Test
    @Transactional
     void getAllPlayersByPlayerAndTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database -7
        gameRepository.saveAndFlush(game);

        // Get all the gameList where player is not null
        defaultGameShouldBeFound("name=" + DEFAULT_PLAYER + "&after=" + DEFAULT_TIME);

        // Get all the gameList where player is null
        defaultGameShouldNotBeFound("name=" + UPDATED_PLAYER + "&after=" + UPDATED_TIME);
    }

    @Test
    @Transactional
     void getAllPlayersByPlayerAndTimeIsLessThanSomething() throws Exception {
        // Initialize the database -6
        gameRepository.saveAndFlush(game);

        // Get all the players where player contains DEFAULT_PLAYER
        defaultGameShouldBeFound("name=" + DEFAULT_PLAYER + "&before=" + DEFAULT_TIME);

        // Get all the players where player is null
        defaultGameShouldNotBeFound("name=" + UPDATED_PLAYER + "&before=" + UPDATED_TIME);
    }



    /**
     * Executes the search, and checks that the default entity is returned.
     */
    protected void defaultGameShouldBeFound(String filter) throws Exception {
        restGameMockMvc.perform(get("/api/v1/list?" + filter))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(game.getId())))
                .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE)))
                .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.format(FORMATE_DATE_TIME))))
                .andExpect(jsonPath("$.[*].player").value(hasItem(DEFAULT_PLAYER)));

    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGameShouldNotBeFound(String filter) throws Exception {
        restGameMockMvc.perform(get("/api/v1/list?" + filter))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

    }

}
