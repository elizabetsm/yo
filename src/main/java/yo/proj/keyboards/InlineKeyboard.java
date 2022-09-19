package yo.proj.keyboards;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для формирования инлайн клавиатуры, то есть той, что присылается вместе
 * с ответным сообщением в боте
 */
@Component
public class InlineKeyboard {

    public InlineKeyboardMarkup getKeyboard(List<String> list) {

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        list.forEach(friend -> rowList.add(getOneButton(friend, friend)));

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    List<InlineKeyboardButton> getOneButton(String text, String callbackText) {
        List<InlineKeyboardButton> one = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackText);
        one.add(button);
        return one;
    }
}
