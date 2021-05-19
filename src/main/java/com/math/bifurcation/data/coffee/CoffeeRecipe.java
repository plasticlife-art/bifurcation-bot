package com.math.bifurcation.data.coffee;

import com.math.bifurcation.data.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author Leonid Cheremshantsev
 */

@Builder
public class CoffeeRecipe {

    @Getter
    private final User owner;
    @Getter
    private final Date create_ts;
    @Getter
    @Setter
    private Long id;
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
