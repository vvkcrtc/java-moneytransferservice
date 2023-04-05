package ru.netology.moneytransferservice.service;

import org.springframework.stereotype.Service;
import ru.netology.moneytransferservice.cards.Card;
import ru.netology.moneytransferservice.cards.Cards;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import ru.netology.moneytransferservice.cards.Operation;

@Service
public class TransferService {
    Cards cards;
    protected int operationId = 0;
    protected int bankCommPercent = 1;
    Logger logger = new Logger("transfer.log.txt");
    public TransferService(Cards cards) {
        this.cards = cards;
        this.cards.addCard(new Card("0000000000000001","05/28","987","RUR"));
        this.cards.addCard(new Card("0000000000000002","05/28","123","RUR"));
        this.cards.cardOperation("0000000000000001","05/28","987",10000, Operation.ADD);
        this.cards.cardOperation("0000000000000002","05/28","123",10000, Operation.ADD);
    }

    protected String createMessage(String cardFrom, String cardFromValidTill, String cardFromCVV,
                                   String cardTo, float value, float bankComm, Result status) {
        String res = "Ошибка";
        if(status == Result.OK) {
        res ="id : " + operationId + " Перевод с карты " +
                    cardFrom + " действ. до " + cardFromValidTill + " CVV " +
                    cardFromCVV + " на карту " + cardTo + " , сумма " + Float.toString(value) + " коммиссия " + bankComm;
        } else if (status == Result.INPUT_DATA_ERROR) {
            res ="id : " + operationId + " Ошибка перевода " +
                    cardFrom + " действ. до " + cardFromValidTill + " CVV " +
                    cardFromCVV + " не верно указаны данные перевода";
        } else {
            res ="id : " + operationId + " Ошибка перевода " + cardFrom + " действ. до " + cardFromValidTill + " CVV " +
                    cardFromCVV + " на карту " + " операция не выполнена";
        }

        return res;
    }

    public Result goTransfer(String request) throws JSONException {
        operationId++;
        JSONObject jsonObject = new JSONObject(request);
        String cardFrom = (String) jsonObject.get("cardFromNumber");
        String cardFromCVV = (String) jsonObject.get("cardFromCVV");
        String cardFromValidTill = (String) jsonObject.get("cardFromValidTill");
        JSONObject amount  = (JSONObject) jsonObject.get("amount");
        String currency = (String) amount.get("currency");
        System.out.println(amount.get("value"));
        int tmpVal = (int) amount.get("value")/100;
        float value =  (float) tmpVal;
        float bankComm = (value*bankCommPercent)/100;
        String cardTo = (String) jsonObject.get("cardToNumber");
        if(cards.cardOperation(cardFrom, cardFromValidTill, cardFromCVV,0,Operation.VALID_CHECK) &&
                cards.getCard(cardTo) != null) {
            if(cards.transfer(cardFrom, cardFromValidTill, cardFromCVV, cardTo, value, bankComm )) {
                logger.printLog(createMessage(cardFrom, cardFromValidTill, cardFromCVV, cardTo, value, bankComm,Result.OK ));
                return Result.OK;
            } else {
                logger.printLog(createMessage(cardFrom, cardFromValidTill, cardFromCVV, cardTo, value, bankComm,Result.OPERATION_ERROR ));
                return Result.OPERATION_ERROR;
            }

        } else {
            logger.printLog(createMessage(cardFrom, cardFromValidTill, cardFromCVV, cardTo, value, bankComm,Result.INPUT_DATA_ERROR ));
            return Result.INPUT_DATA_ERROR;
        }

    }

    public int getOperationId() {
        return operationId;
    }


}
