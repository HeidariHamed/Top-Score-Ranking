package entity;

import com.game.entity.Game;
import org.junit.jupiter.api.Test;
import utils.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;


public class GameTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Game.class);
        Game game1 = new Game();
        game1.setId(1);
        Game game2 = new Game();
        game2.setId(game1.getId());
        assertThat(game1).isEqualTo(game2);
        game2.setId((int) 2L);
        assertThat(game1).isNotEqualTo(game2);
        game1.setId(null);
        assertThat(game1).isNotEqualTo(game2);
    }
}
