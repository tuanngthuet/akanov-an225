package controller.sound_control;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class MusicManager {
    private Clip currentClip;
    private float volume = 1.0f;

    public float getVolume() {
        return volume;
    }

    public void playSound(String musicfile, boolean loop) {
        stop();
        new Thread(() -> {
            try {
                AudioInputStream audio = AudioSystem.getAudioInputStream(
                        MusicManager.class.getResource(musicfile)
                );
                currentClip = AudioSystem.getClip();
                currentClip.open(audio);

                if (currentClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl gain = (FloatControl) currentClip.getControl(FloatControl.Type.MASTER_GAIN);
                    float min = gain.getMinimum();
                    float max = gain.getMaximum();
                    float dB = min + (max - min) * volume;
                    gain.setValue(dB);
                }

                if(loop) currentClip.loop(Clip.LOOP_CONTINUOUSLY);

                currentClip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
                ).start();
    }

    public void stop() {
        if(currentClip != null) {
            currentClip.stop();
            currentClip.close();
        }
    }

    public void setVolume(float v) {
        volume = Math.max(0f, Math.min(v,1f));
        if(currentClip != null && currentClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gain = (FloatControl) currentClip.getControl(FloatControl.Type.MASTER_GAIN);
            float min = gain.getMinimum();
            float max = gain.getMaximum();
            float dB = min + (max - min) * volume;
            gain.setValue(dB);
        }
    }
}
