package spotinrussia.spotinrussia;

import jdk.jfr.ContentType;
import me.dynomake.yookassa.Yookassa;
import me.dynomake.yookassa.exception.BadRequestException;
import me.dynomake.yookassa.exception.UnspecifiedShopInformation;
import me.dynomake.yookassa.model.Amount;
import me.dynomake.yookassa.model.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery;
import org.telegram.telegrambots.meta.api.objects.payments.SuccessfulPayment;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.processing.SupportedSourceVersion;
import javax.servlet.annotation.HandlesTypes;
import javax.swing.text.html.Option;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
public class TeleBot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String key;
    @Value("${yum.shopArticleId}")
    private String shopArticleId;
    @Value("${yum.shopId}")
    private String shopID;
    private String username;
    private String name1;
    private String email = "0";
    private String password = "0";
    private String subTime;
    private Long chatId = 0L;

    @Value("${telegram.webhook-path}")
    String webhookPath;

    private HashMap<String , String> urls = new HashMap<>();

    public void sendInfoToBogdan(String username , String name1) throws FileNotFoundException {
        SendMessage sm = new SendMessage();
        sm.setText("Новый клиент: "+username+"\nE-mail: "+ email+"\n"
        +"Пароль: "+password+"\nСрок подписки:" + subTime );
        this.sendMessage(583420031 ,sm.getText()+"\nСсылка на профиль ТГ: "
        +"https://t.me/"+name1);
    }

    public void sendInvoice(long chatId) {
        SendInvoice sendInvoice = new SendInvoice();
        if (subTime.equals("1MONTH")) {
            sendInvoice.setChatId(chatId);
            sendInvoice.setTitle("SPOTIFY\uD83D\uDFE2");
            sendInvoice.setDescription("Подписка на 1 месяц⏳");
            sendInvoice.setPayload("Custom-Payload1");
            sendInvoice.setProviderToken("1744374395:TEST:8cf11131f9526009454d");
            sendInvoice.setStartParameter("Custom-Start-Parameter1");
            sendInvoice.setCurrency("RUB");
            List<LabeledPrice> prices = new ArrayList<>();
            prices.add(new LabeledPrice("Итоговая цена", 350*100));
            sendInvoice.setPrices(prices);
        }
        if (subTime.equals("3MONTH")) {
            sendInvoice.setChatId(chatId);
            sendInvoice.setTitle("SPOTIFY\uD83D\uDFE2");
            sendInvoice.setDescription("Подписка на 3 месяца⏳");
            sendInvoice.setPayload("Custom-Payload1");
            sendInvoice.setProviderToken("1744374395:TEST:8cf11131f9526009454d");
            sendInvoice.setStartParameter("Custom-Start-Parameter1");
            sendInvoice.setCurrency("RUB");
            List<LabeledPrice> prices = new ArrayList<>();
            prices.add(new LabeledPrice("Итоговая цена", 840*100));
            sendInvoice.setPrices(prices);
        }
        if (subTime.equals("6MONTH")) {
            sendInvoice.setChatId(chatId);
            sendInvoice.setTitle("SPOTIFY\uD83D\uDFE2");
            sendInvoice.setDescription("Подписка на 6 месяцев⏳");
            sendInvoice.setPayload("Custom-Payload1");
            sendInvoice.setProviderToken("1744374395:TEST:8cf11131f9526009454d");
            sendInvoice.setStartParameter("Custom-Start-Parameter1");
            sendInvoice.setCurrency("RUB");
            List<LabeledPrice> prices = new ArrayList<>();
            prices.add(new LabeledPrice("Итоговая цена", 1450*100));
            sendInvoice.setPrices(prices);
        }
        try {
            System.out.println(sendInvoice.getProviderToken());
            execute(sendInvoice);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void answerPreCheckoutQuery(String preCheckoutQueryId) {
        AnswerPreCheckoutQuery answerPreCheckoutQuery = new AnswerPreCheckoutQuery();
        answerPreCheckoutQuery.setPreCheckoutQueryId(preCheckoutQueryId);
        answerPreCheckoutQuery.setOk(true);
        try {
            execute(answerPreCheckoutQuery);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }



    public SendMessage confirmation(long chatId) throws InterruptedException {
        Thread.sleep(7000);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        var buttonStart1 = new InlineKeyboardButton();
        buttonStart1.setText("Подтвердить оплату");
        buttonStart1.setCallbackData("CONFIRM");
        rowInLine.add(buttonStart1);
        rowsInLine.add(rowInLine);
        markup.setKeyboard(rowsInLine);
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setReplyMarkup(markup);
        message.setChatId(String.valueOf(chatId));
        message.setText("Подписка на Spotify\uD83D\uDFE2\nПослу оплаты нажмите на кнопку\uD83D\uDC47");
        return message;
    }



    @Override
    public void onUpdateReceived(Update update) {
        urls.put("1MONTH" , "К оплате: 350 руб\n\uD83D\uDCB3 https://yoomoney.ru/to/4100118421280688");
        urls.put("3MONTH" , "К оплате: 840 руб\n\uD83D\uDCB3 https://yoomoney.ru/to/4100118421280688");
        urls.put("6MONTH" , "К оплате: 1450 руб\n\uD83D\uDCB3 https://yoomoney.ru/to/4100118421280688");


        if (update.hasPreCheckoutQuery()) {
            AnswerPreCheckoutQuery answerPreCheckoutQuery = new AnswerPreCheckoutQuery();
            answerPreCheckoutQuery.setPreCheckoutQueryId(update.getPreCheckoutQuery().getId());
            answerPreCheckoutQuery.setOk(true);
            answerPreCheckoutQuery.setErrorMessage("ОШИБКА ОПЛАТЫ");
            SendMessage sm = new SendMessage();
            sm.setChatId(chatId);
            chatId = 0L;
            sm.setText("Ожидай подключения!\uD83C\uDF10\uD83E\uDD1D Если у тебя возникли вопросы - пиши @spotikTvoi\uD83E\uDD17");
            try {
                execute(answerPreCheckoutQuery);
                execute(sm);
                sendInfoToBogdan(username , name1);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        if (update.hasMessage() && update.getMessage().hasText()) {

             if (update.getMessage().getText().equals("/start")) {
                     try {
                         username = update.getMessage().getChat().getFirstName();
                         name1 = update.getMessage().getChat().getUserName();
                         chatId = update.getMessage().getChatId();
                         startCommandReceived(update.getMessage().getChatId(), update.getMessage().getChat().getFirstName());
                     } catch (FileNotFoundException e) {
                         throw new RuntimeException(e);
                     }
             } else if (email.equals("-1")) {
                 email = update.getMessage().getText();
                 password = "-1";
                 SendMessage sm = new SendMessage();
                 sm.setText("Супер! Теперь введи пароль от аккаунта Spotify\uD83D\uDFE2");
                 sm.setChatId(update.getMessage().getChatId());
                 try {
                     execute(sm);
                 } catch (TelegramApiException e) {
                     throw new RuntimeException(e);
                 }
             } else if (password.equals("-1")) {
                 password = update.getMessage().getText();
                 SendMessage sm = new SendMessage();
                 String link = urls.get(subTime);
                 sm.setText("Настало время оплатить подписку \uD83D\uDE0F \nЕсли при вводе данных была совершена ошибка, используй команду /start❗");
                 sm.setChatId(update.getMessage().getChatId());
                 try {
                     execute(sm);
                     sendInvoice(update.getMessage().getChatId());
                     //execute(confirmation(update.getMessage().getChatId()));
                 } catch (TelegramApiException e) {
                     throw new RuntimeException(e);
                 }
                 System.out.println(password);
             } else if (email.equals("-2")) {
                 email = update.getMessage().getText();
                 password = "-2";
                 SendMessage sm = new SendMessage();
                 sm.setText("Супер! Теперь введи пароль для нового аккаунта Spotify\uD83D\uDFE2");
                 sm.setChatId(update.getMessage().getChatId());
                 try {
                     execute(sm);
                 } catch (TelegramApiException e) {
                     throw new RuntimeException(e);
                 }
             } else if (password.equals("-2")) {
                 password = update.getMessage().getText();
                 SendMessage sm = new SendMessage();
                 sm.setChatId(update.getMessage().getChatId());
                 String link = urls.get(subTime);
                 sm.setText("Настало время оплатить подписку \uD83D\uDE0F \nЕсли при вводе данных была совершена ошибка, используй команду /start❗");
                 try {
                     execute(sm);
                     sendInvoice(update.getMessage().getChatId());

                     // execute(confirmation(update.getMessage().getChatId()));
                 } catch (TelegramApiException e) {
                     throw new RuntimeException(e);
                 }
             }
        } else if(update.hasCallbackQuery()){
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatIdCallback = update.getCallbackQuery().getMessage().getChatId();
            if (callbackData.equals("1MONTH")) {
                subTime = "Подписка на Spotify (1 месяц)";
                try {
                    sendMessageAccount(chatIdCallback , callbackData);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            if (callbackData.equals("3MONTH")) {
                subTime = "Подписка на Spotify (3 месяца)";
                try {
                    sendMessageAccount(chatIdCallback , callbackData);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            if (callbackData.equals("6MONTH")) {
                subTime = "Подписка на Spotify (6 месяцев)";
                try {
                    sendMessageAccount(chatIdCallback , callbackData);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            if (callbackData.equals("YES")) {
              email = "-1";
                SendMessage sm = new SendMessage();
                sm.setText("Введи эл.почту (от Spotify\uD83D\uDFE2)");
                sm.setChatId(chatIdCallback);
                try {
                    execute(sm);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
            if (callbackData.equals("NO")) {
                email = "-2";
                SendMessage sm = new SendMessage();
                sm.setText("Введи эл.почту (для создания аккаунта на Spotify\uD83D\uDFE2)");
                sm.setChatId(chatIdCallback);
                try {
                    execute(sm);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
            if (callbackData.equals("CONFIRM")) {
                try {
                    SendMessage sm = new SendMessage();
                    sm.setText("Спасибо за покупку подписки! Ожидайте подключения к Spotify\uD83D\uDFE2)");
                    sm.setChatId(chatIdCallback);
                    sendInfoToBogdan(username , name1);
                    execute(sm);
                } catch (FileNotFoundException | TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public String getBotToken() {
        return key;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    public void getEmailFromUser () {

    }

    public void getPasswordFromUser () {

    }

    public void startCommandReceived(long chatId, String firstName) throws FileNotFoundException {
        String answer = "Привет, " + firstName + " \uD83D\uDC4B \n\nТы меломан и хочешь быть в курсе последних релизов мировых звезд? \uD83C\uDFA7⭐ \nТогда оформляй подписку на Spotify\uD83D\uDFE2 у нас! \n\nНа сколько месяцев покупаете подписку? \n1 месяц - 350 руб\n3 месяца - 840 руб (280/мес)\n6 месяцев - 1450 руб (240/мес)\nОтзывы тут: https://t.me/spotikrussia";
        sendMessage(chatId, answer);
    }

    public void InputEmailAnswer(long chatId , Message msg) throws TelegramApiException {

    }

    public void InputPasswordAnswer(long chatId , Message msg) throws TelegramApiException {

    }

    public void sendMessageAccount (long chatId , String callback) throws FileNotFoundException {
        this.subTime = callback;
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Отлично! У тебя уже есть аккаунт в Spotify?");
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        var buttonStart1 = new InlineKeyboardButton();
        buttonStart1.setText("Да");
        buttonStart1.setCallbackData("YES");
        var buttonStart3 = new InlineKeyboardButton();
        buttonStart3.setText("Нет");
        buttonStart3.setCallbackData("NO");
        rowInLine.add(buttonStart1);
        rowInLine.add(buttonStart3);
        rowsInLine.add(rowInLine);
        markup.setKeyboard(rowsInLine);
        message.setReplyMarkup(markup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    private void sendMessage(long chatId, String messageText) throws FileNotFoundException {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        var buttonStart1 = new InlineKeyboardButton();
        buttonStart1.setText("1 месяц");
        buttonStart1.setCallbackData("1MONTH");
        var buttonStart3 = new InlineKeyboardButton();
        buttonStart3.setText("3 месяца");
        buttonStart3.setCallbackData("3MONTH");
        var buttonStart6 = new InlineKeyboardButton();
        buttonStart6.setText("6 месяцев");
        buttonStart6.setCallbackData("6MONTH");
        rowInLine.add(buttonStart1);
        rowInLine.add(buttonStart3);
        rowInLine.add(buttonStart6);
        rowsInLine.add(rowInLine);
        markup.setKeyboard(rowsInLine);
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(messageText);
        message.setReplyMarkup(markup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }
}
