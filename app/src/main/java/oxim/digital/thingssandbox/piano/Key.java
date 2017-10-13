package oxim.digital.thingssandbox.piano;

import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.annimon.stream.Optional;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import oxim.digital.thingssandbox.base.GpioAction;

public final class Key {

    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int FOUR = 4;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ONE, TWO, FOUR})
    @interface KeyValue { }

    public interface OnKeyInteractionListener {

        void onKeyDown(final @KeyValue int keyValue);

        void onKeyUp(final @KeyValue int keyValue);
    }

    private final Gpio gpio;
    private final int keyValue;

    private Optional<OnKeyInteractionListener> keyInteractionListener = Optional.empty();

    public Key(final String gpio, final PeripheralManagerService peripheralService, final @KeyValue int keyValue) throws IOException {
        this.gpio = setupGpio(peripheralService, gpio);
        this.keyValue = keyValue;
        this.gpio.registerGpioCallback(new GpioAction(this::onGpioStateChange));
    }

    private Gpio setupGpio(final PeripheralManagerService peripheralManagerService,
                           final String gpioName) throws IOException {
        final Gpio gpio = peripheralManagerService.openGpio(gpioName);
        gpio.setDirection(Gpio.DIRECTION_IN);
        gpio.setEdgeTriggerType(Gpio.EDGE_BOTH);
        gpio.setActiveType(Gpio.ACTIVE_HIGH);
        return gpio;
    }

    private void onGpioStateChange(final Gpio gpio) {
        try {
            onGpioValue(gpio.getValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onGpioValue(final boolean isPressed) {
        if (isPressed) {
            keyInteractionListener.ifPresent(interactionListener -> interactionListener.onKeyDown(keyValue));
        } else {
            keyInteractionListener.ifPresent(interactionListener -> interactionListener.onKeyUp(keyValue));
        }
    }

    public void setKeyInteractionListener(final @Nullable OnKeyInteractionListener keyInteractionListener) {
        this.keyInteractionListener = Optional.ofNullable(keyInteractionListener);
    }
}
