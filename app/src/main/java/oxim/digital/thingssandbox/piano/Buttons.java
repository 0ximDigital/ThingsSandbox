package oxim.digital.thingssandbox.piano;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

public class Buttons {

    public final Gpio leftButton;
    public final Gpio rightButton;

    public Buttons(final PeripheralManagerService peripheralManagerService,
                   final String leftButtonGpio,
                   final String rightButtonGpio) throws IOException {
        leftButton = setupGpio(peripheralManagerService, leftButtonGpio);
        rightButton = setupGpio(peripheralManagerService, rightButtonGpio);
    }

    private Gpio setupGpio(final PeripheralManagerService peripheralManagerService,
                           final String gpioName) throws IOException {
        final Gpio gpio = peripheralManagerService.openGpio(gpioName);
        gpio.setDirection(Gpio.DIRECTION_IN);
        gpio.setEdgeTriggerType(Gpio.EDGE_RISING);
        return gpio;
    }

    public void registerCallback(final Gpio gpio, final GpioCallback gpioCallback) {
        try {
            gpio.registerGpioCallback(gpioCallback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void unregisterCallback(final Gpio gpio, final GpioCallback gpioCallback) {
        gpio.unregisterGpioCallback(gpioCallback);
    }
}
