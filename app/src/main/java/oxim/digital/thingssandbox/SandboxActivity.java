package oxim.digital.thingssandbox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.things.pio.PeripheralManagerService;

import oxim.digital.thingssandbox.piano.BinaryPiano;
import oxim.digital.thingssandbox.piano.Speaker;

public class SandboxActivity extends AppCompatActivity {

    private final PeripheralManagerService peripheralService = new PeripheralManagerService();

    private Speaker speaker;
    private BinaryPiano piano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandbox);

        speaker = new Speaker(GPIOS.PWM_BCM_13);
        piano = new BinaryPiano(speaker, peripheralService);
    }

    @Override
    protected void onDestroy() {
        speaker.close();
        super.onDestroy();
    }
}
