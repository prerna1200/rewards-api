package org.example.rewardsapi;

import com.example.rewardsapi.service.RewardService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RewardServiceTest {

    private final RewardService rewardService = new RewardService(null);

    @Test
    void testCalculatePoints() {

        assertEquals(90, rewardService.calculatePoints(120));
        assertEquals(25, rewardService.calculatePoints(75));
        assertEquals(0, rewardService.calculatePoints(40));
    }
}