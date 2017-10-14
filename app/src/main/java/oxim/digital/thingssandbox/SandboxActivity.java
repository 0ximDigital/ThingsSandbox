package oxim.digital.thingssandbox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

import oxim.digital.thingssandbox.light.LightTrigger;
import oxim.digital.thingssandbox.piano.BinaryPiano;
import oxim.digital.thingssandbox.piano.Speaker;

public class SandboxActivity extends AppCompatActivity {

    private final PeripheralManagerService peripheralService = new PeripheralManagerService();

    private Speaker speaker;
    private BinaryPiano piano;

    private LightTrigger lightTrigger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandbox);

        speaker = new Speaker(GPIOS.PWM_BCM_13);
        piano = new BinaryPiano(speaker, peripheralService);

        setupLightTrigger();
    }

    private void setupLightTrigger() {
        try {
            lightTrigger = new LightTrigger(GPIOS.BCM4, GPIOS.BCM5, peripheralService);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        speaker.close();
        super.onDestroy();
    }
}
