package yo.proj.handler;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import yo.proj.entity.User;
import yo.proj.keyboards.InlineKeyboard;
import yo.proj.keyboards.ReplyKeyboard;
import yo.proj.repository.UserRepository;
import yo.proj.utils.CustomStrUtils;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Класс обработчик для всех текстовых сообщений
 * ВСЁ, что не инлайн кнопки, считаются таковыми(текстовыми), даже команды начинающиеся с "/"
 * (Inline - Клавиатура привязанная к конкретному сообщению бота)
 */
@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageHandler implements Handler {

    final UserRepository repository;
    final Pattern patternToSayHello;
    final Pattern patternToAdd;
    final ReplyKeyboard replyKeyboard;
    final InlineKeyboard inlineKeyboard;

    MessageHandler(UserRepository repository, ReplyKeyboard replyKeyboard, InlineKeyboard inlineKeyboard) {
        this.repository = repository;
        this.patternToSayHello = Pattern.compile("\\w*");
        this.inlineKeyboard = inlineKeyboard;
        this.patternToAdd = Pattern.compile("@\\w*");
        this.replyKeyboard = replyKeyboard;
    }

    @Override
    public BotApiMethod<?> handle(BotApiObject message) {
        if (message instanceof Message msg) {
            log.info("Message received with text \"{}\" from user {}", msg.getText(), msg.getChatId());
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(msg.getChatId().toString());
            String text = msg.getText();
            // TODO switch case
            if (text.equals("/start")) {
                log.info("printing hello text for user {}", msg.getChatId());
                sendMessage.setText(msg.getChat().getFirstName() + """
                        , привет!! Тут ты можешь добавлять в друзья своих
                        друзей и слать им свои приветики
                        Давай начнем)""");
                sendMessage.setReplyMarkup(replyKeyboard.getKeyboard());
            }
//            else if ((patternToSayHello.matcher(text)).matches()) {
//                throw new Trigger(msg.getText());
//            }
            else if ((patternToAdd.matcher(text)).matches()) {
                //TODO понять как это сделать красиво
                if (repository.getUserById(text.substring(1)) != null) {
                    if (!repository.existsById(msg.getChatId())) {
                        log.info("creating new user and adding his first friend for user {}", msg.getChatId());
                        repository.save(new User(msg.getChatId(), msg.getChat().getUserName(), text));
                    } else {
                        log.info("adding new friend for user {}", msg.getChatId());
                        User user = repository.getById(msg.getChatId());
                        user.setFriendsList(user.getFriendsList() + text);
                        repository.save(user);
                    }
                    sendMessage.setText("все сохранилось, что нибудь еще ?");
                    sendMessage.setReplyMarkup(replyKeyboard.getKeyboard());
                } else {
                    sendMessage.setText("""
                            твоего друга еще нет в этом боте:(
                            поэтому сначала пришласи его сюда, и я с радостью его добавлю в твои друзья""");
                    sendMessage.setReplyMarkup(replyKeyboard.getKeyboard());
                }
            }
            else if (text.equals("Добавить друга")) {
                log.info("asking for friends username for user {}", msg.getChatId());
                sendMessage.setText("мне нужен ник друга, пиши");
                sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
            } else if (text.equals("Показать всех друзей")) {
                log.info("returning friends list for user {}", msg.getChatId());
                List<String> list = CustomStrUtils.getListUserFriends(repository.getById(msg.getChatId()).getFriendsList());
//                sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
                sendMessage.setText("все твои друзья");
                sendMessage.setReplyMarkup(inlineKeyboard.getKeyboard(list));
//                sendMessage.setReplyMarkup(replyKeyboard.getKeyboard(list.subList(1, list.size())));
                System.out.println(list);
                System.out.println(list.subList(1, list.size()));
//                System.out.println(replyKeyboard.getKeyboard(list.subList(1, list.size())));
            } else if (text.equals("Удалить друга")) { //объединить с добавлением
                sendMessage.setText("мне нужен ник друга, пиши");
                sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
                repository.deleteById(msg.getChatId());
            }
            return sendMessage;
        }
        return null;
    }
}