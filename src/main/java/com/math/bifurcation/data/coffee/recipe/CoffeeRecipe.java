package com.math.bifurcation.data.coffee.recipe;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

/**
 * @author Leonid Cheremshantsev
 */

@Builder
public class CoffeeRecipe {

    @Getter
    private final Long owner_user_id;
    @Getter
    private final Date create_ts;
    @Getter
    @Setter
    private UUID id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private Integer floweringWaterAmount;
    @Getter
    @Setter
    private Integer floweringTime;
    @Getter
    @Setter
    private Integer pouroverTime;
    @Getter
    @Setter
    private Integer beansAmount;
    @Getter
    @Setter
    private Integer temp;
}
