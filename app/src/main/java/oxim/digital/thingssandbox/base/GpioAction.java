package oxim.digital.thingssandbox.base;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;

import rx.functions.Action1;

public final class GpioAction extends GpioCallback {

    private final Action1<Gpio> gpioAction;

    public GpioAction(final Action1<Gpio> gpioAction) {
        this.gpioAction = gpioAction;
    }

    @Override
    public boolean onGpioEdge(final Gpio gpio) {
        gpioAction.call(gpio);
        return true;
    }
}
