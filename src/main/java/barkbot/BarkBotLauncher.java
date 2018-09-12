package barkbot;

import barkbot.model.Message;
import barkbot.model.Request;
import barkbot.model.Response;
import barkbot.provider.HandlerProvider;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

@Slf4j
public class BarkBotLauncher implements RequestHandler<Request, Response> {
    @NonNull
    private final ApplicationContext applicationContext;

    public BarkBotLauncher() {
        this(new AnnotationConfigApplicationContext(HandlerProvider.class));
    }

    public BarkBotLauncher(@NonNull final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Response handleRequest(@NonNull final Request request, final Context context) {
        log.info("request={}", request);

        final Message message;
        try {
            final ObjectMapper objectMapper = applicationContext.getBean(ObjectMapper.class);
            message = objectMapper.readValue(request.getBody(), Message.class);

        } catch (final JsonParseException | JsonMappingException e) {
            log.error(e.getMessage(), e);
            return Response.clientError();

        } catch (final IOException e) {
            log.error(e.getMessage(), e);
            return Response.serverError();
        }

        try {
            final BarkBot barkBot = applicationContext.getBean(BarkBot.class);
            barkBot.accept(message);

        } catch (final IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            return Response.clientError();

        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            return Response.serverError();
        }

        return Response.success();
    }
}
