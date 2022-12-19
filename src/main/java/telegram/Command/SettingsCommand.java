package telegram.Command;

import aplication.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import telegram.Bot;
import telegram.nonCommand.Settings;


/**
 * Команда получения текущих настроек
 */
public class SettingsCommand extends ServiceCommand {
    private Logger logger = LoggerFactory.getLogger(SettingsCommand.class);

    @Deprecated
    public SettingsCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        long time = System.currentTimeMillis();
        String userName = Utils.getUserName(user);

        logger.debug(String.format("Пользователь %s. Начато выполнение команды %s", userName,
                this.getCommandIdentifier()));

        Long chatId = chat.getId();
        Settings settings = Bot.getUserSettings(chatId);
        sendAnswer(absSender, chatId, this.getCommandIdentifier(), userName,
                String.format("*Current settings - %s*\n" +
                                "If you want to change this settings, write new \uD83D\uDC49",
                        settings.getSettings()));

        time = System.currentTimeMillis() - time;
        logger.info(String.format("Пользователь %s. Завершено выполнение команды %s, время выполнения команды в милисекундах - {}", userName,
                this.getCommandIdentifier()), time);
    }
}