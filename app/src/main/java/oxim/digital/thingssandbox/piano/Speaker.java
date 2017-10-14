package oxim.digital.thingssandbox.piano;

/*
 * Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.support.annotation.FloatRange;

import com.google.android.things.pio.PeripheralManagerService;
import com.google.android.things.pio.Pwm;

import java.io.IOException;

public final class Speaker implements AutoCloseable {

    private static final double SQUARE_WAVE = 50.0;

    private Pwm pwm;

    /**
     * Create a Speaker connected to the given PWM pin name
     */
    public Speaker(final String speakerPin) {
        final PeripheralManagerService pioService = new PeripheralManagerService();
        try {
            pwm = pioService.openPwm(speakerPin);
            pwm.setPwmDutyCycle(SQUARE_WAVE);
        } catch (final IOException | RuntimeException e) {
            try {
                close();
            } catch (final RuntimeException ignored) {
            }
        }
    }

    @Override
    public void close() {
        if (pwm != null) {
            try {
                pwm.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                pwm = null;
            }
        }
    }

    /**
     * Play the specified frequency. Play continues until {@link #stop()} is called.
     *
     * @param frequency the frequency to play in Hz
     */
    public void play(double frequency) {
        try {
            pwm.setPwmFrequencyHz(frequency);
            pwm.setEnabled(true);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stop a currently playing frequency
     */
    public void stop() {
        try {
            pwm.setEnabled(false);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
