package telegram;

import telegram.Command.HelpCommand;
import telegram.Command.SayCommand;
import telegram.Command.SettingsCommand;
import telegram.Command.StartCommand;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import telegram.nonCommand.Settings;
import telegram.nonCommand.NonCommand;
import aplication.Utils;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.util.HashMap;
import java.util.Map;

/**
 * Собственно, бот
 */
@Slf4j
public final class Bot extends TelegramLongPollingCommandBot {

    @Getter
    private final String BOT_NAME;
    @Getter
    private final String BOT_TOKEN;

    @Getter
    private static final Settings defaultSettings = new Settings("Settings");
    private final NonCommand nonCommand;

    /**
     * Настройки файла для разных пользователей. Ключ - уникальный id чата
     */
    @Getter
    private static Map<Long, Settings> userSettingsMap;

    public Bot(String botName, String botToken) {
        super();
        log.debug("Конструктор суперкласса отработал");
        this.BOT_NAME = botName;
        this.BOT_TOKEN = botToken;
        log.debug("Имя и токен присвоены");

        this.nonCommand = new NonCommand();
        log.debug("Класс обработки сообщения, не являющегося командой, создан");

        register(new StartCommand("start", "Старт"));
        log.debug("Команда start создана");

        register(new SayCommand("say", "Сказать привет"));
        log.debug("Команда sayHi создана");

        register(new SettingsCommand("settings", "Настройки"));
        log.debug("Команда settings создана");

        register(new HelpCommand("help","Помощь"));
        log.debug("Команда help создана");

        userSettingsMap = new HashMap<>();
        log.info("Бот создан!");
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    /**
     * Ответ на запрос, не являющийся командой
     */
    @Override
    public void processNonCommandUpdate(Update update) {
        Message msg = update.getMessage();
        Long chatId = msg.getChatId();
        String userName = Utils.getUserName(msg);

        String answer = nonCommand.nonCommandExecute(chatId, userName, msg.getText());
        setAnswer(chatId, userName, answer);
    }

    /**
     * Получение настроек по id чата. Если ранее для этого чата в ходе сеанса работы бота настройки не были установлены,
     * используются настройки по ум
     * олчанию
     */
    public static Settings getUserSettings(Long chatId) {
        Map<Long, Settings> userSettings = Bot.getUserSettingsMap();
        Settings settings = userSettings.get(chatId);
        if (settings == null) {
            return defaultSettings;
        }
        return settings;
    }

    /**
     * Отправка ответа
     * @param chatId id чата
     * @param userName имя пользователя
     * @param text текст ответа
     */
    private void setAnswer(Long chatId, String userName, String text) {
        SendMessage answer = new SendMessage();
        answer.setText(text);
        answer.setChatId(chatId.toString());
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            log.error(String.format("Ошибка %s. Сообщение, не являющееся командой. Пользователь: %s", e.getMessage(),
                    userName));
            e.printStackTrace();
        }
    }
}