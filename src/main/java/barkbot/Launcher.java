package barkbot;

import barkbot.model.Request;
import barkbot.provider.HandlerProvider;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import lombok.NonNull;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Launcher implements RequestHandler<Request, Void> {
    @NonNull
    private final ApplicationContext applicationContext;

    public Launcher() {
        this(new AnnotationConfigApplicationContext(HandlerProvider.class));
    }

    public Launcher(@NonNull final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Void handleRequest(@NonNull final Request request, final Context context) {
        final Handler handler = applicationContext.getBean(Handler.class);
        handler.accept(request.getBody());
        return null;
    }
}
