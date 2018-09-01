package barkbot.factory;

import com.amazonaws.services.rekognition.model.Label;

public class RandomLabelFactory {
    private RandomLabelFactory() {
        throw new UnsupportedOperationException();
    }

    public static Label create() {
        return new Label()
                .withConfidence(RandomPrimitiveFactory.createFloat() * 100)
                .withName(RandomPrimitiveFactory.createString());
    }
}
