package yo.proj.handler;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import yo.proj.entity.User;
import yo.proj.keyboards.ReplyKeyboard;
import yo.proj.repository.UserRepository;

import java.util.regex.Pattern;

/**
 * Класс обработчик для апдейтов прилетающих с кнопок с клавиатуры
 */
@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CallbackQueryHandler implements Handler {

    final ReplyKeyboard replyKeyboard;
    final Pattern pattern;
    final UserRepository repository;

    public CallbackQueryHandler(ReplyKeyboard replyKeyboard, UserRepository repository) {
        this.replyKeyboard = replyKeyboard;
        this.repository = repository;
        this.pattern = Pattern.compile("\\w*");
    }

    @Override
    public BotApiMethod<?> handle(BotApiObject callbackQuery) {
        if (callbackQuery instanceof CallbackQuery query) {
            Message msg = query.getMessage();
            log.info("CallbackQuery received with text {} and query data {}", msg.getText(), query.getData());
            SendMessage sendMessage = new SendMessage();
            // заменить айди на дружеский
//            sendMessage.setChatId(msg.getChatId().toString());

            if (pattern.matcher(query.getData()).matches()) {
                User user = repository.getUserById(query.getData());
//                sendMessage.setChatId(user.getId().toString());
                sendMessage.setChatId(String.valueOf(415907542));
                sendMessage.setText("Тебе приветики от " + query.getFrom().getUserName());
            } else {
                sendMessage.setChatId(msg.getChatId().toString());
                sendMessage.setText("что то пошла не так.... Напиши об этом @elizabetsm....");
            }
            return sendMessage;
        }
        return null;
    }
}
