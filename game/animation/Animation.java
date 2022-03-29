package game.animation;

import java.util.ArrayList;
import java.util.List;

public class Animation {
    private List<Integer> frames;
    private int frame;
    private int ticksPerFrame;
    private boolean loops;

    private int ticksSinceLastFrame;

    public Animation(int ticksPerFrame, boolean loops) {
        frames = new ArrayList<>();
        frame = 0;

        this.ticksPerFrame = ticksPerFrame;
        this.loops = loops;

        ticksSinceLastFrame = 0;
    }

    private void nextFrame() {
        frame++;

        if (frame >= frames.size()) {
            frame = 0;
        }
    }

    public int update() {
        ticksSinceLastFrame++;

        if (ticksSinceLastFrame >= ticksPerFrame) {
            if (loops || frame < frames.size() - 1) {
                nextFrame();
                ticksSinceLastFrame = 0;
            }
        }

        return frames.get(frame);
    }

    public void reset() {
        frame = 0;
        ticksSinceLastFrame = 0;
    }

    public int getFrame() {
        return frames.get(frame);
    }

    public boolean loops() {
        return loops;
    }

    public boolean isDone() {
        return frame == frames.size() - 1 || loops;
    }

    public void addFrame(int f) {
        frames.add(f);
    }

    public void addFrames(int... f) {
        for (int i = 0; i < f.length; i++) {
            frames.add(f[i]);
        }
    }
}