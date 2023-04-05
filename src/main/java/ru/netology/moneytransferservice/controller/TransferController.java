package ru.netology.moneytransferservice.controller;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netology.moneytransferservice.cards.Cards;
import ru.netology.moneytransferservice.service.Result;
import ru.netology.moneytransferservice.service.TransferService;

import java.util.UUID;

@CrossOrigin
@RestController

public class TransferController {
    TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }
    @RequestMapping("/transfer")
    @PostMapping
    public ResponseEntity<String> getRequest(@RequestBody String request) throws JSONException {
        System.out.println(request);
        //{"cardFromNumber":"1234123412341234","cardToNumber":"5678567856785678","cardFromCVV":"123","cardFromValidTill":"06/26","amount":{"currency":"RUR","value":50000}}
        //transferService.goTransfer(request);


        //JSONObject json = new JSONObject("operationId:"+operationId);

        //System.out.println("JSON: "+json.toString());

        Result res = transferService.goTransfer(request);
        int operationId = transferService.getOperationId();
        switch (res) {
            case OK:
                return new ResponseEntity<>(String.format("{\"description\":\"Success transfer\"," +
                        " \"operationId\":%s}", Integer.toString(operationId)), HttpStatus.OK);
            case INPUT_DATA_ERROR:
                return new ResponseEntity<>(String.format("{\"description\":\"Error input data\"," +
                        "\"message\":\"Error customer message : INPUT_DATA_ERROR\", " +
                        "\"id\":%s}", Integer.toString(operationId)), HttpStatus.BAD_REQUEST);
            default:
                return new ResponseEntity<>(String.format("{\"description\":\"Error transfer\"," +
                        "\"message\":\"Error customer message\", " +
                        "\"id\":%s}", Integer.toString(operationId)), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping("/confirmOperation")
    @PostMapping
    public ResponseEntity<String> getConfirm(@RequestBody String request) throws JSONException {
        //transferService.goTransfer(request);
        System.out.println(request);
        //{"code":"0000","operationId":12345}
        JSONObject jsonObject = new JSONObject(request);
        String code = (String) jsonObject.get("code");

        int operationId = transferService.getOperationId();

 //       return new ResponseEntity<>(String.format("{\"operationId\":%s}",operationId), HttpStatus.OK);
        return new ResponseEntity<>(String.format("{\"description\":\"Success confirmation\" ,\"operationId\":%s}",Integer.toString(operationId)), HttpStatus.OK);
    }

    /*
        @GetMapping
        public List<Post> all() {
            return service.all();
        }

        @GetMapping("/{id}")
        public Post getById(@PathVariable long id) {
            return service.getById(id);
        }

        @PostMapping
        public Post save(@RequestBody Post post) {
            return service.save(post);
        }



        @DeleteMapping("/{id}")
        public void removeById(@PathVariable long id) {
            service.removeById(id);
        }

 */

}
