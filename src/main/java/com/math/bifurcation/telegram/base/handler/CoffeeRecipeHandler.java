package com.math.bifurcation.telegram.base.handler;

import com.math.bifurcation.telegram.base.UpdateWrapper;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author Leonid Cheremshantsev
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Service
public class CoffeeRecipeHandler extends Handler {

    protected CoffeeRecipeHandler(MessageSource messages) {
        super(messages);
    }

    @Override
    public boolean support(UpdateWrapper update) {
        return isCommand(update, getMessage("handler.recipe.command"));
    }

    @Override
    public void handle(UpdateWrapper update) throws IOException {

    }
}
