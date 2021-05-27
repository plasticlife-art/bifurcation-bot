package com.math.bifurcation.data.coffee;

import com.math.bifurcation.BifurcationBotApplication;
import com.math.bifurcation.config.BeansConfig;
import com.math.bifurcation.data.coffee.recipe.CoffeeRecipeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Leonid Cheremshantsev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BeansConfig.class, BifurcationBotApplication.class})
public class CoffeeRecipeRepositoryTest {

    @Autowired
    private CoffeeRecipeRepository coffeeRecipeRepository;

    @Test
    public void getCount() {
        coffeeRecipeRepository.getCount();
    }
}