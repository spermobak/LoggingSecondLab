package telegram.Command;

import aplication.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;


public class HelpCommand extends ServiceCommand {
    private final Logger logger = LoggerFactory.getLogger(HelpCommand.class);

    @Deprecated
    public HelpCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        long time = System.currentTimeMillis();
        String userName = Utils.getUserName(user);

        logger.debug(String.format("Пользователь %s. Начато выполнение команды %s", userName,
                this.getCommandIdentifier()));
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                "Command list:\n"+
                        "/help\n"+
                        "/settings\n"+
                        "/start\n"+
                        "/say");
        time = System.currentTimeMillis() - time;
        logger.info(String.format("Пользователь %s. Завершено выполнение команды %s, время выполнения команды в милисекундах - {}", userName,
                this.getCommandIdentifier()), time);
    }
}