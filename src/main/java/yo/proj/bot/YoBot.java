package yo.proj.bot;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotOptions;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import yo.proj.exception.Trigger;
import yo.proj.handler.CallbackQueryHandler;
import yo.proj.handler.Handler;
import yo.proj.handler.MessageHandler;

/**
 * класс для использования в {@link yo.proj.config.SpringConfig#springWebhookBot(SetWebhook, MessageHandler, CallbackQueryHandler)}
 */
//@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class YoBot extends SpringWebhookBot {

    final String webhook;
    final String token;
    final String name;

    // два некоторых обработчика
    final Handler messageHandler; //собщение словами (текстом, буквами некими)
    final Handler callbackQueryHandler;//инлайн клава
//    final ScheduledService service;



    public YoBot(SetWebhook setWebhook, String webhook, String token, String name, MessageHandler messageHandler,
                 CallbackQueryHandler callbackQueryHandler) {
        super(setWebhook);
        this.webhook = webhook;
        this.token = token;
        this.name = name;
        this.messageHandler = messageHandler;
        this.callbackQueryHandler = callbackQueryHandler;
//        this.service = service;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            return handleUpdate(update);
        } catch (IllegalArgumentException e) {
            return new SendMessage(update.getMessage().getChatId().toString(),
                    "BotMessageEnum.EXCEPTION_ILLEGAL_MESSAGE.getMessage()");
//        } catch (Trigger trigger) {
//            return sendYo(trigger.getMessage());
////            BotApiMethod
        } catch (Exception e) {
            log.error(e.getMessage());
            return new SendMessage(update.getMessage().getChatId().toString(),
                    "BotMessageEnum.EXCEPTION_WHAT_THE_FUCK.getMessage()");
        }
    }

    private BotApiMethod<?> handleUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return callbackQueryHandler.handle(callbackQuery);
        } else {
            Message message = update.getMessage();
            if (message != null) {
                return messageHandler.handle(update.getMessage());
            }
        }
        return null;
    }

    //падает потому что ссылка сейчас по факту https://api.telegram.org/botnull/sendmessage
    //  а должна быть https://api.telegram.org/botТОКЕНБОТА/sendmessage
    // берется из DefaultBotOptions, падает в DEfaultAbsSender#sendMethodRequest
//    public BotApiMethod<?> sendYo(String userName){
//        BotApiMethod<?> returnn = null;
//        try {
//            //ClassCastException
//            returnn = execute(sendHello());
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//        return returnn;
//    }
//
//    BotApiMethod<?> sendHello() {
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setChatId(String.valueOf(415907542));
//        sendMessage.setText("hello from lizka");
//        return sendMessage;
//    }

    @Override
    public String getBotPath() {
        return null;
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

//    @Scheduled(cron = "@daily")
//    public void checkDayNotifs(){
//        Map<Long, List<Birthday>> birthdayMap = service.findDailyNotifs();
//        if (!birthdayMap.isEmpty()){
//                birthdayMap.forEach((id, birthday) -> {
//                    SendMessage sendMessage = new SendMessage();
//                    sendMessage.setChatId(id.toString());
//                    sendMessage.setText("Список на сегодня" + birthday);
//                    try {
//                        execute(sendMessage);
//                    } catch (TelegramApiException e) {
//                        log.error("checkDayNotifs", e.getMessage());
//                    }
//                });
////                execute(service.sendNotif(birthdayMap));
//        }
    }

