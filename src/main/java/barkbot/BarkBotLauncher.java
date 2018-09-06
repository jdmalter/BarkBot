package barkbot;

import barkbot.model.Request;
import barkbot.model.Response;
import barkbot.provider.HandlerProvider;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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

        try {
            final BarkBot barkBot = applicationContext.getBean(BarkBot.class);
            barkBot.accept(request.getBody());

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
