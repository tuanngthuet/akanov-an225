package controller.sound_control;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SoundEffectManager {
    private static final ExecutorService audioPool = Executors.newFixedThreadPool(5);
    private float soundVolume = 1.0f;

    private final List<Clip> activeClips = Collections.synchronizedList(new ArrayList<>());

    public void playSound(String soundFile) {
        audioPool.submit(() -> {
            try {
                Clip clip = AudioSystem.getClip();
                AudioInputStream audio = AudioSystem.getAudioInputStream(
                        SoundEffectManager.class.getResource(soundFile));
                clip.open(audio);

                if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    float dB;
                    if (soundVolume == 0f) {
                        dB = gain.getMinimum();
                    } else {
                        dB = (float) (20.0 * Math.log10(soundVolume));
                        dB = Math.max(dB, gain.getMinimum());
                    }
                    gain.setValue(dB);
                }

                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void shutdown() {
        audioPool.shutdown();
    }

    public void setVolume(float volume) {
        soundVolume = Math.max(0f,(Math.min(1f,volume)));
    }

    public float getVolume() {
        return soundVolume;
    }
}
