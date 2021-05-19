package com.math.bifurcation.telegram.base.handler;

import com.math.bifurcation.data.coffee.CoffeeTask;
import com.math.bifurcation.data.user.UserRepository;
import com.math.bifurcation.telegram.base.UpdateWrapper;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Predicate;


@Order(Ordered.HIGHEST_PRECEDENCE)
@Service
public class CoffeeHandler extends Handler {

    private final ScheduledExecutorService executorService;
    private final UserRepository userRepository;

    private final LinkedList<CoffeeTask> tasks = new LinkedList<>();


    public CoffeeHandler(MessageSource messages, UserRepository userRepository) {
        super(messages);
        this.userRepository = userRepository;

        this.executorService = initScheduler();
    }

    @Override
    public boolean support(UpdateWrapper update) {
        return isCommand(update, getMessage("handler.coffee.command"))
                || isStopCommand(update)
                || isTaskExist(update);
    }

    private boolean isStopCommand(UpdateWrapper update) {
        return isCommand(update, getMessage("handler.stop.command"));
    }

    private boolean isTaskExist(UpdateWrapper update) {
        return tasks.stream()
                .anyMatch(findTaskByChatId(update));
    }

    private CoffeeTask getTask(UpdateWrapper update) {
        return tasks.stream().filter(findTaskByChatId(update))
                .findAny()
                .orElseThrow(IllegalStateException::new);
    }

    private Predicate<CoffeeTask> findTaskByChatId(UpdateWrapper update) {
        return task -> task.getUser().equalsById(update.getChatId());
    }

    @Override
    public void handle(UpdateWrapper update) {
        if (isTaskExist(update)) {
            CoffeeTask task = getTask(update);

            if (isStopCommand(update)) {
                cancelTask(task);
            } else if (task.isWatingForWater()) {
                saveWaterCount(update, task);

                sendWaterAndGrainsNeeded(task);

                askForStart(update);
            } else if (task.isWaitingForStart()) {
                RemoveKeyboard(task);
                startTask(task);
            } else if (task.isStarted()) {
                sendCurrentTaskMessage(task);
            }
        } else {
            createTask(update);
            askWaterCount(update);
        }
    }

    private void RemoveKeyboard(CoffeeTask task) {
        api.sendRemoveKeyboard(task.getUser().getChatId(), "Begin...");
    }

    private void sendWaterAndGrainsNeeded(CoffeeTask task) {
        String chatId = task.getUser().getChatId();
        Integer water = task.getWater();
        Integer grains = task.getGrains();

        api.sendTextMessage(chatId, String.format("Water: %s ml", water));
        api.sendTextMessage(chatId, String.format("Сoffee beans: %s g", grains));
    }

    private void askForStart(UpdateWrapper update) {
        api.sendButton(update.getChatId(), "Click to start", "Start");
    }


    private void askWaterCount(UpdateWrapper update) {
        api.sendTextMessage(update.getChatId(), "How much coffee (in ml) do you want to get?");
    }

    private void createTask(UpdateWrapper update) {
        saveTask(update);
    }

    private void saveTask(UpdateWrapper update) {
        tasks.add(buildTask(update));
    }

    private CoffeeTask buildTask(UpdateWrapper update) {
        return new CoffeeTask(userRepository.findById(update.getChatIdLong()));
    }

    private void saveWaterCount(UpdateWrapper update, CoffeeTask task) {
        try {
            Integer water = Integer.valueOf(update.getText());
            task.setWater(water);
        } catch (NumberFormatException e) {
            api.sendReplyMessage(update, "Something is wrong with your volume...");
        }
    }

    private void cancelTask(CoffeeTask task) {
        tasks.remove(task);
    }

    private void startTask(CoffeeTask task) {
        executorService.execute(runTask(task));
    }

    private Runnable runTask(CoffeeTask task) {
        return () -> {
            try {
                task.start();

                String chatId = task.getUser().getChatId();

                api.sendCountDown(chatId);

                sendCurrentTaskMessage(task);
                sleepSeconds(task.getFloweringTime());

                do {
                    task.pourWater();
                    sendCurrentTaskMessage(task);
                    sleepSeconds(task.getPuringTimePart());
                } while (task.isStarted());

                api.sendTextMessage(chatId, "Wait for 1 minute...");

                sleepSeconds(task.getTimeForWating());

                api.sendTextMessage(chatId, "Your coffee is ready...");
                sleepSeconds(1);

                api.sendTextMessage(chatId, "Enjoy");

                api.sendCoffeeSticker(chatId);

                tasks.remove(task);
            } catch (InterruptedException e) {
                log.error("Error while coffee task", e);
            }
        };
    }

    private void sendCurrentTaskMessage(CoffeeTask task) {
        api.sendTextMessage(task.getUser().getChatId(), task.getMessage());
    }

}