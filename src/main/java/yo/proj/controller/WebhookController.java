package yo.proj.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import yo.proj.bot.YoBot;


/**
 * Класс для получения всех апдейтов от телеграма, то есть от юзера, который пишет боту
 */
@Slf4j
@RestController
public record WebhookController(YoBot bot) {

    @PostMapping("/")
    public BotApiMethod<?> onUpdate(@RequestBody Update update) {
        log.info("Update received");
        return bot.onWebhookUpdateReceived(update);
    }
}
