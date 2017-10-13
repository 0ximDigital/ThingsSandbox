package oxim.digital.thingssandbox.piano;

import android.util.SparseBooleanArray;

import com.annimon.stream.Stream;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import oxim.digital.thingssandbox.GPIOS;

public final class BinaryPiano implements Key.OnKeyInteractionListener {

    // Silence, C, D, E, F, G, A, B
    private static final int[] NOTES = {2, 3830, 3400, 3038, 2864, 2550, 2272, 2028};

    private final Speaker speaker;
    private final List<Key> keys;

    private SparseBooleanArray activeKeys = new SparseBooleanArray(3);

    public BinaryPiano(final Speaker speaker, final PeripheralManagerService peripheralManagerService) {
        this.speaker = speaker;
        this.keys = initializeKeys(peripheralManagerService);
    }

    private List<Key> initializeKeys(final PeripheralManagerService peripheralManagerService) {
        List<Key> keys = Collections.emptyList();
        try {
            keys = Arrays.asList(
                    new Key(GPIOS.BCM22, peripheralManagerService, Key.ONE),
                    new Key(GPIOS.BCM27, peripheralManagerService, Key.TWO),
                    new Key(GPIOS.BCM17, peripheralManagerService, Key.FOUR));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stream.of(keys)
              .forEach(key -> key.setKeyInteractionListener(this));
        return keys;
    }

    @Override
    public void onKeyDown(@Key.KeyValue final int keyValue) {
        activeKeys.append(keyValue, true);
        changeTone();
    }

    @Override
    public void onKeyUp(@Key.KeyValue final int keyValue) {
        activeKeys.delete(keyValue);
        changeTone();
    }

    private void changeTone() {
        speaker.play(NOTES[getActiveTone()]);
    }

    private int getActiveTone() {
        int sum = 0;
        for (int i = 0, to = activeKeys.size(); i < to; i++) {
            sum += activeKeys.keyAt(i);
        }
        return sum;
    }
}
