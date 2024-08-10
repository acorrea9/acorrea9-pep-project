package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        app.post("/register", this::postRegisterAccountHandler);
        app.post("/login", this::postLoginAccountHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::patchMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccountIdHander);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    /**
     * Handler to process new User registrations.
     * The Jackson ObjectMapper will automatically convert the JSON of the POST request into an Account object.
     * If AccountService returns a null account (meaning creating an Account was unsuccessful), the API will return a 400
     * message (client error).
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void postRegisterAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account registeredAccount = accountService.registerAccount(account);
        if(registeredAccount != null) {
            ctx.json(mapper.writeValueAsString(registeredAccount));
        }
        else {
            ctx.status(400);
        }
    }

    /**
     * Handler to process new User logins.
     * The Jackson ObjectMapper will automatically convert the JSON of the POST request into an Account object.
     * If AccountService returns a null account (meaning verifying/logging in an Account was unsuccessful), 
     * the API will return a 401 message (Unauthorized).
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void postLoginAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loggedInAccount = accountService.loginAccount(account);
        if(loggedInAccount != null) {
            ctx.json(mapper.writeValueAsString(loggedInAccount));
        }
        else {
            ctx.status(401);
        }
    }

    /**
     * Handler to process the creation of new messages.
     * The Jackson ObjectMapper will automatically convert the JSON of the POST request into a Message object.
     * If MessageService returns a null message (meaning creating a message was unsuccessful), the API will return a 400
     * message (client error).
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.createMessage(message);
        if(addedMessage != null) {
            ctx.json(mapper.writeValueAsString(addedMessage));
        }
        else {
            ctx.status(400);
        }
    }

    /**
     * Handler to retrieve all messages.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     */
    private void getAllMessagesHandler(Context ctx) {
        ctx.json(messageService.getAllMessages());
    }

    /**
     * Handler to retrieve a message by its ID.
     * The pathParam() method will extract the parameter from the URI in order to get the message_id.
     * If MessageService returns a null message (meaning retrieving a Message was unsuccessful), 
     * the response body will be empty.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message messageById = messageService.getMessageById(id);
        if(messageById != null) {
            ctx.json(mapper.writeValueAsString(messageById));
        }
    }

    /**
     * Handler to delete a message by its ID.
     * The pathParam() method will extract the parameter from the URI in order to get the message_id.
     * If MessageService returns a null message (meaning the deletion of a Message was unsuccessful), 
     * the response body will be empty.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void deleteMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessage(id);
        if(deletedMessage != null) {
            ctx.json(mapper.writeValueAsString(deletedMessage));
        }
    }

    /**
     * Handler to update a message by its ID.
     * The pathParam() method will extract the parameter from the URI in order to get the message_id.
     * If MessageService returns a null message (meaning the updating of a Message was unsuccessful), 
     * the API will return a 400 message (client error).
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void patchMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message updatedMessage = messageService.updateMessage(id, message);
        if(updatedMessage != null) {
            ctx.json(mapper.writeValueAsString(updatedMessage));
        }
        else {
            ctx.status(400);
        }
    }

    /**
     * Handler to retrieve all messages written by a particular user.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     */
    private void getAllMessagesByAccountIdHander(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("account_id"));
        ctx.json(messageService.getAllMessagesByAccountId(id));
    }
}