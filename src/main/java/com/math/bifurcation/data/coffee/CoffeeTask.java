package com.math.bifurcation.data.coffee;

import com.google.common.base.Preconditions;
import com.math.bifurcation.data.user.User;
import lombok.Getter;

import static com.math.bifurcation.data.coffee.UserState.*;

/**
 * @author Leonid Cheremshantsev
 */

public class CoffeeTask {

    public static final int PURING_TIME = (int) (2.5 * 60);
    @Getter
    private final User user;
    @Getter
    private Integer coefWaterCoffee;

    @Getter
    private Integer water;
    @Getter
    private Integer waterPart;

    @Getter
    private Integer grains;

    @Getter
    private Integer puringTime;
    @Getter
    private Integer puringTimePart;
    private Integer lastTime;
    private Integer time;
    @Getter
    private Integer usedWater;
    @Getter
    private UserState state;

    public CoffeeTask(User user) {
        Preconditions.checkNotNull(user);
        this.user = user;
        this.state = WAITING_FOR_WATER;
    }

    public void setWater(Integer water) {

        this.water = water;

        coefWaterCoffee = 16;

        Preconditions.checkNotNull(water);
        grains = water / coefWaterCoffee;

        this.water += grains * 2;

        lastTime = 0;
        time = getFloweringTime();

        puringTime = PURING_TIME - time;
        puringTimePart = puringTime / 3;

        usedWater = grains * 2;
        waterPart = (this.water - usedWater) / 3;

        state = WAITING_FOR_START;
    }

    public int getFloweringTime() {
        return 45;
    }

    private int getMin(int time) {
        return time / 60;
    }

    private int getSec(int time) {
        return time % 60;
    }

    private String getPeriodStartTime() {
        return String.format(getTimeFormat(), getMin(lastTime), getSec(lastTime));
    }

    private String getPeriodEndTime() {
        return String.format(getTimeFormat(), getMin(time), getSec(time));
    }

    private String getTimeFormat() {
        return "%02d:%02d";
    }

    public String getMessage() {
        return String.format("%s - %s, use water: %d ml",
                getPeriodStartTime(),
                getPeriodEndTime(),
                usedWater);
    }

    public void pourWater() {
        lastTime = time;
        time += puringTimePart;
        usedWater += waterPart;

        if (time >= PURING_TIME) {
            usedWater = water;
            state = WAITING_FOR_END;
        }
    }

    public void start() {
        state = STARTED;
    }


    public boolean isWatingForWater() {
        return this.state == WAITING_FOR_WATER;
    }

    public boolean isWaitingForStart() {
        return this.state == WAITING_FOR_START;
    }

    public boolean isStarted() {
        return this.state == STARTED;
    }

    public int getTimeForWating() {
        return 60;
    }
}
