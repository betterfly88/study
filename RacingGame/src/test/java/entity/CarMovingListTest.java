package entity;

/**
 * Created by betterFLY on 2018. 3. 25.
 * Github : http://github.com/betterfly88
 */
import dto.MOVING_TYPE;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class CarMovingListTest {
    private CarMovingList state;

    @Before
    public void setUp(){
        state = new CarMovingList();
    }

    @Test
    public void 자동차_이동리스트(){
        state.addCarMoveState(MOVING_TYPE.MOVE);
        assertThat(state.getMoveList()).contains(MOVING_TYPE.MOVE);
    }

    @Test
    public void 자동차정보_파싱(){
        state.addCarMoveState(MOVING_TYPE.MOVE);
    }

}